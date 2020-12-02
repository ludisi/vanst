package com.vanst.client;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class XyjControlActivity extends Activity {
	 DiyButton [] diyButtons = DiyButton.XYJ_BUTTONS;
	 byte type;
	 byte index;
	 byte reByte;
	 byte geByte;
	 byte pose;
	 byte value2;
	 boolean power;
	 boolean start;
	 int f;
	 Device device;
	 ImageButton imageButton0;
	 ImageButton imageButton1;
	 ImageButton imageButton2;
	 ImageButton imageButton3;
	 ImageButton imageButton4;
	 ImageButton imageButton5;
	 ImageButton imageButton6;
	 ImageButton imageButton7;
	 ImageButton imageButton8;
	 ImageButton imageButtonz;
	 ImageButton imageButton;
	 LinearLayout linearLayout;
	 RefreshThread refreshThread;
	 float bzWidthCenter;
	 float bzHeightCenter;
	 Handler handler = new Handler(){
		 public void handleMessage(android.os.Message msg) {
			 if(msg.what / 256 == 2 || msg.what / 256 == 3)
				 setPose(msg.what % 256 ,msg.what / 256);
			 device.pose = 1;
		 };
	 };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.xyj2);
		} catch (OutOfMemoryError e) {
			Toast.makeText(XyjControlActivity.this, "ÄÚ´æ¿Õ¼ä²»×ã£¡", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		imageButton0 = (ImageButton)findViewById(R.id.xyj_bt0);
		imageButton1 = (ImageButton)findViewById(R.id.xyj_bt1);
		imageButton2 = (ImageButton)findViewById(R.id.xyj_bt2);
		imageButton3 = (ImageButton)findViewById(R.id.xyj_bt3);
		imageButton4 = (ImageButton)findViewById(R.id.xyj_bt4);
		imageButton5 = (ImageButton)findViewById(R.id.xyj_bt5);
		imageButton6 = (ImageButton)findViewById(R.id.xyj_bt6);
		imageButton7 = (ImageButton)findViewById(R.id.xyj_bt7);
		imageButton8 = (ImageButton)findViewById(R.id.xyj_bt8);
		imageButtonz = (ImageButton)findViewById(R.id.xyj_btz);
		linearLayout = (LinearLayout)findViewById(R.id.xyj);
		imageButton0.setOnClickListener(new MyListener());
		imageButton1.setOnClickListener(new MyListener());
		imageButton2.setOnClickListener(new MyListener());
		imageButton3.setOnClickListener(new MyListener());
		imageButton4.setOnClickListener(new MyListener());
		imageButton5.setOnClickListener(new MyListener());
		imageButton6.setOnClickListener(new MyListener());
		imageButton7.setOnClickListener(new MyListener());
		imageButton8.setOnClickListener(new MyListener());
		imageButtonz.setOnTouchListener(new MyListener2());
		imageButton = (ImageButton)findViewById(R.id.xyj_start);
		Intent intent = getIntent();
        if(intent != null){
	        type = intent.getByteExtra("type", (byte) 0x00);
	        index = intent.getByteExtra("index", (byte) 0x00);	
	        pose = intent.getByteExtra("pose", (byte) 0x00);
	        value2 = intent.getByteExtra("pose", (byte) 0x00);
        }
        setPose(value2,pose);
        Connect.writeControl(new byte[]{(byte)0x0f1,type,index,0x02,0x00,0x00}, XyjControlActivity.this);
//		imageButton.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//			}
//		});
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
							if(diyButton.num == 0){
								if(power){
									Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,(byte)0x0c,0x00}, XyjControlActivity.this);
								}else{
									Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,(byte)0x0b,0x00}, XyjControlActivity.this);
								}
							}else if(power && !start){
								setSelect(diyButton.num);
								Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,(byte)diyButton.num,0x00}, XyjControlActivity.this);
							}
						}
					}
				}
				// TODO Auto-generated method stub
				return false;
			}
		});
		imageButton.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					if(start){
						imageButton.setBackgroundResource(R.drawable.buttond2);
					}else{
						imageButton.setBackgroundResource(R.drawable.buttonc2);
					}
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					if(reByte == 0){
						imageButton.setBackgroundResource(R.drawable.buttonc);
						if(power){
							Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,(byte)0x0c,0x00}, XyjControlActivity.this);
						}else{
							Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,(byte)0x0b,0x00}, XyjControlActivity.this);
						}
					}else{
						if(start){
							imageButton.setBackgroundResource(R.drawable.buttond);
							Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,(byte)0x0a,0x00}, XyjControlActivity.this);
						}else{
							imageButton.setBackgroundResource(R.drawable.buttonc);
							Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,(byte)0x09,0x00}, XyjControlActivity.this);
						}
					}
				}
				
				
				return true;
			}
		});
		
		
		
		 for(Device device:MainView.devices){
				if(type == device.type && index == device.index){
					this.device = device;
				}
			}
		 refreshThread = new RefreshThread();
		 refreshThread.start();
	}
	protected void onResume() {
		super.onResume();
		Connect.activity = this;
	}
	public void setPose(int i , int j){
		if(j == 3){
			if(i < 0) i += 256;
	//		if(i != reByte){
			if(i >= 128){
				power = true;
				imageButton0.setBackgroundResource(R.drawable.buttong);
				reByte = (byte) (i % 16);
				geByte = (byte)(i % 16);
				if(i % 16 == 1){
					f = 1;
					imageButtonz.setBackgroundResource(R.drawable.zzswasher002);
					imageButton1.setBackgroundResource(R.drawable.buttonf);
					imageButton2.setBackgroundResource(R.drawable.buttone);
					imageButton3.setBackgroundResource(R.drawable.buttone);
					imageButton4.setBackgroundResource(R.drawable.buttone);
					imageButton5.setBackgroundResource(R.drawable.buttone);
					imageButton6.setBackgroundResource(R.drawable.buttone);
					imageButton7.setBackgroundResource(R.drawable.buttone);
					imageButton8.setBackgroundResource(R.drawable.buttone);
				}else if(i % 16 == 2){
					f = 2;
					imageButtonz.setBackgroundResource(R.drawable.zzswasher003);
					imageButton1.setBackgroundResource(R.drawable.buttone);
					imageButton2.setBackgroundResource(R.drawable.buttonf);
					imageButton3.setBackgroundResource(R.drawable.buttone);
					imageButton4.setBackgroundResource(R.drawable.buttone);
					imageButton5.setBackgroundResource(R.drawable.buttone);
					imageButton6.setBackgroundResource(R.drawable.buttone);
					imageButton7.setBackgroundResource(R.drawable.buttone);
					imageButton8.setBackgroundResource(R.drawable.buttone);
				}else if(i % 16 == 3){
					f = 3;
					imageButtonz.setBackgroundResource(R.drawable.zzswasher004);
					imageButton1.setBackgroundResource(R.drawable.buttone);
					imageButton2.setBackgroundResource(R.drawable.buttone);
					imageButton3.setBackgroundResource(R.drawable.buttonf);
					imageButton4.setBackgroundResource(R.drawable.buttone);
					imageButton5.setBackgroundResource(R.drawable.buttone);
					imageButton6.setBackgroundResource(R.drawable.buttone);
					imageButton7.setBackgroundResource(R.drawable.buttone);
					imageButton8.setBackgroundResource(R.drawable.buttone);
				}else if(i % 16 == 4){
					f = 4;
					imageButtonz.setBackgroundResource(R.drawable.zzswasher005);
					imageButton1.setBackgroundResource(R.drawable.buttone);
					imageButton2.setBackgroundResource(R.drawable.buttone);
					imageButton3.setBackgroundResource(R.drawable.buttone);
					imageButton4.setBackgroundResource(R.drawable.buttonf);
					imageButton5.setBackgroundResource(R.drawable.buttone);
					imageButton6.setBackgroundResource(R.drawable.buttone);
					imageButton7.setBackgroundResource(R.drawable.buttone);
					imageButton8.setBackgroundResource(R.drawable.buttone);
				}else if(i % 16 == 5){
					f = 5;
					imageButtonz.setBackgroundResource(R.drawable.zzswasher006);
					imageButton1.setBackgroundResource(R.drawable.buttone);
					imageButton2.setBackgroundResource(R.drawable.buttone);
					imageButton3.setBackgroundResource(R.drawable.buttone);
					imageButton4.setBackgroundResource(R.drawable.buttone);
					imageButton5.setBackgroundResource(R.drawable.buttonf);
					imageButton6.setBackgroundResource(R.drawable.buttone);
					imageButton7.setBackgroundResource(R.drawable.buttone);
					imageButton8.setBackgroundResource(R.drawable.buttone);
				}else if(i % 16 == 6){
					f = 6;
					imageButtonz.setBackgroundResource(R.drawable.zzswasher007);
					imageButton1.setBackgroundResource(R.drawable.buttone);
					imageButton2.setBackgroundResource(R.drawable.buttone);
					imageButton3.setBackgroundResource(R.drawable.buttone);
					imageButton4.setBackgroundResource(R.drawable.buttone);
					imageButton5.setBackgroundResource(R.drawable.buttone);
					imageButton6.setBackgroundResource(R.drawable.buttonf);
					imageButton7.setBackgroundResource(R.drawable.buttone);
					imageButton8.setBackgroundResource(R.drawable.buttone);
				}else if(i % 16 == 7){
					f = 7;
					imageButtonz.setBackgroundResource(R.drawable.zzswasher008);
					imageButton1.setBackgroundResource(R.drawable.buttone);
					imageButton2.setBackgroundResource(R.drawable.buttone);
					imageButton3.setBackgroundResource(R.drawable.buttone);
					imageButton4.setBackgroundResource(R.drawable.buttone);
					imageButton5.setBackgroundResource(R.drawable.buttone);
					imageButton6.setBackgroundResource(R.drawable.buttone);
					imageButton7.setBackgroundResource(R.drawable.buttonf);
					imageButton8.setBackgroundResource(R.drawable.buttone);
				}else if(i % 16 == 8){
					f = 8;
					imageButtonz.setBackgroundResource(R.drawable.zzswasher009);
					imageButton1.setBackgroundResource(R.drawable.buttone);
					imageButton2.setBackgroundResource(R.drawable.buttone);
					imageButton3.setBackgroundResource(R.drawable.buttone);
					imageButton4.setBackgroundResource(R.drawable.buttone);
					imageButton5.setBackgroundResource(R.drawable.buttone);
					imageButton6.setBackgroundResource(R.drawable.buttone);
					imageButton7.setBackgroundResource(R.drawable.buttone);
					imageButton8.setBackgroundResource(R.drawable.buttonf);
				}
				
				
				if(i - 128 >= 64){
					start = true;
					imageButton.setBackgroundResource(R.drawable.buttond);
					if(f == 1) imageButton1.setBackgroundResource(R.drawable.buttong);
					else if(f == 2) imageButton2.setBackgroundResource(R.drawable.buttong);
					else if(f == 3) imageButton3.setBackgroundResource(R.drawable.buttong);
					else if(f == 4) imageButton4.setBackgroundResource(R.drawable.buttong);
					else if(f == 5) imageButton5.setBackgroundResource(R.drawable.buttong);
					else if(f == 6) imageButton6.setBackgroundResource(R.drawable.buttong);
					else if(f == 7) imageButton7.setBackgroundResource(R.drawable.buttong);
					else if(f == 8) imageButton8.setBackgroundResource(R.drawable.buttong);
				}else{
					start = false;
					imageButton.setBackgroundResource(R.drawable.buttonc);
					if(f == 1) imageButton1.setBackgroundResource(R.drawable.buttonf);
					else if(f == 2) imageButton2.setBackgroundResource(R.drawable.buttonf);
					else if(f == 3) imageButton3.setBackgroundResource(R.drawable.buttonf);
					else if(f == 4) imageButton4.setBackgroundResource(R.drawable.buttonf);
					else if(f == 5) imageButton5.setBackgroundResource(R.drawable.buttonf);
					else if(f == 6) imageButton6.setBackgroundResource(R.drawable.buttonf);
					else if(f == 7) imageButton7.setBackgroundResource(R.drawable.buttonf);
					else if(f == 8) imageButton8.setBackgroundResource(R.drawable.buttonf);
//					imageButton1.setBackgroundResource(R.drawable.buttone);
//					imageButton2.setBackgroundResource(R.drawable.buttone);
//					imageButton3.setBackgroundResource(R.drawable.buttone);
//					imageButton4.setBackgroundResource(R.drawable.buttone);
//					imageButton5.setBackgroundResource(R.drawable.buttone);
//					imageButton6.setBackgroundResource(R.drawable.buttone);
//					imageButton7.setBackgroundResource(R.drawable.buttone);
//					imageButton8.setBackgroundResource(R.drawable.buttone);
				}
			}else {
				power =false;
				imageButton0.setBackgroundResource(R.drawable.buttone);
				imageButton1.setBackgroundResource(R.drawable.buttone);
				imageButton2.setBackgroundResource(R.drawable.buttone);
				imageButton3.setBackgroundResource(R.drawable.buttone);
				imageButton4.setBackgroundResource(R.drawable.buttone);
				imageButton5.setBackgroundResource(R.drawable.buttone);
				imageButton6.setBackgroundResource(R.drawable.buttone);
				imageButton7.setBackgroundResource(R.drawable.buttone);
				imageButton8.setBackgroundResource(R.drawable.buttone);
			}
		}else{
			if(i == 1){
				reByte = (byte) i;
				geByte = (byte) i;
				f = i;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher002);
//				imageButton0.setBackgroundResource(R.drawable.buttonf);
				imageButton1.setBackgroundResource(R.drawable.buttonf);
				imageButton2.setBackgroundResource(R.drawable.buttone);
				imageButton3.setBackgroundResource(R.drawable.buttone);
				imageButton4.setBackgroundResource(R.drawable.buttone);
				imageButton5.setBackgroundResource(R.drawable.buttone);
				imageButton6.setBackgroundResource(R.drawable.buttone);
				imageButton7.setBackgroundResource(R.drawable.buttone);
				imageButton8.setBackgroundResource(R.drawable.buttone);
			}else if(i == 2){
				reByte = (byte) i;
				geByte = (byte) i;
				f = i;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher003);
//				imageButton0.setBackgroundResource(R.drawable.buttonf);
				imageButton1.setBackgroundResource(R.drawable.buttone);
				imageButton2.setBackgroundResource(R.drawable.buttonf);
				imageButton3.setBackgroundResource(R.drawable.buttone);
				imageButton4.setBackgroundResource(R.drawable.buttone);
				imageButton5.setBackgroundResource(R.drawable.buttone);
				imageButton6.setBackgroundResource(R.drawable.buttone);
				imageButton7.setBackgroundResource(R.drawable.buttone);
				imageButton8.setBackgroundResource(R.drawable.buttone);
			}else if(i == 3){
				reByte = (byte) i;
				geByte = (byte) i;
				f = i;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher004);
