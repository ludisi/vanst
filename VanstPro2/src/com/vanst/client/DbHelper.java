package com.vanst.client;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	public DbHelper(Context context, String name) {
		super(context, name, null, 1);
		MyLog.i("dbhelper","db");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql="create table devices(" +
		"id integer primary key autoincrement," +
		"type,index1,pose,value2,value3,remark,room,offsetx,offsety);";
		db.execSQL(sql);
		MyLog.i("oncreate","db");

		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
