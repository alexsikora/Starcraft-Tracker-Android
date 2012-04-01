package illinois.sweng.sctracker;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class TeamListActivity extends ListActivity {
	static String TAG = "teamsActivity";

	private DBAdapter mDBAdapter;
	private Cursor mTeamCursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new TeamListClickListener());
		
		mDBAdapter = new DBAdapter(this);
		mDBAdapter.open();
		
		mTeamCursor = mDBAdapter.getAllTeams();
		startManagingCursor(mTeamCursor);
		
		String fields[] = 	{
								DBAdapter.KEY_NAME, 
								DBAdapter.KEY_TAG
							};
		int textViews[] = {R.id.teamListName, R.id.teamListTag};
		
		CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
				R.layout.teamlistrow, mTeamCursor, fields, textViews);
		
		setListAdapter(cursorAdapter);
	}
	
	private class TeamListClickListener implements AdapterView.OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO move cursor to this position and send data to player status activity
			// pack data into an intent to send
			mTeamCursor.moveToPosition(position);
			
			
		}
		
	}


}
