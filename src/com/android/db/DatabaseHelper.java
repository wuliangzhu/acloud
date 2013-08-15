package com.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	DatabaseHelper(Context context, String name, CursorFactory cursorFactory,
			int version) {

		super(context, name, cursorFactory, version);

	}
	
	public DatabaseHelper(Context context, String name){
		super(context, name, null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("Create table BlackList(_id INTEGER PRIMARY KEY AUTOINCREMENT, phoneNum TEXT);");
		
		// 信息过期时间
		db.execSQL("Create table HistoryClear(_id INTEGER PRIMARY KEY AUTOINCREMENT, phoneNum TEXT, eTime int(16));");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


	}

	@Override
	public void onOpen(SQLiteDatabase db) {

		super.onOpen(db);


	}

}
