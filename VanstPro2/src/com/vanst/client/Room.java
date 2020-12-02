package com.vanst.client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.View;


public class Room {
	static int count;
	static int defaultAngle;
	static Bitmap defaultBitmap;
	static Bitmap defaultBitmap2;
	static int defaultWidth;
	static int defaultHeight;
	static ArrayList<Room> rooms = new ArrayList<Room>();
	int num;
	Bitmap roomBg;
	boolean isRotato;
	boolean isUp;
	static BitmapFactory.Options optReadBitmap;
	static BitmapFactory.Options optReadBound;
	String path;
	int width;
	int height;
	int outWidth;
	int outHeight;
	static void initRooms(MainView mainView){
		optReadBitmap = new BitmapFactory.Options();
		optReadBitmap.inPreferredConfig = Config.RGB_565;
		optReadBitmap.inPurgeable = true;  
		optReadBitmap.inInputShareable = true; 
		optReadBound = new BitmapFactory.Options();
		optReadBound.inJustDecodeBounds = true;
		
	}
	public static Bitmap readBitMap(View view, int resId){  
        BitmapFactory.Options opt = new BitmapFactory.Options();  
        opt.inPreferredConfig = Bitmap.Config.RGB_565;   
        opt.inPurgeable = true;  
        opt.inInputShareable = true;  
          //获取资源图片  
        InputStream is = view.getResources().openRawResource(resId);  
        return BitmapFactory.decodeStream(is,null,opt);  
    }
	public void ssfw(){
		path.substring(path.lastIndexOf("/"));
		path.lastIndexOf("/");
	}
	
	static public void readFile(Room room){
		MyLog.i(room.num+"",room.path);
		File file = new File(room.path);
		byte[] bytes = null;
		try {
			FileInputStream fIn = new FileInputStream(file);
			bytes = new byte[(int)file.length()];
			fIn.read(bytes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(bytes!=null && bytes.length > 0){
			Device.typeBytes = bytes;
			Connect.deleteTpye("room"+room.num);
		}
	}
	
	static public void loadBackground(Room room,byte[] bytes,MainView mainView){
		if(bytes != null){
			BitmapFactory.decodeByteArray(bytes, 0, bytes.length,optReadBound);
			room.width = optReadBound.outWidth;
			room.height = optReadBound.outHeight;
			if(room.width > room.height){
				room.isRotato = true;
				room.width = optReadBound.outHeight;
				room.height = optReadBound.outWidth;
			}
			if(room.width > MainView.screenWidth && room.height > MainView.screenHeight){
				if(MainView.screenWidth * 1.0/room.width > MainView.screenHeight *1.0/room.height){
					room.outWidth = (int)MainView.screenWidth;
					room.outHeight = (int)(room.height * MainView.screenWidth / room.width); 
				}else{
					room.outWidth = (int)(room.width * MainView.screenHeight / room.height);
					room.outHeight = (int)(MainView.screenHeight);
				}
			}else{
				desighRoomBitmap(room);
				room.outWidth = room.width;
				room.outHeight = room.height;
			}
//			int inSampleSize = 0;
//			if(room.width>MainView.screenWidth || room.height > MainView.screenHeight){
//				if(MainView.screenWidth * 1.0/room.width < MainView.screenHeight *1.0/room.height){
//					inSampleSize = (int) (room.width/MainView.screenWidth);
//				}else{
//					inSampleSize = (int) (room.height/MainView.screenHeight);
//				}
//			}
//			optReadBitmap.inSampleSize = inSampleSize+1;
			optReadBitmap.inSampleSize = 1;
			Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,optReadBitmap);
			Bitmap bitmap2 = designRoomBitmap(bitmap);
			if(bitmap != bitmap2) bitmap.recycle();
			bitmap = bitmap2;
//			room.roomBg = designRoomBitmap(bitmap);
//			MainView.bitmap = room.roomBg;
			float ratio = 1;
			if(MainView.screenWidth * 1.0/bitmap.getWidth()> MainView.screenHeight *1.0/bitmap.getHeight()){
				ratio = MainView.screenWidth / bitmap.getWidth();
			}else{
				ratio = MainView.screenHeight / bitmap.getHeight();
			}
//			if(MainView.screenWidth * 1.0/bitmap.getWidth()< MainView.screenHeight *1.0/bitmap.getHeight()){
//				ratio = MainView.screenWidth / bitmap.getWidth();
//			}else{
//				ratio = MainView.screenHeight / bitmap.getHeight();
//			}
			Room.defaultBitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*ratio),(int)( bitmap.getHeight()*ratio), true);
			if(Room.defaultBitmap != bitmap) bitmap.recycle();
			MainView.bitmap = Room.defaultBitmap;
//			room.roomBg = designRoomBitmap(Room.defaultBitmap);
//			MainView.bitmap = room.roomBg;
//			if(bitmap != null && room.roomBg != bitmap) bitmap.recycle();
//			optReadBitmap.inSampleSize = 1;
//			Bitmap linBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,optReadBitmap);
//			if(linBitmap.getWidth() > linBitmap.getHeight()){
//				Matrix matrix = new Matrix();
//				matrix.postRotate(90);
//				if(MainView.bgBitmap != null)MainView.bgBitmap.recycle();
//				MainView.bgBitmap = Bitmap.createBitmap(linBitmap, 0, 0, linBitmap.getWidth(), linBitmap.getHeight(), matrix, true);
//				linBitmap.recycle();
//			}else{
//				MainView.bgBitmap = linBitmap;
//			}
//			MainView.bgratio = (float) (MainView.bitmap.getWidth() *1.0 / MainView.bgBitmap.getWidth());
//			if(bitmap != null && room.roomBg != bitmap) bitmap.recycle();
			
		}else{
			Bitmap bitmap = Room.readBitMap(mainView, R.drawable.default1);
//			if(MainView.roomNum == 2)	bitmap = Room.readBitMap(mainView, R.drawable.default2);
//			else bitmap = Room.readBitMap(mainView, R.drawable.default1);
			float ratio = 1;
			if(MainView.screenWidth * 1.0/bitmap.getWidth()> MainView.screenHeight *1.0/bitmap.getHeight()){
				ratio = MainView.screenWidth / bitmap.getWidth();
			}else{
				ratio = MainView.screenHeight / bitmap.getHeight();
			}
			Room.defaultBitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*ratio),(int)( bitmap.getHeight()*ratio), true);
			if(Room.defaultBitmap != bitmap) bitmap.recycle();
			MainView.bitmap = Room.defaultBitmap;