//				imageButton0.setBackgroundResource(R.drawable.buttonf);
				imageButton1.setBackgroundResource(R.drawable.buttone);
				imageButton2.setBackgroundResource(R.drawable.buttone);
				imageButton3.setBackgroundResource(R.drawable.buttonf);
				imageButton4.setBackgroundResource(R.drawable.buttone);
				imageButton5.setBackgroundResource(R.drawable.buttone);
				imageButton6.setBackgroundResource(R.drawable.buttone);
				imageButton7.setBackgroundResource(R.drawable.buttone);
				imageButton8.setBackgroundResource(R.drawable.buttone);
			}else if(i == 4){
				reByte = (byte) i;
				geByte = (byte) i;
				f = i;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher005);
//				imageButton0.setBackgroundResource(R.drawable.buttonf);
				imageButton1.setBackgroundResource(R.drawable.buttone);
				imageButton2.setBackgroundResource(R.drawable.buttone);
				imageButton3.setBackgroundResource(R.drawable.buttone);
				imageButton4.setBackgroundResource(R.drawable.buttonf);
				imageButton5.setBackgroundResource(R.drawable.buttone);
				imageButton6.setBackgroundResource(R.drawable.buttone);
				imageButton7.setBackgroundResource(R.drawable.buttone);
				imageButton8.setBackgroundResource(R.drawable.buttone);
			}else if(i == 5){
				reByte = (byte) i;
				geByte = (byte) i;
				f = i;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher006);
