package illinois.sweng.sctracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

public class ServerCommunicator {
	Context mContext;
	
	/**
	 * Creates a new ServerCommunicator with the given context
	 * @param context Context of the owner of the ServerCommunicator
	 */
	public ServerCommunicator(Context context) {
		mContext = context;
	}
	
	/**
	 * Sends a request to the server to create a new user account with the given
	 * username and password
	 * @param username Username for the new user account
	 * @param password Password for the new user account
	 */
	public void sendAccountCreationRequest(String username, String password) {
		String urlString = buildAccountCreationURL(username, password);
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(urlString);
		
		try {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);
			pairs.add(new BasicNameValuePair("username", username));
			pairs.add(new BasicNameValuePair("password", password));
			httpPost.setEntity(new UrlEncodedFormEntity(pairs));
			
			HttpResponse response = httpClient.execute(httpPost);
			readHttpResponse(response);
			
		} catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	}

	/**
	 * Read the httpResponse and display the appropriate success/failure notification
	 * @param httpResponse InputStream returned from the web server
	 */
	private void readHttpResponse(HttpResponse httpResponse) {
		// TODO read the response
	}

	/**
	 * Given a username and a password, builds the appropriate url to send a GET to
	 * in order to create a new account
	 * @param username User's new account email address/username
	 * @param password User's password
	 * @return String representing the URL to generate a new user account
	 */
	private String buildAccountCreationURL(String username, String password) {
		CharSequence baseURL = mContext.getResources().getText(R.string.serverURL);
		CharSequence registerURL = mContext.getResources().getText(R.string.serverRegisterURL);
		
		StringBuilder sb = new StringBuilder("http://");
		sb.append(baseURL);
		sb.append(registerURL);
		String urlString = sb.toString();
		
		return urlString;
	}
}
