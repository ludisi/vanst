package com.vanst.client;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ListenControlActivity extends Activity{
	ImageView bg;
	ImageView imageView;
	int width;
	int height;
	byte type;
	byte index;
	byte value2;
	Device device;
	RefreshThread refreshThread;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			width = bg.getWidth();
			height = bg.getHeight();
			MyLog.i("width",bg.getWidth()+"");
			MyLog.i("height",bg.getHeight()+"");
			MyLog.i("what",msg.what+"");
			int key = msg.what;
			if(key < 0) key = 0;
			if(key > 11) key = 11;
			imageView.layout(width * 112 / 250, (int)(height * (235 - ((235 - 87) * key / 11) - 18 )/ 400), width * 138 / 250, 
												(int)(height * (235 - ((235 - 87) * key / 11) + 18 )/ 400));
		};
	};
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listen);
		bg = (ImageView)findViewById(R.id.listen_bg);
		imageView = (ImageView)findViewById(R.id.listen_key);
		Intent intent = getIntent();
		if(intent != null){
			type = intent.getByteExtra("type", (byte)0x00);
			index = intent.getByteExtra("index", (byte)0x00);
			value2 = intent.getByteExtra("value2", (byte)0x00);
			for(Device device:MainView.devices){
				if(type == device.type && index == device.index){
					this.device = device;
				}
			}
			refreshThread = new RefreshThread();
			refreshThread.start();
		}
		new Thread(){
			@Override
			public void run() {
				try {
					sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(value2 < 0) handler.sendEmptyMessage(0);
				else handler.sendEmptyMessage(value2);
				super.run();
			}
		}.start();
		
		bg.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				width = bg.getWidth();
				height = bg.getHeight();
				if(event.getAction() == MotionEvent.ACTION_MOVE){
					float y = event.getY() * 400 / height;
					if(y < 87) y = 87;
					if(y > 235) y = 235;
					imageView.layout(width * 112 / 250, (int)(height * ( y - 18) / 400) , width * 138 / 250, (int)(height * (y + 18) /400));
//					imageView.layout(width * 111 / 250, (int) (event.getY() -27) , width * 137 / 250, (int)(event.getY() +27));
//					MyLog.i("height",height+"");
//					MyLog.i("Y", event.getY()+"");
//					MyLog.i("y", y +"");
//					MyLog.i("la" ,height * y / 400+"");
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					int key = (int)(235 - event.getY() * 400 /height ) * 11/( 235 - 87);
					if(key < 0) key = 0;
					if(key > 11) key = 11;
					MyLog.i("key",key+"");
					Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,(byte)key,0x00}, ListenControlActivity.this);
				}
				return true;
			}
		});
	
	}
	@Override
	protected void onResume() {
		super.onResume();
		Connect.activity = this;
	}
	@Override
	protected void onDestroy() {
		refreshThread.sflag = true;
		super.onDestroy();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP){
			finish();
		}
		return super.onTouchEvent(event);
	}
	class RefreshThread extends Thread{
		boolean sflag = false;
		int value = value2;
		public void run() {
			Connect.writeControl(new byte[]{(byte)0xf1,type,index,0x00,0x00,0x00}, ListenControlActivity.this);
			while(!sflag){
				if(value != device.value2){
					value = device.value2;
					MyLog.i("value",value+"");
					if(device != null) handler.sendEmptyMessage(value);
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
