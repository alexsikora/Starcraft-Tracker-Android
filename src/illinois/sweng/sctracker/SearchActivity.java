package illinois.sweng.sctracker;

import org.json.JSONObject;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

public class SearchActivity extends ListActivity {

	ServerCommunicator mServerCommunicator;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		// Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
//	      JSONObject allData = mServerCommunicator.sendGetAllRequest(userpass);
	      //executeSearch(query);
	    }

	}
	
}
