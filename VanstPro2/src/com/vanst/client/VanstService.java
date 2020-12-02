package com.vanst.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class VanstService extends Service {
	static Socket socket;
	static boolean ConnectFlag;
	static boolean isAlive = true;
	static String serviceIP = "192.168.1.103";
	static int servicePort = 9988;
	static DataInputStream in;
	static DataOutputStream out;
	static VanstSeviceConnectThread vanstSeviceConnectThread;
	static VanstServiceReadThread vanstServiceReadThread;
	static VanstServiceThread vanstServiceThread;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
//			VanstWidgetProvider.updateAppWidget(VanstService.this);
			Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
			intent.putExtra("fun", "updata");
//			Toast.makeText(VanstService.this, "v", Toast.LENGTH_SHORT).show();
			VanstService.this.sendBroadcast(intent);
		};
	};
	@Override
	public void onCreate() {
		super.onCreate();
		isAlive = true;
	}
	
	@Override
	public void onDestroy() {
		isAlive = false;
		vanstServiceThread.flag = false;
		super.onDestroy();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Intent broadcast = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
		broadcast.putExtra("fun", "updata");
		sendBroadcast(broadcast);
		if(vanstServiceThread == null){
			vanstServiceThread = new VanstServiceThread();
			vanstServiceThread.start();
		}else{
			vanstServiceThread.flag = false;
			vanstServiceThread = new VanstServiceThread();
			vanstServiceThread.start();
		}
//		if(vanstSeviceConnectThread == null){
//			vanstSeviceConnectThread = new VanstSeviceConnectThread();
//			vanstSeviceConnectThread.start();
//		}
//		if(vanstServiceReadThread == null){
//			vanstServiceReadThread = new VanstServiceReadThread();
//			vanstServiceReadThread.start();
//		}
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	class VanstSeviceConnectThread extends Thread{
		public void run() {
			while(isAlive){
				if(!ConnectFlag){
					try {
						if(socket != null) {
							socket.close();
						}
						socket=new Socket();
						InetSocketAddress isa = new InetSocketAddress(serviceIP,servicePort);
						socket.connect(isa,5000);
						out=new DataOutputStream(socket.getOutputStream());
						in=new DataInputStream(socket.getInputStream());
						
						if(socket.isConnected()){
							ConnectFlag = true;
						}
					} catch (Exception e) {
						ConnectFlag = false;
						socket = null;
					}
				}
			}
		}
	}
	class VanstServiceReadThread extends Thread{
		@Override
		public void run() {
			while(isAlive){
				if(ConnectFlag && in!=null){
					try {
						byte b = in.readByte();
						MyLog.i("in",b+"");
					} catch (IOException e) {
						ConnectFlag = false;
					}
				}
			}
		}
	}
	class VanstServiceThread extends Thread{
		boolean flag = true;
		@Override
		public void run() {
			while(flag){
				handler.sendEmptyMessage(0);
				try {
					sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			super.run();
		}
	}
}
