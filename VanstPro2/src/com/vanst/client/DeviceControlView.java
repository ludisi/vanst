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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class DeviceControlView implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SDCARD_ROOT =Environment.getExternalStorageDirectory()+"/vanst/";
	byte type;
	byte index;
	Instruction [] instructions;
	ReInstruction [] reInstructions;
	byte[] DEVICE_IMAGE;
	byte[] DEVICE_ON_IMAGE;
	byte[] DEVICE_OFF_IMAGE;
	byte[] DEVICE_UNKNOW_IMAGE;
	byte[][] backgroundBitmaps;
	public DeviceControlView(byte type,byte index){
		this.type = type;
		this.index = index;
	}
	
	class Instruction implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int view;
		int toView;
		byte[] instructionBytes;
		String toast;
		int[] showPostions;
		String textString;
		int [] textPostions;
		public Instruction(int view, int toView, byte[] instructionBytes,
				String toast, int[] showPostions, String textString,
				int[] textPostions) {
			super();
			this.view = view;
			this.toView = toView;
			this.instructionBytes = instructionBytes;
			this.toast = toast;
			this.showPostions = showPostions;
			this.textString = textString;
			this.textPostions = textPostions;
		}
	}
	class ReInstruction implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int view;
		int toView;
		byte[] reInstructionBytes;
		String toast;
		int [] showPostions;
		String textString;
		int [] textPostions;
		public ReInstruction(int view, int toView, byte[] reInstructionBytes,
				String toast, int[] showPostions, String textString,
				int[] textPostions) {
			super();
			this.view = view;
			this.toView = toView;
			this.reInstructionBytes = reInstructionBytes;
			this.toast = toast;
			this.showPostions = showPostions;
			this.textString = textString;
			this.textPostions = textPostions;
		}
	}
	public Instruction createInstruction(int view, int toView, byte[] instructionBytes,
			String toast, int[] showPostions, String textString,
			int[] textPostions){
		return new Instruction(view, toView, instructionBytes, toast, showPostions, textString, textPostions);
		
	}
	
	public ReInstruction createReInstruction(int view, int toView, byte[] reInstructionBytes,
			String toast, int[] showPostions, String textString,
			int[] textPostions){
		return new ReInstruction(view, toView, reInstructionBytes, toast, showPostions, textString, textPostions);
	}
	static public DeviceControlView bytesToControlview(byte[] bytes){
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
		return (DeviceControlView) obj;  
	}
	static public byte[] controlviewToBytes(DeviceControlView deviceControlView){
		byte[] bytes = null;  
	    try {  
	        ByteArrayOutputStream bo = new ByteArrayOutputStream();  
	        ObjectOutputStream oo = new ObjectOutputStream(bo);  
	        oo.writeObject(deviceControlView);  
	  
	        bytes = bo.toByteArray();  
	  
	        bo.close();  
	        oo.close();  
	    } catch (Exception e) {
	    	MyLog.i("xxxxxxxxx", "error");
	        System.out.println("translation" + e.getMessage());  
	        e.printStackTrace();  
	    }  
	    return bytes; 
	}
	static DeviceControlView readControlView(String name){
		byte [] bytes = null;
		File file = new File(SDCARD_ROOT);
		if(!file.exists()) file.mkdir(); 
		File ff[] = file.listFiles();
		if(ff!=null&&ff.length!=0){
			for(File f:ff){
				if(name.equalsIgnoreCase(f.getName())){
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
		if(bytes != null) return bytesToControlview(bytes);
		else return null;
	}
	
	static DeviceControlView readControlView(byte type,byte index){
		byte [] bytes = null;
		File file = new File(SDCARD_ROOT);
		if(!file.exists()) file.mkdir(); 
		File ff[] = file.listFiles();
		if(ff!=null&&ff.length!=0){
			for(File f:ff){
				if(checkFileName(f.getName()) && compareName(type, index, f.getName())){
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
		if(bytes != null) return bytesToControlview(bytes);
		else return null;
	}
	
	static byte[] bitmapfileToBytes(File file){
		byte[] bytes = null;
		Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
		bytes= Room.bitmapToBytes(bitmap);
		if(bitmap !=null && !bitmap.isRecycled())bitmap.recycle();
		return bytes;
		
	}
	
	static void saveControlView(DeviceControlView deviceControlView,byte type,byte index){
		if(deviceControlView != null){
			byte [] bytes = controlviewToBytes(deviceControlView);
			if(bytes != null){
				int iType = type;
				if(iType < 0) iType += 256;
				int iIndex = index;
				if(iIndex < 0) iIndex += 256;
				File file = new File(SDCARD_ROOT);
				if(!file.exists()) file.mkdir(); 
				File f = new File(SDCARD_ROOT + (iType *256 + iIndex) + ".vanstcv");
				if(f.exists()) f.delete();
				try {
					f.createNewFile();
					FileOutputStream fOut = new FileOutputStream(f);
					fOut.write(bytes);
					fOut.flush();
					fOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	static void saveControlView(DeviceControlView deviceControlView,String filename){
		if(deviceControlView != null){
			byte [] bytes = controlviewToBytes(deviceControlView);
			if(bytes != null){
				File file = new File(SDCARD_ROOT);
				if(!file.exists()) file.mkdir(); 
				File f = new File(SDCARD_ROOT + filename + ".vanstcv");
				if(f.exists()) f.delete();
				try {
					f.createNewFile();
					FileOutputStream fOut = new FileOutputStream(f);
					fOut.write(bytes);
					fOut.flush();
					fOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	static boolean compareName(byte type,byte index,String name){
		int iType = type;
		if(iType < 0) iType += 256;
		int iIndex = index;
		if(iIndex < 0) iIndex += 256;
		int i = name.lastIndexOf('.');
		if(i>-1&&i<name.length()){
			try {
				int iName = Integer.valueOf(name.substring(0, i));
				if(iName == iType * 256 + iIndex){
					return true;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	static String[] getVanstcvNames(){
		String [] names = null;
		File file = new File(SDCARD_ROOT);
		if(!file.exists()) file.mkdir(); 
		File ff[] = file.listFiles();
		int count = 0;
		if(ff!=null && ff.length != 0){
			for(File f:ff){
				if(checkFileName(f.getName())){
					count++;
				}
			}
			if(count != 0){
				names = new String[count];
				int i =0;
				for(File f:ff){
					if(checkFileName(f.getName())){
						names[i] = f.getName();
						i++;
					}
				}
			}
		}
		return names;
	}
	
	static boolean checkFileName(String name){
		if(name != null&&name.length()>0){
			int i = name.lastIndexOf('.');
			if(i>-1&&i<name.length()){
				String lastName = name.substring(i+1);
				if(lastName.equalsIgnoreCase("vanstcv")){
					return true;
				}
			}
		}
		return false;
	}
	
}
