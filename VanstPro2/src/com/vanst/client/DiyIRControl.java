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

import android.os.Environment;

public class DiyIRControl implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int [][] instructions;
	byte[] backgroud;
	int [] backgroudrect;
	String remark;
	public DiyIRControl(int [][] instructions ,byte[] backgroud,int[] backgroudrect,String remark){
		this.instructions = instructions;
		this.backgroud = backgroud;
		this.backgroudrect = backgroudrect;
		this.remark = remark;
	}
	
	static public byte[] diyIRToBytes(DiyIRControl diyIRControl){
		byte[] bytes = null;  
	    try {  
	        ByteArrayOutputStream bo = new ByteArrayOutputStream();  
	        ObjectOutputStream oo = new ObjectOutputStream(bo);  
	        oo.writeObject(diyIRControl);  
	  
	        bytes = bo.toByteArray();  
	  
	        bo.close();  
	        oo.close();  
	    } catch (Exception e) {  
	        System.out.println("translation" + e.getMessage());  
	        e.printStackTrace();  
	    }  
	    return bytes; 
	}
	static public DiyIRControl bytesToDiyIRControl(byte[] bytes){
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
		return (DiyIRControl) obj;  
	}
	
	static DiyIRControl load(int index){
		File f = new File(Environment.getExternalStorageDirectory()+"/vanst/"+"diy"+index);
		DiyIRControl diyIRControl = null;
		FileInputStream fIn;
		try {
			fIn = new FileInputStream(f);
			byte[] bytes = new byte[(int)f.length()];
			fIn.read(bytes);
			diyIRControl = bytesToDiyIRControl(bytes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return diyIRControl;
		
	}
	
	static void save(int index,DiyIRControl diyIRControl){
		File f = new File(Environment.getExternalStorageDirectory()+"/vanst/"+"diy"+index);
	    FileOutputStream fOut = null;
	    try {
	    	if(diyIRControl != null){
	    		if(f.isFile()&&f.exists()) f.delete();
			     f.createNewFile();
			     fOut = new FileOutputStream(f);
			     byte[]bs = diyIRToBytes(diyIRControl);
			     fOut.write(bs);
			     fOut.flush();
			     fOut.close();
	    	}
	     } catch (Exception e) {
	     e.printStackTrace();
	     }
	}
}
