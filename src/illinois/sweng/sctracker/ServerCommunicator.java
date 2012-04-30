package illinois.sweng.sctracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.util.Base64;
import android.util.Log;

public class ServerCommunicator {
	private final String TAG;
	private DelegateActivity mDelegate;
	private Resources mResources;
	
	/**
	 * Creates a new ServerCommunicator with the given context
	 * 
	 * @param delegate
	 *            Context of the owner of the ServerCommunicator
	 * @param delegateTag
	 *            String identifying the owner of this ServerCommunicator
	 */
	public ServerCommunicator(DelegateActivity delegate, String delegateTag) {
		mDelegate = delegate;
		mResources = mDelegate.getResources(); 
		TAG = "sc2TrackerServerCommunicator-" + delegateTag;
	}

	/**
	 * Sends a request to the server to create a new user account with the given
	 * username and password
	 * 
	 * @param username
	 *            Username for the new user account
	 * @param password
	 *            Password for the new user account
	 * 
	 */
	public void sendAccountCreationRequest(String username, String password) {
		CharSequence registerURL = mResources.getText(R.string.serverRegisterURL);
		String urlString = buildURL(registerURL);
		HttpPost httpPost = new HttpPost(urlString);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);
		pairs.add(new BasicNameValuePair("username", username));
		pairs.add(new BasicNameValuePair("password", password));

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(pairs));

		} catch (IOException e) {
			String message = mResources.getString(R.string.serverIOExceptionMessage);
			mDelegate.handleServerError(message);
			Log.e(TAG, message, e);
			e.printStackTrace();
		} 
		
		Log.d(TAG, "Sending account creation request");
		executeHttpRequest(httpPost);
	}

	/**
	 * Sends a request to the server to delete a user account with the given
	 * username and password
	 * 
	 * @param username
	 *            Username for the user account
	 * @param password
	 *            Password for the user account
	 */
	public void sendAccountDeletionRequest(String username, String password) {
		CharSequence unregisterURL = mResources.getText(R.string.serverUnregisterURL);
		String urlString = buildURL(unregisterURL);
		HttpPost httpPost = new HttpPost(urlString);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);
		pairs.add(new BasicNameValuePair("username", username));
		pairs.add(new BasicNameValuePair("password", password));
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(pairs));

		} catch (IOException e) {
			String message = mResources.getString(R.string.serverIOExceptionMessage);
			mDelegate.handleServerError(message);
			Log.e(TAG, message, e);
			e.printStackTrace();
		} 

		Log.i(TAG, "Sending account deletion request");
		executeHttpRequest(httpPost);
	}
	
	/**
	 * Sends a GET request for authentication to the server with String userpass
	 * as username:password
	 * 
	 * @param userpass String of the form username:password
	 */
	public void sendAuthenticationRequest(String userpass) {
		Log.i(TAG, "Sending authentication request" + userpass);
		CharSequence authURL = mResources.getText(R.string.serverAuthenticateURL);
		String urlString = buildURL(authURL);
		sendBasicAuthGet(userpass, urlString);
	}
	
	/**
	 * Sends a GET request to retrieve all player data from the server
	 * @param userpass String of the form username:password
	 */
	public void sendGetAllPlayersRequest(String userpass) {
		Log.i(TAG, "Sending get all players request:" + userpass);
		CharSequence getPlayersURL = mResources.getText(R.string.serverGetAllPlayersURL);
		String urlString = buildURL(getPlayersURL);
		sendBasicAuthGet(userpass, urlString);
	}
	
	/**
	 * Sends a GET request to retrieve all team data from the server
	 * @param userpass String of the form username:password
	 */
	public void sendGetAllTeamsRequest(String userpass) {
		Log.i(TAG, "Sending get all teams request");
		CharSequence getTeamsURL = mResources.getText(R.string.serverGetAllTeamsURL);
		String urlString = buildURL(getTeamsURL);
		sendBasicAuthGet(userpass, urlString);
	}
	
	/**
	 * Sends a GET request to retrieve a list of all Events from the server 
	 * @param userpass String of the form username:password
	 */
	public void sendGetAllEventsRequest(String userpass) {
		Log.i(TAG, "Sending get all events request");
		CharSequence getEventsURL = mResources.getText(R.string.serverGetAllEventsURL);
		String urlString = buildURL(getEventsURL);
		sendBasicAuthGet(userpass, urlString);
	}
	
	/**
	 * Sends a GET request to the server to retrieve all data about a single event
	 * @param userpass String of the form username:password
	 * @param eventPK long representing the event's primary key ID
	 */
	public void sendGetEventRequest(String userpass, long eventPK){
		Log.i(TAG, "Sending get event request");
		CharSequence getEventURL = mResources.getText(R.string.serverGetEventURL);
		String urlString = buildURL(getEventURL, String.valueOf(eventPK));
		sendBasicAuthGet(userpass, urlString);
	}
	
	/**
	 * Sends a GET request to the server to retrieve all of a user's favorites
	 * @param userpass String of the form username:password
	 */
	public void sendGetAllFavoritesRequest(String userpass) {
		Log.i(TAG, "Sending get all favorites request"  + userpass);
		CharSequence allFavoritesURL = mResources.getText(R.string.serverGetAllFavoritesURL);
		String urlString = buildURL(allFavoritesURL);
		sendBasicAuthGet(userpass, urlString);
	}
	
	/**
	 * Sends a GET request to add a player to a user's list of Favorites
	 * @param userpass String of the form username:password
	 * @param playerPK long representing the player's primary key ID
	 */
	public void sendFavoritePlayerRequest(String userpass, long playerPK) {
		Log.i(TAG, "Sending favorite player " + String.valueOf(playerPK) + " request");
		CharSequence favoritePlayerURL = mResources.getText(R.string.serverFavoritePlayerURL);
		String urlString = buildURL(favoritePlayerURL, String.valueOf(playerPK));
		sendBasicAuthGet(userpass, urlString);
	}
	
	/**
	 * Sends a GET request to add a team to a user's list of Favorites
	 * @param userpass String of the form username:password
	 * @param teamPK long representing the team's primary key ID
	 */
	public void sendFavoriteTeamRequest(String userpass, long teamPK) {
		Log.i(TAG, "Sending favorite team" + String.valueOf(teamPK) + " request");
		CharSequence favoriteTeamURL = mResources.getText(R.string.serverFavoriteTeamURL);
		String urlString = buildURL(favoriteTeamURL, String.valueOf(teamPK));
		sendBasicAuthGet(userpass, urlString);
	}
	
	/**
	 * Sends a GET request to add an event to a user's list of Favorites
	 * @param userpass String of the form username:password
	 * @param eventPK long representing the event's primary key ID
	 */
	public void sendFavoriteEventRequest(String userpass, long eventPK) {
		Log.i(TAG, "Sending favorite event" + String.valueOf(eventPK) + " request");
		CharSequence favoriteEventURL = mResources.getText(R.string.serverFavoriteEventURL);
		String urlString = buildURL(favoriteEventURL, String.valueOf(eventPK));
		sendBasicAuthGet(userpass, urlString);
	}
	
	/**
	 * Sends a GET request to remove a player from a user's list of Favorites
	 * @param userpass String of the form username:password
	 * @param playerPK long representing the player's primary key ID
	 */
	public void sendUnfavoritePlayerRequest(String userpass, long playerPK) {
		Log.i(TAG, "Sending unfavorite player " + String.valueOf(playerPK) + " request");
		CharSequence unfavoritePlayerURL = mResources.getText(R.string.serverUnfavoritePlayerURL);
		String urlString = buildURL(unfavoritePlayerURL, String.valueOf(playerPK));
		sendBasicAuthGet(userpass, urlString);
	}
	
	/**
	 * Sends a GET request to remove a team from a user's list of Favorites
	 * @param userpass String of the form username:password
	 * @param teamPK long representing the team's primary key ID
	 */
	public void sendUnfavoriteTeamRequest(String userpass, long teamPK) {
		Log.i(TAG, "Sending unfavorite team " + String.valueOf(teamPK) + " request");
		CharSequence unfavoriteTeamURL = mResources.getText(R.string.serverUnfavoriteTeamURL);
		String urlString = buildURL(unfavoriteTeamURL, String.valueOf(teamPK));
		sendBasicAuthGet(userpass, urlString);
	}
	
	/**
	 * Sends a GET request to remove an event from a user's list of Favorites
	 * @param userpass String of the form username:password
	 * @param eventPK long representing the event's primary key ID
	 */
	public void sendUnfavoriteEventRequest(String userpass, long eventPK) {
		Log.i(TAG, "Sending unvaforite event " + String.valueOf(eventPK) + " request");
		CharSequence unfavoriteEventURL = mResources.getText(R.string.serverUnfavoriteEventURL);
		String urlString = buildURL(unfavoriteEventURL, String.valueOf(eventPK));
		sendBasicAuthGet(userpass, urlString);
	}
	
	/**
	 * Sends a GET request to register a device with the server for push notifications
	 * @param userpass String of the form username:password
	 * @param type String containing the device type
	 * @param regId String containing the device ID
	 */
	public void sendDeviceRegistrationRequest(String userpass, String type, String regId) {
		Log.i(TAG, "Sending device registration request");
		CharSequence registerDeviceURL = mResources.getText(R.string.serverRegisterDeviceURL);
		String params = "?type=" + type + "&rid=" + regId;
		String urlString = buildURL(registerDeviceURL, params);
		sendBasicAuthGet(userpass, urlString);
	}

	/**
	 * Sends a GET request to a given URL using given credentials
	 * @param userpass String of the form username:password
	 * @param urlString String containing the URL to send a request to
	 */
	private void sendBasicAuthGet(String userpass, String urlString) {
		HttpGet request = new HttpGet(urlString);
		request.setHeader(
				"Authorization",
				"Basic "
						+ Base64.encodeToString(userpass.getBytes(),
								Base64.NO_WRAP));
		executeHttpRequest(request);
	}

	/**
	 * Sends an Http request and handles the response from the server 
	 * @param request HttpUriRequest to be executed
	 */
	private void executeHttpRequest(HttpUriRequest request) {
		HttpClient httpClient = new DefaultHttpClient();
		
		try {
			HttpResponse response = httpClient.execute(request);
			readHttpResponse(response);
		} catch (IOException e) {
			String message = mResources.getString(R.string.serverIOExceptionMessage);
			mDelegate.handleServerError(message);
			Log.e(TAG, message, e);
			e.printStackTrace();
		} catch (JSONException e) {
			String message = mResources.getString(R.string.serverJSONError);
			mDelegate.handleServerError(message);
			Log.e(TAG, message, e);
			e.printStackTrace();
		}
	}

	/**
	 * Read the httpResponse and display the appropriate success/failure
	 * notification
	 * 
	 * @param httpResponse InputStream returned from the web server
	 * @throws IOException
	 * @throws JSONException
	 */
	private void readHttpResponse(HttpResponse httpResponse)
			throws IOException, JSONException {
		
		StatusLine statusLine = httpResponse.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		
		if (statusCode == HttpStatus.SC_OK) {
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream in = httpEntity.getContent();			
			String responseString = readStream(in);
			
			Log.d(TAG, "HTTP response string:" + responseString);
			JSONObject json = new JSONObject(responseString);
			
			int responseCode = json.getInt("status_code");
			Log.d(TAG, "HTTP response Status code:" + String.valueOf(responseCode));
			if(responseCode == mResources.getInteger(R.integer.server_OK)) {
				sendSuccessCallback(json);
			} else {
				String errorMessage = json.getString("response");
				mDelegate.handleServerError(errorMessage);
			}			
		} else {
			String response = String.valueOf(statusCode);
			Log.d(TAG, response);
			String message = "An error occurred on the server";
			mDelegate.handleServerError(message);
		}
	}

	/**
	 * When a request is successfully completed, call back to the delegate
	 * that made the request with the response from the server
	 * @param json JSONObject returned from the server
	 */
	private void sendSuccessCallback(JSONObject json) {
		JSONArray array = json.optJSONArray("response");
		if(array != null) {
			mDelegate.handleServerResponseData(array);
		} else {
			String message = json.optString("response");
			mDelegate.handleServerResponseMessage(message);
		}
	}

	/**
	 * Reads an InputStream into a String
	 * @param in InputStream to be read
	 * @return String containing the data from the InputStream
	 * @throws IOException
	 */
	private String readStream(InputStream in) throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		StringBuilder total = new StringBuilder();
		String line;
		while ((line = r.readLine()) != null) {
			total.append(line);
		}		
		return total.toString();
	}
	
	/**
	 * Builds a URL to make calls to the server with no extras
	 * @param commandURL String specifying what remote API call to make
	 * @return String of the complete URL necessary for server calls
	 */
	private String buildURL(CharSequence commandURL) {
		return buildURL(commandURL, "");
	}
	
	/**
	 * Builds a URL to make calls to the server
	 * @param commandURL String specifying what remote API call to make
	 * @param extras String containing any extras to append on the URL
	 * @return String of the complete URL necessary for server calls
	 */
	private String buildURL(CharSequence commandURL, String extras) {
		CharSequence baseURL = mResources.getText(R.string.serverURL);		
		StringBuilder sb = new StringBuilder("http://");
		sb.append(baseURL);
		sb.append(commandURL);
		sb.append(extras);		
		return sb.toString();
	}

}
