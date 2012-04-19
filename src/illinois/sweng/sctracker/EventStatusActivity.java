package illinois.sweng.sctracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EventStatusActivity extends Activity implements DelegateActivity{
	static String TAG = "eventStatusActivity";

	long eventPK = -1;
	String name = "";
	String startdate = "";
	String enddate = "";
	
	private Button mRoundsButton;
	
	JSONObject eventJSON;
	JSONArray rounds;
	JSONArray finals;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventstatus);
		
		try {
			getDataFromIntent();
			
			TextView t = (TextView) findViewById(R.id.eventStatusNameTextView);
			t.append(" " + name);
			t = (TextView) findViewById(R.id.eventStatusStartDateTextView);
			t.append(" " + startdate);
			t = (TextView) findViewById(R.id.eventStatusEndDateTextView);
			t.append(" " + enddate);
			
			mRoundsButton = (Button) findViewById(R.id.eventStatusRoundsButton);
			mRoundsButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v){
					Log.d(TAG, "Rounds Button Clicked.");
					LaunchRounds();
				}
			});
		
		} catch (JSONException e) {
			Log.e(TAG, "Exception thrown while retrieving JSON event information: " + e.toString());
		}
		
	}
	
	private void getDataFromIntent() throws JSONException{
		Intent intent = getIntent();
		Resources res = getResources();
		
		String pkKey = res.getString(R.string.keyPK); 
		eventPK = intent.getLongExtra(pkKey, -1);
		
		getEventInfo(eventPK);
	}
	
	private void getEventInfo(long eventPK){
		String prefsFile = getResources().getString(R.string.preferencesFilename);
		SharedPreferences prefs = getSharedPreferences(prefsFile, 0);
		String key = getResources().getString(R.string.preferencesUserpass);
		String userpass = prefs.getString(key,  "");
		
		ServerCommunicator comm = new ServerCommunicator(this, TAG);
		comm.sendGetEventRequest(userpass, eventPK);
	}

	private void LaunchRounds(){
		Intent i = new Intent(this, Rounds.class);
		i.putExtra("data", rounds.toString());
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
		try{
			Log.d(TAG, "Server Response message received: " + message);
			eventJSON = new JSONObject(message);
			
			name = eventJSON.getString("name");
			startdate = eventJSON.getString("start_date");
			enddate = eventJSON.getString("end_date");

			rounds = eventJSON.getJSONArray("rounds");
		}
		
		catch(JSONException e){
			Log.e(TAG, "Error retrieving JSON string returned from server.");
			e.printStackTrace();
		}
	}
}

	

