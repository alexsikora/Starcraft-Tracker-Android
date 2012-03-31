package illinois.sweng.sctracker;

import org.json.JSONArray;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Home activity for the application. Gives access to multiple functions for user. 
 */
public class HomeActivity extends Activity implements DelegateActivity {
	static String TAG = "homeActivity";
	static final String PREFS_FILE = "sc2prefs";
	private Button mSearchButton;
	private Button mUnregisterButton;
	private Button mLogOutButton;
	TrackerDatabaseAdapter mDBAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		mSearchButton = (Button) findViewById(R.id.searchButton);
		mUnregisterButton = (Button) findViewById(R.id.unregisterButton);
		mLogOutButton = (Button) findViewById(R.id.logoutButton);

		mSearchButton.setOnClickListener(new SearchButtonHandler());
		mUnregisterButton.setOnClickListener(new UnregisterButtonHandler());
		mLogOutButton.setOnClickListener(new LogOutButtonHandler());

		mDBAdapter = new TrackerDatabaseAdapter(this);
	}

	/**
	 * Log the user out; launches the main activity.
	 */
	private void launchLogOut() {
		Intent i = new Intent(this, SC2TrackerActivity.class);
		startActivity(i);
	}

	/**
	 * Launches the unregister activity.
	 */
	private void launchUnregister() {
		Intent i = new Intent(this, UnregisterActivity.class);
		startActivity(i);
	}

	/**
	 * Handler for the logout button. 
	 */
	private class LogOutButtonHandler implements View.OnClickListener {
		public void onClick(View v) {
			Log.d(TAG, "LogOut Button clicked.");
			launchLogOut();
		}
	}
	
	/**
	 * Handler for the unregister button. 
	 */
	private class UnregisterButtonHandler implements View.OnClickListener {
		public void onClick(View v) {
			Log.d(TAG, "Unreg Button clicked");
			launchUnregister();
		}
	};

	/**
	 * Dumb button to view first returned player
	 */
	private class SearchButtonHandler implements View.OnClickListener {
		public void onClick(View v) {
			Log.d(TAG, "Search Button Clicked");
			updatePlayers();
		}
	}

	/**
	 * Makes a request to the server communicator to get all the players.
	 * In the handleServerResponse, the database will be updated with the result.
	 */
	//TODO
	public void updatePlayers() {
		ServerCommunicator comm = new ServerCommunicator(this, "HOME");
		String key = getResources().getString(R.string.preferencesUserpass);
		SharedPreferences preferences = getSharedPreferences(PREFS_FILE, 0);
		String userpass = preferences.getString(key, "");
		comm.sendGetAllPlayersRequest(userpass);
	}

	@Override
	public void handleServerError(String message) {
		// TODO Auto-generated method stub

	}

	/**
	 * Handles the response data by launching the player status activity.
	 */
	@Override
	public void handleServerResponseData(JSONArray values) {
		Log.d("TAG", "attempting to display");
		mDBAdapter.open();
		mDBAdapter.updateDatabase(values);
		mDBAdapter.close();
		Intent i = new Intent(this, PlayerStatusActivity.class);
		startActivity(i);

	}

	@Override
	public void handleServerResponseMessage(String message) {
		// TODO Auto-generated method stub

	}
}
