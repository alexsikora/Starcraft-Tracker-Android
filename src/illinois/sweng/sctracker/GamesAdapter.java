package illinois.sweng.sctracker;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GamesAdapter extends ArrayAdapter<JSONObject>{
	private static String TAG = "GamesAdapter";
	
	public GamesAdapter(Activity a, List<JSONObject> games){
		super(a, 0, games);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		Activity activity = (Activity) getContext();
		
		LayoutInflater inflater = activity.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.games_row, null);
		
		TextView winner = (TextView) rowView.findViewById(R.id.winner);
		TextView description = (TextView) rowView.findViewById(R.id.description);
		TextView map = (TextView) rowView.findViewById(R.id.map);
		
		JSONObject game = getItem(position);
		try {
			winner.setText(game.getString("handle"));
			description.setText(game.getString("description"));
			map.setText(game.getString("map"));
		} 
		
		catch (JSONException e) {
			Log.e(TAG, "Error: " + e.toString());
		}
		
		return rowView;
	}
}
