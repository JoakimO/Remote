package se.newbie.remote.tellduslive.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TelldusLiveDeviceTable {
	private static final String TAG = "TelldusLiveDeviceTable";
	private static final String DATABASE_CREATE = "create table teldus_live_device "
			+ "("
			+ "_id integer primary key autoincrement"
			+ ",device_id integer null"
			+ ",name text null"
			+ ",state integer null"
			+ ",state_value integer null"
			+ ",methods integer null"
			+ ",client text null"
			+ ",client_name text null"
			+ ",online text null"
			+ ",editable integer null"			
			+ ");";

	public static void onCreate(SQLiteDatabase database) {
		Log.v(TAG, "Create database");
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.v(TAG, "Updating database from " + oldVersion + " to " + newVersion);
	}
}
