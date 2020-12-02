package com.vanst.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import org.apache.http.conn.util.InetAddressUtils;




import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Connect {
	static byte b1;
	static byte b2;
	static long timeread;
	static long timewrite;
	static final int controllen = 0x06;
	static boolean widgetConnect;
	static Socket disSocket;
	static int CRCERROR = 0;
	static int socketindex = 0;
	static boolean disconnectShow;
	static int controlFlag;
	static boolean initSearch;
	static boolean initSynchronization;
	static boolean initRealtime;
	static boolean init3G;
	static boolean reConnect;
	static int connectNum = 0;
	static ArrayList<Byte> indates;
	static ArrayList<Byte> configList;
	final static int MAXIO_COUNT = 64;
	final static int control = 0x00;
	final static int upLoad = 0x01;
	final static int upIng = 0x11;
	final static int download = 0x02;
	final static int delete = 0x03;
	final static int readConfig = 0x05;
	final static int typedelete = 0xA0;
	final static int typeready = 0xA1;
	final static int typedo = 0xA2;
	final static int password = 0x04;
	final static int loadBitmap = 0xba;
	final static int learn = 0x06;
	final static int DATA_PAG_LENTH = 64;
	final static String defaultIp = "10.10.100.254";
	static int lastIp;
	static boolean isLogin = false;
	static boolean is3G;
	static boolean isHead;
	static String pwd;
	static String useIp;
	static String localIp;
	static int useIpd = 0;
	static String _3GIp;
	static String lanIp;
	static String phoneNumber;
	static byte[] bitmapBytes;
	final static int port = 8899;
	static boolean flag;
	static byte [] configs = new byte[MainActivity.deviceNames.length];
	static byte [] inByte = new byte[6];
	static byte [] outByte = new byte[6];
	static byte [] dateBytes;
	static int dateBytesP;
	static boolean succeed;
	static byte type;
	static boolean inFlag = true;
	static Socket socket;
	static DataInputStream in; 
	static DataOutputStream out;
	static int wFlag;
	static int rwPose;
	static int progress;
	static int progressLength;
	static boolean stopFlag;
	static boolean readCf;
	static boolean isEnter;
	static boolean newDate;
	static int configFlag;
	static boolean isConnecting;
	static Activity activity;
	static StartActivity startActivity;
	static CheckThread checkThread;
	static boolean refreshFlag;
	static public MainActivity mainActivity;
	static boolean writeTypeDataLag = false;
	static boolean writeTypeSucceed = false;
	static boolean writeTypeLag = false;
	static boolean refreshPose;
	static boolean phone3g;
	static boolean isAddShow;
	static Download downLoad;
	static GetList getList;
	static boolean needStopPhone = false;
	static AlertDialog.Builder newDeviceDialog;
	static Writetypesynchronizetion writetypesynchronizetion;
	static Configsynchronization configsynchronization;
	Device selectDevice;
	Device newDevice;
	static boolean writeFlag;
	static int reSearch = 0;
	static int isShared;
	String connectIp;
	final static int SYNCHRONIZATIONG_FINISHED = 0x4d;
	final static int UPLOAD_DOING_SINGLE = 0x02;
	final static int UPLOAD_READY_SINGLE = 0x03;
	final static int UPLOAD_SUCCEED_SINGLE = 0x04;
	final static int CONNECT_SINGLE = 0x11;
	final static int DISCONNECT_SINGLE = 0x12;
	final static int WRITE_TYPE_DATA = 0xAA;
	final static int WRITE_TYPE = 0xAB;
	final static int WRITE_CL = 0xAC;
	final static int CONNECT_3G = 0xCA;
	final static int SYNCHRONIZATIONG_FAIL_SINGLE = 0x4a;
	final static int PRE_SYNCHRONIZATIONE = 0xB1;
	final static int _3GIP_REFRESH = 0xB2;
	final static int REDA_CONFIG2_FAILED = 0xb3;
	final static int SDCARD_FAILED = 0xb4;
	final static int READ_DATE_ENTER = 0xBD;
	final static int READ_CONFIG2_SINGLE = 0xBE;
	final static int GET_POSE = 0xCC;
	final static int SYNCHRONIZATION_CL_SINGLE = 0xBF;
	final static int READ_DATE_FINISHED = 0x0D;
	final static int SERVCE_DISCONNECT = 0xFD;
	final static int SEND_FAILED = 0xfe;
	final static int NEW_DEVICE_SINGLE = 0xBC;
	final static int CONNECT_FAILED = 0x5a;
	final static int CONNECT_SUCCED = 0x5b;
	final static int PASSWORD_SUCCED = 0xfa;
	final static int PASSWORD_FAIL = 0xfb;
	final static int SEARCH_WIFI = 0xfc;
	final static int SEARCH_END = 0xfd;
	final static int UPNEXT = 0xf0;
	final static int LOAD_NEXT = 0xf1;
	final static int UP_REFRESH_PROGRESS = 0xf2;
	final static int LOAD_REFRESH_PROGRESS = 0xf3;
	final static int REFRESH_ROOMS = 0xf4;
	final static int LOAD_NO = 0xf5;
	final static int PROGRESS_DISMISS = 0xff;
	final static int FILE_LIST_FINISHED = 0xf6;
	final static int ANOTHER_DONWLOAD = 0xf7;
	final static int READ_DATA_CRC_ERROR = 0xf8;
	final static int RE_SOUND = 0xf9;
	final static int BING_SUCCEED = 0xe0;
	final static int UNBIND_SUCCEED = 0xe1;
	static ArrayList<String> fileList;
	static ArrayList<Character> chars = new ArrayList<Character>();
	static ArrayList<SocketIP> sockets;
	static DataInputStream[] ins; 
	static DataOutputStream[] outs;
	static boolean isUpBitmap;
	static String filename;
	static String readName;
	static int upRoom = 0;
	static Room[] upRooms;
	static String[] downRooms;
	static long configTime;
	static long downLoadTime;
	static String[] phones;
	AlertDialog.Builder builder;
	static ProgressDialog progressDialog;
	static Handler learnHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == (byte)0xff){
				if(KtControlActivity.imageView != null){
					KtControlActivity.imageView.setBackgroundResource(R.drawable.kt_learn1);
				}
			}else
				ClControlActivity.learnSingle(msg.what);
			Toast.makeText(activity, "学习成功！", Toast.LENGTH_LONG).show();
			MainActivity.musicDo.musicInfo(activity);
		};
	};
	static Handler diylearnHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			DiyIRActivity.learnSingle(msg.what);
			MainActivity.musicDo.musicInfo(activity);
		};
	};
	static Handler synchronizationHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == SYNCHRONIZATIONG_FAIL_SINGLE){
//				Toast.makeText(activity, "同步失败" ,Toast.LENGTH_SHORT).show();
				Connect.rwPose = Connect.control;
				if(MainActivity.progressDialog != null){
					MainActivity.progressDialog.dismiss();
					Toast.makeText(activity, "加载文件列表失败" ,Toast.LENGTH_SHORT).show();
				}
//				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//				builder.setTitle("同步失败");
//				builder.setIcon(android.R.drawable.ic_dialog_alert);
//				builder.setPositiveButton("确认", null);
//				builder.show();
			}else if(msg.what == PRE_SYNCHRONIZATIONE){
				MyLog.i("PRE","同步");
				SharedPreferences pref=activity.getSharedPreferences("config", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor=pref.edit();
				editor.putInt("angel", Room.defaultAngle);
				if(Room.count!=0)editor.putInt("rooms", Room.count);
				if(useIp!=null)editor.putString("useip", useIp);
				if(_3GIp!=null)editor.putString("lanip", _3GIp);
				if(phoneNumber!=null)editor.putString("phone", phoneNumber);
				editor.putBoolean("search", initSearch);
				editor.putBoolean("synchronization", initSynchronization);
				editor.putBoolean("realtime", initRealtime);
				editor.putBoolean("3gserver", init3G);
				editor.commit();
			}else if(msg.what == _3GIP_REFRESH){
				if(phone3g){
					phone3g = false;
					if(!isEnter) startActivity.waitTextView.setVisibility(View.GONE);
					Toast.makeText(activity, "认证成功，请连接服务器！",Toast.LENGTH_LONG).show();
					AlertDialog.Builder builder = new AlertDialog.Builder(activity);
					builder.setTitle("3G连接");
					builder.setMessage("认证成功，是否要连接？");
					builder.setIcon(android.R.drawable.ic_menu_help);
					builder.setPositiveButton("连接", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							if(isEnter){
								MainActivity.netPre = new ArrayList<String>();
								new Connect(activity,_3GIp,true);
							}else{
								startActivity.connect3g();
							}
						}
					});
					builder.setNegativeButton("取消", null);
					builder.show();
				}
			}else if(msg.what == SERVCE_DISCONNECT ){
				Toast.makeText(activity,"服务器未连接",Toast.LENGTH_LONG).show();
			}else if(msg.what == SEND_FAILED){
				Toast.makeText(activity, "发送失败", Toast.LENGTH_LONG).show();
			}else if(msg.what == REDA_CONFIG2_FAILED){
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle("获取服务器状态失败，是否重新获取？");
				builder.setPositiveButton("重新获取", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						readConfig2();
					}
				});
				builder.setNegativeButton("取消", null);
			}else if(msg.what == READ_CONFIG2_SINGLE){
				readConfig2();
			}else if(msg.what == GET_POSE){
				getPose((byte)0);
			}else if(msg.what == SDCARD_FAILED){
				Toast.makeText(activity,"SD有误，请确认SD卡正常插入！",Toast.LENGTH_LONG).show();
			}
		}
	};
	
	class TimeThread extends Thread{
		boolean sflag = false;
		public void run() {
			while(!sflag)
				try {
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				writeFlag = true;
		}
	}
	static class RefreshPose extends Thread{
		public void run() {
			try {
				sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if(Connect.flag) Connect.getPose((byte)0);
		}
	}
	Handler progressHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(MainActivity.progressDialog !=null && msg.what >=0 && msg.what <= 100){
				MainActivity.progressDialog.setProgress(msg.what);
			}
			if(progressDialog != null && msg.what >= 0 && msg.what <= 100){
				progressDialog.setProgress(msg.what);
			}
		};
	};
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == BING_SUCCEED){
				Toast.makeText(activity, "绑定成功", Toast.LENGTH_LONG).show();
			}else if(msg.what == UNBIND_SUCCEED){
				Toast.makeText(activity, "解除绑定成功", Toast.LENGTH_LONG).show();
			}else if(msg.what == SYNCHRONIZATIONG_FINISHED){
				if(MainActivity.progressDialog!=null) MainActivity.progressDialog.dismiss();
				if(progressDialog!=null) progressDialog.dismiss();
				Toast.makeText(activity, "同步成功", Toast.LENGTH_LONG).show();
				MainActivity.musicDo.musicInfo(activity);
			}else if(msg.what == UPLOAD_DOING_SINGLE){
				byte[] bytes = new byte[MainActivity.pre.size()];
				int i = 0;
				int crc = 0xfe + 0xf6 +0x1A + 0xf8 + lastIp + 0x17;
				for(i = 0;i<MainActivity.pre.size();i++){
					bytes[i] = Byte.parseByte((String) MainActivity.deviceNums.get(i).get("number"));
					crc += bytes[i];
					MyLog.i("byte"+i,bytes[i]+"");
				}
				try {
					rwPose = upIng;
					byte[] writebytes = new byte[8+bytes.length];
					writebytes[0] = (byte)0xfe;
					writebytes[1] = (byte)0xf6;
					writebytes[2] = (byte)0x1a;
					writebytes[3] = (byte)0xf8;
					writebytes[4] = (byte)lastIp;
					writebytes[5] = (byte)0x17;
					for(int i1 = 0;i1<bytes.length;i1++){
						writebytes[i1+6] = bytes[i1];
					}
					writebytes[6+bytes.length] = (byte)(crc/256);
					writebytes[7+bytes.length] = (byte)(crc%256);
					out.write(writebytes);
//					out.writeByte((byte)0xFE);
//					out.writeByte((byte)0xF6);
//					out.writeByte((byte)0x1A);
//					out.writeByte((byte)0xF8);
//					out.writeByte((byte)lastIp);
//					out.writeByte((byte)0x17);
//					out.write(bytes);
//					out.writeByte((byte)(crc/256));
//					out.writeByte((byte)(crc%256));
					out.flush();
					MyLog.i("crc",crc+"");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if(msg.what == UPLOAD_READY_SINGLE){
				writeConfig();
			}else if(msg.what == UPLOAD_SUCCEED_SINGLE){
				rwPose = control;
				if(configsynchronization !=null) configsynchronization.sflag = true;
				Toast.makeText(activity, "同步成功", Toast.LENGTH_LONG).show();
				MainActivity.musicDo.musicInfo(activity);
				MainActivity.progressDialog.cancel();
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle("同步成功");
				builder.setIcon(android.R.drawable.ic_dialog_info);
				builder.setPositiveButton("确认", null);
				AlertDialog dialog = builder.create();
				dialog.show();
			}else if(msg.what == CONNECT_SINGLE){	
				if(!connectWifi(activity, lanIp+progress,500)){	
					MyLog.i("ip",lanIp+progress);
					if(progress >= 255){
						Toast.makeText(activity,"服务器连接失败，请重试，如仍有问题请联系网络管理员", Toast.LENGTH_SHORT).show();
						startActivity.dialogShow(true);
						progress = 0;
						startActivity.startButton.setEnabled(true);
//						startActivity.progressBar.setVisibility(View.GONE);
						startActivity.textView.setVisibility(View.GONE);
					}else{
						new ProgressThread().start();
//						((StartActivity)activity).progressBar.setProgress(progress);
					}
					progress++;
				}else{
					useIp = lanIp+progress;
					SharedPreferences pref=activity.getSharedPreferences("config", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putString("useip", useIp);
					editor.commit();
					Toast.makeText(activity, "服务器IP："+lanIp+progress, Toast.LENGTH_SHORT).show();
//					((StartActivity)activity).progressBar.setProgress(((StartActivity)activity).progressBar.getMax());
					progress = 0;
					startActivity.enter();
					startActivity.startButton.setEnabled(true);
//					startActivity.progressBar.setVisibility(View.GONE);
					startActivity.textView.setVisibility(View.GONE);
				}
			}else if(msg.what == DISCONNECT_SINGLE){
				disconnectShow = true;
				if((is3G||sockets == null || sockets.size() <=1) && !isConnecting){
					Toast.makeText(activity, "Vanst:连接已经断开", Toast.LENGTH_LONG).show();
					flag = false;
					sockets = null;
					VanstWidgetProvider.updateAppWidget(activity);
					if(mainActivity!=null)mainActivity.reConnect();
				}else if(!isConnecting){
					ArrayList<SocketIP> ss = new ArrayList<SocketIP>();
					for(SocketIP socketIP:sockets){
						if(socketIP.socket != disSocket){
	//						Toast.makeText(activity, socketIP.ip, Toast.LENGTH_LONG).show();
	//						try {
	//							sockets.remove(disSocket);
	//						} catch (Exception e) {
	//						}
							ss.add(socketIP);
						}
						
					}
					sockets = ss;
					if(sockets.size()!=0){
						ins = new DataInputStream[sockets.size()];
						outs = new DataOutputStream[sockets.size()];
						int j = 0;
					for(SocketIP socketIP:sockets){
						try {
							ins[j] = new DataInputStream(socketIP.socket.getInputStream());
							outs[j] = new DataOutputStream(socketIP.socket.getOutputStream());
						} catch (IOException e) {
							e.printStackTrace();
						}
						j++;
					}
					in = ins[0];
					out = outs[0];
					socket = sockets.get(0).socket;
					socketindex = 0;
					Toast.makeText(activity, "失去了一个链接", Toast.LENGTH_LONG).show();
					}
				} 
				/**
				disconnectShow = true;
				if(connectNum == 0 && !isConnecting){
					Toast.makeText(activity, "Vanst:连接已经断开", Toast.LENGTH_LONG).show();
					flag = false;
					if(mainActivity!=null)mainActivity.reConnect();
				}
				*/
			}else if(msg.what == WRITE_TYPE_DATA){
				if(filename.equals("WIFI") || filename.equals("cl"))writeTypeDate();
				else writeBitmap();
			}else if(msg.what == WRITE_TYPE){
				if(MainActivity.progressDialog != null) MainActivity.progressDialog.setMessage("上传中...");
				writeTpye(filename);
			}else if(msg.what == WRITE_CL){
				if(MainActivity.progressDialog != null) MainActivity.progressDialog.setMessage("校验中...");
				isShared = 3;
				Connect.askShare(Connect.pwd);
			}else if(msg.what == CONNECT_3G){
				Toast.makeText(activity, "Vanst:3G连接成功", Toast.LENGTH_LONG).show();
				if(checkThread != null) checkThread.fflag = false;
			}else if(msg.what == 0x93){
				progressDialog.dismiss();
			}else if(msg.what == READ_DATE_ENTER){
				progressHandler.sendEmptyMessage(progress * 100 / progressLength);
//				try {
//					int crc = 0xfe + 0xf6 + 0x03 + 0xf5 + lastIp + 0x81;
//					out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)0x03,(byte)0xf5,(byte)lastIp,(byte)0x81,(byte)(crc/256),(byte)(crc%256)});
////					out.writeByte((byte)(crc/256));
////					out.writeByte((byte)(crc%256));
//					out.flush();
//					timewrite = System.currentTimeMillis();
//					Log.i("time", timewrite - timeread +"");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			}else if(msg.what == READ_DATA_CRC_ERROR){
				try {
					int crc = 0xfe + 0xf6 + 0x03 + 0xf5 + lastIp + 0x01;
					out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)0x03,(byte)0xf5,(byte)lastIp,(byte)0x01,(byte)(crc/256),(byte)(crc%256)});
//					out.writeByte((byte)(crc/256));
//					out.writeByte((byte)(crc%256));
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if(msg.what == READ_CONFIG2_SINGLE){
				readConfig2();
			}else if(msg.what == SYNCHRONIZATION_CL_SINGLE){
				Device.formatConfigBytes();
				deleteTpye("cl");
			}else if(msg.what == READ_DATE_FINISHED){
				progressHandler.sendEmptyMessage(100);
				if(readName!=null)MyLog.i("readName", readName);
				if(readName.equals("bayern")){
					rwPose = control;
					byte[] bytes = dateBytes;
					dateBytes = new byte[bytes.length];
					String string="";
					for(int j=0;j<bytes.length;j++){
						dateBytes[j]=bytes[bytes.length-1-j];
						string += (char) dateBytes[j];
					}
					int count = 0;
					phones = new String[0];
//					Toast.makeText(activity, string, Toast.LENGTH_LONG).show();
					if(dateBytes !=null){
						if(dateBytes.length != 0){
							for(byte b:dateBytes){
								if(b == 's'){
									count++;
								}
							}
//							Toast.makeText(activity, count+"", Toast.LENGTH_LONG).show();
							if(count!=0){
								phones = new String[count];
								int i = 0;
								for(byte b:dateBytes){
									
									if(b == 'e'){
										phones[i] = "";
									}
									if(b != 's' && b!='e'){
										char c = (char)b;
										phones[i] += c;
									}
									if(b == 's'){
										i++;
										if(i<count)phones[i] = "";
									}
								}
							}
						}
					}
					if(progressDialog !=null) progressDialog.dismiss();
					phoneManage();
				}else if(readName != null){
//					byte[] bytes = indates.toString().getBytes();
//					byte[] bytes = new byte[indates.size()];
//					for(int i=0;i< bytes.length;i++){
//						bytes[i] = indates.get(i);
//					}
//					indates = null;
					int configNum = 0;
					String string = downRooms[upRoom].substring(6);
					try {
						configNum = Integer.parseInt(string);
					} catch (Exception e) {
						MyLog.i("error","error"); 
					}
					MyLog.i("readName", readName.length()+"");
					MyLog.i("len",string.length()+"");
					MyLog.i("downRooms",downRooms[upRoom].substring(6));
					MyLog.i("ConfigNum", string+"");
					MyLog.i("dateBytes",dateBytes.length+"");
					if(configNum > 0 && Config.checkConfigBytes(dateBytes)) {
						/**
						Config.saveConfig(dateBytes, configNum);
						*/
						Config.updateConfig(dateBytes, configNum);
						if(configNum == MainView.roomNum){
							mainActivity.mainView.refreshThread.isRun = false;
							Config.askConfig(Config.readConfig(MainView.roomNum),mainActivity.mainView);
							mainActivity.mainView.repaintRoom();
							mainActivity.mainView.refreshThread.isRun = true;
						}
						Toast.makeText(activity, "下载房间"+downRooms[upRoom].substring(6)+"配置文件成功！", Toast.LENGTH_LONG).show();
						MainActivity.musicDo.musicInfo(activity);
					}else{
						Toast.makeText(activity, "下载房间"+downRooms[upRoom].substring(6)+"配置文件有误！", Toast.LENGTH_LONG).show();
						MainActivity.musicDo.musicError(activity);
					}
					
					dateBytes = null;
					
					rwPose = control;
					if(upRoom < downRooms.length-1){
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						upRoom++;
						handler.sendEmptyMessage(LOAD_REFRESH_PROGRESS);
						handler.sendEmptyMessage(LOAD_NEXT);
					}else{
						handler.sendEmptyMessage(PROGRESS_DISMISS);
						handler.sendEmptyMessage(REFRESH_ROOMS);
					}
				}
				
			}else if(msg.what == NEW_DEVICE_SINGLE){
				MyLog.i("new",mainActivity.isSetting+"");
				if(isEnter){
					if(mainActivity.isSetting && !isAddShow){
						MainActivity.musicDo.musicInfo(activity);
						Toast.makeText(mainActivity, "有新的设备", Toast.LENGTH_LONG).show();
						final Device device = newDevice;
						if(newDeviceDialog==null){
						newDeviceDialog = new AlertDialog.Builder(mainActivity);
						newDeviceDialog.setCancelable(false);
						}
						newDeviceDialog.setTitle("有新的"+MainActivity.deviceNames[device.type-1]+"是否要加入？");
						newDeviceDialog.setIcon(Device.DEVICE_IMAGES[device.type-1]);
						newDeviceDialog.setPositiveButton("加入", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface arg0, int arg1) {
								mainActivity.mainView.addDevice(device);
								isAddShow = false;
							}
						});
						newDeviceDialog.setNegativeButton("忽略", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								isAddShow = false;
							}
						});
						newDeviceDialog.create().show();
						isAddShow = true;
					}
				}
			}else if(msg.what == CONNECT_FAILED){
				isConnecting = false;
				if(progressDialog!=null)progressDialog.dismiss();
				MainActivity.musicDo.musicError(activity);
				if(!isEnter){
					if(is3G){
						startActivity.phoneDialog();
					}else{
						AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				    	builder.setIcon(android.R.drawable.ic_menu_search);
				    	builder.setTitle("连接失败是否要进行搜索网络？");
				    	builder.setMessage("WIFI服务器IP：");
				    	final EditText editText = new EditText(activity);
				    	editText.setText(useIp);
				    	editText.setHint("wifi服务器IP");
				    	builder.setView(editText);
				    	builder.setPositiveButton("搜索网络", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								sockets = new ArrayList<SocketIP>();
								progress = 0;
								handler.sendEmptyMessage(SEARCH_WIFI);
								progressDialog = new ProgressDialog(activity);
//								progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
								progressDialog.setMax(100);
								progressDialog.setCancelable(false);
								progressDialog.setTitle("正在连接中。。。");
								progressDialog.setMessage("请稍等...");
								progressDialog.show();
								new Thread(){
									public void run() {
//										for(int i = 0;i<100;i++){
//											try {
//												sleep(100);
//											} catch (InterruptedException e) {
//												e.printStackTrace();
//											}
//											progressHandler.sendEmptyMessage(i);
//										}
//										try {
//											sleep(1000);
//										} catch (InterruptedException e) {
//											e.printStackTrace();
//										}
//										progressHandler.sendEmptyMessage(100);
										try {
											sleep(2000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										handler.sendEmptyMessage(SEARCH_END);
										if(!sockets.isEmpty()){
											int i = 0;
											for(SocketIP socketIP:sockets){
												MyLog.i("SOCKET",socketIP.ip);
												i++;
											}
										}
									};
								}.start();
							}
						});
				    	builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								StartActivity startActivity = (StartActivity)activity;
								startActivity.dialogShow(true);
							}
						});
				    	builder.setNeutralButton("再次连接", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								if(editText.getText().toString().length() > 0){
									useIp = editText.getText().toString();
									SharedPreferences pref=activity.getSharedPreferences("config", Context.MODE_PRIVATE);
									SharedPreferences.Editor editor=pref.edit();
									editor.putString("useip", useIp);
									editor.commit();
								}
								Toast.makeText(activity, "正在为您连接中", Toast.LENGTH_SHORT).show();
								new Thread(){
									public void run() {
										try {
											sleep(500);
										} catch (Exception e) {
										}
										StartActivity startActivity = (StartActivity)activity;
										startActivity.handler.sendEmptyMessage(StartActivity.CONNECT_WIFI);
									};
								}.start();
							}
						});
						builder.show();
					}
				}else{
					if(!Connect.flag && !is3G){
						AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				    	builder.setIcon(android.R.drawable.ic_menu_search);
				    	builder.setTitle("连接失败是否要进行搜索网络？");
				    	final EditText editText = new EditText(activity);
				    	editText.setText(useIp);
				    	editText.setHint("wifi服务器IP");
				    	if(!isEnter){
				    		builder.setMessage("WIFI服务器IP：");
				    		builder.setView(editText);
				    	}
				    	builder.setPositiveButton("搜索网络", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								lanIp = ipLan();
								sockets = new ArrayList<SocketIP>();
								progress = 0;
								handler.sendEmptyMessage(SEARCH_WIFI);
								progressDialog = new ProgressDialog(activity);
//								progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
								progressDialog.setMax(100);
								progressDialog.setCancelable(false);
								progressDialog.setTitle("正在为您连接中。。。");
								progressDialog.setMessage("请稍等...");
								progressDialog.show();
								new Thread(){
									public void run() {
//										for(int i = 0;i<100;i++){
//											try {
//												sleep(100);
//											} catch (InterruptedException e) {
//												e.printStackTrace();
//											}
//											progressHandler.sendEmptyMessage(i);
//										}
//										try {
//											sleep(1000);
//										} catch (InterruptedException e) {
//											e.printStackTrace();
//										}
//										progressHandler.sendEmptyMessage(100);
										try {
											sleep(2000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										handler.sendEmptyMessage(SEARCH_END);
										if(!sockets.isEmpty()){
											int i = 0;
											for(SocketIP socketIP:sockets){
												MyLog.i("SOCKET",socketIP.ip);
												i++;
											}
										}
									};
								}.start();
							}
						});
				    	builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								if(!isEnter){
								StartActivity startActivity = (StartActivity)activity;
								startActivity.dialogShow(true);
								}
							}
						});
				    	if(!isEnter)
				    	builder.setNeutralButton("再次连接", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								if(editText.getText().toString().length() > 0){
									useIp = editText.getText().toString();
									SharedPreferences pref=activity.getSharedPreferences("config", Context.MODE_PRIVATE);
									SharedPreferences.Editor editor=pref.edit();
									editor.putString("useip", useIp);
									editor.commit();
								}
								Toast.makeText(activity, "正在为您连接中", Toast.LENGTH_SHORT).show();
								new Thread(){
									public void run() {
										try {
											sleep(500);
										} catch (Exception e) {
										}
										
											StartActivity startActivity = (StartActivity)activity;
											startActivity.handler.sendEmptyMessage(StartActivity.CONNECT_WIFI);
									};
								}.start();
							}
						});
						builder.show();
					}
					if(!Connect.flag && is3G){
						AlertDialog.Builder builder = new AlertDialog.Builder(activity);
						builder.setTitle("连接失败！");
						builder.setIcon(android.R.drawable.ic_menu_call);
						builder.setMessage("请重新进行用户认证");
						builder.setPositiveButton("用户认证", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								Connect.phone3g = true;
								Connect.PhoneCall(activity);
							}
						});
						builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								Toast.makeText(activity, "请检查服务器号码的正确性再进行认证！",Toast.LENGTH_LONG).show();
							}
						});
						builder.show();
					}
				}
				Toast.makeText(activity, "Vanst：服务器连接失败", Toast.LENGTH_LONG).show();
			}else if(msg.what == CONNECT_SUCCED){
				SharedPreferences pref=activity.getSharedPreferences("config", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor=pref.edit();
				if(!is3G){
					editor.putInt("connect", 1);
				}else{
					editor.putInt("connect", 2);
				}
				editor.commit();
				isConnecting = false;
				if(progressDialog!=null)progressDialog.dismiss();
				if(isEnter && !is3G) getPose((byte)0);
				else if(isEnter && is3G) new ReadConfig().start();
				new ConnectThread().start();
				
				if(isEnter){
					Toast.makeText(activity, "Vanst：服务器连接成功", Toast.LENGTH_LONG).show();
					new RefreshPose().start();
				}else{
					Toast.makeText(activity, "Vanst：服务器连接成功", Toast.LENGTH_LONG).show();
					startActivity.enter();
				}
			}else if(msg.what == PASSWORD_SUCCED){
				MyLog.i("Password","Succeed");
				if(isShared != 3 && MainActivity.progressDialog!=null) MainActivity.progressDialog.dismiss();
				if(isShared == 0){
					if(Connect.flag){
						MainActivity.progressDialog = new ProgressDialog(activity);
						MainActivity.progressDialog.setTitle("写入配置中...");
						MainActivity.progressDialog.setMessage("准备中...");
						MainActivity.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
						MainActivity.progressDialog.setIcon(R.drawable.bt_upload);
						MainActivity.progressDialog.setMax(100); 
						MainActivity.progressDialog.setProgress(0);
						MainActivity.progressDialog.setButton("取消",new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface arg0,
									int arg1) {
								Connect.rwPose = Connect.control;
								MainActivity.progressDialog.dismiss();
							}
						});
						MainActivity.progressDialog.setCancelable(false);
						MainActivity.progressDialog.show();
					}
					Connect.deleteTpye("config"+MainView.roomNum);
				}else if(isShared == 1){
					if(Connect.flag){
						MainActivity.progressDialog = new ProgressDialog(activity);
						MainActivity.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						MainActivity.progressDialog.setTitle("网络配置中...");
						MainActivity.progressDialog.setMessage("请稍等..正在将配置网络，完成后请重新启动WIFI模块！");
						MainActivity.progressDialog.setButton("取消",new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface arg0,
									int arg1) {
								Connect.rwPose = Connect.control;
								if(Connect.writetypesynchronizetion!=null)Connect.writetypesynchronizetion.sflag = true;
							}
						});
						MainActivity.progressDialog.setCancelable(false);
						MainActivity.progressDialog.show();
					}
					Connect.deleteTpye("WIFI");
