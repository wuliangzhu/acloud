package com.android.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DbTools {
	private static final String DATA_BASE = "acloud";
	private static SQLiteDatabase db;
	
	public static SQLiteDatabase createDb(Context context){
		db = new DatabaseHelper(context, DATA_BASE).getWritableDatabase();
		Log.i("ERROR", "create db success:" + db);
		
		return db;
	}	
	
	public static long insert(String table, ContentValues cv){
		return db.insert(table, null, cv);
	}
	
	public static long delete(String table, String whereClause, String[] args){
		return db.delete(table, whereClause, args);
	}
	
	public static long update(String table, ContentValues values, String whereClause, String[] args){
		return db.update(table, values, whereClause, args);
	}
	
	public static String selectBlackList(String phoneNum){
		String phone = null;
		Cursor cursor = db.query("BlackList", new String[]{"phoneNum"}, "phoneNum = ? ", new String[]{phoneNum}, null, null, null, null);
		while (cursor.moveToNext()) {
		      phone = cursor.getString(0);
		}
		cursor.close();
		
		return phone;
	}
	
	public static ArrayList<String> selectAllBlackList(){
		String phone = null;
		ArrayList<String> ret = new ArrayList<String>();
		Cursor cursor = db.query("BlackList", new String[]{"phoneNum"}, null, null, null, null, null, null);
		while (cursor.moveToNext()) {
		      phone = cursor.getString(0);
		      ret.add(phone);
		}
		cursor.close();
		
		return ret;
	}
	
	public static void close(){
		db.close();
	}
}
