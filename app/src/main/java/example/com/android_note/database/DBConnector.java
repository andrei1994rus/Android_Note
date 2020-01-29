package example.com.android_note.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import example.com.android_note.model.Note;

/**
 * This class is used for work with database.
 */
public class DBConnector 
{
	/**
	 * Column "Date".
	 */
	private String COLUMN_DATE="Date";

	/**
	 * Column "Id".
	 */
	private String COLUMN_ID="_id";

	/**
	 * Column "ImagePath".
	 */
	private String COLUMN_IMAGEPATH="PathImage";

	/**
	 * Column "Text".
	 */
	private String COLUMN_TEXT="Text";

	/**
	 * Column "Name".
	 */
	private String DATABASE_NAME="note.db";

	/**
	 * Version of database.
	 */
	private int DATABASE_VERSION=1;

	/**
	 * Number of column "Date".
	 */
	private int NUM_COLUMN_DATE=1;

	/**
	 * Number of column "Id".
	 */
	private int NUM_COLUMN_ID=0;

	/**
	 * Number of column "ImagePath".
	 */
	private int NUM_COLUMN_IMAGEPATH=3;

	/**
	 * Number of column "Text".
	 */
	private int NUM_COLUMN_TEXT=2;

	/**
	 * Name of table.
	 */
	private String TABLE_NAME="Note";

	/**
	 * Object of class SQLiteDatabase. Is used for work with data.
	 */
	private SQLiteDatabase dataBase;

	/** Constructor of class DBConnector.
	 *
	 * @param context
	 * 				 object of class Context. Is used for work with database.
	 *
	 */
	public DBConnector(Context context) 
	{
		OpenHelper openHelper=new OpenHelper(context);
		dataBase=openHelper.getWritableDatabase();
	}

	/**
	 * Deletes note via id
	 *
	 * @param id
	 * 			id of note for SQL-command DELETE.
     *
	 */
	public void delete(long id) 
	{
		dataBase.delete(TABLE_NAME,
						COLUMN_ID+" = ?",
						 new String[]
						 {
						 		String.valueOf(id)
						 });
	}

	/**
	 * Deletes all notes.
	 */
	public void deleteAll() 
	{
		dataBase.delete(TABLE_NAME,null,null);
	}

	/**
	 * Inserts note.
	 *
	 * @param note
	 * 			  object of class Note for SQL-command INSERT.
	 *
	 * @return inserted note via SQL-command INSERT.
     *
	 */
	public long insert(Note note)
	{
		ContentValues cv=new ContentValues();
		cv.put(COLUMN_DATE,note.getDate());
		cv.put(COLUMN_TEXT,note.getText());
		cv.put(COLUMN_IMAGEPATH,note.getImagePath());
		
		return dataBase.insert(TABLE_NAME,null,cv);
	}

	/**
	 * Selects note via id.
	 *
	 * @param id
	 * 			id of note SQL-command SELECT.
	 *
	 * @return selected note via id.
     *
	 */
	public Note select(long id) 
	{
		Cursor mCursor=dataBase.query(TABLE_NAME,
						        	 null,
									 COLUMN_ID+" = ?",
									  new String[]
									  {
									  		String.valueOf(id)
									  },
									null,
								 	null,
									 COLUMN_DATE);
		
		mCursor.moveToFirst();

		String date=mCursor.getString(NUM_COLUMN_DATE);

		String title=mCursor.getString(NUM_COLUMN_TEXT);

		String imagePath=mCursor.getString(NUM_COLUMN_IMAGEPATH);

		return new Note(id,date,title,imagePath);
	}

	/**
	 * Selects all notes.
	 *
	 * @return all selected notes.
     *
	 */
	public ArrayList<Note> selectAll() 
	{
		Cursor cursor=dataBase.query(TABLE_NAME,
								null,
								null,
							null,
								null,
								null,
										COLUMN_DATE);
		
		ArrayList<Note> noteArrayList=new ArrayList<>();

		cursor.moveToFirst();

		if(!cursor.isAfterLast())
		{
			do 
			{
				long id=cursor.getLong(NUM_COLUMN_ID);
				String date=cursor.getString(NUM_COLUMN_DATE);
				String title=cursor.getString(NUM_COLUMN_TEXT);
				String imagePath=cursor.getString(NUM_COLUMN_IMAGEPATH);
				noteArrayList.add(new Note(id,date,title,imagePath));
			} 
			while (cursor.moveToNext());
		}
		return noteArrayList;
	}

	/**
	 * Updates note.
	 *
	 * @param note
	 * 			  object of class Note for SQL-command UPDATE.
	 *
	 * @return updated note.
     *
	 */
	public int update(Note note) 
	{
		ContentValues cv=new ContentValues();
		cv.put(COLUMN_DATE,note.getDate());
		cv.put(COLUMN_TEXT,note.getText());
		cv.put(COLUMN_IMAGEPATH,note.getImagePath());

		return dataBase.update(TABLE_NAME,
								cv,
								COLUMN_ID + " = ?",
								new String[]
								{
										String.valueOf(note.getID())
								});
	}

	/**
	 * This class is used to create and update database.
	 */
	private class OpenHelper extends SQLiteOpenHelper
	{
		/**
		 * Constructor of class OpenHelper.
		 *
		 * @param context
		 * 				 object of class Context. Is used for work with database.
         *
		 */
		OpenHelper(Context context)
		{
			super(context,DATABASE_NAME,null,DATABASE_VERSION);
		}

		/**
		 * Creates database.
		 *
		 * @param db
		 * 			object of class SQLiteDatabase for SQL-command CREATE TABLE.
         *
		 */
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			String query="CREATE TABLE "+TABLE_NAME+" ("+
					COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
					COLUMN_DATE+" TEXT, "+
					COLUMN_TEXT+" TEXT, "+COLUMN_IMAGEPATH+" TEXT); ";

			db.execSQL(query);
		}

		/**
		 * Updates database.
		 *
		 * @param db
		 * 			object of class SQLiteDatabase for SQL-commands: DROP TABLE and CREATE TABLE.
		 *
		 * @param oldVersion
		 * 				    old version.
		 *
		 * @param newVersion
		 * 					new version.
         *
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

			onCreate(db);
		}
	}
}