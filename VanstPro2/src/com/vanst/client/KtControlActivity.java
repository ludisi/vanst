package com.vanst.client;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class KtControlActivity extends Activity{
	static byte type;
	static byte index;
	byte value1;
	byte value2;
	byte value3 = 0x17;
	int v1;
	int v2;
	int v3;
	float heightRatio;
	float widthRatio;
	boolean startFlag = false;
	boolean succeedFlag = false;
	boolean isDo = false;
	ImageView bgImageView;
	ImageView kuangImageView;
	Device device;
	TextView textView1;
	TextView textView2;
	TextView textView3;
	Intent data;
	ImageButton kg;
	ImageButton ms;
	ImageButton fs;
	ImageButton up;
	ImageButton down;
	ImageButton direction;
	RefreshThread refreshThread;
	static int pixWidth;
	static int pixHeight;
	static int width;
	static int height;
	static ImageView imageView;
	static DiyButton[] diyButtons = DiyButton.KT_BUTTONS;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what ==0x01){
				width = bgImageView.getWidth();
				height = bgImageView.getHeight();
				kg.layout(diyButtons[0].x1 * width / pixWidth, diyButtons[0].y1 * height / pixHeight, 
						diyButtons[0].x2 * width / pixWidth+25, diyButtons[0].y2 * height / pixHeight);
				ms.layout(diyButtons[1].x1 * width / pixWidth, diyButtons[1].y1 * height / pixHeight, 
						diyButtons[1].x2 * width / pixWidth+25, diyButtons[1].y2 * height / pixHeight);
				fs.layout(diyButtons[2].x1 * width / pixWidth, diyButtons[2].y1 * height / pixHeight, 
						diyButtons[2].x2 * width / pixWidth+25, diyButtons[2].y2 * height / pixHeight);
				up.layout(diyButtons[3].x1 * width / pixWidth, diyButtons[3].y1 * height / pixHeight, 
						diyButtons[3].x2 * width / pixWidth+25, diyButtons[3].y2 * height / pixHeight);
				down.layout(diyButtons[4].x1 * width / pixWidth, diyButtons[4].y1 * height / pixHeight, 
						diyButtons[4].x2 * width / pixWidth+25, diyButtons[4].y2 * height / pixHeight);
				direction.layout(diyButtons[5].x1 * width / pixWidth, diyButtons[5].y1 * height / pixHeight, 
						diyButtons[5].x2 * width / pixWidth+25, diyButtons[5].y2 * height / pixHeight);
			}else if(msg.what == 0x02){
				if(device.value2 != 0 || device.value3 != 0){
				if(v2 != device.value2 || v3 != device.value3){
					value1 = (byte) device.pose;
					value2 = (byte) device.value2;
					value3 = (byte) device.value3;
					
					if(value2 < 0){
						if(value2 + 256 >= 128){
							startFlag = true;
						}else{
							startFlag = false;
						}
					}else{
						if(value2 >= 128){
							startFlag = true;
						}else{
							startFlag = false;
						}
					}
					initText();
					new initThread().start();
				}
				}
//				initText();
//				width = bgImageView.getWidth();
//				height = bgImageView.getHeight();
//				kg.layout(diyButtons[0].x1 * width / pixWidth, diyButtons[0].y1 * height / pixHeight, 
//						diyButtons[0].x2 * width / pixWidth+25, diyButtons[0].y2 * height / pixHeight);
//				ms.layout(diyButtons[1].x1 * width / pixWidth, diyButtons[1].y1 * height / pixHeight, 
//						diyButtons[1].x2 * width / pixWidth+25, diyButtons[1].y2 * height / pixHeight);
//				fs.layout(diyButtons[2].x1 * width / pixWidth, diyButtons[2].y1 * height / pixHeight, 
//						diyButtons[2].x2 * width / pixWidth+25, diyButtons[2].y2 * height / pixHeight);
//				up.layout(diyButtons[3].x1 * width / pixWidth, diyButtons[3].y1 * height / pixHeight, 
//						diyButtons[3].x2 * width / pixWidth+25, diyButtons[3].y2 * height / pixHeight);
//				down.layout(diyButtons[4].x1 * width / pixWidth, diyButtons[4].y1 * height / pixHeight, 
//						diyButtons[4].x2 * width / pixWidth+25, diyButtons[4].y2 * height / pixHeight);
			}
		};
	};
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if(Connect.rwPose == Connect.learn) setContentView(R.layout.kt2);
//		else setContentView(R.layout.kt);
		setContentView(R.layout.kt3);
		WindowManager wManager=getWindowManager();
		Display display=wManager.getDefaultDisplay();
		heightRatio=(float) ((float)display.getHeight()/480.0);
		widthRatio=(float) ((float)display.getWidth()/320.0);
		textView1 = (TextView)findViewById(R.id.text1);
		textView2 = (TextView)findViewById(R.id.text2);
		textView3 = (TextView)findViewById(R.id.text3);
		direction = (ImageButton)findViewById(R.id.kt_direction);
		imageView = (ImageView)findViewById(R.id.kt_learn);
		pixWidth = 200;
		pixHeight = 500;
		bgImageView = (ImageView)findViewById(R.id.kt_bg);
		kuangImageView = (ImageView)findViewById(R.id.kt_kuang);
		kuangImageView.setBackgroundResource(R.drawable.kuang);
		if(Connect.rwPose == Connect.learn){
			imageView.setVisibility(View.VISIBLE);
			imageView.setBackgroundResource(R.drawable.kt_learn1);
		}
		Intent intent = getIntent();
		if(intent != null){
			type = intent.getByteExtra("type", (byte)0x00);
			index = intent.getByteExtra("index", (byte)0x00);
			value1 = intent.getByteExtra("pose", (byte)0x00); 
			value2 = intent.getByteExtra("value2", (byte)0x00);
			if(value2 == -1) value2 = 0x00;
			value3 = intent.getByteExtra("value3", (byte)0x17);
			if(value3 == -1) value3 = 0x17;
		}
		for(Device device:MainView.devices){
			if(device.type == type && device.index == index){
				this.device = device;
			}
		}
		kg = (ImageButton)findViewById(R.id.kt_kg);
		ms = (ImageButton)findViewById(R.id.kt_ms);
		fs = (ImageButton)findViewById(R.id.kt_fs);
		up = (ImageButton)findViewById(R.id.kt_up);
		down = (ImageButton)findViewById(R.id.kt_down);
		kg.setOnTouchListener(new MyTouchEvent());
		ms.setOnTouchListener(new MyTouchEvent());
		fs.setOnTouchListener(new MyTouchEvent());
		up.setOnTouchListener(new MyTouchEvent());
		down.setOnTouchListener(new MyTouchEvent());
		direction.setOnTouchListener(new MyTouchEvent());
		int v = value2;
		if(v < 0) v += 256;
		if(v >= 128){
			startFlag = true;
			initText();
		}
		if(device.value2 == -1){
			device.value2 = 0;
		}
		if(device.value3 == -1){
			device.value3 = 23;
		}
		new initThread().start();
		refreshThread = new RefreshThread();
		refreshThread.start();
		Connect.IRControl(new byte[]{(byte)0xf1,type,index,0x00,0x00,0x00}, KtControlActivity.this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		Connect.activity = this;
		new initThread().start();
	}
	
	class MyTouchEvent implements View.OnTouchListener{

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if(view == kg){
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					kuangImageView.layout(diyButtons[0].xc * width / pixWidth -25, diyButtons[0].yc * height / pixHeight - 25, 
							diyButtons[0].xc * width / pixWidth+25, diyButtons[0].yc * height / pixHeight+ 25);
//					kg.setBackgroundResource(R.drawable.kt_kg2);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					startFlag = !startFlag;
					if(startFlag) {
						value2 += 128;
//						value1 = 0x01;
//						value2 = 0x00;
//						value3 = 0x17;
					}
					else value2 -= 128;
					initText();
					imageView.setBackgroundResource(R.drawable.kt_learn2);
					if(startFlag) Connect.IRControl(new byte[]{(byte)0xf0,type,index,(byte) ((Connect.rwPose == Connect.learn)?0x03:0x02),value2,value3}, KtControlActivity.this);
					else Connect.IRControl(new byte[]{(byte)0xf0,type,index,(byte) ((Connect.rwPose == Connect.learn)?0x03:0x02),value2,value3}, KtControlActivity.this);
//					kg.setBackgroundResource(R.drawable.kt_kg1);
					kuangImageView.layout(0, 0, 0, 0);
				}
			}else if(view == ms){
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					kuangImageView.layout(diyButtons[1].xc * width / pixWidth -25, diyButtons[1].yc * height / pixHeight - 25, 
							diyButtons[1].xc * width / pixWidth+25, diyButtons[1].yc * height / pixHeight+ 25);
//					ms.setBackgroundResource(R.drawable.kt_ms2);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					value2+=16;
					if(value2 < 0){
						if((value2 + 256)/16 % 8== 5){
							value2 -=80;
						} 
					}else{
						if(value2/16 % 8== 5){
							value2 -=80;
						}
					}
					initText();
					imageView.setBackgroundResource(R.drawable.kt_learn2);
					Connect.IRControl(new byte[]{(byte)0xf0,type,index,(byte)((Connect.rwPose == Connect.learn)?0x03:0x02),value2,value3}, KtControlActivity.this);
//					ms.setBackgroundResource(R.drawable.kt_ms1);
					kuangImageView.layout(0, 0, 0, 0);
				}
			}else if(view == fs){
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					kuangImageView.layout(diyButtons[2].xc * width / pixWidth -25, diyButtons[2].yc * height / pixHeight - 25, 
							diyButtons[2].xc * width / pixWidth+25, diyButtons[2].yc * height / pixHeight+ 25);
//					fs.setBackgroundResource(R.drawable.kt_fs2);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					value2++;
					if(value2 < 0){
						if((value2 + 256)%16 == 4){
							value2 -=4;
						}
					}else{
						if(value2%16 == 4){
							value2 -=4;
						}
					}
					initText();
					imageView.setBackgroundResource(R.drawable.kt_learn2);
					Connect.IRControl(new byte[]{(byte)0xf0,type,index,(byte)((Connect.rwPose == Connect.learn)?0x03:0x02),value2,value3}, KtControlActivity.this);
//					fs.setBackgroundResource(R.drawable.kt_fs1);
					kuangImageView.layout(0, 0, 0, 0);
				}
			}else if(view == up){
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					kuangImageView.layout(diyButtons[3].xc * width / pixWidth -25, diyButtons[3].yc * height / pixHeight - 25, 
							diyButtons[3].xc * width / pixWidth+25, diyButtons[3].yc * height / pixHeight+ 25);
//					up.setBackgroundResource(R.drawable.kt_up2);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					value3++;
					if(value3 < 0){
						if((value3 + 256) % 32 == 0){
							value3--;
						}
					}else{
						if(value3 % 32 == 0){
							value3--;
						}
					}
//					if(value3 > 31) value3--;
					initText();
					imageView.setBackgroundResource(R.drawable.kt_learn2);
					Connect.IRControl(new byte[]{(byte)0xf0,type,index,(byte)((Connect.rwPose == Connect.learn)?0x03:0x02),value2,value3}, KtControlActivity.this);
//					up.setBackgroundResource(R.drawable.kt_up1);
					kuangImageView.layout(0, 0, 0, 0);
				}
			}else if(view == down){
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					kuangImageView.layout(diyButtons[4].xc * width / pixWidth -25, diyButtons[4].yc * height / pixHeight - 25, 
							diyButtons[4].xc * width / pixWidth+25, diyButtons[4].yc * height / pixHeight+ 25);
//					down.setBackgroundResource(R.drawable.kt_down2);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					value3--;
					if(value3 < 0){
						if((value3 + 256) % 32 < 16){
							value3++;
						}
					}else{
						if(value3 % 32 < 16){
							value3++;
						}
					}
//					if(value3 < 16) value3++;
					initText();
					imageView.setBackgroundResource(R.drawable.kt_learn2);
					Connect.IRControl(new byte[]{(byte)0xf0,type,index,(byte)((Connect.rwPose == Connect.learn)?0x03:0x02),value2,value3}, KtControlActivity.this);
//					down.setBackgroundResource(R.drawable.kt_down1);
					kuangImageView.layout(0, 0, 0, 0);
				}
			}else if(view == direction){
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					kuangImageView.layout(diyButtons[5].xc * width / pixWidth -25, diyButtons[5].yc * height / pixHeight - 25, 
							diyButtons[5].xc * width / pixWidth+25, diyButtons[5].yc * height / pixHeight+ 25);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					if(isDo){
						isDo = false;
						Connect.IRControl(new byte[]{(byte)0xf0,type,index,(byte)((Connect.rwPose == Connect.learn)?0x03:0x02),value2,value3}, KtControlActivity.this);
					}else{
						isDo = true;
						Connect.IRControl(new byte[]{(byte)0xf0,type,index,(byte)((Connect.rwPose == Connect.learn)?0x03:0x02),value2,value3}, KtControlActivity.this);
					}
					initText();
					imageView.setBackgroundResource(R.drawable.kt_learn2);
					kuangImageView.layout(0, 0, 0, 0);
				}
			}
			if(event.getAction() == MotionEvent.ACTION_UP){
				new initThread().start();
			}
			
			return false;
		}
		
	}
	
	/**原监听 保留
	public boolean onTouchEvent(MotionEvent event) {
		float touchX = event.getX();
		float touchY = event.getY();
		MyMyLog.i("touchX",touchX+"");
		MyLog.i("touchY",touchY+"");
		if(event.getAction() == MotionEvent.ACTION_UP){
			if(touchX > 46 * widthRatio && touchX < 92 * widthRatio &&
					touchY > 228 * heightRatio && touchY < 269 * heightRatio){
					startFlag = !startFlag;
					if(startFlag) value1 = 0x01;
					else value1 = 0x00;
					initText();
					Connect.writeControl(new byte[]{(byte)0xf0,type,index,(byte) (startFlag?0x01:0x00),0x00,0x00}, KtControlActivity.this);
				
			}else if(touchX > 32 * widthRatio && touchX < 122 * widthRatio &&
					touchY > 290 * heightRatio && touchY < 352 * heightRatio){
				value2+=16;
				if(value2/16 == 3){
					value2 -=48;
				}
				initText();
				Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,value2,value3}, KtControlActivity.this);
			}else if(touchX > 32 * widthRatio && touchX < 122 * widthRatio &&
					touchY > 365 * heightRatio && touchY < 427 * heightRatio){
				value2++;
				if(value2%16 == 4){
					value2 -=4;
				}
				initText();
				Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,value2,value3}, KtControlActivity.this);
			}else if(touchX > 169 * widthRatio && touchX < 256 * widthRatio &&
					touchY > 275 * heightRatio && touchY < 354 * heightRatio){
				value3++;
				if(value3 > 31) value3--;
				initText();
				Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,value2,value3}, KtControlActivity.this);
			}else if(touchX > 169 * widthRatio && touchX < 256 * widthRatio &&
					touchY > 354 * heightRatio && touchY < 427 * heightRatio){
				value3--;
				if(value3 < 16) value3++;
				initText();
				Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,value2,value3}, KtControlActivity.this);
			}
		}
		return super.onTouchEvent(event);	
	}
	*/
	private void initText(){
//		Toast.makeText(KtControlActivity.this, v2+" "+v3+" "+this.value2+" "+this.value3, Toast.LENGTH_SHORT).show();
		MyLog.i("", v2+" "+v3+" "+this.value2+" "+this.value3);
		int value1 = this.value1;
		int value2 = this.value2;
		int value3 = this.value3;
		if(value1 < 0) value1+=256;
		if(value2 < 0) value2+=256;
		if(value3 < 0) value3+=256;
		if(device != null){
			device.pose = value1;
			device.value2 = value2;
			device.value3 = value3;
			v1 = value1;
			v2 = value2;
			v3 = value3;
		}
		if(startFlag){
			if(value2/16 %8 == 0){
				textView1.setText("自动");
			}else if(value2/16 %8== 1){
				textView1.setText("冷风");
			}else if(value2/16 %8== 2){
				textView1.setText("热风");
			}else if(value2/16 %8== 3){
				textView1.setText("通风");
			}else if(value2/16 %8== 4){
				textView1.setText("去湿");
			}
			if(value2%16 == 0){
				textView3.setText("自动");
			}else if(value2%16 == 1){
				textView3.setText("一级");
			}else if(value2%16 == 2){
				textView3.setText("二级");
			}else if(value2%16 == 3){
				textView3.setText("三级");
			}
			textView2.setText(value3 % 32+"℃");
		}else{
			textView1.setText("");
			textView2.setText("");
			textView3.setText("");
		}
		
		data = new Intent();
		data.putExtra("type", type);
		data.putExtra("index", index);
		data.putExtra("value1",value1);
		data.putExtra("value2", value2);
		data.putExtra("value3", value3);
		setResult(RESULT_OK, data);
	}
	@Override
	protected void onDestroy() {
		if(MainView.IRDevice != null && Connect.rwPose == Connect.learn)
			Connect.writeControl(new byte[]{(byte)0xf0,(byte)MainView.IRDevice.type,(byte)MainView.IRDevice.index,0x00,0x00,0x00}, KtControlActivity.this);
		if(refreshThread!=null)
			refreshThread.sflag = true;
		finish();
		super.onDestroy();
	}
	class RefreshThread extends Thread{
		boolean sflag = false;
		public void run() {
			while(!sflag){
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0x02);
			}
			super.run();
		}
	}
	
	class initThread extends Thread{
		@Override
		public void run() {
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handler.sendEmptyMessage(0x01);
		}
	}
	
	
}
