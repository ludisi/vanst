package com.vanst.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class WidgetActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent vanstservice = new Intent("android.intent.action.vanstservice");
        startService(vanstservice);
		if(!Connect.flag){
//			if(Connect.isEnter || StartActivity.isStart){
				moveTaskToBack(true);
				if(!Connect.widgetConnect && Connect.isEnter){
					Connect.widgetConnect = true;
					try {
						if(!Connect.is3G)
			    			new Connect(Connect.activity,false);
			    			else new Connect(Connect.activity,Connect._3GIp,false);
					} catch (Exception e) {
						Connect.widgetConnect = false;
					}
					
				}else {
					Intent intent = getPackageManager().getLaunchIntentForPackage( 
	                        "com.vanst.client"); 
	                startActivity(intent); 
//					Toast.makeText(this, "”¶”√Œ¥∆Ù∂Ø£°", Toast.LENGTH_SHORT).show();
				}
//			}else{
//				if(!Connect.widgetConnect){
//					if(Connect.activity != null) Connect.activity.finish();
//					Connect.widgetConnect = true;
//					Intent intent = new Intent(this,StartActivity.class);
//					intent.putExtra("widget", true);
//					startActivity(intent);
//				}
//			}
		}else{
			moveTaskToBack(true);
		}
		finish();
	}
}
