package illinois.sweng.sctracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TeamsDBAdapter {

	private static final String TAG = "TeamsDatabaseAdapter";
	
	//COLUMNS OF PLAYER DATABASE
	public static final String KEY_ROWID = "_id";
	public static final String KEY_PK = "pk";
	public static final String KEY_NAME = "name";
	public static final String KEY_TAG = "tag";
	
	//DATABASE INFORMATION
	private static final String DATABASE_NAME = "TrackerDatabase";
	public static final String DATABASE_TEAM_TABLE = "teams";
	private static final int DATABASE_VERSION = 1;
	
	//STRING TO CREATE PLAYER TABLE
    private static final String CREATE_TEAM_TABLE =
            "create table " + DATABASE_TEAM_TABLE + " ( " 
            + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_PK + " integer, "
            + KEY_NAME + " text not null, "
            + KEY_TAG + " text not null);";
    
    private final Context mContext;
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    
    /**
     * Constructor for the adapter; takes a context, sets a databasehelper.
     * @param context Context of the activity creating the adapter.
     */
	public TeamsDBAdapter(Context context) {
		this.mContext = context;
		mDatabaseHelper = new DatabaseHelper(mContext);
	}

	/**
	 * Static DatabaseHelper; used to create tables and get database
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		/**
		 * Constructor for DatabaseHelper
		 * @param context Context of the activity creating the database; mContext in this scenario
		 */
	    DatabaseHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }

	    /**
	     * Has the database execute table creation statements if necessary
	     * @param db Database to execute the SQL
	     */
	    @Override
	    public void onCreate(SQLiteDatabase db) {
//	        db.execSQL(CREATE_TEAM_TABLE);
	    }

	    /**
	     * Performs necessary actions if the database is upgraded
	     */
		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// TODO Auto-generated method stub
			// Do whatever we need to do to upgrade...
//			db.execSQL("DROP TABLE IF EXISTS players");
//            onCreate(db);
		}
	}
	
	public TeamsDBAdapter open() {
		mDatabase = mDatabaseHelper.getWritableDatabase();
		return this;
	}
	
	/**
	 * Closes the database object.
	 */
	public void close() {
		mDatabaseHelper.close();
	}
	
	public Cursor getTeam(int rowid) {
		return mDatabase.query(DATABASE_TEAM_TABLE,
				new String[] {KEY_ROWID, KEY_PK, KEY_NAME,
					KEY_TAG},
				KEY_ROWID + "=" + rowid, null, null, null, null);
	}
	
	public Cursor getTeamByPK(int pk) {
		return mDatabase.query(DATABASE_TEAM_TABLE,
				new String[] {KEY_ROWID, KEY_PK, KEY_NAME,
					KEY_TAG},
				KEY_PK + "=" + pk, null, null, null, null);
	}
	
	public Cursor getAllTeams() {
		return mDatabase.query(DATABASE_TEAM_TABLE, 
				new String[] {KEY_ROWID, KEY_PK, KEY_NAME, KEY_TAG},
				null, null, null, null, KEY_NAME+" ASC");
	}
	
	public boolean updateDatabase(JSONArray teams) {
		int numPlayers = teams.length();
		try{
			for(int i = 0; i < numPlayers; i++) {
				JSONObject team = (JSONObject) teams.get(i);
				int pk = team.getInt("pk");
				if (hasTeam(pk)) {
					updateTeam(pk, team.getJSONObject("fields"));
				} else {
					insertTeam(pk, team.getJSONObject("fields"));
				}
			}
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean hasTeam(int pk) {
		Cursor teamCursor = mDatabase.query(DATABASE_TEAM_TABLE,
				new String[] {KEY_ROWID, KEY_PK, KEY_NAME, KEY_TAG},
			KEY_PK + "=" + pk, null, null, null, null);
		return (teamCursor.getCount() == 1);
	}
	
	public boolean updateTeam(int pk, JSONObject teamData) {
		ContentValues data = new ContentValues();
		try{
			data.put(KEY_NAME, teamData.getString("name").toString());
			data.put(KEY_TAG, teamData.getString("tag").toString());
	        return mDatabase.update(DATABASE_TEAM_TABLE, data, 
                    KEY_PK + "=" + pk, null) > 0;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean insertTeam(int pk, JSONObject teamData) {
		ContentValues data = new ContentValues();
		try{
			data.put(KEY_PK, pk);
			data.put(KEY_NAME, teamData.getString("name").toString());
			data.put(KEY_TAG, teamData.getString("tag").toString());
	        return mDatabase.insert(DATABASE_TEAM_TABLE, null, data) > 0; 
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
}