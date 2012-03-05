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
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UnregisterActivity extends Activity {
	
	private static final String TAG = "sc2TrackerUnregisterActivity";
	//TODO establish proper ids when handling possible errors; can't validate the information on own
	// so have to wait for server
//	private static final int DIALOG_INVALID_EMAIL_ID = 1;
//	private static final int DIALOG_INVALID_PASSWORD_ID = 2;
	
	private Button mDeleteAccountButton;
	private EditText mEmail, mPassword, mPasswordConfirm;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unregister);
        
        mDeleteAccountButton = (Button) findViewById(R.id.unregisterDeleteButton);
        mEmail = (EditText) findViewById(R.id.emailEditText);
        mPassword = (EditText) findViewById(R.id.passwordTextEdit);
        
        mDeleteAccountButton.setOnClickListener(new DeleteAccountHandler());
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
		
		manageDialog(id, dialog);
		
		return dialog;
	}
	
	/**
	 * Process the message to be displayed on the Dialog based on entered text
	 */
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		manageDialog(id, dialog);
	}
	
	/**
	 * Process the message to be displayed on the Dialog based on entered text
	 * @param id ID of the Dialog
	 * @param dialog Dialog being opened
	 */
	private void manageDialog(int id, Dialog dialog) {
		clearInputFields();
		AlertDialog alertDialog = (AlertDialog) dialog;
		CharSequence message;
		
		switch (id) {
//		case DIALOG_INVALID_EMAIL_ID:
//			message = getResources().getText(R.string.registerInvalidEmailMessage);
//			break;
//		case DIALOG_INVALID_PASSWORD_ID:
//			message = getResources().getText(R.string.registerNonmatchingPasswords);
//			break;
		default:
			message = "Please reenter your information";
		}
		
		alertDialog.setMessage(message);
	}
	
	/**
	 * Validate the user-entered data fields, and send the info to the server
	 * to create a new user account if it is allowable.
	 */
	private void deleteAccount() {
		String email = mEmail.getText().toString();
		String password = mPassword.getText().toString();
		
//		if(!validateEmailAddress(email)) {		
//			showDialog(DIALOG_INVALID_EMAIL_ID);
//			return;
//		}
//		
//		if(!validatePassword(password, confirmPassword)) {
//			showDialog(DIALOG_INVALID_PASSWORD_ID);
//			return;
//		}
		
		sendAccountDeletionRequest(email, password);
	}

	
	/**
	 * Sends a request to the server to create a new user account with the given
	 * username and password
	 * @param username Username for the new user account
	 * @param password Password for the new user account
	 * @return InputStream of the Http response, null if there was an exception
	 */
	private String sendAccountDeletionRequest(String username, String password) {
		//TODO
		String urlString = buildAccountDeletionURL(username, password);
		
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
		
		return "";
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
	private String buildAccountDeletionURL(String username, String password) {
		CharSequence baseURL = getResources().getText(R.string.serverURL);
		CharSequence registerURL = getResources().getText(R.string.serverUnregisterURL);
		
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
	private class DeleteAccountHandler implements View.OnClickListener {
		/**
		 * Log that the button was pressed, and attempt to create a new account
		 */
		public void onClick(View v) {
			Log.d(TAG, "Delete Account Button clicked");
            deleteAccount();
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
