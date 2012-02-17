package illinois.sweng.sctracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends Activity {
	
	private static final String TAG = "sc2TrackerRegisterActivity";
	private Button mCreateAccountButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mCreateAccountButton = (Button) findViewById(R.id.registerButton);
        mCreateAccountButton.setOnClickListener(createAccountHandler);
	}
	
	private void createAccount() {
		
	}
	
	View.OnClickListener createAccountHandler = new View.OnClickListener() {
		
		public void onClick(View v) {
			Log.d(TAG, "Create Account Button clicked");
            createAccount();
		}
	};
}
