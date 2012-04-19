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
import android.view.View;
import android.widget.ListView;

public class Rounds extends ListActivity {
	private final String TAG = "Rounds";
	List<JSONObject> rounds;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rounds);
		
		try{
			rounds = new ArrayList<JSONObject>();
			rounds = getDataFromIntent();
			
			RoundsAdapter adapter = new RoundsAdapter(this, rounds);
			setListAdapter(adapter);
		}
		
		catch(Exception e){
			Log.e(TAG, "Error: " + e.toString());
			e.printStackTrace();
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		
		try {
			Intent i = new Intent();
			i.setClass(this, PlayerMatches.class);
			String playerMatches = rounds.get(position).getJSONArray("player_matches").toString();
			i.putExtra("data", playerMatches);
			startActivity(i);
		} 
		
		catch (JSONException e) {
			Log.e(TAG, "Error: " + e.toString());
			e.printStackTrace();
		}
	}
	
	private List<JSONObject> getDataFromIntent() throws JSONException{
		Intent intent = getIntent();
		
		String data = intent.getStringExtra("data");
		JSONArray arr = new JSONArray(data);
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(int i=0; i < arr.length(); i++){
			list.add(arr.getJSONObject(i));
		}
		
		return list;
	}
}
