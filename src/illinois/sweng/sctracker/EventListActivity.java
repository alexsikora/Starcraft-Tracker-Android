package illinois.sweng.sctracker;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class EventListActivity extends ListActivity{
	private EventDatabaseAdapter mEventDBAdapter;
	private Cursor mEventCursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new EventListClickListener());
		
		mEventDBAdapter = new EventDatabaseAdapter(this);
		mEventDBAdapter.open();
		
		mEventCursor = mEventDBAdapter.getAllEvents();
		startManagingCursor(mEventCursor);
		
		String fields[] = 	{
								EventDatabaseAdapter.KEY_NAME, 
								EventDatabaseAdapter.KEY_STARTDATE,
								EventDatabaseAdapter.KEY_ENDDATE
							};
		
		int textViews[] = {R.id.eventListName, R.id.eventListStartDate, R.id.eventListEndDate};
		
		CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
				R.layout.playerlistrow, mEventCursor, fields, textViews);
		
		setListAdapter(cursorAdapter);
	}
	
	private class EventListClickListener implements AdapterView.OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO move cursor to this position and send data to player status activity
			// pack data into an intent to send
			mEventCursor.moveToPosition(position);
		}
		
	}

}
