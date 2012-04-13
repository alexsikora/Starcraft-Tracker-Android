package illinois.sweng.sctracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TeamStatusActivity extends ListActivity implements DelegateActivity{
	static String TAG = "teamStatusActivity";

	DBAdapter mDBAdapter;
	Cursor mPlayerCursor;
	String name = "";
	String teamTag = "";
	long pk = -1;
	int rowID = -1;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teamstatus);
		
		getDataFromIntent();
		
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new PlayerListClickListener());
		
		mDBAdapter = new DBAdapter(this);
		mDBAdapter.open();
		
		

			mPlayerCursor = mDBAdapter.getPlayersByTeam(rowID);
			
			TextView t = (TextView)findViewById(R.id.textView1);
			t.append("Team Name: " + name);
			t = (TextView)findViewById(R.id.textView2);
			t.append("Tag: " + teamTag);
			
			startManagingCursor(mPlayerCursor);
			String fields[] = 	{
					TrackerDatabaseAdapter.KEY_HANDLE,
					TrackerDatabaseAdapter.KEY_RACE,
					TrackerDatabaseAdapter.KEY_ROWID,
//					TrackerDatabaseAdapter.KEY_PK,
					TrackerDatabaseAdapter.KEY_PICTURE,
					TrackerDatabaseAdapter.KEY_NAME,
					TrackerDatabaseAdapter.KEY_TEAM,
					TrackerDatabaseAdapter.KEY_NATIONALITY,
					TrackerDatabaseAdapter.KEY_ELO
								};
			int textViews[] = {R.id.playerListName, R.id.playerListRace};
			
			CursorAdapter cursorAdapter = new SimpleCursorAdapter(this, 
					R.layout.playerlistrow, mPlayerCursor, fields, textViews);
			
			setListAdapter(cursorAdapter);
			
			CheckBox favorite = (CheckBox) findViewById(R.id.checkBox1);
			favorite.setChecked(isFavorite(pk));
			favorite.setOnCheckedChangeListener(new FavoriteCheckboxClickHandler());

	}
	/**
	 * Checks whether the current team is a favorite and returns true or false
	 * @param pk - server's unique code for team
	 * @return true if a favorite, false otherwise
	 */
	private boolean isFavorite(long pk) {
		String prefsName = getResources().getString(R.string.favoriteSharedPrefs);
		SharedPreferences prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
		JSONArray def = new JSONArray();
		
		try {
			String teamsKey = getResources().getString(R.string.favoriteTeamKey);
			JSONArray favorites = new JSONArray(prefs.getString(teamsKey, def.toString()));
			for(int i = 0; i < favorites.length(); i++) {
				if(pk == favorites.getLong(i)) {
					return true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d(TAG, "Error reading favorite teams");
		}
		
		return false;
	}
	
	/**
	 * Sends a request to server to get the favorites list and updates it in 
	 * shared preferences
	 */
	private void getFavoritesList() {
		String prefsFile = getResources().getString(R.string.preferencesFilename);
		SharedPreferences prefs = getSharedPreferences(prefsFile, 0);
		String key = getResources().getString(R.string.preferencesUserpass);
		String userpass = prefs.getString(key, "");
		
		ServerCommunicator comm = new ServerCommunicator(this, TAG);
		comm.sendGetAllFavoritesRequest(userpass);
	}
	
	/**
	 * Pulls data in from calling activity
	 */
	private void getDataFromIntent(){
		Intent intent = getIntent();
		Resources res = getResources();
		
		String tagKey = res.getString(R.string.keyTag);
		teamTag = intent.getStringExtra(tagKey);
		
		String nameKey = res.getString(R.string.keyName);
		name = intent.getStringExtra(nameKey);
		
		String rowIDKey = res.getString(R.string.keyRowID);
		rowID = intent.getIntExtra(rowIDKey, -1);
		
		String pkKey = res.getString(R.string.keyPK);
		pk = intent.getLongExtra(pkKey, -1);
		
	}
	
	/**
	 * Opens a player status activity
	 * @param i is the Intent class
	 */
	private void showPlayerStatus(Intent i) {
		i.setClass(this, PlayerStatusActivity.class);
		startActivity(i);
	}
		

	private class PlayerListClickListener implements AdapterView.OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			
			Intent i = new Intent();
			Resources res = getResources();
			
			String rowKey = res.getString(R.string.keyRowID);
			putIntExtra(rowKey, i);
			
			String pkKey = res.getString(R.string.keyPK);
			putLongExtra(pkKey, i);
			
			String pictureKey = res.getString(R.string.keyPicture);
			putStringExtra(pictureKey, i);
			
			String handleKey = res.getString(R.string.keyHandle);
			putStringExtra(handleKey, i);
			
			String nameKey = res.getString(R.string.keyName);
			putStringExtra(nameKey, i);
			
			String raceKey = res.getString(R.string.keyRace);
			putStringExtra(raceKey, i);
			
			String teamKey = res.getString(R.string.keyTeam);
			int teamIndex = mPlayerCursor.getColumnIndexOrThrow(teamKey);
			int team = mPlayerCursor.getInt(teamIndex);
			
			Cursor teamCursor = mDBAdapter.getTeamByPK(team);
			teamCursor.moveToFirst();
			int teamNameIndex = teamCursor.getColumnIndexOrThrow(DBAdapter.KEY_NAME);
			Log.d(TAG, teamNameIndex + "");
			String teamName = teamCursor.getString(teamNameIndex);
			teamCursor.close();
			
			i.putExtra(teamKey, teamName);
			
			String nationalityKey = res.getString(R.string.keyNationality);
			putStringExtra(nationalityKey, i);
			
			String eloKey = res.getString(R.string.keyELO);
			putIntExtra(eloKey, i);
			
			showPlayerStatus(i);
		}
		
		/**
		 * packs a int
		 * @param key
		 * @param i
		 */
		private void putIntExtra(String key, Intent i) {
			int index = mPlayerCursor.getColumnIndexOrThrow(key);
			int rowID = mPlayerCursor.getInt(index);
			i.putExtra(key, rowID);
		}
		
		/**
		 * packs a string
		 * @param key
		 * @param i
		 */
		private void putStringExtra(String key, Intent i) {
			int index = mPlayerCursor.getColumnIndexOrThrow(key);
			String name = mPlayerCursor.getString(index);
			i.putExtra(key, name);
		}
		
		/**
		 * packs a long
		 * @param key
		 * @param i
		 */
		private void putLongExtra(String key, Intent i) {
			int index = mPlayerCursor.getColumnIndexOrThrow(key);
			long pk = mPlayerCursor.getLong(index);
			i.putExtra(key, pk);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mDBAdapter.close();
	}

	private class FavoriteCheckboxClickHandler implements CompoundButton.OnCheckedChangeListener {
		public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
			sendFavoriteRequest(isChecked);
		}
	}
	
	/**
	 * send favorite or unfavorite request to server based on whether the checkbox is
	 * checked or unchecked
	 * @param isChecked
	 */
	public void sendFavoriteRequest(boolean isChecked) {
		String prefsFile = getResources().getString(R.string.preferencesFilename);
		SharedPreferences prefs = getSharedPreferences(prefsFile, 0);
		
		String key = getResources().getString(R.string.preferencesUserpass);
		
		String userpass = prefs.getString(key, "");
		ServerCommunicator com = new ServerCommunicator(this, TAG);
		if (isChecked) {
			com.sendFavoriteTeamRequest(userpass, pk + "");
		} else {
			com.sendUnfavoriteTeamRequest(userpass, pk + "");
		}
		getFavoritesList();
	}

	public void handleServerError(String message) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Receives favorites list from server, processes and updates shared prefs
	 */
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

	public void handleServerResponseMessage(String message) {
		// TODO Auto-generated method stub
		
	}
	
}