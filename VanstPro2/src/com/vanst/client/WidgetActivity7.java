package com.vanst.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class WidgetActivity7 extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent vanstservice = new Intent("android.intent.action.vanstservice");
        startService(vanstservice);
		moveTaskToBack(true);
		if(Connect.isEnter){
			if(MainView.isChanged && MainActivity.flag == 2 || MainActivity.widgetbusy || MainActivity.widgetroom) {
				if(MainActivity.widgetbusy){
					Toast.makeText(this, "正在控制中，请稍候。。。", Toast.LENGTH_SHORT).show();
				}
			}else{
	//			if(progressDialog != null) progressDialog.dismiss();
	//			progressDialog = new ProgressDialog(MainActivity.this);
	//			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	//			progressDialog.setMessage("正在加载，请稍后...");
	//			progressDialog.setCancelable(false);
	//			progressDialog.show();
				new Thread(){
					public void run() {
						if(MainActivity.mainView !=null){
							MainActivity.widgetroom = true;
							if(MainActivity.mainView.refreshThread !=null){
								MainActivity.mainView.refreshThread.isRun = false;
								MainActivity.handler.sendEmptyMessage(MainActivity.UPDATA_WIDGET);
								MainView.roomNum++;
			//					if(MainView.bitmap!=null&&!MainView.bitmap.isRecycled())
			//					MainView.bitmap.recycle();
								if(MainView.roomNum > Room.count) MainView.roomNum = 1;
						    	Config.askConfig(Config.readConfig(MainView.roomNum), MainActivity.mainView);
						    	MainActivity.mainView.repaintRoom();
						    	if(MainActivity.mainView.refreshThread !=null)
						    	MainActivity.mainView.refreshThread.isRun = true;
								try {
									sleep(300);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								MainActivity.handler.sendEmptyMessage(MainActivity.UPDATA_WIDGET);
								if(MainActivity.flag == 1) Connect.getPose((byte)0);
							}
							MainActivity.widgetroom = false;
						}
					};
				}.start();
			}
		}else{
//			Toast.makeText(this, "应用未启动！", Toast.LENGTH_SHORT).show();
			Intent intent = getPackageManager().getLaunchIntentForPackage( 
                    "com.vanst.client"); 
            startActivity(intent); 
		}
		finish();
	}
}
