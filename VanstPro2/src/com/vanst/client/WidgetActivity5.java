package com.vanst.client;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class WidgetActivity5 extends Activity {
	ProgressDialog progressDialog;
	int getPoseTypes;
	Handler getposeHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(progressDialog != null)progressDialog.setProgress(msg.what);
			VanstWidgetProvider.progress = msg.what;
			VanstWidgetProvider.updateAppWidget(WidgetActivity5.this);
		};
	};
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(progressDialog != null)progressDialog.dismiss();
			MainActivity.widgetbusy = false;
			VanstWidgetProvider.updateAppWidget(WidgetActivity5.this);
			finish();
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent vanstservice = new Intent("android.intent.action.vanstservice");
        startService(vanstservice);
		moveTaskToBack(true);
		if(Connect.isEnter && Connect.flag && !MainActivity.widgetbusy && !MainActivity.widgetroom){
			MainActivity.widgetbusy = true;
			for(Device device:MainView.devices){
				if(device.visible && (device.moshi3 == 0 || device.moshi3 == 1)){
					getPoseTypes++;
				}
			}
			VanstWidgetProvider.max = getPoseTypes;
			VanstWidgetProvider.progress = 0;
			refreshProgress();
			new AskMode().start();
		}else{
			if(MainActivity.widgetbusy){
				Toast.makeText(this, "正在控制中，请稍后在试。。。", Toast.LENGTH_SHORT).show();
			}else if(MainActivity.widgetroom){
				Toast.makeText(this, "正在切换房间中，请稍后在试。。。", Toast.LENGTH_SHORT).show();
			}else
			Toast.makeText(this, "服务器未连接", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
		
	
	private void refreshProgress(){
		if(Connect.flag){
			progressDialog = new ProgressDialog(this);   
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);   
			progressDialog.setTitle("刷新状态中！");   
			progressDialog.setIcon(android.R.drawable.ic_menu_rotate);   
			progressDialog.setMessage("请稍等。。");   
			progressDialog.setMax(getPoseTypes);   
			progressDialog.setProgress(0);   
			progressDialog.setIndeterminate(false);   
			progressDialog.setCancelable(false);
			progressDialog.show();
		}
	}
	class AskMode extends Thread{
		boolean sflag = false;
		public void run() {
			int j = 0;
			for(Device device:MainView.devices){
				if(device.visible && (device.moshi3 == 0 || device.moshi3 == 1)){
					int time = 0;
					while(true){
						if(device.type == Device.DEVICE_CURTAIN){
							Connect.writeSelect(new byte[]{(byte)0xf0,(byte)device.type,(byte)device.index,0x02,(byte) (0x05 - device.moshi3),0x00});
						}else
							Connect.writeSelect(new byte[]{(byte)0xf0,(byte)device.type,(byte)device.index,(byte) device.moshi3,0x00,0x00});
						getposeHandler.sendEmptyMessage(j+1);
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						time++;
						if(device.pose == 0x00 || sflag||time >= 3){ 
							break;
						}
					}
					if(sflag){ 
						break;
					}
					j++;
				}
			}
			handler.sendEmptyMessage(0);
		}
	}
}
