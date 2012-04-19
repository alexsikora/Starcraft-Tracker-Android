package illinois.sweng.sctracker;

import org.json.JSONArray;

import android.app.ListActivity;
import android.content.Intent;
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
		
		//getFavoritesList();
		
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
	
	/*
	public void getFavoritesList(){
		String prefsFile = getResources().getString(R.string.preferencesFilename);
		SharedPreferences prefs = getSharedPreferences(prefsFile, 0);
		String key = getResources().getString(R.string.preferencesUserpass);
		String userpass = prefs.getString(key, "");
		
		ServerCommunicator comm = new ServerCommunicator(this, TAG);
		comm.sendGetAllEventsRequest(userpass);
	}
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
	}

	public void handleServerResponseMessage(String message) {
		// TODO Auto-generated method stub
		
	}


}
