package com.wfour.onlinestoreapp.datastore.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * create database which stores all data
 * 
 * @author
 */
public class DataBaseHelper extends SQLiteOpenHelper implements IDatabaseConfig {

	private static final String TAG = "DatabaseHelper";
	private Context mContext;
	private String mNameDb;
	private SQLiteDatabase mDatabase;
	private String DB_PATH;

	public DataBaseHelper(Context context, String nameDb) {
		super(context, nameDb, null, DATABASE_VERSION);
		this.mContext = context;
		this.mNameDb = nameDb;
		DB_PATH = "data/data/" + mContext.getPackageName() + "/";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	public void copyDataBase() throws IOException {
		InputStream mInput = null;
		try {
			/**
			 * Open your local db as the input stream
			 */
			mInput = mContext.getAssets().open(mNameDb);

			/**
			 * Path to the just created empty db
			 */
			String outFileName = DB_PATH + mNameDb;

			/**
			 * Open the empty db as the output stream
			 */
			OutputStream mOutput = new FileOutputStream(outFileName);

			/**
			 * transfer bytes from the inputfile to the outputfile
			 */
			byte[] buffer = new byte[1024];
			int length;
			while ((length = mInput.read(buffer)) > 0) {
				mOutput.write(buffer, 0, length);
			}
			mOutput.flush();
			mOutput.close();
			mInput.close();
			Log.i(TAG, "New database created...");

		} catch (IOException e) {
			Log.e(TAG, "Could not create new database...");
			e.printStackTrace();
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + mNameDb;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {
			Log.v(TAG, "Database not exist");
			e.printStackTrace();
		}

		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		if (!dbExist) {
			/**
			 * By calling this method and empty database will be created into
			 * the default system path of your application so we are gonna be
			 * able to overwrite that database with our database.
			 **/
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * open database
	 * 
	 * @throws SQLException
	 */
	public void openDataBase() throws SQLException {
		/**
		 * Open the database
		 */
		String mPath = DB_PATH + mNameDb;
		mDatabase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READWRITE);
	}

	@Override
	public synchronized void close() {
		if (mDatabase != null) {
			mDatabase.close();
		}
		super.close();
	}

	/**
	 * Query table
	 * 
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 */
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
						String having, String orderBy) {
		Cursor cursor = this.mDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

		return cursor;

	}

	public Cursor rawQuery(String sql) {
		Cursor cursor = mDatabase.rawQuery(sql, null);
		return cursor;
	}

	/**
	 * Updates value for table in database
	 * 
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		return this.mDatabase.update(table, values, whereClause, whereArgs);
	}

	public SQLiteDatabase getmDatabase() {
		return mDatabase;
	}

	public void setmDatabase(SQLiteDatabase mDatabase) {
		this.mDatabase = mDatabase;
	}

}