//					rwPose = control;
//					Intent intent = new Intent("android.intent.action.proWifi");
//					activity.startActivity(intent);
				}else if(isShared == 2){
					if(Connect.flag){
						if(progressDialog !=null) progressDialog.dismiss();
						MainActivity.progressDialog = new ProgressDialog(activity);
						MainActivity.progressDialog.setTitle("写入配置中...");
						MainActivity.progressDialog.setMessage("准备中...");
						MainActivity.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
						MainActivity.progressDialog.setIcon(R.drawable.bt_upload);
						MainActivity.progressDialog.setMax(100); 
						MainActivity.progressDialog.setProgress(0);
						MainActivity.progressDialog.setButton("取消",new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface arg0,
									int arg1) {
								Connect.rwPose = Connect.control;
								MainActivity.progressDialog.dismiss();
							}
						});
						MainActivity.progressDialog.setCancelable(false);
						MainActivity.progressDialog.show();
					}
					Connect.deleteTpye(filename);
				}else if(isShared == 3){
					Config.getConfigCl();
				}
			}else if(msg.what == PASSWORD_FAIL){
				MainActivity.musicDo.musicError(activity);
				if(MainActivity.progressDialog!=null) MainActivity.progressDialog.dismiss();
				MyLog.i("Password","fail");
//				rwPose = control;
				final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle("密码输入错误！请重新输入。");
				builder.setIcon(android.R.drawable.ic_menu_edit);
				final EditText editText = new EditText(activity);
				editText.setHint("同步密码");
				builder.setView(editText);
				editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						Connect.pwd = editText.getText().toString();
						Connect.askShare(editText.getText().toString());
						MainActivity.progressDialog = new ProgressDialog(activity);
						MainActivity.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						MainActivity.progressDialog.setMessage("请稍等..等待密码验证");
						MainActivity.progressDialog.setButton("取消",new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface arg0,
									int arg1) {
								Connect.rwPose = Connect.control;
//								Connect.writetypesynchronizetion.sflag = true;
							}
						});
						MainActivity.progressDialog.setCancelable(false);
						MainActivity.progressDialog.show();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface arg0, int arg1) {
						rwPose = control;
					}
				});
				builder.show();
				Toast.makeText(activity, "密码验证错误", Toast.LENGTH_LONG).show();
			}else if(msg.what == UPNEXT){
				Room.readFile(Room.rooms.get(upRoom));
			}else if(msg.what == LOAD_NEXT){
				MyLog.i("Load",downRooms[upRoom]);
				progress = 0;
				Connect.loadBitmap(downRooms[upRoom]);
//				readFile(downRooms[upRoom]);
			}else if(msg.what == ANOTHER_DONWLOAD){
				if(MainActivity.progressDialog!=null) MainActivity.progressDialog.dismiss();
				Toast.makeText(activity, "正在有其他设备在下载。。。", Toast.LENGTH_LONG).show();
			}else if(msg.what == LOAD_NO){
				Toast.makeText(activity, "空", Toast.LENGTH_LONG).show();
				if(readName.equals("bayern")){
					if(progressDialog!=null){
						progressDialog.dismiss();
					}
					phones = new String[0];
					phoneManage();
				}
			}else if(msg.what == UP_REFRESH_PROGRESS){
				
			}else if(msg.what == LOAD_REFRESH_PROGRESS){
				if(MainActivity.progressDialog!=null){
					MainActivity.progressDialog.setMessage("下载第"+(Connect.upRoom+1)+"个配置文件中(共"+downRooms.length+"个)");
					MainActivity.progressDialog.setProgress(0);
				}
			}else if(msg.what == PROGRESS_DISMISS){
				if(MainActivity.progressDialog!=null){
					MainActivity.progressDialog.dismiss();
				}
				if(progressDialog!=null){
					progressDialog.dismiss();
				}
			}else if(msg.what == REFRESH_ROOMS){
				Room.initRooms(mainActivity.mainView);
			}else if(msg.what == FILE_LIST_FINISHED){
				if(getList != null) getList.isAlive = false;
				if(MainActivity.progressDialog != null) MainActivity.progressDialog.dismiss();
				if(fileList.size() != 0){
					builder = new AlertDialog.Builder(activity);
					builder.setTitle("选择要下载的配置文件");
					builder.setIcon(R.drawable.bt_download);
					final String[] strings = new String[fileList.size()];
					String[] names = new String[fileList.size()];
					int i = 0;
					for(String string:fileList){
						strings[i] = string;
						i++;
					}
					Arrays.sort(strings);
					for(int j=0;j<strings.length;j++){
						names[j] = "房间"+strings[j].substring(6);
					}
					
					final boolean [] bs = new boolean[fileList.size()];
					builder.setMultiChoiceItems(names,
												new boolean[fileList.size()], new  DialogInterface.OnMultiChoiceClickListener() {
													public void onClick(DialogInterface arg0, int num, boolean arg2) {
//														rooms[num] = arg2;
														bs[num] = arg2;
													}
												});
					builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface arg0, int arg1) {
							int  count = 0;
							for(boolean b:bs){
								if(b)count++;
							}
							if(count != 0){
								downRooms = new String[count];
								int i = 0;
								int j = 0;
								for(boolean b:bs){
									if(b) {
										downRooms[i] = strings[j];
										i++;
									}
									j++;
								}
								upRoom = 0;
								progress = 0;
								MainActivity.progressDialog = new ProgressDialog(activity);
								MainActivity.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								MainActivity.progressDialog.setTitle("正在下载配置文件");
								MainActivity.progressDialog.setIcon(R.drawable.bt_download);
								MainActivity.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
								MainActivity.progressDialog.setMax(100); 
								MainActivity.progressDialog.setMessage("下载第"+(Connect.upRoom+1)+"个配置文件中(共"+count+"个)");
								MainActivity.progressDialog.setButton("取消",new DialogInterface.OnClickListener() {
									
									public void onClick(DialogInterface arg0,
											int arg1) {
										Connect.rwPose = Connect.control;
									}
								});
								MainActivity.progressDialog.setCancelable(false);
								MainActivity.progressDialog.show();
								loadBitmap(downRooms[upRoom]);
							}
						}
					});
					builder.show();
				}else{
					Toast.makeText(activity, "网络配置文件为空！", Toast.LENGTH_SHORT).show();
				}
			}else if(msg.what == RE_SOUND){
				MainActivity.musicDo.musicRe(activity);
			}else if(msg.what == SEARCH_WIFI){
				searchWifi();
			}else if(msg.what == SEARCH_END){
				if(sockets.isEmpty() && reSearch < 3){
					reSearch++;
					new Thread(){
						public void run() {
							try {
								sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							handler.sendEmptyMessage(SEARCH_END);
						};
					}.start();
				}else{
					reSearch = 0;
				if(progressDialog!=null)
				progressDialog.dismiss();
				if(sockets.isEmpty()){
					if(!widgetConnect){
						MainActivity.musicDo.musicError(activity);
						AlertDialog.Builder builder = new AlertDialog.Builder(activity);
						builder.setTitle("没有搜服务器，请重试，如仍有问题请联系网络管理员到服务器");
						builder.setPositiveButton("再次搜索", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								lanIp = ipLan2();
								sockets = new ArrayList<SocketIP>();
								progress = 0;
								handler.sendEmptyMessage(SEARCH_WIFI);
								progressDialog = new ProgressDialog(activity);
	//							progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
								progressDialog.setMax(100);
								progressDialog.setCancelable(false);
								progressDialog.setTitle("正在为您连接中。。。");
								progressDialog.setMessage("请稍等...");
								progressDialog.show();
								new Thread(){
									public void run() {
										try {
											sleep(2000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										handler.sendEmptyMessage(SEARCH_END);
										if(!sockets.isEmpty()){
											int i = 0;
											for(SocketIP socketIP:sockets){
												MyLog.i("SOCKET",socketIP.ip);
												i++;
											}
										}
									};
								}.start();
							}
						});
				    	builder.setNeutralButton("取消", null);
				    	builder.show();
					}
					Toast.makeText(activity, "没有搜到服务器，请重试，如仍有问题请联系网络管理员到服务器",Toast.LENGTH_LONG).show();
					if(widgetConnect){
						widgetConnect = false;
						VanstWidgetProvider.updateAppWidget(Connect.activity);
					}
					
				}else{
					ins = new DataInputStream[sockets.size()];
					outs = new DataOutputStream[sockets.size()];
					int j = 0;
					for(SocketIP socketIP:sockets){
						try {
							ins[j] = new DataInputStream(socketIP.socket.getInputStream());
							outs[j] = new DataOutputStream(socketIP.socket.getOutputStream());
						} catch (IOException e) {
							e.printStackTrace();
						}
						j++;
					}
					in = ins[0];
					out = outs[0];
					socket = sockets.get(0).socket;
					socketindex = 0;
					initCommunicate();
					flag = true;
					widgetConnect = false;
					VanstWidgetProvider.updateAppWidget(activity);
					isConnecting = false;
					disconnectShow = false;
					SharedPreferences pref=activity.getSharedPreferences("config", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putInt("connect", 1);
					editor.commit();
					Toast.makeText(activity, "Vanst:服务器连接成功！", Toast.LENGTH_SHORT).show();
					if(!isEnter) startActivity.enter();
					/**
					String [] strings = new String[sockets.size()];
					for(int i =0;i<sockets.size();i++){
						strings[i] = sockets.get(i).ip;
					}
					socket = sockets.get(0).socket;
					useIp = sockets.get(0).ip;
					AlertDialog.Builder builder = new AlertDialog.Builder(activity);
					builder.setTitle("请选择要连接的服务器：");
					builder.setIcon(android.R.drawable.ic_menu_more);
					builder.setSingleChoiceItems(strings, 0, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							socket = sockets.get(arg1).socket;
							useIp = sockets.get(arg1).ip;
						}
					});
					builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							SharedPreferences pref=activity.getSharedPreferences("config", Context.MODE_PRIVATE);
							SharedPreferences.Editor editor=pref.edit();
							if(Connect.useIp!=null)editor.putString("useip", Connect.useIp);
							editor.commit();
							if(socket!=null&&socket.isConnected()){
								try {
									out=new DataOutputStream(socket.getOutputStream());
									in=new DataInputStream(socket.getInputStream());
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								is3G = false;
								if(socket.isConnected()){
									connectNum++;
									try {
										Toast.makeText(activity, "Vanst:服务器连接成功", Toast.LENGTH_SHORT).show();
									} catch (Exception e) {
										MyLog.i("error","Toast错误！");
									}
									rwPose = control;
									flag = true;
									disconnectShow = false;
									configFlag = 0;
//									if(isEnter && !is3G) readConfig2();
//									else if(isEnter && is3G) new ReadConfig().start();
									new ConnectThread().start();
									if(!isEnter)startActivity.enter();
								}else{
									flag = false;
									socket = null;
								}
							}
							for(SocketIP socketIP:sockets){
								if(socket != socketIP.socket){
									try {
										socketIP.socket.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						}
					});
					builder.show();
					*/
					}
				}
			}
		}
	};
	
	static public String ipLan2(){
		String serviceIP = getIPAddress2(activity);
//		Toast.makeText(activity, serviceIP, Toast.LENGTH_LONG).show();
		if(serviceIP == null) return null;
		char[] as = serviceIP.toCharArray();
		int i = 0;
		int j = 0;
		for(char a:as){
			if(a == '.'){
				i = j;
			}
			j++;
		}
		String rtn = "";
		for(j=0;j<=i;j++){
			rtn += as[j];
		}
		MyLog.i("rtn",rtn);
		return rtn;
	}
	static public String ipLan(){
		String serviceIP = getIPAddress(activity);
//		Toast.makeText(activity, serviceIP, Toast.LENGTH_LONG).show();
		if(serviceIP == null) return null;
		char[] as = serviceIP.toCharArray();
		int i = 0;
		int j = 0;
		for(char a:as){
			if(a == '.'){
				i = j;
			}
			j++;
		}
		String rtn = "";
		for(j=0;j<=i;j++){
			rtn += as[j];
		}
		MyLog.i("rtn",rtn);
		return rtn;
	}
	public class ProgressThread extends Thread{
		public void run() {
			handler.sendEmptyMessage(CONNECT_SINGLE);
		}
	}
	public class CheckThread extends Thread{
		boolean fflag = true;
		public void run(){
			while(flag && fflag){
				try {
					sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(!checkConnect(activity)){
					flag = false;
					VanstWidgetProvider.updateAppWidget(activity);
					handler.sendEmptyMessage(DISCONNECT_SINGLE);
				}else{
					MyLog.i("checkthread","OK");
				}
			}
		}
	}
	
	public void searchWifi(){
		progress++;
		if(progress >255) return;
		new Thread(){
			public void run() {
				int j = progress;
				handler.sendEmptyMessage(SEARCH_WIFI);
//				for(int i = 1;i<3;i++){
					Socket socket = new Socket();
					InetSocketAddress isa = new InetSocketAddress(lanIp+j,port);
					try {
						socket.connect(isa,1000);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(socket.isConnected()){
						SocketIP socketIP = new SocketIP();
						socketIP.socket = socket;
						socketIP.ip = lanIp+j;
						sockets.add(socketIP);
					}
//					long time2 = System.currentTimeMillis();
//					try {
//						if(time2 - time >= 3000) sleep(500);
//						else sleep(3000 - (time2-time) + 500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//				int j = progress;
//				handler.sendEmptyMessage(SEARCH_WIFI);
//				for(int i = 1;i<3;i++){
//					Socket socket = new Socket();
//					InetSocketAddress isa = new InetSocketAddress(lanIp+j,port);
//					long time = System.currentTimeMillis();
//					try {
//						socket.connect(isa,3000);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					if(socket.isConnected()){
//						SocketIP socketIP = new SocketIP();
//						socketIP.socket = socket;
//						socketIP.ip = lanIp+j;
//						sockets.add(socketIP);
//						break;
//					}
//					long time2 = System.currentTimeMillis();
//					try {
//						if(time2 - time >= 3000) sleep(500);
//						else sleep(3000 - (time2-time) + 500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
			};		
		}.start();
	}
	static public void phoneManage(){
		AlertDialog.Builder builder= new AlertDialog.Builder(activity);
		builder.setTitle("认证用户列表");
		builder.setItems(phones, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, final int which) {
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle("要去除认证用户"+phones[which]+"吗？");
				builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which1) {
						String [] linPhones = phones;
						phones = new String[phones.length-1];
						for(int i=0;i<phones.length;i++){
							if(i<which){
								phones[i] = linPhones[i];
							}else{
								phones[i] = linPhones[i+1];
							}
						}
						phoneManage();
					}
				});
				builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						phoneManage();
					}
				});
				builder.show();
			}
		});
		builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				final EditText editText = new EditText(activity);
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle("请输入要添加的号码");
				builder.setView(editText);
				editText.setInputType(InputType.TYPE_CLASS_PHONE);
				builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						if(editText.getText().toString()!=null && editText.getText().toString().length() != 0){
							String [] linPhones = phones;
							phones = new String[phones.length+1];
							for(int i=0;i<phones.length;i++){
								if(i == phones.length-1){
									phones[i] = editText.getText().toString();
								}else{
									phones[i] = linPhones[i];
								}
							}
							phoneManage();
						}else{
							phoneManage();
							Toast.makeText(activity, "号码不能为空", Toast.LENGTH_LONG).show();
						}
