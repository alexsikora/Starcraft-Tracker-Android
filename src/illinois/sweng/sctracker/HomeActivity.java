package illinois.sweng.sctracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends DelegateActivity {
	static String TAG = "homeActivity";
	private Button mSearchButton;
	private Button mUnregisterButton;
	private Button mLogOutButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		mSearchButton = (Button) findViewById(R.id.searchButton);
		mUnregisterButton = (Button) findViewById(R.id.unregisterButton);
		mLogOutButton = (Button) findViewById(R.id.logoutButton);
		
		mSearchButton.setOnClickListener(new SearchButtonHandler());
		mUnregisterButton.setOnClickListener(new UnregisterButtonHandler());
		mLogOutButton.setOnClickListener(new LogOutButtonHandler());
	}
	
	private void launchLogOut(){
		Intent i = new Intent(this, SC2TrackerActivity.class);
		startActivity(i);
	}
	
	private void launchUnregister() {
    	Intent i = new Intent(this, UnregisterActivity.class);
		startActivity(i);
    }
	
	private class LogOutButtonHandler implements View.OnClickListener {
		public void onClick(View v) {
			Log.d(TAG, "LogOut Button clicked.");
			launchLogOut();
		}
	}
	
	private class UnregisterButtonHandler implements View.OnClickListener {
		public void onClick(View v) {
			Log.d(TAG, "Unreg Button clicked");
            launchUnregister();
		}
	};
	
	private class SearchButtonHandler implements View.OnClickListener {
		public void onClick(View v) {
			Log.d(TAG, "Search Button Clicked");
			onSearchRequested();
			doStuff();
		}
	}

	public void doStuff() {
		ServerCommunicator comm = new ServerCommunicator(this, "HOME");
		comm.sendGetAllPlayersRequest("test@account.com:test");
	}

	@Override
	public void handleServerError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleServerResponseData(JSONArray values) {
		// TODO Auto-generated method stub
		try {
			Log.d("TAG", "attempting to display");
			JSONObject player1 = (JSONObject) values.get(0);
			Intent i = new Intent(this, PlayerStatusActivity.class);
			i.putExtra("player", player1.toString());
			startActivity(i);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void handleServerResponseMessage(String message) {
		// TODO Auto-generated method stub
		
	}
}
