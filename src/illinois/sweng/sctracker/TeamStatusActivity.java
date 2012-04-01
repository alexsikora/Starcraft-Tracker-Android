package illinois.sweng.sctracker;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TeamStatusActivity extends ListActivity{
	static String TAG = "teamStatusActivity";
	DBAdapter mDBAdapter;
	
	String name = "";
	String teamTag = "";
	//player list of some sort?
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		//TODO: Setup listener
		
		mDBAdapter = new DBAdapter(this);
		mDBAdapter.open();
		Cursor team = mDBAdapter.getTeam(1); //TODO: Find way to pull in rowID

		if(team.moveToFirst()){
			name = team.getString(2);
			teamTag = team.getString(3);
			Cursor players = mDBAdapter.getPlayersByTeam(name);
			
			TextView t = (TextView)findViewById(R.id.textView1);
			t.append(name);
			t = (TextView)findViewById(R.id.textView2);
			t.append(teamTag);
			
			startManagingCursor(players);
			String fields[] = 	{
					DBAdapter.KEY_NAME, 
					DBAdapter.KEY_RACE
				};
			int textViews[] = {R.id.playerListName, R.id.playerListRace};
			
			CursorAdapter cursorAdapter = new SimpleCursorAdapter(this, 
					R.layout.teamlistrow, players, fields, textViews);
			//TODO: make layout for this
			
			setListAdapter(cursorAdapter);
			
			
		} else{
			Log.d("TAG", "OH WE GONE DONE FUCKED UP THE CURSOR");
		}
		
		mDBAdapter.close();
	}

}