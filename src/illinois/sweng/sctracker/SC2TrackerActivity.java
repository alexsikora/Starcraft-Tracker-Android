package illinois.sweng.sctracker;


import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SC2TrackerActivity extends DelegateActivity {
    /** Called when the activity is first created. */
	
	static String TAG = "sc2trackerMainActivity";
	private Button mRegisterButton, mLoginButton, mUnregisterButton;
	private EditText mEmail, mPassword;
	private ServerCommunicator mServerCommunicator;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mRegisterButton = (Button) findViewById(R.id.registerButton);
        mUnregisterButton = (Button) findViewById(R.id.unregisterButton);
        mLoginButton = (Button) findViewById(R.id.LoginButton);
        
        mRegisterButton.setOnClickListener(new RegisterButtonHandler());
        mUnregisterButton.setOnClickListener(new UnregisterButtonHandler());
        mLoginButton.setOnClickListener(new LoginButtonHandler());
        
        mEmail = (EditText) findViewById(R.id.mainEmailEditText);
        mPassword = (EditText) findViewById(R.id.mainPasswordTextEdit);
        
        mServerCommunicator = new ServerCommunicator(this, TAG);
    }
   
    
    private void launchRegister() {
    	Intent i = new Intent(this, RegisterActivity.class);
		startActivity(i);
    }
    
    private void launchUnregister() {
    	Intent i = new Intent(this, UnregisterActivity.class);
		startActivity(i);
    }
    
    private void loginUser() {
		String username = mEmail.getText().toString();
		String password = mPassword.getText().toString();
		String userpass = username + ":" + password;
		
		mServerCommunicator.sendAuthenticationRequest(userpass);
		
		
		
//		HttpClient client = new HttpClient();
//		client.getParams().setAuthenticationPreemptive(true);
//		Credentials defaultcreds = new UsernamePasswordCredentials("username", "password");
//		client.getState().setCredentials(new AuthScope("myhost", 80, AuthScope.ANY_REALM), defaultcreds);
		
    }
    
    /* Button click handlers */
    private class RegisterButtonHandler implements View.OnClickListener {
		public void onClick(View v) {
			Log.d(TAG, "Reg Button clicked");
            launchRegister();
		}
	}
	
	private class UnregisterButtonHandler implements View.OnClickListener {
		public void onClick(View v) {
			Log.d(TAG, "Unreg Button clicked");
            launchUnregister();
		}
	};
	
	private class LoginButtonHandler implements View.OnClickListener {
		public void onClick(View v) {
			Log.d(TAG, "Login Button clicked");
			loginUser();
		}
	}

	@Override
	public void handleServerError(String message) {
		Toast errorToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
		errorToast.show();
	}


	@Override
	public void handleServerResponse(List<NameValuePair> values) {
		mLoginButton.setText(R.string.registerNewAccountSuccess);
		
	}
}