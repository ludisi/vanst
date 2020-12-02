package com.vanst.client;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DiyIRActivity extends Activity {
	static byte type;
	static byte index;
	Bitmap rPhoto;
	static boolean error;
	boolean isChange = true;
	boolean ismove;
	int keyid = 0;
	boolean you;
	static boolean isfull;
	boolean isold;
	boolean islearn;
	static boolean learnRefresh;
	static int learnId = 0;
	final String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/diy.jpg";
	final static int CAMERA = 0xCA;
	final static int PHOTO = 0xCB;
	final static int OLD = 0xCC;
	static FrameLayout frameLayout;
	RefreshThread refreshThread;
	int touchId;
	int bitmapWidth;
	int bitmapheight;
	int imageviewWidth;
	int imageviewHeigth;
	int srceenWidth;
	int srceenHeight;
	int windowsWidth;
	int windowsHeight;
	int x1;
	int y1;
	int x2;
	int y2;
	int movex;
	int movey;
	TextView remark;
	Bitmap diyIrBackground;
	byte[] backgroundBytes;
	DiyIRControl diyIRControl;
	Map<String, Object> keyMap;
	ArrayList<Map<String, Object>> keyList = new ArrayList<Map<String,Object>>();
	static ArrayList<Map<String, Object>> learnList = new ArrayList<Map<String,Object>>();
	Button save;
	Button design;
	ImageView kuang;
	ImageView background;
	ImageView imageKey;
	ImageView yaogan1;
	ImageView yaogan2;
	ImageView left;
	ImageView right;
	ImageView up;
	ImageView down;
	ImageView delete;
	ImageView deleteV;
	boolean deleteYou;
	static boolean isAuto;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0x01){
				imageviewWidth = background.getWidth();
				imageviewHeigth = background.getHeight();
				srceenWidth = frameLayout.getWidth();
				srceenHeight = frameLayout.getHeight();
				int buttonRect = windowsWidth / 30;
				if(imageviewWidth != 0 && imageviewHeigth != 0 && bitmapWidth != 0){
					int desX = (srceenWidth - imageviewWidth)/2;
					int desY = (srceenHeight - bitmapheight *  imageviewWidth / bitmapWidth)/2;
					if(isfull){
						x1 = 0;
						y1 = 0;
						x2 = imageviewWidth;
						y2 = bitmapheight * imageviewWidth / bitmapWidth;
					}
					if(learnRefresh){
						learnRefresh = false;
						if(isAuto){
							for(Map<String, Object> map:learnList){
								if((Integer)map.get("key") == learnId){
									((ImageView)map.get("view")).setBackgroundResource(R.drawable.kuang);
									Connect.IRControl(new byte[]{(byte)0xf0,(byte)type,(byte)index,0x03,(byte)((int)(Integer)map.get("key")),0x00}, DiyIRActivity.this);
								}else{
									((ImageView)map.get("view")).setBackgroundResource(R.drawable.kuang2);
								}
							}
						}else{
							for(Map<String, Object> map:learnList){
								((ImageView)map.get("view")).setBackgroundResource(R.drawable.kuang2);
							}
						}
					}
					yaogan1.layout(x1 -buttonRect + desX, y1+ desY - buttonRect,
							x1+buttonRect + desX, y1+ desY +buttonRect);
					yaogan2.layout(x2 -buttonRect + desX, y2+ desY - buttonRect,
							x2+buttonRect + desX, y2+ desY +buttonRect);
					left.layout(x1 + desX, y1 + desY, x1+2 + desX, y2+ desY);
					right.layout(x2-2 + desX, y1+ desY, x2 + desX, y2+ desY);
					up.layout(x1 + desX,y1+ desY,x2 + desX,y1+ 2 + desY);
					down.layout(x1 + desX,y2+ desY -2,x2 + desX,y2+ desY);
					try {
						if(deleteV.getRight() != 0){
						delete.layout((deleteV.getLeft() + deleteV.getRight())/2 - buttonRect,(deleteV.getTop() + deleteV.getBottom())/2 + deleteV.getHeight() - buttonRect ,
								(deleteV.getLeft() + deleteV.getRight())/2 + buttonRect, (deleteV.getTop() + deleteV.getBottom())/2 + deleteV.getHeight() + buttonRect);
						}
					} catch (Exception e) {
					}
					for(Map<String, Object> map:keyList){
						if((Boolean)map.get("get")){
							((ImageView)map.get("view")).layout((Integer)map.get("x")-(buttonRect) + desX, (Integer)map.get("y")-(buttonRect) + desY, 
								(Integer)map.get("x")+(buttonRect) + desX, (Integer)map.get("y")+(buttonRect)+desY);
						}else {
							((ImageView)map.get("view")).layout(0,0,0,0);
						}
					}
				}
			}else if(msg.what == 0x02){
				if(diyIrBackground != null && imageviewWidth > 0 && imageviewHeigth > 0 && bitmapWidth > 0 && srceenWidth > 0){
					x1 = diyIRControl.backgroudrect[0] * imageviewWidth / bitmapWidth;
					y1 = diyIRControl.backgroudrect[1] * imageviewWidth / bitmapWidth;
					x2 = diyIRControl.backgroudrect[2] * imageviewWidth / bitmapWidth;
					y2 = diyIRControl.backgroudrect[3] * imageviewWidth / bitmapWidth;
					
					keyList = new ArrayList<Map<String,Object>>();
					for(int[] is:diyIRControl.instructions){
						imageKey = new ImageView(DiyIRActivity.this);
						imageKey.setBackgroundResource(R.drawable.kuang);
						FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(0,0);
						frameLayout.addView(imageKey,layoutParams);
						keyMap = new HashMap<String, Object>();
						keyMap.put("view", imageKey);
						keyMap.put("x", (is[0] + is[2])/2 * imageviewWidth / bitmapWidth);
						keyMap.put("y", (is[1] + is[3])/2 * imageviewWidth / bitmapWidth);
						keyMap.put("get", true);
						keyList.add(keyMap);
						imageKey.setOnTouchListener(new View.OnTouchListener() {
							public boolean onTouch(View v, MotionEvent event) {
								if(!islearn){
									
								
								if(event.getAction() == MotionEvent.ACTION_DOWN){
									movex = (int) event.getX();
									movey = (int) event.getY();
									v.setBackgroundResource(R.drawable.kuang2);
								}
								else if(event.getAction() == MotionEvent.ACTION_MOVE){
									ismove = true;
									int desX = (srceenWidth - imageviewWidth)/2;
									int desY = (srceenHeight - bitmapheight *  imageviewWidth / bitmapWidth)/2;
									for(Map<String, Object> map:keyList){
										if((ImageView)map.get("view") == v){
											int touchX = (int)event.getX() - movex + (Integer)map.get("x");
											int touchY = (int)event.getY() - movey + (Integer)map.get("y");
											int buttonRect = windowsWidth/30;
											if(touchX < x1 + buttonRect) touchX = x1 + buttonRect;
											if(touchX > x2 - buttonRect) touchX = x2 - buttonRect;
											if(touchY < y1 + buttonRect) touchY = y1 + buttonRect;
											if(touchY > y2 - buttonRect) touchY = y2 - buttonRect;
											boolean cross = false;
											for(Map<String, Object>map1 : keyList){
												if((ImageView)map1.get("view") != v &&(Boolean)map1.get("get") && rectCross((int)touchX - buttonRect, (int)touchX + buttonRect, (int)touchY - buttonRect, (int)touchY + buttonRect, 
														(Integer)map1.get("x") - buttonRect, (Integer)map1.get("x") + buttonRect, (Integer)map1.get("y") - buttonRect, (Integer)map1.get("y") + buttonRect)){
													cross  = true;
												}
											}
											if(!cross)
												kuang.layout((int)touchX - buttonRect + desX, (int)touchY - buttonRect + desY, 
													(int)touchX + buttonRect + desX, (int)touchY + buttonRect + desY);
											else
												kuang.layout(0, 0, 0, 0);
										}
									}
								}
								else if(event.getAction() == MotionEvent.ACTION_UP){
									ismove = false;
									v.setBackgroundResource(R.drawable.kuang);
									for(Map<String, Object> map:keyList){
										if((ImageView)map.get("view") == v){
											int touchX = (int)event.getX() - movex + (Integer)map.get("x");
											int touchY = (int)event.getY() - movey + (Integer)map.get("y");
											int buttonRect = windowsWidth/30;
											if(touchX < x1 + buttonRect) touchX = x1 + buttonRect;
											if(touchX > x2 - buttonRect) touchX = x2 - buttonRect;
											if(touchY < y1 + buttonRect) touchY = y1 + buttonRect;
											if(touchY > y2 - buttonRect) touchY = y2 - buttonRect;
											boolean cross = false;
											for(Map<String, Object>map1 : keyList){
												if((ImageView)map1.get("view") != v &&(Boolean)map1.get("get") && rectCross((int)touchX - buttonRect, (int)touchX + buttonRect, (int)touchY - buttonRect, (int)touchY + buttonRect, 
														(Integer)map1.get("x") - buttonRect, (Integer)map1.get("x") + buttonRect, (Integer)map1.get("y") - buttonRect, (Integer)map1.get("y") + buttonRect)){
													cross = true;
												}
											}
											if(!cross){
												isChange = true;
												map.put("x", touchX);
												map.put("y", touchY);
											}
											
										}
									}
									kuang.layout(0, 0, 0, 0);
								}
								}
								return false;
								
							}
						});
						imageKey.setOnClickListener(new View.OnClickListener() {
							public void onClick(final View v) {
								if(islearn){
									for(Map<String, Object> map:learnList){
										if((ImageView)map.get("view") == v){
											((ImageView)map.get("view")).setBackgroundResource(R.drawable.kuang);
											Connect.IRControl(new byte[]{(byte)0xf0,(byte)type,(byte)index,0x03,(byte)((int)(Integer)map.get("key")),0x00}, DiyIRActivity.this);
										}else{
											((ImageView)map.get("view")).setBackgroundResource(R.drawable.kuang2);
										}
									}
								}else{
								}
							}
						});
						imageKey.setOnLongClickListener(new View.OnLongClickListener() {
							public boolean onLongClick(final View v) {
								if(!islearn){
									delete  = new ImageView(DiyIRActivity.this);
									delete.setBackgroundResource(R.drawable.delete);
									FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(0,0);
									frameLayout.addView(delete,layoutParams);
									deleteV = (ImageView) v;
									
									delete.setOnClickListener(new View.OnClickListener() {
										public void onClick(View v2) {
											isChange = true;
											frameLayout.removeView(v);
											frameLayout.removeView(v2);
											for(Map<String, Object> map:keyList){
												if((ImageView)map.get("view") == v){
													map.put("get", false);
												}
											}
										}
									});
									deleteYou = true;
								}
								return false;
							}
						});
					}
				}
			}else if(msg.what == 0x03){
//				try {
					BitmapFactory.decodeFile(imageFilePath,Room.optReadBound);
					if(Room.optReadBound.outHeight > 1080 && Room.optReadBound.outWidth > 1080){
						if(Room.optReadBound.outHeight > Room.optReadBound.outWidth){
							Room.optReadBitmap.inSampleSize = 1+Room.optReadBound.outWidth / 1080;
						}else{
							Room.optReadBitmap.inSampleSize = 1+Room.optReadBound.outHeight / 1080;
						}
					}else{
						Room.optReadBitmap.inSampleSize = 1;
					}
//					Toast.makeText(DiyIRActivity.this, "x:"+Room.optReadBound.outWidth+"y:"+Room.optReadBound.outHeight,Toast.LENGTH_SHORT).show();
					rPhoto = BitmapFactory.decodeFile(imageFilePath,Room.optReadBitmap);
					if(rPhoto != null){
						Log.e("cameraerro", "fw"+frameLayout.getWidth()+"fh"+frameLayout.getHeight()+"rw"+rPhoto.getWidth()+"rh"+rPhoto.getHeight());
						if(rPhoto.getHeight() > frameLayout.getHeight() || rPhoto.getWidth() > frameLayout.getHeight()){
							if(rPhoto.getWidth() > rPhoto.getHeight()){
								rPhoto = Bitmap.createScaledBitmap(rPhoto, frameLayout.getHeight(), frameLayout.getHeight() * rPhoto.getHeight() / rPhoto.getWidth(), true);
							}else{
								rPhoto = Bitmap.createScaledBitmap(rPhoto, frameLayout.getHeight() * rPhoto.getWidth()/rPhoto.getHeight(), frameLayout.getHeight(), true);
							}
								
						}
					
//						Toast.makeText(DiyIRActivity.this, rPhoto.getWidth() + " " + rPhoto.getHeight(), Toast.LENGTH_SHORT).show();
						final ImageView imageView = new ImageView(DiyIRActivity.this);
						imageView.setBackgroundDrawable(new BitmapDrawable(rPhoto));
						AlertDialog.Builder builder = new AlertDialog.Builder(DiyIRActivity.this);
						builder.setView(imageView);
						builder.setTitle("是否要使用此图片？");
						builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								bitmapWidth = rPhoto.getWidth();
								bitmapheight = rPhoto.getHeight();
								background.setImageBitmap(rPhoto);
	//							background.setBackgroundDrawable(new BitmapDrawable(photo));
								diyIrBackground = rPhoto;
								restart();
							}
						});
						builder.show();
					}
