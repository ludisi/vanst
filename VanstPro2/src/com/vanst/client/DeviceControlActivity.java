package com.vanst.client;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.vanst.client.DeviceControlView.Instruction;

public class DeviceControlActivity extends Activity {
	int view;
	ImageView imageView;
	boolean isControl;
	static DeviceControlView deviceControlView;
	byte type;
	byte index;
	Device device;
	ImageView imageView2;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			imageView2.layout(0,0,0,0);
//			imageView2.setVisibility(View.GONE);
			isControl = false;
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if(intent !=null){
			type = intent.getByteExtra("type", (byte) 0x00);
	        index = intent.getByteExtra("index", (byte) 0x00);
		}
		for(Device device:MainView.devices){
			if(type == device.type && index == device.index){
				this.device = device;
			}
		}
		
//		deviceControlView = new DeviceControlView(type, index);
//		Instruction [] instructions = new Instruction[4];
//		ReInstruction [] reInstructions = new ReInstruction[1];
//		byte[][] backgroudBitmaps = new byte[][]{bitmapToBytes(BitmapFactory.decodeResource(getResources(), R.drawable.diyjj)),
//												bitmapToBytes(BitmapFactory.decodeResource(getResources(), R.drawable.kt_bg))};
//		instructions[0] = deviceControlView.createInstruction(0, 0,new byte[]{(byte)0xf0,1,1,1,0,0}, "开一号灯！", new int[]{37,98,109,169}, null, null);
//		instructions[1] = deviceControlView.createInstruction(0, 0,new byte[]{(byte)0xf0,1,1,0,0,0}, "关一号灯！", new int[]{142,98,212,169}, null, null);
//		instructions[2] = deviceControlView.createInstruction(0, 1,null, "切换到二号界面！", new int[]{37,192,109,262}, null, null);
//		instructions[3] = deviceControlView.createInstruction(1, 0,null, "切换到一号界面！", new int[]{0,0,200,200}, null, null);
//		deviceControlView.instructions = instructions;
//		deviceControlView.reInstructions = reInstructions;
//		deviceControlView.backgroundBitmaps = backgroudBitmaps;
//		DeviceControlView.saveControlView(deviceControlView, type, index);
		deviceControlView = DeviceControlView.readControlView(type, index);
		if(deviceControlView == null){
			Toast.makeText(this, "没有该自定义设备的操作界面！", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
//		view = new View(this);
//		view.setBackgroundDrawable(new BitmapDrawable(bitmap));
////		view.setBackgroundDrawable(new BitmapDrawable(deviceControlView.backgroundBitmaps[0]));
//		LayoutParams parames = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		parames.height = bitmap.getHeight();
//		parames.width = bitmap.getWidth();
//		setContentView(view,parames);
		setContentView(R.layout.diy);
		imageView2 = (ImageView)findViewById(R.id.diy_view2);
		imageView2.setBackgroundResource(R.drawable.kuang);
		imageView = (ImageView)findViewById(R.id.diy_view);
		imageView.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeByteArray(deviceControlView.backgroundBitmaps[0], 0, deviceControlView.backgroundBitmaps[0].length,Room.optReadBitmap)));
		imageView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {  
				MyLog.i("XY","X:"+event.getX()+" Y"+event.getY());
				int width = imageView.getWidth();
				int height = imageView.getHeight();
				BitmapFactory.decodeByteArray(deviceControlView.backgroundBitmaps[view], 0, deviceControlView.backgroundBitmaps[view].length, Room.optReadBound);
				int bitmapWidth = Room.optReadBound.outWidth;
				int bitmapHeight = Room.optReadBound.outHeight;
				MyLog.i("XXX","W1:"+width+" H1"+height +" W2"+bitmapWidth+" H2"+bitmapHeight);
				if(!isControl){
//					imageView2.layout((int)(event.getX()-20),(int)( event.getY()-20), (int)(event.getX()+20), (int)(event.getY()+20));
					if(event.getAction() == MotionEvent.ACTION_DOWN && deviceControlView != null && deviceControlView.instructions != null){
//						imageView2.setVisibility(View.VISIBLE);
						for(Instruction instruction:deviceControlView.instructions){
							if(view == instruction.view && event.getX() > instruction.showPostions[0] * width / bitmapWidth && event.getX() < instruction.showPostions[2] * width / bitmapWidth &&
							      event.getY() > instruction.showPostions[1] * height / bitmapHeight && event.getY() < instruction.showPostions[3] * height / bitmapHeight){
//								int centerX = (instruction.showPostions[0] + instruction.showPostions[2]) / 2;
//								int centerY = (instruction.showPostions[1] + instruction.showPostions[3]) / 2;
								imageView2.layout(instruction.showPostions[0] * width / bitmapWidth, instruction.showPostions[1] * height / bitmapHeight, 
										instruction.showPostions[2] * width / bitmapWidth, instruction.showPostions[3] * height / bitmapHeight );
							}
						}
					}
					if(event.getAction() == MotionEvent.ACTION_UP && deviceControlView != null && deviceControlView.instructions != null){
						boolean isChoose = false;
						for(Instruction instruction:deviceControlView.instructions){
							if(view == instruction.view && event.getX() > instruction.showPostions[0] * width / bitmapWidth && event.getX() < instruction.showPostions[2] * width / bitmapWidth&&
							      event.getY() > instruction.showPostions[1] * height / bitmapHeight && event.getY() < instruction.showPostions[3] * height / bitmapHeight){
								isChoose = true;
								if(instruction.instructionBytes != null){
									if(Connect.flag)Connect.writeControl(instruction.instructionBytes, DeviceControlActivity.this);
								}
								if(instruction.toast != null && instruction.toast.length() != 0){
									Toast.makeText(DeviceControlActivity.this, instruction.toast, Toast.LENGTH_SHORT).show();
								}
								if(instruction.toView != instruction.view){
									view = instruction.toView;
									imageView.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeByteArray(deviceControlView.backgroundBitmaps[instruction.toView], 0, deviceControlView.backgroundBitmaps[instruction.toView].length,Room.optReadBitmap)));
								}
							}
						}
						if(isChoose){
							new Thread(){
								public void run() {
									isControl = true;
									try {
										Thread.sleep(500);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									handler.sendEmptyMessage(0);
								};
							}.start();
						}else{
							imageView2.layout(0,0,0,0);
//							imageView2.setVisibility(View.GONE);
						}
					}else if(event.getAction() == MotionEvent.ACTION_UP){
						imageView2.layout(0,0,0,0);
//						imageView2.setVisibility(View.GONE);
					}
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
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
	static public byte[] bitmapToBytes(Bitmap bitmap){
		ByteArrayOutputStream os = new ByteArrayOutputStream(); 
		bitmap.compress(CompressFormat.PNG, 100, os);
		byte [] rtn = os.toByteArray();
		return rtn;
	}
	
	
	
}
