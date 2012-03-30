package illinois.sweng.sctracker;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class PlayerListActivity extends ListActivity {

	private TrackerDatabaseAdapter mDatabaseAdapter;
	private Cursor mPlayerCursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new PlayerListClickListener());
		
		mDatabaseAdapter = new TrackerDatabaseAdapter(this);
		mDatabaseAdapter.open();
		
		mPlayerCursor = mDatabaseAdapter.getAllPlayers();
		startManagingCursor(mPlayerCursor);
		
		String fields[] = 	{
								TrackerDatabaseAdapter.KEY_NAME, 
								TrackerDatabaseAdapter.KEY_RACE
							};
		int textViews[] = {R.id.playerListName, R.id.playerListRace};
		
		CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
				R.layout.playerlistrow, mPlayerCursor, fields, textViews);
		
		setListAdapter(cursorAdapter);
	}
	
	private class PlayerListClickListener implements AdapterView.OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO move cursor to this position and send data to player status activity
			// pack data into an intent to send
			mPlayerCursor.moveToPosition(position);
			
			
		}
		
	}
}
