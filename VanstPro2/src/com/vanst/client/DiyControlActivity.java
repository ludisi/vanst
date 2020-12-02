package com.vanst.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DiyControlActivity extends Activity {
	FrameLayout frameLayout;
	ImageView background;
	DiyIRControl diyIRControl;
	RefreshThread refreshThread;
	ImageView imageKey;
	Map<String, Object> keyMap;
	ArrayList<Map<String, Object>> keyList = new ArrayList<Map<String,Object>>();
	Bitmap bg;
	byte type;
	byte index;
	int bitmapWidth;
	int bitmapheight;
	int x1;
	int y1;
	int x2;
	int y2;
	int screenWidth;
	int screenHeight;
	TextView textView;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0x01){
				int buttonRect = screenWidth / 30;
				for(Map<String, Object> map:keyList){
					if((Boolean)map.get("get")){
						((ImageView)map.get("view")).layout((Integer)map.get("x")-(buttonRect), (Integer)map.get("y")-(buttonRect), 
							(Integer)map.get("x")+(buttonRect), (Integer)map.get("y")+(buttonRect));
					}else {
						((ImageView)map.get("view")).layout(0,0,0,0);
					}
				}
			}else if(msg.what == 0x02){
				keyList = new ArrayList<Map<String,Object>>();
				int key = 0;
				for(int[] is:diyIRControl.instructions){
					imageKey = new ImageView(DiyControlActivity.this);
					imageKey.setBackgroundResource(R.drawable.touming);
					FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(0,0);
					frameLayout.addView(imageKey,layoutParams);
					keyMap = new HashMap<String, Object>();
					keyMap.put("view", imageKey);
					
					keyMap.put("x", ((is[0] + is[2])/2 - x1) * background.getWidth()/ (x2 - x1));
					keyMap.put("y", ((is[1] + is[3])/2 - y1) * background.getHeight()/ (y2 - y1));
					keyMap.put("get", true);
					keyMap.put("key", key);
					key++;
					keyList.add(keyMap);
					imageKey.setOnTouchListener(new View.OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							for(Map<String, Object> map:keyList){
								if((ImageView)map.get("view")  == v){
									if(event.getAction() == MotionEvent.ACTION_DOWN){
										v.setBackgroundResource(R.drawable.kuang);
									}else if(event.getAction() == MotionEvent.ACTION_UP){
										v.setBackgroundResource(R.drawable.touming);
										Connect.IRControl(new byte[]{(byte)0xf0,(byte)type,(byte)index,0x02,(byte)((int)(Integer)map.get("key")),0x00}, DiyControlActivity.this);
									}
								}
							}
							return true;
						}
					});
				}
			}
		};
	};
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diycontro);
		WindowManager wManager=getWindowManager();
		Display display=wManager.getDefaultDisplay();
		screenHeight=display.getHeight();
		screenWidth=display.getWidth();
		frameLayout = (FrameLayout)findViewById(R.id.diycontro_frame);
		background = (ImageView)findViewById(R.id.diycontrol_bg);
		textView = (TextView)findViewById(R.id.diycontrol_remark);
		Intent intent = getIntent();
		if(intent !=null){
			type = intent.getByteExtra("type", (byte) 0x00);
			index = intent.getByteExtra("index", (byte) 0x00);
		}
		diyIRControl = DiyIRControl.load(index);
		if(diyIRControl == null){
			Toast.makeText(DiyControlActivity.this, "没有该自定义设备，请先设置", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		Room.optReadBitmap.inSampleSize = 1;
		Bitmap diyIrBackground = BitmapFactory.decodeByteArray(diyIRControl.backgroud, 0, diyIRControl.backgroud.length, Room.optReadBitmap);
		x1 = diyIRControl.backgroudrect[0];
		y1 = diyIRControl.backgroudrect[1];
		x2 = diyIRControl.backgroudrect[2];
		y2 = diyIRControl.backgroudrect[3];
		bg = Bitmap.createBitmap(diyIrBackground, x1, y1, x2-x1, y2-y1);
		bitmapWidth = diyIrBackground.getWidth();
		bitmapheight = diyIrBackground.getHeight();
		textView.setText(diyIRControl.remark);
		background.setImageBitmap(bg);
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
		refreshThread = new RefreshThread();
		refreshThread.start();
		
	}
	@Override
	protected void onResume() {
		Connect.activity = this;
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		if(refreshThread != null) refreshThread.sflag = true;
		super.onDestroy();
	}
	class RefreshThread extends Thread{
		boolean sflag = false;
		@Override
		public void run() {
			while(!sflag){
				try {
					sleep(100);
					handler.sendEmptyMessage(0x01);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			super.run();
		}
	}
}
