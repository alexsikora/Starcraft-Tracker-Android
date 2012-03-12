package illinois.sweng.sctracker;


import org.json.JSONArray;

import android.os.Bundle;

public class PlayerStatusActivity extends DelegateActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playerstatus);

	}

	@Override
	public void handleServerError(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServerResponseData(JSONArray values) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServerResponseMessage(String message) {
		// TODO Auto-generated method stub
	}
}
