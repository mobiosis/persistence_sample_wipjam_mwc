package com.mobiosis.persistencesample.data;

import java.sql.SQLException;
import java.util.HashMap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mobiosis.persistencesample.model.MyUser;

public class SQLiteHelper 
	//extends SQLiteOpenHelper 
	extends OrmLiteSqliteOpenHelper
{
	
	private static final String DATABASE_NAME = "sample_db";
	private enum DbVersion {
		Preinit, //ignore, we just need to start from 1
		Init,
//		Update1, //added column "country"
//		Update2,
		
		//insert next versions above
		CurrentVersion
	}
	private static final int DATABASE_CURRENT_VERSION = DbVersion.CurrentVersion.ordinal()-1;
	
	public static HashMap<String, String> sUsersColumns  = new HashMap<String, String>();

	private static volatile Object mSQLiteMutex = new Object();

	private static SQLiteHelper mSQLiteHelper;
	
	static {
		sUsersColumns.put(BaseColumns._ID, BaseColumns._ID);
		sUsersColumns.put(MyUser.USER_NAME, MyUser.USER_NAME);
		sUsersColumns.put(MyUser.AGE, MyUser.AGE);
		sUsersColumns.put(MyUser.GENDER, MyUser.GENDER);
		sUsersColumns.put(MyUser.COUNTRY, MyUser.COUNTRY);
	}

	public enum DatabaseType {
		USERS("vnd.android.cursor.dir/vnd.example.users");
		
		private String mType;
		DatabaseType(String type) {
			mType = type;
		}
		
		public String getType() {
			return mType;
		}
	}
	
	public static SQLiteHelper getInstance(Context applicationContext) {
		synchronized (mSQLiteMutex) {
			if (mSQLiteHelper == null)
				mSQLiteHelper = new SQLiteHelper(applicationContext);
		}
		return mSQLiteHelper;

	}

	
	private SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_CURRENT_VERSION);
	}

//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		db.execSQL( "CREATE TABLE " + DatabaseType.USERS.name() /*this is the name of the table*/ + " ("
//				+ BaseColumns._ID 					+ " INTEGER PRIMARY KEY,"
//				+ MyUser.USER_NAME 			+ " TEXT,"
//				+ MyUser.AGE       			+ " INTEGER,"
//				+ MyUser.GENDER			+ " INTEGER"
//				+ ")" );
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		if (oldVersion == DbVersion.Init.ordinal()) {
//			db.beginTransaction();
//
//			//add new column
//			db.execSQL("ALTER TABLE " + DatabaseType.USERS.name() /*this is the name of the table*/  
//					+ " ADD COLUMN "+ MyUser.COUNTRY +" TEXT default 'Poland'"
//					);
//			
//			db.setTransactionSuccessful();
//			db.endTransaction();
//
//		}
//	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, MyUser.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion,
			int newVersion) {
		if (oldVersion == DbVersion.Init.ordinal()) {
			db.beginTransaction();

			//add new column
			db.execSQL("ALTER TABLE " + MyUser.class.getSimpleName().toLowerCase() /*this is the name of the table*/  
					+ " ADD COLUMN "+ MyUser.COUNTRY +" TEXT default 'Lalaland'"
					);
			
			db.setTransactionSuccessful();
			db.endTransaction();

		}
	}
	
	public Dao<MyUser, ?> getUsers() throws SQLException {
			return getDao(MyUser.class);
	}

}
