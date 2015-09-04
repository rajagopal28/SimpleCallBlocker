package com.android.simple.call.blocker.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.android.simple.call.blocker.constants.CallBlockerConstants.BLOCK_TYPE;
import com.android.simple.call.blocker.util.OpenHelper;

public class CallBlockerDAO {
	 private static final String DATABASE_NAME = "callblocker.db";
	   private static final int DATABASE_VERSION = 1;
	   private static final String TABLE_NAME = "blockedcalls";
	   private Context context;
	   private OpenHelper openHelper ;
	   private SQLiteStatement insertStmt;
	   private static final String INSERT = "insert into " + TABLE_NAME + "(blockednumber, blocktype) values (?,?)";
	   public CallBlockerDAO(Context context) {	     
		try {
			this.context = context;
			this.openHelper = new OpenHelper(this.context, DATABASE_NAME,
					DATABASE_VERSION, TABLE_NAME);
		} catch (Exception ex) {
			Log.e("CallBlockerDAO", "Exception in constructor " + ex.toString());
			ex.printStackTrace();
		}
	     
	   }
	   public boolean addToBlockedList(String blockedNumber, BLOCK_TYPE blockType) {
		   boolean isSuccess = false;
		   if(!getAllBlockedNumbers().contains(blockedNumber)) {
			   SQLiteDatabase db = this.openHelper.getWritableDatabase();
			   Log.i("SMSStatistisDAO", "Inside Insertion block. First time msg");
			   this.insertStmt = db.compileStatement(INSERT);
			   this.insertStmt.bindString(1, blockedNumber);
			   this.insertStmt.bindString(2, blockType.toString());
			   long newRowId = this.insertStmt.executeInsert();
			   isSuccess = newRowId != -1;	// -1 indicates insertion failed		
			   db.close();
		   }
		   return isSuccess;		   
	   }
	   public List<String> getAllBlockedNumbers(){
		   List<String> blockedNumbers = new ArrayList<String>();
		   try {
			   SQLiteDatabase db = this.openHelper.getReadableDatabase();
			 
			      Cursor cursor = db.query(TABLE_NAME, new String[] { "blockednumber" },
			      null, null, null, null, "id asc");
			      if (cursor.moveToFirst()) {
			         do {
			        	String blockedNumber = cursor.getString(0);// parameterIndex starts at 0
			        	blockedNumbers.add(blockedNumber);
			         } while (cursor.moveToNext());
			         cursor.close();
			      }
			      db.close();
			     } catch(Exception ex) {
			    	 Log.w("CallBlockerDAO", ex.fillInStackTrace());
			     }
		   return blockedNumbers;
	   }
	   public synchronized List<String> getBlockedCallsofType(BLOCK_TYPE blockType){
		   List<String> blockedNumbers = new ArrayList<String>();
		   try {
			   	SQLiteDatabase db = this.openHelper.getReadableDatabase();
			      Cursor cursor = db.query(TABLE_NAME, new String[] { "blockednumber" },
			      "blocktype = ?", new String[] {blockType.toString()}, null, null, "id asc");
			      if (cursor.moveToFirst()) {
			         do {
			        	String blockedNumber = cursor.getString(0);// parameterIndex starts at 0
			        	blockedNumbers.add(blockedNumber);
			         } while (cursor.moveToNext());
			         cursor.close();
			      }
			      db.close();
			     } catch(Exception ex) {
			    	 Log.w("CallBlockerDAO", ex.fillInStackTrace());
			     }
		   return blockedNumbers;
	   }
	   
	   public boolean removeSelectedNumbers(List<String> removeList) {
		   boolean isSuccess = true;
		   SQLiteDatabase db = this.openHelper.getWritableDatabase();
		   for(String item : removeList) {
			   isSuccess &= db.delete(TABLE_NAME, "blockednumber LIKE ?", new String[] {item+"%"})>0;
			}
		   db.close();
		   return isSuccess;
	   }
}
