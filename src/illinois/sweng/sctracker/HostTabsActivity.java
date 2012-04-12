package illinois.sweng.sctracker;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;


public class HostTabsActivity extends TabActivity{
	static String TAG = "hostActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.host);
		
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent; 
		
		intent = new Intent().setClass(this, HomeActivity.class);
		spec = tabHost.newTabSpec("home").setIndicator("Home").setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, PlayerListActivity.class);
		spec = tabHost.newTabSpec("players").setIndicator("Players").setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, TeamListActivity.class);
		spec = tabHost.newTabSpec("teams").setIndicator("Teams").setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, EventListActivity.class);
		spec = tabHost.newTabSpec("events").setIndicator("Events").setContent(intent);
		tabHost.addTab(spec);
		
		// This should set the tab, upon opening, to the home screen. 
		tabHost.setCurrentTab(0);
	}

}
