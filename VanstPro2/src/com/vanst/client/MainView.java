package com.vanst.client;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;




import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class MainView extends SurfaceView{
	int orinBw = 800;
	int maxRatio = 2;
	static ArrayList<Device> devices;
	static Config config;
	static Room room = new Room();
	MainActivity activity;
	SurfaceHolder holder;
	Canvas canvas;
	static float screenHeight;
	static float screenWidth;
	static Bitmap bitmap;
//	static Bitmap bgBitmap;
//	static float bgratio;
	long downTime;
	long downTime2;
	Bitmap number;
	Bitmap move;
	Bitmap wait;
	Bitmap wait1;
	Bitmap wait2;
	Bitmap wait3;
	Bitmap wait4;
	Bitmap fail;
	Paint paint;
	Paint paint2;
	Paint paint3;
	RefreshThread refreshThread;
	String [] items = {"关","开"};
	static int roomNum = 1;
	static boolean isChanged;
	float visible1;
	float visible2;
	float visible3;
	float touchX;
	float touchY;
	float touchX2;
	float touchY2;
	float touchX3;
	float touchY3;
	float chooseX;
	float chooseY;
	float offsetX;
	float offsetY;
	float devOffsetX;
	float devOffsetY;
	float yuanX;
	float yuanY;
	double ratio = 1;
	double yuanRatio = 1;
	boolean ratioFlag;
	boolean drawFlag;
	boolean chooseFlag;
	boolean settingFlag;
	boolean waitflag;
	boolean outOfMemory = false;
	static boolean intiFlag;
	boolean layoutchanged;
	static String remark;
	float minRatio;
	final static int INTI_SINGLE = 0x33;
	final static int INTI_PROGRESSDIALOG = 0x04;
	final static int CANCEL_PROGRESSDIALOG = 0x05;
	final static int GET_POSE_BEGIN = 0xBB;
	final static int ASKPOSE_SINGLE = 0xB3;
	final static int CONTROL_END = 0xED;
	final static int READCONFIG2 = 0xec;
	ProgressDialog progressDialog;
	Device device;
	static Device IRDevice;
	public SensorManager sensorManager;
	public Sensor sensor;
	static public boolean waitForControl;
	float x;
	float y;
	float z;
	int getPoseTypes = 0;
	int selectType;
	long controlTime;
	byte allFlag;
	float ratate = 0;
	static int mode;
	static long touchTime = System.currentTimeMillis();
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0x01){
				if(ratio<minRatio) ratio = minRatio;
				if(offsetX > 0) offsetX = 0;
				if(offsetY > 0) offsetY = 0;
				if(offsetX < screenWidth - bitmap.getWidth()*ratio) offsetX = (float) (screenWidth - bitmap.getWidth()*ratio);
				if(offsetY < screenHeight - bitmap.getHeight()*ratio) offsetY = (float) (screenHeight - bitmap.getHeight()*ratio);
//				drawFlag = false;
//				draw();
			}else if(msg.what == INTI_SINGLE){
				initDevice();
			}else if(msg.what == INTI_PROGRESSDIALOG){
				refreshProgress();
			}else if(msg.what == CANCEL_PROGRESSDIALOG){
				progressDialog.dismiss();
			}else if(msg.what == GET_POSE_BEGIN){
				Connect.writeControl(new byte[]{(byte)0xf0,(byte)selectType,0x00,allFlag,0x00,0x00}, activity);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				waitflag = false;
			}else if(msg.what == ASKPOSE_SINGLE){
				getPoseTypes = 0;
				for(Device device:devices){
					if(device.type == selectType && device.visible){
						getPoseTypes++;
					}
				}
				refreshProgress();
				new AskPose().start();
			}else if(msg.what == CONTROL_END){
				waitForControl = false;
			}else if(msg.what == READCONFIG2){
				if(!Connect.is3G) Connect.readConfig2();
		        else if(Connect.is3G && Connect.newDate) Connect.readConfig2();
		        else new ReadConfig().start();
			}
		};
	};
	Handler progressHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			progressDialog.setProgress(msg.what);
		};
	};
	Handler modeHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			MainActivity.widgetbusy = true;
			getPoseTypes = 0;
			mode = msg.what;
			if(msg.what == 0){
				for(Device device:devices){
					if(device.allOn == 1 && device.visible){
						getPoseTypes++;
					}
				}
			}else if(msg.what == 1){
				for(Device device:devices){
					if(device.allOff == 1 && device.visible){
						getPoseTypes++;
					}
				}
			}else if(msg.what ==2){
				for(Device device:devices){
					if(device.visible && (device.moshi1 == 0 || device.moshi1 == 1)){
						getPoseTypes++;
					}
				}
			}else if(msg.what ==3){
				for(Device device:devices){
					if(device.visible && (device.moshi2 == 0 || device.moshi2 == 1)){
						getPoseTypes++;
					}
				}
			}else if(msg.what ==4){
				for(Device device:devices){
					if(device.visible && (device.moshi3 == 0 || device.moshi3 == 1)){
						getPoseTypes++;
					}
				}
			}else if(msg.what ==5){
				for(Device device:devices){
					if(device.visible && (device.moshi4 == 0 || device.moshi4 == 1)){
						getPoseTypes++;
					}
				}
			}
			VanstWidgetProvider.max = getPoseTypes;
			VanstWidgetProvider.progress = 0;
			refreshProgress();
			new AskMode().start();
		};
	};
	Handler getposeHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
