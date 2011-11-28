package se.newbie.remote.tellduslive.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TelldusLiveClientTable {
	private static final String TAG = "TelldusLiveClientTable";
	private static final String DATABASE_CREATE = "create table teldus_live_client "
			+ "("
			+ "_id integer primary key autoincrement"
			+ ",client_id integer null"
			+ ",uuid text null"
			+ ",name text null"
			+ ",online text null"
			+ ",editable integer null"
			+ ",version text null"
			+ ",type text null"
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
