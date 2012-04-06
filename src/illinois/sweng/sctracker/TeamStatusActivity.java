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
import android.widget.TextView;

public class TeamStatusActivity extends ListActivity {
	static String TAG = "teamStatusActivity";

	DBAdapter mDBAdapter;
	Cursor mPlayerCursor;
//>>>>>>> e3328afa3e04efdd01896a07062054d285ef7468
	String name = "";
	String teamTag = "";
	int rowID = -1;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teamstatus);
		
		getDataFromIntent();
		
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
//<<<<<<< HEAD
//		listView.setOnItemClickListener(new TeamStatusClickListener());
//=======
		//TODO: Setup listener
		listView.setOnItemClickListener(new PlayerListClickListener());
//>>>>>>> e3328afa3e04efdd01896a07062054d285ef7468
		
		mDBAdapter = new DBAdapter(this);
		mDBAdapter.open();
		
		

			mPlayerCursor = mDBAdapter.getPlayersByTeam(rowID);
			
			TextView t = (TextView)findViewById(R.id.textView1);
			t.append("Team Name: " + name);
			t = (TextView)findViewById(R.id.textView2);
			t.append("Tag: " + teamTag);
			
			startManagingCursor(mPlayerCursor);
			String fields[] = 	{
					TrackerDatabaseAdapter.KEY_HANDLE,
					TrackerDatabaseAdapter.KEY_RACE,
					TrackerDatabaseAdapter.KEY_ROWID,
//					TrackerDatabaseAdapter.KEY_PK,
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
	
	private void getDataFromIntent(){
		Intent intent = getIntent();
		Resources res = getResources();
		
		String tagKey = res.getString(R.string.keyTag);
		teamTag = intent.getStringExtra(tagKey);
		
		String nameKey = res.getString(R.string.keyName);
		name = intent.getStringExtra(nameKey);
		
		String rowIDKey = res.getString(R.string.keyRowID);
		rowID = intent.getIntExtra(rowIDKey, -1);
		
	}
	

	private void showPlayerStatus(Intent i) {
		i.setClass(this, PlayerStatusActivity.class);
		startActivity(i);
	}
	
/*<<<<<<< HEAD
	private class TeamStatusClickListener implements AdapterView.OnItemClickListener {
		
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Log.d(TAG, "Entering player onclick");
			players.moveToPosition(position);
			Log.d(TAG, "after movetoposition");
			Intent i = new Intent();
			Log.d(TAG, "after declaring intent");
			Resources res = getResources();
			
			Log.d(TAG, "before rowkey");
			String rowKey = res.getString(R.string.keyRowID);
			putIntExtra(rowKey, i);
			
//			String pkKey = res.getString(R.string.keyPK);
//			putLongExtra(pkKey, i);
			
			Log.d(TAG, "before picKey");
			String pictureKey = res.getString(R.string.keyPicture);
			putStringExtra(pictureKey, i);
			
			Log.d(TAG, "before handlekey");
			String handleKey = res.getString(R.string.keyHandle);
			putStringExtra(handleKey, i);
			
			Log.d(TAG, "before namekey");
			String nameKey = res.getString(R.string.keyName);
			putStringExtra(nameKey, i);
			
			Log.d(TAG, "before racekey");
			String raceKey = res.getString(R.string.keyRace);
			putStringExtra(raceKey, i);
			
			Log.d(TAG, "before teamkey");
			String teamKey = res.getString(R.string.keyTeam);
			int teamIndex = players.getColumnIndexOrThrow(teamKey);
			int team = players.getInt(teamIndex);
			// TODO look up the team, not just send the team ID
			i.putExtra(teamKey, team + "");
			
			Log.d(TAG, "before nationalitykey");
			String nationalityKey = res.getString(R.string.keyNationality);
			putStringExtra(nationalityKey, i);
			
			Log.d(TAG, "before elokey");
			String eloKey = res.getString(R.string.keyELO);
			putIntExtra(eloKey, i);
			
			Log.d(TAG, "Exiting onclick, going into player status");
			showPlayerStatus(i);
		}*/
		

	private class PlayerListClickListener implements AdapterView.OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			
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
			
			Cursor teamCursor = mDBAdapter.getTeamByPK(team);
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
		mDBAdapter.close();
	}

}