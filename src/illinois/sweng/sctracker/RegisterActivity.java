package illinois.sweng.sctracker;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {
	
	private static final String TAG = "sc2TrackerRegisterActivity";
	private static final int DIALOG_INVALID_EMAIL_ID = 1;
	private static final int DIALOG_NONMATCH_PASSWORDS_ID = 2;
	
	private Button mCreateAccountButton;
	private EditText mEmail, mPassword, mPasswordConfirm;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        mCreateAccountButton = (Button) findViewById(R.id.registerCreateButton);
        mEmail = (EditText) findViewById(R.id.emailEditText);
        mPassword = (EditText) findViewById(R.id.passwordTextEdit);
        mPasswordConfirm = (EditText) findViewById(R.id.passwordConfirmTextEdit);
        
        mCreateAccountButton.setOnClickListener(new CreateAccountHandler());
	}

	/**
	 * Builds the alert dialog for improperly entered text
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("Ok", new CloseDialogListener());
		
		Dialog dialog = alertDialogBuilder.create();
		
		manageInvalidInformationDialog(id, dialog);
		
		return dialog;
	}
	
	/**
	 * Process the message to be displayed on the Dialog based on entered text
	 */
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		manageInvalidInformationDialog(id, dialog);
	}
	
	/**
	 * Process the message to be displayed on the Dialog based on entered text
	 * @param id ID of the Dialog
	 * @param dialog Dialog being opened
	 */
	private void manageInvalidInformationDialog(int id, Dialog dialog) {
		clearInputFields();
		AlertDialog alertDialog = (AlertDialog) dialog;
		CharSequence message;
		
		switch (id) {
		case DIALOG_INVALID_EMAIL_ID:
			message = getResources().getText(R.string.registerInvalidEmailMessage);
			break;
		case DIALOG_NONMATCH_PASSWORDS_ID:
			message = getResources().getText(R.string.registerNonmatchingPasswords);
			break;
		default:
			message = "Please reenter your information";
		}
		
		alertDialog.setMessage(message);
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
			showDialog(DIALOG_INVALID_EMAIL_ID);
			return;
		}
		
		if(!confirmPasswordsMatch(password, confirmPassword)) {
			showDialog(DIALOG_NONMATCH_PASSWORDS_ID);
			return;
		}
		
		ProgressDialog dialog = ProgressDialog.show(this, "", "Creating account. Please wait...", true);
		String httpResponse = sendAccountCreationRequest(email, password);
		dialog.dismiss();
	}

	
	/**
	 * Sends a request to the server to create a new user account with the given
	 * username and password
	 * @param username Username for the new user account
	 * @param password Password for the new user account
	 * @return InputStream of the Http response, null if there was an exception
	 */
	private String sendAccountCreationRequest(String username, String password) {
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
			urlConnection.setRequestMethod("GET");
			in = new BufferedInputStream(urlConnection.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return readHttpResponse(in);
	}
	
	/**
	 * Read the httpResponse and display the appropriate success/failure notification
	 * @param httpResponse InputStream returned from the web server
	 */
	private String readHttpResponse(InputStream httpResponse) {
		// TODO read the http response
		if(httpResponse == null) return "";
		StringBuilder sb = new StringBuilder();
		
		try {
			httpResponse.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
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
		CharSequence registerURL = getResources().getText(R.string.serverRegisterURL);
		
		StringBuilder sb = new StringBuilder("http://");
		sb.append(baseURL);
		sb.append(registerURL);
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
	 * Clear all input fields, forcing the user to start over
	 */
	private void clearInputFields() {
		mEmail.setText("");
		mPassword.setText("");
		mPasswordConfirm.setText("");
	}
	
	/**
	 * Custom handler for the account creation button
	 */
	private class CreateAccountHandler implements View.OnClickListener {
		/**
		 * Log that the button was pressed, and attempt to create a new account
		 */
		public void onClick(View v) {
			Log.d(TAG, "Create Account Button clicked");
            createAccount();
		}
	};
	
	/**
	 * Custom handler to close the error dialog
	 */
	private class CloseDialogListener implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	};
}
