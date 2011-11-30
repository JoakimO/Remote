package se.newbie.remote.tellduslive.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TelldusLiveJobTable {
	private static final String TAG = "TelldusLiveJobTable";
	private static final String DATABASE_CREATE = "create table teldus_live_job "
			+ "("
			+ "_id integer primary key autoincrement"
			+ ",job_id integer null"
			+ ",device_id integer null"
			+ ",method text null"
			+ ",method_value text null"
			+ ",next_run_time long null"
			+ ",type text null"
			+ ",hour integer null"
			+ ",minute integer null"
			+ ",offset integer null"
			+ ",random_interval integer null"
			+ ",retries integer null"
			+ ",retry_interval integer null"
			+ ",week_days text null"
			+ ");";

	public static void onCreate(SQLiteDatabase database) {
		Log.v(TAG, "Create database");
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.v(TAG, "Updating database from " + oldVersion + " to " + newVersion);
		database.execSQL(DATABASE_CREATE);
	}
}
