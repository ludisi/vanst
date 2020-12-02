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
import android.widget.LinearLayout;
import android.widget.Toast;

public class DfbControlActivity extends Activity {
	 DiyButton [] diyButtons = DiyButton.DFB_BUTTONS;
	 byte type;
	 byte index;
	 byte reByte;
	 byte pose;
	 float heightRatio;
	 float widthRatio;
	 float heightCenter;
	 float widthCenter;
	 boolean startFlag = false;
	 boolean suceedFlag = false;
	 int f;
	 RefreshThread refreshThread;
	 Device device;
	 ImageButton imageButton;
	 ImageButton button0;
	 ImageButton button1;
	 ImageButton button2;
	 ImageButton button3;
	 ImageButton button4;
	 ImageButton button5;
	 ImageButton button6;
	 ImageButton buttonz;
	 LinearLayout linearLayout;
	 float bzWidthCenter;
	 float bzHeightCenter;
	 Handler handler = new Handler(){
		 public void handleMessage(android.os.Message msg) {
			 if(device.value3 != 0) setPose(msg.what);
			 device.value3 = 0;
			 
		 };
	 };
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        WindowManager wManager=getWindowManager();
			Display display=wManager.getDefaultDisplay();
			try {
				setContentView(R.layout.dfb2);
			} catch (OutOfMemoryError e) {
				Toast.makeText(DfbControlActivity.this, "ÄÚ´æ¿Õ¼ä²»×ã£¡", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
			heightRatio=(float) ((float)display.getHeight()/480.0);
			widthRatio=(float) ((float)display.getWidth()/320.0);
			heightCenter = (float)((float)display.getHeight()/2);
			widthCenter = (float)((float)display.getWidth()/2);
			startFlag = false;
			button0 = (ImageButton)findViewById(R.id.dfb_bt0);
			button1 = (ImageButton)findViewById(R.id.dfb_bt1);
			button2 = (ImageButton)findViewById(R.id.dfb_bt2);
			button3 = (ImageButton)findViewById(R.id.dfb_bt3);
			button4 = (ImageButton)findViewById(R.id.dfb_bt4);
			button5 = (ImageButton)findViewById(R.id.dfb_bt5);
			button6 = (ImageButton)findViewById(R.id.dfb_bt6);
			buttonz = (ImageButton)findViewById(R.id.dfb_btz);
			linearLayout = (LinearLayout)findViewById(R.id.dfb);
			buttonz.setOnTouchListener(new MyListener2());
//			button0.setBackgroundResource(R.drawable.dfb_02);
			imageButton = (ImageButton)findViewById(R.id.dfb_start);
			button0.setOnClickListener(new MyListener());
			button1.setOnClickListener(new MyListener());
			button2.setOnClickListener(new MyListener());
			button3.setOnClickListener(new MyListener());
			button4.setOnClickListener(new MyListener());
			button5.setOnClickListener(new MyListener());
			button6.setOnClickListener(new MyListener());
			
			setStartListner();
			linearLayout.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int width = linearLayout.getWidth();
					int height = linearLayout.getHeight();
					int pixWidth = 375;
					int pixHeight = 600;
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						for(DiyButton diyButton:diyButtons){
							if(event.getX() * pixWidth / width > diyButton.x1 && event.getX() * pixWidth / width< diyButton.x2 && 
									event.getY() * pixHeight / height > diyButton.y1 && event.getY() * pixHeight / height < diyButton.y2){
								setSelect(diyButton.num);
								
							}
						}
					}
					// TODO Auto-generated method stub
					return false;
				}
			});
	        Intent intent = getIntent();
	        if(intent != null){
		        type = intent.getByteExtra("type", (byte) 0x00);
		        index = intent.getByteExtra("index", (byte) 0x00);	
		        pose = intent.getByteExtra("pose", (byte) 0xff);
	        }
	        setPose(pose);
	        setSelect(pose);
	        for(Device device:MainView.devices){
				if(type == device.type && index == device.index){
					this.device = device;
				}
			}
	       refreshThread = new RefreshThread();
	       refreshThread.start();
	 }
	 class MyListener2 implements View.OnTouchListener{

		public boolean onTouch(View arg0, MotionEvent event) {
			bzWidthCenter = buttonz.getWidth()/2;
			bzHeightCenter = buttonz.getHeight()/2;
			float touchX = event.getX();
			float touchY = event.getY();
			double angle = Math.atan((touchX - bzWidthCenter)/(touchY - bzHeightCenter))*180/3.14;
			if(touchY - bzHeightCenter >= 0){
				if(touchX - bzWidthCenter < 0){
					setSelect(0);
				}else{
					setSelect(4);
				}
			}else if(angle < -75){
				setSelect(4);
			}else if(angle < -45){
				setSelect(3);
			}else if(angle < -15){
				setSelect(5);
			}else if(angle < 15){
				setSelect(6);
			}else if(angle < 45){
				setSelect(2);
			}else if(angle < 75){
				setSelect(1);
			}else{
				setSelect(0);
			}
//			MyLog.i("bzWidthCenter "+bzWidthCenter, touchX+"");
//			MyLog.i("bzHeightCenter "+bzHeightCenter, touchY+"");
			return false;
		}
		 
	 }
	protected void onResume() {
		super.onResume();
		Connect.activity = this;
	}
	public void setSelect(int i){
		if(!startFlag){
			if(i != reByte){
				switch (i) {
				case 0:
					reByte = (byte)0x00;
					buttonz.setBackgroundResource(R.drawable.zzswasher002);
					break;
				case 1:
					reByte = (byte)0x01;
					buttonz.setBackgroundResource(R.drawable.zzswasher003);
					break;
				case 2:
					reByte = (byte)0x02;
					buttonz.setBackgroundResource(R.drawable.zzswasher004);
					break;
				case 3:
					reByte = (byte)0x03;
					buttonz.setBackgroundResource(R.drawable.zzswasher007);
					break;
				case 4:
					reByte = (byte)0x04;
					buttonz.setBackgroundResource(R.drawable.zzswasher008);
					break;
				case 5:
					reByte = (byte)0x05;
					buttonz.setBackgroundResource(R.drawable.zzswasher006);
					break;
				case 6:
					reByte = (byte)0x06;
					buttonz.setBackgroundResource(R.drawable.zzswasher005);
					break;
				default:
					break;
				}
				MyLog.i("reByte",reByte+"");
			}
		}
	}
	
	
	 public void setPose(int i){
//		if(i != reByte){
			switch (i) {
			case 0:
				button0.setBackgroundResource(R.drawable.buttone);
				button1.setBackgroundResource(R.drawable.buttone);
				button2.setBackgroundResource(R.drawable.buttone);
				button3.setBackgroundResource(R.drawable.buttone);
				button4.setBackgroundResource(R.drawable.buttone);
				button5.setBackgroundResource(R.drawable.buttone);
				button6.setBackgroundResource(R.drawable.buttone);
				imageButton.setBackgroundResource(R.drawable.buttona);
				buttonz.setBackgroundResource(R.drawable.zzswasher002);
				startFlag = false;
				break;
			case 1:
				f = i; 
				button0.setBackgroundResource(R.drawable.buttone);
				button1.setBackgroundResource(R.drawable.buttonf);
				button2.setBackgroundResource(R.drawable.buttone);
				button3.setBackgroundResource(R.drawable.buttone);
				button4.setBackgroundResource(R.drawable.buttone);
				button5.setBackgroundResource(R.drawable.buttone);
				button6.setBackgroundResource(R.drawable.buttone);
				imageButton.setBackgroundResource(R.drawable.buttona);
				startFlag = false;
				buttonz.setBackgroundResource(R.drawable.zzswasher003);
//				imageButton.setBackgroundResource(R.drawable.buttond);
//				startFlag = true;
				break;
			case 2:
				f = i; 
				button0.setBackgroundResource(R.drawable.buttone);
				button1.setBackgroundResource(R.drawable.buttone);
				button2.setBackgroundResource(R.drawable.buttonf);
				button3.setBackgroundResource(R.drawable.buttone);
				button4.setBackgroundResource(R.drawable.buttone);
				button5.setBackgroundResource(R.drawable.buttone);
				button6.setBackgroundResource(R.drawable.buttone);
				imageButton.setBackgroundResource(R.drawable.buttona);
				startFlag = false;
				buttonz.setBackgroundResource(R.drawable.zzswasher004);
//				imageButton.setBackgroundResource(R.drawable.buttond);
//				startFlag = true;
				break;
			case 3:
				f = i; 
				button0.setBackgroundResource(R.drawable.buttone);
				button1.setBackgroundResource(R.drawable.buttone);
				button2.setBackgroundResource(R.drawable.buttone);
				button3.setBackgroundResource(R.drawable.buttonf);
				button4.setBackgroundResource(R.drawable.buttone);
				button5.setBackgroundResource(R.drawable.buttone);
				button6.setBackgroundResource(R.drawable.buttone);
				imageButton.setBackgroundResource(R.drawable.buttona);
				startFlag = false;
				buttonz.setBackgroundResource(R.drawable.zzswasher007);
//				imageButton.setBackgroundResource(R.drawable.buttond);
//				startFlag = true;
				break;
			case 4:
				f = i; 
				button0.setBackgroundResource(R.drawable.buttone);
				button1.setBackgroundResource(R.drawable.buttone);
				button2.setBackgroundResource(R.drawable.buttone);
				button3.setBackgroundResource(R.drawable.buttone);
				button4.setBackgroundResource(R.drawable.buttonf);
				button5.setBackgroundResource(R.drawable.buttone);
				button6.setBackgroundResource(R.drawable.buttone);
				imageButton.setBackgroundResource(R.drawable.buttona);
				startFlag = false;
				buttonz.setBackgroundResource(R.drawable.zzswasher008);
//				imageButton.setBackgroundResource(R.drawable.buttond);
//				startFlag = true;
				break;
			case 5:
				f = i; 
				button0.setBackgroundResource(R.drawable.buttone);
				button1.setBackgroundResource(R.drawable.buttone);
				button2.setBackgroundResource(R.drawable.buttone);
				button3.setBackgroundResource(R.drawable.buttone);
				button4.setBackgroundResource(R.drawable.buttone);
				button5.setBackgroundResource(R.drawable.buttonf);
				button6.setBackgroundResource(R.drawable.buttone);
				imageButton.setBackgroundResource(R.drawable.buttona);
				startFlag = false;
				buttonz.setBackgroundResource(R.drawable.zzswasher006);
//				imageButton.setBackgroundResource(R.drawable.buttond);
//				startFlag = true;
				break;
			case 6:
				f = i; 
				button0.setBackgroundResource(R.drawable.buttone);
				button1.setBackgroundResource(R.drawable.buttone);
				button2.setBackgroundResource(R.drawable.buttone);
				button3.setBackgroundResource(R.drawable.buttone);
				button4.setBackgroundResource(R.drawable.buttone);
				button5.setBackgroundResource(R.drawable.buttone);
				button6.setBackgroundResource(R.drawable.buttonf);
				imageButton.setBackgroundResource(R.drawable.buttona);
				startFlag = false;
				buttonz.setBackgroundResource(R.drawable.zzswasher005);
//				imageButton.setBackgroundResource(R.drawable.buttond);
//				startFlag = true;
				break;
			case 7:
				f = i; 
				button0.setBackgroundResource(R.drawable.buttong);
				button1.setBackgroundResource(R.drawable.buttone);
				button2.setBackgroundResource(R.drawable.buttone);
				button3.setBackgroundResource(R.drawable.buttone);
				button4.setBackgroundResource(R.drawable.buttone);
				button5.setBackgroundResource(R.drawable.buttone);
				button6.setBackgroundResource(R.drawable.buttone);
				imageButton.setBackgroundResource(R.drawable.buttond);
				buttonz.setBackgroundResource(R.drawable.zzswasher002);
				startFlag = true;
				break;
			case 8:
				if(f == 1){
					button1.setBackgroundResource(R.drawable.buttong);
					buttonz.setBackgroundResource(R.drawable.zzswasher003);
				}else if(f == 2){
					button2.setBackgroundResource(R.drawable.buttong);
					buttonz.setBackgroundResource(R.drawable.zzswasher004);
				}else if(f == 3){
					button3.setBackgroundResource(R.drawable.buttong);
					buttonz.setBackgroundResource(R.drawable.zzswasher007);
				}else if(f == 4) {
					button4.setBackgroundResource(R.drawable.buttong);
					buttonz.setBackgroundResource(R.drawable.zzswasher008);
				}else if(f == 5) {
					button5.setBackgroundResource(R.drawable.buttong);
					buttonz.setBackgroundResource(R.drawable.zzswasher006);
				}else if(f == 6){
					button6.setBackgroundResource(R.drawable.buttong);
					buttonz.setBackgroundResource(R.drawable.zzswasher005);
				}
//				button0.setBackgroundResource(R.drawable.buttonf);
//				button1.setBackgroundResource(R.drawable.buttone);
//				button2.setBackgroundResource(R.drawable.buttone);
//				button3.setBackgroundResource(R.drawable.buttone);
//				button4.setBackgroundResource(R.drawable.buttone);
//				button5.setBackgroundResource(R.drawable.buttone);
//				button6.setBackgroundResource(R.drawable.buttone);
				imageButton.setBackgroundResource(R.drawable.buttond);
				startFlag = true;
				break;
			case 9:
				if(f == 1) button1.setBackgroundResource(R.drawable.buttonf);
				else if(f == 2) button2.setBackgroundResource(R.drawable.buttonf);
				else if(f == 3) button3.setBackgroundResource(R.drawable.buttonf);
				else if(f == 4) button4.setBackgroundResource(R.drawable.buttonf);
				else if(f == 5) button5.setBackgroundResource(R.drawable.buttonf);
				else if(f == 6) button6.setBackgroundResource(R.drawable.buttonf);
//				button0.setBackgroundResource(R.drawable.buttone);
//				button1.setBackgroundResource(R.drawable.buttone);
//				button2.setBackgroundResource(R.drawable.buttone);
//				button3.setBackgroundResource(R.drawable.buttone);
//				button4.setBackgroundResource(R.drawable.buttone);
//				button5.setBackgroundResource(R.drawable.buttone);
//				button6.setBackgroundResource(R.drawable.buttone);
				imageButton.setBackgroundResource(R.drawable.buttona);
				startFlag = false;
				break;
			default:
				break;
			}
			MyLog.i("reByte",reByte+"");
//		}
		
	 }
	 

	 public void  setStartListner(){
		 imageButton.setOnTouchListener(new View.OnTouchListener() {
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if(arg1.getAction() == MotionEvent.ACTION_DOWN){
						if(!startFlag)
							imageButton.setBackgroundResource(R.drawable.buttona2);
						else {
							imageButton.setBackgroundResource(R.drawable.buttond2);
						}
//						startFlag = false;
					}else if(arg1.getAction() == MotionEvent.ACTION_UP){
						
//						if(suceedFlag){
						Intent data = new Intent();
						data.putExtra("type", type);
						data.putExtra("index", index);
						data.putExtra("reByte", reByte);
						data.putExtra("flag", suceedFlag);
						setResult(RESULT_OK, data);
						if(!startFlag){
							if(reByte == 0x00)
								suceedFlag = Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x01,0x00,0x00}, DfbControlActivity.this);
							else
								suceedFlag = Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x01,reByte,0x00}, DfbControlActivity.this);
							imageButton.setBackgroundResource(R.drawable.buttona);
//							imageButton.setBackgroundResource(R.drawable.buttond);
//							startFlag = true;
						}else{
							suceedFlag = Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x01,0x00,0x00}, DfbControlActivity.this);
							imageButton.setBackgroundResource(R.drawable.buttond);
