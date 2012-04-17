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

public class Games extends ListActivity {
	private final String TAG = "Games";
	private List<JSONObject> games;
	private DBAdapter mDatabaseAdapter;
	private Cursor mPlayerCursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.games);
		
		try{
			games = new ArrayList<JSONObject>();
			games = getDataFromIntent(); 	
			
			GamesAdapter adapter = new GamesAdapter(this, games);
			setListAdapter(adapter);
		}
		
		catch (Exception e){
			Log.e(TAG, "Error: " + e.toString());
		}
	}
	
	public List<JSONObject> getDataFromIntent() throws JSONException{
		Intent intent = getIntent();
		String data = intent.getStringExtra("data");
		JSONArray arr = new JSONArray(data);
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(int i=0; i < arr.length(); i++){
			list.add(arr.getJSONObject(i));
			
			int pk = arr.getJSONObject(i).getInt("winner");
			mPlayerCursor = mDatabaseAdapter.getPlayerByPK(pk);
			mPlayerCursor.moveToFirst();
			int index = mPlayerCursor.getColumnIndex("handle");
			String handle = mPlayerCursor.getString(index);
			
			list.get(i).put("handle", handle);
		}
		
		return list;
	}
}
