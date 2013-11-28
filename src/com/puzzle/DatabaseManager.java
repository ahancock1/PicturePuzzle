package com.puzzle;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager {

	// the Activity or Application that is creating an object from this class.
	Context context;

	private SQLiteDatabase db; // a reference to the database manager class.
	private final String DB_NAME = "game_scores"; // the name of our database
	private final int DB_VERSION = 1; // the version of the database

	// the names for our database columns
	private final String TABLE_NAME = "level_scores";
	private final String TABLE_ROW_ID = "game_id";
	private final String TABLE_ROW_ONE = "game_score";

	public DatabaseManager(Context context) {
		this.context = context;

		// create or open the databse
		CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
		this.db = helper.getWritableDatabase();
	}

	// the beginnings our SQLiteOpenHelper class
	private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {

		public CustomSQLiteOpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// the SQLite query string that will create a 2 column database
			// table.
			String newTableQueryString = "create table " + TABLE_NAME + " ("
					+ TABLE_ROW_ID
					+ " integer primary key autoincrement not null,"
					+ TABLE_ROW_ONE + " integer" + ");";

			// execute the query string to the database.
			db.execSQL(newTableQueryString);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			// NOTHING TO DO HERE. THIS IS THE ORIGINAL DATABASE VERSION.
			// OTHERWISE, YOU WOULD SPECIFIY HOW TO UPGRADE THE DATABASE
			// FROM OLDER VERSIONS.
		}
	}

	/**
	 * ADDING A ROW TO THE DATABASE TABLE
	 * 
	 * the key is automatically assigned by the database
	 * 
	 * @param rowStringOne
	 *            the value for the row's first column
	 * @param rowStringTwo
	 *            the value for the row's second column
	 */
	public void addRow(String rowStringOne) {
		// this is a key value pair holder used by android's SQLite functions
		ContentValues values = new ContentValues();

		// this is how you add a value to a ContentValues object
		// we are passing in a key string and a value string each time
		values.put(TABLE_ROW_ONE, rowStringOne);

		// ask the database object to insert the new data
		try {
			db.insert(TABLE_NAME, null, values);
		} catch (Exception e) {
			Log.e("DB ERROR", e.toString()); // prints the error message to the
												// log
			e.printStackTrace(); // prints the stack trace to the log
		}
	}

	/**
	 * DELETING A ROW FROM THE DATABASE TABLE
	 * 
	 * @param rowID
	 *            the SQLite database identifier for the row to delete.
	 */
	public void deleteRow(long rowID) {
		// ask the database manager to delete the row of given id
		try {
			db.delete(TABLE_NAME, TABLE_ROW_ID + "=" + rowID, null);
		} catch (Exception e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * UPDATING A ROW IN THE DATABASE TABLE
	 * 
	 * @param rowID
	 *            the SQLite database identifier for the row to update.
	 * @param rowStringOne
	 *            the new value for the row's first column
	 * @param rowStringTwo
	 *            the new value for the row's second column
	 */
	public void updateRow(long rowID, String rowStringOne) {
		// this is a key value pair holder used by android's SQLite functions
		ContentValues values = new ContentValues();
		values.put(TABLE_ROW_ONE, rowStringOne);

		// ask the database object to update the database row of given rowID
		try {
			db.update(TABLE_NAME, values, TABLE_ROW_ID + "=" + rowID, null);
		} catch (Exception e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * RETRIEVING A ROW FROM THE DATABASE TABLE
	 * 
	 * @param rowID
	 *            the id of the row to retrieve
	 * @return an array containing the data from the row
	 */
	public ArrayList<Object> getRowAsArray(long rowID) {
		// create an array list to store data from the database row.
		ArrayList<Object> rowArray = new ArrayList<Object>();
		Cursor cursor;

		try {
			// this method call is spread out over seven lines as a personal
			// preference
			cursor = db.query(TABLE_NAME, new String[] { TABLE_ROW_ID,
					TABLE_ROW_ONE }, TABLE_ROW_ID + "=" + rowID, null, null,
					null, null, null);

			// move the pointer to position zero in the cursor.
			cursor.moveToFirst();

			// if there is data available after the cursor's pointer, add
			// it to the ArrayList that will be returned by the method.
			if (!cursor.isAfterLast()) {
				do {
					rowArray.add(cursor.getLong(0));
					rowArray.add(cursor.getString(1));

				// try to move the cursor's pointer forward one position.
				} while (cursor.moveToNext());
			}
			
			// let java know that you are through with the cursor.
			cursor.close();
		} catch (SQLException e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}

		return rowArray;
	}
	
	/**
	 * RETRIEVING ALL ROWS FROM THE DATABASE TABLE
	 * 
	 * the key is automatically assigned by the database
	 */
	public ArrayList<ArrayList<Object>> getAllRowsAsArrays()
	{
		// create an ArrayList that will hold all of the data collected from
		// the database.
		ArrayList<ArrayList<Object>> dataArrays =
			new ArrayList<ArrayList<Object>>();
	 
		// this is a database call that creates a "cursor" object.
		// the cursor object store the information collected from the
		// database and is used to iterate through the data.
		Cursor cursor;
	 
		try
		{
			// ask the database object to create the cursor.
			cursor = db.query(
					TABLE_NAME,
					new String[]{TABLE_ROW_ID, TABLE_ROW_ONE},
					null, null, null, null, null
			);
	 
			// move the cursor's pointer to position zero.
			cursor.moveToFirst();
	 
			// if there is data after the current cursor position, add it
			// to the ArrayList.
			if (!cursor.isAfterLast())
			{
				do
				{
					ArrayList<Object> dataList = new ArrayList<Object>();
	 
					dataList.add(cursor.getLong(0));
					dataList.add(cursor.getString(1));
	 
					dataArrays.add(dataList);
				}
				// move the cursor's pointer up one position.
				while (cursor.moveToNext());
			}
		}
		catch (SQLException e)
		{
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
	 
		// return the ArrayList that holds the data collected from
		// the database.
		return dataArrays;
	}
	
	/**
	 * CHECK TO SEE IF TABLE EXISTS
	 * 
	 * if table exists 1 is returned else 0 for not exists
	 */
	public boolean isTableExists()
	{
	    if (TABLE_NAME == null || db == null || !db.isOpen())
	    {
	        return false;
	    }
	    Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", TABLE_NAME});
	    if (!cursor.moveToFirst())
	    {
	        return false;
	    }
	    int count = cursor.getInt(0);
	    cursor.close();
	    return count > 0;
	}

}
