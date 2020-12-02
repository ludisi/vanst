package com.vanst.client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.style.BulletSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	final String version = "2.3";
	final String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/vanstbg.jpg";
	int currentVolume;
	static boolean pause;
	final static int PhotoAc = 0xab;
	final static int CAMERA = 0xac;
	static String selectConfigPath;
	static List<Map<String, Object>> deviceList;
	static MainView mainView;
	static MusicDo musicDo;
	boolean isSetting;
	static boolean exit = false;
	static boolean widgetbusy = false;
	static boolean widgetroom = false;
	LinearLayout linearLayout;
	TextView infoText;
	ImageButton imageButton1;
	ImageButton imageButton2;
	ImageButton imageButton3;
	ImageButton controlButton;
	ImageButton settingButton;
	ImageButton netButton;
	ImageButton infoButton;
	ListView listView;
	HorizontalListView deviceListView;
	FrameLayout frameLayout;
	static boolean buttonFlag = true;
	static boolean isOK; 
	static int flag = 1;
	static Map<String, Object> deviceMap;
	SimpleAdapter controlAdapter;
	SimpleAdapter deviceAdapter;
	SimpleAdapter modeAdapter;
	static String [] deviceNames = new String[]{"照明灯",
		"猫眼",
		"节能灯",
		"空调",
		"音响",
		"窗帘",
		"门锁",
		"电视",
		"洗衣机",
		"插座",
		"电炉",
		"台灯",
		"气体报警",
		"防盗报警",
		"门禁",
		"求助钮",
		"电脑",
		"电视监控",
		"温度计",
		"电饭煲",
		"微波炉",
		"冰箱",
		"风扇",
		"空气净化",
		"热水壶",
		"DVD",
		"卫星",
		"机顶盒",
		"3D控制",
		"自定义",
		"热水器",
		"IR精灵",
		"立式空调"};
	List<Map<String, Object>> connectList = new ArrayList<Map<String,Object>>();
	List<Map<String, Object>> namesList = new ArrayList<Map<String,Object>>();
	List<Map<String, Object>> modeList = new ArrayList<Map<String,Object>>();
	static ProgressDialog progressDialog;
	static String[] connectItems= {"设置WIFI连接","设置3G连接","精灵绑定模式","局域网络配置","修改配置密码","上传配置","下载配置","更新配置","3G认证号码设置","声音设置"};
//	final int[] settingImages = new int[]{android.R.drawable.ic_menu_search,
//			  android.R.drawable.ic_menu_call,
//			  android.R.drawable.ic_menu_manage,
//			  android.R.drawable.ic_menu_more,
//			  android.R.drawable.ic_menu_edit,
//			  android.R.drawable.ic_menu_send,
//			  android.R.drawable.ic_menu_save,
//			  android.R.drawable.ic_menu_delete,
//				};
	final int[] settingImages = new int[]{
			  R.drawable.bt_wifi,
			  R.drawable.bt_3g,
			  R.drawable.bt_testnet,
			  R.drawable.bt_apchange,
			  R.drawable.bt_pwd,
			  R.drawable.bt_upload,
			  R.drawable.bt_download,
			  R.drawable.bt_update,
			  R.drawable.bt_check,
			  R.drawable.bt_sound
				};
    static ArrayList<String> netPre = new ArrayList<String>();
	static ArrayList<String> pre = new ArrayList<String>();
	static List<Map<String,Object>> deviceTypes = new ArrayList<Map<String,Object>>();
	static List<Map<String,Object>> deviceNums = new ArrayList<Map<String,Object>>();
	static final int PROGRESS_CANCEL = 0x01;
	static final int UPDATA_WIDGET = 0x02;
	static Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == PROGRESS_CANCEL){
				if(progressDialog!=null) progressDialog.dismiss();
				VanstWidgetProvider.updateAppWidget(Connect.activity);
//				Toast.makeText(Connect.activity, MainView.bitmap.getWidth()+"", Toast.LENGTH_SHORT).show();
//				Toast.makeText(Connect.activity, MainView.bgBitmap.getWidth()+"", Toast.LENGTH_SHORT).show();
//				MainView.bgratio = MainView.bitmap.getWidth() / MainView.bgBitmap.getWidth();
//				Toast.makeText(Connect.activity, MainView.bgratio+"", Toast.LENGTH_SHORT).show();
			}else if(msg.what == UPDATA_WIDGET){
				VanstWidgetProvider.updateAppWidget(Connect.activity);
			}
		};
	};
	class Get3GIP extends Thread{
		public void run() {
			while(true){
				if(Connect._3GIp!=null)MyLog.i("3GIP",Connect._3GIp);
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Connect.deleteSms();
			}
		}
	}
	class ReadConfig extends Thread{
		boolean isAlive = true;
		public void run() {
			while (isAlive) {
				try {
					sleep(1000);
					if(Connect.newDate){
//						Connect.synchronizationHandler.sendEmptyMessage(Connect.READ_CONFIG2_SINGLE);
						Connect.getPose((byte)0);
						isAlive = false;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	class HeartThread extends Thread{
		public void run(){
			while(true){
				try{
					sleep(15000);
					if(Connect.flag && Connect.out!=null && (Connect.rwPose == Connect.control||Connect.rwPose == Connect.learn)){
						if(Connect.is3G || Connect.outs == null || Connect.outs.length == 0){
							try {
								Connect.out.writeByte((byte)0xaa);
								Connect.out.flush();
							} catch (Exception e) {
							}
						}else
						for(DataOutputStream out:Connect.outs){
							try {
								out.writeByte((byte)0xaa);
								out.flush();
							} catch (Exception e) {
							}
						}
					}
				}catch (Exception e) {
				}
			}
		}
	}
	List<ApplicationInfo> packages;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Connect.activity = this;
        Connect.mainActivity = this;
        Connect.isEnter = true;
        Connect.lastIp = Connect.localIp();
        handler.sendEmptyMessage(UPDATA_WIDGET);
        if(exit) finish();
        else MainActivity.musicDo = new MusicDo(this);
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
        	Toast.makeText(this, "SD卡未插入！", Toast.LENGTH_LONG).show();
//	        File file = getFilesDir();
//	        Config.FILE_ROOT = file.getAbsolutePath()+"/";
        }
        MainView.devices = new ArrayList<Device>();
        SharedPreferences pref=getSharedPreferences("config", MODE_PRIVATE);
    	Room.count = pref.getInt("rooms", 2);
        Connect.useIp = pref.getString("useip", "10.10.100.254");
        Connect._3GIp = pref.getString("lanip", null);
        Connect.phoneNumber = pref.getString("phone", null);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.main);
        imageButton1 = (ImageButton)findViewById(R.id.imageButton1);
        imageButton2 = (ImageButton)findViewById(R.id.imageButton2);
        imageButton3 = (ImageButton)findViewById(R.id.imageButton3);
        controlButton = (ImageButton)findViewById(R.id.control);
        settingButton = (ImageButton)findViewById(R.id.setting);
        netButton = (ImageButton)findViewById(R.id.net);
        infoButton = (ImageButton)findViewById(R.id.info);
//        controlButton.setTextColor(Color.RED);
		controlButton.setBackgroundResource(R.drawable.touming);
		controlButton.setImageResource(R.drawable.bt_1_1);
        linearLayout = (LinearLayout)findViewById(R.id.buttons);
        mainView = (MainView)findViewById(R.id.mainView);
        listView = (ListView)findViewById(R.id.listView);
        infoText = (TextView)findViewById(R.id.infoView);
        infoText.setText("             用户使用说明：\n\n1：本应用必须配置我公司的WIFI模块或3G模块以及前端控制模块一起使用，如果没有便无法正常使用。但是可选择直接进入进行离线预览或配置。\n" +
        		"2：配置密码默认为vanst，此密码可修改，如果忘记密码请卸载应用重新安装。\n" +
        		"3：同步密码为无线路由器登录密码，若WIFI精灵没有设置过局域网络，默认密码仍为vanst。\n" +
        		"4：背景图标保存在手机或PAD中的储存器或SD卡中的vanst文件夹，没有请新建一个，图片支持三种格式jpg,png,bmg。图片文件名格式为诸如1.jpg,2.png等，文件名的数字表示图片的房间号码，存储完成后可直接重启应用识别。\n" +
        		"5：一般设备有两种基本状态，红色图标的表示为开，蓝色图标为关，如果是灰色图标可能无该设备或状态未知。\n\n\n" + 
        		"             关于Vanst生活：\n\n 版本："+version+"\n 通用版本：Android 2.3.3及以上\n公司：澜腾智能 Copyright © 2012 Vanst Technology Co., Ltd.\n电话：400 820 4959\n邮箱：vanst@vanst.com\n网址：http://www.vanst.com\n地址：上海市静安区万航渡路623弄85号建华大厦501室 ");
        infoText.setVisibility(View.GONE);
        
        listView.setVisibility(View.GONE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(flag == 3){
					if(arg2 == 0){
						LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this); 
				        View myLoginView = layoutInflater.inflate(R.layout.dialog_wifi, null); 
						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						final EditText editText = new EditText(MainActivity.this);
						editText.setText(Connect.useIp);
						editText.setHint("10.10.100.254");
						editText.setSingleLine();
						builder.setView(myLoginView);
						builder.setTitle("设置WIFI连接：（已经连接上的服务器如下）");
//						builder.setMessage("服务器IP地址：");
						builder.setIcon(R.drawable.bt_wifi);
						String []ips = null;
						String ip = "";
						if(Connect.sockets!=null && Connect.sockets.size() != 0){
							ips = new String[Connect.sockets.size()];
				    		int j = 0;
				    		for(SocketIP socketIP:Connect.sockets){
				    			ips[j] = socketIP.ip;
				    			ip += socketIP.ip;
				    			if(j+1 != Connect.sockets.size()){
				    				ip+= "\n";
				    			}
				    			j++;
				    		}
				    		
						}else{
							ips = new String[0];
							if(Connect.flag && !Connect.is3G){
								ip = "10.10.100.254";
							}
						}
						CheckBox checkBox = (CheckBox)myLoginView.findViewById(R.id.dialog_wifi_checkbox);
						SharedPreferences pref=getSharedPreferences("config", MODE_PRIVATE);
				        checkBox.setChecked(pref.getBoolean("autowifi", true));
						checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							
							@Override
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								// TODO Auto-generated method stub
								SharedPreferences pref=getSharedPreferences("config", Context.MODE_PRIVATE);
								SharedPreferences.Editor editor=pref.edit();
								editor.putBoolean("autowifi", isChecked);
								editor.commit();
							}
						});
						TextView textView = (TextView)myLoginView.findViewById(R.id.dialog_wifi_textview);
						textView.setText(ip);
//			    		builder.setItems(ips,null);
//						builder.setView(editText);
//						builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface arg0, int arg1) {
//								Connect.useIp = editText.getText().toString();
//								writePre();
//							}
//						});
						builder.setNeutralButton("重新连接",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
//								Connect.useIp = editText.getText().toString();
//								writePre();
//								MyLog.i("IP",editText.getText().toString());
								netPre = new ArrayList<String>();
								new Connect(MainActivity.this,true);
							}
						});
						builder.setPositiveButton("修改WLAN", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								changeWifi();
							}
						});
						builder.setNegativeButton("取消", null);
						builder.show();
						
