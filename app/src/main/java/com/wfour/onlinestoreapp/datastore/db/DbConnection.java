package com.wfour.onlinestoreapp.datastore.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wfour.onlinestoreapp.globals.Constants;

import java.io.IOException;
import java.util.HashMap;


/**
 * Class connects to Database, get all data here
 *
 * 
 */
public class DbConnection implements IDatabaseConfig {

	private Context mContext;
	private DataBaseHelper mDBHelper;

	public DbConnection(Context mContext) {
		this.mContext = mContext;
		this.mDBHelper = new DataBaseHelper(this.mContext, DATABASE_NAME);
	}

	/**
	 * close database
	 */
	private void close() {
		mDBHelper.close();
	}

	/**
	 * open and connect to database
	 */
	private void openAndConnectDB() {
		try {
			mDBHelper.createDataBase();
			mDBHelper.openDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void closeDB() {
		this.mDBHelper.close();
	}

	private void execSQL(String sqlQuery) {
		mDBHelper.getmDatabase().execSQL(sqlQuery);
	}

	public HashMap<String,String> getAllCachingTime(){
		this.openAndConnectDB();
		HashMap<String, String> cachingTimes = new HashMap<>();
		String response = "";
		String query = "SELECT * FROM " + TABLE_CACHING ;
		Cursor mCursor = this.mDBHelper.rawQuery(query);
		if (mCursor.moveToFirst()){
			do {
				cachingTimes.put(mCursor.getString(1),mCursor.getString(3));
			}while (mCursor.moveToNext());
		}

		return cachingTimes;
	}

	/**
	 * **************************************************************** Caching
	 */
	public String getCaching(String request){
		this.openAndConnectDB();
		String response = "";
		String query = "SELECT * FROM " + TABLE_CACHING + " WHERE "
				+  Constants.Caching.KEY_REQUEST + " = '%s' " ;
		Cursor mCursor = this.mDBHelper.rawQuery(String.format(query,request));
		if (mCursor.moveToFirst()){
			response = mCursor.getString(2);
		}

		return response;
	}

	public boolean saveCaching(String request, String response, String timeUpdated){
		synchronized (this) {
			openAndConnectDB();

			ContentValues contentValues = new ContentValues();
			contentValues.put(Constants.Caching.KEY_REQUEST, request);
			contentValues.put(Constants.Caching.KEY_RESPONSE, response);
			contentValues.put(Constants.Caching.KEY_TIME_UPDATED, timeUpdated);

			long result = mDBHelper.getmDatabase().insertWithOnConflict(TABLE_CACHING, null,
					contentValues, SQLiteDatabase.CONFLICT_REPLACE);
			closeDB();
			return (result >= 0) ? true : false;
		}
	}



}
