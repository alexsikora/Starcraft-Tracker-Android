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

public class PlayerMatchesAdapter extends ArrayAdapter<JSONObject>{
	private final String TAG = "PlayerMatchesAdapter";
	
	private DBAdapter mDatabaseAdapter;
	private Cursor mPlayerCursor;
	
	public PlayerMatchesAdapter(Activity a, List<JSONObject> matches){
		super(a, 0, matches);
	}
	
	@Override 
	public View getView(int position, View convertView, ViewGroup parent){
		Activity activity = (Activity) getContext();
		
		mDatabaseAdapter = new DBAdapter(getContext());
		mDatabaseAdapter.open();
		
		LayoutInflater inflater = activity.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.playermatches_row, null);
		
		TextView player1 = (TextView) rowView.findViewById(R.id.player1);
		TextView player2 = (TextView) rowView.findViewById(R.id.player2);
		
		JSONObject match = getItem(position);
		try {
			int pk1 = match.getInt("first_player");
			mPlayerCursor = mDatabaseAdapter.getPlayerByPK(pk1);
			mPlayerCursor.moveToFirst();
			int index = mPlayerCursor.getColumnIndex("handle");
			String first_player = mPlayerCursor.getString(index);
			
			int pk2 = match.getInt("second_player");
			mPlayerCursor = mDatabaseAdapter.getPlayerByPK(pk2);
			mPlayerCursor.moveToFirst();
			index = mPlayerCursor.getColumnIndex("handle");
			String second_player = mPlayerCursor.getString(index);
			
			player1.setText(first_player + " vs. ");
			player2.setText(second_player);
		} 
		
		catch (JSONException e) {
			Log.e(TAG, "Error: " + e.toString());
		}
		
		return rowView;
	}

}
