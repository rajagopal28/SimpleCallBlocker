package com.android.simple.call.blocker.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OpenHelper extends SQLiteOpenHelper {
	private String tableName;
    public OpenHelper(Context context, String databaseName, int databaseVersion, String tableName) {
       super(context, databaseName, null, databaseVersion);
   	this.tableName = tableName;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("CREATE TABLE " + tableName + "(id INTEGER PRIMARY KEY, blockednumber TEXT , blocktype TEXT NOT NULL)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       Log.w("OpenHelper", "Upgrading database, this will drop tables and recreate.");
       db.execSQL("DROP TABLE IF EXISTS " + tableName);
       onCreate(db);
    }
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
    
 }