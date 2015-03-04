package com.alang.winwificonnector;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class WifiAuthContent extends ContentProvider {

	public static final String AUTHORITY = "com.alang.winwificonnector.data";
	private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
	
	public static final Uri CONTENT_URI = BASE_URI;

	private WifiAuthDatabase wifiDb;
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = wifiDb.getWritableDatabase();
		int count = db.delete(wifiDb.getTableName(uri), selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		return WifiAuthDatabase.WIFI_HOSTS_CONTENT_TYPE_DIR;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// don't allow inserts for now
		return null;
	}

	@Override
	public boolean onCreate() {
		wifiDb = WifiAuthDatabase.getInstance(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = wifiDb.getReadableDatabase();
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(wifiDb.getTableName (uri));
		return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// don't allow updates for now
		return 0;
	}

}

