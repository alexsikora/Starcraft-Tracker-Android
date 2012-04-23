package illinois.sweng.sctracker;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GamesAdapter extends ArrayAdapter<JSONObject>{
	private static String TAG = "GamesAdapter";
	
	private DBAdapter mDatabaseAdapter;
	private Cursor mPlayerCursor;
	
	public GamesAdapter(Activity a, List<JSONObject> games){
		super(a, 0, games);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		Activity activity = (Activity) getContext();
		
		mDatabaseAdapter = new DBAdapter(getContext());
		mDatabaseAdapter.open();
		
		LayoutInflater inflater = activity.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.games_row, null);
		
		TextView gameNumber = (TextView) rowView.findViewById(R.id.gameNumber);
		TextView winner = (TextView) rowView.findViewById(R.id.winner);
		TextView description = (TextView) rowView.findViewById(R.id.description);
		TextView map = (TextView) rowView.findViewById(R.id.map);
		
		JSONObject game = getItem(position);
		try {
			int pk = game.getInt("winner");
			mPlayerCursor = mDatabaseAdapter.getPlayerByPK(pk);
			mPlayerCursor.moveToFirst();
			int index = mPlayerCursor.getColumnIndex("handle");
			
			String handle = mPlayerCursor.getString(index);
			String desc = game.getString("description");
			String map_name = game.getJSONObject("map").getString("name");
			
			gameNumber.setText("Game " + (position+1));
			winner.setText("Winner: " + handle);
			description.setText("Description: " + desc);
			map.setText("Map: " + map_name);
		} 
		
		catch (JSONException e) {
			Log.e(TAG, "Error: " + e.toString());
			e.printStackTrace();
		}
		
		return rowView;
	}
}
