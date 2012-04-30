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
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playermatches);
		
		String key = getResources().getString(R.string.preferencesUserpass);
		SharedPreferences preferences = getSharedPreferences(PREFS_FILE, 0);
		String userpass = preferences.getString(key, "");
		
		String matchID = getIntent().getExtras().getString("match_id");
		if(matchID == null) {
			Log.d("WHY", "WHY");
		} else {
			Log.d("match id", matchID);
		}
		for(String name : getIntent().getExtras().keySet()) {
			Log.d("check check", name);
			Log.d("check check", name + " " + getIntent().getExtras().getString(name));
		}
		ServerCommunicator comm = new ServerCommunicator(this, TAG);
		comm.sendGetMatchRequest(userpass, matchID);
	}

	public void handleServerError(String message) {
		Toast errorToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
		errorToast.show();
		Log.e(TAG, message);
	}

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

	public void handleServerResponseMessage(String message) {
		Log.d(TAG, "Got message from server");
	}

}