//						Toast.makeText(Connect.activity,Connect.connectNum+"",500).show();
						
						/**
						
						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						final EditText editText = new EditText(MainActivity.this);
						editText.setText(Connect.useIp);
						editText.setHint("10.10.100.254");
						editText.setSingleLine();
						builder.setTitle("设置WIFI连接");
						builder.setMessage("服务器IP地址：");
						builder.setIcon(R.drawable.bt_wifi);
						builder.setView(editText);
						builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								Connect.useIp = editText.getText().toString();
								writePre();
							}
						});
						builder.setNeutralButton("连接",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								Connect.useIp = editText.getText().toString();
								writePre();
								MyLog.i("IP",editText.getText().toString());
								netPre = new ArrayList<String>();
								new Connect(MainActivity.this);
							}
						});
						builder.setNegativeButton("取消", null);
						builder.show();
						*/
					}else if(arg2 == 1){
						LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this); 
				        View myLoginView = layoutInflater.inflate(R.layout.dialog_3g, null); 
						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						final EditText editText = (EditText)myLoginView.findViewById(R.id.dialog_edit);
						Button button = (Button)myLoginView.findViewById(R.id.dialog_bt);
						editText.setText(Connect.phoneNumber);
						editText.setHint("11位手机号");
						editText.setInputType(InputType.TYPE_CLASS_PHONE);
						editText.setSingleLine();
						
						button.setOnClickListener(new View.OnClickListener() {
							public void onClick(View arg0) {
								final SharedPreferences pref=getSharedPreferences("config", Context.MODE_PRIVATE);
								final String phones = pref.getString("phonehistroy", "");
								final String [] phoneNumbers = phones.split(" ");
								if(phones.length() !=0){
									AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
									builder.setTitle("历史记录");
									builder.setIcon(android.R.drawable.ic_menu_more);
									builder.setItems(phoneNumbers, new DialogInterface.OnClickListener() {
									    public void onClick(DialogInterface dialog, final int which) {
									    	Connect.phoneNumber = phoneNumbers[which];
									    	editText.setText(phoneNumbers[which]);
									    }
									});
									builder.setPositiveButton("清空记录", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface arg0, int arg1){
											SharedPreferences.Editor editor=pref.edit();
											editor.putString("phonehistroy", "");
											editor.commit();
										}
									});
									builder.setNegativeButton("返回", null);
									builder.show();
								}else{
									Toast.makeText(MainActivity.this, "记录为空" ,Toast.LENGTH_SHORT).show();
								}
							}
						});
						
						builder.setTitle("设置3G连接");
						builder.setMessage("服务器号码：");
						builder.setIcon(R.drawable.bt_3g);
						builder.setView(myLoginView);
						builder.setPositiveButton("用户认证", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1){
								if(checkPhoneNumber(editText.getText().toString())){
									final SharedPreferences pref=getSharedPreferences("config", Context.MODE_PRIVATE);
									final String phones = pref.getString("phonehistroy", "");
									final String [] phoneNumbers = phones.split(" ");
										Connect.phoneNumber = editText.getText().toString();
										writePre();
										Connect.phone3g = true;
										boolean have = false;
										for(String phoneNumber:phoneNumbers){
											if(phoneNumber.equals(Connect.phoneNumber)){
												have = true;
											}
										}
										if(!have){
//											SharedPreferences pref=getSharedPreferences("config", Context.MODE_PRIVATE);
											SharedPreferences.Editor editor=pref.edit();
											if(phones.length() == 0) editor.putString("phonehistroy", Connect.phoneNumber); 
											else editor.putString("phonehistroy", Connect.phoneNumber+" "+phones);
											editor.commit();
										}
										try{
											Connect.PhoneCall(MainActivity.this);
											Toast.makeText(MainActivity.this, "用户认证中...", Toast.LENGTH_LONG).show();
											Toast.makeText(MainActivity.this, "请耐心等待服务器的回复...", Toast.LENGTH_LONG).show();
										}catch (Exception e) {
											Toast.makeText(MainActivity.this, "无法认证，请检查是否插入SIM卡", Toast.LENGTH_LONG).show();
										}
										
								}else {
									Toast.makeText(MainActivity.this, "请输入正确的11位有效号码", Toast.LENGTH_LONG).show();
								}
							}
						});
						builder.setNeutralButton("3G连接",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								writePre();
								if(Connect._3GIp == null){
									Toast.makeText(MainActivity.this, "连接失败请先进行用户认证", Toast.LENGTH_LONG).show();
									AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
									builder.setTitle("连接失败！");
									builder.setIcon(R.drawable.bt_3g);
									builder.setMessage("请重新进行用户认证");
									builder.setPositiveButton("用户认证", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface arg0, int arg1) {
											Connect.phone3g = true;
											Connect.PhoneCall(MainActivity.this);
										}
									});
									builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface arg0, int arg1) {
											Toast.makeText(MainActivity.this, "请检查服务器号码的正确性再进行认证！",Toast.LENGTH_LONG).show();
										}
									});
									builder.show();
								}else{
									MyLog.i("3GIp",Connect._3GIp);
									netPre = new ArrayList<String>();
									new Connect(MainActivity.this,Connect._3GIp,true);
								}
								if(checkPhoneNumber(editText.getText().toString())){
									Connect.phoneNumber = editText.getText().toString();
								}
							}
						});
						builder.setNegativeButton("取消", null);
						builder.show();
					}else if(arg2 == 2){
						if(Connect.sockets != null && Connect.sockets.size()>1){
				    		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				    		builder.setTitle("有多个服务器链接，请选择一个进行绑定或解除绑定");
				    		String []ips = new String[Connect.sockets.size()];
				    		int j = 0;
				    		for(SocketIP socketIP:Connect.sockets){
				    			ips[j] = socketIP.ip;
				    			j++;
				    		}
				    		builder.setItems(ips,new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									Connect.in = Connect.ins[which];
									Connect.out = Connect.outs[which];
									Connect.socket = Connect.sockets.get(which).socket;
									bind();
//									if(Connect.checkConnect(MainActivity.this)){
//										Toast.makeText(MainActivity.this, "网络通信正常", Toast.LENGTH_SHORT).show();
//									}else{
//										Toast.makeText(MainActivity.this, "网络通信失败", Toast.LENGTH_SHORT).show();
//									}
								}
							});
				    		builder.show();
				    	}else{
				    		bind();
//				    		if(Connect.checkConnect(MainActivity.this)){
//								Toast.makeText(MainActivity.this, "网络通信正常", Toast.LENGTH_SHORT).show();
//							}else{
//								Toast.makeText(MainActivity.this, "网络通信失败", Toast.LENGTH_SHORT).show();
//							}
				    	}                                                                                                                                 
						
					}else if(arg2 == 3){
						Intent intent = new Intent("android.intent.action.proWifi");
						startActivity(intent);
					}else if(arg2 == 4){
						Intent intent = new Intent("android.intent.action.proPassword");
						startActivity(intent);
					}else if(arg2 == 5){
						/**
						if(Connect.flag){
							final String [] strings = Config.getConfigNames();
							if(strings != null){
								Arrays.sort(strings);
								String [] strings2 = new String[strings.length];
								for(int i =0;i<strings.length;i++){
									strings2[i] = "房间"+ strings[i].subSequence(6, strings[i].lastIndexOf('.'));
								}
								AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
								builder.setTitle("选择要上传的房间");
								builder.setIcon(android.R.drawable.ic_menu_more);
								builder.setItems(strings2, new DialogInterface.OnClickListener() {
								    public void onClick(DialogInterface dialog, final int which) {
								    	final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
										builder.setTitle("请输入密码");
										builder.setIcon(android.R.drawable.ic_menu_edit);
										final EditText editText = new EditText(MainActivity.this);
										editText.setHint("同步密码");
										editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
										builder.setView(editText);
										builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface arg0, int arg1) {
												Connect.pwd = editText.getText().toString();
												Connect.isShared = 2;
												Device.typeBytes = Config.readConfig(strings[which]);
												Connect.filename = strings[which].substring(0, strings[which].length()-6);
//												Toast.makeText(MainActivity.this,Connect.filename+" "+Device.typeBytes.length/1024+"KB "+Device.typeBytes.length%1024+"B",Toast.LENGTH_LONG).show();
												Connect.askShare(editText.getText().toString());
												progressDialog = new ProgressDialog(MainActivity.this);
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
								    }
								   });
								builder.setPositiveButton("取消", null);
								builder.show();
							}else{
								Toast.makeText(MainActivity.this, "本地没有配置文件", Toast.LENGTH_LONG).show();
							}
						}else{
							Toast.makeText(MainActivity.this, "服务器未连接", Toast.LENGTH_LONG).show();
						}
						*/
						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		   				builder.setTitle("请选择要上传到那里？");
		   				builder.setIcon(R.drawable.bt_upload);
		   				builder.setPositiveButton("上传至服务器", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								if(Connect.flag){
									if(Connect.sockets != null && Connect.sockets.size()>1){
							    		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
							    		builder.setTitle("有多个服务器链接，请选择一个进行上传");
							    		String []ips = new String[Connect.sockets.size()];
							    		int j = 0;
							    		for(SocketIP socketIP:Connect.sockets){
							    			ips[j] = socketIP.ip;
							    			j++;
							    		}
							    		builder.setItems(ips,new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												Connect.in = Connect.ins[which];
												Connect.out = Connect.outs[which];
												Connect.socket = Connect.sockets.get(which).socket;

												final String [] strings = Config.getConfigNames();
												Arrays.sort(strings);
												if(strings != null){
													String [] strings2 = new String[strings.length];
													for(int i =0;i<strings.length;i++){
														strings2[i] = "房间"+ strings[i].subSequence(6, strings[i].lastIndexOf('.'));
													}
													AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
													builder.setTitle("选择要上传的房间");
													builder.setIcon(R.drawable.bt_upload);
													builder.setItems(strings2, new DialogInterface.OnClickListener() {
													    public void onClick(DialogInterface dialog, final int which) {
													    	final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
															builder.setTitle("请输入密码");
															builder.setIcon(android.R.drawable.ic_menu_edit);
															final EditText editText = new EditText(MainActivity.this);
															editText.setHint("同步密码");
															editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
															builder.setView(editText);
															builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
																public void onClick(DialogInterface arg0, int arg1) {
																	Connect.pwd = editText.getText().toString();
																	Connect.isShared = 2;
																	/**
																	Device.typeBytes = Config.readConfig(strings[which]);
																	*/
																	Device.typeBytes = Config.uploadConfig(strings[which]);
																	Connect.filename = strings[which].substring(0, strings[which].length()-6);
																	Toast.makeText(MainActivity.this,Connect.filename+" "+Device.typeBytes.length/1024+"KB "+Device.typeBytes.length%1024+"B",Toast.LENGTH_LONG).show();
																	Connect.askShare(editText.getText().toString());
																	progressDialog = new ProgressDialog(MainActivity.this);
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
													    }
													   });
													builder.setPositiveButton("取消", null);
													builder.show();
												}else{
													Toast.makeText(MainActivity.this, "本地没有配置文件", Toast.LENGTH_LONG).show();
												}
											}
										});
							    		builder.show();
							    	}else{

										final String [] strings = Config.getConfigNames();
										Arrays.sort(strings);
										if(strings != null){
											String [] strings2 = new String[strings.length];
											for(int i =0;i<strings.length;i++){
												strings2[i] = "房间"+ strings[i].subSequence(6, strings[i].lastIndexOf('.'));
											}
											AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
											builder.setTitle("选择要上传的房间");
											builder.setIcon(R.drawable.bt_upload);
											builder.setItems(strings2, new DialogInterface.OnClickListener() {
											    public void onClick(DialogInterface dialog, final int which) {
											    	final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
													builder.setTitle("请输入密码");
													builder.setIcon(android.R.drawable.ic_menu_edit);
													final EditText editText = new EditText(MainActivity.this);
													editText.setHint("同步密码");
													editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
													builder.setView(editText);
													builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
														public void onClick(DialogInterface arg0, int arg1) {
															Connect.pwd = editText.getText().toString();
															Connect.isShared = 2;
															/**
															Device.typeBytes = Config.readConfig(strings[which]);
															*/
															Device.typeBytes = Config.uploadConfig(strings[which]);
															Connect.filename = strings[which].substring(0, strings[which].length()-6);
															Toast.makeText(MainActivity.this,Connect.filename+" "+Device.typeBytes.length/1024+"KB "+Device.typeBytes.length%1024+"B",Toast.LENGTH_LONG).show();
															Connect.askShare(editText.getText().toString());
															progressDialog = new ProgressDialog(MainActivity.this);
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
											    }
											   });
											builder.setPositiveButton("取消", null);
											builder.show();
										}else{
											Toast.makeText(MainActivity.this, "本地没有配置文件", Toast.LENGTH_LONG).show();
										}
							    	}
									
									
									
								}else{
									Toast.makeText(MainActivity.this, "服务器未连接", Toast.LENGTH_LONG).show();
								}
							}
						});
		   				builder.setNeutralButton("上传至其他设备",  new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								SendFromPhone.isOK = true;
								new SendFromPhone(true,MainActivity.this);
								progressDialog = new ProgressDialog(MainActivity.this);
								progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								progressDialog.setMessage("等待其他设备连接");
								progressDialog.setButton("取消",new DialogInterface.OnClickListener() {
									
									public void onClick(DialogInterface arg0,
											int arg1) {
										SendFromPhone.isOK = false;
										if(SendFromPhone.serverSocket!=null)
										try {
											SendFromPhone.serverSocket.close();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								});
								progressDialog.setCancelable(false);
								progressDialog.show();
							}
						});
		   				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								
							}
						});
		   				
		   				builder.show();
						
					}else if(arg2 == 6){
//						if(Connect.flag){
//							Connect.readConfig2();
//						}else{
//							Toast.makeText(MainActivity.this, "服务器未连接", Toast.LENGTH_LONG).show();
//						}
						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		   				builder.setTitle("请选择要哪里下载？");
		   				builder.setIcon(R.drawable.bt_download);
		   				builder.setPositiveButton("从服务器下载", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								if(Connect.flag){
									if(Connect.sockets != null && Connect.sockets.size()>1){
							    		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
							    		builder.setTitle("有多个服务器链接，请选择一个进行下载");
							    		String []ips = new String[Connect.sockets.size()];
							    		int j = 0;
							    		for(SocketIP socketIP:Connect.sockets){
							    			ips[j] = socketIP.ip;
							    			j++;
							    		}
							    		builder.setItems(ips,new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												Connect.in = Connect.ins[which];
												Connect.out = Connect.outs[which];
												Connect.socket = Connect.sockets.get(which).socket;
												Connect.readConfig2();
											}
										});
							    		builder.show();
							    	}else{
							    		Connect.readConfig2();
							    	}
								}else{
									Toast.makeText(MainActivity.this, "服务器未连接", Toast.LENGTH_LONG).show();
								}
							}
						});
		   				builder.setNeutralButton("从其他设备下载",  new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								SendFromPhone.isOK = true;
								new SendFromPhone(false,MainActivity.this);
								progressDialog = new ProgressDialog(MainActivity.this);
								progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								progressDialog.setMessage("等待其他设备连接");
								progressDialog.setButton("取消",new DialogInterface.OnClickListener() {
									
									public void onClick(DialogInterface arg0,
											int arg1) {
										SendFromPhone.isOK = false;
									}
								});
								progressDialog.setCancelable(false);
								progressDialog.show();
							}
						});
		   				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								
							}
						});
		   				
		   				builder.show();

						
					}else if(arg2 == 7){
						final String [] strings = Config.getConfigNames();
						if(strings != null){
							Arrays.sort(strings);
							final String [] rooms = new String[strings.length];
							for(int i =0;i<strings.length;i++){
								rooms[i] = "房间"+ strings[i].subSequence(6, strings[i].lastIndexOf('.'));
							}
							AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
							builder.setTitle("选择要更新的房间");
							builder.setIcon(R.drawable.bt_update);
							builder.setItems(rooms, new DialogInterface.OnClickListener() {
							    public void onClick(DialogInterface dialog, final int which) {
							    	final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
							    	builder.setTitle("更新"+rooms[which]+"配置");
							    	builder.setIcon(android.R.drawable.ic_menu_edit);
							    	builder.setPositiveButton("替换背景图", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface arg0, int arg1) {
											final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
											builder.setTitle("替换"+rooms[which]+"背景图");
											builder.setIcon(R.drawable.bt_update);
											builder.setPositiveButton("从相册替换", new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface arg0, int arg1) {
													selectConfigPath = strings[which]; 
													Intent intent=new Intent("android.intent.action.proPhoto");
													startActivityForResult(intent, PhotoAc);
												}
											});
											builder.setNegativeButton("恢复默认背景",new DialogInterface.OnClickListener(){

												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
													builder.setTitle("确认要恢复"+rooms[which]+"为默认背景吗？");
													builder.setIcon(R.drawable.bt_update);
													builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
														public void onClick(DialogInterface arg0, int arg1) {
															Config config = Config.bytesToConfig(Config.readConfig(strings[which]));
															if(config !=null) config.bitmap = null;
															Config.saveConfig(config,strings[which]);
															if(strings[which].equals("config"+MainView.roomNum+".vanst")){
																new Thread(){
																	public void run() {
																		Config.askConfig(Config.readConfig(MainView.roomNum), mainView);
																		mainView.repaintRoom();
																	};
																}.start();
															}
														}
													});
													builder.setNegativeButton("取消", null);
													builder.show();
												}
												
											});
											builder.setNeutralButton("拍照替换", new DialogInterface.OnClickListener(){

												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													selectConfigPath = strings[which]; 
													Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
													intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imageFilePath)));
													startActivityForResult(intent, CAMERA);
												}
												
											});
											builder.show();
										}
									});
							    	builder.setNeutralButton("清空配置", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface arg0, int arg1) {
									    	final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
											builder.setTitle("确认要清空"+rooms[which]+"吗？");
											builder.setIcon(R.drawable.bt_update);
											builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface arg0, int arg1) {
													Config.deleteConfig(strings[which]);
													if(strings[which].equals("config"+MainView.roomNum+".vanst")){
														new Thread(){
															public void run() {
																Config.askConfig(Config.readConfig(MainView.roomNum), mainView);
																mainView.repaintRoom();
															};
														}.start();
													}
												}
											});
											builder.setNegativeButton("取消", null);
											builder.show();
										}
									});
									builder.setNegativeButton("取消", null);
									builder.show();
							    	
							    	
