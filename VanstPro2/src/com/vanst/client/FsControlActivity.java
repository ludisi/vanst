package com.vanst.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;

public class FsControlActivity extends Activity {
	byte pose;
	byte value2;
	byte linValue2;
	byte type;
	byte index;
	Device device;
	ImageButton zero;
	ImageButton one;
	ImageButton two;
	ImageButton three;
	ImageButton zp;
//	Button bigButton;
//	Button onButton;
//	Button offButton;
//	Button smallbButton;
	RefreshThread refreshThread;
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == 1){
				if(device.pose ==0 || device.value2 == 0x0a){
					zp.setBackgroundResource(R.drawable.dfb_z5);
					zero.setImageResource(R.drawable.fs_off2);
					one.setImageResource(R.drawable.fs_one1);
					two.setImageResource(R.drawable.fs_two1);
					three.setImageResource(R.drawable.fs_three1);
				}else if(device.value2 == 0x05){
					zp.setBackgroundResource(R.drawable.dfb_z3);
					zero.setImageResource(R.drawable.fs_off1);
					one.setImageResource(R.drawable.fs_one2);
					two.setImageResource(R.drawable.fs_two1);
					three.setImageResource(R.drawable.fs_three1);
				}else if(device.value2 == 0x03){
					zp.setBackgroundResource(R.drawable.dfb_z1);
					zero.setImageResource(R.drawable.fs_off1);
					one.setImageResource(R.drawable.fs_one1);
					two.setImageResource(R.drawable.fs_two2);
					three.setImageResource(R.drawable.fs_three1);
				}else if(device.value2 == 0x01){
					zp.setBackgroundResource(R.drawable.dfb_z6);
					zero.setImageResource(R.drawable.fs_off1);
					one.setImageResource(R.drawable.fs_one1);
					two.setImageResource(R.drawable.fs_two1);
					three.setImageResource(R.drawable.fs_three2);
				}
			}
		}
	};
	private void refresh(){
		if(device.pose == 0 || linValue2 == 0x0a){
			zp.setBackgroundResource(R.drawable.dfb_z5);
			zero.setImageResource(R.drawable.fs_off2);
			one.setImageResource(R.drawable.fs_one1);
			two.setImageResource(R.drawable.fs_two1);
			three.setImageResource(R.drawable.fs_three1);
		}else if(linValue2 == 0x05){
			zp.setBackgroundResource(R.drawable.dfb_z3);
			zero.setImageResource(R.drawable.fs_off1);
			one.setImageResource(R.drawable.fs_one2);
			two.setImageResource(R.drawable.fs_two1);
			three.setImageResource(R.drawable.fs_three1);
		}else if(linValue2 == 0x03){
			zp.setBackgroundResource(R.drawable.dfb_z1);
			zero.setImageResource(R.drawable.fs_off1);
			one.setImageResource(R.drawable.fs_one1);
			two.setImageResource(R.drawable.fs_two2);
			three.setImageResource(R.drawable.fs_three1);
		}else if(linValue2 == 0x01){
			zp.setBackgroundResource(R.drawable.dfb_z6);
			zero.setImageResource(R.drawable.fs_off1);
			one.setImageResource(R.drawable.fs_one1);
			two.setImageResource(R.drawable.fs_two1);
			three.setImageResource(R.drawable.fs_three2);
		}
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fs);
		zero = (ImageButton)findViewById(R.id.fs_0);
		one = (ImageButton)findViewById(R.id.fs_1);
		two = (ImageButton)findViewById(R.id.fs_2);
		three = (ImageButton)findViewById(R.id.fs_3);
		zp = (ImageButton)findViewById(R.id.fs_z);
//		bigButton = (Button)findViewById(R.id.fs_big);
//		smallbButton = (Button)findViewById(R.id.fs_small);
//		onButton = (Button)findViewById(R.id.fs_on);
//		offButton = (Button)findViewById(R.id.fs_off);
		Intent intent = getIntent();
		if(intent != null){
			type = intent.getByteExtra("type", (byte)0x00);
			index = intent.getByteExtra("index", (byte)0x00);
			pose = intent.getByteExtra("pose", (byte)0xff);
			value2 = intent.getByteExtra("value2", (byte)0x0a);
			linValue2 = value2;
//			linValue2 = 0x0a;
			for(Device device:MainView.devices){
				if(type == device.type && index == device.index){
					this.device = device;
				}
			}
			refreshThread = new RefreshThread();
			refreshThread.start();
		}
//		bigButton.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View arg0) {
//				if(linValue2 == 0x0a){
//					
//				}else if(linValue2 == 0x01){
//					
//				}else{
//					linValue2 -= 2;
//				}
//
//				Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,linValue2,0x00}, FsControlActivity.this);
//				refresh();
//			}
//		});
//		smallbButton.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View arg0) {
//				if(linValue2 == 0x0a){
//					
//				}else if(linValue2 == 0x05){
//					linValue2 = 0x0a;
//				}else{
//					linValue2 += 2;
//				}
//				Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,linValue2,0x00}, FsControlActivity.this);
//				refresh();
//			}
//		});
//		onButton.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View arg0) {
//				linValue2 = 0x01;
//				Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x01,0x00,0x00}, FsControlActivity.this);
//				refresh();
//			}
//		});
//		offButton.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View arg0) {
//				linValue2 = 0x0a;
//				Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x00,0x00,0x00}, FsControlActivity.this);
//				refresh();
//			}
//		});
		
		zero.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				linValue2 = 0x0a;
				Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x00,0x00,0x00}, FsControlActivity.this);
				refresh();
			}
		});
		three.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(device.pose == 0 || linValue2 == 0x0a){
					Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x01,0x00,0x00}, FsControlActivity.this);
				}else{
					Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,0x01,0x00}, FsControlActivity.this);
				}
				linValue2 = 0x01;
				refresh();
			}
		});
		two.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(device.pose == 0 || linValue2 == 0x0a){
					Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x01,0x00,0x00}, FsControlActivity.this);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				linValue2 = 0x03;
				Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,0x03,0x00}, FsControlActivity.this);
				refresh();
			}
		});
		one.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(device.pose == 0||linValue2 == 0x0a){
					Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x01,0x00,0x00}, FsControlActivity.this);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				linValue2 = 0x05;
				Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,0x05,0x00}, FsControlActivity.this);
				refresh();
			}
		});
	}
	protected void onResume() {
		super.onResume();
		Connect.activity = this;
	}
	class RefreshThread extends Thread{
		boolean sflag = false;
		int value;
		public void run() {
			while(!sflag){
				if(value != device.value2 || Connect.refreshPose){
					Connect.refreshPose = false;
					value = (byte) device.value2;
					linValue2 = (byte) device.value2;
					if(device != null) handler.sendEmptyMessage(0x01);
				}
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
}
