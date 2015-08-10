package com.ha.halauncher.model;

import java.util.LinkedList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBModel extends SQLiteOpenHelper{

	private static final String TABLE_WIDGETS = "widgets";

	// Widgets Table Columns names
    private static final String KEY_ID = "id";
	private static final String KEY_TOPX = "topx";
	private static final String KEY_TOPY = "topy";

    private static final String[] COLUMNS = {KEY_ID, KEY_TOPX, KEY_TOPY};

	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "HaLauncherDB";

	public DBModel(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
    // SQL statement to create Widget table
		String CREATE_WIDGET_TABLE = "CREATE TABLE WIDGETS ( " + "id INTEGER, " + "topx INTEGER, "+ "topy INTEGER )";
	
		// create Widgets table
		db.execSQL(CREATE_WIDGET_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older Widgets table if existed
		db.execSQL("DROP TABLE IF EXISTS WIDGETS");
	
		// create fresh Widgets table
		this.onCreate(db);
   }
	//---------------------------------------------------------------------
	
    /**
	  * CRUD operations (create "add", read "get", update, delete) Widget + get all Widgets + delete all Widgets
	  */

	public void addWidget(Widget widget){
		Log.d("addWidget", widget.toString());
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		
		// 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
		values.put(KEY_ID, widget.getId()); 
		values.put(KEY_TOPX, widget.getTopX()); 
		values.put(KEY_TOPY, widget.getTopY()); 
	    // 3. insert
		db.insert(TABLE_WIDGETS, // table
				null, //nullColumnHack
				values); // key/value -> keys = column names/ values = column values
	
		// 4. close
		db.close(); 
	}
	
    public Widget getWidget(int id){

		// 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
	
		// 2. build query
		Cursor cursor = 
		db.query(TABLE_WIDGETS, // a. table
                 COLUMNS, // b. column names
                 " id = ?", // c. selections 
                 new String[] { String.valueOf(id) }, // d. selections args
                 null, // e. group by
                 null, // f. having
                 null, // g. order by
                 null); // h. limit
	
		// 3. if we got results get the first one
		if (cursor != null)
			cursor.moveToFirst();
		
		// 4. build Widget object
		Widget widget = new Widget();
		widget.setId(Integer.parseInt(cursor.getString(0)));
		widget.setTopX(Integer.parseInt(cursor.getString(1)));
		widget.setTopY(cursor.getInt(2));
		
		Log.d("getWidget("+id+")", widget.toString());
		
		// 5. return Widget
		return widget;
     }
	
	// Get All Widgets
	public List<Widget> getAllWidgets() {
		List<Widget> widgets = new LinkedList<Widget>();
		
		// 1. build the query
		String query = "SELECT  * FROM " + TABLE_WIDGETS;
	
		// 2. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		// 3. go over each row, build Widget and add it to list
		Widget widget = null;
		if (cursor.moveToFirst()) {
			do {
				widget = new Widget();
				widget.setId(Integer.parseInt(cursor.getString(0)));
				widget.setTopX(cursor.getInt(1));
				widget.setTopY(cursor.getInt(2));
				
				// Add Widget to Widgets
				widgets.add(widget);
				} while (cursor.moveToNext());
			}
		
		Log.d("getAllWidgets()", widgets.toString());
		
		// return Widgets
		return widgets;
		}
	
	// Updating single Widget
	public int updateWidget(Widget widget) {
		// 1. get reference to writable DB
       SQLiteDatabase db = this.getWritableDatabase();
		
		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put("topx", widget.getTopX()); // get title 
		values.put("toopy", widget.getTopY()); // get author
		
		// 3. updating row
		int i = db.update(TABLE_WIDGETS, //table
				values, // column/value
				KEY_ID+" = ?", // selections
				new String[] { String.valueOf(widget.getId()) }); //selection args
		
		// 4. close
		db.close();
		
		return i;
     }
	
	 // Deleting single Widget
	 public void deleteWidget(Widget Widget) {
	
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		
		// 2. delete
		db.delete(TABLE_WIDGETS, KEY_ID+" = ?", new String[] { String.valueOf(Widget.getId()) });
		
		// 3. close
		db.close();
		
		Log.d("deleteWidget", Widget.toString());
		
     }

}