//							    	 builder = new AlertDialog.Builder(MainActivity.this);
//									builder.setTitle("确认要更新"+strings[which]+"吗？");
//									builder.setIcon(android.R.drawable.ic_menu_edit);
//									builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//										public void onClick(DialogInterface arg0, int arg1) {
//											Config.deleteConfig(strings[which]);
//											if(strings[which].equals("config"+MainView.roomNum+".vanst")){
//												new Thread(){
//													public void run() {
//														Config.askConfig(Config.readConfig(MainView.roomNum), mainView);
//														mainView.repaintRoom();
//													};
//												}.start();
//											}
//										}
//									});
//									builder.setNegativeButton("取消", null);
//									builder.show();
							    }
							   });
							builder.setPositiveButton("取消", null);
							builder.show();
						}else{
							Toast.makeText(MainActivity.this, "本地无配置文件", Toast.LENGTH_LONG).show();
						}
					}else if(arg2 == 8){
						if(Connect.flag){
							Connect.readPhone();
						}else{
							Toast.makeText(MainActivity.this, "服务器未链接", Toast.LENGTH_LONG).show();
						}
					}else if(arg2 == 9){
						sound();
					}
				}
			}
		});
        deviceListView = (HorizontalListView)findViewById(R.id.horizontalListView);
        deviceListView.setVisibility(View.GONE);
        initDeviceTypes();
		initAdpter();
		deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) { 
				if(isSetting){ 
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					builder.setTitle(deviceNames[arg2]+"控制:");
					builder.setIcon(Device.DEVICE_IMAGES[arg2]);
					builder.setPositiveButton("新增", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
							builder.setTitle("选择新增设备的编号");
							builder.setIcon(Device.DEVICE_IMAGES[arg2]);
							final EditText editText = new EditText(MainActivity.this);
							editText.setHint("编号");
							editText.setInputType(InputType.TYPE_CLASS_PHONE);
							builder.setView(editText);
							builder.setPositiveButton("增加", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0, int arg1) {
									int index = 0;
									boolean isCover = false;
									if(editText.getText().toString().length()!=0 ){
										try{
											index = Integer.parseInt(editText.getText().toString());
											MyLog.i("wfw","fwf");
											if(index > 0 && index < 100){
												for(Device device:MainView.devices){
													if(device.visible && device.type == arg2+1 && device.index == index){
														isCover = true;
														Toast.makeText(MainActivity.this, "编号输入重复！", Toast.LENGTH_LONG).show();
													}
												}
												if(!isCover){
													mainView.addDevice(arg2+1, index);
													MainView.isChanged = true;
												}
											}else{
												MainActivity.musicDo.musicError(MainActivity.this);
												Toast.makeText(MainActivity.this, "编号范围在1~99", Toast.LENGTH_LONG).show();
											}
											MyLog.i("wfw","fwfee");
										}catch (Exception e) {
											MainActivity.musicDo.musicError(MainActivity.this);
											Toast.makeText(MainActivity.this, "编号输入有误！", Toast.LENGTH_LONG).show();
										}
									}else{
										MainActivity.musicDo.musicError(MainActivity.this);
										Toast.makeText(MainActivity.this, "编号不能为空", Toast.LENGTH_LONG).show();
									}
								}
							});
							builder.setNegativeButton("取消", null);
							builder.show();
						}
					});
					builder.setNeutralButton("移除", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							int count = 0;
							for(Device device:MainView.devices){
								if(device.visible && device.type == arg2+1){
									count++;
								}
							}
							MyLog.i("count", count+"");
							if(count != 0){
								int i = 0;
								String [] strings = new String[count];
								final Device[] deleteDevices = new Device[count];
								for(int j = 1;j < 100;j++){
									for(Device device:MainView.devices){
										if(device.visible && device.type == arg2+1 && device.index == j){
											strings[i] = deviceNames[device.type-1]+device.index;
											deleteDevices[i] = device;
											i++;
										}
									}
								}
								AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
								builder.setTitle("在需要移除的设备旁边打勾");
								builder.setIcon(android.R.drawable.ic_menu_more);
								builder.setMultiChoiceItems(strings,
															new boolean[count], new  DialogInterface.OnMultiChoiceClickListener() {
																public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
																	deleteDevices[arg1].visible = !arg2;
																	MainView.isChanged = true;
																}
															});
								builder.setPositiveButton("确认", null);
								builder.show();
							}else{
								Toast.makeText(MainActivity.this, "无该类型设备", Toast.LENGTH_LONG).show();
							}
						}
					});
					builder.setNegativeButton("取消", null);
					builder.show();
