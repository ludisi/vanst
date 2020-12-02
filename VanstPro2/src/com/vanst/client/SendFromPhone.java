package com.vanst.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.widget.Toast;

public class SendFromPhone {
	Activity activity;
	static byte[] bytes;
	static int room;
	static int len;
	static String ip;
	static ServerSocket serverSocket;
   	static Socket socket;
   	static DataInputStream dis;
   	static DataOutputStream dos;
   	static int progress;
   	static boolean flag;
   	static boolean isOK;
   	boolean isService;
   	AlertDialog.Builder builder;
   	final static int CONNECT_NEXT = 0x862;
   	final static int CONNECT_END = 0x863;
   	final static int CONNECT_SUCCEED = 0x861;
   	final static int SEND_SUCCEED = 0x860;
   	final static int SEND_ING = 0x859;
   	Handler handler = new Handler(){
   		public void handleMessage(android.os.Message msg) {
   			if(msg.what == CONNECT_NEXT){
   				MyLog.i("progress",progress+"");
   				progress++;
   				if(progress < 256) new connect().start();
   			}else if(msg.what == CONNECT_END){
   				isOK = false;
   				if(MainActivity.progressDialog !=null) MainActivity.progressDialog.dismiss();
   				Toast.makeText(activity, "连接已经断开", Toast.LENGTH_LONG).show();
   			}else if(msg.what == CONNECT_SUCCEED){
   				if(MainActivity.progressDialog !=null) MainActivity.progressDialog.dismiss();
   				if(!isService){
   					MainActivity.progressDialog = new ProgressDialog(activity);
   					MainActivity.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
   					MainActivity.progressDialog.setTitle("连接成功！");
   					MainActivity.progressDialog.setMessage("等待对方选择传输文件...");
   					MainActivity.progressDialog.setButton("取消",new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface arg0,
								int arg1) {
							flag = false;
							try {
								dos.writeByte(1);
							} catch (IOException e) {
								e.printStackTrace();
							}
							if(socket!=null)
								socket.isClosed();
							isOK = false;
						}
					});
   					MainActivity.progressDialog.setCancelable(false);
   					MainActivity.progressDialog.show();
   				}else{
   					final String [] strings = Config.getConfigNames();
					Arrays.sort(strings);
   					if(strings != null){
						String [] strings2 = new String[strings.length];
						for(int i =0;i<strings.length;i++){
							strings2[i] = "房间"+ strings[i].subSequence(6, strings[i].lastIndexOf('.'));
						}
						builder = new AlertDialog.Builder(activity);
						builder.setTitle("选择要上传的房间");
						builder.setIcon(android.R.drawable.ic_menu_more);
						builder.setItems(strings2, new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog, final int which) {
						    	if(flag){
						    		MainActivity.progressDialog = new ProgressDialog(activity);
				   					MainActivity.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				   					MainActivity.progressDialog.setTitle("正在传输中！");
				   					MainActivity.progressDialog.setMessage("请稍等。。。。");
				   					MainActivity.progressDialog.setButton("取消",new DialogInterface.OnClickListener() {
										
										public void onClick(DialogInterface arg0,
												int arg1) {
											flag = false;
											if(socket!=null)
												try {
													socket.close();
												} catch (IOException e1) {
													// TODO Auto-generated catch block
													e1.printStackTrace();
												}
											isOK = false;
											if(serverSocket!=null)
												try {
													serverSocket.close();
												} catch (IOException e) {
													e.printStackTrace();
												}
										}
									});
				   					MainActivity.progressDialog.setCancelable(false);
				   					MainActivity.progressDialog.show();
							    	final byte[] bytes = Config.readConfig(strings[which]);
							    	final int room = Integer.parseInt(strings[which].substring(6, strings[which].length()-6));
							    	MyLog.i("room",room+"");
							    	MyLog.i("len",bytes.length+"");
//							    	Toast.makeText(activity, "room:"+room+"len"+bytes.length, Toast.LENGTH_LONG).show();
							    	new Thread(){
							    		public void run() {
							    			try {
									    		dos.writeInt(room);
									    		dos.writeInt(bytes.length);
												dos.flush();
												dos.write(bytes);
												dos.flush();
											} catch (IOException e) {
												e.printStackTrace();
											}
							    		};
							    	}.start();
							    	
						    	}
						    }
						   });
						builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface arg0,
									int arg1) {
								flag = false;
								if(socket!=null)
									try {
										socket.close();
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								isOK = false;
								if(serverSocket!=null)
									try {
										serverSocket.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
									
							}
						});
						builder.show();
					}else{
						Toast.makeText(activity, "本地没有配置文件", Toast.LENGTH_LONG).show();
						
					}
   				}
   			}else if(msg.what == SEND_SUCCEED){
   				if(MainActivity.progressDialog !=null) MainActivity.progressDialog.dismiss();
   				if(isService){
   					MainActivity.musicDo.musicInfo(activity);
	   				builder = new AlertDialog.Builder(activity);
	   				builder.setTitle("传输成功，是否要继续传输");
	   				builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							handler.sendEmptyMessage(CONNECT_SUCCEED);
						}
					});
	   				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							flag = false;
							if(socket !=null){
								try {
									socket.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							if(serverSocket!=null)
								try {
									serverSocket.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							isOK = false;
						}
					});
	   				builder.show();
   				}else{
   					MainActivity.musicDo.musicInfo(activity);
   					Toast.makeText(activity, "已经接受房间"+room+"配置", Toast.LENGTH_LONG).show();
   					MainActivity.progressDialog = new ProgressDialog(activity);
   					MainActivity.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
   					MainActivity.progressDialog.setTitle("连接成功！");
   					MainActivity.progressDialog.setMessage("等待对方选择传输文件...");
   					MainActivity.progressDialog.setButton("取消",new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface arg0,
								int arg1) {
							flag = false;
							try {
								dos.writeByte(1);
							} catch (IOException e) {
								e.printStackTrace();
							}
							if(socket!=null)
								socket.isClosed();
							
							isOK = false;
						}
					});
   					MainActivity.progressDialog.setCancelable(false);
   					MainActivity.progressDialog.show();
   				}
   			}else if(msg.what == SEND_ING){
   				if(MainActivity.progressDialog !=null) MainActivity.progressDialog.setMessage("正在传输中....");
   			}
   		};
   	};
	public SendFromPhone(boolean isService,Activity activity){
		this.activity = activity;
		this.isService = isService;
		if(Connect.lanIp == null) Connect.lanIp = Connect.ipLan();
		if(isService){
			new send().start();
		}else{
			progress = 0;
			new connect().start();
		}
	}
	class send extends Thread{
	   	public void run() {
	   		try {
	   			SendFromPhone.serverSocket=new ServerSocket(8620);
	   			SendFromPhone.socket=SendFromPhone.serverSocket.accept();
	   			if(isOK){
	   				flag = true;
	   				dis=new DataInputStream(socket.getInputStream());
	   				dos=new DataOutputStream(socket.getOutputStream());
	   				handler.sendEmptyMessage(CONNECT_SUCCEED);
	   				new read().start();
	   			}else{
	   				if(socket !=null)
	   					socket.close();
	   			}
			} catch (IOException e) {
				e.printStackTrace();
			}
	   	}
	}
	class read extends Thread{
		public void run() {
			if(isService){
				while(flag){
					try {
						byte b = dis.readByte();
						MyLog.i("read",b+"");
						if(b == -1){
							handler.sendEmptyMessage(SEND_SUCCEED);
						}else if(b == 1){
							flag = false;
							handler.sendEmptyMessage(CONNECT_END);
						}
					} catch (IOException e) {
						flag = false;
						handler.sendEmptyMessage(CONNECT_END);
						e.printStackTrace();
					}
				}
			}else{
				while(flag){
					try {
						room = dis.readInt();
						len = dis.readInt();
						handler.sendEmptyMessage(SEND_ING);
						MyLog.i("room227",room+"");
						MyLog.i("len228", len+"");
						bytes = new byte[len];
//						dis.read(bytes, 0, len);
//						dis.read(bytes);
						dis.readFully(bytes);
//						for(int i =0;i<len;i++){
//							bytes[i] = dis.readByte();
//						}
						Config.saveConfig(bytes, room);
						dos.writeByte((byte)0xff);
						if(room == MainView.roomNum){
							new Thread(){
								public void run() {
									Config.askConfig(Config.readConfig(MainView.roomNum), ((MainActivity)activity).mainView);
									((MainActivity)activity).mainView.repaintRoom();
								};
							}.start();
						}
						handler.sendEmptyMessage(SEND_SUCCEED);
					} catch (IOException e) {
						flag = false;
						handler.sendEmptyMessage(CONNECT_END);
						e.printStackTrace();
					}
				}
			}
			
		}
	}
	
	
	class connect extends Thread{
		public void run() {
			int j = progress;
			handler.sendEmptyMessage(CONNECT_NEXT);
			Socket socket;
			while(!flag && isOK){
				socket = new Socket();
				MyLog.i("ip",Connect.lanIp+j);
				InetSocketAddress isa = new InetSocketAddress(Connect.lanIp+j,8620);
				long time = System.currentTimeMillis();
				try {
					socket.connect(isa,2000);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(!flag && socket.isConnected()){
					SendFromPhone.flag = true;
					ip = Connect.lanIp+j;
					SendFromPhone.socket = socket;
					try {
						SendFromPhone.dis=new DataInputStream(socket.getInputStream());
						SendFromPhone.dos=new DataOutputStream(socket.getOutputStream());
					} catch (IOException e) {
						e.printStackTrace();
					}
					handler.sendEmptyMessage(CONNECT_SUCCEED);
					new read().start();
					break;
				}
				long time2 = System.currentTimeMillis();
				try {
					if(time2 - time >= 2000) sleep(500);
					else sleep(2000 - (time2-time) + 500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}