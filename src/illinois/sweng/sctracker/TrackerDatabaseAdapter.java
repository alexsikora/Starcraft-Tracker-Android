package illinois.sweng.sctracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TrackerDatabaseAdapter {
	
	private static final String TAG = "TrackerDatabaseAdapter";
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_PK = "pk";
	public static final String KEY_PICTURE = "picture";
	public static final String KEY_HANDLE = "handle";
	public static final String KEY_NAME = "name";
	public static final String KEY_RACE = "race";
	public static final String KEY_TEAM = "team";
	public static final String KEY_NATIONALITY = "nationality";
	public static final String KEY_ELO = "elo";
	
	private static final String DATABASE_NAME = "TrackerDatabase";
	private static final String DATABASE_PLAYER_TABLE = "players";
	private static final int DATABASE_VERSION = 1;
	
    private static final String CREATE_PLAYER_TABLE =
            "create table " + DATABASE_PLAYER_TABLE + " ( " 
            + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_PK + " integer, "
            + KEY_PICTURE + " text, "
            + KEY_HANDLE + " text not null, "
            + KEY_NAME + " text not null, "
            + KEY_RACE + " text not null, "
            + KEY_TEAM + " text, "
            + KEY_NATIONALITY + " text not null, "
            + KEY_ELO + " text not null);";
    
    private final Context mContext;
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    
	public TrackerDatabaseAdapter(Context context) {
		this.mContext = context;
		mDatabaseHelper = new DatabaseHelper(mContext);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

	    DatabaseHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        db.execSQL(CREATE_PLAYER_TABLE);
	    }

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// TODO Auto-generated method stub
			// Do whatever we need to do to upgrade...
			db.execSQL("DROP TABLE IF EXISTS players");
            onCreate(db);
		}
	}
	
	public TrackerDatabaseAdapter open() {
		mDatabase = mDatabaseHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		mDatabaseHelper.close();
	}
	
	public Cursor getAllPlayers() {
		return mDatabase.query(DATABASE_PLAYER_TABLE, 
				new String[] {KEY_ROWID, KEY_NAME, KEY_RACE},
				null, null, null, null, KEY_NAME+" ASC");
	}
	
	public Cursor getPlayer(int rowid) {
		return mDatabase.query(DATABASE_PLAYER_TABLE,
				new String[] {KEY_ROWID, KEY_PICTURE, KEY_HANDLE, KEY_NAME,
					KEY_RACE, KEY_TEAM, KEY_NATIONALITY, KEY_ELO},
				KEY_ROWID + "=" + rowid, null, null, null, null);
	}
	
	public Cursor getPlayerByPK(int pk) {
		return mDatabase.query(DATABASE_PLAYER_TABLE,
				new String[] {KEY_ROWID, KEY_PICTURE, KEY_HANDLE, KEY_NAME,
					KEY_RACE, KEY_TEAM, KEY_NATIONALITY, KEY_ELO},
				KEY_PK + "=" + pk, null, null, null, null);
	}
	public boolean updatePlayer(int pk, JSONObject playerData) {
		ContentValues data = new ContentValues();
		try{
			data.put(KEY_PICTURE, playerData.getString("picture").toString());
			data.put(KEY_HANDLE, playerData.getString("handle").toString());
			data.put(KEY_NAME, playerData.getString("name").toString());
			data.put(KEY_RACE, playerData.getString("race").toString());
			data.put(KEY_TEAM, playerData.getString("team").toString());
			data.put(KEY_NATIONALITY, playerData.getString("nationality").toString());
			data.put(KEY_ELO, playerData.getString("elo").toString());
			
	        return mDatabase.update(DATABASE_PLAYER_TABLE, data, 
                    KEY_PK + "=" + pk, null) > 0;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateDatabase(JSONArray players) {
		int numPlayers = players.length();
		try{
			for(int i = 0; i < numPlayers; i++) {
				JSONObject player = (JSONObject) players.get(i);
				int pk = player.getInt("pk");
				if (hasPlayer(pk)) {
					updatePlayer(pk, player.getJSONObject("fields"));
				} else {
					insertPlayer(pk, player.getJSONObject("fields"));
				}
			}
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean hasPlayer(int pk) {
		Cursor playerCursor = mDatabase.query(DATABASE_PLAYER_TABLE,
				new String[] {KEY_ROWID, KEY_PICTURE, KEY_HANDLE, KEY_NAME,
				KEY_RACE, KEY_TEAM, KEY_NATIONALITY, KEY_ELO},
			KEY_PK + "=" + pk, null, null, null, null);
		if(playerCursor.getCount() == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean insertPlayer(int pk, JSONObject playerData) {
		ContentValues data = new ContentValues();
		try{
			data.put(KEY_PK, pk);
			data.put(KEY_PICTURE, playerData.getString("picture").toString());
			data.put(KEY_HANDLE, playerData.getString("handle").toString());
			data.put(KEY_NAME, playerData.getString("name").toString());
			data.put(KEY_RACE, playerData.getString("race").toString());
			data.put(KEY_TEAM, playerData.getString("team").toString());
			data.put(KEY_NATIONALITY, playerData.getString("nationality").toString());
			data.put(KEY_ELO, playerData.getString("elo").toString());
			
	        return mDatabase.insert(DATABASE_PLAYER_TABLE, null, data) > 0; 
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
}