//					dialog(arg2);
				}else{
					mainView.selectType = arg2+1;
					boolean you = false;
					for(Device device:MainView.devices){
						if(device.type == mainView.selectType){
							you = true;
						}
					}
					if(you){
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					builder.setTitle(deviceNames[arg2]+"控制:");
					builder.setIcon(Device.DEVICE_IMAGES[arg2]);
					builder.setPositiveButton("全开", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							mainView.allFlag = 0x01;
							mainView.handler.sendEmptyMessage(MainView.ASKPOSE_SINGLE);
						}
					});
					builder.setNegativeButton("取消", null);
					builder.setNeutralButton("全关", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							mainView.allFlag = 0x00;
							mainView.handler.sendEmptyMessage(MainView.ASKPOSE_SINGLE);
						}
					});
					builder.show();
					}else{
						Toast.makeText(MainActivity.this, "无此类设备！" ,Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
//        imageButton1.setOnLongClickListener(new View.OnLongClickListener() {
//			public boolean onLongClick(View arg0) {
//				if(deviceListView.getVisibility() == View.VISIBLE){
//					deviceListView.setVisibility(View.GONE);
//				}else if(!buttonFlag){
//					buttonFlag = true;
//					linearLayout.setVisibility(View.VISIBLE);
//				}else{
//					buttonFlag = false;
//					linearLayout.setVisibility(View.GONE);
//				}
//				return true;
//			}
//		});
        imageButton1.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				if(deviceListView.getVisibility() == View.GONE)
					deviceListView.setVisibility(View.VISIBLE);
				else 
					deviceListView.setVisibility(View.GONE);
			}
		});
        imageButton2.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View arg0) {
				if(isSetting){
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("房间数量与设备显示默认角度设置");
				builder.setIcon(android.R.drawable.ic_menu_edit);
				builder.setPositiveButton("房间数量", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
							final EditText editText = new EditText(MainActivity.this);
							editText.setText(Room.count+"");
							editText.setHint("房间数量");
							editText.setInputType(InputType.TYPE_CLASS_PHONE);
							editText.setSingleLine();
							builder.setTitle("房间数量:");
							builder.setIcon(android.R.drawable.ic_menu_edit);
							builder.setView(editText);
							builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0, int arg1) {
									int count = 0;
									try {
										count = Integer.valueOf(Integer.valueOf(editText.getText().toString()));
									} catch (Exception e) {
									}
									if(count > 0&&count < 100){
										Room.count = count;
										writePre();
										Config.reInitRooms(count);
									}else{
										Toast.makeText(MainActivity.this, "房间总数必须大于等于1并且小于100", Toast.LENGTH_LONG).show();
									}
								}
							});
							builder.setNeutralButton("取消", null);
							builder.show();
						
					}
				});
				builder.setNegativeButton("角度设置", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						builder.setTitle("设备显示默认角度设置");
						builder.setPositiveButton("逆时针旋转90度", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								Room.defaultAngle += 90;
								if(Room.defaultAngle == 360) Room.defaultAngle = 0;
								builder.show();
								SharedPreferences pref=getSharedPreferences("config", Context.MODE_PRIVATE);
								SharedPreferences.Editor editor=pref.edit();
								editor.putInt("angel", Room.defaultAngle);
								editor.commit();
							}
						});
						builder.setNeutralButton("顺时针旋转90度", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								Room.defaultAngle -= 90;
								if(Room.defaultAngle == -360) Room.defaultAngle = 0;
								builder.show();
								SharedPreferences pref=getSharedPreferences("config", Context.MODE_PRIVATE);
								SharedPreferences.Editor editor=pref.edit();
								editor.putInt("angel", Room.defaultAngle);
								editor.commit();
							}
						});
						builder.setNegativeButton("完成", null);
						builder.show();
					}
				});
				
				builder.setNeutralButton("房间备注", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						final EditText editText = new EditText(MainActivity.this);
						editText.setText(MainView.remark);
						editText.setHint("房间备注");
						editText.setSingleLine();
						builder.setTitle("房间备注:");
						builder.setIcon(android.R.drawable.ic_menu_edit);
						builder.setView(editText);
						builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								MainView.remark = editText.getText().toString();
								MainView.isChanged = true;
							}
						});
						builder.setNeutralButton("取消", null);
						builder.show();
					}
				});
				builder.show();
				}else{
					if(progressDialog != null) progressDialog.dismiss();
					progressDialog = new ProgressDialog(MainActivity.this);
					progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressDialog.setMessage("正在加载，请稍后...");
					progressDialog.setCancelable(false);
					progressDialog.show();
					new Thread(){
						public void run() {
							mainView.refreshThread.isRun = false;
							MainView.roomNum--;
//							if(MainView.bitmap!=null&&!MainView.bitmap.isRecycled())
//								MainView.bitmap.recycle();
							if(MainView.roomNum < 1) MainView.roomNum = Room.count;
							Config.askConfig(Config.readConfig(MainView.roomNum), mainView);
							mainView.repaintRoom();
							mainView.refreshThread.isRun = true;
							try {
								sleep(300);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							handler.sendEmptyMessage(PROGRESS_CANCEL);
							handler.sendEmptyMessage(UPDATA_WIDGET);
							Connect.getPose((byte)0);
						};
					}.start();
					
				}
				return true;
			}
		});
        imageButton2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(MainView.isChanged && isSetting) shareConfig();
				else{
					if(progressDialog != null) progressDialog.dismiss();
					progressDialog = new ProgressDialog(MainActivity.this);
					progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressDialog.setMessage("正在加载，请稍后...");
					progressDialog.setCancelable(false);
					progressDialog.show();
					new Thread(){
						public void run() {
							mainView.refreshThread.isRun = false;
							MainView.roomNum++;
//							if(MainView.bitmap!=null&&!MainView.bitmap.isRecycled())
//							MainView.bitmap.recycle();
							if(MainView.roomNum > Room.count) MainView.roomNum = 1;
					    	Config.askConfig(Config.readConfig(MainView.roomNum), mainView);
							mainView.repaintRoom();
							mainView.refreshThread.isRun = true;
							try {
								sleep(300);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							handler.sendEmptyMessage(PROGRESS_CANCEL);
							handler.sendEmptyMessage(UPDATA_WIDGET);
							if(flag == 1) Connect.getPose((byte)0);
						};
					}.start();
				}
			}
		});
        imageButton3.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				int count = 0;
		    	for(Device device:MainView.devices){
		    		if(device.visible){
		    			count++;
		    		}
		    	}
		    	if(count != 0){
				String[] strings = new String[]{
						"全部打开",
						"全部关闭",
						"会客模式",
						"娱乐模式",
						"外出模式",
						"睡眠模式",
					
				};
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setIcon(android.R.drawable.ic_menu_info_details);
				if(flag == 1)builder.setTitle("请选择情景模式");
				else if(flag == 2)builder.setTitle("请选择要设置的情景模式");
				builder.setItems(strings, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(flag == 1){
							if(Connect.flag) mainView.modeHandler.sendEmptyMessage(which);
							else Toast.makeText(MainActivity.this, "服务器未连接！", Toast.LENGTH_LONG).show();
						}else if(flag == 2){
							modeSet(which);
						}
									
					}
				});
				builder.setPositiveButton("返回", null);
				builder.show();
		    	}else{
		    		Toast.makeText(MainActivity.this, "设备列表为空，请先添加设备", Toast.LENGTH_LONG).show();
		    	}
			}
		});
        
        
        controlButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(MainView.isChanged) shareConfig();
				controlButton.setImageResource(R.drawable.bt_1_1);
	        	settingButton.setImageResource(R.drawable.mybutton2);
	        	netButton.setImageResource(R.drawable.mybutton3);
	        	infoButton.setImageResource(R.drawable.mybutton4);
	        	imageButton1.setEnabled(true);
	        	imageButton2.setEnabled(true);
	        	imageButton3.setEnabled(true);