//				imageButton0.setBackgroundResource(R.drawable.buttonf);
				imageButton1.setBackgroundResource(R.drawable.buttone);
				imageButton2.setBackgroundResource(R.drawable.buttone);
				imageButton3.setBackgroundResource(R.drawable.buttone);
				imageButton4.setBackgroundResource(R.drawable.buttone);
				imageButton5.setBackgroundResource(R.drawable.buttonf);
				imageButton6.setBackgroundResource(R.drawable.buttone);
				imageButton7.setBackgroundResource(R.drawable.buttone);
				imageButton8.setBackgroundResource(R.drawable.buttone);
			}else if(i == 6){
				reByte = (byte) i;
				geByte = (byte) i;
				f = i;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher007);
//				imageButton0.setBackgroundResource(R.drawable.buttonf);
				imageButton1.setBackgroundResource(R.drawable.buttone);
				imageButton2.setBackgroundResource(R.drawable.buttone);
				imageButton3.setBackgroundResource(R.drawable.buttone);
				imageButton4.setBackgroundResource(R.drawable.buttone);
				imageButton5.setBackgroundResource(R.drawable.buttone);
				imageButton6.setBackgroundResource(R.drawable.buttonf);
				imageButton7.setBackgroundResource(R.drawable.buttone);
				imageButton8.setBackgroundResource(R.drawable.buttone);
			}else if(i == 7){
				reByte = (byte) i;
				geByte = (byte) i;
				f = i;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher008);
