package illinois.sweng.sctracker;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class PlayerListActivity extends ListActivity {

	private TrackerDatabaseAdapter mDatabaseAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnClickListener(new PlayerListClickListener());
		
		mDatabaseAdapter = new TrackerDatabaseAdapter(this);
		mDatabaseAdapter.open();
		Cursor playerCursor = mDatabaseAdapter.getAllPlayers();
		
		String fields[] = 	{
								TrackerDatabaseAdapter.KEY_NAME, 
								TrackerDatabaseAdapter.KEY_RACE
							};
		int textViews[] = {R.id.playerListName, R.id.playerListRace};
		
		CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
				R.layout.playerlistrow, playerCursor, fields, textViews);
		
		setListAdapter(cursorAdapter);
	}
	
	private class PlayerListClickListener implements View.OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
