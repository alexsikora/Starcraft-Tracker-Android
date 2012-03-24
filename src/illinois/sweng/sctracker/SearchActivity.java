package illinois.sweng.sctracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SimpleAdapter;

public class SearchActivity extends ListActivity implements DelegateActivity {

	ServerCommunicator mServerCommunicator;
	static final String PREFS_FILE = "sc2prefs";
	JSONArray mAllPlayers;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			
			searchPlayers(query);
			
		}

	}

	private void searchPlayers(String query) {
		String key = getResources().getString(R.string.preferencesUserpass);
		SharedPreferences preferences = getSharedPreferences(PREFS_FILE, 0);
		String userpass = preferences.getString(key, "");
		mServerCommunicator.sendGetAllPlayersRequest(userpass);
	}

	@Override
	public void handleServerError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleServerResponseData(JSONArray values) {
		mAllPlayers = values;
		
		int length = values.length();
		List<HashMap<String, String>> playerList = new ArrayList<HashMap<String, String>>();
//		for(int i = 0; i < length; i++) {
//			HashMap<String, String> player = new HashMap<String, String>();
//			player.put(name, values.))
//		}
//		
//		
//		
//		SimpleAdapter adapter = new SimpleAdapter(
//				this,
//				,
//				android.R.layout.simple_list_item_1,
//				new String[] {
//                new int[] {android.R.id.text1})
//				)
//		
		
		
	}

	@Override
	public void handleServerResponseMessage(String message) {
		// TODO Auto-generated method stub
		
	}

}