//						String [] linPhones = phones;
//						phones = new String[phones.length-1];
//						for(int i=0;i<phones.length;i++){
//							if(i<which){
//								phones[i] = linPhones[i];
//							}else{
//								phones[i] = linPhones[i+1];
//							}
//						}
					}
				});
				builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						phoneManage();
					}
				});
				builder.show();
			}
		});
		builder.setNeutralButton("保存", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle("请输入密码");
				builder.setIcon(android.R.drawable.ic_menu_edit);
				final EditText editText = new EditText(activity);
				editText.setHint("同步密码");
				editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				builder.setView(editText);
				builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						Connect.pwd = editText.getText().toString();
						Connect.isShared = 2;
						int count = 1;
						for(String phone:phones){
							count += phone.length()+1;
						}
						byte[] bytes = new byte[count];
						bytes[0] = 'e';
						int i = 1;
						for(String phone:phones){
							for(int j=0;j<phone.length();j++){
								bytes[i] = (byte) phone.charAt(j);
								i++;
							}
							bytes[i] = 's';
							i++;
						}
						Device.typeBytes = new byte[count];
						for(int j =0;j<count;j++){
							Device.typeBytes[j] = bytes[count-1-j];
						}
						Connect.filename = "bayern";
						Connect.askShare(editText.getText().toString());
						progressDialog = new ProgressDialog(activity);
						progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						progressDialog.setMessage("请稍等..等待密码验证");
						progressDialog.setButton("取消",new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface arg0,
									int arg1) {
								Connect.rwPose = Connect.control;
								progressDialog.dismiss();
							}
						});
						progressDialog.setCancelable(false);
						progressDialog.show();
					}
				});
				builder.setNegativeButton("取消", null);
				builder.show();
				deleteTpye("bayern");
			}
		});
		builder.setCancelable(false);
		builder.setNegativeButton("取消", null);
		builder.show();
	}
	
	public void connectService(final String ip,final int time){
		new Thread(){
			public void run(){
				isConnecting = true;
				for(int i = 0;i<3;i++){
					try {
						connectIp = ip;
						if(socket != null) {
							socket.close();
						}
						flag = false;
						VanstWidgetProvider.updateAppWidget(activity);
						if(ip.equals(_3GIp)) is3G = true;
						else is3G = false;
						socket=new Socket();
						newDate = false;
						InetSocketAddress isa = new InetSocketAddress(ip,port);
						socket.connect(isa,time);
						out=new DataOutputStream(socket.getOutputStream());
						in=new DataInputStream(socket.getInputStream());
						if(socket.isConnected()){
							connectNum++;
							rwPose = control;
							flag = true;
							VanstWidgetProvider.updateAppWidget(activity);
							disconnectShow = false;
							configFlag = 0;
							
							handler.sendEmptyMessage(CONNECT_SUCCED);
							return;
						}else{
							flag = false;
							VanstWidgetProvider.updateAppWidget(activity);
//							socket = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				handler.sendEmptyMessage(CONNECT_FAILED);
			}
		}.start();
	}
	public Connect(final Activity activity,int i){
		Connect.activity = activity;
		lanIp = ipLan();
		lastIp = localIp();
		if(lanIp == null){
			Toast.makeText(activity, "网络未连接，如受限制，请联系管理员", Toast.LENGTH_LONG).show();
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle("网络未连接！");
			builder.setMessage("请检查网络后再连接服务器，如受限制，请联系管理员");
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setPositiveButton("确认", null);
			builder.show();
		}else{
			if(progressDialog !=null) progressDialog.dismiss();
			progressDialog = new ProgressDialog(activity);
			progressDialog.setCancelable(false);
			if(i == 0x3cf) progressDialog.setTitle("正在为您连接移动网络中...");
			else {progressDialog.setTitle("正在为您连接网络中...");
//				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setMax(100);
			}
			progressDialog.setCancelable(false);
			progressDialog.setTitle("正在为您连接中。。。");
			progressDialog.setMessage("请稍等...");

			progressDialog.show();
			if(i == 0x3cf){
				is3G = true;
				connectService(_3GIp, 5000);
			}else if(!connectWifi(activity, defaultIp,1000)){
//				is3G = false;
//				connectService(useIp, 3000);
				is3G = false;
				lanIp = ipLan();
				sockets = new ArrayList<SocketIP>();
				progress = 0;
				handler.sendEmptyMessage(SEARCH_WIFI);
				new Thread(){
					public void run() {
						
//								for(int i = 0;i<100;i++){
//									try {
//										sleep(100);
//									} catch (InterruptedException e) {
//										e.printStackTrace();
//									}
//									progressHandler.sendEmptyMessage(i);
//								}
//								try {
//									sleep(1000);
//								} catch (InterruptedException e) {
//									e.printStackTrace();
//								}
//								progressHandler.sendEmptyMessage(100);
								try {
									sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
						handler.sendEmptyMessage(SEARCH_END);
						if(!sockets.isEmpty()){
							int i = 0;
							for(SocketIP socketIP:sockets){
								MyLog.i("SOCKET",socketIP.ip);
								i++;
							}
						}
					};
				}.start();
			}else{
//				((StartActivity)activity).progressBar.setProgress(((StartActivity)activity).progressBar.getMax());
				if(progressDialog != null) progressDialog.dismiss();
				startActivity.enter();
			}
		}
	}
	
	public Connect(final Activity activity,boolean isHead){
		Connect.activity = activity;
		lastIp = localIp();
		lanIp = ipLan();
		isConnecting = true;
		is3G = false;
		if(sockets != null){
			for(SocketIP socketIP:sockets){
				if(socketIP !=null && socketIP.socket !=null)
				try {
					socketIP.socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(socket != null){
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		sockets = new ArrayList<SocketIP>();
		outs = null;
		ins = null;
		if(!connectWifi(activity, defaultIp,1000)){
			progress = 0;
			handler.sendEmptyMessage(SEARCH_WIFI);
			try {
				progressDialog = new ProgressDialog(activity);
//				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setMax(100);
				progressDialog.setCancelable(false);
				progressDialog.setTitle("正在为您连接中。。。");
				progressDialog.setMessage("请稍等...");
				if(isHead)
				progressDialog.show();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			new Thread(){
				public void run() {
//					for(int i = 0;i<100;i++){
//						try {
//							sleep(100);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//						progressHandler.sendEmptyMessage(i);
//					}
//					try {
//						sleep(1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					progressHandler.sendEmptyMessage(100);
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handler.sendEmptyMessage(SEARCH_END);
					if(sockets!=null && !sockets.isEmpty()){
						int i = 0;
						for(SocketIP socketIP:sockets){
							MyLog.i("SOCKET",socketIP.ip);
							i++;
						}
					}
				};
			}.start();
		}
		
//		Connect.activity = activity;
//		lastIp = localIp();
//		progressDialog = new ProgressDialog(activity);
//		progressDialog.setCancelable(false);
//		progressDialog.setTitle("正在为您连接网络中。。。");
//		progressDialog.setMessage("请稍等...");
//		progressDialog.show();
//		connectService(useIp, 5000);
	}
	public Connect(Activity activity,String ip,boolean isHead){
		Connect.activity = activity;
		lastIp = localIp();
		progressDialog = new ProgressDialog(activity);
		progressDialog.setCancelable(false);
		progressDialog.setTitle("正在为您连接移动网络中。。。");
		progressDialog.setMessage("请稍等...");
		if(isHead)
		progressDialog.show();
		if(sockets != null){
			for(SocketIP socketIP:sockets){
				if(socketIP !=null && socketIP.socket !=null)
				try {
					socketIP.socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		outs = null;
		sockets = null;
		connectService(_3GIp, 5000);
	}
	static public boolean checkConnect(Activity activity){
		try {
			if(flag){
				socket.sendUrgentData(1);
				return true;
			}else{
				return false;
			}
		} catch (IOException e) {
			return false;
		}
	}
	
	class LongTimeConnect extends Thread{
		public void run() {
			socket=new Socket();
			InetSocketAddress isa = new InetSocketAddress(connectIp,port);
			try {
				socket.connect(isa,5000);
			} catch (IOException e) {
				e.printStackTrace();
			}
			handler.sendEmptyMessage(0x93);
		}
	}
	static public void discoonectWifi(){
		try {
			if(socket != null&&is3G) {
				if(out != null){
					int crc = 0xfe + 0xf6 + 0x02 + 0xfa + 0x88;
					out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)0x02,(byte)0xfa,(byte)0x88,(byte)(crc/256),(byte)(crc%256)});
//					out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)0x02,(byte)0xfa,(byte)0x88});
//					out.writeByte((byte)(crc/256));
//					out.writeByte((byte)(crc%256));
					out.flush();
				}
				socket.close();
			}
		} catch (Exception e) {
		}
	}
	public boolean connectWifi(Activity activity,String ip,int time){
			try {
				MyLog.i("ip",ip);
				connectIp = ip;
				if(socket != null) {
					socket.close();
				}
				socket=new Socket();
				newDate = false;
				InetSocketAddress isa = new InetSocketAddress(ip,port);
				socket.connect(isa,time);
				out=new DataOutputStream(socket.getOutputStream());
				in=new DataInputStream(socket.getInputStream());
				
				if(socket.isConnected()){
					if(ip.equals(_3GIp)) is3G = true;
					else is3G = false;
					MyLog.i("3g",is3G+"");
					connectNum++;
					SharedPreferences pref=activity.getSharedPreferences("config", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					if(!is3G){
						editor.putInt("connect", 1);
					}else{
						editor.putInt("connect", 2);
					}
					editor.commit();
					try {
						Toast.makeText(activity, "Vanst:服务器连接成功", Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						MyLog.i("error","Toast错误！");
					}
					rwPose = control;
					flag = true;
					widgetConnect = false;
					VanstWidgetProvider.updateAppWidget(activity);
					disconnectShow = false;
					configFlag = 0;
//					if(isEnter && !is3G) readConfig2();
//					else if(isEnter && is3G) new ReadConfig().start();
					new ConnectThread().start();
					return true;
				}else{
					flag = false;
					socket = null;
					return false;
				}
			} catch (Exception e) {
				flag = false;
				VanstWidgetProvider.updateAppWidget(activity);
				socket = null;
				return false;
			}
	}
	 static public  String readSms(){
    	 Cursor cursor = activity.managedQuery(Uri.parse("content://sms/inbox"),
         		new String[]{"address", "person", "body", "date"}, null, null, "date desc");
         if(cursor == null || cursor.getCount() == 0) {        
         	MyLog.i("TAG", "cursor null or count is 0");          
         	return null;         }     
         cursor.moveToFirst();  
         String address = cursor.getString(0);        
         String person = cursor.getString(1); 
         String body = cursor.getString(2);    
         String date = cursor.getString(3);
         MyLog.i("address",address);
         if(phoneNumber!=null&&address.equals(phoneNumber)||address.equals("+86"+phoneNumber)){
        	 return body; 
         }
         if(person !=null)
	         MyLog.i("person",person);
	         MyLog.i("body", body);
	         MyLog.i("date", date); 
         do{
        cursor.moveToNext();
         address = cursor.getString(0);        
         person = cursor.getString(1); 
         body = cursor.getString(2);    
         date = cursor.getString(3);
         if(address.equals(phoneNumber)||address.equals("+86"+phoneNumber)){
        	 return body; 
         }
         MyLog.i("address",address);
         if(person !=null)
	         MyLog.i("person",person);
	         MyLog.i("body", body);
	         MyLog.i("date", date);   
         }while(!cursor.isLast());
         return null;
    }
	private static String getIp(String string){
			int first = string.indexOf("ipad")+4;
			int last = string.lastIndexOf("ipad");
			
			if(first < 0 || first > string.length() || last < 0 || last > string.length() || first >= last)
				return null;
			return string.substring(first, last);
		}
	static public void deleteSms(){ 
		try {   
	        ContentResolver CR = activity.getContentResolver();   
	        Uri uriSms = Uri.parse("content://sms/inbox");   
	        Cursor c = CR.query(uriSms,   
	                new String[] { "_id", "thread_id" ,"address","body"}, null, null,"date");   
	        if (null != c && c.moveToFirst()) {   
	            do {   
	                if(c.getString(2).equals(phoneNumber)||c.getString(2).equals("+86"+phoneNumber)){
	                	String ip = getIp(c.getString(3));
	                	if(ip !=null){
	                		_3GIp = ip;
		                	synchronizationHandler.sendEmptyMessage(PRE_SYNCHRONIZATIONE);
		                	synchronizationHandler.sendEmptyMessage(_3GIP_REFRESH);
	                	}
	                	MyLog.i("3GIP",_3GIp);
	                	long threadId = c.getLong(1);  
	                	CR.delete(Uri.parse("content://sms/conversations/" + threadId),null, null);   
	                }
	                  
	            } while (c.moveToNext());   
	        }   
	    } catch (Exception e) {   
	        MyLog.i("deleteSMS", "Exception:: " + e);   
	    }  
		
	}
	 
	static public void PhoneCall(Activity activity){
		Uri localUri = Uri.parse("tel:" + phoneNumber);
		Intent call = new Intent(Intent.ACTION_CALL, localUri);
		call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			needStopPhone = true;
			activity.startActivity(call);
		} catch (Exception e) {
		}
	}
	static public String getIPAddress(Context ctx){
		WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);     
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();     
		int ipAddress = wifiInfo.getIpAddress();     
		String ip = intToIp(ipAddress);     
		return ip;    
		
	}
	static public String intToIp(int i){
		return (i & 0xff) + "." +((i >> 8 ) & 0xFF) +"." +((i >> 16 ) & 0xFF)+"."+((i >> 24 ) & 0xFF ) ;
	}
	static public String getIPAddress2(Context ctx){  
        try{
			 for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				 NetworkInterface intf = en.nextElement();  
	                for (Enumeration<InetAddress> enumIpAddr = intf  
	                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {  
	                    InetAddress inetAddress = enumIpAddr.nextElement();  
	                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {  
	                        
	                    	return inetAddress.getHostAddress().toString();  
	                    }  
	                }  
			 }
		}catch (SocketException e) {
		}
        return null; 
    }  
	static class ReadConfigThread extends Thread{
		public void run() {
			try {
				sleep(10000);
				if(configFlag == 0){
					synchronizationHandler.sendEmptyMessage(REDA_CONFIG2_FAILED);
//					configFlag = 2;
//					rwPose = control;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			MyLog.i("ReadConfigThread","over");
		}
	}
	static public void readFile(String name){
		char []cs = name.toCharArray();
		try{
			if(flag && out !=null){
				MyLog.i("name",name);
				MyLog.i("readFile","readFile");
				readName = name;
				readCf = false;
				dateBytes = new byte[progressLength];
				dateBytesP = 0;
				indates = new ArrayList<Byte>();
				rwPose = download;
				int crc = 0xfe + 0xf6 + 0x03+cs.length +0xf6 + lastIp;
				for(char c:cs){
					crc += c;
				}
				byte[] writeybytes = new byte[8+cs.length];
				writeybytes[0] = (byte)0xfe;
				writeybytes[1] = (byte)0xf6;
				writeybytes[2] = (byte)(0x03+cs.length);
				writeybytes[3] = (byte)0xf6;
				writeybytes[4] = (byte)lastIp;
				for(int i=0;i<cs.length;i++){
					writeybytes[5+i] = (byte)cs[i];
				}
				writeybytes[5+cs.length] = (byte)0x00;
				writeybytes[6+cs.length] = (byte)(crc/256);
				writeybytes[7+cs.length] = (byte)(crc%256);
				out.write(writeybytes);
//				out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)(0x03+cs.length),(byte)0xf6,(byte)lastIp});
//				for(char c:cs){		
//					out.writeByte((byte)c);
//				}
//				out.writeByte(0x00);
//				out.writeByte((byte)(crc/256));
//				out.writeByte((byte)(crc%256));
				out.flush();
//				new ReadConfigThread().start();
			}else{
				Toast.makeText(activity,"服务器未连接",Toast.LENGTH_LONG).show();
			}
		}catch (Exception e) {
		}
	}
	static public void loadBitmap(String name){
		char []cs = name.toCharArray();
		MyLog.i("name",name);
		try{
			if(flag && out !=null){
				readName = name;
				readCf = false;
//				indates = new ArrayList<Byte>();
				rwPose = loadBitmap;
				int crc = 0xfe + 0xf6 + 0x03+cs.length +0xfd + lastIp;
				for(char c:cs){
					crc += c;
				}
				byte[] writeybytes = new byte[8+cs.length];
				writeybytes[0] = (byte)0xfe;
				writeybytes[1] = (byte)0xf6;
				writeybytes[2] = (byte)(0x03+cs.length);
				writeybytes[3] = (byte)0xfd;
				writeybytes[4] = (byte)lastIp;
				for(int i = 0;i<cs.length;i++){
					writeybytes[5+i] = (byte)cs[i];
				}
				writeybytes[5+cs.length] = (byte)0x00;
				writeybytes[6+cs.length] = (byte)(crc/256);
				writeybytes[7+cs.length] = (byte)(crc%256);
				out.write(writeybytes);
//				out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)(0x03+cs.length),(byte)0xfd,(byte)lastIp});
//				for(char c:cs){		
//					out.writeByte((byte)c);
//				}
//				out.writeByte(0x00);
//				out.writeByte((byte)(crc/256));
//				out.writeByte((byte)(crc%256));
				out.flush();
			}else{
				Toast.makeText(activity,"服务器未连接",Toast.LENGTH_LONG).show();
			}
		}catch (Exception e) {
		}
	}
	
	
	static public void readConfig2(){
		try {
			if(flag && out != null){
				fileList = new ArrayList<String>();
				rwPose = readConfig;
				int crc = 0xfe + 0xf6 + 0x03 + 0xfc + lastIp + 1;
				out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)0x03,(byte)0xfc,(byte)lastIp,0x01,(byte)(crc/256),(byte)(crc%256)});
//				out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)0x03,(byte)0xfc,(byte)lastIp,0x01});
//				out.writeByte((byte)(crc/256));
//				out.writeByte((byte)(crc%256));
				out.flush();
				configFlag = 0;
				if(getList != null) getList.isAlive = false;
				getList = new GetList();
				getList.start();
				MainActivity.progressDialog = new ProgressDialog(activity);
				MainActivity.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				MainActivity.progressDialog.setTitle("正在获取配置文件列表");
				MainActivity.progressDialog.setIcon(android.R.drawable.ic_dialog_alert);
				MainActivity.progressDialog.setMessage("请稍等...");
				MainActivity.progressDialog.setButton("取消",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0,
							int arg1) {
						if(getList != null) getList.isAlive = false;
						Connect.rwPose = Connect.control;
						MainActivity.progressDialog.dismiss();
					}
				});
				MainActivity.progressDialog.setCancelable(false);
				MainActivity.progressDialog.show();
			}
		} catch (Exception e) {
		}
	}
	static public void readConfig(){
		try {
			if(flag  && out != null){
				readCf = true;
				rwPose = download;
				int crc = 0xfe + 0xf6 + 0x05 +0xf6 + lastIp + 'c' + 'f';
				out.write(new byte[]{(byte)0xfe,(byte)0xf6,0x05,(byte)0xf6,(byte)lastIp,'c','f',0x00,(byte)(crc/256),(byte)(crc%256)});
//				out.write(new byte[]{(byte)0xfe,(byte)0xf6,0x05,(byte)0xf6,(byte)lastIp,'c','f',0x00});
//				out.writeByte((byte)(crc/256));
//				out.writeByte((byte)(crc%256));
				out.flush();
				new ReadConfigThread().start();
			}else{
				Toast.makeText(activity,"服务器未连接",Toast.LENGTH_LONG).show();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static public void readPhone(){
		if(Connect.is3G){
			loadBitmap("bayern");
			progressDialog = new ProgressDialog(activity);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage("正在获取认证记录信息");
			progressDialog.setButton("取消",new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface arg0,
						int arg1) {
					Connect.rwPose = Connect.control;
					progressDialog.dismiss();
				}
			});
			progressDialog.setCancelable(false);
			progressDialog.show();
		}else{
			Toast.makeText(activity, "必须在3G模式下使用", Toast.LENGTH_LONG).show();
		}
	}
	
	static class Writetypesynchronizetion extends Thread{
		boolean sflag;
		public void run() {
			try {
				Thread.sleep(50000);
				if(rwPose == control || sflag){
					
				}else{
					writeTypeLag = true;
					writeTypeDataLag = true;
					rwPose = control;
					synchronizationHandler.sendEmptyMessage(1);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	static class Configsynchronization extends Thread{
		static int ss;
		boolean sflag;
		public void run() {
			try {
				Thread.sleep(90000);
				if(rwPose == control || sflag){
					
//				}else if(ss < 3){
//					rwPose = delete;
//					ss++;
//					deleteConfig();
				}else{
//					rwPose = control;
					ss = 0;
//					synchronizationHandler.sendEmptyMessage(SYNCHRONIZATIONG_FAIL_SINGLE);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	static public void deleteConfig(){
		try {
			if(flag && out != null){
				if(configsynchronization != null) configsynchronization.sflag = true;
				configsynchronization = new Configsynchronization();
				configsynchronization.start();
				rwPose = delete;
				MyLog.i("deleteConfig","deleteConfig");
				int crc = 0xfe + 0xf6 + 0x05 + 0xff + lastIp + 'c' + 'f';
				out.write(new byte[]{(byte)0xfe,(byte)0xf6,0x05,(byte)0xff,(byte)lastIp,'c','f',0x00,(byte)(crc/256),(byte)(crc%256)});
//				out.write(new byte[]{(byte)0xfe,(byte)0xf6,0x05,(byte)0xff,(byte)lastIp,'c','f',0x00});
//				out.writeByte((byte)(crc/256));
//				out.writeByte((byte)(crc%256));
				out.flush();
			}else{
				Toast.makeText(activity,"服务器未连接",Toast.LENGTH_LONG).show();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static class CheckTypeDate extends Thread{
		boolean sflag = false;
		public void run() {
			try {
				sleep(20000);
				if(!sflag){
					writeTypeDataLag = true;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	public void writeBitmap(){
		new Thread(){
			public void run() {
				rwPose = typedo;
				MyLog.i("writeTypeDate","writeTypeDate");
				int j = 0;
				MyLog.i("Device.typeBytes.length",Device.typeBytes.length+"");
				int max = Device.typeBytes.length / 64;
				int send = 0;
				for(j = 0;j < Device.typeBytes.length / 64;j++){
					try {
						do{
							writeTypeDataLag = false;
							writeTypeSucceed = false;
							MyLog.i("j",j+"");
							int crc = 0xfe + 0xf6 + 0x43 + 0xf8 + lastIp + 0x40;
							for(int k = 0;k < 64 ;k++){
								crc += Device.typeBytes[j * MAXIO_COUNT + k];
								if(Device.typeBytes[j * MAXIO_COUNT + k] < 0) crc += 256;
							}
							byte[] writebytes = new byte[8+MAXIO_COUNT];
							writebytes[0] = (byte)0xfe;
							writebytes[1] = (byte)0xf6;
							writebytes[2] = (byte)(MAXIO_COUNT+3);
							writebytes[3] = (byte)0xf8;
							writebytes[4] = (byte)lastIp;
							writebytes[5] = (byte)MAXIO_COUNT;
							for(int i = 0 ;i < MAXIO_COUNT;i++){
								writebytes[i+6] = Device.typeBytes[j * MAXIO_COUNT + i];
							}
							writebytes[6 + MAXIO_COUNT] = (byte)(crc/256);
							writebytes[7 + MAXIO_COUNT] = (byte)(crc%256);
							out.write(writebytes);
//							out.writeByte((byte)0xfe);
//							out.writeByte((byte)0xf6);
//							out.writeByte((byte)0x43);
//							out.writeByte((byte)0xf8);
//							out.writeByte((byte)lastIp);
//							out.writeByte((byte)0x40);
//							out.write(Device.typeBytes, j * MAXIO_COUNT, MAXIO_COUNT);
//							out.writeByte((byte)(crc/256));
//							out.writeByte((byte)(crc%256));
							out.flush();
							CheckTypeDate checkTypeDate = new CheckTypeDate();
							checkTypeDate.start();
							while(!writeTypeDataLag && !writeTypeSucceed && !writeTypeLag && rwPose != control){
										
							}
							if(rwPose == control || writeTypeLag) return;
							if(writeTypeSucceed) checkTypeDate.sflag = true;
							if(send != j *100/max){
								send = j * 100 / max;
								progressHandler.sendEmptyMessage(send);
							}
						}while(writeTypeDataLag);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(writeTypeLag) return;
				}
				if(Device.typeBytes.length % 64 > 0){
					try {
						do{		
							int crc = 0xfe + 0xf6 + Device.typeBytes.length%64+3 + 0xf8 + lastIp + Device.typeBytes.length%64;
							for(int k = 0;k < Device.typeBytes.length%64 ;k++){
								crc += Device.typeBytes[Device.typeBytes.length/MAXIO_COUNT * MAXIO_COUNT + k];
								if(Device.typeBytes[Device.typeBytes.length/MAXIO_COUNT * MAXIO_COUNT + k] < 0) crc += 256;
							}
							byte[] writebytes = new byte[8+Device.typeBytes.length%64];
							writebytes[0] = (byte)0xfe;
							writebytes[1] = (byte)0xf6;
							writebytes[2] = (byte)(Device.typeBytes.length%64+3);
							writebytes[3] = (byte)0xf8;
							writebytes[4] = (byte)lastIp;
							writebytes[5] = (byte)(Device.typeBytes.length%64);
							for(int i = 0 ;i < Device.typeBytes.length%64;i++){
								writebytes[i+6] = Device.typeBytes[Device.typeBytes.length/MAXIO_COUNT * MAXIO_COUNT + i];
							}
							writebytes[6 + Device.typeBytes.length%64] = (byte)(crc/256);
							writebytes[7 + Device.typeBytes.length%64] = (byte)(crc%256);
							out.write(writebytes);
							
							
//							out.writeByte((byte)0xfe);
//							out.writeByte((byte)0xf6);
//							out.writeByte((byte)(Device.typeBytes.length%64+3));
//							out.writeByte((byte)0xf8);
//							out.writeByte((byte)lastIp);
//							out.writeByte((byte)(Device.typeBytes.length%64));
//							out.write(Device.typeBytes,Device.typeBytes.length/MAXIO_COUNT * MAXIO_COUNT,Device.typeBytes.length%64);
//							out.writeByte((byte)(crc/256));
//							out.writeByte((byte)(crc%256));
							out.flush();
							CheckTypeDate checkTypeDate = new CheckTypeDate();
							checkTypeDate.start();
							while(!writeTypeDataLag && !writeTypeSucceed && !writeTypeLag){
								
							}
							progressHandler.sendEmptyMessage(100);
							if(writeTypeLag) return;
							if(writeTypeSucceed) checkTypeDate.sflag = true;
						}while(writeTypeDataLag);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
//				MyLog.i("upRoom", ""+upRoom);
//				if(upRoom >= upRooms.length-1){
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
					handler.sendEmptyMessage(WRITE_CL);
//					rwPose = control;
//				}else{
//					upRoom++;
//					try {
//						sleep(500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					askShare(pwd);
////					handler.sendEmptyMessage(UPNEXT);
//				}
					
			};
		}.start();
	}
	
	static public void writeTypeDate(){
		if(flag && out != null){
			rwPose = typedo;
			MyLog.i("writeTypeDate","writeTypeDate");
			int j = 0;
			MyLog.i("Device.typeBytes.length",Device.typeBytes.length+"");
			for(j = 0;j < Device.typeBytes.length / 64;j++){
				try {
					do{
						writeTypeDataLag = false;
						writeTypeSucceed = false;
						MyLog.i("j",j+"");
						int crc = 0xfe + 0xf6 + 0x43 + 0xf8 + lastIp + 0x40;
						for(int k = 0;k < 64 ;k++){
							crc += Device.typeBytes[j * MAXIO_COUNT + k];
							if(Device.typeBytes[j * MAXIO_COUNT + k] < 0) crc += 256;
						}
						byte[] writebytes = new byte[8+MAXIO_COUNT];
						writebytes[0] = (byte)0xfe;
						writebytes[1] = (byte)0xf6;
						writebytes[2] = (byte)(MAXIO_COUNT+3);
						writebytes[3] = (byte)0xf8;
						writebytes[4] = (byte)lastIp;
						writebytes[5] = (byte)MAXIO_COUNT;
						for(int i = 0 ;i < MAXIO_COUNT;i++){
							writebytes[i+6] = Device.typeBytes[j * MAXIO_COUNT + i];
						}
						writebytes[6 + MAXIO_COUNT] = (byte)(crc/256);
						writebytes[7 + MAXIO_COUNT] = (byte)(crc%256);
						out.write(writebytes);
						
//						out.writeByte((byte)0xfe);
//						out.writeByte((byte)0xf6);
//						out.writeByte((byte)0x43);
//						out.writeByte((byte)0xf8);
//						out.writeByte((byte)lastIp);
//						out.writeByte((byte)0x40);
//						out.write(Device.typeBytes, j * MAXIO_COUNT, MAXIO_COUNT);
//						out.writeByte((byte)(crc/256));
//						out.writeByte((byte)(crc%256));
						out.flush();
						CheckTypeDate checkTypeDate = new CheckTypeDate();
						checkTypeDate.start();
						while(!writeTypeDataLag && !writeTypeSucceed && !writeTypeLag){
									
						}
						if(writeTypeLag) return;
						if(writeTypeSucceed) checkTypeDate.sflag = true;
					}while(writeTypeDataLag);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(writeTypeLag) return;
			}
			if(Device.typeBytes.length % 64 > 0){
				try {
					do{		
						int crc = 0xfe + 0xf6 + Device.typeBytes.length%64+3 + 0xf8 + lastIp + Device.typeBytes.length%64;
						for(int k = 0;k < Device.typeBytes.length%64 ;k++){
							crc += Device.typeBytes[Device.typeBytes.length/MAXIO_COUNT * MAXIO_COUNT + k];
							if(Device.typeBytes[Device.typeBytes.length/MAXIO_COUNT * MAXIO_COUNT + k] < 0) crc += 256;
						}
						byte[] writebytes = new byte[8+Device.typeBytes.length%64];
						writebytes[0] = (byte)0xfe;
						writebytes[1] = (byte)0xf6;
						writebytes[2] = (byte)(Device.typeBytes.length%64+3);
						writebytes[3] = (byte)0xf8;
						writebytes[4] = (byte)lastIp;
						writebytes[5] = (byte)(Device.typeBytes.length%64);
						for(int i = 0 ;i < Device.typeBytes.length%64;i++){
							writebytes[i+6] = Device.typeBytes[Device.typeBytes.length/MAXIO_COUNT * MAXIO_COUNT + i]; 
						}
						writebytes[6 + Device.typeBytes.length%64] = (byte)(crc/256);
						writebytes[7 + Device.typeBytes.length%64] = (byte)(crc%256);
						out.write(writebytes);
//						out.writeByte((byte)0xfe);
//						out.writeByte((byte)0xf6);
//						out.writeByte((byte)(Device.typeBytes.length%64+3));
//						out.writeByte((byte)0xf8);
//						out.writeByte((byte)lastIp);
//						out.writeByte((byte)(Device.typeBytes.length%64));
//						out.write(Device.typeBytes,Device.typeBytes.length/MAXIO_COUNT * MAXIO_COUNT,Device.typeBytes.length%64);
//						out.writeByte((byte)(crc/256));
//						out.writeByte((byte)(crc%256));
						out.flush();
						CheckTypeDate checkTypeDate = new CheckTypeDate();
						checkTypeDate.start();
						while(!writeTypeDataLag && !writeTypeSucceed && !writeTypeLag){
							
						}
						if(writeTypeLag) return;
						if(writeTypeSucceed) checkTypeDate.sflag = true;
					}while(writeTypeDataLag);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			rwPose = control;
			Toast.makeText(activity, "同步成功", Toast.LENGTH_LONG).show();
			MainActivity.musicDo.musicInfo(activity);
			MainActivity.progressDialog.cancel();
			
			if(filename.equals("WIFI")){
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle("是否要重启WIFI模块？");
				builder.setIcon(android.R.drawable.ic_dialog_info);
				builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						try {
							int crc = 0xfe + 0xf6 + 0x02 + 0xfa + 0x77;
							out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)0x02,(byte)0xfa,(byte)0x77,(byte)(crc/256),(byte)(crc%256)});
//							out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)0x02,(byte)0xfa,(byte)0x77});
//							out.writeByte((byte)(crc/256));
//							out.writeByte((byte)(crc%256));
							out.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				builder.setNegativeButton("取消", null);
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		}
	}
	static public void reStartWifi(){
		if(flag && out != null){
			try {
				int crc = 0xfe + 0xf6 + 0x02 + 0xfa + 0x77;
				out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)0x02,(byte)0xfa,(byte)0x77,(byte)(crc/256),(byte)(crc%256)});
//				out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)0x02,(byte)0xfa,(byte)0x77});
//				out.writeByte((byte)(crc/256));
//				out.writeByte((byte)(crc%256));
				out.flush();
				Toast.makeText(activity, "重启模块", Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	static public void writeTpye(String name){
		char []cs = name.toCharArray();
		if(flag && out != null){
			MyLog.i("writeType","writeType");
			rwPose = typeready;
			try {
				int crc = 0xfe + 0xf6 + 0x07+cs.length + 0xf7 + lastIp +  Device.typeBytes.length/256/256/256 + 
				  Device.typeBytes.length/256/256%256 +
				  Device.typeBytes.length/256%256 +
				  Device.typeBytes.length%256;  
					for(char c:cs){
						crc += c;
					}
					
				byte[] writebytes = new byte[12+cs.length];
				writebytes[0] = (byte)0xfe;
				writebytes[1] = (byte)0xf6;
				writebytes[2] = (byte)(0x07+cs.length);
				writebytes[3] = (byte)(0xf7);
				writebytes[4] = (byte)lastIp;
				writebytes[5] = (byte)(Device.typeBytes.length/256/256/256);
				writebytes[6] = (byte)(Device.typeBytes.length/256/256%256);
				writebytes[7] = (byte)(Device.typeBytes.length/256%256);
				writebytes[8] = (byte)(Device.typeBytes.length%256);
				for(int i = 0;i<cs.length;i++){
					writebytes[9+i] = (byte)cs[i];
				}
				writebytes[9+cs.length] = (byte)0x00;
				writebytes[10+cs.length]= (byte)(crc/256);
				writebytes[11+cs.length]= (byte)(crc%256);
				out.write(writebytes);
//				out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)(0x07+cs.length),(byte)0xf7,(byte)lastIp,(byte)(Device.typeBytes.length/256/256/256),
//																							(byte)(Device.typeBytes.length/256/256%256),
//																							(byte)(Device.typeBytes.length/256%256),
//																							(byte)(Device.typeBytes.length%256)});
//				for(char c :cs){
//					out.writeByte(c);
//				}
//				out.writeByte(0);
//				out.writeByte((byte)(crc/256));
//				out.writeByte((byte)(crc%256));
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	static public void deleteTpye(String name){
		filename = name;
		char []cs = name.toCharArray();
		if(flag && out != null){
			if(writetypesynchronizetion != null) writetypesynchronizetion.sflag = true;
			if(name.equals("cl")){
				writetypesynchronizetion = new Writetypesynchronizetion();
				writetypesynchronizetion.start();
				writeTypeLag = false;
			}
			MyLog.i("deleteTpye","deleteType");
			rwPose = typedelete;
			try {
				int crc = 0xfe + 0xf6 + 0x03+cs.length +0xff + lastIp;
				for(char c:cs){
					crc += c;
				}
				byte[] writebytes = new byte[8+cs.length];
				writebytes[0] = (byte)0xfe;
				writebytes[1] = (byte)0xf6;
				writebytes[2] = (byte)(0x03+cs.length);
				writebytes[3] = (byte)0xff;
				writebytes[4] = (byte)lastIp;
				for(int i = 0;i<cs.length;i++){
					writebytes[5+i]= (byte)cs[i];
				}
				writebytes[5+cs.length]=(byte)0x00;
				writebytes[6+cs.length]=(byte)(crc/256);
				writebytes[7+cs.length]=(byte)(crc%256);
				out.write(writebytes);
//				out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)(0x03+cs.length),(byte)0xff,(byte)lastIp,});
//				for(char c:cs){
//					out.writeByte(c);
//				}
//				out.writeByte(0);
//				out.writeByte((byte)(crc/256));
//				out.writeByte((byte)(crc%256));
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			Toast.makeText(activity,"服务器未连接",Toast.LENGTH_LONG).show();
		}
		
	}
	
	static public void writeConfig(){
		try {
			MyLog.i("writeConfig","writeConfig");
			if(flag && out != null){
				rwPose = upLoad;
				int crc = 0xfe + 0xf6 + 0x09 + 0xf7 + lastIp + 0x17 + 'c'+'f';
				out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)0x09,(byte)0xf7,(byte)lastIp,0x00,0x00,0x00,0x17,'c','f',0x00,(byte)(crc/256),(byte)(crc%256)});
//				out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)0x09,(byte)0xf7,(byte)lastIp,0x00,0x00,0x00,0x17,'c','f',0x00});
//				MyLog.i("crc",crc+"");
//				out.writeByte((byte)(crc/256));
//				out.writeByte((byte)(crc%256));
				out.flush();
			}else{
				Toast.makeText(activity,"服务器未连接",Toast.LENGTH_LONG).show();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	static public void getPose(byte type){
		try {
			if(flag){
//				rwPose = control;
				if(rwPose != control) {
					try {
						new RefreshPose().start();
					} catch (Exception e) {
					}
				}else{
					MyLog.i("getPose","getPose");
					int crc = 0xfe + 0xf6 + 0x06 + 0xf1 + type;
					if(type < 0) crc += 256;
					if(is3G || outs == null || outs.length == 0){
						out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)0x06,(byte)0xf1,(byte)type,0,0,0,0,(byte)(crc/256),(byte)(crc%256)});
//						out.writeByte((byte)0xFE);
//						out.writeByte((byte)0xF6);
//						out.writeByte((byte)0x06);
//						out.writeByte((byte)0xf1);
//						out.writeByte(type);
//						out.writeByte(0x00);
//						out.writeByte(0x00);
//						out.writeByte(0x00);
//						out.writeByte(0x00);
//						out.writeByte((byte)(crc/256));
//						out.writeByte((byte)(crc%256));
						out.flush();
					}else{
						for(DataOutputStream out:outs){
							out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)0x06,(byte)0xf1,(byte)type,0,0,0,0,(byte)(crc/256),(byte)(crc%256)});
//							out.writeByte((byte)0xFE);
//							out.writeByte((byte)0xF6);
//							out.writeByte((byte)0x06);
//							out.writeByte((byte)0xf1);
//							out.writeByte(type);
//							out.writeByte(0x00);
//							out.writeByte(0x00);
//							out.writeByte(0x00);
//							out.writeByte(0x00);
//							out.writeByte((byte)(crc/256));
//							out.writeByte((byte)(crc%256));
							out.flush();
						}
					}
				}
			}else{
				flag = false;
				VanstWidgetProvider.updateAppWidget(activity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static public void writeSelect(byte[] bytes){ 
		try {
			if(flag){
				rwPose = control;
				succeed = false;
				int sum = 0xfe+0xf6+controllen;
				for(byte b:bytes){
					sum += b;
					if(b < 0) sum += 256;
				}
				if(is3G || outs == null || outs.length == 0){
					out.write(new byte[]{(byte)0xFe,(byte)0xf6,(byte)controllen,bytes[0],bytes[1],bytes[2],bytes[3],bytes[4],bytes[5],(byte)(sum/256),(byte)(sum%256)});
//					out.writeByte((byte)0xFE);
//					out.writeByte((byte)0xF6);
//					out.writeByte((byte)controllen);
//					out.write(bytes);
//					out.writeByte((byte)(sum/256));
//					out.writeByte((byte)(sum%256));
					out.flush();
				}else
				for(DataOutputStream out:outs){
					out.write(new byte[]{(byte)0xFe,(byte)0xf6,(byte)controllen,bytes[0],bytes[1],bytes[2],bytes[3],bytes[4],bytes[5],(byte)(sum/256),(byte)(sum%256)});
//					out.writeByte((byte)0xFE);
//					out.writeByte((byte)0xF6);
//					out.writeByte((byte)controllen);
//					out.write(bytes);
//					out.writeByte((byte)(sum/256));
//					out.writeByte((byte)(sum%256));
					out.flush();
				}
			}else{
				flag = false;
				VanstWidgetProvider.updateAppWidget(activity);
			}
		} catch (Exception e) {	
			e.printStackTrace();
		}
	}
	static public void askShare(String password){
		if(flag){
			MyLog.i("Ask", "AskShare");
			try {
				rwPose = Connect.password; 
				int crc = 0xfe + 0xf6 + 3 + password.length() + 0xfb + lastIp;
				byte[] bytes = new byte[5+password.length()+3];
				bytes[0] = (byte)0xfe;
				bytes[1] = (byte)0xf6;
				bytes[2] = (byte)(3+password.length());
				bytes[3] = (byte)0xfb;
				bytes[4] = (byte)lastIp;
				for(int i = 0;i < password.length();i++){
					bytes[i+5] = (byte) password.charAt(i);
					crc += password.charAt(i);
				}
				bytes[5+password.length()] = (byte)0x00;
				bytes[6+password.length()] = (byte)(crc/256);
				bytes[7+password.length()] = (byte)(crc%256);
				out.write(bytes);		
//				out.writeByte((byte)0xFE);
//				out.writeByte((byte)0xF6);
//				out.writeByte((byte)(3+password.length()));
//				out.writeByte((byte)0xFB);
//				out.writeByte((byte)lastIp);
//				for(int i = 0;i < password.length();i++){
//					out.writeByte((byte)password.charAt(i));
//					crc += password.charAt(i);
//				}
//				out.write(00);
//				out.writeByte((byte)(crc/256));
//				out.writeByte((byte)(crc%256));
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}else{
			try {
				Toast.makeText(activity, "服务器未连接！", Toast.LENGTH_LONG).show();
			} catch (Exception e) {
			}
			
		}
	}
	static public void clearAllFiles(){
		if(flag && out != null){
			try {
				int crc = 0xfe + 0xf6 + 0x06 + 0xf9;
				out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)0x06,(byte)0xf9,0,0,0,0,0,(byte)(crc/256),(byte)(crc%256)});
//				out.writeByte((byte)0xFE);
//				out.writeByte((byte)0xF6);
//				out.writeByte((byte)0x06);
//				out.write(new byte[]{(byte)0xf9,0,0,0,0,0});
//				out.writeByte((byte)(crc/256));
//				out.writeByte((byte)(crc%256));
				out.flush();
				Toast.makeText(activity, "清空网络存储", Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
	}
	static public boolean IRControl(byte[] bytes,Activity activity){
		type = bytes[1];
		outByte = bytes;
			try {
				if(flag){
					if(bytes[3] == 0x03){
						rwPose = learn;
					}else{
						rwPose = control;
					}
					succeed = false;
					int sum = 0xfe+0xf6+controllen;
					for(byte b:bytes){
						sum += b;
						if(b < 0) sum += 256;
					}
					if(is3G || outs == null || outs.length == 0){
						out.write(new byte[]{(byte)0xFe,(byte)0xf6,(byte)controllen,bytes[0],bytes[1],bytes[2],bytes[3],bytes[4],bytes[5],(byte)(sum/256),(byte)(sum%256)});
//						out.writeByte((byte)0xFE);
//						out.writeByte((byte)0xF6);
//						out.writeByte((byte)controllen);
//						out.write(bytes);
//						out.writeByte((byte)(sum/256));
//						out.writeByte((byte)(sum%256));
						out.flush();
					}else
					for(DataOutputStream out:outs){
						out.write(new byte[]{(byte)0xFe,(byte)0xf6,(byte)controllen,bytes[0],bytes[1],bytes[2],bytes[3],bytes[4],bytes[5],(byte)(sum/256),(byte)(sum%256)});
//						out.writeByte((byte)0xFE);
//						out.writeByte((byte)0xF6);
//						out.writeByte((byte)controllen);
//						out.write(bytes);
//						out.writeByte((byte)(sum/256));
//						out.writeByte((byte)(sum%256));
						out.flush();
					}
					MyLog.i("outByte0",bytes[0]+"");
					MyLog.i("outByte1",bytes[1]+"");
					MyLog.i("outByte2",bytes[2]+"");
					MyLog.i("outByte3",bytes[3]+"");
					MyLog.i("outByte4",bytes[4]+"");
					MyLog.i("outByte5",bytes[5]+"");
				}else{
					flag = false;
					VanstWidgetProvider.updateAppWidget(activity);
					synchronizationHandler.sendEmptyMessage(SERVCE_DISCONNECT);
				}
			} catch (Exception e) {	
				e.printStackTrace();
				synchronizationHandler.sendEmptyMessage(SEND_FAILED);
			}
		return false;
	}
	
	static public boolean writeControl(byte[] bytes,Activity activity){
		type = bytes[1];
		outByte = bytes;
			try {
				if(flag){
					MyLog.i("write","write");
					rwPose = control;
					succeed = false;
					int sum = 0xfe+0xf6+controllen;
					for(byte b:bytes){
						sum += b;
						if(b < 0) sum += 256;
					}
					if(is3G || outs == null || outs.length == 0){
						out.write(new byte[]{(byte)0xFe,(byte)0xf6,(byte)controllen,bytes[0],bytes[1],bytes[2],bytes[3],bytes[4],bytes[5],(byte)(sum/256),(byte)(sum%256)});
//						out.writeByte((byte)0xFE);
//						out.writeByte((byte)0xF6);
//						out.writeByte((byte)controllen);
//						out.write(bytes);
//						out.writeByte((byte)(sum/256));
//						out.writeByte((byte)(sum%256));
						out.flush();
					}else
					for(DataOutputStream out:outs){
						out.write(new byte[]{(byte)0xFe,(byte)0xf6,(byte)controllen,bytes[0],bytes[1],bytes[2],bytes[3],bytes[4],bytes[5],(byte)(sum/256),(byte)(sum%256)});
//						out.writeByte((byte)0xFE);
//						out.writeByte((byte)0xF6);
//						out.writeByte((byte)controllen);
//						out.write(bytes);
//						out.writeByte((byte)(sum/256));
//						out.writeByte((byte)(sum%256));
						out.flush();
					}
					for(Device device:MainView.devices){
							if(bytes[1] == device.type && bytes[2] == device.index){
								if(Connect.flag){
									device.isWait = true;
									device.controlTime = System.currentTimeMillis();
								}
							}
						}
					MyLog.i("outByte0",bytes[0]+"");
					MyLog.i("outByte1",bytes[1]+"");
					MyLog.i("outByte2",bytes[2]+"");
					MyLog.i("outByte3",bytes[3]+"");
					MyLog.i("outByte4",bytes[4]+"");
					MyLog.i("outByte5",bytes[5]+"");
				}else{
					flag = false;
					VanstWidgetProvider.updateAppWidget(activity);
					MyLog.i("flag",flag+"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
					synchronizationHandler.sendEmptyMessage(SERVCE_DISCONNECT);
				}
			} catch (Exception e) {	
				e.printStackTrace();
				synchronizationHandler.sendEmptyMessage(SEND_FAILED);
			}
		return false;
	}
	class ReadConfig extends Thread{
		boolean isAlive = true;
		public void run() {
			while (isAlive && flag) {
				try {
					sleep(1000);
					if(Connect.newDate){
						Connect.getPose((byte)0);
						isAlive = false;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	static class GetList extends Thread{
		boolean isAlive = true;
		public void run() {
			try {
				sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(isAlive){
				synchronizationHandler.sendEmptyMessage(SYNCHRONIZATIONG_FAIL_SINGLE);
			}
		}
	}
	
	static class WriteControlThread extends Thread{
		boolean sflag = false;
		public void run() {
			try {
				MyLog.i("qflag",sflag+"");
				Thread.sleep(2000);
				if(controlFlag == 0 && sflag == false){
					controlFlag = 1;
				}
				MyLog.i("sflag",sflag+"");
			} catch (InterruptedException e) {
				
			}
		}
	}
	class ServerThread extends Thread{
		public void run() {
			try {
				ServerSocket serverSocket=new ServerSocket(6666);
				MyLog.i("服务器","启动");
				while (true) {
					Socket socket1 = serverSocket.accept();
					connectNum++;
					if(socket != null) socket.close();
					socket = socket1;
					in = new DataInputStream(socket.getInputStream());
					out = new DataOutputStream(socket.getOutputStream());
					rwPose = control;
					flag = true;
					widgetConnect = false;
					VanstWidgetProvider.updateAppWidget(activity);
					disconnectShow = false;
					handler.sendEmptyMessage(CONNECT_3G);
					readData();
				}
			}catch (Exception e) {
			}
		}
    }
	class Download extends Thread{
		boolean isAlive = true;
		public void run() {
			while (isAlive) {
				try {
					sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(isAlive && rwPose == download && System.currentTimeMillis() - downLoadTime > 20000 ){
					if(isHead) readFile(readName);
					else handler.sendEmptyMessage(READ_DATA_CRC_ERROR);
				}
			}
		}
	}
	
	class ConnectThread extends Thread{
    	public void run(){
    		readData();
    	}
    }
	public void readData(){
		byte c;
		boolean sflag = false;
		while(flag && !sflag){
			try {
//				MyLog.i("read","read");
				if(socket.isConnected()){
    				if((c = in.readByte())==(byte)0xfe && in.readByte()==(byte)0xf6){
    				int len = in.readByte();
    				if(len < 0) len+= 256;
    				if(len >0 && rwPose == learn){
    					byte [] bytes = new byte[len];
    					in.read(bytes);
    					in.readByte();
    					in.readByte();
    					for(byte b:bytes){
    						MyLog.i("b","fwaefaw"+b);
    					}
    					if(bytes[0] == (byte)0xf8){
    						if(bytes[1] == Device.DEVICE_AIR_CONDITIONING){
    							if(KtControlActivity.index == bytes[2]/*&& bytes[3] == 0x03*/){
    								learnHandler.sendEmptyMessage((byte)0xff);
    							}
    						}else if(Device.DEVICE_DIY == bytes[1] && DiyIRActivity.index == bytes[2] /*&& bytes[3] == 0x03*/){
    							int key = bytes[4];
    							if(key < 0) key += 256;
    							diylearnHandler.sendEmptyMessage(key);
    						}else if(ClControlActivity.type == bytes[1] && ClControlActivity.index == bytes[2] /*&& bytes[3] == 0x03*/){
    							int key = bytes[4];
    							if(key < 0) key += 256;
    							learnHandler.sendEmptyMessage(key);
    						}
    					}else if(len == 6 && (bytes[0] == (byte)0xfa||bytes[0]==(byte)0xfc)){
    						for(Device device:MainView.devices){
								if(device != null ){
									if(device.type == bytes[1] && device.type == Device.DEVICE_THERMOMETER && device.index == bytes[2]){
										MyLog.i("fawfawfawfwafwafwafafwafwafwafwa","faewwfawfawfawfwafwafwawfe");
										if(bytes[3]!=-1) device.pose = bytes[3];
										if(bytes[4]!=-1) device.value2 = bytes[4];
										if(bytes[5]!=-1) device.value3 = bytes[5];
									}else if(device.type == bytes[1] && device.type == Device.DEVICE_LIGHT && device.index == bytes[2]){
										if(bytes[3]!=-1) device.pose = bytes[3];
										if(bytes[4]!=-1) device.value2 = bytes[4];
									}else if(device.type == bytes[1] && device.type == Device.DEVICE_RICE_COOKER && device.index == bytes[2]){
										device.pose = bytes[3];
										if(bytes[4]!=-1)device.value2 = bytes[4];
										device.value3 = 1;
									}else if(device.type == inByte[1] && device.type == Device.DEVICE_WASHER && device.index == inByte[2]){
										device.pose = inByte[3];
										if(inByte[4]!=-1) device.value2 = inByte[4];
										if(inByte[4] == 11){
											device.value3 = 1;
										}else if(inByte[4] == 12){
											device.value3 = 0;
										}
									}else if(device.type == bytes[1] && device.index == bytes[2]){
										if(device.type != Device.DEVICE_AIR_CONDITIONING && device.type != Device.DEVICE_CURTAIN &&
												device.type != Device.DEVICE_DIY && device.type != Device.DEVICE_DVD &&
												device.type != Device.DEVICE_JDH && device.type != Device.DEVICE_TV &&
												device.type != Device.DEVICE_3D
										)
										device.pose = bytes[3];
										if(bytes[4]!=-1) device.value2 = bytes[4];
										if(bytes[5]!=-1) device.value3 = bytes[5];
									}	
								}
							}
    					}
    				}else if(len > 0x00 && (rwPose == typedo || rwPose == typeready || rwPose == typedelete)){
    					byte [] bytes = new byte[len];
    					in.read(bytes);
    					in.readByte();
    					in.readByte();
    					for(byte b:bytes){
    						MyLog.i("b",b+"");
    					}
    					if(bytes[1] == (byte)lastIp){
    					if(bytes[0] == (byte)0xf5){
    						if(rwPose == typedelete){
    							if(filename.equals("bayern") && Device.typeBytes.length <=1 ){
    								handler.sendEmptyMessage(SYNCHRONIZATIONG_FINISHED);
    							}else
    								handler.sendEmptyMessage(WRITE_TYPE);
    						}else if(rwPose == typeready && bytes[2] < 0){
    							handler.sendEmptyMessage(WRITE_TYPE_DATA);
    						}else if(rwPose == typedo && bytes[2] < 0){
    							writeTypeSucceed = true;
    						}
    					}
    					}
    				}else if(len > 0x00 && rwPose!= control){   					
    					byte [] bytes = new byte[len];
    					in.read(bytes);
//    					for(int i =0;i<len;i++){
//    						bytes[i] = in.readByte();
//    					}
    					int crc1 = in.readByte();
    					int crc2 = in.readByte();
    					if(crc1 < 0) crc1 += 256;
    					if(crc2 < 0) crc2 += 256;
    					int crc = crc1 *256 + crc2;
//    					for(int i = 0;i<bytes.length;i++){
//							MyLog.i(i+"",bytes[i]+"");
//						}
    					if(bytes[1] == (byte)lastIp){
    					if(bytes[0] == (byte)0xfc){
    						
    						if(rwPose == readConfig){
    							if(bytes[2] != 0x00 && bytes[2] > 0){
    								String file = new String(bytes, 2, len - 3);
    								MyLog.i("file",file);
    								if(file.length()>6){
    									String configString = file.substring(0, 6);
    									MyLog.i("configString",configString);
    									if(configString.equals("config"))
    									fileList.add(file);
    								}
    							}else{
    								rwPose = control;
        							handler.sendEmptyMessage(FILE_LIST_FINISHED);
        						}
    						}
    					}else if(bytes[0] == (byte)0xfd){
    						MyLog.i("bytes[0]", bytes[0]+"");
    						MyLog.i("bytes[1]", bytes[1]+"");
    						MyLog.i("bytes[2]", bytes[2]+"");
    						MyLog.i("bytes[3]", bytes[3]+"");
    						MyLog.i("bytes[4]", bytes[4]+"");
    						MyLog.i("bytes[5]", bytes[5]+"");
    						if(rwPose == loadBitmap && (bytes[2] != 0 || bytes[3] != 0 || bytes[4] != 0 || bytes[5] != 0)){
    							
    							int len5 = bytes[5] < 0? bytes[5]+256:bytes[5];
    							int len4 = bytes[4] < 0? bytes[4]+256:bytes[4];
    							int len3 = bytes[3] < 0? bytes[3]+256:bytes[3];
    							int len2 = bytes[2] < 0? bytes[2]+256:bytes[2];
    							progressLength = len5 + len4 * 256 + len3 * 256 * 256 + len2 * 256 * 256 *256;
    							isHead = true;
    							readFile(readName);
    							CRCERROR =0;
    							downLoad = new Download();
    							downLoad.start();
    						}else if(rwPose == loadBitmap){
    							progressHandler.sendEmptyMessage(100);
    							handler.sendEmptyMessage(LOAD_NO);
    							if(!readName.equals("bayern")){
	    							if(upRoom < downRooms.length-1){
										try {
											Thread.sleep(500);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										upRoom++;
										handler.sendEmptyMessage(LOAD_REFRESH_PROGRESS);
										handler.sendEmptyMessage(LOAD_NEXT);
									}else{
										handler.sendEmptyMessage(PROGRESS_DISMISS);
										handler.sendEmptyMessage(REFRESH_ROOMS);
									}
    							}
    						}
    					}else if(bytes[0] == (byte)0xfb){
    						MyLog.i("0",bytes[0]+"");
    						MyLog.i("1",bytes[1]+"");
    						MyLog.i("2",bytes[2]+"");
    						if(bytes[2] == (byte)0xaa){
    							handler.sendEmptyMessage(PASSWORD_SUCCED);
    						}else{
    							handler.sendEmptyMessage(PASSWORD_FAIL);
    						}
    					}else if(bytes[0] == (byte)0xf5){
    						if(rwPose == delete){
    							handler.sendEmptyMessage(UPLOAD_READY_SINGLE);
    						}else if(bytes[2] < 0){
	    						if(rwPose == upLoad){
	    							handler.sendEmptyMessage(UPLOAD_DOING_SINGLE);
	    						}else if(rwPose == upIng){
	    							handler.sendEmptyMessage(SYNCHRONIZATION_CL_SINGLE);
	    						}
    						}
    					}else if(bytes[0] == (byte)0xf8){
    						if(dateBytes != null){
//    							MyLog.i("crc1",crc1+"");
//    							MyLog.i("crc2",crc2+"");
//    							MyLog.i("crc",crc+"" );
	    						downLoadTime = System.currentTimeMillis();
	    						if(bytes[2] != 0x04){
	    							int dataCrc =  0;
	    							for(int i = 4;i<len;i++){
	    								dataCrc += bytes[i];
	    								if(bytes[i] < 0) dataCrc += 256;
		    						}
	    							dataCrc += 0xfe + 0xf6 + len + len - 4 + 0xf8 + lastIp + bytes[2];
	    							if(bytes[2] < 0) dataCrc += 256;
	    							Log.i("dataCrc", dataCrc+"");
	    							Log.i("crc", crc+"");
	    							if(crc == dataCrc){
			    						for(int i = 4;i<len;i++){
			    							if(dateBytesP < dateBytes.length){
				    							dateBytes[dateBytesP] = bytes[i];
				    							dateBytesP++;
			    							}
		//	    							indates.add(bytes[i]);
			    						}
			    						if(bytes[2] == (byte)0x85){
			    							isHead = false;
					    					handler.sendEmptyMessage(READ_DATE_FINISHED);
					    					rwPose = control;
					    					MyLog.i("CRCERROR",CRCERROR+"");
					    					if(downLoad !=null) downLoad.isAlive = false;
				    					}else{
				    						progress += (len - 4);
//				    						MyLog.i("progress", progress+"");
				    						isHead = false;
				    						try {
				    							int crc11 = 0xfe + 0xf6 + 0x03 + 0xf5 + lastIp + 0x81;
				    							out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)0x03,(byte)0xf5,(byte)lastIp,(byte)0x81,(byte)(crc11/256),(byte)(crc11%256)});
//				    							out.writeByte((byte)(crc/256));
//				    							out.writeByte((byte)(crc%256));
				    							out.flush();
				    							timewrite = System.currentTimeMillis();
				    							Log.i("time", timewrite - timeread +"");
				    						} catch (IOException e) {
				    							e.printStackTrace();
				    						}
				    						handler.sendEmptyMessage(READ_DATE_ENTER);
				    					}
	    							}else{
	    								CRCERROR++;
	    								if(isHead){
	    									readFile(readName);
	    								}else{
	    									handler.sendEmptyMessage(READ_DATA_CRC_ERROR);
	    								}
	    							}
	    						}
    						}else{
    							handler.sendEmptyMessage(ANOTHER_DONWLOAD);
    							rwPose = control;;
    						}
    					}
    					}
    				}else if(len == 0x06 && rwPose == control){
						in.read(inByte);
						for(int i = 0;i<6;i++){
							MyLog.i("intByte"+i,inByte[i]+"");
						}
						in.readByte();
						in.readByte();
						inFlag = false;
						if(inByte[0] == (byte)0xfa ||inByte[0]==(byte)0xfc){
							MainView.waitForControl = false;
							if(VanstService.ConnectFlag && VanstService.out!=null){
								try {
									VanstService.out.write(inByte);
									VanstService.out.flush();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							handler.sendEmptyMessage(1);
							if(outByte[0] == (byte)0xf1){
								if(inByte[1] == outByte[1] && inByte[2] == outByte[2]){
									controlFlag = 1;
									MyLog.i("getpose","getpose");
								}
							}else{	
								if(type == Device.DEVICE_AIR_CONDITIONING){
									if(outByte[3] == 0x00 || outByte[3] == 0x01){
										if(inByte[1] == outByte[1] && inByte[2] == outByte[2] && outByte[3] == inByte[3]){
											succeed = true;
											controlFlag = 1;
										}
									}
								}else if (type == Device.DEVICE_THERMOMETER){
									if(inByte[1] == outByte[1] && inByte[2] == outByte[2]){
										succeed = true;
										controlFlag = 1;
									}
								}else if(type == Device.DEVICE_RICE_COOKER){
									if(inByte[1] ==outByte[1] && inByte[2] == outByte[2] && inByte[4] == outByte[4]){
										succeed = true;
										controlFlag = 1;
									}
								}else{
									if(inByte[1] == outByte[1] && inByte[2] == outByte[2] && inByte[3] == outByte[3]){
										succeed = true;
										controlFlag = 1;
									}
								}
							}
							if(inByte[1] == b1 && inByte[2] == b2&& (inByte[3]=='B'&&inByte[4]=='B'&&inByte[5]=='B' || inByte[3]=='S'&&inByte[4]=='S'&&inByte[5]=='S' || inByte[3]=='Q'&&inByte[4]=='Q'&&inByte[5]=='Q')){
								if(inByte[3]=='B' ||inByte[3]=='S'){
									handler.sendEmptyMessage(BING_SUCCEED);
								}else{
									handler.sendEmptyMessage(UNBIND_SUCCEED);
								}
							}else if(MainView.devices != null && MainView.devices.size() > 0){
								MyLog.i("length",MainView.devices.size()+"");
								for(Device device:MainView.devices){
									if(device != null ){
										device.isNew = true;
										refreshPose = true;
										if(device.type == inByte[1] && device.index == inByte[2]&&device.bind && (inByte[3]=='B'&&inByte[4]=='B'&&inByte[5]=='B' || inByte[3]=='S'&&inByte[4]=='S'&&inByte[5]=='S' || inByte[3]=='Q'&&inByte[4]=='Q'&&inByte[5]=='Q')){
											device.bind = false;
											if(inByte[3]=='B' ||inByte[3]=='S'){
												handler.sendEmptyMessage(BING_SUCCEED);
											}else if(inByte[3] =='Q') {
												handler.sendEmptyMessage(UNBIND_SUCCEED);
											}
										}else if(device.type == inByte[1] && device.type == Device.DEVICE_THERMOMETER && device.index == inByte[2]){
											MyLog.i("fawfawfawfwafwafwafafwafwafwafwa","faewwfawfawfawfwafwafwawfe");
											if(inByte[3]!=-1) device.pose = inByte[3];
											if(inByte[4]!=-1) device.value2 = inByte[4];
											if(inByte[5]!=-1) device.value3 = inByte[5];
										}else if(device.type == inByte[1] && device.type == Device.DEVICE_LIGHT && device.index == inByte[2]){
											if(inByte[3]!=-1) device.pose = inByte[3];
											if(inByte[4]!=-1) device.value2 = inByte[4];
										}else if(device.type == inByte[1] && device.type == Device.DEVICE_RICE_COOKER && device.index == inByte[2]){
											device.pose = inByte[3];
											if(inByte[4]!=-1)device.value2 = inByte[4];
											device.value3 = 1;
										}else if(device.type == inByte[1] && device.type == Device.DEVICE_WASHER && device.index == inByte[2]){
											device.pose = inByte[3];
											if(inByte[4]!=-1) device.value2 = inByte[4];
											if(inByte[4] == 11){
												device.value3 = 1;
											}else if(inByte[4] == 12){
												device.value3 = 0;
											}
										}else if(device.type == inByte[1] && device.index == inByte[2]){
											if(device.type != Device.DEVICE_AIR_CONDITIONING && device.type != Device.DEVICE_CURTAIN &&
													device.type != Device.DEVICE_DIY && device.type != Device.DEVICE_DVD &&
													device.type != Device.DEVICE_JDH && device.type != Device.DEVICE_TV &&
													device.type != Device.DEVICE_3D
											)
											device.pose = inByte[3];
											if(inByte[4]!=-1) device.value2 = inByte[4];
											if(inByte[5]!=-1) device.value3 = inByte[5];
										}	
									}
								}
								boolean you = false;
								for(Device device:MainView.devices){
									if(device != null && device.type == inByte[1] && device.index == inByte[2] && device.visible){
										if(device.pose != -1&& device.isWait) {
											handler.sendEmptyMessage(RE_SOUND);
										}
										if(device.pose != -1) device.isWait = false;
										you = true;
									}
								}
								if(!you && inByte[1]>0&&inByte[0]==(byte)0xfa && inByte[1]<=Device.DEVICE_TYPE_COUNT  && inByte[2] > 0 && inByte[2] < 100 && mainActivity!= null && mainActivity.mainView!=null){
									newDevice = new Device(mainActivity.mainView, inByte[1], inByte[2]);
									newDevice.pose = inByte[3];
									newDevice.value2 = inByte[4];
									newDevice.value3 = inByte[5];
									handler.sendEmptyMessage(NEW_DEVICE_SINGLE);
								}
								MyLog.i("you",you+"");
							}else{
								if(inByte[1]>0 &&inByte[0]==(byte)0xfa&& inByte[1]<=Device.DEVICE_TYPE_COUNT && inByte[2] > 0 && inByte[2] < 100 && mainActivity!= null && mainActivity.mainView!=null){
									newDevice = new Device(mainActivity.mainView, inByte[1], inByte[2]);
									newDevice.pose = inByte[3];
									newDevice.value2 = inByte[4];
									newDevice.value3 = inByte[5];
									handler.sendEmptyMessage(NEW_DEVICE_SINGLE);
								}
							}
						}
    				}
    				}else{
    					char b = (char)c;
    					MyLog.i("c",b+" "+(byte)b);
    				}
    				newDate = true;
				}else{
					MyLog.i("error","error");
					break;
				}
			} catch (IOException e){
				connectNum--;
				sflag = true;
				newDate = false;
				handler.sendEmptyMessage(DISCONNECT_SINGLE);
			}
		}
	}
	
	public void readData2(DataInputStream in,DataOutputStream out,Socket socket){
		byte c;
		boolean sflag = false;
		socketindex++;
		if(socketindex < sockets.size()){
			initCommunicate();
		}
		while(flag && !sflag){
			try {
//				MyLog.i("read","read");
				if(socket.isConnected()){
    				if((c = in.readByte())==(byte)0xfe && in.readByte()==(byte)0xf6){
    					
    				timeread = System.currentTimeMillis();
    				int len = in.readByte();
    				if(len < 0) len+= 256;
    				if(len >0 && rwPose == learn){
    					byte [] bytes = new byte[len];
    					in.read(bytes);
    					in.readByte();
    					in.readByte();
    					for(byte b:bytes){
    						MyLog.i("b","fwaefaw"+b);
    					}
    					if(bytes[0] == (byte)0xf8){
    						if(bytes[1] == Device.DEVICE_AIR_CONDITIONING){
    							if(KtControlActivity.index == bytes[2] /*&& bytes[3] == 0x03*/){
    								learnHandler.sendEmptyMessage((byte)0xff);
    							}
    						}else if(Device.DEVICE_DIY == bytes[1] && DiyIRActivity.index == bytes[2] /*&& bytes[3] == 0x03*/){
    							int key = bytes[4];
    							if(key < 0) key += 256;
    							diylearnHandler.sendEmptyMessage(key);
    						}else if(ClControlActivity.type == bytes[1] && ClControlActivity.index == bytes[2] /*&& bytes[3] == 0x03*/){
    							int key = bytes[4];
    							if(key < 0) key += 256;
    							learnHandler.sendEmptyMessage(key);
    						}
    					}else if(len == 6 && (bytes[0] == (byte)0xfa ||bytes[0]==(byte)0xfc)){
    						for(Device device:MainView.devices){
								if(device != null ){
									if(device.type == bytes[1] && device.type == Device.DEVICE_THERMOMETER && device.index == bytes[2]){
										MyLog.i("fawfawfawfwafwafwafafwafwafwafwa","faewwfawfawfawfwafwafwawfe");
										if(bytes[3]!=-1) device.pose = bytes[3];
										if(bytes[4]!=-1) device.value2 = bytes[4];
										if(bytes[5]!=-1) device.value3 = bytes[5];
									}else if(device.type == bytes[1] && device.type == Device.DEVICE_LIGHT && device.index == bytes[2]){
										if(bytes[3]!=-1) device.pose = bytes[3];
										if(bytes[4]!=-1) device.value2 = bytes[4];
									}else if(device.type == bytes[1] && device.type == Device.DEVICE_RICE_COOKER && device.index == bytes[2]){
										device.pose = bytes[3];
										if(bytes[4]!=-1)device.value2 = bytes[4];
										device.value3 = 1;
									}else if(device.type == inByte[1] && device.type == Device.DEVICE_WASHER && device.index == inByte[2]){
										device.pose = inByte[3];
										if(inByte[4]!=-1) device.value2 = inByte[4];
										if(inByte[4] == 11){
											device.value3 = 1;
										}else if(inByte[4] == 12){
											device.value3 = 0;
										}
									}else if(device.type == bytes[1] && device.index == bytes[2]){
										if(device.type != Device.DEVICE_AIR_CONDITIONING && device.type != Device.DEVICE_CURTAIN &&
												device.type != Device.DEVICE_DIY && device.type != Device.DEVICE_DVD &&
												device.type != Device.DEVICE_JDH && device.type != Device.DEVICE_TV &&
												device.type != Device.DEVICE_3D
										)
										device.pose = bytes[3];
										if(bytes[4]!=-1) device.value2 = bytes[4];
										if(bytes[5]!=-1) device.value3 = bytes[5];
									}	
								}
							}
    					}
    				}else if(len > 0x00 && (rwPose == typedo || rwPose == typeready || rwPose == typedelete)){
    					byte [] bytes = new byte[len];
    					in.read(bytes);
    					in.readByte();
    					in.readByte();
    					for(byte b:bytes){
    						MyLog.i("b",b+"");
    					}
    					if(bytes[1] == (byte)lastIp){
    					if(bytes[0] == (byte)0xf5){
    						if(rwPose == typedelete){
    							if(filename.equals("bayern") && Device.typeBytes.length <=1 ){
    								handler.sendEmptyMessage(SYNCHRONIZATIONG_FINISHED);
    							}else
    								handler.sendEmptyMessage(WRITE_TYPE);
    						}else if(rwPose == typeready && bytes[2] < 0){
    							handler.sendEmptyMessage(WRITE_TYPE_DATA);
    						}else if(rwPose == typedo && bytes[2] < 0){
    							writeTypeSucceed = true;
    						}
    					}
    					}
    				}else if(len > 0x00 && rwPose!= control){   					
    					byte [] bytes = new byte[len];
    					in.read(bytes);
//    					for(int i =0;i<len;i++){
//    						bytes[i] = in.readByte();
//    					}
    					int crc1 = in.readByte();
    					int crc2 = in.readByte();
    					if(crc1 < 0) crc1 += 256;
    					if(crc2 < 0) crc2 += 256;
    					int crc = crc1 *256 + crc2;
//    					for(int i = 0;i<bytes.length;i++){
//							MyLog.i(i+"",bytes[i]+"");
//						}
    					if(bytes[1] == (byte)lastIp){
    					if(bytes[0] == (byte)0xfc){
    						
    						if(rwPose == readConfig){
    							if(bytes[2] != 0x00 && bytes[2] > 0){
    								String file = new String(bytes, 2, len - 3);
    								MyLog.i("file",file);
    								if(file.length()>6){
    									String configString = file.substring(0, 6);
    									MyLog.i("configString",configString);
    									if(configString.equals("config"))
    									fileList.add(file);
    								}
    							}else{
    								rwPose = control;
        							handler.sendEmptyMessage(FILE_LIST_FINISHED);
        						}
    						}
    					}else if(bytes[0] == (byte)0xfd){
    						MyLog.i("bytes[0]", bytes[0]+"");
    						MyLog.i("bytes[1]", bytes[1]+"");
    						MyLog.i("bytes[2]", bytes[2]+"");
    						MyLog.i("bytes[3]", bytes[3]+"");
    						MyLog.i("bytes[4]", bytes[4]+"");
    						MyLog.i("bytes[5]", bytes[5]+"");
    						if(rwPose == loadBitmap && (bytes[2] != 0 || bytes[3] != 0 || bytes[4] != 0 || bytes[5] != 0)){
    							
    							int len5 = bytes[5] < 0? bytes[5]+256:bytes[5];
    							int len4 = bytes[4] < 0? bytes[4]+256:bytes[4];
    							int len3 = bytes[3] < 0? bytes[3]+256:bytes[3];
    							int len2 = bytes[2] < 0? bytes[2]+256:bytes[2];
    							progressLength = len5 + len4 * 256 + len3 * 256 * 256 + len2 * 256 * 256 *256;
    							isHead = true;
    							readFile(readName);
    							CRCERROR =0;
    							downLoad = new Download();
    							downLoad.start();
    						}else if(rwPose == loadBitmap){
    							progressHandler.sendEmptyMessage(100);
    							handler.sendEmptyMessage(LOAD_NO);
    							if(!readName.equals("bayern")){
	    							if(upRoom < downRooms.length-1){
										try {
											Thread.sleep(500);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										upRoom++;
										handler.sendEmptyMessage(LOAD_REFRESH_PROGRESS);
										handler.sendEmptyMessage(LOAD_NEXT);
									}else{
										handler.sendEmptyMessage(PROGRESS_DISMISS);
										handler.sendEmptyMessage(REFRESH_ROOMS);
									}
    							}
    						}
    					}else if(bytes[0] == (byte)0xfb){
    						MyLog.i("0",bytes[0]+"");
    						MyLog.i("1",bytes[1]+"");
    						MyLog.i("2",bytes[2]+"");
    						if(bytes[2] == (byte)0xaa){
    							handler.sendEmptyMessage(PASSWORD_SUCCED);
    						}else{
    							handler.sendEmptyMessage(PASSWORD_FAIL);
    						}
    					}else if(bytes[0] == (byte)0xf5){
    						if(rwPose == delete){
    							handler.sendEmptyMessage(UPLOAD_READY_SINGLE);
    						}else if(bytes[2] < 0){
	    						if(rwPose == upLoad){
	    							handler.sendEmptyMessage(UPLOAD_DOING_SINGLE);
	    						}else if(rwPose == upIng){
	    							handler.sendEmptyMessage(SYNCHRONIZATION_CL_SINGLE);
	    						}
    						}
    					}else if(bytes[0] == (byte)0xf8){
    						if(dateBytes != null){
//    							MyLog.i("crc1",crc1+"");
//    							MyLog.i("crc2",crc2+"");
//    							MyLog.i("crc",crc+"" );
	    						downLoadTime = System.currentTimeMillis();
	    						if(bytes[2] != 0x04){
	    							int dataCrc =  0;
	    							for(int i = 4;i<len;i++){
	    								dataCrc += bytes[i];
	    								if(bytes[i] < 0) dataCrc += 256;
		    						}
	    							dataCrc += 0xfe + 0xf6 + len + len - 4 + 0xf8 + lastIp + bytes[2];
	    							if(bytes[2] < 0) dataCrc += 256;
	    							Log.i("dataCrc", dataCrc+"");
	    							Log.i("crc",crc+"");
	    							if(crc == dataCrc){
			    						for(int i = 4;i<len;i++){
			    							if(dateBytesP < dateBytes.length){
				    							dateBytes[dateBytesP] = bytes[i];
				    							dateBytesP++;
			    							}
		//	    							indates.add(bytes[i]);
			    						}
			    						if(bytes[2] == (byte)0x85){
			    							isHead = false;
					    					handler.sendEmptyMessage(READ_DATE_FINISHED);
					    					rwPose = control;
					    					MyLog.i("CRCERROR",CRCERROR+"");
					    					if(downLoad !=null) downLoad.isAlive = false;
				    					}else{
				    						progress += (len - 4);
//				    						MyLog.i("progress", progress+"");
				    						isHead = false;
				    						try {
				    							int crc11 = 0xfe + 0xf6 + 0x03 + 0xf5 + lastIp + 0x81;
				    							out.write(new byte[]{(byte)0xfe,(byte)0xf6,(byte)0x03,(byte)0xf5,(byte)lastIp,(byte)0x81,(byte)(crc11/256),(byte)(crc11%256)});
//				    							out.writeByte((byte)(crc/256));
//				    							out.writeByte((byte)(crc%256));
				    							out.flush();
				    							timewrite = System.currentTimeMillis();
				    							Log.i("time", timewrite - timeread +"");
				    						} catch (IOException e) {
				    							e.printStackTrace();
				    						}
				    						handler.sendEmptyMessage(READ_DATE_ENTER);
				    					}
	    							}else{
	    								CRCERROR++;
	    								if(isHead){
	    									readFile(readName);
	    								}else{
	    									handler.sendEmptyMessage(READ_DATA_CRC_ERROR);
	    								}
	    							}
	    						}
    						}else{
    							handler.sendEmptyMessage(ANOTHER_DONWLOAD);
    							rwPose = control;;
    						}
    					}
    					}
    				}else if(len == 0x06 && rwPose == control){
						in.read(inByte);
						for(int i = 0;i<6;i++){
							MyLog.i("intByte"+i,inByte[i]+"");
						}
						in.readByte();
						in.readByte();
						inFlag = false;
						if(inByte[0] == (byte)0xfa||inByte[0]==(byte)0xfc){
							MainView.waitForControl = false;
							if(VanstService.ConnectFlag && VanstService.out!=null){
								try {
									VanstService.out.write(inByte);
									VanstService.out.flush();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							handler.sendEmptyMessage(1);
							if(outByte[0] == (byte)0xf1){
								if(inByte[1] == outByte[1] && inByte[2] == outByte[2]){
									controlFlag = 1;
									MyLog.i("getpose","getpose");
								}
							}else{	
								if(type == Device.DEVICE_AIR_CONDITIONING){
									if(outByte[3] == 0x00 || outByte[3] == 0x01){
										if(inByte[1] == outByte[1] && inByte[2] == outByte[2] && outByte[3] == inByte[3]){
											succeed = true;
											controlFlag = 1;
										}
									}
								}else if (type == Device.DEVICE_THERMOMETER){
									if(inByte[1] == outByte[1] && inByte[2] == outByte[2]){
										succeed = true;
										controlFlag = 1;
									}
								}else if(type == Device.DEVICE_RICE_COOKER){
									if(inByte[1] ==outByte[1] && inByte[2] == outByte[2] && inByte[4] == outByte[4]){
										succeed = true;
										controlFlag = 1;
									}
								}else{
									if(inByte[1] == outByte[1] && inByte[2] == outByte[2] && inByte[3] == outByte[3]){
										succeed = true;
										controlFlag = 1;
									}
								}
							}
							if(inByte[1] == b1 && inByte[2] == b2&& (inByte[3]=='B'&&inByte[4]=='B'&&inByte[5]=='B' || inByte[3]=='S'&&inByte[4]=='S'&&inByte[5]=='S' || inByte[3]=='Q'&&inByte[4]=='Q'&&inByte[5]=='Q')){
								if(inByte[3]=='B' ||inByte[3]=='S'){
									handler.sendEmptyMessage(BING_SUCCEED);
								}else{
									handler.sendEmptyMessage(UNBIND_SUCCEED);
								}
							}else if(MainView.devices != null && MainView.devices.size() > 0){
								MyLog.i("length",MainView.devices.size()+"");
								for(Device device:MainView.devices){
									if(device != null ){
										device.isNew = true;
										refreshPose = true;
										if(device.type == inByte[1] && device.index == inByte[2]&&device.bind&& (inByte[3]=='B'&&inByte[4]=='B'&&inByte[5]=='B' || inByte[3]=='S'&&inByte[4]=='S'&&inByte[5]=='S' || inByte[3]=='Q'&&inByte[4]=='Q'&&inByte[5]=='Q')){
											device.bind = false;
											if(inByte[3]=='B' ||inByte[3]=='S'){
												handler.sendEmptyMessage(BING_SUCCEED);
											}else{
												handler.sendEmptyMessage(UNBIND_SUCCEED);
											}
										}else if(device.type == inByte[1] && device.type == Device.DEVICE_THERMOMETER && device.index == inByte[2]){
											MyLog.i("fawfawfawfwafwafwafafwafwafwafwa","faewwfawfawfawfwafwafwawfe");
											if(inByte[3]!=-1) device.pose = inByte[3];
											if(inByte[4]!=-1) device.value2 = inByte[4];
											if(inByte[5]!=-1) device.value3 = inByte[5];
										}else if(device.type == inByte[1] && device.type == Device.DEVICE_LIGHT && device.index == inByte[2]){
											if(inByte[3]!=-1) device.pose = inByte[3];
											if(inByte[4]!=-1) device.value2 = inByte[4];
										}else if(device.type == inByte[1] && device.type == Device.DEVICE_RICE_COOKER && device.index == inByte[2]){
											device.pose = inByte[3];
											if(inByte[4]!=-1)device.value2 = inByte[4];
											device.value3 = 1;
										}else if(device.type == inByte[1] && device.type == Device.DEVICE_WASHER && device.index == inByte[2]){
											device.pose = inByte[3];
											if(inByte[4]!=-1) device.value2 = inByte[4];
											if(inByte[4] == 11){
												device.value3 = 1;
											}else if(inByte[4] == 12){
												device.value3 = 0;
											}
										}else if(device.type == inByte[1] && device.index == inByte[2]){
											if(device.type != Device.DEVICE_AIR_CONDITIONING && device.type != Device.DEVICE_CURTAIN &&
													device.type != Device.DEVICE_DIY && device.type != Device.DEVICE_DVD &&
													device.type != Device.DEVICE_JDH && device.type != Device.DEVICE_TV &&
													device.type != Device.DEVICE_3D
											)
											device.pose = inByte[3];
											if(inByte[4]!=-1) device.value2 = inByte[4];
											if(inByte[5]!=-1) device.value3 = inByte[5];
										}	
									}
								}
								boolean you = false;
								for(Device device:MainView.devices){
									if(device != null && device.type == inByte[1] && device.index == inByte[2] && device.visible){
										if(device.pose != -1&& device.isWait) {
											handler.sendEmptyMessage(RE_SOUND);
										}
										if(device.pose != -1) device.isWait = false;
										you = true;
									}
								}
								if(!you && inByte[1]>0 &&inByte[0]==(byte)0xfa&& inByte[1]<=Device.DEVICE_TYPE_COUNT  && inByte[2] > 0 && inByte[2] < 100 && mainActivity!= null && mainActivity.mainView!=null){
									newDevice = new Device(mainActivity.mainView, inByte[1], inByte[2]);
									newDevice.pose = inByte[3];
									newDevice.value2 = inByte[4];
									newDevice.value3 = inByte[5];
									handler.sendEmptyMessage(NEW_DEVICE_SINGLE);
								}
								MyLog.i("you",you+"");
							}else{
								if(inByte[1]>0&&inByte[0]==(byte)0xfa && inByte[1]<=Device.DEVICE_TYPE_COUNT && inByte[2] > 0 && inByte[2] < 100 && mainActivity!= null && mainActivity.mainView!=null){
									newDevice = new Device(mainActivity.mainView, inByte[1], inByte[2]);
									newDevice.pose = inByte[3];
									newDevice.value2 = inByte[4];
									newDevice.value3 = inByte[5];
									handler.sendEmptyMessage(NEW_DEVICE_SINGLE);
								}
							}
						}
    				}
    				}else{
    					char b = (char)c;
    					MyLog.i("c",b+" "+(byte)b);
    				}
    				newDate = true;
				}else{
					MyLog.i("error","error");
					break;
				}
			} catch (IOException e){
				connectNum--;
				sflag = true;
				newDate = false;
				disSocket = socket;
				handler.sendEmptyMessage(DISCONNECT_SINGLE);
			}
		}
	}
	
	
	public void initCommunicate(){
		new Thread(){
			public void run() {
				connectNum++;
				readData2(ins[socketindex],outs[socketindex],sockets.get(socketindex).socket);
			};
		}.start();
	}
	
	static public int localIp(){
		String serviceIP = getIPAddress(activity);
		if(serviceIP == null) return 256;
		int size = serviceIP.lastIndexOf('.');
		String ip = serviceIP.substring(size+1, serviceIP.length());
		int iIp = 256;
		try {
			iIp = Integer.parseInt(ip);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return iIp;
	}
	
}
