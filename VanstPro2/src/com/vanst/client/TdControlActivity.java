package com.vanst.client;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class TdControlActivity extends Activity {
	ImageButton imageButton;
	int alpha;
	boolean pose1;
	boolean isLong;
	float touchX;
	float touchY;
	boolean move;
	byte type;
	byte index;
	byte pose;
	byte value2;
	byte linValue2;
	byte linPose;
	Device device;
	RefreshThread refreshThread;
	long controlTime;
	SeekBar seekBar;
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == 0x01){
				linValue2 = (byte) device.value2;
				linPose = (byte) device.pose;
				if(device.pose != 1){
					imageButton.setImageResource(R.drawable.light0);
					seekBar.setProgress(0);
					seekBar.setEnabled(false);
				}
				else if(device.value2 == 1) imageButton.setImageResource(R.drawable.light1);
				else if(device.value2 == 2) imageButton.setImageResource(R.drawable.light2);
				else if(device.value2 == 3) imageButton.setImageResource(R.drawable.light3);
				else if(device.value2 == 4) imageButton.setImageResource(R.drawable.light4);
				else if(device.value2 == 5) imageButton.setImageResource(R.drawable.light5);
				else if(device.value2 == 6) imageButton.setImageResource(R.drawable.light6);
				else if(device.value2 == 7) imageButton.setImageResource(R.drawable.light7);
				else if(device.value2 == 8) imageButton.setImageResource(R.drawable.light8);
				else if(device.value2 == 9) imageButton.setImageResource(R.drawable.light9);
				else if(device.value2 == 10) imageButton.setImageResource(R.drawable.light9);
				if(device.pose == 1){
					seekBar.setEnabled(true);
					seekBar.setProgress(9-(linValue2-1));
				}
				
			}
		}
	};
	private void refresh(){
		if(device.pose != 1) imageButton.setImageResource(R.drawable.light0);
		else if(linValue2 == 1) imageButton.setImageResource(R.drawable.light1);
		else if(linValue2 == 2) imageButton.setImageResource(R.drawable.light2);
		else if(linValue2 == 3) imageButton.setImageResource(R.drawable.light3);
		else if(linValue2 == 4) imageButton.setImageResource(R.drawable.light4);
		else if(linValue2 == 5) imageButton.setImageResource(R.drawable.light5);
		else if(linValue2 == 6) imageButton.setImageResource(R.drawable.light6);
		else if(linValue2 == 7) imageButton.setImageResource(R.drawable.light7);
		else if(linValue2 == 8) imageButton.setImageResource(R.drawable.light8);
		else if(linValue2 == 9) imageButton.setImageResource(R.drawable.light9);
		else if(linValue2 == 10) imageButton.setImageResource(R.drawable.light9);
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.td);
		pose1 = true;
		imageButton = (ImageButton)findViewById(R.id.td_image);
		seekBar = (SeekBar)findViewById(R.id.td_seekbar);
		seekBar.setProgress(0);
		Intent intent = getIntent();
		if(intent !=null){
			type = intent.getByteExtra("type", (byte)0x00);
			index = intent.getByteExtra("index", (byte)0x00);
			pose = intent.getByteExtra("pose", (byte)0x00);
			value2 = intent.getByteExtra("value2", (byte)0x00);
			linValue2 = value2;
			linPose = pose;
			for(Device device:MainView.devices){
				if(type == device.type && index == device.index){
					this.device = device;
				}
			}
			refreshThread = new RefreshThread();
			refreshThread.start();
		}
		if(device != null) handler.sendEmptyMessage(0x01);
		imageButton.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View arg0) {
				if(System.currentTimeMillis() - controlTime > 500 && !move){
					if(device.pose == 1){
						controlTime = System.currentTimeMillis();
						Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x00,0x00,0x00}, TdControlActivity.this);
//						device.pose = 0;
//						linPose = 0;
//						linValue2 = 10;
//						handler.sendEmptyMessage(0x01);
					}
					else {
						controlTime = System.currentTimeMillis();
						Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x01,0x00,0x00}, TdControlActivity.this);
