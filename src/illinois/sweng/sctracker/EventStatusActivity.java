package illinois.sweng.sctracker;

// startrack.alexsikora.com
// admin, pw: st4rtrack

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class EventStatusActivity extends Activity {
	static String TAG = "eventStatusActivity";
	EventDatabaseAdapter mEventDBAdapter;

	Object picture = "";
	String name = "";
	String startdate = "";
	String enddate = "";

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventstatus);
		mEventDBAdapter = new EventDatabaseAdapter(this);
		mEventDBAdapter.open();
		Cursor event = mEventDBAdapter.getEvent(1);

		if(event.moveToFirst()) {
			name = event.getString(2);
			startdate = event.getString(3);
			enddate = event.getString(4);

			mEventDBAdapter.close();

			TextView t = (TextView)findViewById(R.id.textView1);
			t.append(name);
			t = (TextView)findViewById(R.id.textView2);
			t.append(startdate);
			t = (TextView)findViewById(R.id.textView3);
			t.append(enddate);
	
		} else {
			Log.d("TAG", "EMPTY EVENT CURSOR - ALL HOPE IS LOST");
		}

	}
}

