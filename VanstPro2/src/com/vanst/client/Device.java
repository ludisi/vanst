package com.vanst.client;


import java.io.InputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

public class Device {
	static byte [] typeBytes;
	final static int DEVICE_AIR_CONDITIONING = 0x04;
	final static int DEVICE_THERMOMETER = 0x13;
	final static int DEVICE_RICE_COOKER = 0x14;
	final static int DEVICE_CURTAIN = 0x06;
	final static int DEVICE_LOCK = 0x07;
	final static int DEVICE_TV = 0x08;
	final static int DEVICE_LIGHT = 0x0c;
	final static int DEVICE_FAN = 0x17;
	final static int DEVICE_DIY = 0x1E;
	final static int DEVICE_JDH = 0x1C;
	final static int DEVICE_DVD = 0x1A;
	final static int DEVICE_IR = 0x20;
	final static int DEVICE_3D = 0x1d;
	final static int DEVICE_SATELLITE = 0x1B;
	final static int DEVICE_LISTEN = 0x05;
	final static int DEVICE_WASHER = 0x09;
	final static int DEVICE_AIR_CONDITIONING2 = 0x21;
	final static int DEVICE_TYPE_COUNT = 33;
	long id;
	int type;
	int index;
	int pose = -1;
	int value2 = -1;
	int value3 = -1;
	float offsetX;
	float offsetY;
	int posRX;
	int posRY;
	Bitmap bitmap;
	Bitmap onBitmap;
	Bitmap offBitmap;
	Bitmap unknowBitmap;
	boolean bind = false;
	static View view;
	int width;
	int height;
	int room;
	boolean isWait;
	long controlTime;
	boolean visible;
	boolean isSelect;
	boolean isMove;
	boolean isNew;
	int moshi1 = -1;
	int moshi2 = -1;
	int moshi3 = -1;
	int moshi4 = -1;
	int allOn = 1;
	int allOff = 1;
	String remark;
	String linkapp;
	final static int UNKNOWIMAGE = R.drawable.device_0;
	final static int UNKNOWIMAGE_ON = R.drawable.device_0_1;
	final static int UNKNOWIMAGE_OFF = R.drawable.device_0_0;
	final static int UNKNOWIMAGE_UNKNOW = R.drawable.device_0_2;
	static Bitmap UNKNOW_BITMAP;
	static Bitmap UNKNOW_ON_BITMAP;
	static Bitmap UNKNOW_OFF_BITMAP;
	static Bitmap UNKNOW_UNKNOW_BITMAP;
	final static int[] DEVICE_IMAGES = {R.drawable.device_1,
										R.drawable.device_2,
										R.drawable.device_3,
										R.drawable.device_4,
										R.drawable.device_5,
										R.drawable.device_6,
										R.drawable.device_7,
										R.drawable.device_8,
										R.drawable.device_9,
										R.drawable.device_10,
										R.drawable.device_11,
										R.drawable.device_12,
										R.drawable.device_13,
										R.drawable.device_14,
										R.drawable.device_15,
										R.drawable.device_16,
										R.drawable.device_17,
										R.drawable.device_18,
										R.drawable.device_19,
										R.drawable.device_20,
										R.drawable.device_21,
										R.drawable.device_22,
										R.drawable.device_23,
										R.drawable.device_24,
										R.drawable.device_25,
										R.drawable.device_26,
										R.drawable.device_27,
										R.drawable.device_28,
										R.drawable.device_29,
										R.drawable.device_30,
										R.drawable.device_31,
										R.drawable.device_32,
										R.drawable.device_33,
	};
	final static int[] DEVICE_ON_IMAGES = {
										R.drawable.device_1_1,
										R.drawable.device_2_1,
										R.drawable.device_3_1,
										R.drawable.device_4_1,
										R.drawable.device_5_1,
										R.drawable.device_6_1,
										R.drawable.device_7_1,
										R.drawable.device_8_1,
										R.drawable.device_9_1,
										R.drawable.device_10_1,
										R.drawable.device_11_1,
										R.drawable.device_12_1,
										R.drawable.device_13_1,
										R.drawable.device_14_1,
										R.drawable.device_15_1,
										R.drawable.device_16_1,
										R.drawable.device_17_1,
										R.drawable.device_18_1,
										R.drawable.device_19_1,
										R.drawable.device_20_1,
										R.drawable.device_21_1,
										R.drawable.device_22_1,
										R.drawable.device_23_1,
										R.drawable.device_24_1,
										R.drawable.device_25_1,
										R.drawable.device_26_1,
										R.drawable.device_27_1,
										R.drawable.device_28_1,
										R.drawable.device_29_1,
										R.drawable.device_30_1,
										R.drawable.device_31_1,
										R.drawable.device_32_1,
										R.drawable.device_33_1
	};
	final static int[] DEVICE_OFF_IMAGES = {
										R.drawable.device_1_0,
										R.drawable.device_2_0,
										R.drawable.device_3_0,
										R.drawable.device_4_0,
										R.drawable.device_5_0,
										R.drawable.device_6_0,
										R.drawable.device_7_0,
										R.drawable.device_8_0,
										R.drawable.device_9_0,
										R.drawable.device_10_0,
										R.drawable.device_11_0,
										R.drawable.device_12_0,
										R.drawable.device_13_0,
										R.drawable.device_14_0,
										R.drawable.device_15_0,
										R.drawable.device_16_0,
										R.drawable.device_17_0,
										R.drawable.device_18_0,
										R.drawable.device_19_0,
										R.drawable.device_20_0,
										R.drawable.device_21_0,
										R.drawable.device_22_0,
										R.drawable.device_23_0,
										R.drawable.device_24_0,
										R.drawable.device_25_0,
										R.drawable.device_26_0,
										R.drawable.device_27_0,
										R.drawable.device_28_0,
										R.drawable.device_29_0,
										R.drawable.device_30_0,
										R.drawable.device_31_0,
										R.drawable.device_32_0,
										R.drawable.device_33_0,
	};
	
