package illinois.sweng.sctracker;

// startrack.alexsikora.com
// admin, pw: st4rtrack

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EventStatusActivity extends Activity implements DelegateActivity{
	static String TAG = "eventStatusActivity";
	DBAdapter mDBAdapter;

	String name = "";
	String startdate = "";
	String enddate = "";
	
	private Button mRoundOfButton;
	private Button mFinalsButton;

	JSONObject eventJSON;
	JSONArray rounds;
	JSONArray playerMatchesRoundOf;
	JSONArray playerMatchesFinals;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventstatus);
		
		try {
			getDataFromIntent();
			
			TextView t = (TextView) findViewById(R.id.eventStatusNameTextView);
			t.append(name);
			t = (TextView) findViewById(R.id.eventStatusStartDateTextView);
			t.append(startdate);
			t = (TextView) findViewById(R.id.eventStatusEndDateTextView);
			t.append(enddate);
			
			mRoundOfButton = (Button) findViewById(R.id.eventStatusRoundOfButton);
			mRoundOfButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v){
					Log.d(TAG, "RoundOf Button Clicked.");
					LaunchRoundOf();
				}
			});
			
			mFinalsButton = (Button) findViewById(R.id.eventStatusFinalsButton);
			mFinalsButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v){
					Log.d(TAG, "Finals Button Clicked.");
					LaunchFinals();
				}
			});
		
		} catch (JSONException e) {
			Log.e(TAG, "Exception thrown while retrieving JSON event information: " + e.toString());
		}
		
	}
	
	private void getDataFromIntent() throws JSONException{
		Intent intent = getIntent();
		Resources res = getResources();
		
		String data = intent.getStringExtra("data");
		Log.d(TAG, data);
		eventJSON = new JSONObject("JSON: " + data);
		eventJSON = eventJSON.getJSONObject("response");
		
		name = eventJSON.getString("name");
		startdate = eventJSON.getString("start_date");
		enddate = eventJSON.getString("end_date");
		
		rounds = eventJSON.getJSONArray("rounds");
		for(int i=0; i < rounds.length(); i++){
			JSONObject round = rounds.getJSONObject(i);
			if(round.getString("name") == "Round Of 64"){
				playerMatchesRoundOf = round.getJSONArray("player_matches");
				Log.d(TAG, "Round of 64 Assigned.");
			}
			
			else if(round.getString("name") == "Finals"){
				playerMatchesFinals = round.getJSONArray("player_matches");
				Log.d(TAG, "Finals Assigned.");
			}
		}
	}

	private void LaunchRoundOf(){
		Intent i = new Intent(this, PlayerMatches.class);
		i.putExtra("data", playerMatchesRoundOf.toString());
		startActivity(i);
	}
	
	private void LaunchFinals(){
		Intent i = new Intent(this, PlayerMatches.class);
		i.putExtra("data", playerMatchesFinals.toString());
		startActivity(i);
	}

	public void handleServerError(String message) {
		// TODO Auto-generated method stub
		
	}

	public void handleServerResponseData(JSONArray values) {
		// TODO Auto-generated method stub
		
	}

	public void handleServerResponseMessage(String message) {
		// TODO Auto-generated method stub
		
	}
}

	