//				controlButton.setTextColor(Color.RED);
//				controlButton.setBackgroundResource(R.drawable.touming);
//				settingButton.setTextColor(Color.WHITE);
//				settingButton.setBackgroundResource(R.drawable.mybutton);
//				netButton.setTextColor(Color.WHITE);
//				netButton.setBackgroundResource(R.drawable.mybutton);
//				infoButton.setTextColor(Color.WHITE);
//				infoButton.setBackgroundResource(R.drawable.mybutton);
				flag = 1;
				isSetting = false;
				mainView.refreshThread.isRun =true;
				frameLayout.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				deviceListView.setVisibility(View.GONE);
				infoText.setVisibility(View.GONE);
				Connect.getPose((byte)0);
			}
		});
		settingButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				SharedPreferences pref=getSharedPreferences("config", MODE_PRIVATE);
				final String password = pref.getString("password", "vanst");
				if(password.equals("vanst")) builder.setTitle("请输入vanst");
				else builder.setTitle("请输入密码");
				builder.setIcon(android.R.drawable.ic_menu_edit);
				final EditText editText = new EditText(MainActivity.this);
				if(password.equals("vanst")) editText.setHint("vanst"); 
				else editText.setHint("配置密码");
				editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				builder.setView(editText);
				builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						if(editText.getText().toString().equals("clearallfiles")){
							if(Connect.sockets != null && Connect.sockets.size()>1){
					    		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					    		builder.setTitle("有多个服务器链接，请选择一个进行下载");
					    		String []ips = new String[Connect.sockets.size()];
					    		int j = 0;
					    		for(SocketIP socketIP:Connect.sockets){
					    			ips[j] = socketIP.ip;
					    			j++;
					    		}
					    		builder.setItems(ips,new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										Connect.in = Connect.ins[which];
										Connect.out = Connect.outs[which];
										Connect.socket = Connect.sockets.get(which).socket;
										Connect.clearAllFiles();
									}
								});
					    		builder.show();
					    	}else{
					    		Connect.clearAllFiles();
					    	}
							
						}else if(editText.getText().toString().equals("restartwifidevice")){
							if(Connect.sockets != null && Connect.sockets.size()>1){
					    		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					    		builder.setTitle("有多个服务器链接，请选择一个进行下载");
					    		String []ips = new String[Connect.sockets.size()];
					    		int j = 0;
					    		for(SocketIP socketIP:Connect.sockets){
					    			ips[j] = socketIP.ip;
					    			j++;
					    		}
					    		builder.setItems(ips,new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										Connect.in = Connect.ins[which];
										Connect.out = Connect.outs[which];
										Connect.socket = Connect.sockets.get(which).socket;
										Connect.reStartWifi();
									}
								});
					    		builder.show();
					    	}else{
					    		Connect.reStartWifi();
					    	}
//						}else if(editText.getText().toString().equals("b")){
//							Intent intent = new Intent("android.intent.action.proDesign");
//							startActivity(intent);
						}else if(editText.getText().toString().equals(password)){
							controlButton.setImageResource(R.drawable.mybutton1);
				        	settingButton.setImageResource(R.drawable.bt_2_1);
				        	netButton.setImageResource(R.drawable.mybutton3);
				        	infoButton.setImageResource(R.drawable.mybutton4);
				        	imageButton1.setEnabled(true);
				        	imageButton2.setEnabled(true);
				        	imageButton3.setEnabled(true);
//							controlButton.setTextColor(Color.WHITE);
//							controlButton.setBackgroundResource(R.drawable.mybutton);
//							settingButton.setTextColor(Color.RED);
//							settingButton.setBackgroundResource(R.drawable.touming);
//							netButton.setTextColor(Color.WHITE);
//							netButton.setBackgroundResource(R.drawable.mybutton);
//							infoButton.setTextColor(Color.WHITE);
//							infoButton.setBackgroundResource(R.drawable.mybutton);
							flag = 2;
							isSetting = true;
							mainView.refreshThread.isRun =true;
							frameLayout.setVisibility(View.VISIBLE);
							listView.setVisibility(View.GONE);
							infoText.setVisibility(View.GONE);
							Connect.isLogin = true;
						}else{
							Toast.makeText(MainActivity.this, "密码输入错误", Toast.LENGTH_LONG).show();
							MainActivity.musicDo.musicError(MainActivity.this);
						}
					}
				});
				builder.setNegativeButton("取消", null);
				builder.show();
			}
		});
		netButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(MainView.isChanged)shareConfig();
				controlButton.setImageResource(R.drawable.mybutton1);
	        	settingButton.setImageResource(R.drawable.mybutton2);
	        	netButton.setImageResource(R.drawable.bt_3_1);
	        	infoButton.setImageResource(R.drawable.mybutton4);
	        	imageButton1.setEnabled(false);
	        	imageButton2.setEnabled(false);
	        	imageButton3.setEnabled(false);
//				controlButton.setTextColor(Color.WHITE);
//				controlButton.setBackgroundResource(R.drawable.mybutton);
//				settingButton.setTextColor(Color.WHITE);
//				settingButton.setBackgroundResource(R.drawable.mybutton);
//				netButton.setTextColor(Color.RED);
//				netButton.setBackgroundResource(R.drawable.touming);
//				infoButton.setTextColor(Color.WHITE);
//				infoButton.setBackgroundResource(R.drawable.mybutton);
				flag = 3;
				isSetting = false;
				mainView.refreshThread.isRun =false;
				listView.setVisibility(View.VISIBLE);
				deviceListView.setVisibility(View.GONE);
		    	frameLayout.setVisibility(View.GONE);
		    	infoText.setVisibility(View.GONE);
			}
		});
		infoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
//				Config.getConfigCl();
//				Config.saveConfig(Device.typeBytes, 20);
				if(MainView.isChanged)shareConfig();
				controlButton.setImageResource(R.drawable.mybutton1);
	        	settingButton.setImageResource(R.drawable.mybutton2);
	        	netButton.setImageResource(R.drawable.mybutton3);
	        	infoButton.setImageResource(R.drawable.bt_4_1);
	        	imageButton1.setEnabled(false);
	        	imageButton2.setEnabled(false);
	        	imageButton3.setEnabled(false);
//				controlButton.setTextColor(Color.WHITE);
//				controlButton.setBackgroundResource(R.drawable.mybutton);
//				settingButton.setTextColor(Color.WHITE);
//				settingButton.setBackgroundResource(R.drawable.mybutton);
//				netButton.setTextColor(Color.WHITE);
//				netButton.setBackgroundResource(R.drawable.mybutton);
//				infoButton.setTextColor(Color.RED);
//				infoButton.setBackgroundResource(R.drawable.touming);
				flag = 4;
				isSetting = false;
				mainView.refreshThread.isRun =false;
//				imageView.setBackgroundResource(R.drawable.info);
				infoText.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				deviceListView.setVisibility(View.GONE);
		    	frameLayout.setVisibility(View.GONE);
					
					
//					packages = getPackageManager().getInstalledApplications(0) ;
//					if(packages != null){
//						String[] items = new String[packages.size()];
//						String ss = "";
//						int i = 0;
//						for(ApplicationInfo applicationInfo:packages){
//							items[i] = (String) applicationInfo.loadLabel(getPackageManager());
////							ss+=applicationInfo.packageName;
//							i++;
//						}
//						AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
//						builder.setItems(items, new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog, int which) {
//								try {
//									ApplicationInfo appInfo = packages.get(which); 
//					                Intent intent = getPackageManager().getLaunchIntentForPackage( 
//					                        appInfo.packageName); 
//					                startActivity(intent); 
//								} catch (Exception e) {
//									// TODO: handle exception
//								}
//								
//							}
//						});
//						builder.show();
//					}
			}
		});
		MainView.intiFlag = true;
		new HeartThread().start();
		if(!exit)musicDo.musicStart(this);
    }
    protected void onResume() {
    	Connect.activity = this;
    	Connect.isEnter = true;
    	StartActivity.isStart = true;
    	pause = false;
    	VanstWidgetProvider.initAppWidget(this);
    	MyLog.i("onrusume","onResume");
    	SharedPreferences pref=getSharedPreferences("config", MODE_PRIVATE);
    	Room.count = pref.getInt("rooms", 2);
        Connect.useIp = pref.getString("useip", "10.10.100.254");
        Connect._3GIp = pref.getString("lanip", null);
        Connect.phoneNumber = pref.getString("phone", null);
        Room.defaultAngle = pref.getInt("angel", 0);
        try {
        	if(flag >= 3 && Integer.parseInt(android.os.Build.VERSION.SDK)<=10){
                if(Connect.rwPose != Connect.learn){
              	controlButton.setImageResource(R.drawable.bt_1_1);
              	settingButton.setImageResource(R.drawable.mybutton2);
              	netButton.setImageResource(R.drawable.mybutton3);
              	infoButton.setImageResource(R.drawable.mybutton4);
      		isSetting = false;
              }else{
              	Connect.rwPose = Connect.control;
              }
                flag = 1;
      		frameLayout.setVisibility(View.VISIBLE);
      		listView.setVisibility(View.GONE);
      		deviceListView.setVisibility(View.GONE);
      		infoText.setVisibility(View.GONE);
      		imageButton1.setEnabled(true);
      		imageButton2.setEnabled(true);
      		imageButton3.setEnabled(true);
              }
//        	if(flag == 3){
//        		controlButton.setImageResource(R.drawable.mybutton1);
//	        	settingButton.setImageResource(R.drawable.mybutton2);
//	        	netButton.setImageResource(R.drawable.bt_3_1);
//	        	infoButton.setImageResource(R.drawable.mybutton4);
//	        	imageButton1.setEnabled(false);
//	        	imageButton2.setEnabled(false);
//	        	imageButton3.setEnabled(false);
//				flag = 3;
//				isSetting = false;
//				mainView.refreshThread.isRun =false;
//				listView.setVisibility(View.VISIBLE);
//				deviceListView.setVisibility(View.GONE);
//		    	frameLayout.setVisibility(View.GONE);
//		    	imageView.setVisibility(View.GONE);
//        	}else if (flag == 4){
//        		controlButton.setImageResource(R.drawable.mybutton1);
//	        	settingButton.setImageResource(R.drawable.mybutton2);
//	        	netButton.setImageResource(R.drawable.mybutton3);
//	        	infoButton.setImageResource(R.drawable.bt_4_1);
//	        	imageButton1.setEnabled(false);
//	        	imageButton2.setEnabled(false);
//	        	imageButton3.setEnabled(false);
//				flag = 4;
//				isSetting = false;
//				mainView.refreshThread.isRun =false;
//				imageView.setBackgroundResource(R.drawable.info);
//				imageView.setVisibility(View.VISIBLE);
//				listView.setVisibility(View.GONE);
//				deviceListView.setVisibility(View.GONE);
//		    	frameLayout.setVisibility(View.GONE);
//        	}
		} catch (Exception e) {
			// TODO: handle exception
		}
        

		if(flag <=2 && mainView!=null && mainView.refreshThread!=null)
		mainView.refreshThread.isRun =true;

		super.onResume();
		if(flag == 1 && Connect.flag) Connect.getPose((byte)0);
//		if(!Connect.flag){
//			if(!Connect.is3G)
//			new Connect(MainActivity.this,false);
//			else new Connect(MainActivity.this,Connect._3GIp,false);
//		}
		reConnect();
    }
    public void reConnect(){
    	if(Connect.disconnectShow && !Connect.flag && !pause){
    		if(!Connect.is3G)
    			new Connect(MainActivity.this,false);
    			else new Connect(MainActivity.this,Connect._3GIp,false);
			Connect.disconnectShow = false;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.stat_notify_error);
			builder.setTitle("错误");
			builder.setMessage("连接已经断开，请检查网络后重连");
			builder.setNegativeButton("取消",null);
			builder.setPositiveButton("WIFI连接", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					new Connect(MainActivity.this,true);
				}
			});
			builder.setNeutralButton("3G连接", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					new Connect(MainActivity.this,Connect._3GIp,true);
				}
					
			});
			AlertDialog dialog = builder.create();
