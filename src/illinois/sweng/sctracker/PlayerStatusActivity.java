package illinois.sweng.sctracker;

import java.util.List;

import org.apache.http.NameValuePair;

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
	public void handleServerResponse(List<NameValuePair> values) {
		// TODO Auto-generated method stub

	}

}
