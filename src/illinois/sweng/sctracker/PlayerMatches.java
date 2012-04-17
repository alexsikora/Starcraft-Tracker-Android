package illinois.sweng.sctracker;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;


public class PlayerMatches extends ListActivity{
	private final String TAG = "PlayerMatches";
	private List<JSONObject> matches;
	private DBAdapter mDatabaseAdapter;
	private Cursor mPlayerCursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playermatches);
		
		mDatabaseAdapter = new DBAdapter(this);
		mDatabaseAdapter.open();
		
		try{
			matches = new ArrayList<JSONObject>();
			matches = getDataFromIntent(); 	
			
			PlayerMatchesAdapter adapter = new PlayerMatchesAdapter(this, matches);
			setListAdapter(adapter);
		}
		
		catch (Exception e){
			Log.e(TAG, "Error: " + e.toString());
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);

		try {
			Intent i = new Intent();
			i.setClass(this, Games.class);
			startActivity(i);
			i.putExtra("data", matches.get(position).getJSONArray("games").toString());
		} 
		
		catch (JSONException e) {
			Log.e(TAG, "Error: " + e.toString());
		}
		
	}
	
	private List<JSONObject> getDataFromIntent() throws JSONException{
		Intent intent = getIntent();
		
		String data = intent.getStringExtra("data");
		JSONArray arr = new JSONArray(data);
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(int i=0; i < arr.length(); i++){
			list.add(arr.getJSONObject(i));
			
			int pk1 = arr.getJSONObject(i).getInt("first_player");
			mPlayerCursor = mDatabaseAdapter.getPlayerByPK(pk1);
			mPlayerCursor.moveToFirst();
			int index = mPlayerCursor.getColumnIndex("handle");
			String handle = mPlayerCursor.getString(index);
			
			list.get(i).put("player1", handle);
			
			int pk2 = arr.getJSONObject(i).getInt("second_player");
			mPlayerCursor = mDatabaseAdapter.getPlayerByPK(pk2);
			mPlayerCursor.moveToFirst();
			index = mPlayerCursor.getColumnIndex("handle");
			handle = mPlayerCursor.getString(index);
			
			list.get(i).put("player2", handle);
		}
		
		return list;
	}
}
