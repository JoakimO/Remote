package se.newbie.remote.tellduslive.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TelldusLiveClientAdapter {
	private static final String TAG = "TelldusLiveClientAdapter";
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_CLIENT_ID = "client_id";
	public static final String KEY_UUID = "uuid";
	public static final String KEY_NAME = "name";
	public static final String KEY_ONLINE = "online";
	public static final String KEY_EDITABLE = "editable";
	public static final String KEY_VERSION = "version";
	public static final String KEY_TYPE = "type";
	
	private static final String DB_TABLE = "teldus_live_client";
	
	private static final String[] TABLE_FIELDS = new String[] { KEY_ROWID, KEY_CLIENT_ID, KEY_UUID, KEY_NAME, KEY_ONLINE, KEY_EDITABLE, KEY_VERSION, KEY_TYPE };

	private Context context;
	private SQLiteDatabase db;
	private TelldusLiveDatabaseHelper dbHelper;	
	
	public TelldusLiveClientAdapter(Context context) {
		this.context = context;
	}

	public TelldusLiveClientAdapter open() throws SQLException {
		dbHelper = new TelldusLiveDatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}	
	
	private ContentValues createContentValues(long clientId, String uuid, String name, String online, long editable, String version, String type) {
		ContentValues values = new ContentValues();
		values.put(KEY_CLIENT_ID, clientId);
		values.put(KEY_UUID, uuid);
		values.put(KEY_NAME, name);
		values.put(KEY_ONLINE, online);
		values.put(KEY_EDITABLE, editable);
		values.put(KEY_VERSION, version);
		values.put(KEY_TYPE, type);
		return values;
	}	

	public long createTelldusLiveClient(long clientId, String uuid, String name, String online, long editable, String version, String type) {
		Log.v(TAG, "createTelldusLiveClient: " + clientId);
		ContentValues values = createContentValues(clientId, uuid, name, online, editable, version, type);
		return db.insert(DB_TABLE, null, values);
	}
	
	public boolean updateTelldusLiveClient(long rowId, long clientId, String uuid, String name, String online, long editable, String version, String type) {
		Log.v(TAG, "updateTelldusLiveClient: " + clientId);
		ContentValues values = createContentValues(clientId, uuid, name, online, editable, version, type);
		return db.update(DB_TABLE, values, KEY_ROWID + "=" + rowId, null) > 0;
	}	
	
	public boolean deleteTelldusLiveClient(long rowId) {
		return db.delete(DB_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public Cursor fetchTelldusLiveClients() {
		return db.query(DB_TABLE, TABLE_FIELDS, null, null, null, null, null);
	}	
	
	public Cursor fetchTelldusLiveClient(long rowId) throws SQLException {
		Cursor cursor = db.query(true, DB_TABLE, TABLE_FIELDS, KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}	
	
	public Cursor fetchTelldusLiveClientByClientId(long clientId) throws SQLException {
		Cursor cursor = db.query(true, DB_TABLE, TABLE_FIELDS, KEY_CLIENT_ID + "=" + clientId, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}	
}
