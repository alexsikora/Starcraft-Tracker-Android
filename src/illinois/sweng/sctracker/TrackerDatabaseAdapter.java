package illinois.sweng.sctracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TrackerDatabaseAdapter {
	
	
	private static final String TAG = "TrackerDatabaseAdapter";
	
	//COLUMNS OF PLAYER DATABASE
	public static final String KEY_ROWID = "_id";
	public static final String KEY_PK = "pk";
	public static final String KEY_PICTURE = "picture";
	public static final String KEY_HANDLE = "handle";
	public static final String KEY_NAME = "name";
	public static final String KEY_RACE = "race";
	public static final String KEY_TEAM = "team";
	public static final String KEY_NATIONALITY = "nationality";
	public static final String KEY_ELO = "elo";
	
	//DATABASE INFORMATION
	private static final String DATABASE_NAME = "TrackerDatabase";
	private static final String DATABASE_PLAYER_TABLE = "players";
	private static final int DATABASE_VERSION = 1;
	
	//STRING TO CREATE PLAYER TABLE
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
    
    /**
     * Constructor for the adapter; takes a context, sets a databasehelper.
     * @param context Context of the activity creating the adapter.
     */
	public TrackerDatabaseAdapter(Context context) {
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
	        db.execSQL(CREATE_PLAYER_TABLE);
	    }

	    /**
	     * Performs necessary actions if the database is upgraded
	     */
		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// TODO Auto-generated method stub
			// Do whatever we need to do to upgrade...
			db.execSQL("DROP TABLE IF EXISTS players");
            onCreate(db);
		}
	}
	
	/**
	 * Retrieves a database for writing
	 * @return The TrackerDatabaseAdapter, with the database open to read/write operations.
	 */
	public TrackerDatabaseAdapter open() {
		mDatabase = mDatabaseHelper.getWritableDatabase();
		Log.d(TAG, "TrackerDatabase opened");
		return this;
	}
	
	/**
	 * Closes the database object.
	 */
	public void close() {
		mDatabaseHelper.close();
		Log.d(TAG, "TrackerDatabase closed");
	}
	
	/**
	 * Retrieves a cursor across all the players in the database.
	 * @return A cursor across all players in the database.
	 */
	public Cursor getAllPlayers() {
		String[] columns = new String[] { KEY_ROWID, KEY_PK, KEY_PICTURE,
				KEY_HANDLE, KEY_NAME, KEY_RACE, KEY_TEAM, KEY_NATIONALITY,
				KEY_ELO };
		return mDatabase.query(DATABASE_PLAYER_TABLE, columns, null, null,
				null, null, KEY_NAME + " ASC");
	}
	
	/**
	 * getPlayer returns a player from the database based upon the supplied rowid.
	 * @param rowid Integer specifying the rowid of the player in the database.
	 * @return A cursor over the player returned from the database.
	 */
	public Cursor getPlayer(int rowid) {
		return mDatabase.query(DATABASE_PLAYER_TABLE,
				new String[] {KEY_ROWID, KEY_PICTURE, KEY_HANDLE, KEY_NAME,
					KEY_RACE, KEY_TEAM, KEY_NATIONALITY, KEY_ELO},
				KEY_ROWID + "=" + rowid, null, null, null, null);
	}
	
	/**
	 * getPlayerByPK finds a player based upon their (as guaranteed by the server)
	 * unique PK.
	 * @param pk The integer PK that uniquely identifies players (As used in serverside database).
	 * @return A cursor over the player found.
	 */
	public Cursor getPlayerByPK(int pk) {
		return mDatabase.query(DATABASE_PLAYER_TABLE,
				new String[] {KEY_ROWID, KEY_PICTURE, KEY_HANDLE, KEY_NAME,
					KEY_RACE, KEY_TEAM, KEY_NATIONALITY, KEY_ELO},
				KEY_PK + "=" + pk, null, null, null, null);
	}
	
	/**
	 * Updates an individual player's data with the database.
	 * @param pk The integer PK that can be used to uniquely identify players (as guaranteed by server).
	 * @param playerData The JSONObject containing the data about the player.
	 * @return Boolean; true if the operation has succeeded, false otherwise.
	 */
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

	/**
	 * Updates the data in the database based upon the players supplied by the server.
	 * @param players A JSONArray containing the JSONObjects for each player.
	 * @return True if the operation succeeded, false otherwise.
	 */
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
	
	/**
	 * Checks to see whether a player with the supplied pk exists in the database.
	 * @param pk An integer that uniquely identifies a player (as guaranteed by the server).
	 * @return True if the database contains the player; false otherwise.
	 */
	public boolean hasPlayer(int pk) {
		Cursor playerCursor = mDatabase.query(DATABASE_PLAYER_TABLE,
				new String[] {KEY_ROWID, KEY_PICTURE, KEY_HANDLE, KEY_NAME,
				KEY_RACE, KEY_TEAM, KEY_NATIONALITY, KEY_ELO},
			KEY_PK + "=" + pk, null, null, null, null);
		return (playerCursor.getCount() == 1);
	}
	
	/**
	 * Inserts a player into the database.
	 * @param pk Integer that uniquely identifies a player (as guaranteed by the server).
	 * @param playerData The JSONObject containing the information about the player to insert.
	 * @return True if the operation succeeds; false otherwise.
	 */
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
