package illinois.sweng.sctracker;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SC2TrackerActivity extends Activity {
    /** Called when the activity is first created. */
	
	static String TAG = "sc2tracker";
	private Button mRegisterButton;
	private Button mUnregisterButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mRegisterButton = (Button) findViewById(R.id.registerButton);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Reg Button clicked");
               // launchRegister();
            }
        });
        
        mUnregisterButton = (Button) findViewById(R.id.unregisterButton);
        mUnregisterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Unreg Button clicked");
                //launchUnregister();
            }
        });
        
    }
   
    /*
    public void launchRegister() {
    	Intent i = new Intent(this, RegisterActivity.class);
		startActivity(i);
    }
    
    public void launchUnregister() {
    	Intent i = new Intent(this, UnregisterActivity.class);
		startActivity(i);
    }
    */
    
}