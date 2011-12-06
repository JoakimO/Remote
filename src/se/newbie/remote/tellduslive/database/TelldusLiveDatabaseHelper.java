package se.newbie.remote.tellduslive.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TelldusLiveDatabaseHelper  extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "telldus_live";

	private static final int DATABASE_VERSION = 1;

	public TelldusLiveDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		TelldusLiveClientTable.onCreate(database);
		TelldusLiveDeviceTable.onCreate(database);
		TelldusLiveJobTable.onCreate(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		TelldusLiveClientTable.onUpgrade(database, oldVersion, newVersion);
		TelldusLiveDeviceTable.onUpgrade(database, oldVersion, newVersion);
		TelldusLiveJobTable.onUpgrade(database, oldVersion, newVersion);
	}
}
