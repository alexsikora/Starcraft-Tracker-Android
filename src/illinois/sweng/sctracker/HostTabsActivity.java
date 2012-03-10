package illinois.sweng.sctracker;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;


public class HostTabsActivity extends TabActivity{
	static String TAG = "hostActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.host);
		
		TabHost tabHost = getTabHost(); // the activity TabHost
		TabHost.TabSpec spec; // Reusable TabSpec for each tab
		Intent intent; // Reusable Intent for each Tab
		
		// Create an Intent to launch an Activity for the tab
		intent = new Intent().setClass(this, EventsActivity.class);
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("home").setIndicator("Home").setContent(intent);
		tabHost.addTab(spec);
		
		// Rinse and Repeat
		intent = new Intent().setClass(this, EventsActivity.class);
		spec = tabHost.newTabSpec("events").setIndicator("Events").setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, TeamsActivity.class);
		spec = tabHost.newTabSpec("teams").setIndicator("Teams").setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, PlayersActivity.class);
		spec = tabHost.newTabSpec("players").setIndicator("Players").setContent(intent);
		tabHost.addTab(spec);
		
		// This should set the tab, upon opening, to the home screen. 
		// Not sure if it's 1 to n or 0 to n
		tabHost.setCurrentTab(1);
	}

}
