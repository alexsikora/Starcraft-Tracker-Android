package illinois.sweng.sctracker;

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
	private DBAdapter mDBAdapter;
	private Cursor mEventCursor;
	
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
		
		int textViews[] = {R.id.eventListName, R.id.eventListStartDate, R.id.eventListEndDate};
		
		CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
				R.layout.eventlistrow, mEventCursor, fields, textViews);
		
		setListAdapter(cursorAdapter);
	}
	
	private class EventListClickListener implements AdapterView.OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO move cursor to this position and send data to player status activity
			// pack data into an intent to send
			mEventCursor.moveToPosition(position);
			Intent i = new Intent();
			Resources res = getResources();
			/*
			String rowKey = res.getString(R.string.keyRowID);
			putIntExtra(rowKey, i);
			
			String nameKey = res.getString(R.string.keyName);
			putStringExtra(keyName, i);
			
			String startDateKey = res.getString(R.string.keyStartDate);
			putStringExtra(keyStartDate, i);
			
			String endDateKey = res.getString(R.string.keyEndDate);
			putStringExtra(keyEndDate, i);
			*/
			// use Cursor get methods?
			// 
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mDBAdapter.close();
	}

}