//				imageButton0.setBackgroundResource(R.drawable.buttonf);
				imageButton1.setBackgroundResource(R.drawable.buttone);
				imageButton2.setBackgroundResource(R.drawable.buttone);
				imageButton3.setBackgroundResource(R.drawable.buttone);
				imageButton4.setBackgroundResource(R.drawable.buttone);
				imageButton5.setBackgroundResource(R.drawable.buttone);
				imageButton6.setBackgroundResource(R.drawable.buttone);
				imageButton7.setBackgroundResource(R.drawable.buttonf);
				imageButton8.setBackgroundResource(R.drawable.buttone);
			}else if(i == 8){
				reByte = (byte) i;
				geByte = (byte) i;
				f = i;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher009);
//				imageButton0.setBackgroundResource(R.drawable.buttonf);
				imageButton1.setBackgroundResource(R.drawable.buttone);
				imageButton2.setBackgroundResource(R.drawable.buttone);
				imageButton3.setBackgroundResource(R.drawable.buttone);
				imageButton4.setBackgroundResource(R.drawable.buttone);
				imageButton5.setBackgroundResource(R.drawable.buttone);
				imageButton6.setBackgroundResource(R.drawable.buttone);
				imageButton7.setBackgroundResource(R.drawable.buttone);
				imageButton8.setBackgroundResource(R.drawable.buttonf);
			}else if(i == 9){
				start = true;
				imageButton.setBackgroundResource(R.drawable.buttond);
				if(f == 1) imageButton1.setBackgroundResource(R.drawable.buttong);
				else if(f == 2) imageButton2.setBackgroundResource(R.drawable.buttong);
				else if(f == 3) imageButton3.setBackgroundResource(R.drawable.buttong);
				else if(f == 4) imageButton4.setBackgroundResource(R.drawable.buttong);
				else if(f == 5) imageButton5.setBackgroundResource(R.drawable.buttong);
				else if(f == 6) imageButton6.setBackgroundResource(R.drawable.buttong);
				else if(f == 7) imageButton7.setBackgroundResource(R.drawable.buttong);
				else if(f == 8) imageButton8.setBackgroundResource(R.drawable.buttong);
//				if(reByte == 1){
//					imageButton1.setBackgroundResource(R.drawable.buttonf);
//					imageButton2.setBackgroundResource(R.drawable.buttone);
//					imageButton3.setBackgroundResource(R.drawable.buttone);
//					imageButton4.setBackgroundResource(R.drawable.buttone);
//					imageButton5.setBackgroundResource(R.drawable.buttone);
//					imageButton6.setBackgroundResource(R.drawable.buttone);
//					imageButton7.setBackgroundResource(R.drawable.buttone);
//					imageButton8.setBackgroundResource(R.drawable.buttone);
//				}else if(reByte == 2){
//					imageButton1.setBackgroundResource(R.drawable.buttone);
//					imageButton2.setBackgroundResource(R.drawable.buttonf);
//					imageButton3.setBackgroundResource(R.drawable.buttone);
//					imageButton4.setBackgroundResource(R.drawable.buttone);
//					imageButton5.setBackgroundResource(R.drawable.buttone);
//					imageButton6.setBackgroundResource(R.drawable.buttone);
//					imageButton7.setBackgroundResource(R.drawable.buttone);
//					imageButton8.setBackgroundResource(R.drawable.buttone);
//				}else if(reByte == 3){
//					imageButton1.setBackgroundResource(R.drawable.buttone);
//					imageButton2.setBackgroundResource(R.drawable.buttone);
//					imageButton3.setBackgroundResource(R.drawable.buttonf);
//					imageButton4.setBackgroundResource(R.drawable.buttone);
//					imageButton5.setBackgroundResource(R.drawable.buttone);
//					imageButton6.setBackgroundResource(R.drawable.buttone);
//					imageButton7.setBackgroundResource(R.drawable.buttone);
//					imageButton8.setBackgroundResource(R.drawable.buttone);
//				}else if(reByte == 4){
//					imageButton1.setBackgroundResource(R.drawable.buttone);
//					imageButton2.setBackgroundResource(R.drawable.buttone);
//					imageButton3.setBackgroundResource(R.drawable.buttone);
//					imageButton4.setBackgroundResource(R.drawable.buttonf);
//					imageButton5.setBackgroundResource(R.drawable.buttone);
//					imageButton6.setBackgroundResource(R.drawable.buttone);
//					imageButton7.setBackgroundResource(R.drawable.buttone);
//					imageButton8.setBackgroundResource(R.drawable.buttone);
//				}else if(reByte == 5){
//					imageButton1.setBackgroundResource(R.drawable.buttone);
//					imageButton2.setBackgroundResource(R.drawable.buttone);
//					imageButton3.setBackgroundResource(R.drawable.buttone);
//					imageButton4.setBackgroundResource(R.drawable.buttone);
//					imageButton5.setBackgroundResource(R.drawable.buttonf);
//					imageButton6.setBackgroundResource(R.drawable.buttone);
//					imageButton7.setBackgroundResource(R.drawable.buttone);
//					imageButton8.setBackgroundResource(R.drawable.buttone);
//				}else if(reByte == 6){
//					imageButton1.setBackgroundResource(R.drawable.buttone);
//					imageButton2.setBackgroundResource(R.drawable.buttone);
//					imageButton3.setBackgroundResource(R.drawable.buttone);
//					imageButton4.setBackgroundResource(R.drawable.buttone);
//					imageButton5.setBackgroundResource(R.drawable.buttone);
//					imageButton6.setBackgroundResource(R.drawable.buttonf);
//					imageButton7.setBackgroundResource(R.drawable.buttone);
//					imageButton8.setBackgroundResource(R.drawable.buttone);
//				}else if(reByte == 7){
//					imageButton1.setBackgroundResource(R.drawable.buttone);
//					imageButton2.setBackgroundResource(R.drawable.buttone);
//					imageButton3.setBackgroundResource(R.drawable.buttone);
//					imageButton4.setBackgroundResource(R.drawable.buttone);
//					imageButton5.setBackgroundResource(R.drawable.buttone);
//					imageButton6.setBackgroundResource(R.drawable.buttone);
//					imageButton7.setBackgroundResource(R.drawable.buttonf);
//					imageButton8.setBackgroundResource(R.drawable.buttone);
//				}else if(reByte == 8){
//					imageButton1.setBackgroundResource(R.drawable.buttone);
//					imageButton2.setBackgroundResource(R.drawable.buttone);
//					imageButton3.setBackgroundResource(R.drawable.buttone);
//					imageButton4.setBackgroundResource(R.drawable.buttone);
//					imageButton5.setBackgroundResource(R.drawable.buttone);
//					imageButton6.setBackgroundResource(R.drawable.buttone);
//					imageButton7.setBackgroundResource(R.drawable.buttone);
//					imageButton8.setBackgroundResource(R.drawable.buttonf);
//				}
			}else if(i == 10){
				start = false;
				imageButton.setBackgroundResource(R.drawable.buttonc);
				setSelect(geByte);
				if(f == 1) imageButton1.setBackgroundResource(R.drawable.buttonf);
				else if(f == 2) imageButton2.setBackgroundResource(R.drawable.buttonf);
				else if(f == 3) imageButton3.setBackgroundResource(R.drawable.buttonf);
				else if(f == 4) imageButton4.setBackgroundResource(R.drawable.buttonf);
				else if(f == 5) imageButton5.setBackgroundResource(R.drawable.buttonf);
				else if(f == 6) imageButton6.setBackgroundResource(R.drawable.buttonf);
				else if(f == 7) imageButton7.setBackgroundResource(R.drawable.buttonf);
				else if(f == 8) imageButton8.setBackgroundResource(R.drawable.buttonf);
//				imageButton1.setBackgroundResource(R.drawable.buttone);
//				imageButton1.setBackgroundResource(R.drawable.buttone);
//				imageButton2.setBackgroundResource(R.drawable.buttone);
//				imageButton3.setBackgroundResource(R.drawable.buttone);
//				imageButton4.setBackgroundResource(R.drawable.buttone);
//				imageButton5.setBackgroundResource(R.drawable.buttone);
//				imageButton6.setBackgroundResource(R.drawable.buttone);
//				imageButton7.setBackgroundResource(R.drawable.buttone);
//				imageButton8.setBackgroundResource(R.drawable.buttone);
			}else if(i == 11){
				power = true;
				start = false;
				imageButton.setBackgroundResource(R.drawable.buttonc);
				imageButton0.setBackgroundResource(R.drawable.buttong);
				reByte = 0;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher001);
			}else if(i == 12){
				power = false;
				start = false;
				imageButton.setBackgroundResource(R.drawable.buttonc);
				imageButton0.setBackgroundResource(R.drawable.buttone);
				imageButton1.setBackgroundResource(R.drawable.buttone);
				imageButton2.setBackgroundResource(R.drawable.buttone);
				imageButton3.setBackgroundResource(R.drawable.buttone);
				imageButton4.setBackgroundResource(R.drawable.buttone);
				imageButton5.setBackgroundResource(R.drawable.buttone);
				imageButton6.setBackgroundResource(R.drawable.buttone);
				imageButton7.setBackgroundResource(R.drawable.buttone);
				imageButton8.setBackgroundResource(R.drawable.buttone);
				reByte = 0;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher001);
			}
		}
	 }
	
	
	public void setSelect(int i){
		if(i != reByte){
			switch (i) {
			case 0:
				reByte = (byte)0x00;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher001);
				break;
			case 1:
				reByte = (byte)0x01;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher002);
				break;
			case 2:
				reByte = (byte)0x02;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher003);
				break;
			case 3:
				reByte = (byte)0x03;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher004);
				break;
			case 4:
				reByte = (byte)0x04;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher005);
				break;
			case 5:
				reByte = (byte)0x05;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher006);
				break;
			case 6:
				reByte = (byte)0x06;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher007);
				break;
			case 7:
				reByte = (byte)0x07;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher008);
				break;
			case 8:
				reByte = (byte)0x08;
				imageButtonz.setBackgroundResource(R.drawable.zzswasher009);
				break;
			default:
				break;
			}
			MyLog.i("reByte",reByte+"");
		}
	}
	class MyListener2 implements View.OnTouchListener{

		public boolean onTouch(View arg0, MotionEvent event) {
			if(power &&!start){
			bzWidthCenter = imageButtonz.getWidth()/2;
			bzHeightCenter = imageButtonz.getHeight()/2;
			float touchX = event.getX();
			float touchY = event.getY();
			double angle = Math.atan((touchX - bzWidthCenter)/(touchY - bzHeightCenter))*180/3.14;
			if(touchY - bzHeightCenter >= 0){
				if(touchX - bzWidthCenter < 0){
					setSelect(0);
				}else{
					setSelect(8);
				}
			}else if(angle < -75){
				setSelect(7);
			}else if(angle < -45){
				setSelect(6);
			}else if(angle < -15){
				setSelect(5);
			}else if(angle < 15){
				setSelect(4);
			}else if(angle < 45){
				setSelect(3);
			}else if(angle < 75){
				setSelect(2);
			}else{
				setSelect(1);
			}
			if(event.getAction() == MotionEvent.ACTION_UP){
				Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,(byte)reByte,0x00}, XyjControlActivity.this);
			}
			}
