package com.vanst.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbControl {
	DbHelper dbHelper;
	public DbControl(Activity activity){
		MyLog.i("dbcontrol","db");
		dbHelper = new DbHelper(activity, "device.db");
	}
	public long insert(Device device){
		long id = -1;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("type", device.type);
		values.put("index1", device.index);
		values.put("pose", device.pose);
		values.put("value2", device.value2);
		values.put("value3", device.value3);
		values.put("remark", device.remark);
		values.put("room", device.room);
		values.put("offsetx", device.offsetX);
		values.put("offsety", device.offsetY);
		try {
			id = db.insert("devices", null, values);
			db.close();
		} catch (Exception e) {
			MyLog.i("dbinsert","error");
		}
		
		return id;
	}
	public int delete(){
		int row = 0;
		try {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			row = db.delete("devices", null	, null);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return row;
	}
	public int delete(Device device){
		int row = 0;
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		row=db.delete("devices", "id=?", new String[]{device.id+""});
		db.close();
		return row;
	}
	
	public int update(Device device){
		int row=0;
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("type", device.type);
		values.put("index1", device.index);
		values.put("pose", device.pose);
		values.put("value2", device.value2);
		values.put("value3", device.value3);
		values.put("remark", device.remark);
		values.put("room", device.room);
		values.put("offsetx", device.offsetX);
		values.put("offsety", device.offsetY);

		/**
		 * table����
		 * values ��ֵ����
		 * whereClause where�Ӿ�
		 * whereArgs where�Ӿ����
		 * ���� ��Ӱ������� 0��ʾû���޸�
		 */
		try {
			row=db.update("devices", values, "id=?", new String[]{device.id+""});
			db.close();
		} catch (Exception e) {
			MyLog.i("dbupdate","error");
		}
		
		return row;
	}
	public List<Map<String,Object>> queryAll(){
		List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();
		try {
		
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		/**
		boolean distinct, true��������ͬ��¼ Ĭ��false  select distinct name from student
		String table, ����
		String[] columns,��ѯ�������� null��ʾ��ѯ������ �� new String[]{"_id","name","cname","age","score"}
        String selection, where�Ӿ䲻��Ҫ����where�ؼ���  age>? and score<?
        String[] selectionArgs, �滻��һ������where�Ӿ�?ռλ�� new String[]{25+"",60+""};
        String groupBy,��������
        String having, ������������
        String orderBy,order by����  "name desc,score asc"
         String limit ����ǰlimit����� "10"
		 */
		Cursor cursor=db.query("devices", null, null,null,null,null,null);
		while(cursor.moveToNext()){
			Map<String,Object> map=new HashMap<String, Object>();
			long id = cursor.getLong(cursor.getColumnIndex("id"));
			int type = cursor.getInt(cursor.getColumnIndex("type"));
			int index = cursor.getInt(cursor.getColumnIndex("index1"));
			int pose = cursor.getInt(cursor.getColumnIndex("pose"));
			int value2 = cursor.getInt(cursor.getColumnIndex("value2"));
			int value3 = cursor.getInt(cursor.getColumnIndex("value3"));
			int room = cursor.getInt(cursor.getColumnIndex("room"));
			int offsetX = cursor.getInt(cursor.getColumnIndex("offsetx"));
			int offsetY = cursor.getInt(cursor.getColumnIndex("offsety"));
			String remark = cursor.getString(cursor.getColumnIndex("remark"));
			map.put("id", id);
			map.put("type",type	);
			map.put("index",index);
			map.put("pose", pose);
			map.put("value2", value2);
			map.put("value3", value3);
			map.put("remark", remark);
			map.put("room", room);
			map.put("offsetx", offsetX);
			map.put("offsety", offsetY);
			data.add(map);
		}
		db.close();
		cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return data;
		
	}
	
	
}
