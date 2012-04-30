package illinois.sweng.sctracker;

import org.json.JSONArray;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class PushDisplayActivity extends Activity implements DelegateActivity {
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
		ServerCommunicator comm = new ServerCommunicator(this, TAG);
		comm.sendGetMatchRequest(userpass, Long.parseLong(matchID));
	}

	public void handleServerError(String message) {
		// TODO Auto-generated method stub

	}

	public void handleServerResponseData(JSONArray values) {
		// TODO Auto-generated method stub

	}

	public void handleServerResponseMessage(String message) {
		// TODO Auto-generated method stub

	}

}
