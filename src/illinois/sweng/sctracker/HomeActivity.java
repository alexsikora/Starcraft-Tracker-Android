package illinois.sweng.sctracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends Activity {
	static String TAG = "homeActivity";
	private Button mSearchButton;
	private Button mUnregisterButton;
	private Button mLogOutButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		mSearchButton = (Button) findViewById(R.id.searchButton);
		mUnregisterButton = (Button) findViewById(R.id.unregisterButton);
		
		mSearchButton.setOnClickListener(new SearchButtonHandler());
	}
	
	private void launchLogOut(){
		Intent i = new Intent(this, HomeActivity.class);
		startActivity(i);
	}
	
	private void launchUnregister() {
    	Intent i = new Intent(this, UnregisterActivity.class);
		startActivity(i);
    }
	
	private class LogOutButtonHandler implements View.OnClickListener {
		public void onClick(View v) {
			Log.d(TAG, "LogOut Button clicked.");
			launchLogOut();
		}
	}
	
	private class UnregisterButtonHandler implements View.OnClickListener {
		public void onClick(View v) {
			Log.d(TAG, "Unreg Button clicked");
            launchUnregister();
		}
	};
	
	private class SearchButtonHandler implements View.OnClickListener {
		public void onClick(View v) {
			Log.d(TAG, "Search Button Clicked");
			onSearchRequested();
		}
	}
}
