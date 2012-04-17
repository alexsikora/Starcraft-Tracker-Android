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

public class PlayerMatchesAdapter extends ArrayAdapter<JSONObject>{
	private final String TAG = "PlayerMatchesAdapter";
	
	public PlayerMatchesAdapter(Activity a, List<JSONObject> matches){
		super(a, 0, matches);
	}
	
	@Override 
	public View getView(int position, View convertView, ViewGroup parent){
		Activity activity = (Activity) getContext();
		
		LayoutInflater inflater = activity.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.playermatches_row, null);
		
		TextView player1 = (TextView) rowView.findViewById(R.id.player1);
		TextView player2 = (TextView) rowView.findViewById(R.id.player2);
		
		JSONObject match = getItem(position);
		try {
			player1.setText(match.getString("player1") + " vs. ");
			player2.setText(match.getString("player2"));
		} 
		
		catch (JSONException e) {
			Log.e(TAG, "Error: " + e.toString());
		}
		
		return rowView;
	}

}
