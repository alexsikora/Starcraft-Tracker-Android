package illinois.sweng.sctracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Home activity for the application. Gives access to multiple functions for
 * user.
 */
public class HomeActivity extends Activity implements DelegateActivity {
	private static String TAG = "homeActivity";
	private static final String PREFS_FILE = "sc2prefs";
	private Button mUnregisterButton;
	private Button mLogOutButton;
	private DBAdapter mDBAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		mUnregisterButton = (Button) findViewById(R.id.unregisterButton);
		mLogOutButton = (Button) findViewById(R.id.logoutButton);

		mUnregisterButton.setOnClickListener(new UnregisterButtonHandler());
		mLogOutButton.setOnClickListener(new LogOutButtonHandler());

		mDBAdapter = new DBAdapter(this);
		
		registerWithServer();
		
		//Update the database
		updatePlayers();
		updateTeams();
		updateEvents();
	}
	
	private void registerWithServer() {
    	Log.i("Registration Info", "Attempting to register c2dm");
    	Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
    	registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
    	registrationIntent.putExtra("sender", "star2tracker@gmail.com");
    	this.startService(registrationIntent);
    	Log.i("Registration Info", "Finished Sending registration intent");
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
	 * Makes a request to the server communicator to get all the players.
	 */
	private void updatePlayers() {
		Log.i(TAG, "Updating players");
		ServerCommunicator comm = new ServerCommunicator(this, TAG);
		String userpass = getUserPass();
		comm.sendGetAllPlayersRequest(userpass);
	}


	/**
	 * Makes a request to the server communicator to get all the teams.
	 */
	private void updateTeams() {
		Log.i(TAG, "Updating teams");
		ServerCommunicator comm = new ServerCommunicator(this, TAG);
		String userpass = getUserPass();
		comm.sendGetAllTeamsRequest(userpass);
	}

	/**
	 * Makes a request to the server communicator to get alist of all events.
	 */
	private void updateEvents() {
		Log.i(TAG, "Updating events");
		ServerCommunicator comm = new ServerCommunicator(this, TAG);
		String userpass = getUserPass();
		comm.sendGetAllEventsRequest(userpass);
	}

	/**
	 * Extracts the user's email address and password from shared preferences
	 * @return User's userpass in the format email:password
	 */
	private String getUserPass() {
		String key = getResources().getString(R.string.preferencesUserpass);
		SharedPreferences preferences = getSharedPreferences(PREFS_FILE, 0);
		String userpass = preferences.getString(key, "");
		return userpass;
	}
	
	public void handleServerError(String message) {
		Toast errorToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
		errorToast.show();
		Log.e(TAG, message);
	}

	/**
	 * Handles the response data by launching the player status activity.
	 */
	public void handleServerResponseData(JSONArray values) {
		Log.d("TAG", "UPDATING DATABASE");
		try {
			JSONObject firstEntry = (JSONObject) (values.get(0));
			mDBAdapter.open();
			if (firstEntry.getString("model").equals("players.player")) {
				mDBAdapter.updatePlayerTable(values);
			}
			if (firstEntry.getString("model").equals("players.team")) {
				mDBAdapter.updateTeamTable(values);
			}
			if (firstEntry.getString("model").equals("events.event")) {
				mDBAdapter.updateEventTable(values);
			}
			mDBAdapter.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Receives and logs a non-data message from the server. This should not
	 * occurr under normal operation for this activity.
	 */
	public void handleServerResponseMessage(String message) {
		Log.d(TAG, "Got message from server");
	}
}