//			dialog.show();
		}
    	
    }
    @Override
    protected void onPause() {
    	if(mainView!=null && mainView.refreshThread!=null)
    	mainView.refreshThread.isRun = false;
    	pause = true;
    	super.onPause();
    }
    private void initModeAdpter(int mode){
    	modeList = new ArrayList<Map<String,Object>>();
    	for(Device device:MainView.devices){
    		if(device.visible){
	    		deviceMap = new HashMap<String, Object>();
	    		if(mode == 2){
	    			if(device.moshi1 == 0) deviceMap.put("image", Device.DEVICE_OFF_IMAGES[device.type-1]);
	    			else if(device.moshi1 == 1) deviceMap.put("image", Device.DEVICE_ON_IMAGES[device.type-1]);
	    			else deviceMap.put("image", Device.DEVICE_UNKNOW_IMAGES[device.type-1]);
	    		}else if(mode == 3){
	    			if(device.moshi2 == 0) deviceMap.put("image", Device.DEVICE_OFF_IMAGES[device.type-1]);
	    			else if(device.moshi2 == 1) deviceMap.put("image", Device.DEVICE_ON_IMAGES[device.type-1]);
	    			else deviceMap.put("image", Device.DEVICE_UNKNOW_IMAGES[device.type-1]);
	    		}else if(mode == 4){
	    			if(device.moshi3 == 0) deviceMap.put("image", Device.DEVICE_OFF_IMAGES[device.type-1]);
	    			else if(device.moshi3 == 1) deviceMap.put("image", Device.DEVICE_ON_IMAGES[device.type-1]);
	    			else deviceMap.put("image", Device.DEVICE_UNKNOW_IMAGES[device.type-1]);
	    		}else if(mode == 5){
	    			if(device.moshi4 == 0) deviceMap.put("image", Device.DEVICE_OFF_IMAGES[device.type-1]);
	    			else if(device.moshi4 == 1) deviceMap.put("image", Device.DEVICE_ON_IMAGES[device.type-1]);
	    			else deviceMap.put("image", Device.DEVICE_UNKNOW_IMAGES[device.type-1]);
	    		}
	    		deviceMap.put("name", deviceNames[device.type-1]+device.index);
	    		deviceMap.put("device", device);
	    		modeList.add(deviceMap);
    		}
    		
    	}
    	modeAdapter = new SimpleAdapter(this, modeList, R.layout.modeset,
    									new String[]{"image","name"},
    									new int []{R.id.image,R.id.name});
    }
    
    private void initAdpter(){
         int i = 0;
         for(String deviceItem:deviceNames){
        	 deviceMap = new HashMap<String, Object>();
        	 deviceMap.put("image", Device.DEVICE_IMAGES[i]);
        	 deviceMap.put("name", deviceItem);
        	 namesList.add(deviceMap);
        	 i++;
         }
    	controlAdapter = new SimpleAdapter(this,connectList,R.layout.netlist,
				new String[]{"image","name"},
				new int[]{R.id.image,R.id.name});
    	listView.setAdapter(controlAdapter);
    	deviceAdapter = new SimpleAdapter(this, namesList, R.layout.devicelist,
    			new String[]{"image","name"},
				new int[]{R.id.image,R.id.name});
    	
    	deviceListView.setAdapter(deviceAdapter);
    }
    
    public void dialog(final int arg2){
		final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setIcon(Device.DEVICE_IMAGES[arg2]);
		builder.setTitle((String)deviceNums.get(arg2).get("name"));
		builder.setMessage("数量："+(String)deviceNums.get(arg2).get("number"));
		builder.setPositiveButton("+", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(Integer.parseInt((String)deviceNums.get(arg2).get("number"))<99)
                deviceNums.get(arg2).put("number",Integer.parseInt((String)deviceNums.get(arg2).get("number"))+1+"");
                builder.setMessage("数量："+(String)deviceNums.get(arg2).get("number"));
                mainView.addDevice(arg2+1, Integer.parseInt((String) deviceNums.get(arg2).get("number")));
                mainView.refreshThread.isRefresh = true;
                dialog(arg2);
			}
		});
		builder.setNegativeButton("-", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
                if(Integer.parseInt((String)deviceNums.get(arg2).get("number"))>0)
                deviceNums.get(arg2).put("number",Integer.parseInt((String)deviceNums.get(arg2).get("number"))-1+"");
                builder.setMessage("数量："+(String)deviceNums.get(arg2).get("number"));
                mainView.deleteDevice(arg2+1, Integer.parseInt((String) deviceNums.get(arg2).get("number"))+1);
                mainView.refreshThread.isRefresh = true;
                dialog(arg2);
			}
		});
		builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences pref=getSharedPreferences("config", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor=pref.edit();
				for(int i = 1;i <=deviceNames.length;i++){
					editor.putString(String.valueOf(i),(String)deviceNums.get(i-1).get("number"));
					MyLog.i((String)deviceNums.get(i-1).get("number"),(String)deviceNums.get(i-1).get("number"));
				}
				editor.commit();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface arg0) {
				SharedPreferences pref=getSharedPreferences("config", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor=pref.edit();
				for(int i = 1;i <=deviceNames.length;i++){
					editor.putString(String.valueOf(i),(String)deviceNums.get(i-1).get("number"));
					MyLog.i((String)deviceNums.get(i-1).get("number"),(String)deviceNums.get(i-1).get("number"));
				}
				editor.commit();
			}
		});
		dialog.show();
	}
    
    public void modeChoose(){
    	int count = 0;
    	for(Device device:MainView.devices){
    		if(device.visible){
    			count++;
    		}
    	}
    	if(count != 0){
    		
    	
    	String[] strings = new String[]{
				"全部打开",
				"全部关闭",
				"会客模式",
				"娱乐模式",
				"外出模式",
				"睡眠模式",
			
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setIcon(android.R.drawable.ic_menu_info_details);
		builder.setTitle("请选择要设置的情景模式");
		builder.setItems(strings, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				modeSet(which);
			}
		});
		builder.setPositiveButton("返回", null);
		builder.show();
    	}else{
    		Toast.makeText(this, "设备列表为空，请先添加设备", Toast.LENGTH_LONG).show();
    	}
    }
    
    public void modeSet(final int mode){
    	int count = 0;
    	for(Device device:MainView.devices){
    		if(device.visible){
    			count++;
    		}
    	}
    	int i = 0;
    	String [] stringNames = new String[count];
    	final boolean [] booleans = new boolean[count];
    	for(Device device:MainView.devices){
    		if(device.visible){
    			stringNames[i] = deviceNames[device.type-1]+device.index;
    			i++;
    		}
    	}
    	i = 0;
    	for(Device device:MainView.devices){
        	if(device.visible){
        		if(mode == 0){
        			if(device.allOn == 1) booleans[i] = true;
        			else booleans[i] = false;
        		}else if(mode == 1){
        			if(device.allOff == 1) booleans[i] = true;
        			else booleans[i] = false;
        		}else if(mode == 2){
        			if(device.moshi1 == 1) booleans[i] = true;
        			else booleans[i] = false;
        		}else if(mode == 3){
        			if(device.moshi2 == 1) booleans[i] = true;
        			else booleans[i] = false;
        		}else if(mode == 4){
        			if(device.moshi3 == 1) booleans[i] = true;
        			else booleans[i] = false;
        		}else if(mode == 5){
        			if(device.moshi4 == 1) booleans[i] = true;
        			else booleans[i] = false;
        		}
       			i++;
       		}
       	}
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	if(mode == 0)builder.setTitle("请在要全开的设备旁边打勾");
    	else if(mode == 1)builder.setTitle("请在全关的设备旁边打勾");
    	else if(mode == 2)builder.setTitle("请在会客模式下要设备状态（红色表示打开，蓝色表示关闭，灰色表示不响应)");
    	else if(mode == 3)builder.setTitle("请在娱乐模式下要设备状态（红色表示打开，蓝色表示关闭，灰色表示不响应)");
    	else if(mode == 4)builder.setTitle("请在外出模式下要设备状态（红色表示打开，蓝色表示关闭，灰色表示不响应)");
    	else if(mode == 5)builder.setTitle("请在睡眠模式下要设备状态（红色表示打开，蓝色表示关闭，灰色表示不响应)");
    	if(mode == 0 || mode == 1){
    		builder.setMultiChoiceItems(stringNames, booleans, new DialogInterface.OnMultiChoiceClickListener() {
    			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
    				booleans[which] = isChecked;
    			}
    		});
        	builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				int i = 0;
    				for(Device device:MainView.devices){
    		    		if(device.visible){
    		    			if(mode == 0){
    		    				if(booleans[i]) device.allOn = 1;
    		        			else device.allOn = 0;
    		    			}else if(mode == 1){
    		    				if(booleans[i]) device.allOff = 1;
    		    				else device.allOff = 0;
    		    			}
    		    			i++;
    		    		}
    		    	}
    				modeChoose();
    				MainView.isChanged = true;
    			}
    		});
        	builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				modeChoose();
    			}
    		});
        	builder.show();
    	}else{
    		initModeAdpter(mode);
    		builder.setAdapter(modeAdapter, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					try {   
		    		    Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");   
		    		    field.setAccessible(true);   
		    		    field.set(dialog, false);  
		    		} catch (Exception e) {   
		    		    e.printStackTrace();   
		    		}
					Device device = (Device) modeList.get(which).get("device");
					if(mode == 2){
						if(device.moshi1 == 0){
							device.moshi1 = 1;
							modeList.get(which).put("image", Device.DEVICE_ON_IMAGES[device.type-1]);
						}else if(device.moshi1 == 1){
							device.moshi1 = -1;
							modeList.get(which).put("image", Device.DEVICE_UNKNOW_IMAGES[device.type-1]);
						}else {
							device.moshi1 = 0;
							modeList.get(which).put("image", Device.DEVICE_OFF_IMAGES[device.type-1]);
						}
					}else if(mode == 3){
						if(device.moshi2 == 0){
							device.moshi2 = 1;
							modeList.get(which).put("image", Device.DEVICE_ON_IMAGES[device.type-1]);
						}else if(device.moshi2 == 1){
							device.moshi2 = -1;
							modeList.get(which).put("image", Device.DEVICE_UNKNOW_IMAGES[device.type-1]);
						}else {
							device.moshi2 = 0;
							modeList.get(which).put("image", Device.DEVICE_OFF_IMAGES[device.type-1]);
						}
					}else if(mode == 4){
						if(device.moshi3 == 0){
							device.moshi3 = 1;
							modeList.get(which).put("image", Device.DEVICE_ON_IMAGES[device.type-1]);
						}else if(device.moshi3 == 1){
							device.moshi3 = -1;
							modeList.get(which).put("image", Device.DEVICE_UNKNOW_IMAGES[device.type-1]);
						}else {
							device.moshi3 = 0;
							modeList.get(which).put("image", Device.DEVICE_OFF_IMAGES[device.type-1]);
						}
					}else if(mode == 5){
						if(device.moshi4 == 0){
							device.moshi4 = 1;
							modeList.get(which).put("image", Device.DEVICE_ON_IMAGES[device.type-1]);
						}else if(device.moshi4 == 1){
							device.moshi4 = -1;
							modeList.get(which).put("image", Device.DEVICE_UNKNOW_IMAGES[device.type-1]);
						}else {
							device.moshi4 = 0;
							modeList.get(which).put("image", Device.DEVICE_OFF_IMAGES[device.type-1]);
						}
					}
					modeAdapter.notifyDataSetChanged();
					MainView.isChanged = true;
				}
			});
    		builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					try {   
		    		    Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");   
		    		    field.setAccessible(true);   
		    		    field.set(dialog, true);  
		    		} catch (Exception e) {   
		    		    e.printStackTrace();   
		    		}
		    		modeChoose();
				}
			});
    		builder.show();
    	}
    	
    }
    
    private void initDeviceTypes(){
		int k = 0;
		for(String connectItem:connectItems){
			deviceMap = new HashMap<String, Object>();
			deviceMap.put("image",settingImages[k]);
			deviceMap.put("name", connectItem);
			connectList.add(deviceMap);
			k++;
		}
	}
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("退出Vanst生活");
			builder.setMessage("确认要退出Vanst生活吗？");
			builder.setNeutralButton("后台运行", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					moveTaskToBack(true);
				}
			});
			builder.setPositiveButton("退出程序", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					handler.sendEmptyMessage(UPDATA_WIDGET);
					String ns = Context.NOTIFICATION_SERVICE;
			        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
			        //定义通知栏展现的内容信息
			        int icon = R.drawable.ic_launcher;
			        CharSequence tickerText = "Vanst";
			        long when = System.currentTimeMillis();
			        Notification notification = new Notification(icon, tickerText, when);
			        //定义下拉通知栏时要展现的内容信息
			        Context context = getApplicationContext();
			        CharSequence contentTitle = "Vanst";
			        CharSequence contentText = "Vanst引领您走进全新的生活体验";
			        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vanst.com"));
			        PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this, 0,notificationIntent, 0);
			        notification.setLatestEventInfo(context, contentTitle, contentText,contentIntent); 
			        notification.flags|=Notification.FLAG_AUTO_CANCEL;