//						device.pose = 1;
//						handler.sendEmptyMessage(0x01);
					}
					isLong = true;
					writeBytes();
				}
				return false;
			}
		});
		
		imageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(System.currentTimeMillis() - controlTime > 500){
					if(device.pose == 1){
						controlTime = System.currentTimeMillis();
						device.pose = 0;
						seekBar.setEnabled(false);
						Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x00,0x00,0x00}, TdControlActivity.this);
					}
					else {
						controlTime = System.currentTimeMillis();
						device.pose = 1;
						seekBar.setEnabled(true);
						Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x01,0x00,0x00}, TdControlActivity.this);
//						refresh();
					}
					refresh();
					writeBytes();
				}
			}
		});
		
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,(byte)linValue2,0x00}, TdControlActivity.this);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				linValue2 = (byte)(11- (progress + 1));
				refresh();
			}
		});
		
//		
//		imageButton.setOnTouchListener(new View.OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View arg0, MotionEvent event) {
//				int heght = imageButton.getHeight();
//				if(event.getAction() == MotionEvent.ACTION_DOWN){
//					touchX = event.getX();
//					touchY = event.getY();
//				}else if(event.getAction() == MotionEvent.ACTION_UP){
//					linValue2 = (byte)(event.getY()*11/heght);
//					if(linValue2 < 1) linValue2 = 1;
//					if(linValue2 > 10) linValue2 = 10;
//					if(System.currentTimeMillis() - controlTime > 500 && device.pose == 1 &&!isLong){
//						Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,(byte)linValue2,0x00}, TdControlActivity.this);
//						refresh();
//					}
//					
////					if(System.currentTimeMillis() - controlTime > 500 && touchY > event.getY() + 20 && device.value2 > 0){
////						controlTime = System.currentTimeMillis();
////						alpha+=20; if(alpha>255) alpha =255;
////						Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x03,0x00,0x00}, TdControlActivity.this);
////						linValue2--; 
////						if(linValue2 < 1) linValue2 = 1;
//////						refresh();
////						writeBytes();
////					}else if(System.currentTimeMillis() - controlTime > 500 && touchY < event.getY() - 20 && device.value2 > 0){
////						controlTime = System.currentTimeMillis();
////						alpha-=20; if(alpha<75) alpha = 75;
////						Connect.writeControl(new byte[]{(byte)0xf0,type,index,0x02,0x00,0x00}, TdControlActivity.this);
////						linValue2++; 
////						if(linValue2 > 10) linValue2 = 10;
//////						refresh();
////						writeBytes();
////					}
//					isLong = false;
//					move = false;
//				}else if(event.getAction() == MotionEvent.ACTION_MOVE){
//					if(touchY > event.getY() + 20 || touchY < event.getY() - 20){
//						move = true;
//					}
//					linValue2 = (byte)(event.getY()*11/heght);
//					if(linValue2 < 1) linValue2 = 1;
//					if(linValue2 > 10) linValue2 = 10;
//					refresh();
//				}
//				return false;
//			}
//		});
//		controlTime = System.currentTimeMillis();
//		Connect.writeControl(new byte[]{(byte)0xf1,type,index,0x00,0x00,0x00}, TdControlActivity.this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		Connect.activity = this;
	}
	
	private void writeBytes(){
		Intent data = new Intent();
		data.putExtra("type", type);
		data.putExtra("index", index);
		data.putExtra("pose", pose);
		data.putExtra("value2", value2);
		setResult(RESULT_OK, data);
	}
	protected void onDestroy() {
		finish();
		if(refreshThread != null){
			refreshThread.sflag = true;
		}
		super.onDestroy();
	}
	class RefreshThread extends Thread{
		boolean sflag = false;
		int value;
		int pose;
		public void run() {
			while(!sflag){
				if(device != null && device.isNew){
					device.isNew = false;
					handler.sendEmptyMessage(0x01);
				}
//				if(value != device.value2 || pose != device.pose){
//					linPose = (byte) device.pose;
//					pose = (byte) device.pose;
//					value = (byte) device.value2;
//					linValue2 = (byte) device.value2;
//					if(device != null) handler.sendEmptyMessage(0x01);
//				}
//				if(device != null) handler.sendEmptyMessage(0x01);
				try {
					sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
}