//			MainView.bgBitmap = MainView.bitmap;
//			MainView.bgratio = 1;
		}
//		else{
//			Bitmap bitmap = Room.readBitMap(mainView, R.drawable.default2);
//			float ratio = 1;
//			if(MainView.screenWidth * 1.0/bitmap.getWidth()> MainView.screenHeight *1.0/bitmap.getHeight()){
//				ratio = MainView.screenWidth / bitmap.getWidth();
//			}else{
//				ratio = MainView.screenHeight / bitmap.getHeight();
//			}
//			Room.defaultBitmap2 = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*ratio),(int)( bitmap.getHeight()*ratio), true);
//			if(Room.defaultBitmap2 != bitmap) bitmap.recycle();
//			MainView.bitmap = Room.defaultBitmap2;
//		} 
		
	}
	
	static public byte[] bitmapToBytes(Bitmap bitmap){
		ByteArrayOutputStream os = new ByteArrayOutputStream(); 
		bitmap.compress(CompressFormat.JPEG, 100, os);
		byte [] rtn = os.toByteArray();
		return rtn;
	}
	
	
	static public void desighRoomBitmap(Room room){
		if(room.width/MainView.screenWidth < 1 || room.height/MainView.screenHeight < 1 ){
			if(MainView.screenWidth * 1.0/room.width > MainView.screenHeight *1.0/room.height){
				room.width *= MainView.screenWidth/room.width;
				room.height *= MainView.screenWidth/room.width; 
			}else{
				room.width *= MainView.screenHeight/room.height;
				room.height *= MainView.screenHeight/room.height;
			}
		}
	}
	
	static public Bitmap designRoomBitmap(Bitmap bitmap){
		if(bitmap.getWidth() > bitmap.getHeight()){
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			Bitmap linBitmap = bitmap;
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			linBitmap.recycle();
		}
//		if(bitmap.getWidth()/MainView.screenWidth < 1 || bitmap.getHeight()/MainView.screenHeight < 1 ){
//			Bitmap linBitmap = bitmap;
//			if(MainView.screenWidth * 1.0/bitmap.getWidth() > MainView.screenHeight *1.0/bitmap.getHeight()){
//				bitmap = dePaint(bitmap, (float) (MainView.screenWidth * 1.0/bitmap.getWidth()));
//			}else{
//				bitmap = dePaint(bitmap, (float) (MainView.screenHeight * 1.0/bitmap.getHeight()));
//			}
//			if(linBitmap !=null && linBitmap != bitmap && !linBitmap.isRecycled()) linBitmap.recycle();
//		}
		return bitmap;
	}
	static public Bitmap dePaint(Bitmap bitmap,float ratio){
		 Matrix matrix = new Matrix(); 
		  matrix.postScale(ratio,ratio); //长和宽放大缩小的比例
//		  Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		  Bitmap resizeBmp;
		  try {
			  resizeBmp = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*ratio), (int)(bitmap.getHeight()*ratio),true);
		  } catch (OutOfMemoryError e) {
			  resizeBmp = bitmap;
		  }
		  return resizeBmp;
	}
	
	
	static int checkFileName(String name){
		if(name != null&&name.length()>0){
			int i = name.lastIndexOf('.');
			if(i>-1&&i<name.length()){
				String lastName = name.substring(i+1);
				try {
					Integer.valueOf(name.substring(0, i));
				} catch (Exception e) {
					return 0;
				}
				if(lastName.equalsIgnoreCase("jpg")||lastName.equalsIgnoreCase("png")||lastName.equalsIgnoreCase("bmp")||lastName.equalsIgnoreCase("jpeg")){
					return Integer.valueOf(name.substring(0,i));
				}
			}
		}
		return 0;
	}
}
