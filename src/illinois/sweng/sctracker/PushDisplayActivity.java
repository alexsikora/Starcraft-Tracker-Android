package illinois.sweng.sctracker;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;

public class PushDisplayActivity extends Activity implements DelegateActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventstatus);
	}

	public void handleServerError(String message) {
		// TODO Auto-generated method stub

	}

	public void handleServerResponseData(JSONArray values) {
		// TODO Auto-generated method stub

	}

	public void handleServerResponseMessage(String message) {
		// TODO Auto-generated method stub

	}

}
