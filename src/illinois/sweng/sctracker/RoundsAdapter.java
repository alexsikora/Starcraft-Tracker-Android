package illinois.sweng.sctracker;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class RoundsAdapter extends ArrayAdapter<JSONObject> {
	private final String TAG = "RoundsAdapter";
	
	public RoundsAdapter(Activity a, List<JSONObject> rounds){
		super(a,0,rounds);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		Activity activity = (Activity) getContext();
		
		LayoutInflater inflater = activity.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.rounds_row, null);
		
		TextView roundName = (TextView) rowView.findViewById(R.id.roundName);
		JSONObject round = getItem(position);
		try{
			String name = round.getString("name");
			roundName.setText(name);
		}
		
		catch(JSONException e){
			Log.e(TAG, "Error: " + e.toString());
			e.printStackTrace();
		}
		
		return rowView;
	}
}

