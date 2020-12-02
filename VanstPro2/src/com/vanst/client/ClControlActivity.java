package com.vanst.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ClControlActivity extends Activity {
	static DiyButton[] diyButtons;
	DiyButton selectButton;
	ImageView bgImageView;
	static ImageView kuangImageView;
	static ImageView hong1;
	static ImageView hong2;
	Button waiOn;
	Button waiOff;
	Button neiOn;
	Button neiOff;
	Button allOn;
	Button allOff;
	static int clselect;
	long controlTime;
	static byte type;
	static byte index;
	byte pose;
	int learnNum = 0;
	static int pixWidth;
	static int pixHeight;
	static int width;
	static int height;
	boolean succeedFlag;
	boolean keyDown;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0x01){
				if(clselect == 0){
					width = bgImageView.getWidth();
					height = bgImageView.getHeight();
					hong1.layout(diyButtons[0].x1 * width / pixWidth, diyButtons[0].y1* height / pixHeight, diyButtons[0].x2 * width / pixWidth, diyButtons[0].y2* height / pixHeight);
				}
			}
		};
	};
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cl2);
		Intent intent = getIntent();
		clselect = 0;
		if(intent != null){
			type = intent.getByteExtra("type", (byte)0x00);
			index = intent.getByteExtra("index", (byte)0x00);
			pose = intent.getByteExtra("pose", (byte)0x00);
		}
		controlTime = System.currentTimeMillis();
		bgImageView = (ImageView)findViewById(R.id.cl2_bg);
		kuangImageView = (ImageView)findViewById(R.id.cl2_kuang);
		hong1 = (ImageView)findViewById(R.id.cl2_hong1);
		hong2 = (ImageView)findViewById(R.id.cl2_hong2);
		hong1.setBackgroundResource(R.drawable.hongkuang);
		hong2.setBackgroundResource(R.drawable.hongkuang);
		if(Connect.rwPose == Connect.learn){
			if(type == Device.DEVICE_CURTAIN){
				pixWidth = 200;
				pixHeight = 500;
				diyButtons = DiyButton.CL_BUTTONS;
				bgImageView.setBackgroundResource(R.drawable.cl2);
			}else if(type == Device.DEVICE_TV){
				pixWidth = 200;
				pixHeight = 500;
				diyButtons = DiyButton.TV_BUTTONS;
				bgImageView.setBackgroundResource(R.drawable.tv_bg);
			}else if(type == Device.DEVICE_JDH){
				pixWidth = 200;
				pixHeight = 500;
				diyButtons = DiyButton.JDH_BUTTONS;
				bgImageView.setBackgroundResource(R.drawable.jdh_bg);
			}else if(type == Device.DEVICE_DVD){
				pixWidth = 200;
				pixHeight = 500;
				diyButtons = DiyButton.DVD_BUTTONS;
				bgImageView.setBackgroundResource(R.drawable.dvd_bg);
			}else if(type == Device.DEVICE_3D){
				pixWidth = 200;
				pixHeight = 500;
				diyButtons = DiyButton._3D_BUTTONS;
				bgImageView.setBackgroundResource(R.drawable.xyz_bg);
			}else if(type == Device.DEVICE_DIY){
				pixWidth = 200;
				pixHeight = 500;
				diyButtons = DiyButton.DIY_BUTTONS;
				bgImageView.setBackgroundResource(R.drawable.diy_bg);
			}else if(type == Device.DEVICE_SATELLITE){
				pixWidth = 200;
				pixHeight = 500;
				diyButtons = DiyButton.SATELLITE_BUTTONS;
				bgImageView.setBackgroundResource(R.drawable.wx_bg);
			}
			kuangImageView.setBackgroundResource(R.drawable.kuang2);
			kuangImageView.layout(diyButtons[learnNum].xc - 25, diyButtons[learnNum].xc + 25, diyButtons[learnNum].yc - 25, diyButtons[learnNum].yc + 25);
			bgImageView.setOnTouchListener(new View.OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					width = bgImageView.getWidth();
					height = bgImageView.getHeight();
					if(event.getAction() == MotionEvent.ACTION_UP){
						if(diyButtons != null && diyButtons.length != 0){
							for(DiyButton diyButton:diyButtons){
								if(event.getX() * pixWidth / width > diyButton.x1 && event.getX() * pixWidth / width< diyButton.x2 && 
										event.getY() * pixHeight / height > diyButton.y1 && event.getY() * pixHeight / height < diyButton.y2){
									if(type == Device.DEVICE_CURTAIN){
										if(diyButton.num == 2){
											clselect++;
											if(clselect > 2) clselect = 0;
											if(clselect ==  0){
												hong1.layout(diyButtons[0].x1 * width / pixWidth, diyButtons[0].y1* height / pixHeight, diyButtons[0].x2 * width / pixWidth, diyButtons[0].y2* height / pixHeight);
												hong2.layout(0, 0, 0, 0);
											}else if(clselect == 1){
												hong2.layout(diyButtons[1].x1 * width / pixWidth, diyButtons[1].y1* height / pixHeight, diyButtons[1].x2 * width / pixWidth, diyButtons[1].y2* height / pixHeight);
												hong1.layout(0, 0, 0, 0);
											}else if(clselect == 2){
												hong1.layout(diyButtons[0].x1 * width / pixWidth, diyButtons[0].y1* height / pixHeight, diyButtons[0].x2 * width / pixWidth, diyButtons[0].y2* height / pixHeight);
												hong2.layout(diyButtons[1].x1 * width / pixWidth, diyButtons[1].y1* height / pixHeight, diyButtons[1].x2 * width / pixWidth, diyButtons[1].y2* height / pixHeight);
											}
										}
										if(diyButton.num > 2){
											kuangImageView.setBackgroundResource(R.drawable.kuang2);
											kuangImageView.layout(diyButton.xc * width / pixWidth -25, diyButton.yc * height / pixHeight - 25, 
													diyButton.xc * width / pixWidth+25, diyButton.yc * height / pixHeight+ 25);
											if(diyButton.num == 3){
													Connect.IRControl(new byte[]{(byte)0xf0,type,index,3,(byte)(clselect*2),0}, ClControlActivity.this);
											}else if(diyButton.num == 4){
													Connect.IRControl(new byte[]{(byte)0xf0,type,index,3,(byte)0xff,0}, ClControlActivity.this);
											}else if(diyButton.num == 5){
													Connect.IRControl(new byte[]{(byte)0xf0,type,index,3,(byte)(clselect*2+1),0}, ClControlActivity.this);
											}
										}
									}else{
										kuangImageView.setBackgroundResource(R.drawable.kuang2);
										kuangImageView.layout(diyButton.xc * width / pixWidth -25, diyButton.yc * height / pixHeight - 25, 
																diyButton.xc * width / pixWidth+25, diyButton.yc * height / pixHeight+ 25);
										MyLog.i("xc", diyButton.xc+"");
										MyLog.i("yc", diyButton.yc+"");
										MyLog.i("touch",diyButton.num+"");
										Connect.IRControl(new byte[]{(byte)0xf0,type,index,3,(byte) diyButton.num,0}, ClControlActivity.this);
									}
								}
							}
						}
					}
					return true;
				}
			});
		}else{
			if(type == Device.DEVICE_CURTAIN){
				pixWidth = 200;
				pixHeight = 500;
				diyButtons = DiyButton.CL_BUTTONS;
				bgImageView.setBackgroundResource(R.drawable.cl2);
			}else if(type == Device.DEVICE_TV){
				pixWidth = 200;
				pixHeight = 500;
				diyButtons = DiyButton.TV_BUTTONS;
				bgImageView.setBackgroundResource(R.drawable.tv_bg);
			}else if(type == Device.DEVICE_JDH){
				pixWidth = 200;
				pixHeight = 500;
				diyButtons = DiyButton.JDH_BUTTONS;
				bgImageView.setBackgroundResource(R.drawable.jdh_bg);
			}else if(type == Device.DEVICE_DVD){
				pixWidth = 200;
				pixHeight = 500;
				diyButtons = DiyButton.DVD_BUTTONS;
				bgImageView.setBackgroundResource(R.drawable.dvd_bg);
			}else if(type == Device.DEVICE_3D){
				pixWidth = 200;
				pixHeight = 500;
				diyButtons = DiyButton._3D_BUTTONS;
				bgImageView.setBackgroundResource(R.drawable.xyz_bg);
			}else if(type == Device.DEVICE_DIY){
				pixWidth = 200;
				pixHeight = 500;
				diyButtons = DiyButton.DIY_BUTTONS;
				bgImageView.setBackgroundResource(R.drawable.diy_bg);
			}else if(type == Device.DEVICE_SATELLITE){
				pixWidth = 200;
				pixHeight = 500;
				diyButtons = DiyButton.SATELLITE_BUTTONS;
				bgImageView.setBackgroundResource(R.drawable.wx_bg);
			}
			kuangImageView.setBackgroundResource(R.drawable.kuang);
			bgImageView.setOnTouchListener(new View.OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					width = bgImageView.getWidth();
					height = bgImageView.getHeight();
					MyLog.i("",event.getX()+"  " + event.getY()+"");
					if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
						boolean isSelect = false;
						for(DiyButton diyButton:diyButtons){
							if(event.getX() * pixWidth / width > diyButton.x1 && event.getX() * pixWidth / width< diyButton.x2 && 
									event.getY() * pixHeight / height > diyButton.y1 && event.getY() * pixHeight / height < diyButton.y2){
								if(type == Device.DEVICE_CURTAIN){
									if(diyButton.num >= 2){
										kuangImageView.layout(diyButton.xc * width / pixWidth -25, diyButton.yc * height / pixHeight - 25, 
												diyButton.xc * width / pixWidth+25, diyButton.yc * height / pixHeight+ 25);
										if(diyButton.num == 2){
											isSelect = true;
											if(event.getAction() == MotionEvent.ACTION_DOWN && !keyDown){
												clselect++;
												if(clselect > 2) clselect = 0;
												if(clselect ==  0){
													hong1.layout(diyButtons[0].x1 * width / pixWidth, diyButtons[0].y1* height / pixHeight, diyButtons[0].x2 * width / pixWidth, diyButtons[0].y2* height / pixHeight);
													hong2.layout(0, 0, 0, 0);
												}else if(clselect == 1){
													hong2.layout(diyButtons[1].x1 * width / pixWidth, diyButtons[1].y1* height / pixHeight, diyButtons[1].x2 * width / pixWidth, diyButtons[1].y2* height / pixHeight);
													hong1.layout(0, 0, 0, 0);
												}else if(clselect == 2){
													hong1.layout(diyButtons[0].x1 * width / pixWidth, diyButtons[0].y1* height / pixHeight, diyButtons[0].x2 * width / pixWidth, diyButtons[0].y2* height / pixHeight);
													hong2.layout(diyButtons[1].x1 * width / pixWidth, diyButtons[1].y1* height / pixHeight, diyButtons[1].x2 * width / pixWidth, diyButtons[1].y2* height / pixHeight);
												}
												keyDown = true;
											}
											
										}else if(diyButton.num == 3){
											isSelect = true;
											if(event.getAction() == MotionEvent.ACTION_DOWN && !keyDown){
												if(System.currentTimeMillis() - controlTime > 100){
													controlTime = System.currentTimeMillis();
													Connect.IRControl(new byte[]{(byte)0xf0,type,index,2,(byte)(clselect*2),0}, ClControlActivity.this);
												}
												keyDown = true;
											}
										}else if(diyButton.num == 4){
											isSelect = true;
											if(event.getAction() == MotionEvent.ACTION_DOWN && !keyDown){
												if(System.currentTimeMillis() - controlTime > 100){
													controlTime = System.currentTimeMillis();
													Connect.IRControl(new byte[]{(byte)0xf0,type,index,2,(byte)0xff,0}, ClControlActivity.this);
												}
												keyDown = true;
											}
										}else if(diyButton.num == 5){
											isSelect = true;
											if(event.getAction() == MotionEvent.ACTION_DOWN && !keyDown){
												if(System.currentTimeMillis() - controlTime > 100){
													controlTime = System.currentTimeMillis();
													Connect.IRControl(new byte[]{(byte)0xf0,type,index,2,(byte)(clselect*2+1),0}, ClControlActivity.this);
												}
												keyDown = true;
											}
										}
									}
								}else{
								
									kuangImageView.layout(diyButton.xc * width / pixWidth -25, diyButton.yc * height / pixHeight - 25, 
															diyButton.xc * width / pixWidth+25, diyButton.yc * height / pixHeight+ 25);
									
									MyLog.i("xc", diyButton.xc+"");
									MyLog.i("yc", diyButton.yc+"");
									MyLog.i("touch",diyButton.num+"");
									isSelect = true;
									if(event.getAction() == MotionEvent.ACTION_DOWN && !keyDown){
										if(System.currentTimeMillis() - controlTime > 100){
											controlTime = System.currentTimeMillis();
											Connect.IRControl(new byte[]{(byte)0xf0,type,index,2,(byte) diyButton.num,0}, ClControlActivity.this);
										}
										keyDown = true;
									}
								}
							}
						}
						if(!isSelect){
							kuangImageView.layout(0, 0, 0, 0);
							if(keyDown){
//								if(type == Device.DEVICE_CURTAIN)
//								Connect.IRControl(new byte[]{(byte)0xf0,type,index,2,(byte)0xff,0}, ClControlActivity.this);
								keyDown = false;
							}
						}
					}else if(event.getAction() == MotionEvent.ACTION_UP){
						if(keyDown){
//							if(type == Device.DEVICE_CURTAIN)
//							Connect.IRControl(new byte[]{(byte)0xf0,type,index,2,(byte)0xff,0}, ClControlActivity.this);
							keyDown = false;
						}
//						for(DiyButton diyButton:diyButtons){
//							if(System.currentTimeMillis() - controlTime > 500 && event.getX() * pixWidth / width > diyButton.x1 && event.getX() * pixWidth / width< diyButton.x2 && 
//									event.getY() * pixHeight / height > diyButton.y1 && event.getY() * pixHeight / height < diyButton.y2){
//								controlTime = System.currentTimeMillis();
//								Connect.IRControl(new byte[]{(byte)0xf0,type,index,2,(byte) diyButton.num,0}, ClControlActivity.this);
//							}
//						}
						kuangImageView.layout(0, 0, 0, 0);
					}
					return true;
				}
			});
		}
		if(type == Device.DEVICE_CURTAIN){
			new clinitThread().start();
		}
	}	
	static void learnSingle(int key){
		Toast.makeText(Connect.activity,"学习成功，请选择下一个",Toast.LENGTH_LONG);
		if(type == Device.DEVICE_CURTAIN){
			kuangImageView.setBackgroundResource(R.drawable.kuang);
			if(key % 2 == 0){
				kuangImageView.layout(diyButtons[3].xc * width / pixWidth - 25, diyButtons[3].yc * height / pixHeight - 25, 
						diyButtons[3].xc * width / pixWidth+ 25, diyButtons[3].yc * height / pixHeight + 25);
			}else{
				kuangImageView.layout(diyButtons[5].xc * width / pixWidth - 25, diyButtons[5].yc * height / pixHeight - 25, 
						diyButtons[5].xc * width / pixWidth+ 25, diyButtons[5].yc * height / pixHeight + 25);
			}
			clselect = key / 2;
			if(clselect ==  0){
				hong1.layout(diyButtons[0].x1 * width / pixWidth, diyButtons[0].y1* height / pixHeight, diyButtons[0].x2 * width / pixWidth, diyButtons[0].y2* height / pixHeight);
				hong2.layout(0, 0, 0, 0);
			}else if(clselect == 1){
				hong2.layout(diyButtons[1].x1 * width / pixWidth, diyButtons[1].y1* height / pixHeight, diyButtons[1].x2 * width / pixWidth, diyButtons[1].y2* height / pixHeight);
				hong1.layout(0, 0, 0, 0);
			}else if(clselect == 2){
				hong1.layout(diyButtons[0].x1 * width / pixWidth, diyButtons[0].y1* height / pixHeight, diyButtons[0].x2 * width / pixWidth, diyButtons[0].y2* height / pixHeight);
				hong2.layout(diyButtons[1].x1 * width / pixWidth, diyButtons[1].y1* height / pixHeight, diyButtons[1].x2 * width / pixWidth, diyButtons[1].y2* height / pixHeight);
			}
		}else{
			kuangImageView.setBackgroundResource(R.drawable.kuang);
			kuangImageView.layout(diyButtons[key].xc * width / pixWidth - 25, diyButtons[key].yc * height / pixHeight - 25, 
					diyButtons[key].xc * width / pixWidth+ 25, diyButtons[key].yc * height / pixHeight + 25);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
	@Override
	protected void onResume() {
		super.onResume();
		Connect.activity = this;
	}	
	
	class clinitThread extends Thread{
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
	public void writeBytes(){
		
		Intent data = new Intent();
		data.putExtra("type", type);
		data.putExtra("index", index);
		data.putExtra("pose", pose);
		setResult(RESULT_OK, data);
	}
	@Override
	protected void onDestroy() {
		if(MainView.IRDevice != null && Connect.rwPose == Connect.learn)
			Connect.writeControl(new byte[]{(byte)0xf0,(byte)MainView.IRDevice.type,(byte)MainView.IRDevice.index,0x00,0x00,0x00}, ClControlActivity.this);
//		Connect.rwPose = Connect.control;
		if(succeedFlag){
			
		}
		finish();
		super.onDestroy();
	}
}

