package illinois.sweng.sctracker;


import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SC2TrackerActivity extends Activity {
    /** Called when the activity is first created. */
	
	static String TAG = "sc2trackerMainActivity";
	private Button mRegisterButton;
	private Button mUnregisterButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mRegisterButton = (Button) findViewById(R.id.registerButton);
        mUnregisterButton = (Button) findViewById(R.id.unregisterButton);
        
        mRegisterButton.setOnClickListener(new RegisterButtonHandler());
        mUnregisterButton.setOnClickListener(new UnregisterButtonHandler());
    }
   
    
    private void launchRegister() {
    	Intent i = new Intent(this, RegisterActivity.class);
		startActivity(i);
    }
    
    private void launchUnregister() {
    	Intent i = new Intent(this, UnregisterActivity.class);
		startActivity(i);
    }
    
    
    /* Button click handlers */
    private class RegisterButtonHandler implements View.OnClickListener {
		public void onClick(View v) {
			Log.d(TAG, "Reg Button clicked");
            launchRegister();
		}
	};
	
	private class UnregisterButtonHandler implements View.OnClickListener {
		public void onClick(View v) {
			Log.d(TAG, "Unreg Button clicked");
            launchUnregister();
		}
	};
	
	public void login() {
		HttpGet request = new HttpGet();
		String username = "";
		request.setHeader("Authorization", "Basic " + Base64.encodeToString("user:password".getBytes(), Base64.NO_WRAP));
		
		
		
	}
}