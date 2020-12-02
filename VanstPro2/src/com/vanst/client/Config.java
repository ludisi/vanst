package com.vanst.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;




public class Config implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String FILE_ROOT = Environment.getExternalStorageDirectory()+"/vanst/";
	public static final String SDCARD_ROOT =Environment.getExternalStorageDirectory()+"/vanst/";
	long time;
	String path;
	String remark;
	byte[] deviceconfigs;
	byte[] devicemodes;
	byte[] bitmap;
	Linkapp [] linkapps;
	public Config(long time, String path, byte[] deviceconfigs, byte[] bytes) {
		super();
		this.time = time;
		this.path = path;
		this.deviceconfigs = deviceconfigs;
		this.bitmap = bytes;
	}
	static public Config bytesToConfig(byte[] bytes){
		Object obj = null; 
		try {  
		    ByteArrayInputStream bi = new ByteArrayInputStream(bytes);  
		    ObjectInputStream oi = new ObjectInputStream(bi);  
		    obj = null; 
		    obj = oi.readObject();  
		    bi.close();  
		    oi.close();  
		} catch (Exception e) {  
		    System.out.println("translation" + e.getMessage());  
		    e.printStackTrace();  
		}  
		return (Config) obj;  
	}
	static public boolean checkConfigBytes(byte[] bytes){
		try {  
		    ByteArrayInputStream bi = new ByteArrayInputStream(bytes);  
		    ObjectInputStream oi = new ObjectInputStream(bi);  
		    oi.readObject();  
		    bi.close();  
		    oi.close();  
		} catch (Exception e) {  
			e.printStackTrace();
		    return false;  
		}  
		return true;
		
	}
	
	static public byte[] configToBytes(Config config){
		byte[] bytes = null;  
	    try {  
	        ByteArrayOutputStream bo = new ByteArrayOutputStream();  
	        ObjectOutputStream oo = new ObjectOutputStream(bo);  
	        oo.writeObject(config);  
	  
	        bytes = bo.toByteArray();  
	  
	        bo.close();  
	        oo.close();  
	    } catch (Exception e) {  
	        System.out.println("translation" + e.getMessage());  
	        e.printStackTrace();  
	    }  
	    return bytes; 
	}
	static public void intiConfig(){
		MyLog.i("Media_mounted", Environment.MEDIA_MOUNTED);
		MyLog.i("EternalStorageState", Environment.getExternalStorageState());
		MyLog.i("storageDirctory",Environment.getExternalStorageDirectory().toString());
		File file = new File(FILE_ROOT);
		if(!file.exists()) file.mkdir(); 
		File ff[] = file.listFiles();
		if(ff!=null&&ff.length!=0){
			for(File f:ff){
				int num = 0;
				if (f.isFile() && (num = checkFileName(f.getName()))!=0) {
					readConfig(num);
				}
			}
		}
	}
	
	static public String [] getConfigNames(){
		String[] names = null;
		File file = new File(FILE_ROOT);
		if(!file.exists()) file.mkdir(); 
		File ff[] = file.listFiles();
		int count = 0;
		if(ff !=null && ff.length!=0){
			for(File f:ff){
				if (f.isFile() && checkConfigName(f.getName())){
					count++;
				}
			}
		}
		MyLog.i("count",count+"");
		if(count != 0){
			names = new String[count];
			int i = 0;
			for(File f:ff){
				if (f.isFile() && checkConfigName(f.getName())){
					names[i] = f.getName();
					i++;
				}
			}
		}
		return names;
	}
	static public void deleteConfig(String configName){
		File file = new File(FILE_ROOT);
		if(!file.exists()) file.mkdir(); 
		File ff[] = file.listFiles();
		for(File f:ff){
			if (f.isFile() && configName.equals(f.getName())){
				f.delete();
				try {
					f.createNewFile();
					Config config = new Config(System.currentTimeMillis(), FILE_ROOT+configName, null, null);
					saveConfig(config, configName);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	static public byte[] readFile(String path){
		byte[] bytes = null;
		File file = new File(path);
		if(file != null && file.isFile()){
			try {
				FileInputStream fIn = new FileInputStream(file);
				bytes = new byte[(int)file.length()];
				fIn.read(bytes);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes;
	}
	
	static public byte[] uploadConfig(String configName){
		byte[] bytes = null;
		Config config = bytesToConfig(Config.readConfig(configName));
		if(config !=null) config.bitmap = null;
		bytes = configToBytes(config);
		return bytes;
	}
	
	static public byte[] readConfig(String configName){
		byte[] bytes = null;
		File file = new File(FILE_ROOT);
		if(!file.exists()) file.mkdir(); 
		File ff[] = file.listFiles();
		if(ff != null&& ff.length !=0){
			for(File f:ff){
				if (f.isFile() && configName.equals(f.getName())){
					try {
						FileInputStream fIn = new FileInputStream(f);
						bytes = new byte[(int)f.length()];
						fIn.read(bytes);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return bytes;
	}
	
	static public Config readConfig(int configNum){
		Config config = null;
		File file = new File(FILE_ROOT+"config"+configNum+".vanst");
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
		if(bytes == null){
			config = new Config(System.currentTimeMillis(), FILE_ROOT+"config"+configNum+".vanst", null, null);
			saveConfig(config, configNum);
		}else{
			config = bytesToConfig(bytes);
		}
		if(config == null){
			config = new Config(System.currentTimeMillis(), FILE_ROOT+"config"+configNum+".vanst", null, null);
			saveConfig(config, configNum);
		}
		
		bytes = null;
		
		file = new File(SDCARD_ROOT);
		if(!file.exists()) file.mkdir(); 
		File ff[] = file.listFiles();
		int num = 0;
		if(ff!=null&&ff.length!=0){
			for(File f:ff){
				if (f.isFile() && (num = checkFileName(f.getName()))!=0) {
					if(configNum == num){
						try {
							if(f.isFile() && f.exists()) {
								
								MyLog.i("configNum",f.getName());
								bytes = fomateBitmapBytes(f);
								f.delete();
							}
						
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		if(bytes !=null && configNum != 0){
			config.bitmap = bytes;
			saveConfig(config, configNum);
		}
		return config;
	}
	static void saveConfig(byte[] bytes,String configName){
		File f = new File(FILE_ROOT+configName);
	    FileOutputStream fOut = null;
	    try {
	    	if(bytes != null){
		    	if(f.isFile()&&f.exists()) f.delete();
			     f.createNewFile();
			     fOut = new FileOutputStream(f);
			     fOut.write(bytes);
			     fOut.flush();
			     fOut.close();
	    	}
	     } catch (Exception e) {
	     e.printStackTrace();
	     }
	}
	
	static void updateConfig(byte[] bytes,int configNum){
		Config config = readConfig(configNum);
		if(config == null){
			saveConfig(bytes, configNum);
		}else{
			Config donwConfig = bytesToConfig(bytes);
			config.deviceconfigs = donwConfig.deviceconfigs;
			config.devicemodes = donwConfig.devicemodes;
			config.path = donwConfig.path;
			config.time = donwConfig.time;
			config.remark = donwConfig.remark;
			saveConfig(config, configNum);
		}
	}
	
	static void saveConfig(byte[] bytes,int configNum){
		File f = new File(FILE_ROOT+"config"+configNum+".vanst");
	    FileOutputStream fOut = null;
	    try {
	    	if(bytes != null){
		    	if(f.isFile()&&f.exists()) f.delete();
			     f.createNewFile();
			     fOut = new FileOutputStream(f);
			     fOut.write(bytes);
			     fOut.flush();
			     fOut.close();
	    	}
	     } catch (Exception e) {
	     e.printStackTrace();
	     }
	}
	
	
	static void saveConfig(Config config,String configName){
		File f = new File(FILE_ROOT+configName);
	    FileOutputStream fOut = null;
	    try {
	    	if(config !=null){
		    	if(f.isFile()&&f.exists()) f.delete();
			     f.createNewFile();
			     fOut = new FileOutputStream(f);
			     byte[]bs = configToBytes(config);
			     fOut.write(bs);
			     fOut.flush();
			     fOut.close();
	    	}
	     } catch (Exception e) {
	    	 Connect.synchronizationHandler.sendEmptyMessage(Connect.SDCARD_FAILED);
	     e.printStackTrace();
	     }
	}
	static void saveConfig(Config config,int configNum){
		File f = new File(FILE_ROOT+"config"+configNum+".vanst");
	    FileOutputStream fOut = null;
	    try {
	    	if(config !=null){
		    	if(f.isFile()&&f.exists()) f.delete();
			     f.createNewFile();
			     fOut = new FileOutputStream(f);
			     byte[]bs = configToBytes(config);
			     fOut.write(bs);
			     fOut.flush();
			     fOut.close();
	    	}
	     } catch (Exception e) {
	    	 Connect.synchronizationHandler.sendEmptyMessage(Connect.SDCARD_FAILED);
	     e.printStackTrace();
	     }
	}
	
	
	static byte[] fomateBitmapBytes(String filePath){
		int w = 1920;
		int h = 1080;
		Bitmap bitmap = null;
		BitmapFactory.decodeFile(filePath, Room.optReadBound);
		int width = Room.optReadBound.outWidth;
		int height = Room.optReadBound.outHeight;
		int inSampleSize = 1;
		if(width > height){
			if(width > w && height > h){
				if(width / w > height / h){
					Room.optReadBitmap.inSampleSize = inSampleSize + height / h;
				}else{
					Room.optReadBitmap.inSampleSize = inSampleSize + width / w;
				}
			}else{
				Room.optReadBitmap.inSampleSize = 1;
			}
		}else{
			if(width > h && height > w){
				if(width / h > height / w){
					Room.optReadBitmap.inSampleSize = inSampleSize + width / h;
				}else{
					Room.optReadBitmap.inSampleSize = inSampleSize + height / w;
				}
			}else{
				Room.optReadBitmap.inSampleSize = 1;
			}
		}
		bitmap = BitmapFactory.decodeFile(filePath, Room.optReadBitmap);
		if(bitmap != null) {
			MyLog.i("width",bitmap.getWidth()+"");
			MyLog.i("height",bitmap.getHeight()+"");
			byte[] bytes= Room.bitmapToBytes(bitmap);
			bitmap.isRecycled();
			return bytes;
		}
		else return null;
	}
	
	
	static byte[] fomateBitmapBytes(File file){
		int w = 1920;
		int h = 1080;
		Bitmap bitmap = null;
		BitmapFactory.decodeFile(file.getPath(), Room.optReadBound);
		int width = Room.optReadBound.outWidth;
		int height = Room.optReadBound.outHeight;
		int inSampleSize = 1;
		if(width > height){
			if(width > w && height > h){
				if(width / w > height / h){
					Room.optReadBitmap.inSampleSize = inSampleSize + height / h;
				}else{
					Room.optReadBitmap.inSampleSize = inSampleSize + width / w;
				}
			}else{
				Room.optReadBitmap.inSampleSize = 1;
			}
		}else{
			if(width > h && height > w){
				if(width / h > height / w){
					Room.optReadBitmap.inSampleSize = inSampleSize + width / h;
				}else{
					Room.optReadBitmap.inSampleSize = inSampleSize + height / w;
				}
			}else{
				Room.optReadBitmap.inSampleSize = 1;
			}
		}
		bitmap = BitmapFactory.decodeFile(file.getPath(), Room.optReadBitmap);
		if(bitmap != null) {
			MyLog.i("width",bitmap.getWidth()+"");
			MyLog.i("height",bitmap.getHeight()+"");
			byte[] bytes= Room.bitmapToBytes(bitmap);
			bitmap.isRecycled();
			return bytes;
		}
		else return null;
	}
	
//	static public void copyFile(int room,String oldPath) {   
//		String newPath = FILE_ROOT+room+".jpg";
//		
//		File newFile = new File(newPath);
//		File oldFile = new File(oldPath);
//		
//		try {
//			FileInputStream fIn = new FileInputStream(oldFile);
//			byte[] bytes = new byte[(int)oldFile.length()];
//			fIn.read(bytes);
//			newFile.createNewFile();
//			FileOutputStream fOut = new FileOutputStream(newFile);
//		     fOut.write(bytes);
//		     fOut.flush();
//		     fOut.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}	
	
	
//	static void copyBackgrond(int room,String path){
//		File file = new File(path);
//		File file2 = new File(FILE_ROOT+room+".jpg");
//		file2.compareTo(file);
//	}
	
	static void askConfig(Config config,MainView mainView){
//		if(MainView.bitmap!=null&&!MainView.bitmap.isRecycled())MainView.bitmap.recycle();
//		if(MainView.bgBitmap!=null&&!MainView.bgBitmap.isRecycled())MainView.bgBitmap.recycle();
		Room.loadBackground(MainView.room, config.bitmap,mainView);
		MainView.remark = config.remark;
		MainView.devices = new ArrayList<Device>();
		int l =0;
		if(config.deviceconfigs!=null && config.deviceconfigs.length % 5 == 0){
			while(l < config.deviceconfigs.length){
				if(config.deviceconfigs[l] > 0 && config.deviceconfigs[l]<= Device.DEVICE_TYPE_COUNT && config.deviceconfigs[l+1] > 0 && config.deviceconfigs[l+1] < 100){
					Device device = new Device(mainView, config.deviceconfigs[l], config.deviceconfigs[l+1]);
					device.room = config.deviceconfigs[l+2];
//					if(device.room < 0) device.room += 256;
//					device.allOn = device.room % 2;
//					device.allOff = device.room / 2 % 2;
//					device.moshi1 = device.room / 4 % 2;
//					device.moshi2 = device.room / 8 % 2;
//					device.moshi3 = device.room / 16 % 2;
//					device.moshi4 = device.room / 32 % 2;
					device.offsetX = 0;
					device.offsetY = 0;
					device.posRX = config.deviceconfigs[l+3];
					device.posRY = config.deviceconfigs[l+4];
					if(MainView.bitmap!=null){
						if(config.deviceconfigs[l+3]<0) device.offsetX = (config.deviceconfigs[l+3]+256) * MainView.bitmap.getWidth() / 256 - device.width / 2;
						else device.offsetX = config.deviceconfigs[l+3] * MainView.bitmap.getWidth() / 256 - device.width / 2;
						if(config.deviceconfigs[l+4]<0) device.offsetY = (config.deviceconfigs[l+4]+256) * MainView.bitmap.getHeight() / 256 - device.height / 2;
						else device.offsetY = config.deviceconfigs[l+4] * MainView.bitmap.getHeight() /256 - device.height / 2;
					}
					if(config.devicemodes!=null && config.devicemodes.length % 5 == 0 && l < config.devicemodes.length){
						device.allOn = config.devicemodes[l] % 2;
						device.allOff = config.devicemodes[l] / 2 % 2;
						device.moshi1 = config.devicemodes[l+1];
						device.moshi2 = config.devicemodes[l+2];
						device.moshi3 = config.devicemodes[l+3];
						device.moshi4 = config.devicemodes[l+4];
					}
					MyLog.i("offsetX", device.offsetX+"");
					MyLog.i("offsetY", device.offsetY+"");
					MainView.devices.add(device);
				}
				l += 5;
			}
			MyLog.i("SIZE", MainView.devices.size()+"");
		}
	}
	static boolean checkConfigName(String name){
		if(name != null&&name.length()>0){
			int i = name.lastIndexOf('.');
			if(i>-1&&i<name.length()){
				String lastName = name.substring(i+1);
				try {
					MyLog.i("name", name.substring(6,i));
					int num = Integer.valueOf(name.substring(6, i));
					if(num < 1 || num > 99) return false;
				} catch (Exception e) {
					return false;
				}
				if(lastName.equalsIgnoreCase("vanst")){
					return true;
				}
			}
		}
		return false;
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
	
	static void reInitRooms(int count){
		FileOutputStream fOut;
		for(int i = 1;i <= count;i++){
			File file = new File(FILE_ROOT+"config"+i+".vanst");
			if(!file.exists()){
				Config config = new Config(System.currentTimeMillis(), FILE_ROOT+"config"+i+".vanst", null, null);
			    try {
			    	 file.createNewFile();
					 fOut = new FileOutputStream(file);
					 fOut.write(configToBytes(config));
				     fOut.flush();
				     fOut.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	static void getConfigCl(){
		byte[][] bytess;
		File file = new File(FILE_ROOT);
		if(!file.exists()) file.mkdir(); 
		File ff[] = file.listFiles();
		int count = 0;
		if(ff!=null&&ff.length!=0){
			for(File f:ff){
				if (f.isFile() && checkConfigName(f.getName())){
					count++;
				}
			}
		}
		bytess = new byte[count][];
		int i = 0;
		if(ff!=null&&ff.length!=0){
			for(File f:ff){
				if (f.isFile() && checkConfigName(f.getName())){
					Config config = bytesToConfig(readConfig(f.getName()));
					if(config != null && config.deviceconfigs != null){
						bytess[i] = config.deviceconfigs;
					}
					i++;
				}
			}
		}
		int byteLen = 0;
		for(byte[] bytes:bytess){
			if(bytes!=null)
			byteLen += bytes.length;
		}
		i = 1;
		Device.typeBytes = new byte[byteLen+1];
		if(bytess != null){
			for(byte []bytes:bytess){
				if(bytes != null){
					for(byte b:bytes){
						Device.typeBytes[i] = b;
						i++;
					}
				}
			}
		}
		i = 0;
		for(byte b:Device.typeBytes){
			MyLog.i(i+"",b+"");
			i++;
		}
		Connect.deleteTpye("cl");
	}
	public class Linkapp implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		byte type;
		byte index;
		String appPackName;
	}
}
