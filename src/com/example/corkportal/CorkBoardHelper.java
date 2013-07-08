package com.example.corkportal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log; 


class CorkboardHelper extends SQLiteOpenHelper{
	private static final String TAG = "DatabaseAdapter";
	
	public static final String KEY_ROWID = "_id";
	
	public static final String TABLE_CORKBOARDS = "Corkboards"; 
	   	public static final String CORKBOARDS_KEY_CORKBOARD = "corkboard";
       	public static final String CORKBOARDS_KEY_CBDATA = "cb_info";
	
	private static final String DATABASE_CREATE_CORKBOARDS_TABLE = 
			"create table " + TABLE_CORKBOARDS + " (" 
					+ KEY_ROWID + " integer primary key autoincrement, " 
					+ CORKBOARDS_KEY_CORKBOARD + " text not null, "
					+ CORKBOARDS_KEY_CBDATA + " text not null)";

	private static final String[] CORKBOARD_COLUMNS = new String[]{
		KEY_ROWID, CORKBOARDS_KEY_CBDATA, CORKBOARDS_KEY_CORKBOARD
	};
	
	private static final String DATABASE_NAME = "Corkboards.db";
	private static final int SCHEMA_VERSION = 1; 
	
	public CorkboardHelper(Context context){
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onOpen(db);
		db.execSQL(DATABASE_CREATE_CORKBOARDS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CORKBOARDS);
        onCreate(db);
	}
	
	public void insert(String corkboard, String cb_info){
		try{	
			Log.d(TAG, corkboard + " and " + cb_info);
			ContentValues cv = new ContentValues(); 
			cv.put("corkboard", corkboard);
			cv.put("cb_info", cb_info);
			getWritableDatabase().insert("Corkboards", null, cv);
		}catch (Exception e) {
			Log.e("ERROR", "ERROR AT: " + e.toString()); 
		}
	}
	
	public Cursor getAll(){
		Cursor c = getReadableDatabase().query(TABLE_CORKBOARDS, CORKBOARD_COLUMNS, "", null, null, null, null);
		if (c != null)
			c.moveToFirst();
		return c;
	}
	
	public String getName(Cursor c){
		return(c.getString(c.getColumnIndex(CORKBOARDS_KEY_CORKBOARD))); 
	}
	
	public Cursor getCorkboard(long id) {
		Cursor c = getReadableDatabase().query(TABLE_CORKBOARDS, CORKBOARD_COLUMNS, KEY_ROWID + " = " + id, null, null, null, null);
		if (c != null)
			c.moveToFirst();
		return c;
	}		
}