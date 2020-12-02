package com.vanst.client;

import android.util.Log;

public class MyLog {
	final static boolean flag = false;
	static void i(String tag,String msg){
		if(flag) Log.i(tag, msg);
	}
}
