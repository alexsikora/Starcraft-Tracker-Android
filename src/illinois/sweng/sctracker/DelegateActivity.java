package illinois.sweng.sctracker;


import org.json.JSONArray;

import android.content.res.Resources;

/**
 * Subclass of Activity that allows for delegate-style callbacks to the Activity
 * 
 * @author Joel Ferm
 *
 */
public interface DelegateActivity {
	/**
	 * Handle the error response given by the server, already processed into 
	 * a single String for display to the user
	 * @param message String explaining the error and proper recourse
	 */
	public abstract void handleServerError(String message);
	
	/**
	 * Handle and process the data returned by the server on a successful request,
	 * already having been separated into key-value pairs by an intermediary
	 * @param values JSONArray containing the data returned by the server
	 */
	public abstract void handleServerResponseData(JSONArray values);
	
	/**
	 * Handle a success message returned by the server for a successful request
	 * that does not include data.
	 * @param message String success message returned by the server
	 */
	public abstract void handleServerResponseMessage(String message);
	
	/**
	 * 
	 */
	public abstract Resources getResources();
}
