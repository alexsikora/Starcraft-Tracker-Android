package illinois.sweng.sctracker;

import java.net.URLEncoder;

import org.json.JSONObject;

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

public class EventListActivity extends ListActivity{
	private final String TAG = "EventListActivity";
	private DBAdapter mDBAdapter;
	private Cursor mEventCursor;
	private String url = "http://startrack.alexsikora.com/events/get_event/?id=";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new EventListClickListener());
		
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
	
	private void showEventStatus(Intent i){
		i.setClass(this, EventStatusActivity.class);
		startActivity(i);
	}
	
	private class EventListClickListener implements AdapterView.OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mEventCursor.moveToPosition(position);
			Intent i = new Intent();
			Resources res = getResources();
			String data;
			
			String pkKey = res.getString(R.string.keyPK);
			int index = mEventCursor.getColumnIndex(pkKey);
			String pk = mEventCursor.getString(index);
			
			// data should look like this:
			// {"status_code": 200, "response": {"pk": 1, "rounds": [{"pk": 1, "player_matches": [{"pk": 1, "first_player": 1, "games": [], "second_player": 2}], "name": "Round of 64", "team_matches": []}, {"pk": 2, "player_matches": [{"pk": 2, "first_player": 1, "games": [], "second_player": 2}], "name": "Finals", "team_matches": []}], "name": "MLG", "end_date": "2012-03-31", "start_date": "2012-03-31"}}
			
			try{
				data = ServerCommunicator.getEventInfo(url + URLEncoder.encode(String.valueOf(pk), "UTF-8"));
				i.putExtra("data", data);
				Log.d(TAG, "JSON: " + data);
			} catch(Exception e){
				Log.e(TAG, "Exception thrown while retrieving event information: " + e.toString());
			}
	
			showEventStatus(i);
			
		}
		
	}

}
