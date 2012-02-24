package illinois.sweng.sctracker;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;

/**
 * Subclass of Activity that allows for delegate-style callbacks to the Activity
 * 
 * @author Joel Ferm
 *
 */
public abstract class DelegateActivity extends Activity {
	/**
	 * Handle the error response given by the server, already processed into 
	 * a single String for display to the user
	 * @param message String explaining the error and proper recourse
	 */
	public abstract void handleServerError(String message);
	
	/**
	 * Handle and process the data returned by the server on a successful request,
	 * already having been separated into key-value pairs by an intermediary
	 * @param values NameValuePairs containing the data returned by the server
	 */
	public abstract void handleServerResponse(List<NameValuePair> values);
}
