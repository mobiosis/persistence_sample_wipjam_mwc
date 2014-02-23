package com.mobiosis.persistencesample.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.mobiosis.persistencesample.data.SQLiteHelper.DatabaseType;
import com.mobiosis.persistencesample.model.MyUser;

public class SampleProvider extends ContentProvider {
	
	public static final String AUTHORITY = SampleProvider.class.getName();
	private SQLiteHelper mSQLiteHelper;

	private static UriMatcher sUriMatcher = 
			new UriMatcher(DatabaseType.USERS.ordinal());
	static {
		sUriMatcher.addURI(AUTHORITY, 
				DatabaseType.USERS.toString(), 
				DatabaseType.USERS.ordinal());
	}
	
	@Override
	public boolean onCreate() {
		mSQLiteHelper = SQLiteHelper.getInstance(
				getContext().getApplicationContext());
		return true;
	}
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		int typeId = sUriMatcher.match(uri);
		if (typeId != -1) {
			DatabaseType dt = DatabaseType.values()[typeId];
			return dt.getType();
		}
		return "";
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		long rowId = -1;
		ContentValues values;
		SQLiteDatabase db = mSQLiteHelper.getWritableDatabase();

		if (initialValues != null)
			values = new ContentValues(initialValues);
		else
			values = new ContentValues();
		
		int typeId = sUriMatcher.match(uri);
		if (typeId != -1) {
			DatabaseType dt = DatabaseType.values()[typeId];
			switch(dt) {
				case USERS:
					rowId = db.insert(DatabaseType.USERS.name(), 
							MyUser.USER_NAME, values);
					break;
			}
		}

		if (rowId > 0)
		{//inform observers about the change in the database
			Uri uriRowInserted = ContentUris.withAppendedId(
					uri, rowId);
			getContext().getContentResolver().notifyChange(
					uriRowInserted, null);
			return uriRowInserted;
		}
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mSQLiteHelper.getWritableDatabase();

		Cursor c = null;
		
		int typeId = sUriMatcher.match(uri);
		if (typeId != -1) {
			DatabaseType dt = DatabaseType.values()[typeId];
			switch(dt) {
				case USERS:
					c = db.query(DatabaseType.USERS.name(), 
							projection, selection, selectionArgs,
							null /*groupBy*/, null/*having*/, sortOrder);
					break;
			}
		}

		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static Uri getUri() {
		return Uri.parse("content://" + SampleProvider.class.getName());
	}

}
