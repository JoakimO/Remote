package se.newbie.remote.tellduslive.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TelldusLiveDeviceAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_DEVICE_ID = "device_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_STATE = "state";
	public static final String KEY_STATE_VALUE = "state_value";
	public static final String KEY_METHOD = "method";
	public static final String KEY_CLIENT = "client";
	public static final String KEY_CLIENT_NAME = "client_name";
	public static final String KEY_ONLINE = "online";
	public static final String KEY_EDITABLE = "editable";	
	
	private static final String DB_TABLE = "teldus_live_device";
	
	private static final String[] TABLE_FIELDS = new String[] { KEY_ROWID, KEY_DEVICE_ID, KEY_NAME, KEY_STATE, KEY_STATE_VALUE, KEY_METHOD, KEY_CLIENT, KEY_CLIENT_NAME, KEY_ONLINE, KEY_EDITABLE};
	
	private Context context;
	private SQLiteDatabase db;
	private TelldusLiveDatabaseHelper dbHelper;	
	
	public TelldusLiveDeviceAdapter(Context context) {
		this.context = context;
	}

	public TelldusLiveDeviceAdapter open() throws SQLException {
		dbHelper = new TelldusLiveDatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}	
	
	private ContentValues createContentValues(long deviceId, String name, long state, long stateValue, long method, String client, String clientName, String online, long editable) {
		ContentValues values = new ContentValues();
		values.put(KEY_DEVICE_ID, deviceId);
		values.put(KEY_NAME, name);
		values.put(KEY_STATE, state);
		values.put(KEY_STATE_VALUE, stateValue);
		values.put(KEY_METHOD, method);
		values.put(KEY_CLIENT, client);
		values.put(KEY_CLIENT_NAME, clientName);
		values.put(KEY_ONLINE, online);
		values.put(KEY_EDITABLE, editable);
		return values;
	}	

	public long createTelldusLiveDevice(long deviceId, String name, long state, long stateValue, long method, String client, String clientName, String online, long editable) {
		ContentValues values = createContentValues(deviceId, name, state, stateValue, method, client, clientName, online, editable);
		return db.insert(DB_TABLE, null, values);
	}
	
	public boolean updateTelldusLiveDevice(long rowId, long deviceId, String name, long state, long stateValue, long method, String client, String clientName, String online, long editable) {
		ContentValues values = createContentValues(deviceId, name, state, stateValue, method, client, clientName, online, editable);
		return db.update(DB_TABLE, values, KEY_ROWID + "=" + rowId, null) > 0;
	}	
	
	public boolean deleteTelldusLiveDevice(long rowId) {
		return db.delete(DB_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public Cursor fetchTelldusLiveDevice() {
		return db.query(DB_TABLE, TABLE_FIELDS, null, null, null, null, null);
	}	
	
	public Cursor fetchTelldusLiveDevice(long rowId) throws SQLException {
		Cursor cursor = db.query(true, DB_TABLE, TABLE_FIELDS, KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}	
	
	public Cursor fetchTelldusLiveDeviceByDeviceId(long deviceId) throws SQLException {
		Cursor cursor = db.query(true, DB_TABLE, TABLE_FIELDS, KEY_DEVICE_ID + "=" + deviceId, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}	
}
