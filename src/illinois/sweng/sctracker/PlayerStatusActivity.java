package illinois.sweng.sctracker;

import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerStatusActivity extends Activity implements DelegateActivity {
	private static String TAG = "playerStatusActivity";

	private String handle = "";
	private String picture = "";
	private String name = "";
	private String race = "";
	private String team = "";
	private String nationality = "";
	private String elo;
	private long pk;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playerstatus);

		getDataFromIntent();
		
		TextView t = (TextView) findViewById(R.id.playerStatusHandleTextView);
		t.append(handle);
		t = (TextView) findViewById(R.id.playerStatusNameTextView);
		t.append(name);
		t = (TextView) findViewById(R.id.playerStatusRaceTextView);
		t.append(race);
		t = (TextView) findViewById(R.id.playerStatusTeamTextView);
		t.append(team);
		t = (TextView) findViewById(R.id.playerStatusNationalityTextView);
		t.append(nationality);
		t = (TextView) findViewById(R.id.playerStatusEloTextView);
		t.append(elo);
		
		ImageView portraitView = (ImageView) findViewById(R.id.playerStatusPortrait);
		try {
		    URL thumb_u = new URL(picture);
		    Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
		    portraitView.setImageDrawable(thumb_d);
		}
		catch (Exception e) {
		    Log.d(TAG, "Error opening player portrait");
		}
		
		CheckBox favorite = (CheckBox) findViewById(R.id.playerStatusFavoriteCheckbox);
		favorite.setChecked(isFavorite(pk));
		favorite.setOnCheckedChangeListener(new FavoriteCheckboxClickHandler());
	}

	/**
	 * Checks if the currently displayed player is favorited by the current user
	 * @param pk pk of the player to check for
	 * @return boolean representing if the player is favorited or not
	 */
	private boolean isFavorite(long pk) {
		String prefsName = getResources().getString(R.string.favoriteSharedPrefs);
		SharedPreferences prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
		JSONArray def = new JSONArray();
		
		try {
			String playersKey = getResources().getString(R.string.favoritePlayerKey);
			JSONArray favorites = new JSONArray(prefs.getString(playersKey, def.toString()));
			for(int i = 0; i < favorites.length(); i++) {
				if(pk == favorites.getLong(i)) {
					return true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d(TAG, "Error reading favorite players");
		}
		
		return false;
	}

	/**
	 * Retrieve field data from the intent used to start this activity
	 */
	private void getDataFromIntent() {
		Intent intent = getIntent();
		Resources res = getResources();
		
		String handleKey = res.getString(R.string.keyHandle);
		handle = intent.getStringExtra(handleKey);
		
		String pictureKey = res.getString(R.string.keyPicture);
		String baseUrl = "http://" + res.getString(R.string.serverURL) + "/media/";
		picture = baseUrl + intent.getStringExtra(pictureKey);
		
		String nameKey = res.getString(R.string.keyName);
		name = intent.getStringExtra(nameKey);
		
		String raceKey = res.getString(R.string.keyRace);
		race = intent.getStringExtra(raceKey);
		
		String teamKey = res.getString(R.string.keyTeam);
		team = intent.getStringExtra(teamKey);
		
		String nationalityKey = res.getString(R.string.keyNationality);
		nationality = intent.getStringExtra(nationalityKey);
		
		String eloKey = res.getString(R.string.keyELO);
		elo = intent.getIntExtra(eloKey, 0) + "";
		
		String pkKey = res.getString(R.string.keyPK);
		pk = intent.getLongExtra(pkKey, -1);
	}

	public void sendFavoriteRequest(boolean isChecked) {
		String prefsFile = getResources().getString(R.string.preferencesFilename);
		SharedPreferences prefs = getSharedPreferences(prefsFile, 0);
		
		String key = getResources().getString(R.string.preferencesUserpass);
		String userpass = prefs.getString(key, "");		
		
		ServerCommunicator com = new ServerCommunicator(this, TAG);
		
		if (isChecked) {
			com.sendFavoritePlayerRequest(userpass, pk + "");
		} else {
			com.sendUnfavoritePlayerRequest(userpass, pk + "");
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


	public void handleServerResponseData(JSONArray values) {
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

	public void handleServerError(String message) {
		// TODO Auto-generated method stub
		
	}
	
	public void handleServerResponseMessage(String message) {
		// TODO Auto-generated method stub
		
	}

}
