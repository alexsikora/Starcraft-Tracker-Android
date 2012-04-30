package illinois.sweng.sctracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class EventStatusActivity extends Activity implements DelegateActivity{
	static String TAG = "eventStatusActivity";

	long eventPK = -1;
	String name = "";
	String startdate = "";
	String enddate = "";

	private Button mRoundsButton;

	JSONObject eventJSON;
	JSONArray rounds;
	JSONArray finals;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventstatus);

		try {
			getDataFromIntent();

			TextView t = (TextView) findViewById(R.id.eventStatusNameTextView);
			t.append(" " + name);
			t = (TextView) findViewById(R.id.eventStatusStartDateTextView);
			t.append(" " + startdate);
			t = (TextView) findViewById(R.id.eventStatusEndDateTextView);
			t.append(" " + enddate);

			mRoundsButton = (Button) findViewById(R.id.eventStatusRoundsButton);
			mRoundsButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v){
					Log.d(TAG, "Rounds Button Clicked.");
					LaunchRounds();
				}
			});

		} catch (JSONException e) {
			Log.e(TAG, "Exception thrown while retrieving JSON event information: " + e.toString());
			e.printStackTrace();
		}

		CheckBox favorite = (CheckBox) findViewById(R.id.eventStatusFavoriteCheckbox);
		favorite.setChecked(isFavorite(eventPK));
		favorite.setOnCheckedChangeListener(new FavoriteCheckboxClickHandler());

	}

	/**
	 * Retrieve field data from the intent used to start this activity
	 */
	private void getDataFromIntent() throws JSONException{
		Intent intent = getIntent();
		Resources res = getResources();

		String pkKey = res.getString(R.string.keyPK); 
		eventPK = intent.getLongExtra(pkKey, -1);

		getEventInfo(eventPK);
	}

	/**
	 * Queries the server for a specific event as indicated by eventPK	
	 * @param eventPK pk for specific event
	 */
	private void getEventInfo(long eventPK){
		String prefsFile = getResources().getString(R.string.preferencesFilename);
		SharedPreferences prefs = getSharedPreferences(prefsFile, 0);
		String key = getResources().getString(R.string.preferencesUserpass);
		String userpass = prefs.getString(key,  "");

		ServerCommunicator comm = new ServerCommunicator(this, TAG);
		comm.sendGetEventRequest(userpass, eventPK);
	}

	/**
	 * Checks if the currently displayed event is favorited by the current user
	 * @param pk pk of the event to check for
	 * @return boolean representing if the event is favorited or not
	 */
	private boolean isFavorite(long pk) {
		String prefsName = getResources().getString(R.string.favoriteSharedPrefs);
		SharedPreferences prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
		JSONArray def = new JSONArray();

		try {
			String eventsKey = getResources().getString(R.string.favoriteEventKey);
			JSONArray favorites = new JSONArray(prefs.getString(eventsKey, def.toString()));
			for(int i = 0; i < favorites.length(); i++) {
				if(pk == favorites.getLong(i)) {
					return true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d(TAG, "Error reading favorite events");
		}

		return false;
	}

	/**
	 * Checks if the currently displayed event is currently checked to indicate if it's favorited
	 * @param isChecked representing if check box is checked or not.
	 */
	public void sendFavoriteRequest(boolean isChecked) {
		String prefsFile = getResources().getString(R.string.preferencesFilename);
		SharedPreferences prefs = getSharedPreferences(prefsFile, 0);

		String key = getResources().getString(R.string.preferencesUserpass);
		String userpass = prefs.getString(key, "");		

		ServerCommunicator com = new ServerCommunicator(this, TAG);

		if (isChecked) {
			com.sendFavoriteEventRequest(userpass, eventPK);
		} else {
			com.sendUnfavoriteEventRequest(userpass, eventPK);
		}
		getFavoritesList();
	}

	/**
	 * Request the list of this user's favorites from the server
	 */
	private void getFavoritesList() {
		String prefsFile = getResources().getString(R.string.preferencesFilename);
		SharedPreferences prefs = getSharedPreferences(prefsFile, 0);
		String key = getResources().getString(R.string.preferencesUserpass);
		String userpass = prefs.getString(key, "");

		ServerCommunicator comm = new ServerCommunicator(this, TAG);
		comm.sendGetAllFavoritesRequest(userpass);
	}

	private class FavoriteCheckboxClickHandler implements CompoundButton.OnCheckedChangeListener {
		public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
			sendFavoriteRequest(isChecked);
		}
	}

	/**
	 * Launches a new activity, Rounds.
	 */
	private void LaunchRounds(){
		Intent i = new Intent(this, Rounds.class);
		i.putExtra("data", rounds.toString());
		startActivity(i);
	}	

	public void handleServerError(String message) {
		// TODO Auto-generated method stub

	}

	public void handleServerResponseData(JSONArray values) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Receiving favorites data");

		String prefsName = getResources().getString(R.string.favoriteSharedPrefs);
		SharedPreferences prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();

		try {
			JSONObject firstEntry = (JSONObject) (values.get(0));

			String eventKey = getResources().getString(R.string.favoriteEventKey);
			JSONArray events = firstEntry.getJSONArray(eventKey);
			editor.putString(eventKey, events.toString());

			String teamKey = getResources().getString(R.string.favoriteTeamKey);
			JSONArray teams = firstEntry.getJSONArray(teamKey);
			editor.putString(teamKey, teams.toString());

			String playerKey = getResources().getString(R.string.favoritePlayerKey);
			JSONArray players = firstEntry.getJSONArray(playerKey);
			editor.putString(playerKey, players.toString());

			editor.commit();
		} catch (JSONException e) {
			Log.d(TAG, "There was an error reading the JSON returned from the server");
			e.printStackTrace();
		}

	}

	public void handleServerResponseMessage(String message) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Server Response message received: " + message);

		try{
			eventJSON = new JSONObject(message);
			rounds = eventJSON.optJSONArray("rounds");

			if(rounds != null){
				name = eventJSON.getString("name");
				startdate = eventJSON.getString("start_date");
				enddate = eventJSON.getString("end_date");
			}
		}

		catch(JSONException e){
			Log.e(TAG, "Error retrieving JSON string returned from server.");
			e.printStackTrace();
		}
	}
}