//			Connect.writeSelect(new byte[]{(byte)0xf1,(byte)selectType,(byte)(msg.what),0x00,0x00,0x00});
			progressDialog.setProgress(msg.what);
			VanstWidgetProvider.progress = msg.what;
			VanstWidgetProvider.updateAppWidget(Connect.activity);
		};
	};
	
	public MainView(Context context, AttributeSet attrs){
		super(context,attrs);
		MyLog.i("debug","1");
		holder=getHolder();
		activity=(MainActivity)context;
		WindowManager wManager=activity.getWindowManager();
		Display display=wManager.getDefaultDisplay();
		screenHeight=display.getHeight();
		screenWidth=display.getWidth();
		sensorManager = (SensorManager)activity.getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		SensorEventListener sensorEventListener = new SensorEventListener() {
			public void onSensorChanged(SensorEvent e) {
				x = e.values[SensorManager.DATA_X];   
			    y = e.values[SensorManager.DATA_Y];   
			    z = e.values[SensorManager.DATA_Z];
			    if(x > 7) ratate = 90;
			    if(x < -7) ratate = -90;
			    if(y < -7) ratate = 180;
			    if(y > 7) ratate = 0;
			}
			public void onAccuracyChanged(Sensor arg0, int arg1) {}
		};
		sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
		MyLog.i("debug","2");
		new Thread(){
			public void run() {
				Room.initRooms(MainView.this);
				Config.intiConfig();
				Config.askConfig(Config.readConfig(1), MainView.this);
				repaintRoom();
				MyLog.i("debug","3");
				number = readBitMap(MainView.this, R.drawable.num);
				move = readBitMap(MainView.this, R.drawable.logo_move);
				wait = readBitMap(MainView.this, R.drawable.logo_wait);
				wait1 = readBitMap(MainView.this, R.drawable.logo_wait1);
				wait2 = readBitMap(MainView.this, R.drawable.logo_wait2);
				wait3 = readBitMap(MainView.this, R.drawable.logo_wait3);
				wait4 = readBitMap(MainView.this, R.drawable.logo_wait4);
				fail = readBitMap(MainView.this, R.drawable.logo_fail);
				paint = new Paint();
				paint2 = new Paint();
				paint3 = new Paint();
				paint.setColor(Color.BLACK);
				paint2.setColor(Color.RED);
				paint2.setTextSize(40);
				paint2.setTypeface(Typeface.DEFAULT_BOLD);
				paint3.setColor(Color.BLUE);
				paint3.setTypeface(Typeface.DEFAULT_BOLD);
				paint3.setTextSize(30);
				minRatio = (float) (screenWidth*1.0/bitmap.getWidth() > screenHeight*1.0/bitmap.getHeight()?
							screenWidth*1.0/bitmap.getWidth():screenHeight*1.0/bitmap.getHeight());
				MyLog.i("minRatio",screenWidth+"");
				MyLog.i("minRatio",bitmap.getWidth()+"");
				MyLog.i("minRatio",screenHeight+"");
				MyLog.i("minRatio",bitmap.getHeight()+"");
				MyLog.i("minRatio",minRatio+"");
				MyLog.i("debug","4");
				refreshThread = new RefreshThread();
				refreshThread.isAlive = true;
				refreshThread.isRun = true;
				refreshThread.start();
				 if(!Connect.is3G) Connect.getPose((byte)0);
			        else if(Connect.is3G && Connect.newDate) Connect.getPose((byte)0);
			        else new ReadConfig().start();
//				handler.sendEmptyMessage(READCONFIG2);
			};
		}.start();
		
	}
	public void draw() throws IllegalStateException {
		try {
			
		
		canvas = holder.lockCanvas();
		if(canvas !=null){
			float offsetX = this.offsetX;
			float offsetY = this.offsetY;
			double ratio = this.ratio;
			float ratate = this.ratate;
			if(outOfMemory){ ratio = 1; this.ratio = 1;}
			canvas.drawRect(0, 0, screenWidth, screenHeight, paint);
			try {
				Rect rect = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
				Rect rectf = new Rect((int)offsetX,(int)offsetY,(int)(bitmap.getWidth()* ratio+offsetX),(int)(bitmap.getHeight()*ratio+offsetY));
				canvas.drawBitmap(bitmap,rect,rectf,null); 
//				Rect rect = new Rect(0,0,bgBitmap.getWidth(),bgBitmap.getHeight());
//				Rect rectf = new Rect((int)offsetX,(int)offsetY,(int)(bgBitmap.getWidth()*bgratio* ratio+offsetX),(int)(bgBitmap.getHeight()*bgratio*ratio+offsetY));
//				canvas.drawBitmap(bgBitmap,rect,rectf,null); 
			} catch (Exception e) {
				MyLog.i("bitmap","error");
				outOfMemory = true;
			}
			if(refreshThread.isRun){
			try {
				deviceDraw(canvas,offsetX,offsetY,ratio,ratate);
			} catch (Exception e) {
			}
			int textwidth = (int)paint2.measureText(roomNum+"");
			canvas.drawText(roomNum+"", screenWidth-textwidth-20 , screenHeight -( MainActivity.buttonFlag ? (activity.controlButton.getHeight()):0)-20, paint2);
			if(remark!=null && remark.length()!=0){ 
				textwidth = (int)paint.measureText(remark);
				canvas.drawText(remark, screenWidth/2 - textwidth/2, screenHeight - ( MainActivity.buttonFlag ? (activity.controlButton.getHeight()):0)-20, paint3);
			}
			
			}
			holder.unlockCanvasAndPost(canvas);
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public Bitmap dePaint(Bitmap bitmap,float ratio){
		Bitmap resizeBmp;
		try {
			resizeBmp = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*ratio),(int)( bitmap.getHeight()*ratio), true);
		} catch (OutOfMemoryError e) {
			outOfMemory = true;
			resizeBmp = bitmap;
		}
		return resizeBmp;
	}
	
	public Bitmap rePaint(Bitmap bitmap,double ratio){
		  Matrix matrix = new Matrix(); 
		  matrix.postScale((float)ratio,(float)ratio); //长和宽放大缩小的比例
		  matrix.postRotate(ratate+Room.defaultAngle, bitmap.getWidth()/2, bitmap.getHeight()/2);
		  Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		  return resizeBmp;
	}
	  public static Bitmap readBitMap(View view, int resId){  
	        BitmapFactory.Options opt = new BitmapFactory.Options();  
	        opt.inPreferredConfig = Bitmap.Config.RGB_565;   
	        opt.inPurgeable = true;  
	        opt.inInputShareable = true;  
	        InputStream is = view.getResources().openRawResource(resId);  
	        return BitmapFactory.decodeStream(is,null,opt);  
	    }
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(System.currentTimeMillis() - touchTime > 5000){
			try {
				if(Connect.flag && Connect.out!=null){
					Connect.out.writeByte((byte)0xaa);
					Connect.out.flush();
					touchTime = System.currentTimeMillis();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(event.getPointerCount() >= 3){
			if(event.getAction() == MotionEvent.ACTION_DOWN ){
				touchX3 = event.getX(2);
				touchY3 = event.getY(2);
				touchX2 = event.getX(1);
			    touchY2 = event.getY(1);
			    touchX = event.getX(0);
			    touchY = event.getY(0);
			    MyLog.i(event.getPointerCount()+"","DOWN");
			}else if(event.getAction() == MotionEvent.ACTION_MOVE){
//				touchX3 = event.getX(2);
//				touchY3 = event.getY(2);
//				touchX2 = event.getX(1);
//			    touchY2 = event.getY(1);
//			    touchX = event.getX(0);
//			    touchY = event.getY(0);
				if(touchY < event.getY(0) && touchY2 < event.getY(1) && touchY3 < event.getY(2)){
					if(visible1<0) visible1 =0;
					visible1++;
				}
				if(touchY > event.getY(0) && touchY2 > event.getY(1) && touchY3 > event.getY(2)){
					if(visible1>0) visible1 =0;
					visible1--;
				}
				touchY = event.getY(0);
				touchY2 = event.getY(1);
				touchY3 = event.getY(2);
				
				Log.i("Y1",touchY + "  " +event.getY(0));
				Log.i("Y2",touchY2 + "  " +event.getY(1));
				Log.i("Y3",touchY3 + "  " +event.getY(2));
				if(visible1 > 2){
					MainActivity.buttonFlag = true;
					activity.linearLayout.setVisibility(View.VISIBLE);
				}
				if(visible1 < - 2){
					MainActivity.buttonFlag = false;
					activity.linearLayout.setVisibility(View.GONE);
				}
//				if(event.getY(0) > touchY + 100 && event.getY(1) > touchY2 + 100){
//					if(activity.linearLayout.getVisibility() == View.VISIBLE){
//						MainActivity.buttonFlag = true;
//						activity.linearLayout.setVisibility(View.VISIBLE);
//					}
//				}
//				if(event.getY(0) < touchY - 100 && event.getY(1) < touchY2 - 100){
//					if(activity.linearLayout.getVisibility() == View.GONE){
//						MainActivity.buttonFlag = false;
//						activity.linearLayout.setVisibility(View.GONE);
//					}
//				}
			}else if(event.getAction() == MotionEvent.ACTION_UP){
				visible3 = 0;
			}
		}else if(event.getPointerCount() == 2){
			visible1 = 0;
			ratioFlag = true;
			if(event.getAction() == MotionEvent.ACTION_DOWN ){
				touchX2 = event.getX(1);
			    touchY2 = event.getY(1);
			    touchX = event.getX(0);
			    touchY = event.getY(0);
			    MyLog.i(event.getPointerCount()+"","DOWN");
			}else if(event.getAction() == MotionEvent.ACTION_MOVE ){
				if(Math.sqrt((event.getX(0)-event.getX(1)) * (event.getX(0)-event.getX(1))+
							(event.getY(0)-event.getY(1)) * (event.getY(0)-event.getY(1)))-
						Math.sqrt((touchX - touchX2)*(touchX - touchX2)+(touchY-touchY2)*(touchY-touchY2))>0){
					if(ratio < maxRatio){
					ratio *= 1.02;
					if(ratio > maxRatio) ratio = maxRatio;
					offsetX -= (screenWidth/2 -offsetX) *1.02 -(screenWidth/2 - offsetX);
					offsetY -= (screenHeight/2 -offsetY) *1.02 - (screenHeight/2 - offsetY);
					}
				}else{
					if(ratio > 0.5){
					ratio /= 1.02;
					offsetX += (screenWidth/2 - offsetX) - (screenWidth/2 -offsetX) /1.02 ;
					offsetY += (screenHeight/2 - offsetY) - (screenHeight/2 -offsetY) /1.02  ;
					}
				}
				touchX2 = event.getX(1);
			    touchY2 = event.getY(1);
			    touchX = event.getX(0);
			    touchY = event.getY(0);
			}else if(event.getAction() == MotionEvent.ACTION_UP){
				 MyLog.i(event.getPointerCount()+"","UP");
				yuanRatio *= ratio;
				ratio = 1;
				
			}
		}else{
			if(event.getAction() == MotionEvent.ACTION_DOWN && checkChoose(event.getX(), event.getY())!=null ){
				chooseX = event.getX();
				chooseY = event.getY();
				touchX = event.getX();
				touchY = event.getY();
				if(device!=null)device.isSelect = true;
				settingFlag = true;
				if(activity.isSetting) chooseFlag = true;
			}else if(event.getAction() == MotionEvent.ACTION_MOVE && chooseFlag){
				isChanged = true;
				if(settingFlag && (event.getX()-chooseX >10 || chooseX-event.getX()>10 || event.getY()-chooseY>10 || chooseY-event.getY()>10)){
					settingFlag = false;
				}
				float x = event.getX() - offsetX - device.width/2;
				float y = event.getY() - offsetY - device.height/2;
				float doffsetX = (float)(x/ratio);
				float doffsetY = (float)(y/ratio);
				if(doffsetX < 10) doffsetX = 10;
				if(doffsetY < 10) doffsetY = 10;
				if(doffsetX > bitmap.getWidth() - device.width - 10) doffsetX = bitmap.getWidth() - device.width - 10;
				if(doffsetY > bitmap.getHeight() - device.height - 10) doffsetY = bitmap.getHeight() - device.height - 10;
				device.offsetX = doffsetX;
				device.offsetY = doffsetY;
//				device.offsetX = (float)(x/ratio);
//				device.offsetY = (float)(y/ratio);
//				if(device.offsetX < 10) device.offsetX = 10;
//				if(device.offsetY < 10) device.offsetY = 10;
//				if(device.offsetX > bitmap.getWidth() - device.width - 10) device.offsetX = bitmap.getWidth() - device.width - 10;
//				if(device.offsetY > bitmap.getHeight() - device.height - 10) device.offsetY = bitmap.getHeight() - device.height - 10;
			}else if(event.getAction() == MotionEvent.ACTION_DOWN){
				if(touchX + 50 > event.getX() && touchX - 50 < event.getX() && touchY + 50 > event.getY() && touchY - 50 < event.getY() &&
						System.currentTimeMillis() < downTime + 300){
					layoutchanged = true;
					if(activity.linearLayout.isShown()){
						MainActivity.buttonFlag = false;
						activity.linearLayout.setVisibility(View.GONE);
					}else{
						MainActivity.buttonFlag = true;
						activity.linearLayout.setVisibility(View.VISIBLE);
					}
				}else{
					downTime = System.currentTimeMillis();
				}
				touchX = event.getX();
				touchY = event.getY();
			}else if(event.getAction() == MotionEvent.ACTION_MOVE){
				if(settingFlag && (event.getX()-chooseX >10 || chooseX-event.getX()>10 || event.getY()-chooseY>10 || chooseY-event.getY()>10)){
					settingFlag = false;
				}
				if(ratioFlag) ratioFlag = false;
				else{
					if(!layoutchanged){
					offsetX += event.getX() - touchX;
					offsetY += event.getY() - touchY;
					}
				}
				touchX = event.getX();
				touchY = event.getY();
			}else if(event.getAction() == MotionEvent.ACTION_UP){
				if(device!=null)device.isSelect = false;
				if(settingFlag && event.getX()-chooseX <10 && chooseX-event.getX()<10 && event.getY()-chooseY<10 && chooseY-event.getY()<10){
					settingFlag = false;
					if(activity.isSetting){
						if(Connect.rwPose == Connect.learn){
							learnDevice();
						}else{
						isChanged = true;
						AlertDialog.Builder builder = new AlertDialog.Builder(activity);
						builder.setIcon(Device.DEVICE_IMAGES[device.type-1]);
						builder.setTitle(MainActivity.deviceNames[device.type-1]+device.index+"设置：");
						builder.setPositiveButton("取消", null);
						if(device.type == Device.DEVICE_3D  || device.type == Device.DEVICE_DVD ||
								device.type == Device.DEVICE_JDH || device.type == Device.DEVICE_SATELLITE || device.type == Device.DEVICE_TV){
							builder.setItems(Device.IR_ITEMS, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									if(which == 0){
										AlertDialog.Builder builder = new AlertDialog.Builder(activity);
										builder.setTitle("确认要移除该"+MainActivity.deviceNames[device.type-1]+"吗？");
										builder.setIcon(Device.DEVICE_IMAGES[device.type-1]);
										builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface arg0, int arg1) {
												device.visible = false;
											}
										});
										builder.setNegativeButton("取消", null);
										builder.show();
									}else if(which == 1){
										device.bind=true;
										Connect.writeSelect(new byte[]{(byte)0xfe,(byte)device.type,(byte)device.index,'B','B','B'});
									}else if(which == 2){
										device.bind=true;
										Connect.writeSelect(new byte[]{(byte)0xfe,(byte)device.type,(byte)device.index,'Q','Q','Q'});
									}else if(which == 3){
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
											AlertDialog.Builder builder = new AlertDialog.Builder(Connect.activity);
											builder.setTitle("请选择红外精灵");
											builder.setCancelable(false);
											builder.setItems(IRStrings, new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// TODO Auto-generated method stub
													if(Connect.flag){
														Connect.writeSelect(new byte[]{(byte)0xf0,(byte)Device.DEVICE_IR,(byte)IRnums[which],0x01,0x00,0x00});
//														Connect.writeControl(new byte[]{(byte)0xf0,(byte)device.type,(byte)device.index,0x01,0x00,0x00}, activity);
														Connect.rwPose = Connect.learn;
														for(Device device:devices){
															if(device.type == Device.DEVICE_IR && device.index == IRnums[which]){
																IRDevice = device;
															}
														}
														learnDevice();
													}
												}
												
											});
											builder.show();
										}else{
											if(Connect.flag){
												Toast.makeText(activity, "请先添加学习精灵", Toast.LENGTH_LONG).show();
											}else
												Toast.makeText(activity, "服务器未连接", Toast.LENGTH_LONG).show();
										}
										
										
//										AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//										builder.setTitle("确认要移除该"+MainActivity.deviceNames[device.type-1]+"吗？");
//										builder.setIcon(Device.DEVICE_IMAGES[device.type-1]);
//										builder.setPositiveButton("取消", null);
//										builder.show();
									}
								}
							});
						}else if(device.type == Device.DEVICE_CURTAIN){
							builder.setItems(Device.CURTAIN_ITEMS, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									if(which == 0){
										AlertDialog.Builder builder = new AlertDialog.Builder(activity);
										builder.setTitle("确认要移除该"+MainActivity.deviceNames[device.type-1]+"吗？");
										builder.setIcon(Device.DEVICE_IMAGES[device.type-1]);
										builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface arg0, int arg1) {
												device.visible = false;
											}
										});
										builder.setNegativeButton("取消", null);
										builder.show();
									}else if(which == 1){
										device.bind=true;
										Connect.writeSelect(new byte[]{(byte)0xfe,(byte)device.type,(byte)device.index,'B','B','B'});
									}else if(which == 2){
										device.bind=true;
										Connect.writeSelect(new byte[]{(byte)0xfe,(byte)device.type,(byte)device.index,'Q','Q','Q'});
									}else if(which == 3){
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
											AlertDialog.Builder builder = new AlertDialog.Builder(Connect.activity);
											builder.setTitle("请选择红外精灵");
											builder.setCancelable(false);
											builder.setItems(IRStrings, new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// TODO Auto-generated method stub
													if(Connect.flag){
														Connect.writeSelect(new byte[]{(byte)0xf0,(byte)Device.DEVICE_IR,(byte)IRnums[which],0x01,0x00,0x00});
//														Connect.writeControl(new byte[]{(byte)0xf0,(byte)device.type,(byte)device.index,0x01,0x00,0x00}, activity);
														Connect.rwPose = Connect.learn;
														for(Device device:devices){
															if(device.type == Device.DEVICE_IR && device.index == IRnums[which]){
																IRDevice = device;
															}
														}
														learnDevice();
													}
												}
												
											});
											builder.show();
										}else{
											if(Connect.flag){
												Toast.makeText(activity, "请先添加学习精灵", Toast.LENGTH_LONG).show();
											}else
												Toast.makeText(activity, "服务器未连接", Toast.LENGTH_LONG).show();
										}
									}else if(which == 4){
										AlertDialog.Builder builder = new AlertDialog.Builder(activity);
										builder.setTitle("确认要清除该"+MainActivity.deviceNames[device.type-1]+"ID吗？");
										builder.setIcon(Device.DEVICE_IMAGES[device.type-1]);
										builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface arg0, int arg1) {
												Connect.writeSelect(new byte[]{(byte)0xf5,(byte)device.type,(byte)device.index,0x00,0x00,0x00});
											}
										});
										builder.setNegativeButton("取消", null);
										builder.show();
									}
								}
							});
						}else if(device.type == Device.DEVICE_AIR_CONDITIONING){
							builder.setItems(Device.AIR_CONDITIONING, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									if(which == 0){
										AlertDialog.Builder builder = new AlertDialog.Builder(activity);
										builder.setTitle("确认要移除该"+MainActivity.deviceNames[device.type-1]+"吗？");
										builder.setIcon(Device.DEVICE_IMAGES[device.type-1]);
										builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface arg0, int arg1) {
												device.visible = false;
											}
										});
										builder.setNegativeButton("取消", null);
										builder.show();
									}else if(which == 1){
										device.bind=true;
										Connect.writeSelect(new byte[]{(byte)0xfe,(byte)device.type,(byte)device.index,'B','B','B'});
									}else if(which == 2){
										device.bind=true;
										Connect.writeSelect(new byte[]{(byte)0xfe,(byte)device.type,(byte)device.index,'Q','Q','Q'});
									}else if(which == 3){
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
											AlertDialog.Builder builder = new AlertDialog.Builder(Connect.activity);
											builder.setTitle("请选择红外精灵");
											builder.setCancelable(false);
											builder.setItems(IRStrings, new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// TODO Auto-generated method stub
													if(Connect.flag){
														Connect.writeSelect(new byte[]{(byte)0xf0,(byte)Device.DEVICE_IR,(byte)IRnums[which],0x01,0x00,0x00});
//														Connect.writeControl(new byte[]{(byte)0xf0,(byte)device.type,(byte)device.index,0x01,0x00,0x00}, activity);
														Connect.rwPose = Connect.learn;
														for(Device device:devices){
															if(device.type == Device.DEVICE_IR && device.index == IRnums[which]){
																IRDevice = device;
															}
														}
														learnDevice();
													}
												}
												
											});
											builder.show();
										}else{
											if(Connect.flag){
												Toast.makeText(activity, "请先添加学习精灵", Toast.LENGTH_LONG).show();
											}else
												Toast.makeText(activity, "服务器未连接", Toast.LENGTH_LONG).show();
										}
									}else if(which == 4){
										final EditText editText = new EditText(activity);
										AlertDialog.Builder builder = new AlertDialog.Builder(activity);
										builder.setTitle("请输入"+MainActivity.deviceNames[device.type-1]+"4位16进制ID。");
										builder.setIcon(Device.DEVICE_IMAGES[device.type-1]);
										builder.setView(editText);
										builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface arg0, int arg1) {
												String string = editText.getText().toString();
												if(string.length() == 4 && checkChar(string.charAt(0)) && checkChar(string.charAt(1)) && checkChar(string.charAt(2))&& checkChar(string.charAt(3))){
													Connect.writeSelect(new byte[]{(byte)0xf9,(byte)device.type,(byte)device.index,(byte) CtB(string.charAt(0), string.charAt(1)) ,(byte) CtB(string.charAt(2), string.charAt(3)),0x00});
												}else{
													Toast.makeText(activity, "输入有误！", Toast.LENGTH_SHORT).show();
												}
//												Connect.writeSelect(new byte[]{(byte)0xf5,(byte)device.type,(byte)device.index,0x00,0x00,0x00});
											}
										});
										builder.setNegativeButton("取消", null);
										builder.show();
									}
								}
							});
						}else if(device.type == Device.DEVICE_DIY){
							builder.setItems(Device.DIY_ITEMS, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									if(which == 0){
										AlertDialog.Builder builder = new AlertDialog.Builder(activity);
										builder.setTitle("确认要移除该"+MainActivity.deviceNames[device.type-1]+"吗？");
										builder.setIcon(Device.DEVICE_IMAGES[device.type-1]);
										builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface arg0, int arg1) {
												device.visible = false;
											}
										});
										builder.setNegativeButton("取消", null);
										builder.show();
									}else if(which == 1){
										device.bind=true;
										Connect.writeSelect(new byte[]{(byte)0xfe,(byte)device.type,(byte)device.index,'B','B','B'});
									}else if(which == 2){
										device.bind=true;
										Connect.writeSelect(new byte[]{(byte)0xfe,(byte)device.type,(byte)device.index,'Q','Q','Q'});
									}else if(which == 3){
										File file = new File(Environment.getExternalStorageDirectory()+"/vanst/"+"diy"+device.index);
										if(file.exists()){
											AlertDialog.Builder builder = new AlertDialog.Builder(activity);
											builder.setTitle("该自定义设备已经存在是否要重新编辑？");
											builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int which) {
													Intent intent=new Intent("android.intent.action.proDiyIR");
													intent.putExtra("type", (byte)device.type);
													intent.putExtra("index", (byte)device.index);
													intent.putExtra("you", true);
													activity.startActivity(intent);
												}
											});
											builder.setNegativeButton("否", null);
											builder.show();
										}else{
											Intent intent=new Intent("android.intent.action.proDiyIR");
											intent.putExtra("type", (byte)device.type);
											intent.putExtra("index", (byte)device.index);
											intent.putExtra("you", false);
											activity.startActivity(intent);
										}
										