	final static int[] DEVICE_UNKNOW_IMAGES = {
										R.drawable.device_1_2,
										R.drawable.device_2_2,
										R.drawable.device_3_2,
										R.drawable.device_4_2,
										R.drawable.device_5_2,
										R.drawable.device_6_2,
										R.drawable.device_7_2,
										R.drawable.device_8_2,
										R.drawable.device_9_2,
										R.drawable.device_10_2,
										R.drawable.device_11_2,
										R.drawable.device_12_2,
										R.drawable.device_13_2,
										R.drawable.device_14_2,
										R.drawable.device_15_2,
										R.drawable.device_16_2,
										R.drawable.device_17_2,
										R.drawable.device_18_2,
										R.drawable.device_19_2,
										R.drawable.device_20_2,
										R.drawable.device_21_2,
										R.drawable.device_22_2,
										R.drawable.device_23_2,
										R.drawable.device_24_2,
										R.drawable.device_25_2,
										R.drawable.device_26_2,
										R.drawable.device_27_2,
										R.drawable.device_28_2,
										R.drawable.device_29_2,
										R.drawable.device_30_2,
										R.drawable.device_31_2,
										R.drawable.device_32_2,
										R.drawable.device_33_2,
		};
	static Bitmap[] deviceBitmaps = new Bitmap[DEVICE_TYPE_COUNT];
	static Bitmap[] deviceOnBitmaps = new Bitmap[DEVICE_TYPE_COUNT];
	static Bitmap[] deviceOffBitmaps = new Bitmap[DEVICE_TYPE_COUNT];
	static Bitmap[] deviceUnknowBitmaps = new Bitmap[DEVICE_TYPE_COUNT];
	final static String[] DEVICE_ITEMS ={
		"移除设备","绑定模块","清除绑定"
	};
	final static String[] DIY_ITEMS = {
		"移除设备","绑定模块","清除绑定","设置设备"
	};
	final static String[] IR_ITEMS = {
		"移除设备","绑定模块","清除绑定","红外学习"
	};
	final static String[] CURTAIN_ITEMS = {
		"移除设备","绑定模块","清除绑定","红外学习","清空窗帘ID"
	};
	final static String[] AIR_CONDITIONING = {
		"移除设备","绑定模块","清除绑定","红外学习","设置ID"
	};
	public static Bitmap readBitMap(View view, int resId){  
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;   
        opt.inPurgeable = true;  
        opt.inInputShareable = true;  
          //获取资源图片  
        InputStream is = view.getResources().openRawResource(resId);  
           return BitmapFactory.decodeStream(is,null,opt);  
    }
	
