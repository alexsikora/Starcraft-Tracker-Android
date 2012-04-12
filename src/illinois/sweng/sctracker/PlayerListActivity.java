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
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class PlayerListActivity extends ListActivity implements DelegateActivity {

	private static final String TAG = "PlayerListActivity";
	
	private DBAdapter mDatabaseAdapter;
	private Cursor mPlayerCursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(TAG, "PlayerListActivity view created");
		
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new PlayerListClickListener());
		
		getFavoritesList();
		
		mDatabaseAdapter = new DBAdapter(this);
		mDatabaseAdapter.open();
		
		mPlayerCursor = mDatabaseAdapter.getAllPlayers();
		startManagingCursor(mPlayerCursor);
		
		String fields[] = 	{
				TrackerDatabaseAdapter.KEY_HANDLE,
				TrackerDatabaseAdapter.KEY_RACE,
				TrackerDatabaseAdapter.KEY_ROWID,
				TrackerDatabaseAdapter.KEY_PK,
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

	private void showPlayerStatus(Intent i) {
		i.setClass(this, PlayerStatusActivity.class);
		startActivity(i);
	}
	
	private class PlayerListClickListener implements AdapterView.OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mPlayerCursor.moveToPosition(position);
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
			
			Cursor teamCursor = mDatabaseAdapter.getTeamByPK(team);
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
		
		private void putIntExtra(String key, Intent i) {
			int index = mPlayerCursor.getColumnIndexOrThrow(key);
			int rowID = mPlayerCursor.getInt(index);
			i.putExtra(key, rowID);
		}
		
		private void putStringExtra(String key, Intent i) {
			int index = mPlayerCursor.getColumnIndexOrThrow(key);
			String name = mPlayerCursor.getString(index);
			i.putExtra(key, name);
		}
		
		private void putLongExtra(String key, Intent i) {
			int index = mPlayerCursor.getColumnIndexOrThrow(key);
			long pk = mPlayerCursor.getLong(index);
			i.putExtra(key, pk);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mDatabaseAdapter.close();
	}

	public void handleServerError(String message) {
		// TODO Auto-generated method stub
		
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

	public void handleServerResponseMessage(String message) {
		// TODO Auto-generated method stub
		
	}
}