//										File file = new File(Environment.getExternalStorageDirectory()+"/vanst/"+"diy"+device.index);
//										if(file.exists()){
//											AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//											builder.setTitle("该自定义设备已经存在是否要重新编辑？");
//											builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
//												public void onClick(DialogInterface dialog, int which) {
//													AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//													builder.setTitle("是否要重新选取背景图");
//													builder.setPositiveButton("照相拍取", new DialogInterface.OnClickListener() {
//														public void onClick(DialogInterface dialog, int which) {
//															Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//															activity.startActivityForResult(intent, CAMERA);
//														}
//													});
//													builder.setNeutralButton("相册选取", new DialogInterface.OnClickListener() {
//														public void onClick(DialogInterface dialog, int which) {
//															Intent intent=new Intent("android.intent.action.proPhoto");
//															activity.startActivityForResult(intent, PHOTO);
//														}
//													});
//													builder.setNegativeButton("沿用原先", new DialogInterface.OnClickListener() {
//														public void onClick(DialogInterface dialog, int which) {
//															Intent intent=new Intent("android.intent.action.proDiyIR");
//															intent.putExtra("type", (byte)device.type);
//															intent.putExtra("index", (byte)device.index);
//															intent.putExtra("from", OLD);
//															activity.startActivityForResult(intent, OLD);
//														}
//													});
//													builder.show();
//												}
//											});
//											builder.setPositiveButton("否", null);
//											builder.show();
//										}
									}
								}
							});
						}else{
							builder.setItems(Device.DEVICE_ITEMS, new DialogInterface.OnClickListener() {			
								public void onClick(DialogInterface dialog, int which) {
									if(which == 0){
										AlertDialog.Builder builder = new AlertDialog.Builder(activity);
										builder.setTitle("确认要移除该"+MainActivity.deviceNames[device.type-1]+"吗？");
										builder.setIcon(Device.DEVICE_IMAGES[device.type-1]);
										builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface arg0, int arg1) {
												device.visible = false;
											}
										});
										builder.setNegativeButton("取消", null);
										builder.show();
									}else if(which == 1){
										device.bind=true;
										Connect.writeSelect(new byte[]{(byte)0xfe,(byte)device.type,(byte)device.index,'B','B','B'});
									}else if(which == 2){
										device.bind=true;
										Connect.writeSelect(new byte[]{(byte)0xfe,(byte)device.type,(byte)device.index,'Q','Q','Q'});
									}
								}
							});
						}
						builder.show();
						}
					}else{
						controlDevice();
					}
				}
				chooseFlag = false;
				layoutchanged = false;
			}
		}
		drawFlag = true;
		return drawFlag;
	}
	public void learnDevice(){
		final int type = device.type;
		final int index = device.index;
		if(type == Device.DEVICE_CURTAIN || type == Device.DEVICE_TV || 
				type == Device.DEVICE_JDH || type == Device.DEVICE_DVD || 
				type == Device.DEVICE_3D || type == Device.DEVICE_DIY ||
				type == Device.DEVICE_SATELLITE){
			Intent intent = new Intent("android.intent.action.proClControl");
			intent.putExtra("type", (byte)type);
			intent.putExtra("index", (byte)index);
			activity.startActivityForResult(intent, type);	
		}else if(type == Device.DEVICE_AIR_CONDITIONING){
			Intent intent = new Intent("android.intent.action.proKtControl");
			intent.putExtra("type", (byte)type);
			intent.putExtra("index", (byte)index);
			activity.startActivityForResult(intent, type);		
		}else if(type == Device.DEVICE_IR){
			if(IRDevice != null && Connect.flag)
			Connect.writeControl(new byte[]{(byte)0xf0,(byte)IRDevice.type,(byte)IRDevice.index,0x00,0x00,0x00}, activity);
			Connect.rwPose = Connect.control;
			Toast.makeText(activity, "退出红外学习", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(activity, "选择设备有误", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void controlDevice(){
		final int type = device.type;
		final int index = device.index;
		final Device controlDevice = this.device;
		Connect.rwPose = Connect.control;
		if(type == Device.DEVICE_THERMOMETER){
			Connect.writeControl(new byte[]{(byte)0xf1,(byte)type,(byte)index,0x00,0x00,0x00},activity);
			for(Device device:devices){
				if(device.type == type && device.index == index){
					MyLog.i("device.type+",device.type+"");
					MyLog.i("device.index",device.index+"");
					MyLog.i("device.value2",device.value2+"");
					MyLog.i("device.value3",device.value3+"");
					float tem = device.value2;
					if(device.value2 < 0) tem += 256;
					tem *= 256.0;
					tem += device.value3;
					if(device.value3 < 0) tem += 256;
					tem = tem / 100 - 40;
					final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
					builder.setTitle(MainActivity.deviceNames[type-1]+index);
					builder.setIcon(Device.DEVICE_IMAGES[type-1]);
					if(device.value2 == -1 && device.value3 == -1) builder.setMessage("温度为：");
					else builder.setMessage("温度为："+((int)(tem*10))/10.0+"℃");
					builder.setPositiveButton("确认", null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
		}else if(type == Device.DEVICE_RICE_COOKER){
			Intent intent = new Intent("android.intent.action.proDfbControl");
			intent.putExtra("type", (byte)type);
			intent.putExtra("index", (byte)index);
			for(Device device:devices){
				if(device.type == type && device.index == index){
					intent.putExtra("pose",(byte)device.value2);
					MyLog.i("value2",device.value2+"");
				}
			}
			activity.startActivityForResult(intent, type);		
		}else if(type == Device.DEVICE_WASHER){
			Intent intent = new Intent("android.intent.action.proXyjControl");
			intent.putExtra("type", (byte)type);
			intent.putExtra("index", (byte)index);
			for(Device device:devices){
				if(device.type == type && device.index == index){
					intent.putExtra("pose",(byte)device.value2);
					MyLog.i("value2",device.value2+"");
				}
			}
			activity.startActivityForResult(intent, type);		
		}else if (type == Device.DEVICE_AIR_CONDITIONING) {
			Intent intent = new Intent("android.intent.action.proKtControl");
			intent.putExtra("type", (byte)type);
			intent.putExtra("index", (byte)index);
			for(Device device:devices){
				if(device.type == type && device.index == index){
					intent.putExtra("pose",(byte)device.pose);
					intent.putExtra("value2", (byte)device.value2);
					intent.putExtra("value3", (byte)device.value3);
				}
			}
			activity.startActivityForResult(intent, type);
		}else if (type == Device.DEVICE_AIR_CONDITIONING2) {
			Intent intent = new Intent("android.intent.action.proKt485Control");
			intent.putExtra("type", (byte)type);
			intent.putExtra("index", (byte)index);
			for(Device device:devices){
				if(device.type == type && device.index == index){
					intent.putExtra("pose",(byte)device.pose);
					intent.putExtra("value2", (byte)device.value2);
					intent.putExtra("value3", (byte)device.value3);
				}
			}
			activity.startActivityForResult(intent, type);
		}else if (type == Device.DEVICE_LISTEN){
			Intent intent = new Intent("android.intent.action.proListenControl");
			intent.putExtra("type", (byte)type);
			intent.putExtra("index", (byte)index);
			for(Device device:devices){
				if(device.type == type && device.index == index){
					intent.putExtra("pose",(byte)device.pose);
					intent.putExtra("value2", (byte)device.value2);
				}
			}
			activity.startActivity(intent);
		}else if(type == Device.DEVICE_DIY){
			Intent intent = new Intent("android.intent.action.proDiyControl");
			intent.putExtra("type", (byte)type);
			intent.putExtra("index", (byte)index);
			activity.startActivityForResult(intent, type);	
//			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			activity.startActivityForResult(intent, ty);
		}else if (type == Device.DEVICE_CURTAIN || type == Device.DEVICE_TV || 
				type == Device.DEVICE_JDH || type == Device.DEVICE_DVD || 
				type == Device.DEVICE_3D || type == Device.DEVICE_DIY ||
				type == Device.DEVICE_SATELLITE) {
			Intent intent = new Intent("android.intent.action.proClControl");
			intent.putExtra("type", (byte)type);
			intent.putExtra("index", (byte)index);
			for(Device device:devices){
				if(device.type == type && device.index == index){
					intent.putExtra("pose",(byte)device.pose);
				}
			}
			activity.startActivityForResult(intent, type);			
		}else if (type == Device.DEVICE_LIGHT){
			Intent intent = new Intent("android.intent.action.proTdControl");
			intent.putExtra("type", (byte)type);
			intent.putExtra("index", (byte)index);
			for(Device device:devices){
				if(device.type == type && device.index == index){
					intent.putExtra("pose",(byte)device.pose);
					intent.putExtra("value2", (byte)device.value2);
				}
			}
			activity.startActivityForResult(intent, type);	
//		}else if (type == Device.DEVICE_FAN){
//			Intent intent = new Intent("android.intent.action.proFsControl");
//			intent.putExtra("type", (byte)type);
//			intent.putExtra("index", (byte)index);
//			for(Device device:devices){
//				if(device.type == type && device.index == index){
//					intent.putExtra("pose",(byte)device.pose);
//					intent.putExtra("value2", (byte)device.value2);
//				}
//			}
//			activity.startActivityForResult(intent, type);	
		}else if (type == Device.DEVICE_DIY){
			Intent intent = new Intent("android.intent.action.proDeviceControl");
			intent.putExtra("type", (byte)type);
			intent.putExtra("index", (byte)index);
			for(Device device:devices){
				if(device.type == type && device.index == index){
					intent.putExtra("pose",(byte)device.pose);
					intent.putExtra("value2", (byte)device.value2);
				}
			}
			activity.startActivityForResult(intent, type);	
		}else{
//			if(device.pose == 0){
//				Connect.writeControl(new byte[]{(byte)0xf0, (byte)type, (byte)index,(byte)0x01,0x00,0x00}, activity);
//			}else if(device.pose == 1){
//				Connect.writeControl(new byte[]{(byte)0xf0, (byte)type, (byte)index,(byte)0x00,0x00,0x00}, activity);
//			}else{
//				Connect.writeControl(new byte[]{(byte)0xf1,(byte)type,(byte)index,0x00,0x00,0x00}, activity);
//			}
			
			if(waitForControl && System.currentTimeMillis()- controlTime < 1000) return;
			waitForControl = true;
			controlTime = System.currentTimeMillis();
			if(device.pose == 0 && !device.isWait){
				new Thread(){
					public void run() {
						Connect.writeControl(new byte[]{(byte)0xf0, (byte)type, (byte)index,(byte)0x01,0x00,0x00}, activity);
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if(controlDevice.isWait) {
							controlTime = System.currentTimeMillis();
							Connect.writeSelect(new byte[]{(byte)0xf0, (byte)type, (byte)index,(byte)0x01,0x00,0x00});
							try {
								sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if(controlDevice.isWait) {
								controlTime = System.currentTimeMillis();
								Connect.writeSelect(new byte[]{(byte)0xf0, (byte)type, (byte)index,(byte)0x01,0x00,0x00});
								
							}
						}
						if(type == Device.DEVICE_LOCK){
							try {
								sleep(4000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							Connect.writeControl(new byte[]{(byte)0xf1,(byte)type,(byte)index,0x00,0x00,0x00}, activity);
						}
//						waitForControl = false;
//						handler.sendEmptyMessage(CONTROL_END);
//						handler.sendEmptyMessage(CANCEL_PROGRESSDIALOG);
					};
				}.start();
			}else if(device.pose == 1 && !device.isWait){
				new Thread(){
					public void run() {
						Connect.writeControl(new byte[]{(byte)0xf0, (byte)type, (byte)index,(byte)0x00,0x00,0x00}, activity);
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if(controlDevice.isWait){
							controlTime = System.currentTimeMillis();
							Connect.writeSelect(new byte[]{(byte)0xf0, (byte)type, (byte)index,(byte)0x00,0x00,0x00});
							try {
								sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if(controlDevice.isWait) {
								controlTime = System.currentTimeMillis();
								Connect.writeSelect(new byte[]{(byte)0xf0, (byte)type, (byte)index,(byte)0x00,0x00,0x00});
							}
//						waitForControl = false;
//						handler.sendEmptyMessage(CONTROL_END);
						}
						if(type == Device.DEVICE_LOCK){
							try {
								sleep(4000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							Connect.writeControl(new byte[]{(byte)0xf1,(byte)type,(byte)index,0x00,0x00,0x00}, activity);
						}
					};
				}.start();
			}else{
				new Thread(){
					public void run() {
						Connect.writeControl(new byte[]{(byte)0xf1,(byte)type,(byte)index,0x00,0x00,0x00}, activity);
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if(!controlDevice.isWait) return;
						controlTime = System.currentTimeMillis();
						Connect.writeSelect(new byte[]{(byte)0xf1,(byte)type,(byte)index,0x00,0x00,0x00});
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if(!controlDevice.isWait) return;
						controlTime = System.currentTimeMillis();
						Connect.writeSelect(new byte[]{(byte)0xf1,(byte)type,(byte)index,0x00,0x00,0x00});
//						waitForControl = false;
//						handler.sendEmptyMessage(CONTROL_END);
					};
				}.start();
			}
		}
	}
	
	
	
	
	public Device checkChoose(float x,float y){
		this.device = null;
		Device thisdevice = null;
		if(!devices.isEmpty()){
			for(Device device:devices){
				if(device.visible && x + 15/ratio > offsetX + (float)(device.offsetX+(ratio-1)*device.offsetX) && 
						   x - 15/ratio < offsetX + (float)(device.offsetX+device.width+(ratio-1)*(device.offsetX+device.width)) &&
						   y + 15/ratio > offsetY + (float)(device.offsetY+(ratio-1)*device.offsetY) &&
					       y - 15/ratio < offsetY + (float)(device.offsetY+device.height+(ratio-1)*(device.offsetY+device.height))){
					thisdevice = device;
				}
			}
			this.device = thisdevice;
		}
		if(this.device !=null) this.device.isMove = false;
		return this.device;
	}
	public Bitmap ratateBitmap(Bitmap bitmap){
		Matrix matrix = new Matrix();
		matrix.postRotate(90, bitmap.getWidth(), bitmap.getHeight());
		return bitmap;
	}
	
	public void deviceDraw(Canvas canvas,float offsetX,float offsetY,double ratio,float ratate){
		float wratio = 1;
		if(screenWidth >= 1000){
			wratio = (float) 1.5;
		}
		if(screenWidth <= 400){
			wratio = (float) 0.5;
		}
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setTextSize((float) (15*ratio));
		paint.setTypeface(Typeface.create("微软雅黑", Typeface.BOLD));
		Paint paint2 = new Paint();
		paint2.setColor(Color.argb(127, 0, 0, 0));
		if(canvas != null){
			if(!devices.isEmpty()){
				for(Device device:devices){
					if(device.visible){
						canvas.rotate(ratate+Room.defaultAngle, offsetX+(float)(device.offsetX+(ratio-1)*device.offsetX+device.width*ratio/2),  
								offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY+device.height*ratio/2));
						canvas.drawCircle((float) (offsetX+(device.offsetX+(ratio-1)*device.offsetX)+device.unknowBitmap.getWidth()*ratio/2),
								(float) (offsetY+(device.offsetY+(ratio-1)*device.offsetY) +device.unknowBitmap.getWidth()*ratio/2), 
								(float) ((device.unknowBitmap.getWidth()/2+3)*ratio * wratio), paint);
						if(device.isSelect){
							drawMyBitmap(canvas, device.unknowBitmap, ratio,(int)(offsetX+(float)(device.offsetX+(ratio-1)*device.offsetX)), 
									(int)(offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY)),wratio);
						}else if(device.type == Device.DEVICE_AIR_CONDITIONING || device.type == Device.DEVICE_AIR_CONDITIONING2){
							int v2 = device.value2;
							if(v2 < 0) v2 += 256;
							if(v2 == 255){
								drawMyBitmap(canvas, device.unknowBitmap, ratio,(int)(offsetX+(float)(device.offsetX+(ratio-1)*device.offsetX)), 
										(int)(offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY)),wratio);
							}else if(v2 > 127){
								drawMyBitmap(canvas, device.onBitmap, ratio,(int)(offsetX+(float)(device.offsetX+(ratio-1)*device.offsetX)), 
										(int)(offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY)),wratio);
							}else {
								drawMyBitmap(canvas, device.offBitmap, ratio,(int)(offsetX+(float)(device.offsetX+(ratio-1)*device.offsetX)), 
										(int)(offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY)),wratio);
							}
						
						}else if(device.type == Device.DEVICE_WASHER){
							if(device.value3 == 0){
								drawMyBitmap(canvas, device.offBitmap, ratio,(int)(offsetX+(float)(device.offsetX+(ratio-1)*device.offsetX)), 
										(int)(offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY)),wratio);
							}else if(device.value3 == 1){
								drawMyBitmap(canvas, device.onBitmap, ratio,(int)(offsetX+(float)(device.offsetX+(ratio-1)*device.offsetX)), 
										(int)(offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY)),wratio);
							}else{
								drawMyBitmap(canvas, device.unknowBitmap, ratio,(int)(offsetX+(float)(device.offsetX+(ratio-1)*device.offsetX)), 
										(int)(offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY)),wratio);
							}
						}else if(device.pose == 0){
							drawMyBitmap(canvas, device.offBitmap, ratio,(int)(offsetX+(float)(device.offsetX+(ratio-1)*device.offsetX)), 
									(int)(offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY)),wratio);
						}else if(device.pose == 1){
							drawMyBitmap(canvas, device.onBitmap, ratio,(int)(offsetX+(float)(device.offsetX+(ratio-1)*device.offsetX)), 
									(int)(offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY)),wratio);
						}else{
							drawMyBitmap(canvas, device.unknowBitmap, ratio,(int)(offsetX+(float)(device.offsetX+(ratio-1)*device.offsetX)), 
									(int)(offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY)),wratio);
						}
						
						if(activity.isSetting){
////							canvas.drawBitmap(Bitmap.createBitmap(number, device.index/100*number.getWidth()/10, 0,number.getWidth()/10 , number.getHeight()),
////									offsetX+(float)(device.offsetX+(ratio-1)*device.offsetX+device.width/2*ratio - number.getWidth()/10*1.5), 
////									offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY+device.height/1.3*ratio),  null);
							{
								Rect rect = new Rect(device.index%100/10*number.getWidth()/10, 0, device.index%100/10*number.getWidth()/10+number.getWidth()/10 , number.getHeight());
								Rect rectf = new Rect((int)offsetX+(int)(device.offsetX+(ratio-1)*device.offsetX+device.width/2*ratio - number.getWidth()*ratio*wratio/10),
										(int)(offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY+device.height/1.3*ratio)),
										(int)offsetX+(int)(device.offsetX+(ratio-1)*device.offsetX+device.width/2*ratio),
										(int)(offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY+device.height/1.3*ratio) + number.getHeight()*ratio*wratio));
								canvas.drawBitmap(number, rect, rectf, null);
							}
							{
								Rect rect = new Rect(device.index%10*number.getWidth()/10, 0, device.index%10*number.getWidth()/10+number.getWidth()/10 , number.getHeight());
								Rect rectf = new Rect((int)offsetX+(int)(device.offsetX+(ratio-1)*device.offsetX+device.width/2*ratio),
										(int)(offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY+device.height/1.3*ratio)),
										(int)offsetX+(int)((float)(device.offsetX+(ratio-1)*device.offsetX+device.width/2*ratio + number.getWidth()*ratio*wratio/10)),
										(int)(offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY+device.height/1.3*ratio) + number.getHeight()*ratio*wratio));
								canvas.drawBitmap(number, rect, rectf, null);
								
							}	
//							canvas.drawBitmap(Bitmap.createBitmap(number, device.index%100/10*number.getWidth()/10, 0,number.getWidth()/10 , number.getHeight()),
//									offsetX+(float)(device.offsetX+(ratio-1)*device.offsetX+device.width/2*ratio - number.getWidth()/10), 
//									offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY+device.height/1.3*ratio),  null);
//							canvas.drawBitmap(Bitmap.createBitmap(number, device.index%10*number.getWidth()/10, 0,number.getWidth()/10 , number.getHeight()),
//									offsetX+(float)(device.offsetX+(ratio-1)*device.offsetX+device.width/2*ratio), 
//									offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY+device.height/1.3*ratio),  null);
							if(device.isMove){
								Rect rect = new Rect(0, 0, move.getWidth(),move.getHeight());
								Rect rectf = new Rect( (int)((float)(offsetX + (device.offsetX + device.width * 1.5)* ratio - move.getWidth() * ratio * wratio /2)), (int)((float)(offsetY + device.offsetY *ratio - move.getHeight() *3/4  * ratio * wratio )), 
										(int)((float)(offsetX + (device.offsetX + device.width * 1.5)* ratio + move.getWidth() * ratio * wratio /2)), (int)((float)(offsetY + device.offsetY *ratio +  move.getHeight() /4 * ratio * wratio )));
								canvas.drawBitmap(move, rect, rectf, paint);
	//							canvas.drawBitmap(move, (float) (offsetX + (device.offsetX +device.width/2)*ratio), (float) (offsetY + (device.offsetY+12) *ratio - move.getHeight()), null);
							}
						}else{
							if(device.isWait){
								if(System.currentTimeMillis() - device.controlTime > 11000){
									device.isWait = false;
									device.pose = -1;
									Connect.writeSelect(new byte[]{(byte)0xf1,(byte)device.type,(byte)device.index,0x00,0x00,0x00});
//									Rect rect = new Rect(0, 0, fail.getWidth(),fail.getHeight());
//									Rect rectf = new Rect( (int)((float)(offsetX + (device.offsetX + device.width/2)* ratio - fail.getWidth() * ratio * wratio /2)), (int)((float)(offsetY + device.offsetY *ratio - fail.getHeight() * ratio * wratio )), 
//											(int)((float)(offsetX + (device.offsetX + device.width/2)* ratio + fail.getWidth() * ratio * wratio /2)), (int)((float)(offsetY + device.offsetY *ratio)));
//									canvas.drawBitmap(fail, rect, rectf, paint);
//									canvas.drawBitmap(fail,(float)(offsetX + (device.offsetX + device.width/2)* ratio - fail.getWidth()/2),(float)(offsetY + device.offsetY *ratio - fail.getHeight()),null);
								}else{
									Rect rect = new Rect(0, 0, wait.getWidth(),wait.getHeight());
									Rect rectf = new Rect( (int)((float)(offsetX + (device.offsetX + device.width/2)* ratio - wait.getWidth() * ratio * wratio /2)), (int)((float)(offsetY + device.offsetY *ratio - wait.getHeight() * ratio * wratio )), 
											(int)((float)(offsetX + (device.offsetX + device.width/2)* ratio + wait.getWidth() * ratio * wratio /2)), (int)((float)(offsetY + device.offsetY *ratio)));
									if((System.currentTimeMillis() - device.controlTime)/1000%4 == 0){
										canvas.drawBitmap(wait1, rect, rectf, paint);
									}else if((System.currentTimeMillis() - device.controlTime)/1000%4 == 1){
										canvas.drawBitmap(wait2, rect, rectf, paint);
									}else if((System.currentTimeMillis() - device.controlTime)/1000%4 == 2){
										canvas.drawBitmap(wait3, rect, rectf, paint);
									}else if((System.currentTimeMillis() - device.controlTime)/1000%4 == 3){
										canvas.drawBitmap(wait4, rect, rectf, paint);
									}
									
//									canvas.drawBitmap(wait,(float)(offsetX + (device.offsetX + device.width/2)* ratio - wait.getWidth()/2),(float)(offsetY + device.offsetY *ratio - wait.getHeight()),null);
								}
							}
						if(device.remark != null && device.remark.length() > 0){
							int textwidth = (int)paint.measureText(device.remark);
							canvas.drawRect(offsetX+(float)(device.offsetX+(ratio-1)*device.offsetX+device.width/2*ratio-textwidth/2*ratio), offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY+device.height*ratio)+1, 
									offsetX+(float)(device.offsetX+(ratio-1)*device.offsetX+device.width/2*ratio+textwidth/2*ratio), offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY+device.height*ratio+paint.getTextSize()*ratio)+2, paint2);
							canvas.drawText(device.remark,offsetX+(float)(device.offsetX+(ratio-1)*device.offsetX+device.width/2*ratio-textwidth/2*ratio), 
									offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY+device.height*ratio+ paint.getTextSize()*ratio), paint);
							}
						}
						canvas.rotate(-ratate-Room.defaultAngle, offsetX+(float)(device.offsetX+(ratio-1)*device.offsetX+device.width*ratio/2),  offsetY+(float)(device.offsetY+(ratio-1)*device.offsetY+device.height*ratio/2));
					}
				}
			}
		}
	}
	public void drawMyBitmap(Canvas canvas,Bitmap bitmap,double ratio,int offsetX,int offsetY,float wratio){
		int des = (int)(bitmap.getWidth() * ratio * wratio - bitmap.getWidth()*ratio)/2;
		Rect rect = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
		Rect rectf = new Rect(offsetX - des,offsetY - des,
				(int)(offsetX+bitmap.getWidth()*ratio * wratio)- des,
				(int)(offsetY+bitmap.getHeight()*ratio * wratio)- des);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		canvas.drawBitmap(bitmap, rect, rectf, paint);
	}
	
	private void initDevice(){
		devices = new ArrayList<Device>();
		for(int i=0;i<MainActivity.deviceNames.length;i++){
			if(!MainActivity.deviceNums.get(i).get("number").equals("0")){
				for(int j = 0;j<Integer.parseInt((String) MainActivity.deviceNums.get(i).get("number"));j++){
					devices.add(new Device(MainView.this, i+1, j+1));
				}
			}
		}
	}
	public void addDevice(Device device){
		boolean flag = true;
		if(!devices.isEmpty()){
			for(Device device2:devices){
				if(device2.type == device.type && device2.index == device.index){
//					MyLog.i("type:"+device.type,"index"+device.index);
					device2.offsetX = (float) ((screenWidth/2 - offsetX)/ratio) - 20;
					device2.offsetY = (float) ((screenHeight/2 - offsetY)/ratio) - 20;
					device2.room = roomNum;
					device2.visible = true;
					device2.isMove = true;
					flag = false;
				}
			}
		}
		if(flag){
			device.offsetX = (float) ((screenWidth/2 - offsetX)/ratio) - 20;
			device.offsetY = (float) ((screenHeight/2 - offsetY)/ratio) - 20;
			device.room = roomNum;
			device.visible = true;
			device.isMove = true;
			devices.add(device);
		}
	}
	public void addDevice(int type,int index){
		boolean flag = true;
		if(!devices.isEmpty()){
			for(Device device:devices){
				if(device.type == type && device.index == index){
					MyLog.i("type:"+device.type,"index"+device.index);
					device.offsetX = (float) ((screenWidth/2 - offsetX)/ratio) - 20;
					device.offsetY = (float) ((screenHeight/2 - offsetY)/ratio) - 20;
					device.room = roomNum;
					device.visible = true;
					device.isMove = true;
					flag = false;
				}
			}
		}
		if(flag){
			Device device = new Device(MainView.this, type, index,roomNum,
					(float)((screenWidth/2 - offsetX)/ratio - 20),
					(float)((screenHeight/2 - offsetY)/ratio - 20));
			devices.add(device);
			device.isMove = true;
		}
	}
	public void deleteDevice(int type,int index){
		if(!devices.isEmpty()){
			for(Device device:devices){
				if(device.type == type && device.index == index){
					device.visible = false;
				}
			}
		}
	}
	
	public void repaintRoom(){
		offsetX = 0;
		offsetY = 0;
		ratio = 1;
		offsetX = (float) (screenWidth / 2 - bitmap.getWidth() /2);
		offsetY = (float) (screenHeight / 2 - bitmap.getHeight() /2);
	}
	
	public boolean checkChar(char c){
		if((c>='0' && c<='9') ||(c>='a' && c<='f')||(c>='A' && c<='F')){
			return true;
		}else
			return false;
	}
	public int CtB(char a,char b){
		int i = 0;
		if(a == '0'){
			i = 0x00;
		}else if(a == '1'){
			i = 0x10;
		}else if(a == '2'){
			i = 0x20;
		}else if(a == '3'){
			i = 0x30;
		}else if(a == '4'){
			i = 0x40;
		}else if(a == '5'){
			i = 0x50;
		}else if(a == '6'){
			i = 0x60;
		}else if(a == '7'){
			i = 0x70;
		}else if(a == '8'){
			i = 0x80;
		}else if(a == '9'){
			i = 0x90;
		}else if(a == 'a'|| a == 'A'){
			i = 0xa0;
		}else if(a == 'b'|| a == 'B'){
			i = 0xb0;
		}else if(a == 'c'|| a == 'C'){
			i = 0xc0;
		}else if(a == 'd'|| a == 'D'){
			i = 0xd0;
		}else if(a == 'e'|| a == 'e'){
			i = 0xe0;
		}else if(a == 'f'|| a == 'f'){
			i = 0xf0;
		}
		if(b == '0'){
			i += 0x00;
		}else if(b == '1'){
			i += 0x01;
		}else if(b == '2'){
			i += 0x02;
		}else if(b == '3'){
			i += 0x03;
		}else if(b == '4'){
			i += 0x04;
		}else if(b == '5'){
			i += 0x05;
		}else if(b == '6'){
			i += 0x06;
		}else if(b == '7'){
			i += 0x07;
		}else if(b == '8'){
			i += 0x08;
		}else if(b == '9'){
			i += 0x09;
		}else if(b == 'a'|| b == 'A'){
			i += 0x0a;
		}else if(b == 'b'|| b == 'B'){
			i += 0x0b;
		}else if(b == 'c'|| a == 'C'){
			i += 0x0c;
		}else if(b == 'd'|| b == 'D'){
			i += 0x0d;
		}else if(b == 'e'|| b == 'e'){
			i += 0x0e;
		}else if(b == 'f'|| b == 'f'){
			i += 0x0f;
		}
		
		return i;
	}
	
	class GetPose extends Thread{
		@Override
		public void run() {
			while(!intiFlag);
			ArrayList<Byte> types = new ArrayList<Byte>();
			if(Connect.initRealtime){
				MyLog.i("devices", devices.size()+"");
				for(Device device:devices){
					if(!types.isEmpty()){
						boolean you = false;
						for(Byte b:types){
							if(b == (byte)device.type){
								you = true;
							}
						}
						if(!you){
							types.add((byte)device.type);
						}
					}else types.add((byte)device.type);
				}
			}
			MyLog.i("TYPES", types.size()+"");
			if(!types.isEmpty()){
				getPoseTypes = types.size();
				handler.sendEmptyMessage(INTI_PROGRESSDIALOG);
				int i = 0;
				for(Byte type:types){
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Connect.getPose(type);
					i++;
					progressHandler.sendEmptyMessage(i);
				}
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(CANCEL_PROGRESSDIALOG);
			}
		}
	}
	
	class AskMode extends Thread{
		boolean sflag = false;
		@Override
		public void run() {
			if(Connect.flag){
				int j = 0;
				if(mode == 0){
					for(Device device:devices){
						if(device.allOn == 1 && device.visible){
							int time = 0;
							while(true){
								if(device.type == Device.DEVICE_CURTAIN){
									Connect.writeSelect(new byte[]{(byte)0xf0,(byte)device.type,(byte)device.index,0x02,0x04,0x00});
								}else
									Connect.writeSelect(new byte[]{(byte)0xf0,(byte)device.type,(byte)device.index,0x01,0x00,0x00});
								getposeHandler.sendEmptyMessage(j+1);
								try {
									sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								time++;
								if(device.pose == 0x01 || sflag||time >= 3){ 
									break;
								}
							}
							if(sflag){ 
								break;
							}
							j++;
						}
					}
				}else if(mode == 1){
					for(Device device:devices){
						if(device.allOff == 1 && device.visible){
							int time = 0;
							while(true){
								if(device.type == Device.DEVICE_CURTAIN){
									Connect.writeSelect(new byte[]{(byte)0xf0,(byte)device.type,(byte)device.index,0x02,0x05,0x00});
								}else
									Connect.writeSelect(new byte[]{(byte)0xf0,(byte)device.type,(byte)device.index,0x00,0x00,0x00});
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
				}else{
					for(Device device:devices){
						if(device.visible && (mode == 2 && (device.moshi1 == 0 || device.moshi1 == 1) ||
								mode == 3 && (device.moshi2 == 0 || device.moshi2 == 1) ||
								mode == 4 && (device.moshi3 == 0 || device.moshi3 == 1) ||
								mode == 5 && (device.moshi4 == 0 || device.moshi4 == 1))){
							int time = 0;
							while(true){
								if(mode == 2) {
									if(device.type == Device.DEVICE_CURTAIN){
										Connect.writeSelect(new byte[]{(byte)0xf0,(byte)device.type,(byte)device.index,0x02,(byte) (0x05 - device.moshi1),0x00});
									}else
										Connect.writeSelect(new byte[]{(byte)0xf0,(byte)device.type,(byte)device.index,(byte) device.moshi1,0x00,0x00});
								}
								else if(mode == 3){
									if(device.type == Device.DEVICE_CURTAIN){
										Connect.writeSelect(new byte[]{(byte)0xf0,(byte)device.type,(byte)device.index,0x02,(byte) (0x05 - device.moshi2),0x00});
									}else
										Connect.writeSelect(new byte[]{(byte)0xf0,(byte)device.type,(byte)device.index,(byte) device.moshi2,0x00,0x00});
								}
								else if(mode == 4){
									if(device.type == Device.DEVICE_CURTAIN){
										Connect.writeSelect(new byte[]{(byte)0xf0,(byte)device.type,(byte)device.index,0x02,(byte) (0x05 - device.moshi3),0x00});
									}else
										Connect.writeSelect(new byte[]{(byte)0xf0,(byte)device.type,(byte)device.index,(byte) device.moshi3,0x00,0x00});
								}
								else if(mode == 5){
									if(device.type == Device.DEVICE_CURTAIN){
										Connect.writeSelect(new byte[]{(byte)0xf0,(byte)device.type,(byte)device.index,0x02,(byte) (0x05 - device.moshi4),0x00});
									}else
										Connect.writeSelect(new byte[]{(byte)0xf0,(byte)device.type,(byte)device.index,(byte) device.moshi4,0x00,0x00});
								}
								getposeHandler.sendEmptyMessage(j+1);
								try {
									sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								time++;
								if(mode == 2 && device.pose == device.moshi1  || 
										mode == 3 && device.pose == device.moshi2 ||
										mode == 4 && device.pose == device.moshi3 ||
									
										
										mode == 5 && device.pose == device.moshi4 ||
										sflag||time >= 3){ 
									break;
								}
							}
							if(sflag){ 
								break;
							}
							j++;
						}
					}
				}
				handler.sendEmptyMessage(CANCEL_PROGRESSDIALOG);
			}
			MainActivity.widgetbusy = false;
			VanstWidgetProvider.updateAppWidget(Connect.activity);
		}
	}
	
	class AskPose extends Thread{
		boolean sflag = false;
		public void run() {
			if(getPoseTypes < 1){
				waitflag = true;
				
				try {
					sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if(getPoseTypes !=0)
				handler.sendEmptyMessage(GET_POSE_BEGIN);
				
				while(waitflag);
				if(Connect.flag){
					int i = 0;
					for(Device device:devices){
						if(device.type == selectType && device.visible){
							Connect.writeSelect(new byte[]{(byte)0xf1,(byte)selectType,(byte)(device.index),0x00,0x00,0x00});
							getposeHandler.sendEmptyMessage(i+1);
							i++;
							try {
								sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						if(sflag){ 
							break;
						}
					}
					handler.sendEmptyMessage(CANCEL_PROGRESSDIALOG);
				}
			}else{
				if(Connect.flag){
					int [] indexs = new int [getPoseTypes];
					int j = 0;
					for(Device device:devices){
						if(device.type == selectType && device.visible){
							indexs[j] = device.index;
							j++;
						}
					}
					Arrays.sort(indexs);
					for(j = 0;j < indexs.length;j++){
						int time = 0;
						while(true){
							Connect.writeSelect(new byte[]{(byte)0xf0,(byte)selectType,(byte)(indexs[j]),allFlag,0x00,0x00});
							getposeHandler.sendEmptyMessage(j+1);
							try {
								sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							time++;
							boolean isSucceed = false;
							for(Device device:devices){
								if(device.type == selectType && device.index == indexs[j] && device.pose == allFlag){
									isSucceed = true;
								}
							}
							if(isSucceed || sflag||time >= 3){ 
								break;
							}
						}
						if(sflag){ 
							break;
						}
					}
//					int i = 0;
//					for(Device device:devices){
//						if(device.type == selectType && device.visible){
//							Connect.writeSelect(new byte[]{(byte)0xf0,(byte)selectType,(byte)(device.index),allFlag,0x00,0x00});
//							getposeHandler.sendEmptyMessage(i+1);
//							i++;
//							try {
//								sleep(1000);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//						}
//						if(sflag){ 
//							break;
//						}
//					}
					handler.sendEmptyMessage(CANCEL_PROGRESSDIALOG);
				}
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
						Connect.synchronizationHandler.sendEmptyMessage(Connect.GET_POSE);
						isAlive = false;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	private void refreshProgress(){
		if(Connect.flag){
			progressDialog = new ProgressDialog(activity);   
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
	class RefreshThread extends Thread{
		boolean isAlive;
		boolean isRun;
		boolean isRefresh;
		public void run() {
			while(isAlive){
				if(isRun){
					try {
						sleep(40);
					} catch (InterruptedException e) {
						
					}
						if(ratio<minRatio) ratio = minRatio;
						if(offsetX < screenWidth - bitmap.getWidth()*ratio) offsetX = (float) (screenWidth - bitmap.getWidth()*ratio);
						if(MainActivity.buttonFlag){
							if(offsetY < screenHeight - bitmap.getHeight()*ratio - activity.controlButton.getHeight()) offsetY = (float) (screenHeight - bitmap.getHeight()*ratio) - activity.controlButton.getHeight();
						}else{
							if(offsetY < screenHeight - bitmap.getHeight()*ratio) offsetY = (float) (screenHeight - bitmap.getHeight()*ratio);
						}
						if(offsetX > 0) offsetX = 0;
						if(offsetY > 0) offsetY = 0;
						try {
							draw();
						} catch (Exception e) {
							// TODO: handle exception
						}
						drawFlag = false;
				}
			}
		}
	}
}
