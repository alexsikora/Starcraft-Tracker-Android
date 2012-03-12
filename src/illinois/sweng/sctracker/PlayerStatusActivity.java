package illinois.sweng.sctracker;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayerStatusActivity extends Activity {
	static String TAG = "playerStatusActivity";
	String data;
	JSONObject player;
	JSONObject playerData;
	
	String handle = "";
	Object picture = "";
	String name = "";
	String race = "";
	String team = "";
	String nationality = "";
	String elo;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.playerstatus);
        
        Bundle extras = getIntent().getExtras();
        if(extras == null)
        	return; // might want to log here - indicate extras was never assigned any data
        
        else {
	        data = extras.getString("player");
	        try{
	        	player = new JSONObject(data);
	        	playerData = player.getJSONObject("fields");
	        	
	        	handle = playerData.getString("handle").toString();
	        	//picture = playerData.getJSONObject("picture").toString();
	        	name = playerData.getString("name").toString();
	        	race = playerData.getString("race").toString();
	        	team = playerData.getString("team").toString();
	        	nationality = playerData.getString("nationality").toString();
	        	elo = playerData.getInt("elo") + "";
	        	
	        } catch (JSONException e){
	        	e.printStackTrace();
	        }
	        
	        TextView t = (TextView)findViewById(R.id.textView1);
	        t.append(handle);
	        t = (TextView)findViewById(R.id.textView2);
	        t.append(name);
	        t = (TextView)findViewById(R.id.textView3);
	        t.append(race);
	        t = (TextView)findViewById(R.id.textView4);
	        t.append(team);
	        t = (TextView)findViewById(R.id.textView5);
	        t.append(nationality);
	        t = (TextView)findViewById(R.id.textView6);
	        t.append(elo);
        }
        
	}

}
