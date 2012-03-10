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
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;

public class ServerCommunicator {
	private final String TAG;
	private DelegateActivity mDelegate;

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
	 * @return 
	 * 
	 */
	public void sendAccountCreationRequest(String username, String password) {
		String urlString = buildAccountCreationURL(username, password);
		HttpPost httpPost = new HttpPost(urlString);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);
		pairs.add(new BasicNameValuePair("username", username));
		pairs.add(new BasicNameValuePair("password", password));

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(pairs));

		} catch (IOException e) {
			String message = mDelegate.getResources().getString(
					R.string.serverIOExceptionMessage);
			mDelegate.handleServerError(message);
			Log.e(TAG, message, e);
			e.printStackTrace();
		} 
		
		Log.d(TAG, "Sending account creation request");
		executeHttpRequest(httpPost);
	}



	/**
	 * Sends a request to the server to create a new user account with the given
	 * username and password
	 * 
	 * @param username
	 *            Username for the new user account
	 * @param password
	 *            Password for the new user account
	 * @return InputStream of the Http response, null if there was an exception
	 */
	public void sendAccountDeletionRequest(String username, String password) {
		// TODO figure out return value
		String urlString = buildAccountDeletionURL(username, password);
		HttpPost httpPost = new HttpPost(urlString);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);
		pairs.add(new BasicNameValuePair("username", username));
		pairs.add(new BasicNameValuePair("password", password));
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(pairs));

		} catch (IOException e) {
			String message = mDelegate.getResources().getString(
					R.string.serverIOExceptionMessage);
			mDelegate.handleServerError(message);
			Log.e(TAG, message, e);
			e.printStackTrace();
		} 

		Log.d(TAG, "Sending account deletion request");
		executeHttpRequest(httpPost);
	}
	
	/**
	 * Sends a GET request for authentication to the server with String userpass
	 * as username:password
	 * 
	 * @param username
	 *            Username for the new user account
	 * @param password
	 *            Password for the new user account
	 * @return InputStream of the Http response, null if there was an exception
	 */
	public void sendAuthenticationRequest(String userpass) {
		String urlString = buildAuthenticateURL();
		HttpGet request = new HttpGet(urlString);
		request.setHeader(
				"Authorization",
				"Basic "
						+ Base64.encodeToString(userpass.getBytes(),
								Base64.NO_WRAP));

		Log.d(TAG, "Sending authentication request");
		executeHttpRequest(request);
	}
	
	/**
	 * Sends an Http request and handles the response from the server 
	 * @param request HttpUriRequest to be executed
	 * @return
	 */
	private void executeHttpRequest(HttpUriRequest request) {
		HttpClient httpClient = new DefaultHttpClient();
		
		try {
			HttpResponse response = httpClient.execute(request);
			readHttpResponse(response);

		} catch (IOException e) {
			String message = mDelegate.getResources().getString(
					R.string.serverIOExceptionMessage);
			mDelegate.handleServerError(message);
			Log.e(TAG, message, e);
			e.printStackTrace();
		} catch (JSONException e) {
			String message = mDelegate.getResources().getString(
					R.string.serverJSONError);
			mDelegate.handleServerError(message);
			Log.e(TAG, message, e);
			e.printStackTrace();
		}
	}

	/**
	 * Read the httpResponse and display the appropriate success/failure
	 * notification
	 * 
	 * @param httpResponse
	 *            InputStream returned from the web server
	 * @throws IOException
	 * @throws JSONException
	 */
	private void readHttpResponse(HttpResponse httpResponse)
			throws IOException, JSONException {
		
		StatusLine statusLine = httpResponse.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		
		Log.d("XX", "Entering statuscode");
		if (statusCode == HttpStatus.SC_OK) {
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream in = httpEntity.getContent();
			
			// TODO Read the JSON
			BufferedReader r = new BufferedReader(new InputStreamReader(in));
			StringBuilder total = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				total.append(line);
			}
			
			Log.d("XX", total.toString());
			JSONObject json = new JSONObject(total.toString());
			List<NameValuePair> values = new ArrayList<NameValuePair>(
					json.length());
			
			mDelegate.handleServerResponse(values);
		} else {
			// TODO Process the error
			String response = "" + httpResponse.getStatusLine().getStatusCode();
			Log.d("XX", response);
			String message = "An error occurred on the server";
			mDelegate.handleServerError(message);
		}
	}

	/**
	 * Given a username and a password, builds the appropriate url to send a GET
	 * to in order to create a new account
	 * 
	 * @param username
	 *            User's new account email address/username
	 * @param password
	 *            User's password
	 * @return String representing the URL to generate a new user account
	 */
	private String buildAccountCreationURL(String username, String password) {
		CharSequence baseURL = mDelegate.getResources().getText(
				R.string.serverURL);
		CharSequence registerURL = mDelegate.getResources().getText(
				R.string.serverRegisterURL);
		
		StringBuilder sb = new StringBuilder("http://");
		sb.append(baseURL);
		sb.append(registerURL);
		String urlString = sb.toString();
		
		return urlString;
	}

	/**
	 * Given a username and a password, builds the appropriate url to send a GET
	 * to in order to create a new account
	 * 
	 * @param username
	 *            User's new account email address/username
	 * @param password
	 *            User's password
	 * @return String representing the URL to generate a new user account
	 */
	private String buildAccountDeletionURL(String username, String password) {
		CharSequence baseURL = mDelegate.getResources().getText(
				R.string.serverURL);
		CharSequence registerURL = mDelegate.getResources().getText(
				R.string.serverUnregisterURL);

		StringBuilder sb = new StringBuilder("http://");
		sb.append(baseURL);
		sb.append(registerURL);
		String urlString = sb.toString();

		return urlString;
	}

	/**
	 * Sends a GET to do authentication
	 * 
	 * @param username
	 *            User's new account email address/username
	 * @param password
	 *            User's password
	 * @return String representing the URL to generate a new user account
	 */
	private String buildAuthenticateURL() {
		CharSequence baseURL = mDelegate.getResources().getText(
				R.string.serverURL);
		CharSequence authURL = mDelegate.getResources().getText(
				R.string.serverAuthenticateURL);

		StringBuilder sb = new StringBuilder("http://");
		sb.append(baseURL);
		sb.append(authURL);
		String urlString = sb.toString();

		return urlString;
	}

	

}