//			        notification.defaults |= Notification.DEFAULT_SOUND;
			        //用mNotificationManager的notify方法通知用户生成标题栏消息通知  
			        mNotificationManager.notify(1, notification);
			        exit = true; 
			        SharedPreferences pref=getSharedPreferences("config", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putLong("lasttime", System.currentTimeMillis());
					editor.commit();
					Connect.isEnter = false;
					VanstWidgetProvider.updateAppWidget(MainActivity.this);
					onDestroy();
					finish();
				}
			});
			builder.setNegativeButton("取消", null);
			AlertDialog dialog = builder.create();
			dialog.show();
			return false;
		}
		else return super.onKeyDown(keyCode, event);
	}
    @Override
    protected void onDestroy() {
    	mainView.refreshThread.isAlive = false;
    	mainView.refreshThread.isRun = false;
    	SharedPreferences pref=getSharedPreferences("config", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();
		if(Room.count!=0)editor.putInt("rooms", Room.count);
		if(Connect.useIp!=null)editor.putString("useip", Connect.useIp);
		if(Connect._3GIp!=null)editor.putString("lanip", Connect._3GIp);
		if(Connect.phoneNumber!=null)editor.putString("phone", Connect.phoneNumber);
		editor.commit();
		Connect.discoonectWifi();
		Connect.flag = false;
		VanstWidgetProvider.updateAppWidget(this);
//		if(Connect.socket!=null)
//			try {
//				Connect.socket.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
    	super.onDestroy();
    }
    private void writePre(){
		Connect.synchronizationHandler.sendEmptyMessage(Connect.PRE_SYNCHRONIZATIONE);
	}
    public void shareConfig(){
    	if(flag == 2){
    		MainView.isChanged = false;
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle("配置文件已经修改是否要将配置信息保存或上传到网络？");
			builder.setIcon(android.R.drawable.ic_menu_help);
			builder.setPositiveButton("保存并上传", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					Config config = Config.readConfig(MainView.roomNum);
					config.deviceconfigs = Device.initDevicesConfig();
					config.devicemodes = Device.initDevicesmodes();
					config.time = System.currentTimeMillis();
					config.remark = MainView.remark;
					Config.saveConfig(config, MainView.roomNum);
					if(Connect.flag){
						config.bitmap = null;
						Device.typeBytes = Config.configToBytes(config);
						final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						builder.setTitle("请输入密码");
						builder.setIcon(android.R.drawable.ic_menu_edit);
						final EditText editText = new EditText(MainActivity.this);
						editText.setHint("同步密码");
						editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
						builder.setView(editText);
						builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								Connect.pwd = editText.getText().toString();
								Connect.isShared = 0;
								Connect.askShare(editText.getText().toString());
								progressDialog = new ProgressDialog(MainActivity.this);
								progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								progressDialog.setMessage("请稍等..等待密码验证");
								progressDialog.setButton("取消",new DialogInterface.OnClickListener() {
									
									public void onClick(DialogInterface arg0,
											int arg1) {
										Connect.rwPose = Connect.control;
									}
								});
								progressDialog.setCancelable(false);
								progressDialog.show();
							}
						});
						builder.setNegativeButton("取消", null);
						builder.show();
					}else{
						Toast.makeText(MainActivity.this, "未连接服务器无法进行上传！", Toast.LENGTH_LONG).show();
					}
				}
			});
			builder.setNeutralButton("只保存不上传", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					Config config = Config.readConfig(MainView.roomNum);
					config.deviceconfigs = Device.initDevicesConfig();
					config.devicemodes = Device.initDevicesmodes();
					config.time = System.currentTimeMillis();
					config.remark = MainView.remark;
					Config.saveConfig(config, MainView.roomNum);
				}
			});
			builder.setNegativeButton("取消", null);
			builder.show();
		}
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
		case PhotoAc:
			if(resultCode==Activity.RESULT_OK){
				final String filePath=data.getStringExtra("path");
				if(filePath !=null){
					Bitmap bitmap = null;
					BitmapFactory.decodeFile(filePath, Room.optReadBound);
					int weigth = Room.optReadBound.outWidth;
					int height = Room.optReadBound.outHeight;
					if(weigth >= 200 || height >= 200){
						int inSampleSize = 0;
						if(weigth > height) inSampleSize = (int) (weigth / 100);
						else inSampleSize = (int)(height / 100);
						Room.optReadBitmap.inSampleSize = inSampleSize;
						bitmap = BitmapFactory.decodeFile(filePath,Room.optReadBitmap);
					}else{
						Room.optReadBitmap.inSampleSize = 1;
						bitmap = BitmapFactory.decodeFile(filePath,Room.optReadBitmap);
					}
					if(bitmap !=null){
						AlertDialog.Builder builder = new AlertDialog.Builder(this);
						builder.setTitle("确认要更换房间"+selectConfigPath.substring(6, selectConfigPath.lastIndexOf('.'))+"背景吗？");
						builder.setIcon(new BitmapDrawable(bitmap));
						builder.setPositiveButton("替换", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
//								Config.copyFile(Integer.parseInt(selectConfigPath.substring(6, selectConfigPath.lastIndexOf('.'))), filePath);
//								Toast.makeText(MainActivity.this, Integer.parseInt(selectConfigPath.substring(6, selectConfigPath.lastIndexOf('.')))+"", Toast.LENGTH_SHORT).show();
								Config config = Config.bytesToConfig(Config.readConfig(selectConfigPath));
								config.bitmap = Config.fomateBitmapBytes(filePath);
								Config.saveConfig(config, selectConfigPath);
								if(selectConfigPath.equals("config"+MainView.roomNum+".vanst")){
									progressDialog = new ProgressDialog(MainActivity.this);
									progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
									progressDialog.setMessage("正在加载，请稍后...");
									progressDialog.setCancelable(false);
									progressDialog.show();
									new Thread(){
										public void run() {
											mainView.refreshThread.isRun = false;
											Config.askConfig(Config.readConfig(MainView.roomNum), mainView);
											mainView.repaintRoom();
											mainView.refreshThread.isRun = true;
											if(Connect.flag)Connect.getPose((byte)0);
											try {
												sleep(300);
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
											handler.sendEmptyMessage(PROGRESS_CANCEL);
										};
									}.start();
								}
							}
						});
						builder.setNegativeButton("取消", null);
						builder.show();
					}
				}
			}
			break;
		case CAMERA:
			if(imageFilePath !=null){
				Bitmap bitmap = null;
				BitmapFactory.decodeFile(imageFilePath, Room.optReadBound);
				int weigth = Room.optReadBound.outWidth;
				int height = Room.optReadBound.outHeight;
				if(weigth >= 200 || height >= 200){
					int inSampleSize = 0;
					if(weigth > height) inSampleSize = (int) (weigth / 100);
					else inSampleSize = (int)(height / 100);
					Room.optReadBitmap.inSampleSize = inSampleSize;
					bitmap = BitmapFactory.decodeFile(imageFilePath,Room.optReadBitmap);
				}else{
					Room.optReadBitmap.inSampleSize = 1;
					bitmap = BitmapFactory.decodeFile(imageFilePath,Room.optReadBitmap);
				}
				if(bitmap !=null){
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("确认要更换房间"+selectConfigPath.substring(6, selectConfigPath.lastIndexOf('.'))+"背景吗？");
					builder.setIcon(new BitmapDrawable(bitmap));
					builder.setPositiveButton("替换", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
//							Config.copyFile(Integer.parseInt(selectConfigPath.substring(6, selectConfigPath.lastIndexOf('.'))), filePath);
//							Toast.makeText(MainActivity.this, Integer.parseInt(selectConfigPath.substring(6, selectConfigPath.lastIndexOf('.')))+"", Toast.LENGTH_SHORT).show();
							Config config = Config.bytesToConfig(Config.readConfig(selectConfigPath));
							config.bitmap = Config.fomateBitmapBytes(imageFilePath);
							Config.saveConfig(config, selectConfigPath);
							if(selectConfigPath.equals("config"+MainView.roomNum+".vanst")){
								progressDialog = new ProgressDialog(MainActivity.this);
								progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								progressDialog.setMessage("正在加载，请稍后...");
								progressDialog.setCancelable(false);
								progressDialog.show();
								new Thread(){
									public void run() {
										mainView.refreshThread.isRun = false;
										Config.askConfig(Config.readConfig(MainView.roomNum), mainView);
										mainView.repaintRoom();
										mainView.refreshThread.isRun = true;
										if(Connect.flag)Connect.getPose((byte)0);
										try {
											sleep(300);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										handler.sendEmptyMessage(PROGRESS_CANCEL);
									};
								}.start();
							}
						}
					});
					builder.setNegativeButton("取消", null);
					builder.show();
				}
			}
			break;
		default:
			break;
		}
    	super.onActivityResult(requestCode, resultCode, data);
    }
    static public String[] sortNames(String[] names){ 
    	String[] rtnNames = null;
    	if(names !=null){
    		rtnNames = new String[names.length];
    		int j = 0;
    		for(int i = 1;i < 100;i++){
    			for(String string:names){
    				if(Integer.parseInt(string.substring(0, string.length()-6)) == i){
    					rtnNames[j] = string;
    					j++;
    					break;
    				}
    			}
    		}
    	}
    	return rtnNames;
    }
    
    private boolean checkPhoneNumber(String phoneNumber){
		if(phoneNumber == null || phoneNumber.length() == 0) return false;
		if(phoneNumber.charAt(0) != '1') return false;
		char a[] = phoneNumber.toCharArray();
		if(phoneNumber.length() !=11){
			return false;
		}
		for(char c:a){
			if(c<'0'||c>'9'){
				return false;
			}
		}
		return true;
	}
    public void sound(){
    	SharedPreferences pref=getSharedPreferences("config", MODE_PRIVATE);
    	final SharedPreferences.Editor editor=pref.edit();
    	LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this); 
        View myLoginView = layoutInflater.inflate(R.layout.sound, null); 
        CheckBox sound1 = (CheckBox)myLoginView.findViewById(R.id.sound1);
        CheckBox sound2 = (CheckBox)myLoginView.findViewById(R.id.sound2);
        SeekBar seekBar = (SeekBar)myLoginView.findViewById(R.id.sound_sound);
        sound1.setChecked(MusicDo.sound1);
        sound2.setChecked(MusicDo.sound2);
        final AudioManager audiomanage;
        audiomanage = (AudioManager)getSystemService(Context.AUDIO_SERVICE);   
        final int maxVolume = audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC); 
        seekBar.setMax(maxVolume);
        currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值  
        seekBar.setProgress(currentVolume);    
        
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				musicDo.musicTest(MainActivity.this);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);    
                currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值  
                seekBar.setProgress(currentVolume);   
                
			}
		});
        
       
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("设置声音");
		builder.setIcon(R.drawable.bt_sound);
		builder.setView(myLoginView);
		sound1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				MusicDo.sound1 = isChecked;
				editor.putBoolean("sound1", isChecked);
				editor.commit();
			}
		});
		sound2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				MusicDo.sound2 = isChecked;
				editor.putBoolean("sound2", isChecked);
				editor.commit();
			}
		});
		builder.setNegativeButton("返回", null);
		builder.show();
    }
    
   public void bind(){
   	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
   	builder.setTitle("绑定/解除绑定精灵");
   	final EditText editText = new EditText(MainActivity.this);
   	builder.setView(editText);
   	builder.setPositiveButton("绑定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				byte [] bs = checkbyte(editText.getText().toString());
				if(bs != null){
					Connect.b1 = bs[0];
					Connect.b2 = bs[1];
					Connect.writeSelect(new byte[]{(byte)0xfe,bs[0],bs[1],'S','S','S'});
				}else{
					Toast.makeText(MainActivity.this, "输入有误", 500).show();
				}
			}
		});
   	builder.setNeutralButton("解除绑定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				byte [] bs = checkbyte(editText.getText().toString());
				if(bs != null){
					Connect.b1 = bs[0];
					Connect.b2 = bs[1];
					Connect.writeSelect(new byte[]{(byte)0xfe,bs[0],bs[1],'Q','Q','Q'});
				}else{
					Toast.makeText(MainActivity.this, "输入有误", 500).show();
				}
			}
		});
   	builder.setNegativeButton("返回", null);
   	builder.show();
   }
   
   public byte[] checkbyte(String ip){
	   char[] cs = ip.toCharArray();
	   byte[] bs = new byte[2];
	   if(ip == null || ip.length() != 4){
		   return null;
	   }else{
		   int count = 0;
		   for(char c:cs){
			   if(c>= '0'&& c<='9'||c>='a'&&c<='f'||c>='A'&& c<='F' ){
				   count++;
			   }
		   }
		   if(count != 4){
			   return null;
		   }else{
			   int [] is = new int [4];
			   int i = 0;
			   for(char c:cs){
				   if(c>= '0'&& c<='9'){
					   is[i] = c-'0';
				   }else if(c>='a'&&c<='f'){
					   is[i] = c-'a'+10;
				   }else if(c>='A'&& c<='F' ){
					   is[i] = c-'A'+10;
				   }
				   i++;
			   }
			   bs[0] = (byte) (is[0]*16+is[1]);
			   bs[1] = (byte) (is[2]*16+is[3]);
			   return bs;
		   }
	   }
   }
   
   
    public void changeWifi(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    	builder.setTitle("请选择要连接的SSID");
    	final WifiManager mWifiManager=(WifiManager) getSystemService(Context.WIFI_SERVICE);  
        //取得WifiInfo对象  
        String[] wifiNames = new String[]{};
        //开始扫描网络  
        mWifiManager.startScan();  
        //得到扫描结果  
        final List<ScanResult> mWifiList = mWifiManager.getScanResults();  
        //得到配置好的网络连接  
        final List<WifiConfiguration> mWifiConfigurations = mWifiManager.getConfiguredNetworks();  
        if(mWifiList!=null && mWifiList.size() != 0){  
        	wifiNames = new String[mWifiList.size()];
        	MyLog.i("wificount", mWifiList.size()+"");
            for(int i=0;i<mWifiList.size();i++){  
                //得到扫描结果  
                ScanResult mScanResult = mWifiList.get(i); 
                wifiNames[i] = mScanResult.SSID;
//                MyLog.i("StringBuffer",mScanResult.SSID);
//                if(mScanResult !=null)
//                sb=sb.append(mScanResult.BSSID+"  ").append(mScanResult.SSID+"   ")  
//                .append(mScanResult.capabilities+"   ").append(mScanResult.frequency+"   ")  
//                .append(mScanResult.level);  
//                MyLog.i("StringBuffer", sb.toString());
            	
            }  
        } 
        builder.setItems(wifiNames, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
//				mWifiManager.enableNetwork(netId, disableOthers)
				 if(mWifiConfigurations!=null && mWifiConfigurations.size() != 0){
					 for(WifiConfiguration wifiConfiguration:mWifiConfigurations){
						 if( wifiConfiguration.SSID.equals("\""+mWifiList.get(which).SSID+"\"")){
//							 Toast.makeText(MainActivity.this, "连接："+ wifiConfiguration.SSID, Toast.LENGTH_SHORT).show();
								mWifiManager.enableNetwork(wifiConfiguration.networkId, 
						                true); 
						 }
					 }
				 }
				
			}
		});
        builder.show();
        
    };
}