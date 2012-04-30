package illinois.sweng.sctracker;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class PushDisplayActivity extends ListActivity implements DelegateActivity {
	private static final String TAG = "PushDisplay";
	private static final String PREFS_FILE = "sc2prefs";
	
	/**
	 * Gets the match ID from the intent, and sends a request to the server
	 * to get information to display for that match. 
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playermatches);
		
		String key = getResources().getString(R.string.preferencesUserpass);
		SharedPreferences preferences = getSharedPreferences(PREFS_FILE, 0);
		String userpass = preferences.getString(key, "");
		
		String matchID = getIntent().getExtras().getString("match_id");
		ServerCommunicator comm = new ServerCommunicator(this, TAG);
		comm.sendGetMatchRequest(userpass, matchID);
	}

	/**
	 * Handles an error message returned from the server
	 */
	public void handleServerError(String message) {
		Toast errorToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
		errorToast.show();
		Log.e(TAG, message);
	}

	/**
	 * Receives an array of game data from the server, puts it in a list,
	 * and sets the list adapter for this activity to handle this list.
	 */
	public void handleServerResponseData(JSONArray values) {
		try {
			JSONObject match = values.getJSONObject(0);
			JSONArray games = match.getJSONArray("games");
			List<JSONObject> list = new ArrayList<JSONObject>();
			for(int i=0; i < games.length(); i++){
				list.add(games.getJSONObject(i));
			}
			GamesAdapter adapter = new GamesAdapter(this, list);
			setListAdapter(adapter);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Receives and logs a non-data message from the server. This should not
	 * occur under normal operation for this activity.
	 */
	public void handleServerResponseMessage(String message) {
		Log.d(TAG, "Got message from server");
	}

}
