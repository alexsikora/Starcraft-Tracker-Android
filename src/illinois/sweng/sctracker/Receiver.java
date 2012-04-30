package illinois.sweng.sctracker;

import org.json.JSONArray;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;


public class Receiver extends BroadcastReceiver implements DelegateActivity {
	private static final String TAG = "Receiver";
	private static final int HELLO_ID = 1;
	private static final String PREFS_FILE = "sc2prefs";
	private static String KEY = "c2dmPref";
	private static String REGISTRATION_KEY = "registrationKey";
	private ServerCommunicator mServerCommunicator;
	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
	    this.context = context;
	    Log.d("Registration Info", "registration received");
		if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
			mServerCommunicator = new ServerCommunicator(this, "RECEIVER");
	        handleRegistration(context, intent);
	        String key = context.getResources().getString(R.string.preferencesUserpass);
			SharedPreferences firstpreferences = context.getSharedPreferences(PREFS_FILE, 0);
			String userpass = firstpreferences.getString(key, "");
	        SharedPreferences preferences = context.getSharedPreferences(KEY, 0);
			String regid = preferences.getString(REGISTRATION_KEY, "");
			mServerCommunicator.sendDeviceRegistrationRequest(userpass, "a", regid);
	        
	    } else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
	        handleMessage(context, intent);
	    }
	 }
	
	public Resources getResources() {
		return this.context.getResources();
	}

	private void handleRegistration(Context context, Intent intent) {
	    String registration = intent.getStringExtra("registration_id");
	    if (intent.getStringExtra("error") != null) {
	        // Registration failed, should try again later.
		    Log.d("c2dm", "registration failed");
		    String error = intent.getStringExtra("error");
		    if(error == "SERVICE_NOT_AVAILABLE"){
		    	Log.d("c2dm", "SERVICE_NOT_AVAILABLE");
		    }else if(error == "ACCOUNT_MISSING"){
		    	Log.d("c2dm", "ACCOUNT_MISSING");
		    }else if(error == "AUTHENTICATION_FAILED"){
		    	Log.d("c2dm", "AUTHENTICATION_FAILED");
		    }else if(error == "TOO_MANY_REGISTRATIONS"){
		    	Log.d("c2dm", "TOO_MANY_REGISTRATIONS");
		    }else if(error == "INVALID_SENDER"){
		    	Log.d("c2dm", "INVALID_SENDER");
		    }else if(error == "PHONE_REGISTRATION_ERROR"){
		    	Log.d("c2dm", "PHONE_REGISTRATION_ERROR");
		    }
	    } else if (intent.getStringExtra("unregistered") != null) {
	        // unregistration done, new messages from the authorized sender will be rejected
	    	Log.d("c2dm", "unregistered");

	    } else if (registration != null) {
	    	Log.d("c2dm", registration);
	    	Editor editor =
                context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
            editor.putString(REGISTRATION_KEY, registration);
    		editor.commit();
	    }
	}

	private void handleMessage(Context context, Intent intent)
	{
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager manager = (NotificationManager) context.getSystemService(ns); 
		String message = intent.getExtras().getString("message");
		String matchID = intent.getExtras().getString("match_id");
		
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		CharSequence tickerText = message;
		CharSequence contentTitle = message;
		CharSequence contentText = message;

		Intent notificationIntent = new Intent(context, PushDisplayActivity.class);
		notificationIntent.putExtra("match_id", matchID);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		manager.notify(HELLO_ID, notification);
		
		Log.d("Receiver", "Handled message");
	}

	public void handleServerError(String message) {
		// TODO Auto-generated method stub
		
	}

	public void handleServerResponseData(JSONArray values) {
		Log.d(TAG, "Receiver got data from server");
	}

	public void handleServerResponseMessage(String message) {
		String success = "success";
		Toast toast = Toast.makeText(context, success, Toast.LENGTH_LONG);
		toast.show();
	}
}