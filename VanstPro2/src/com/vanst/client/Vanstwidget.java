package com.vanst.client;


import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Vanstwidget extends AppWidgetProvider {
	private static final String TAG="TestAppWidget";  
	  private static final String FRESH="com.sinxiao.app.fresh";  
	   private Context mContext ;  
	   private boolean run = true ;  
	   BroadcastReceiver mBroadcast = new BroadcastReceiver() {      
	  
	     
	  
	      public void onReceive(Context context, Intent intent) {     
	       String action =intent.getAction();  
	      
	       if(action.equals(Intent.ACTION_TIME_TICK)) {  
	     
	       mContext.sendBroadcast(new Intent(FRESH));  
	     
	    }  
	     
	   }

	   };
}