//							imageButton.setBackgroundResource(R.drawable.buttona);
//							startFlag = false;
						}
//						}
//						if(!suceedFlag){
//							imageButton.setBackgroundResource(R.drawable.buttonc);
//						}
					}
					return false;
				}
			});
	 }
//	public boolean onTouchEvent(MotionEvent event) {
//		float touchX = event.getX();
//		float touchY = event.getY();
//		if(event.getAction() == MotionEvent.ACTION_UP){
//			
//			if(touchX > 136 * widthRatio && touchX < 177 * widthRatio && 
//					touchY > 59 * heightRatio && touchY < 75 * heightRatio){
//				setBackground(0);
//			}else if(touchX > 198 * widthRatio && touchX < 243 * widthRatio && 
//					touchY > 67 * heightRatio && touchY < 87 * heightRatio){
//				setBackground(1);
//			}else if(touchX > 226 * widthRatio && touchX < 272 * widthRatio && 
//					touchY > 98 * heightRatio && touchY < 118 * heightRatio){
//				setBackground(2);
//			}else if(touchX > 73 * widthRatio && touchX < 117 * widthRatio && 
//					touchY > 67 * heightRatio && touchY < 87 * heightRatio){
//				setBackground(3);
//			}else if(touchX > 46 * widthRatio && touchX < 92 * widthRatio && 
//					touchY > 98 * heightRatio && touchY < 118 * heightRatio){
//				setBackground(4);
//			}else if(touchX > 28 * widthRatio && touchX < 84 * widthRatio && 
//					touchY > 138 * heightRatio && touchY < 158 * heightRatio){
//				setBackground(5);
//			}else if(touchX > 241 * widthRatio && touchX < 295 * widthRatio && 
//					touchY > 138 * heightRatio && touchY < 158 * heightRatio){
//				setBackground(6);
//			}
//			MyLog.i("bzWidthCenter "+bzWidthCenter, touchX+"");
//			MyLog.i("bzHeightCenter "+bzHeightCenter, touchY+"");
//			
//		}
//		return super.onTouchEvent(event);
//	 }
	class MyListener implements View.OnClickListener{
		public void onClick(View view) {
			if(view == button0){
				setSelect(0);
			}else if(view == button1){
				setSelect(1);
			}else if(view == button2){
				setSelect(2);
			}else if(view == button3){
				setSelect(3);
			}else if(view == button4){
				setSelect(4);
			}else if(view == button5){
				setSelect(5);
			}else if(view == button6){
				setSelect(6);
			}
		}
		
	}
	
	class RefreshThread extends Thread{
		boolean sflag = false;
		int value = pose;
		public void run() {
			while(!sflag){
				MyLog.i("pose",device.value2+"");
				handler.sendEmptyMessage(device.value2);
				try {
					sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	
	protected void onDestroy() {
		finish();
		if(refreshThread != null){
			refreshThread.sflag = true;
		}
		super.onDestroy();
	}
}
