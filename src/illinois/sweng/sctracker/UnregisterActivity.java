package illinois.sweng.sctracker;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UnregisterActivity extends DelegateActivity {
	
	private static final String TAG = "UnregisterActivity";
	//TODO establish proper ids when handling possible errors; can't validate the information on own
	// so have to wait for server
	private static final int DIALOG_INVALID_EMAIL_ID = 1;
	
	private Button mDeleteAccountButton;
	private EditText mEmail, mPassword, mPasswordConfirm;
	private ServerCommunicator mServerCommunicator;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unregister);
        
        mDeleteAccountButton = (Button) findViewById(R.id.unregisterDeleteButton);
        mEmail = (EditText) findViewById(R.id.emailEditText);
        mPassword = (EditText) findViewById(R.id.passwordTextEdit);
        mServerCommunicator = new ServerCommunicator(this, TAG);
        
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
		case DIALOG_INVALID_EMAIL_ID:
			message = getResources().getText(R.string.registerInvalidEmailMessage);
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
	private void deleteAccount() {
		String email = mEmail.getText().toString();
		String password = mPassword.getText().toString();
		
		if(!validateEmailAddress(email)) {		
			showDialog(DIALOG_INVALID_EMAIL_ID);
			return;
		}
		
		mServerCommunicator.sendAccountDeletionRequest(email, password);
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
	}

	@Override
	public void handleServerError(String message) {
		Toast errorToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
		errorToast.show();
	}

	@Override
	public void handleServerResponse(List<NameValuePair> values) {
		// TODO Auto-generated method stub
	};

	/**
	 * Checks whether the email field contains a valid Email address
	 * @return a boolean that is true if and only if the Email field contains
	 * 			a valid email address
	 */
	private boolean validateEmailAddress(String email) {
		// This is (obviously) not strong validation, but we aren't too focused on it at the moment
		return  email.matches(".+@.+");
	}
	
}