	public Device(View view,int type, int index) {
		Device.view = view;
		this.type = type;
		this.index = index;
		visible = true;
//		MyLog.i("type", type+"");
		if(type >= 1 && type <= DEVICE_TYPE_COUNT){
			if(deviceBitmaps[type-1] == null) 
				deviceBitmaps[type-1] = readBitMap(view, DEVICE_IMAGES[type-1]);
	//			deviceBitmaps[type-1] = BitmapFactory.decodeResource(view.getResources(), DEVICE_IMAGES[type-1]);
			bitmap = deviceBitmaps[type-1];
			if(deviceOnBitmaps[type-1] == null) 
				deviceOnBitmaps[type-1] = readBitMap(view, DEVICE_ON_IMAGES[type-1]);
	//			deviceOnBitmaps[type-1]= BitmapFactory.decodeResource(view.getResources(), DEVICE_ON_IMAGES[type-1]);
			onBitmap = deviceOnBitmaps[type-1];
			if(deviceOffBitmaps[type-1] == null) 
				deviceOffBitmaps[type-1] = readBitMap(view, DEVICE_OFF_IMAGES[type-1]);
	//			deviceOffBitmaps[type-1] = BitmapFactory.decodeResource(view.getResources(), DEVICE_OFF_IMAGES[type-1]);
			offBitmap = deviceOffBitmaps[type-1];
			if(deviceUnknowBitmaps[type-1] == null) 
				deviceUnknowBitmaps[type-1] = readBitMap(view, DEVICE_UNKNOW_IMAGES[type-1]);
	//			deviceUnknowBitmaps[type-1] = BitmapFactory.decodeResource(view.getResources(), DEVICE_UNKNOW_IMAGES[type-1]);
			unknowBitmap = deviceUnknowBitmaps[type-1];
			
		}else{
			DeviceControlView deviceControlView = DeviceControlView.readControlView((byte)type, (byte)index);
			if(deviceControlView == null){
				if(UNKNOW_BITMAP == null) UNKNOW_BITMAP = readBitMap(view, UNKNOWIMAGE);
				bitmap = UNKNOW_BITMAP;
				if(UNKNOW_ON_BITMAP == null) UNKNOW_ON_BITMAP = readBitMap(view, UNKNOWIMAGE_ON);
				onBitmap = UNKNOW_ON_BITMAP;
				if(UNKNOW_OFF_BITMAP == null) UNKNOW_OFF_BITMAP = readBitMap(view, UNKNOWIMAGE_OFF);
				offBitmap = UNKNOW_OFF_BITMAP;
				if(UNKNOW_UNKNOW_BITMAP == null) UNKNOW_UNKNOW_BITMAP = readBitMap(view, UNKNOWIMAGE_UNKNOW);
				unknowBitmap = UNKNOW_UNKNOW_BITMAP;
			}else{
				Room.optReadBitmap.inSampleSize = 1;
				bitmap = BitmapFactory.decodeByteArray(deviceControlView.DEVICE_IMAGE, 0, deviceControlView.DEVICE_IMAGE.length, Room.optReadBitmap);
				onBitmap = BitmapFactory.decodeByteArray(deviceControlView.DEVICE_ON_IMAGE, 0, deviceControlView.DEVICE_ON_IMAGE.length, Room.optReadBitmap);
				offBitmap = BitmapFactory.decodeByteArray(deviceControlView.DEVICE_OFF_IMAGE, 0, deviceControlView.DEVICE_OFF_IMAGE.length, Room.optReadBitmap);
				unknowBitmap = BitmapFactory.decodeByteArray(deviceControlView.DEVICE_UNKNOW_IMAGE, 0, deviceControlView.DEVICE_UNKNOW_IMAGE.length, Room.optReadBitmap);
			}
			
		}
		if(unknowBitmap != null){
			width = unknowBitmap.getWidth();
			height = unknowBitmap.getHeight();
		}
	}
	public Device(View view,int type, int index,int room, float offsetX, float offsetY){
		this.type = type;
		this.index = index;
		this.room = room;
//		if(room < 0) room+=256;
//		allOn = room % 2;
//		allOff = room / 2 % 2;
//		moshi1 = room / 4 % 2;
//		moshi2 = room / 8 % 2;
//		moshi3 = room / 16 % 2;
//		moshi4 = room / 32 % 2;
		visible = true;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		if(type >= 1 && type <= DEVICE_TYPE_COUNT){
			if(deviceBitmaps[type-1] == null) 
				deviceBitmaps[type-1] = readBitMap(view, DEVICE_IMAGES[type-1]);
	//			deviceBitmaps[type-1] = BitmapFactory.decodeResource(view.getResources(), DEVICE_IMAGES[type-1]);
			bitmap = deviceBitmaps[type-1];
			if(deviceOnBitmaps[type-1] == null) 
				deviceOnBitmaps[type-1] = readBitMap(view, DEVICE_ON_IMAGES[type-1]);
	//			deviceOnBitmaps[type-1]= BitmapFactory.decodeResource(view.getResources(), DEVICE_ON_IMAGES[type-1]);
			onBitmap = deviceOnBitmaps[type-1];
			if(deviceOffBitmaps[type-1] == null) 
				deviceOffBitmaps[type-1] = readBitMap(view, DEVICE_OFF_IMAGES[type-1]);
	//			deviceOffBitmaps[type-1] = BitmapFactory.decodeResource(view.getResources(), DEVICE_OFF_IMAGES[type-1]);
			offBitmap = deviceOffBitmaps[type-1];
			if(deviceUnknowBitmaps[type-1] == null) 
				deviceUnknowBitmaps[type-1] = readBitMap(view, DEVICE_UNKNOW_IMAGES[type-1]);
	//			deviceUnknowBitmaps[type-1] = BitmapFactory.decodeResource(view.getResources(), DEVICE_UNKNOW_IMAGES[type-1]);
			unknowBitmap = deviceUnknowBitmaps[type-1];
			
		}else{
			DeviceControlView deviceControlView = DeviceControlView.readControlView((byte)type, (byte)index);
			if(deviceControlView == null){
				if(UNKNOW_BITMAP == null) UNKNOW_BITMAP = readBitMap(view, UNKNOWIMAGE);
				bitmap = UNKNOW_BITMAP;
				if(UNKNOW_ON_BITMAP == null) UNKNOW_ON_BITMAP = readBitMap(view, UNKNOWIMAGE_ON);
				onBitmap = UNKNOW_ON_BITMAP;
				if(UNKNOW_OFF_BITMAP == null) UNKNOW_OFF_BITMAP = readBitMap(view, UNKNOWIMAGE_OFF);
				offBitmap = UNKNOW_OFF_BITMAP;
				if(UNKNOW_UNKNOW_BITMAP == null) UNKNOW_UNKNOW_BITMAP = readBitMap(view, UNKNOWIMAGE_UNKNOW);
				unknowBitmap = UNKNOW_UNKNOW_BITMAP;
			}else{
				Room.optReadBitmap.inSampleSize = 1;
				bitmap = BitmapFactory.decodeByteArray(deviceControlView.DEVICE_IMAGE, 0, deviceControlView.DEVICE_IMAGE.length, Room.optReadBitmap);
				onBitmap = BitmapFactory.decodeByteArray(deviceControlView.DEVICE_ON_IMAGE, 0, deviceControlView.DEVICE_ON_IMAGE.length, Room.optReadBitmap);
				offBitmap = BitmapFactory.decodeByteArray(deviceControlView.DEVICE_OFF_IMAGE, 0, deviceControlView.DEVICE_OFF_IMAGE.length, Room.optReadBitmap);
				unknowBitmap = BitmapFactory.decodeByteArray(deviceControlView.DEVICE_UNKNOW_IMAGE, 0, deviceControlView.DEVICE_UNKNOW_IMAGE.length, Room.optReadBitmap);
			}
			
		}
		if(unknowBitmap != null){
			width = unknowBitmap.getWidth();
			height = unknowBitmap.getHeight();
		}
	}
	public void init(){
		offsetX = 0;
		offsetY = 0;
		visible = true;
		id = 0;
		pose = -1;
		value2 = -1;
		value3 = -1;
		remark = null;
	}
	static public void test(){
		MyLog.i("indates",Connect.indates.size()+"");
		int i = 0;
		byte [] bytes = new byte[Connect.indates.size()];
		for(byte b : Connect.indates){
			bytes[i] = b;
			i++;
		}
		Room.defaultBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		
//		Room.defaultBitmap = BitmapFactory.decodeByteArray(Room.def(BitmapFactory.decodeResource(view.getResources(), DEVICE_UNKNOW_IMAGES[0])), 0, 
//				Room.def(BitmapFactory.decodeResource(view.getResources(), DEVICE_UNKNOW_IMAGES[0])).length);
	}
	static public byte[] initDevicesmodes(){
		byte[] date = new byte[5];
		int i = 0;
		int deviceCount = 0;
		for(Device device:MainView.devices){
			if(device.visible)
				deviceCount++;
		}
		typeBytes = new byte[deviceCount*5];
		for(Device device:MainView.devices){
			if(device.visible){
				date[0] = (byte)(device.allOn + device.allOff * 2);
				date[1] = (byte)device.moshi1;
				date[2] = (byte)device.moshi2;
				date[3] = (byte)device.moshi3;
				date[4] = (byte)device.moshi4;
				typeBytes[i] = date[0];
				typeBytes[i+1] = date[1];
				typeBytes[i+2] = date[2];
				typeBytes[i+3] = date[3];
				typeBytes[i+4] = date[4];
//				date[2] = (byte)(device.allOn + device.allOff * 2 + device.moshi1 * 4 + device.moshi2 * 8 + device.moshi3 * 16 + device.moshi4 * 32);
//				byte ratioX = 0;
//				byte ratioY = 0;
//				ratioX = (byte) (256 * (device.offsetX + device.width/2)/ MainView.bitmap.getWidth());
//				ratioY = (byte) (256 * (device.offsetY + device.height/2)/ MainView.bitmap.getHeight());
//				date[3] = ratioX;
//				date[4] = ratioY;
//				typeBytes[i] = date[0];
//				typeBytes[i+1] = date[1];
//				typeBytes[i+2] = date[2];
//				typeBytes[i+3] = date[3];
//				typeBytes[i+4] = date[4];
//				MyLog.i("i"+i,typeBytes[i]+"");
//				MyLog.i("i"+(i+1),typeBytes[i+1]+"");
//				MyLog.i("i"+(i+2),typeBytes[i+2]+"");
//				MyLog.i("i"+(i+3),typeBytes[i+3]+"");
//				MyLog.i("i"+(i+4),typeBytes[i+4]+"");
				i += 5;
			}
		}
		return typeBytes;
	}
	