//					} catch (Exception e) {
////						cameraError();
//					} catch (OutOfMemoryError e) {
////						cameraError();
//					}
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		new Thread(){
//			public void run() {
//				for(Device device :MainView.devices){
//					if(device.type == Device.DEVICE_IR){
//						Connect.writeSelect(new byte[]{(byte)0xf0,(byte)device.type,(byte)device.index,0x01,0x00,0x00});
//						try {
//							sleep(500);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			};
//		}.start();
		setContentView(R.layout.diyir);
		WindowManager wManager=getWindowManager();
		Display display=wManager.getDefaultDisplay();
		windowsHeight=display.getHeight();
		windowsWidth=display.getWidth();
		frameLayout = (FrameLayout)findViewById(R.id.diyir_frame);
		save = (Button)findViewById(R.id.diyir_save);
		design = (Button)findViewById(R.id.diyir_design);
		background = (ImageView)findViewById(R.id.diyir_background);
		yaogan1 = (ImageView)findViewById(R.id.diyir_yaogan1);
		yaogan2 = (ImageView)findViewById(R.id.diyir_yaogan2);
		left = (ImageView)findViewById(R.id.diyir_left);
		right = (ImageView)findViewById(R.id.diyir_right);
		up = (ImageView)findViewById(R.id.diyir_up);
		down = (ImageView)findViewById(R.id.diyir_donw);
		left = (ImageView)findViewById(R.id.diyir_left);
		right = (ImageView)findViewById(R.id.diyir_right);
		kuang = (ImageView)findViewById(R.id.diyir_kuang);
		remark = (TextView)findViewById(R.id.diyir_remark);
		Intent intent = getIntent();
		if(intent != null){
			type = intent.getByteExtra("type", (byte)0x00);
			index = intent.getByteExtra("index", (byte)0x00);
			you = intent.getBooleanExtra("you", false);
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(DiyIRActivity.this);
		builder.setTitle("是否要重新选取背景图");
		builder.setPositiveButton("照相拍取", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imageFilePath)));
				startActivityForResult(intent, CAMERA);
			}
		});
		builder.setNeutralButton("相册选取", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent=new Intent("android.intent.action.proPhoto");
				startActivityForResult(intent, PHOTO);
			}
		});
		if(you){
			builder.setNegativeButton("沿用原先", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					isChange = false;
					diyIRControl = DiyIRControl.load(index);
					Room.optReadBitmap.inSampleSize = 1;
					backgroundBytes = diyIRControl.backgroud;
					diyIrBackground = BitmapFactory.decodeByteArray(diyIRControl.backgroud, 0, diyIRControl.backgroud.length, Room.optReadBitmap);
					bitmapWidth = diyIrBackground.getWidth();
					bitmapheight = diyIrBackground.getHeight();
					remark.setText(diyIRControl.remark);
					background.setImageBitmap(diyIrBackground);
//					isold = true;
					new Thread(){
						public void run() {
							try {
								sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							handler.sendEmptyMessage(0x02);
						}
					}.start();
				}
			});
		}
		if(!error){
			builder.show();
		}else{
			error = false;
			new Thread(){
				public void run() {
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handler.sendEmptyMessage(0x03);
					super.run();
				}
			}.start();
			
			
		}
		
		
		design.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(!islearn){
					AlertDialog.Builder builder = new AlertDialog.Builder(DiyIRActivity.this);
					builder.setTitle("提醒：更改图片会让所以已经添加的控件重置！");
					String [] strings = new String[]{
						"拍照选取",
						"照片选取",
						"图片旋转",
						"修改名称"
					};
					builder.setItems(strings, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, final int which) {
							if(which == 0){
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imageFilePath)));
								startActivityForResult(intent, CAMERA);
							}else if(which == 1){
								Intent intent=new Intent("android.intent.action.proPhoto");
								startActivityForResult(intent, PHOTO);
							}else if(which == 2){
								diyIrBackground = ratate(diyIrBackground);
								background.setImageBitmap(diyIrBackground);
								restart();
							}else if(which == 3){
								final String [] strings = new String[]{
										"电视机",
										"机顶盒",
										"DVD",
										"音响",
										"窗帘",
										"其他"
								};
								AlertDialog.Builder builder = new AlertDialog.Builder(DiyIRActivity.this);
								builder.setTitle("添加名称：");
								builder.setItems(strings, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										if(which == strings.length -1){
											AlertDialog.Builder builder = new AlertDialog.Builder(DiyIRActivity.this);
											builder.setTitle("输入自定义名称");
											final EditText editText = new EditText(DiyIRActivity.this);
											builder.setView(editText);
											builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int which) {
													remark.setText(editText.getText().toString());
												}
											});
											builder.show();
										}else{
											remark.setText(strings[which]);
										}
									}
								});
								builder.setPositiveButton("返回", null);
								builder.show();
								
							}
						}
					});
					builder.show();
				}else{
					design.setTextColor(Color.RED);
					save.setTextColor(Color.BLACK);
					isAuto = false;
				}
			}
		});
		
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(!islearn){
					AlertDialog.Builder builder = new AlertDialog.Builder(DiyIRActivity.this);
					builder.setTitle("保存会覆盖原有的自定义按键");
					builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							int instructionCount = 0;
							for(Map<String, Object>map:keyList){
								if((Boolean)map.get("get")){
									instructionCount++;
								}
							}
							if(diyIrBackground == null || instructionCount == 0){
								if(diyIrBackground == null){
									Toast.makeText(DiyIRActivity.this, "请先定义背景图", Toast.LENGTH_SHORT).show();
								}else{
									Toast.makeText(DiyIRActivity.this, "请添加按钮", Toast.LENGTH_SHORT).show();
								}
								return;
							}
							int [][] instructions = new int[instructionCount][4];
							int i = 0;
							for(Map<String, Object>map:keyList){
								if((Boolean)map.get("get")){
									instructions[i][0] = ((Integer) map.get("x") - windowsWidth/20) * diyIrBackground.getWidth() / background.getWidth();
									instructions[i][1] = ((Integer) map.get("y") - windowsWidth/20) * diyIrBackground.getWidth() / background.getWidth();
									instructions[i][2] = ((Integer) map.get("x") + windowsWidth/20) * diyIrBackground.getWidth() / background.getWidth();
									instructions[i][3] = ((Integer) map.get("y") + windowsWidth/20) * diyIrBackground.getWidth() / background.getWidth();
									i++;
									Log.i("xy","x:"+(Integer) map.get("x") +" y:" +(Integer) map.get("y"));
								}
							}
							byte[] backgroud = backgroundBytes;
							int [] backgroudrect = new int[]{
									x1 * diyIrBackground.getWidth() / background.getWidth(),
									y1 * diyIrBackground.getWidth() / background.getWidth(),
									x2 * diyIrBackground.getWidth() / background.getWidth(),
									y2 * diyIrBackground.getWidth() / background.getWidth()
							};
							if(isChange){
								diyIRControl = new DiyIRControl(instructions, backgroud, backgroudrect,remark.getText().toString());
								DiyIRControl.save(index, diyIRControl);
							}
							int IRCount = 0;
							for(Device device:MainView.devices){
								if(device.visible && device.type == Device.DEVICE_IR){
									IRCount++;
								}
							};
							if(IRCount != 0 && Connect.flag){
								String[]IRStrings = new String[IRCount];
								final int[] IRnums = new int[IRCount];
								int ii  = 0;
								for(Device device:MainView.devices){
									if(device.visible && device.type == Device.DEVICE_IR){
										IRnums[ii] = device.index;
										IRStrings[ii] = "红外精灵"+device.index;
										ii++;
									}
								};
								AlertDialog.Builder builder = new AlertDialog.Builder(DiyIRActivity.this);
								builder.setTitle("请选择红外精灵");
								builder.setCancelable(false);
								builder.setItems(IRStrings, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										Connect.writeSelect(new byte[]{(byte)0xf0,(byte)Device.DEVICE_IR,(byte)IRnums[which],0x01,0x00,0x00});
										AlertDialog.Builder builder = new AlertDialog.Builder(DiyIRActivity.this);
										builder.setTitle("请选择学习方式");
										builder.setNegativeButton("自动跳转", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												// TODO Auto-generated method stub
												islearn = true;
												design.setText("手动选择");
												save.setText("自动跳转");
												save.setTextColor(Color.RED);
//												design.setEnabled(false);
//												save.setEnabled(false);
												int key = 0;
												learnList = new ArrayList<Map<String,Object>>();
												for(Map<String,Object>map:keyList){
													if((Boolean)map.get("get")){
														map.put("key",key );
														learnList.add(map);
														key++;
													}
												}
												isAuto = true;
												learnId = 0;
												learnSingle(-1);
											}
										});
										builder.setPositiveButton("手动选择", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												islearn = true;
												design.setText("手动选择");
												save.setText("自动跳转");
												design.setTextColor(Color.RED);
//												design.setEnabled(false);
//												save.setEnabled(false);
												int key = 0;
												learnList = new ArrayList<Map<String,Object>>();
												for(Map<String,Object>map:keyList){
													if((Boolean)map.get("get")){
														map.put("key",key );
														learnList.add(map);
														key++;
													}
												}
												isAuto = false;
												learnId = 0;
												learnRefresh = true;
											}
										});
										builder.setCancelable(false);
										builder.show();
									}
								});
								builder.show();
							}else{
								design.setEnabled(false);
								save.setEnabled(false);
								AlertDialog.Builder builder= new AlertDialog.Builder(DiyIRActivity.this);
								if(Connect.flag)builder.setTitle("配置内没有红外精灵请先配置红外精灵");
								else builder.setTitle("服务器未连接");
								builder.setCancelable(false);
								builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										finish();
									}
								});
								builder.show();
							}
							
	//						new Thread(){
	//							public void run() {
	//								Connect.IRControl(new byte[]{(byte)0xf0,(byte)type,(byte)index,0x03,0x00,0x00},DiyIRActivity.this);
	//							};
	//						}.start();
						}
					});
					builder.setNegativeButton("取消", null);
					builder.show();
				}else{
					design.setTextColor(Color.BLACK);
					save.setTextColor(Color.RED);
					isAuto = true;
				}
			}
		});
		
		frameLayout.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if(!islearn){
				imageviewWidth = background.getWidth();
				imageviewHeigth = background.getHeight();
				srceenWidth = frameLayout.getWidth();
				srceenHeight = frameLayout.getHeight();
				int buttonRect = windowsWidth / 30;
				int desX = (srceenWidth - imageviewWidth)/2;
				int desY = (srceenHeight - bitmapheight *  imageviewWidth / bitmapWidth)/2;
				float touchX = event.getX() - desX;
				float touchY = event.getY() - desY;
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					if(deleteYou){
						try {
							frameLayout.removeView(delete);
							deleteYou = false;
						} catch (Exception e) {
						}
						
						
					}else{
					
					
					if(touchX - x1 < 40 && x1 - touchX < 40 && touchY - y1 < 40 && y1 - touchY < 40){
						touchId = 1;
						isfull = false;
					}else if(touchX - x2 < 40 && x2 - touchX < 40 && touchY - y2 < 40 && y2 - touchY < 40){
						touchId = 2;
						isfull = false;
					}else if(touchX > x1 + buttonRect && touchX < x2 - buttonRect && touchY > y1 + buttonRect && touchY < y2 - buttonRect){
						touchId = 3;
						boolean cross = false;
						for(Map<String, Object>map : keyList){
							if((Boolean)map.get("get") && rectCross((int)touchX - buttonRect, (int)touchX + buttonRect, (int)touchY - buttonRect, (int)touchY + buttonRect, 
									(Integer)map.get("x") - buttonRect, (Integer)map.get("x") + buttonRect, (Integer)map.get("y") - buttonRect, (Integer)map.get("y") + buttonRect)){
								cross  = true;
							}
						}
						if(!cross)
						kuang.layout((int)touchX - buttonRect + desX, (int)touchY - buttonRect + desY, 
								(int)touchX + buttonRect + desX, (int)touchY + buttonRect + desY);
					}
					}
				}else if(event.getAction() == MotionEvent.ACTION_MOVE){
					if(touchId == 1){
						if(touchX < 0){
							touchX = 0;
						}
						if(touchX + 20 > x2 ){
							touchX = x2 - 20;
						}
						if(touchY < 0){
							touchY = 0;
						}
						if(touchY + 20 > y2){
							touchY = y2 - 20;
						}
						int minx = x2;
						int miny = y2;
						for(Map<String, Object> map :keyList){
							if(minx > (Integer)map.get("x") && (Boolean)map.get("get")){
								minx = (Integer)map.get("x");
							}
							if(miny > (Integer)map.get("y") && (Boolean)map.get("get")){
								miny = (Integer)map.get("y");
							}
						}
						if(touchX + buttonRect > minx){
							touchX = minx - buttonRect;
						}
						if(touchY + buttonRect > miny){
							touchY = miny - buttonRect;
						}
						x1 = (int) touchX;
						y1 = (int) touchY;
						isChange = true;
					}else if(touchId == 2){
						if(touchX > imageviewWidth){
							touchX = imageviewWidth;
						}
						if(touchX - 20 < x1 ){
							touchX = x1 + 20;
						}
						if(touchY > bitmapheight * imageviewWidth / bitmapWidth){
							touchY =  bitmapheight * imageviewWidth / bitmapWidth;
						}
						if(touchY - 20 < y1){
							touchY = y1 + 20;
						}
						int maxx = x1;
						int maxy = y1;
						for(Map<String, Object> map :keyList){
							if(maxx < (Integer)map.get("x") && (Boolean)map.get("get")){
								maxx = (Integer)map.get("x");
							}
							if(maxy < (Integer)map.get("y") && (Boolean)map.get("get")){
								maxy = (Integer)map.get("y");
							}
						}
						if(touchX - buttonRect < maxx){
							touchX = maxx + buttonRect;
						}
						if(touchY - buttonRect < maxy){
							touchY = maxy + buttonRect;
						}
						x2 = (int) touchX;
						y2 = (int) touchY;
						isChange = true;
					}else if (touchId == 3){
						if(touchX < x1 + buttonRect) touchX = x1 + buttonRect;
						if(touchX > x2 - buttonRect) touchX = x2 - buttonRect;
						if(touchY < y1 + buttonRect) touchY = y1 + buttonRect;
						if(touchY > y2 - buttonRect) touchY = y2 - buttonRect;
						boolean cross = false;
						for(Map<String, Object>map : keyList){
							if((Boolean)map.get("get") && rectCross((int)touchX - buttonRect, (int)touchX + buttonRect, (int)touchY - buttonRect, (int)touchY + buttonRect, 
									(Integer)map.get("x") - buttonRect, (Integer)map.get("x") + buttonRect, (Integer)map.get("y") - buttonRect, (Integer)map.get("y") + buttonRect)){
								cross  = true;
							}
						}
						if(!cross)
							kuang.layout((int)touchX - buttonRect + desX, (int)touchY - buttonRect + desY, 
								(int)touchX + buttonRect + desX, (int)touchY + buttonRect + desY);
						else
							kuang.layout(0, 0, 0, 0);
					}
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					if(touchId == 3){
						if(touchX < x1 + buttonRect) touchX = x1 + buttonRect;
						if(touchX > x2 - buttonRect) touchX = x2 - buttonRect;
						if(touchY < y1 + buttonRect) touchY = y1 + buttonRect;
						if(touchY > y2 - buttonRect) touchY = y2 - buttonRect;
						boolean cross = false;
						for(Map<String, Object>map : keyList){
							if((Boolean)map.get("get") && rectCross((int)touchX - buttonRect, (int)touchX + buttonRect, (int)touchY - buttonRect, (int)touchY + buttonRect, 
									(Integer)map.get("x") - buttonRect, (Integer)map.get("x") + buttonRect, (Integer)map.get("y") - buttonRect, (Integer)map.get("y") + buttonRect)){
								cross = true;
							}
						}
						if(!cross){
							isChange = true;
						imageKey = new ImageView(DiyIRActivity.this);
						imageKey.setBackgroundResource(R.drawable.kuang);
						FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(0,0);
						frameLayout.addView(imageKey,layoutParams);
						keyMap = new HashMap<String, Object>();
						keyMap.put("view", imageKey);
						keyMap.put("x", (int)touchX);
						keyMap.put("y", (int)touchY);
						keyMap.put("get", true);
						keyList.add(keyMap);
						imageKey.setOnTouchListener(new View.OnTouchListener() {
							public boolean onTouch(View v, MotionEvent event) {
								if(!islearn){
									
								
								if(event.getAction() == MotionEvent.ACTION_DOWN){
									movex = (int) event.getX();
									movey = (int) event.getY();
									v.setBackgroundResource(R.drawable.kuang2);
								}
								else if(event.getAction() == MotionEvent.ACTION_MOVE){
									ismove = true;
									int desX = (srceenWidth - imageviewWidth)/2;
									int desY = (srceenHeight - bitmapheight *  imageviewWidth / bitmapWidth)/2;
									for(Map<String, Object> map:keyList){
										if((ImageView)map.get("view") == v){
											int touchX = (int)event.getX() - movex + (Integer)map.get("x");
											int touchY = (int)event.getY() - movey + (Integer)map.get("y");
											int buttonRect = windowsWidth/30;
											if(touchX < x1 + buttonRect) touchX = x1 + buttonRect;
											if(touchX > x2 - buttonRect) touchX = x2 - buttonRect;
											if(touchY < y1 + buttonRect) touchY = y1 + buttonRect;
											if(touchY > y2 - buttonRect) touchY = y2 - buttonRect;
											boolean cross = false;
											for(Map<String, Object>map1 : keyList){
												if((ImageView)map1.get("view") != v &&(Boolean)map1.get("get") && rectCross((int)touchX - buttonRect, (int)touchX + buttonRect, (int)touchY - buttonRect, (int)touchY + buttonRect, 
														(Integer)map1.get("x") - buttonRect, (Integer)map1.get("x") + buttonRect, (Integer)map1.get("y") - buttonRect, (Integer)map1.get("y") + buttonRect)){
													cross  = true;
												}
											}
											if(!cross)
												kuang.layout((int)touchX - buttonRect + desX, (int)touchY - buttonRect + desY, 
													(int)touchX + buttonRect + desX, (int)touchY + buttonRect + desY);
											else
												kuang.layout(0, 0, 0, 0);
										}
									}
								}
								else if(event.getAction() == MotionEvent.ACTION_UP){
									ismove = false;
									v.setBackgroundResource(R.drawable.kuang);
									for(Map<String, Object> map:keyList){
										if((ImageView)map.get("view") == v){
											int touchX = (int)event.getX() - movex + (Integer)map.get("x");
											int touchY = (int)event.getY() - movey + (Integer)map.get("y");
											int buttonRect = windowsWidth/30;
											if(touchX < x1 + buttonRect) touchX = x1 + buttonRect;
											if(touchX > x2 - buttonRect) touchX = x2 - buttonRect;
											if(touchY < y1 + buttonRect) touchY = y1 + buttonRect;
											if(touchY > y2 - buttonRect) touchY = y2 - buttonRect;
											boolean cross = false;
											for(Map<String, Object>map1 : keyList){
												if((ImageView)map1.get("view") != v &&(Boolean)map1.get("get") && rectCross((int)touchX - buttonRect, (int)touchX + buttonRect, (int)touchY - buttonRect, (int)touchY + buttonRect, 
														(Integer)map1.get("x") - buttonRect, (Integer)map1.get("x") + buttonRect, (Integer)map1.get("y") - buttonRect, (Integer)map1.get("y") + buttonRect)){
													cross = true;
												}
											}
											if(!cross){
												map.put("x", touchX);
												map.put("y", touchY);
												isChange = true;
											}
											
										}
									}
									kuang.layout(0, 0, 0, 0);
//									Toast.makeText(DiyIRActivity.this, "up", Toast.LENGTH_SHORT).show();
								}
								}
								return false;
								
							}
						});
						imageKey.setOnClickListener(new View.OnClickListener() {
							public void onClick(final View v) {
								if(islearn){
									for(Map<String, Object> map:learnList){
										if((ImageView)map.get("view") == v){
											((ImageView)map.get("view")).setBackgroundResource(R.drawable.kuang);
											Connect.IRControl(new byte[]{(byte)0xf0,(byte)type,(byte)index,0x03,(byte)((int)(Integer)map.get("key")),0x00}, DiyIRActivity.this);
										}else{
											((ImageView)map.get("view")).setBackgroundResource(R.drawable.kuang2);
										}
									}
								}else{
								}
							}
						});
						imageKey.setOnLongClickListener(new View.OnLongClickListener() {
							public boolean onLongClick(final View v) {
								if(!islearn){
									delete  = new ImageView(DiyIRActivity.this);
									delete.setBackgroundResource(R.drawable.delete);
									FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(0,0);
									frameLayout.addView(delete,layoutParams);
									deleteV = (ImageView) v;
									delete.setOnClickListener(new View.OnClickListener() {
										public void onClick(View v2) {
											isChange = true;
											frameLayout.removeView(v);
											deleteYou = false;
											frameLayout.removeView(v2);
											for(Map<String, Object> map:keyList){
												if((ImageView)map.get("view") == v){
													map.put("get", false);
												}
											}
										}
									});
									deleteYou = true;
//									AlertDialog.Builder builder = new AlertDialog.Builder(DiyIRActivity.this);
//									builder.setTitle("对此按钮操作");
//									builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
//										public void onClick(DialogInterface dialog, int which) {
//											frameLayout.removeView(v);
//											for(Map<String, Object> map:keyList){
//												if((ImageView)map.get("view") == v){
//													map.put("get", false);
//												}
//											}
//										}
//									});
//									builder.setNegativeButton("返回", null);
//									builder.show();
								}
								return false;
							}
						});
						
					}
					}
					touchId = 0;
				}
				}
				return true;
			}
		});
		refreshThread = new RefreshThread();
		refreshThread.start();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CAMERA:
			if(resultCode == RESULT_OK ){
				if(imageFilePath != null){
					try {
					BitmapFactory.decodeFile(imageFilePath,Room.optReadBound);
					if(Room.optReadBound.outHeight > 1080 && Room.optReadBound.outWidth > 1080){
						if(Room.optReadBound.outHeight > Room.optReadBound.outWidth){
							Room.optReadBitmap.inSampleSize = 1+Room.optReadBound.outWidth / 1080;
						}else{
							Room.optReadBitmap.inSampleSize = 1+Room.optReadBound.outHeight / 1080;
						}
					}else{
						Room.optReadBitmap.inSampleSize = 1;
					}
//					Toast.makeText(DiyIRActivity.this, "x:"+Room.optReadBound.outWidth+"y:"+Room.optReadBound.outHeight,Toast.LENGTH_SHORT).show();
					rPhoto = BitmapFactory.decodeFile(imageFilePath,Room.optReadBitmap);
					if(rPhoto != null){
						if(rPhoto.getHeight() > frameLayout.getHeight() || rPhoto.getWidth() > frameLayout.getHeight()){
							if(rPhoto.getWidth() > rPhoto.getHeight()){
								rPhoto = Bitmap.createScaledBitmap(rPhoto, frameLayout.getHeight(), frameLayout.getHeight() * rPhoto.getHeight() / rPhoto.getWidth(), true);
							}else{
								rPhoto = Bitmap.createScaledBitmap(rPhoto, frameLayout.getHeight() * rPhoto.getWidth()/rPhoto.getHeight(), frameLayout.getHeight(), true);
							}
								
						}
					
//						Toast.makeText(this, rPhoto.getWidth() + " " + rPhoto.getHeight(), Toast.LENGTH_SHORT).show();
						final ImageView imageView = new ImageView(this);
						imageView.setBackgroundDrawable(new BitmapDrawable(rPhoto));
						AlertDialog.Builder builder = new AlertDialog.Builder(this);
						builder.setView(imageView);
						builder.setTitle("是否要使用此图片？");
						builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								bitmapWidth = rPhoto.getWidth();
								bitmapheight = rPhoto.getHeight();
								background.setImageBitmap(rPhoto);
	//							background.setBackgroundDrawable(new BitmapDrawable(photo));
								diyIrBackground = rPhoto;
								restart();
							}
						});
						builder.show();
					}
					} catch (Exception e) {
						cameraError();
					} catch (OutOfMemoryError e) {
						cameraError();
					}
				}
			}
			break;
		case PHOTO:
			if(resultCode == RESULT_OK){
				final String filePath=data.getStringExtra("path");
				if(filePath!=null){
					BitmapFactory.decodeFile(filePath,Room.optReadBound);
					if(Room.optReadBound.outHeight > 1080 && Room.optReadBound.outWidth > 1080){
						if(Room.optReadBound.outHeight > Room.optReadBound.outWidth){
							Room.optReadBitmap.inSampleSize = 1+Room.optReadBound.outWidth / 1080;
						}else{
							Room.optReadBitmap.inSampleSize = 1+Room.optReadBound.outHeight / 1080;
						}
					}else{
						Room.optReadBitmap.inSampleSize = 1;
					}
					
					rPhoto = BitmapFactory.decodeFile(filePath,Room.optReadBitmap);
					if(rPhoto.getHeight() > frameLayout.getHeight() || rPhoto.getWidth() > frameLayout.getHeight()){
						if(rPhoto.getWidth() > rPhoto.getHeight()){
							rPhoto = Bitmap.createScaledBitmap(rPhoto, frameLayout.getHeight(), frameLayout.getHeight() * rPhoto.getHeight() / rPhoto.getWidth(), true);
						}else{
							rPhoto = Bitmap.createScaledBitmap(rPhoto, frameLayout.getHeight() * rPhoto.getWidth()/rPhoto.getHeight(), frameLayout.getHeight(), true);
						}
							
					}
					if(rPhoto != null){
//						Toast.makeText(this, rPhoto.getWidth() + " " + rPhoto.getHeight(), Toast.LENGTH_SHORT).show();
						final ImageView imageView = new ImageView(this);
						imageView.setBackgroundDrawable(new BitmapDrawable(rPhoto));
						AlertDialog.Builder builder = new AlertDialog.Builder(this);
						builder.setView(imageView);
						builder.setTitle("是否要使用此图片？");
						builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								bitmapWidth = rPhoto.getWidth();
								bitmapheight = rPhoto.getHeight();
								background.setImageBitmap(rPhoto);
	//							background.setBackgroundDrawable(new BitmapDrawable(photo));
								diyIrBackground = rPhoto;
								restart();
							}
						});
						builder.show();
					}
				}
			}
			break;
		default:
			break;
		}
	}
	
	public Bitmap ratate(Bitmap bitmap){
		if(bitmap != null){
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			int temp = bitmapheight;
			bitmapheight = bitmapWidth;
			bitmapWidth = temp;
			return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix	, true);
		}else{
			return null;
		}
	}
	protected void onResume() {
		if(refreshThread != null){
			refreshThread.rflag = true;
		}
		Connect.activity = this;
		super.onResume();
	}
	@Override
	protected void onPause() {
		if(refreshThread != null){
			refreshThread.rflag = false;
		}
		super.onPause();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(!islearn){
				AlertDialog.Builder builder = new AlertDialog.Builder(DiyIRActivity.this);
				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle("还没有保存是否要退出编辑？");
				builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						if(refreshThread != null){
							refreshThread.sflag = true;
						}
						onDestroy();
						finish();
					};
				});
				builder.setNegativeButton("返回", null);
				builder.show();
			}else{
				if(refreshThread != null){
					refreshThread.sflag = true;
				}
				onDestroy();
				Connect.rwPose = Connect.control;
				finish();
			}
			return false;
		}else{
			return super.onKeyDown(keyCode, event);
		}
		
	}
	
	@Override
	protected void onDestroy() {
		if(refreshThread != null){
			refreshThread.sflag = true;
		}
		super.onDestroy();
	}
	
	class RefreshThread extends Thread{
		boolean sflag = false;
		boolean rflag = true;
		@Override
		public void run() {
			while(!sflag){
				if(rflag){
				try {
					sleep(100);
					handler.sendEmptyMessage(0x01);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				}
			}
			super.run();
		}
	}
	
	public void restart(){
		isfull = true;
		backgroundBytes = Room.bitmapToBytes(diyIrBackground);
		for(Map<String, Object> map:keyList){
			frameLayout.removeView((ImageView)map.get("view"));
		}
		keyList = new ArrayList<Map<String,Object>>();
		
	}
	
	static void learnSingle(int key){
		if(isAuto){
			learnId = key + 1;
			if(learnId > learnList.size() - 1){
				Toast.makeText(Connect.activity,"学习完成！",Toast.LENGTH_LONG).show();
			}else if(key >= 0){
				Toast.makeText(Connect.activity,"学习成功，请选择下一个",Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(Connect.activity,"学习成功，请选择下一个",Toast.LENGTH_LONG).show();
		}
		learnRefresh = true;
	}
	
	public void cameraError(){
		error = true;
//		Toast.makeText(DiyIRActivity.this, "error", Toast.LENGTH_SHORT).show();
//		new Thread(){
//			public void run() {
//				try {
//					sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				handler.sendEmptyMessage(0x03);
//			};
//		}.start();
	}
	
	public boolean rectCross(int l1,int r1,int t1,int b1,int l2,int r2,int t2,int b2){
		if(l1 >= r2) return false;
		if(r1 <= l2) return false;
		if(t1 >= b2) return false;
		if(b1 <= t2) return false;
		return true;
	}
}
