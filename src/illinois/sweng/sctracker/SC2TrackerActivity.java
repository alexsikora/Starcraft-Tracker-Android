package illinois.sweng.sctracker;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
        
        mRegisterButton.setOnClickListener(registerButtonHandler);
        mUnregisterButton.setOnClickListener(unregisterButtonHandler);
    }
   
    
    private void launchRegister() {
    	//Intent i = new Intent(this, RegisterActivity.class);
		//startActivity(i);
    }
    
    private void launchUnregister() {
    	//Intent i = new Intent(this, UnregisterActivity.class);
		//startActivity(i);
    }
    
    
    /* Button click handlers */
    View.OnClickListener registerButtonHandler = new View.OnClickListener() {
		
		public void onClick(View v) {
			Log.d(TAG, "Reg Button clicked");
            launchRegister();
		}
	};
	
	View.OnClickListener unregisterButtonHandler = new View.OnClickListener() {
		
		public void onClick(View v) {
			Log.d(TAG, "Unreg Button clicked");
            launchUnregister();
		}
	};
}