	static public byte[] initDevicesConfig(){
		byte[] date = new byte[5];
		int i = 0;
		int deviceCount = 0;
		for(Device device:MainView.devices){
			if(device.visible)
				deviceCount++;
		}
		typeBytes = new byte[deviceCount*5];
		for(Device device:MainView.devices){
			if(device.visible){
				date[0] = (byte)device.type;
				date[1] = (byte)device.index;
				date[2] = (byte)device.room;
//				date[2] = (byte)(device.allOn + device.allOff * 2 + device.moshi1 * 4 + device.moshi2 * 8 + device.moshi3 * 16 + device.moshi4 * 32);
				byte ratioX = 0;
				byte ratioY = 0;
				ratioX = (byte) (256 * (device.offsetX + device.width/2)/ MainView.bitmap.getWidth());
				ratioY = (byte) (256 * (device.offsetY + device.height/2)/ MainView.bitmap.getHeight());
				date[3] = ratioX;
				date[4] = ratioY;
				typeBytes[i] = date[0];
				typeBytes[i+1] = date[1];
				typeBytes[i+2] = date[2];
				typeBytes[i+3] = date[3];
				typeBytes[i+4] = date[4];
				MyLog.i("i"+i,typeBytes[i]+"");
				MyLog.i("i"+(i+1),typeBytes[i+1]+"");
				MyLog.i("i"+(i+2),typeBytes[i+2]+"");
				MyLog.i("i"+(i+3),typeBytes[i+3]+"");
				MyLog.i("i"+(i+4),typeBytes[i+4]+"");
				i += 5;
			}
		}
		return typeBytes;
	}
	
	
	static public void formatConfigBytes(){
		
		byte [] date = new byte[5];
		int deviceCount = 0;
		for(Device device:MainView.devices){
			if(device.visible)
				deviceCount++;
		}
		typeBytes = new byte[deviceCount*5+9];
		if(Connect.isUpBitmap){
			byte[] bytes = new byte[8];
			Long time = System.currentTimeMillis();
			bytes = time.toString().getBytes();
			SharedPreferences pref= Connect.activity.getSharedPreferences("config", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor=pref.edit();
			editor.putLong("bitmaptime",Connect.configTime);
			editor.commit();
			for(int j =0;j<8;j++){
				typeBytes[j] = bytes[j]; 
			}
		}else {
			byte[] bytes = new byte[8];
			Long time = Connect.configTime;
			bytes = time.toString().getBytes();
			for(int j =0;j<8;j++){
				typeBytes[j] = bytes[j]; 
			}
		}
		typeBytes[8] = (byte)Room.count;
		int i = 10;
		for(Device device:MainView.devices){
			if(device.visible){
				date[0] = (byte)device.type;
				date[1] = (byte)device.index;
				date[2] = (byte)device.room;
				byte ratioX = 0;
				byte ratioY = 0;
				boolean hasRoom = false;
				for(Room room:Room.rooms){
					if(device.room == room.num){
						ratioX = (byte) (256 * (device.offsetX + device.width/2)/ room.outWidth);
						ratioY = (byte) (256 * (device.offsetY + device.height/2)/ room.outHeight);
						hasRoom = true;
					}
				}
				if(!hasRoom && device.room == 1){
					ratioX = (byte) (256 * (device.offsetX + device.width/2)/ Room.defaultWidth);
					ratioY = (byte) (256 * (device.offsetY + device.height/2)/ Room.defaultHeight);
				}else{
					ratioX = (byte) (256 * (device.offsetX + device.width/2)/ MainView.screenWidth);
					ratioY = (byte) (256 * (device.offsetY + device.height/2)/ MainView.screenHeight);
				}
				date[3] = ratioX;
				date[4] = ratioY;
				typeBytes[i] = date[0];
				typeBytes[i+1] = date[1];
				typeBytes[i+2] = date[2];
				typeBytes[i+3] = date[3];
				typeBytes[i+4] = date[4];
				MyLog.i("i"+i,typeBytes[i]+"");
				MyLog.i("i"+(i+1),typeBytes[i+1]+"");
				MyLog.i("i"+(i+2),typeBytes[i+2]+"");
				MyLog.i("i"+(i+3),typeBytes[i+3]+"");
				MyLog.i("i"+(i+4),typeBytes[i+4]+"");
				i += 5;
			}
		}
	}
	
	
	static public void formatTypeBytes(){
		typeBytes = new byte[MainActivity.deviceNames.length*16];
//		typeBytes = new byte[1024*1024];
		byte [] nums = new byte[MainActivity.deviceNames.length];
		for(int i = 0;i<MainActivity.pre.size();i++){
			nums[i] = Byte.parseByte((String) MainActivity.deviceNums.get(i).get("number"));
			byte []lim = new byte[16];
			for(int j = 0;j < 16;j++){
				lim[j] = (byte)0xff;
			}
			lim[0] = (byte)(i+1);
			lim[1] = (byte)nums[i];
			MyLog.i("nums"+i, nums[i]+"");
			if(nums[i]>0){
//				for(int j = 0;j < num;j++){
//					if(bytes[j/8]<0) bytes[j/8] = (byte)((bytes[j/8]+256)>>1);
//					else bytes[j/8] = (byte)(bytes[j/8]>>1);
//				}
				for(int j = 0;j < nums[i];j++){
					if(lim[j/8+2]<0) lim[j/8+2] = (byte)((lim[j/8+2]+256)>>1);
					else lim[j/8+2] = (byte)(lim[j/8+2]>>1);
				}
			}
			for(int k = 0;k <16;k++){
				MyLog.i("typeBytes["+(i*16+k)+"] = "+lim[k],"lim"+k);
				typeBytes[i*16+k] = lim[k];
				
			}
		}
		
	}
	
}
