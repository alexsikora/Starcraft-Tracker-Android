package illinois.sweng.sctracker;

import android.app.Activity;
import android.content.Intent;
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
		if(!validateEmailAddress()) {
			return;
		}
		
		if(!confirmPasswordsMatch()) {
			return;
		}
	}
	
	/**
	 * Checks whether the email field contains a valid Email address
	 * @return a boolean that is true if and only if the Email field contains
	 * 			a valid email address
	 */
	private boolean validateEmailAddress() {
		boolean isValidEmail;
		
		String email = mEmail.getText().toString();
		// This is (obviously) not strong validation, but we aren't too focused on it at the moment
		isValidEmail = email.matches(".+@.+");
		
		return isValidEmail;
	}
	
	/**
	 * Checks that both the Password and Confirm Password are the same
	 * @return a boolean that is true if and only if the text entered into the Password
	 * 			and Confirm Password fields match
	 */
	private boolean confirmPasswordsMatch() {
		boolean passwordsMatch;
		
		String password = mPassword.getText().toString();
		String confirmPassword = mPasswordConfirm.getText().toString();
		
		passwordsMatch = confirmPassword.equals(password);
		
		return passwordsMatch;
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
