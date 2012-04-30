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

public class EventListActivity extends ListActivity implements DelegateActivity{
	private final String TAG = "EventListActivity";
	
	private DBAdapter mDBAdapter;
	private Cursor mEventCursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(TAG, "EventListActivity was created");
		
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new EventListClickListener());
		
		getFavoritesList();
		
		mDBAdapter = new DBAdapter(this);
		mDBAdapter.open();
		
		mEventCursor = mDBAdapter.getAllEvents();
		startManagingCursor(mEventCursor);
		
		String fields[] = 	{
								DBAdapter.KEY_NAME,
								DBAdapter.KEY_STARTDATE,
								DBAdapter.KEY_ENDDATE,
								DBAdapter.KEY_PK,
								DBAdapter.KEY_ROWID
							};
		
		int textViews[] = 	{
								R.id.eventListName, 
								R.id.eventListStartDate, 
								R.id.eventListEndDate
							};
		
		CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
				R.layout.eventlistrow, mEventCursor, fields, textViews);
		
		setListAdapter(cursorAdapter);
	}
	
	/**
	 * Request the list of this user's favorites from the server
	 */
	public void getFavoritesList(){
		String prefsFile = getResources().getString(R.string.preferencesFilename);
		SharedPreferences prefs = getSharedPreferences(prefsFile, 0);
		String key = getResources().getString(R.string.preferencesUserpass);
		String userpass = prefs.getString(key, "");
		
		ServerCommunicator comm = new ServerCommunicator(this, TAG);
		comm.sendGetAllEventsRequest(userpass);
	}
	
	/**
	 * Launches the event status activity
	 * @param i
	 */
	private void showEventStatus(Intent i){
		i.setClass(this, EventStatusActivity.class);
		startActivity(i);
	}
	
	private class EventListClickListener implements AdapterView.OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mEventCursor.moveToPosition(position);
			Intent i = new Intent();
			Resources res = getResources();
			
			String pkKey = res.getString(R.string.keyPK);
			int index = mEventCursor.getColumnIndex(pkKey);
			long pk = mEventCursor.getLong(index);
			
			i.putExtra(pkKey, pk);
			showEventStatus(i);
		}
		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mDBAdapter.close();
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
		
	}


}
