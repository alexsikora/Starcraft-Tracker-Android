package illinois.sweng.sctracker;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Games extends ListActivity {
	private final String TAG = "Games";
	private List<JSONObject> games;
	
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
			e.printStackTrace();
		}
	}
	
	public List<JSONObject> getDataFromIntent() throws JSONException{
		Intent intent = getIntent();
		String games = intent.getStringExtra("data");
		Log.d(TAG, "String received: " + games);
		JSONArray arr = new JSONArray(games);
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(int i=0; i < arr.length(); i++){
			list.add(arr.getJSONObject(i));
		}
		
		return list;
	}
}
