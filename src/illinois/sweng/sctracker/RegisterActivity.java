package illinois.sweng.sctracker;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {
	
	private static final String TAG = "sc2TrackerRegisterActivity";
	private Button mCreateAccountButton;
	private EditText mEmail, mPassword, mPasswordConfirm;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mCreateAccountButton = (Button) findViewById(R.id.registerCreateButton);
        mEmail = (EditText) findViewById(R.id.emailEditText);
        mPassword = (EditText) findViewById(R.id.passwordTextEdit);
        mPasswordConfirm = (EditText) findViewById(R.id.passwordConfirmTextEdit);
        
        mCreateAccountButton.setOnClickListener(createAccountHandler);
	}
	
	/**
	 * Validate the user-entered data fields, and send the info to the server
	 * to create a new user account if it is allowable.
	 */
	private void createAccount() {
		String email = mEmail.getText().toString();
		String password = mPassword.getText().toString();
		String confirmPassword = mPasswordConfirm.getText().toString();
		
		if(!validateEmailAddress(email)) {
			// TODO invalid email toast
			return;
		}
		
		if(!confirmPasswordsMatch(password, confirmPassword)) {
			// TODO nonmatching passwords toast
			return;
		}
		
		InputStream httpResponse = sendAccountCreationRequest(email, password);
		if(httpResponse == null) {
			// TODO account creation failure
		} else {
			readHttpResponse(httpResponse);
		}
	}
	
	/**
	 * Read the httpResponse and display the appropriate success/failure notification
	 * @param httpResponse InputStream returned from the web server
	 */
	private void readHttpResponse(InputStream httpResponse) {
		// TODO display account creation success
		
		try {
			httpResponse.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a request to the server to create a new user account with the given
	 * username and password
	 * @param username Username for the new user account
	 * @param password Password for the new user account
	 * @return InputStream of the Http response, null if there was an exception
	 */
	private InputStream sendAccountCreationRequest(String username, String password) {
		String urlString = buildAccountCreationURL(username, password);
		BufferedInputStream in = null;
		URL url;
		
		try {
			url = new URL(urlString);			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		
		try {
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			in = new BufferedInputStream(urlConnection.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return in;
	}
	
	/**
	 * Given a username and a password, builds the appropriate url to send a GET to
	 * in order to create a new account
	 * @param username User's new account email address/username
	 * @param password User's password
	 * @return String representing the URL to generate a new user account
	 */
	private String buildAccountCreationURL(String username, String password) {
		CharSequence baseURL = getResources().getText(R.string.serverURL);
		
		StringBuilder sb = new StringBuilder("http://");
		sb.append(baseURL);
		sb.append("?username=");
		sb.append(username);
		sb.append("&password=");
		sb.append(password);
		String urlString = sb.toString();
		
		return urlString;
	}
	
	/**
	 * Checks whether the email field contains a valid Email address
	 * @return a boolean that is true if and only if the Email field contains
	 * 			a valid email address
	 */
	private boolean validateEmailAddress(String email) {
		// This is (obviously) not strong validation, but we aren't too focused on it at the moment
		return  email.matches(".+@.+");
	}
	
	/**
	 * Checks that both the Password and Confirm Password are the same
	 * @return a boolean that is true if and only if the text entered into the Password
	 * 			and Confirm Password fields match
	 */
	private boolean confirmPasswordsMatch(String password, String confirmPassword) {
		return confirmPassword.equals(password);
	}
	
	/**
	 * Custom handler for the account creation button
	 */
	View.OnClickListener createAccountHandler = new View.OnClickListener() {
		/**
		 * Log that the button was pressed, and attempt to create a new account
		 */
		public void onClick(View v) {
			Log.d(TAG, "Create Account Button clicked");
            createAccount();
		}
	};
}