//			MyLog.i("bzWidthCenter "+bzWidthCenter, touchX+"");
//			MyLog.i("bzHeightCenter "+bzHeightCenter, touchY+"");
			return false;
		}
		 
	 }
	class MyListener implements View.OnClickListener{
		public void onClick(View view) {
			if(view == imageButton0){
				setSelect(0);
			}else if(view == imageButton1){
				setSelect(1);
			}else if(view == imageButton2){
				setSelect(2);
			}else if(view == imageButton3){
				setSelect(3);
			}else if(view == imageButton4){
				setSelect(4);
			}else if(view == imageButton5){
				setSelect(5);
			}else if(view == imageButton6){
				setSelect(6);
			}else if(view == imageButton7){
				setSelect(7);
			}else if(view == imageButton8){
				setSelect(8);
			}
			Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,(byte)reByte,0x00}, XyjControlActivity.this);
		}
		
	}
	class RefreshThread extends Thread{
		boolean sflag = false;
		int value = pose;
		public void run() {
			while(!sflag){
				MyLog.i("pose",device.value2+"");
				if(device.value2 < 0){
					int i = device.value2+256;
					handler.sendEmptyMessage(device.pose * 256 + i);
				}else
					handler.sendEmptyMessage(device.pose * 256 + device.value2);
				try {
					sleep(1000);
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
//	public void  setStartListner(){
//		 imageButton.setOnTouchListener(new View.OnTouchListener() {
//				public boolean onTouch(View arg0, MotionEvent arg1) {
//					if(arg1.getAction() == MotionEvent.ACTION_DOWN){
//						imageButton.setBackgroundResource(R.drawable.start2);
//					}else if(arg1.getAction() == MotionEvent.ACTION_UP){
////						suceedFlag = Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x01,reByte,0x00}, DfbControlActivity.this);
//////						if(suceedFlag){
////						Intent data = new Intent();
////						data.putExtra("type", type);
////						data.putExtra("index", index);
////						data.putExtra("reByte", reByte);
////						data.putExtra("flag", suceedFlag);
////						setResult(RESULT_OK, data);
//////						}
////						if(!suceedFlag){
////							imageButton.setBackgroundResource(R.drawable.start);
////						}
//					}
//					return false;
//				}
//			});
//	 }
}
