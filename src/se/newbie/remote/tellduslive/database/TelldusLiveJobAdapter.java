package se.newbie.remote.tellduslive.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TelldusLiveJobAdapter {
	private static final String TAG = "TelldusLiveClientAdapter";
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_JOB_ID = "job_id";
	public static final String KEY_DEVICE_ID = "device_id";
	public static final String KEY_METHOD = "method";
	public static final String KEY_METHOD_VALUE = "method_value";
	public static final String KEY_NEXT_RUN_TIME = "next_run_time";
	public static final String KEY_TYPE = "type";
	public static final String KEY_HOUR = "hour";
	public static final String KEY_MINUTE = "minute";
	public static final String KEY_OFFSET = "offset";
	public static final String KEY_RANDOM_INTERVAL = "random_interval";
	public static final String KEY_RETRIES = "retries";
	public static final String KEY_RETRY_INTERVAL = "retry_interval";
	public static final String KEY_WEEK_DAYS = "week_days";
	
	private static final String DB_TABLE = "teldus_live_job";
	
	private static final String[] TABLE_FIELDS = new String[] { KEY_ROWID, KEY_JOB_ID, KEY_DEVICE_ID, KEY_METHOD, KEY_METHOD_VALUE, KEY_NEXT_RUN_TIME, KEY_TYPE, KEY_HOUR, KEY_MINUTE, KEY_OFFSET, KEY_RANDOM_INTERVAL, KEY_RETRIES, KEY_RETRY_INTERVAL, KEY_WEEK_DAYS };

	private Context context;
	private SQLiteDatabase db;
	private TelldusLiveDatabaseHelper dbHelper;	
	
	public TelldusLiveJobAdapter(Context context) {
		this.context = context;
	}

	public TelldusLiveJobAdapter open() throws SQLException {
		dbHelper = new TelldusLiveDatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}	
	
	private ContentValues createContentValues(long jobId, long deviceId, String method, String methodValue, long nextRunTime,String type, long hour, long minute, long offset, long randomInterval, long retries, long retryInterval, String weekDays) {
		ContentValues values = new ContentValues();
		values.put(KEY_JOB_ID, jobId);
		values.put(KEY_DEVICE_ID, deviceId);
		values.put(KEY_METHOD, method);
		values.put(KEY_METHOD_VALUE, methodValue);
		values.put(KEY_NEXT_RUN_TIME, nextRunTime);
		values.put(KEY_TYPE, type);
		values.put(KEY_HOUR, hour);
		values.put(KEY_MINUTE, minute);
		values.put(KEY_OFFSET, offset);
		values.put(KEY_RANDOM_INTERVAL, randomInterval);
		values.put(KEY_RETRIES, retries);
		values.put(KEY_RETRY_INTERVAL, retryInterval);
		values.put(KEY_WEEK_DAYS, weekDays);
		return values;
	}	

	public long createTelldusLiveJob(long jobId, long deviceId, String method, String methodValue, long nextRunTime,String type, long hour, long minute, long offset, long randomInterval, long retries, long retryInterval, String weekDays) {
		Log.v(TAG, "createTelldusLiveJob: " + jobId);
		ContentValues values = createContentValues(jobId, deviceId, method, methodValue, nextRunTime, type, hour, minute, offset, randomInterval, retries, retryInterval, weekDays);
		return db.insert(DB_TABLE, null, values);
	}
	
	public boolean updateTelldusLiveJob(long rowId, long jobId, long deviceId, String method, String methodValue, long nextRunTime,String type, long hour, long minute, long offset, long randomInterval, long retries, long retryInterval, String weekDays) {
		Log.v(TAG, "createTelldusLiveJob: " + jobId);
		ContentValues values = createContentValues(jobId, deviceId, method, methodValue, nextRunTime, type, hour, minute, offset, randomInterval, retries, retryInterval, weekDays);
		return db.update(DB_TABLE, values, KEY_ROWID + "=" + rowId, null) > 0;
	}	
	
	public boolean deleteTelldusLiveJob(long rowId) {
		return db.delete(DB_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public Cursor fetchTelldusLiveJobs() {
		return db.query(DB_TABLE, TABLE_FIELDS, null, null, null, null, null);
	}	
	
	public Cursor fetchTelldusLiveJob(long rowId) throws SQLException {
		Cursor cursor = db.query(true, DB_TABLE, TABLE_FIELDS, KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}	
	
	public Cursor fetchTelldusLiveJobByJobId(long jobId) throws SQLException {
		Cursor cursor = db.query(true, DB_TABLE, TABLE_FIELDS, KEY_JOB_ID + "=" + jobId, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}	
}
