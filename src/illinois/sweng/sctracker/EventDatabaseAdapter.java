package illinois.sweng.sctracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventDatabaseAdapter {
	private static final String TAG = "EventDatabaseAdapater";

	//COLUMNS OF EVENT DATABASE
	public static final String KEY_ROWID = "_id";
	public static final String KEY_PK = "pk";
	public static final String KEY_PICTURE = "picture";
	public static final String KEY_NAME = "name";
	public static final String KEY_STARTDATE = "startdate";
	public static final String KEY_ENDDATE = "enddate";

	//DATABASE INFORMATION
	private static final String DATABASE_NAME = "EventDatabase";
	private static final String DATABASE_EVENT_TABLE = "events";
	private static final int DATABASE_VERSION = 1;

	//STRING TO CREATE EVENT TABLE
	private static final String CREATE_EVENT_TABLE =
			"create table " + DATABASE_EVENT_TABLE + " ( " 
			+ KEY_ROWID + " integer primary key autoincrement, "
			+ KEY_PK + " integer, "
			+ KEY_PICTURE + " text, "
			+ KEY_NAME + " text not null, "
			+ KEY_STARTDATE + " text not null, "
			+ KEY_ENDDATE + " text not null);";

	private final Context mContext;
	private DatabaseHelper mDatabaseHelper;
	private SQLiteDatabase mDatabase;

	/**
	 * Constructor for the adapter; takes a context, sets a database helper.
	 * @param context Context of the activity creating the adapter.
	 */
	public EventDatabaseAdapter(Context context) {
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
			db.execSQL(CREATE_EVENT_TABLE);
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
	public EventDatabaseAdapter open() {
		mDatabase = mDatabaseHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Closes the database object.
	 */
	public void close() {
		mDatabaseHelper.close();
	}

	/**
	 * Retrieves a cursor across all the events in the database.
	 * @return A cursor across all events in the database.
	 */
	public Cursor getAllEvents() {
		return mDatabase.query(DATABASE_EVENT_TABLE, 
				new String[] {KEY_ROWID, KEY_NAME, KEY_STARTDATE, KEY_ENDDATE},
				null, null, null, null, KEY_NAME+" ASC");
	}

	/**
	 * getEvent returns a event from the database based upon the supplied rowid.
	 * @param rowid Integer specifying the rowid of the event in the database.
	 * @return A cursor over the event returned from the database.
	 */
	public Cursor getEvent(int rowid) {
		return mDatabase.query(DATABASE_EVENT_TABLE,
				new String[] {KEY_ROWID, KEY_PICTURE, KEY_NAME, KEY_STARTDATE, KEY_ENDDATE},
				KEY_ROWID + "=" + rowid, null, null, null, null);
	}

	/**
	 * getEventByEK finds an event based upon their (as guaranteed by the server)
	 * unique EK.
	 * @param pk The integer PK that uniquely identifies events (As used in serverside database).
	 * @return A cursor over the event found.
	 */
	public Cursor getEventByEK(int pk) {
		return mDatabase.query(DATABASE_EVENT_TABLE,
				new String[] {KEY_ROWID, KEY_PICTURE, KEY_NAME, KEY_STARTDATE, KEY_ENDDATE},
				KEY_PK + "=" + pk, null, null, null, null);
	}

	/**
	 * Updates an individual event's data with the database.
	 * @param pk The integer PK that can be used to uniquely identify events (as guaranteed by server).
	 * @param eventData The JSONObject containing the data about the events.
	 * @return Boolean; true if the operation has succeeded, false otherwise.
	 */
	public boolean updateEvent(int pk, JSONObject eventData) {
		ContentValues data = new ContentValues();
		try{
			data.put(KEY_PICTURE, eventData.getString("picture").toString());
			data.put(KEY_NAME, eventData.getString("name").toString());
			data.put(KEY_STARTDATE, eventData.getString("startdate").toString());
			data.put(KEY_ENDDATE, eventData.getString("enddate").toString());

			return mDatabase.update(DATABASE_EVENT_TABLE, data, 
					KEY_PK + "=" + pk, null) > 0;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Updates the data in the database based upon the events supplied by the server.
	 * @param events A JSONArray containing the JSONObjects for each event.
	 * @return True if the operation succeeded, false otherwise.
	 */
	public boolean updateDatabase(JSONArray events) {
		int numEvents = events.length();
		try{
			for(int i = 0; i < numEvents; i++) {
				JSONObject event = (JSONObject) events.get(i);
				int pk = event.getInt("pk");
				if (hasEvent(pk)) {
					updateEvent(pk, event.getJSONObject("fields"));
				} else {
					insertEvent(pk, event.getJSONObject("fields"));
				}
			}
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * Checks to see whether an event with the supplied ek exists in the database.
	 * @param ek An integer that uniquely identifies a player (as guaranteed by the server).
	 * @return True if the database contains the player; false otherwise.
	 */
	public boolean hasEvent(int pk) {
		Cursor eventCursor = mDatabase.query(DATABASE_EVENT_TABLE,
				new String[] {KEY_ROWID, KEY_PICTURE, KEY_NAME, KEY_STARTDATE, KEY_ENDDATE},
				KEY_PK + "=" + pk, null, null, null, null);
		return (eventCursor.getCount() == 1);
	}

	/**
	 * Inserts an event into the database.
	 * @param pk Integer that uniquely identifies an event (as guaranteed by the server).
	 * @param eventData The JSONObject containing the information about the event to insert.
	 * @return True if the operation succeeds; false otherwise.
	 */
	public boolean insertEvent(int pk, JSONObject eventData) {
		ContentValues data = new ContentValues();
		try{
			data.put(KEY_PK, pk);
			data.put(KEY_PICTURE, eventData.getString("picture").toString());
			data.put(KEY_NAME, eventData.getString("name").toString());
			data.put(KEY_STARTDATE, eventData.getString("startdate").toString());
			data.put(KEY_ENDDATE, eventData.getString("enddate").toString());

			return mDatabase.insert(DATABASE_EVENT_TABLE, null, data) > 0; 
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
}
