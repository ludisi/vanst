package com.vanst.client;


import java.io.File;
import java.util.ArrayList;


import com.vanst.client.DeviceControlView.Instruction;
import com.vanst.client.DeviceControlView.ReInstruction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DesignActivity extends Activity{
	Button deisgnenter;
	Button addinstructions;
	Button deleteinstructions;
	Button addreinstructions;
	Button deletereinstructions;
	Button enterButton;
	Button openButton;
	Button instructionenter;
	TextView instructionCount;
	TextView reInstructionCount;
	EditText view;
	EditText toView;
	EditText instructionBytes;
	EditText x1;
	EditText x2;
	EditText y1;
	EditText y2;
	EditText toast;
	EditText textString;
	EditText tx;
	EditText ty;
	EditText filepath;
	boolean isInstruction;
	DeviceControlView deviceControlView;
	ArrayList<byte[]> bitmaps = new ArrayList<byte[]>();
	ArrayList<DeviceControlView.Instruction> instructions = new ArrayList<DeviceControlView.Instruction>();
	ArrayList<DeviceControlView.ReInstruction> reInstructions = new ArrayList<DeviceControlView.ReInstruction>();
	String path;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.design);
		instructionCount = (TextView)findViewById(R.id.design_instructionCount);
		reInstructionCount = (TextView)findViewById(R.id.design_reInstructionCount);
		filepath = (EditText)findViewById(R.id.design_filepath);
		addinstructions = (Button)findViewById(R.id.design_addinstructions);
		addreinstructions = (Button)findViewById(R.id.design_addreinstructions);
		deleteinstructions = (Button)findViewById(R.id.design_deleteinstructions);
		deletereinstructions = (Button)findViewById(R.id.design_detelereinstructions);
		enterButton = (Button)findViewById(R.id.design_enter);
		deviceControlView = new DeviceControlView((byte)0xff,(byte)0xff);
		addinstructions.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LayoutInflater layoutInflater = LayoutInflater.from(DesignActivity.this); 
		        View designView = layoutInflater.inflate(R.layout.instruction, null); 
				AlertDialog.Builder builder = new AlertDialog.Builder(DesignActivity.this);
				builder.setTitle("发送指令编辑");
				builder.setIcon(android.R.drawable.ic_menu_edit);
				builder.setView(designView);
				view = (EditText)designView.findViewById(R.id.instruction_view);
				toView = (EditText)designView.findViewById(R.id.instruction_toview);
				instructionBytes = (EditText)designView.findViewById(R.id.instruction_instructionBytes);
				x1 = (EditText)designView.findViewById(R.id.instruction_x1);
				y1 = (EditText)designView.findViewById(R.id.instruction_y1);
				x2 = (EditText)designView.findViewById(R.id.instruction_x2);
				y2 = (EditText)designView.findViewById(R.id.instruction_y2);
				tx = (EditText)designView.findViewById(R.id.instruction_tx);
				ty = (EditText)designView.findViewById(R.id.instruction_ty);
				toast = (EditText)designView.findViewById(R.id.instruction_toast);
				textString = (EditText)designView.findViewById(R.id.instruction_text);
				instructionenter = (Button)designView.findViewById(R.id.instruction_enter);
				instructionenter.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						boolean error = false;
						String errorString = "";
						int viewInstruction = 0;
						int toViewInstruction = 0;
						String toastInstruction = null;
						String textInstruction = null;
						int [] showPostions = null;
						int [] textPostions = null;
						byte[] bytes = null;
						try {
							viewInstruction = Integer.parseInt(view.getText().toString());
						} catch (Exception e) {
							error = true;
							errorString += "所在界面输入有误！";
						}
						try {
							toViewInstruction = Integer.parseInt(toView.getText().toString());
						} catch (Exception e) {
							error = true;
							errorString += "去向界面输入有误！";
						}	
						try {
							toastInstruction = toast.getText().toString();
						} catch (Exception e) {
							error = true;
							errorString += "弹出提示输入有误！";
						}
						try {
							textInstruction = textString.getText().toString();
						} catch (Exception e) {
							error = true;
							errorString += "状态提示输入有误！";
						}
						try {
							showPostions = new int[]{Integer.parseInt(x1.getText().toString()),
									Integer.parseInt(y1.getText().toString()),		
									Integer.parseInt(x2.getText().toString()),
									Integer.parseInt(y2.getText().toString()),
									};
						} catch (Exception e) {
							error = true;
							errorString += "坐标位置输入有误！";
						}
						try {
							if(tx.getText().toString().length() != 0 && ty.getText().toString().length() != 0)
							textPostions = new int[]{Integer.parseInt(tx.getText().toString()),
									Integer.parseInt(ty.getText().toString())
									};
						} catch (Exception e) {
							error = true;
							errorString += "状态位置输入有误！";
						}	
						try {
							if(instructionBytes.getText().toString().length() != 0){
								String[] bytesStrings = instructionBytes.getText().toString().split(" ");
								bytes = new byte[bytesStrings.length];
								for(int i =0;i<bytesStrings.length;i++){
									if(bytesStrings[i].length() == 2 && (
											bytesStrings[i].charAt(0) >='0' && 
											bytesStrings[i].charAt(0) <='9' ||
											bytesStrings[i].charAt(0) >='a' && 
											bytesStrings[i].charAt(0) <='z' ||
											bytesStrings[i].charAt(0) >='A' && 
											bytesStrings[i].charAt(0) <='Z'
											)&&(
											bytesStrings[i].charAt(1) >='0' && 
											bytesStrings[i].charAt(1) <='9' ||
											bytesStrings[i].charAt(1) >='a' && 
											bytesStrings[i].charAt(1) <='z' ||
											bytesStrings[i].charAt(1) >='A' && 
											bytesStrings[i].charAt(1) <='Z'		
											)
									){
										bytes[i] = hexTobyte(bytesStrings[i]);
									}else{
										error = true;
										errorString += "命令"+i+"输入有误！";
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							errorString += "命令输入有误！";
							error = true;
						}	
						if(!error){
							DeviceControlView.Instruction instruction = deviceControlView.createInstruction(viewInstruction, toViewInstruction, bytes, toastInstruction, showPostions, textInstruction, textPostions);
							instructions.add(instruction);
							Toast.makeText(DesignActivity.this, "添加指令成功", Toast.LENGTH_LONG).show();
							view.setText("");
							toView.setText("");
							instructionBytes.setText("");
							x1.setText("");
							x2.setText("");
							y1.setText("");
							y2.setText("");
							tx.setText("");
							ty.setText("");
							toast.setText("");
							textString.setText("");
							instructionCount.setText("发送命令数量："+instructions.size());
						}else{
							Toast.makeText(DesignActivity.this, errorString, Toast.LENGTH_LONG).show();
						}
					}
				});
				isInstruction = true;
				builder.show();
				
			}
		});
		addreinstructions.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LayoutInflater layoutInflater = LayoutInflater.from(DesignActivity.this); 
		        View designView = layoutInflater.inflate(R.layout.instruction, null); 
				AlertDialog.Builder builder = new AlertDialog.Builder(DesignActivity.this);
				builder.setTitle("接受指令编辑");
				builder.setIcon(android.R.drawable.ic_menu_edit);
				builder.setView(designView);
				view = (EditText)designView.findViewById(R.id.instruction_view);
				toView = (EditText)designView.findViewById(R.id.instruction_toview);
				instructionBytes = (EditText)designView.findViewById(R.id.instruction_instructionBytes);
				x1 = (EditText)designView.findViewById(R.id.instruction_x1);
				y1 = (EditText)designView.findViewById(R.id.instruction_y1);
				x2 = (EditText)designView.findViewById(R.id.instruction_x2);
				y2 = (EditText)designView.findViewById(R.id.instruction_y2);
				tx = (EditText)designView.findViewById(R.id.instruction_tx);
				ty = (EditText)designView.findViewById(R.id.instruction_ty);
				toast = (EditText)designView.findViewById(R.id.instruction_toast);
				textString = (EditText)designView.findViewById(R.id.instruction_text);
				instructionenter = (Button)designView.findViewById(R.id.instruction_enter);
				instructionenter.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						boolean error = false;
						String errorString = "";
						int viewInstruction = 0;
						int toViewInstruction = 0;
						String toastInstruction = null;
						String textInstruction = null;
						int [] showPostions = null;
						int [] textPostions = null;
						byte[] bytes = null;
						try {
							viewInstruction = Integer.parseInt(view.getText().toString());
						} catch (Exception e) {
							error = true;
							errorString += "所在界面输入有误！";
						}
						try {
							toViewInstruction = Integer.parseInt(toView.getText().toString());
						} catch (Exception e) {
							error = true;
							errorString += "去向界面输入有误！";
						}	
						try {
							toastInstruction = toast.getText().toString();
						} catch (Exception e) {
							error = true;
							errorString += "弹出提示输入有误！";
						}
						try {
							textInstruction = textString.getText().toString();
						} catch (Exception e) {
							error = true;
							errorString += "状态提示输入有误！";
						}
						try {
							
							showPostions = new int[]{Integer.parseInt(x1.getText().toString()),
									Integer.parseInt(y1.getText().toString()),		
									Integer.parseInt(x2.getText().toString()),
									Integer.parseInt(y2.getText().toString()),
									};
						} catch (Exception e) {
//							error = true;
//							errorString += "状态位置输入有误！";
						}
						try {
							if(tx.getText().toString().length() != 0 && ty.getText().toString().length() != 0)
							textPostions = new int[]{Integer.parseInt(tx.getText().toString()),
									Integer.parseInt(ty.getText().toString())
									};
						} catch (Exception e) {
							error = true;
							errorString += "状态位置输入有误！";
						}	
						try {
								String[] bytesStrings = instructionBytes.getText().toString().split(" ");
								bytes = new byte[bytesStrings.length];
								for(int i =0;i<bytesStrings.length;i++){
									if(bytesStrings[i].length() == 2 && (
											bytesStrings[i].charAt(0) >='0' && 
											bytesStrings[i].charAt(0) <='9' ||
											bytesStrings[i].charAt(0) >='a' && 
											bytesStrings[i].charAt(0) <='z' ||
											bytesStrings[i].charAt(0) >='A' && 
											bytesStrings[i].charAt(0) <='Z'
											)&&(
											bytesStrings[i].charAt(1) >='0' && 
											bytesStrings[i].charAt(1) <='9' ||
											bytesStrings[i].charAt(1) >='a' && 
											bytesStrings[i].charAt(1) <='z' ||
											bytesStrings[i].charAt(1) >='A' && 
											bytesStrings[i].charAt(1) <='Z'		
											)
									){
										bytes[i] = hexTobyte(bytesStrings[i]);
									}else{
										error = true;
										errorString += "命令"+i+"输入有误！";
									}
								}
						} catch (Exception e) {
							error = true;
							errorString += "命令输入有误！";
						}	
						if(!error){
							DeviceControlView.ReInstruction reInstruction = deviceControlView.createReInstruction(viewInstruction, toViewInstruction, bytes, toastInstruction, showPostions, textInstruction, textPostions);
							reInstructions.add(reInstruction);
							Toast.makeText(DesignActivity.this, "添加指令成功", Toast.LENGTH_LONG).show();
							view.setText("");
							toView.setText("");
							instructionBytes.setText("");
							x1.setText("");
							x2.setText("");
							y1.setText("");
							y2.setText("");
							tx.setText("");
							ty.setText("");
							toast.setText("");
							textString.setText("");
							reInstructionCount.setText("接受命令数量："+reInstructions.size());
						}else{
							Toast.makeText(DesignActivity.this, errorString, Toast.LENGTH_LONG).show();
						}
					}
				});
				builder.show();
				isInstruction = false;
			}
		});
		deleteinstructions.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(instructions.size() != 0){
					final String [] strings = new String[instructions.size()];
					int i = 0;
					for(Instruction instruction:instructions){
						strings[i] = "使用界面：";
						strings[i] += instruction.view;
						strings[i] += " 切换界面：";
						strings[i] += instruction.toView;
						strings[i] += " 命令：";
						if(instruction.instructionBytes != null && instruction.instructionBytes.length !=0)
						for(Byte b:instruction.instructionBytes){
							int h = b/16;
							if(b < 0) h = (b+256)/16;
							int l = b%16;
							if(b < 0) l = (b+256)%16;
							if(h < 10) strings[i]+= h;
							else if(h == 10) strings[i] += 'A';
							else if(h == 11) strings[i] += 'B';
							else if(h == 12) strings[i] += 'C';
							else if(h == 13) strings[i] += 'D';
							else if(h == 14) strings[i] += 'E';
							else if(h == 15) strings[i] += 'F';
							if(l < 10) strings[i]+= l;
							else if(l == 10) strings[i] += 'A';
							else if(l == 11) strings[i] += 'B';
							else if(l == 12) strings[i] += 'C';
							else if(l == 13) strings[i] += 'D';
							else if(l == 14) strings[i] += 'E';
							else if(l == 15) strings[i] += 'F';
							strings[i] += ' ';
						}
						strings[i] += " 按键位置：";
						for(int j : instruction.showPostions){
							strings[i] += j;
							strings[i] += " ";
						}
						strings[i] += " 弹出框：";
						strings[i] += instruction.toast;
						i++;
					}
					AlertDialog.Builder builder = new AlertDialog.Builder(DesignActivity.this);
					builder.setTitle("选择要删除的指令");
					builder.setItems(strings, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, final int which) {
							AlertDialog.Builder builder = new AlertDialog.Builder(DesignActivity.this);
							builder.setTitle("确认要删除该指令吗？");
							builder.setMessage(strings[which]);
							builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which1) {
									instructions.remove(which);
									instructionCount.setText("发送命令数量："+instructions.size());
								}
							});
							builder.setNegativeButton("取消", null);
							builder.show();
						}
					});
					builder.setPositiveButton("返回", null);
					builder.show();
				}
			}
		});
		deletereinstructions.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(reInstructions.size() != 0){
					final String [] strings = new String[reInstructions.size()];
					int i = 0;
					for(ReInstruction reInstruction:reInstructions){
						strings[i] = "使用界面：";
						strings[i] += reInstruction.view;
						strings[i] += " 切换界面：";
						strings[i] += reInstruction.toView;
						strings[i] += " 命令：";
						if(reInstruction.reInstructionBytes != null && reInstruction.reInstructionBytes.length != 0 )
						for(Byte b:reInstruction.reInstructionBytes){
							int h = b/16;
							if(b < 0) h = (b+256)/16;
							int l = b%16;
							if(b < 0) l = (b+256)%16;
							if(h < 10) strings[i]+= h;
							else if(h == 10) strings[i] += 'A';
							else if(h == 11) strings[i] += 'B';
							else if(h == 12) strings[i] += 'C';
							else if(h == 13) strings[i] += 'D';
							else if(h == 14) strings[i] += 'E';
							else if(h == 15) strings[i] += 'F';
							if(l < 10) strings[i]+= l;
							else if(l == 10) strings[i] += 'A';
							else if(l == 11) strings[i] += 'B';
							else if(l == 12) strings[i] += 'C';
							else if(l == 13) strings[i] += 'D';
							else if(l == 14) strings[i] += 'E';
							else if(l == 15) strings[i] += 'F';
							strings[i] += ' ';
						}
						strings[i] += " 弹出框：";
						strings[i] += reInstruction.toast;
						i++;
					}
					AlertDialog.Builder builder = new AlertDialog.Builder(DesignActivity.this);
					builder.setTitle("选择要删除的指令");
					builder.setItems(strings, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, final int which) {
							AlertDialog.Builder builder = new AlertDialog.Builder(DesignActivity.this);
							builder.setTitle("确认要删除该指令吗？");
							builder.setMessage(strings[which]);
							builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which1) {
									reInstructions.remove(which);
									reInstructionCount.setText("接受命令数量："+reInstructions.size());
								}
							});
							builder.setNegativeButton("取消", null);
							builder.show();
						}
					});
					builder.setPositiveButton("返回", null);
					builder.show();
				}
			}
		});
		openButton = (Button)findViewById(R.id.design_open);
		openButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String [] names = DeviceControlView.getVanstcvNames();
				if(names != null && names.length != 0){
					AlertDialog.Builder builder = new AlertDialog.Builder(DesignActivity.this);
					builder.setTitle("选择要编辑的设备文件");
					builder.setItems(names, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							deviceControlView = DeviceControlView.readControlView(names[which]);
							instructions = new ArrayList<DeviceControlView.Instruction>();
							reInstructions = new ArrayList<DeviceControlView.ReInstruction>();
							if(deviceControlView != null){
								if(deviceControlView.instructions!=null && deviceControlView.instructions.length != 0){
									for(DeviceControlView.Instruction instruction : deviceControlView.instructions){
										instructions.add(instruction);
									}
								}
								if(deviceControlView.reInstructions != null && deviceControlView.reInstructions.length != 0){
									for(DeviceControlView.ReInstruction reInstruction : deviceControlView.reInstructions){
										reInstructions.add(reInstruction);
									}
								}
							}
							instructionCount.setText("发送命令数量："+instructions.size());
							reInstructionCount.setText("接受命令数量："+reInstructions.size());
						}
					});
				}else{
					Toast.makeText(DesignActivity.this,"文件夹内自定义设备为空", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		
		deisgnenter = (Button)findViewById(R.id.design_enter);
		deisgnenter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int bitmapCount = 0;
				path = Config.FILE_ROOT + filepath.getText().toString();
				File file = new File(path);
				if(file.exists() && file.isDirectory()){
					File[] ff = file.listFiles();
					if(ff != null && ff.length != 0){
						while(true){
							int count = bitmapCount;
							for(File fileBitmap:ff){
								if(Config.checkFileName(fileBitmap.getName()) == bitmapCount+1){
									bitmapCount++;
									bitmaps = new ArrayList<byte[]>();
									bitmaps.add(DeviceControlView.bitmapfileToBytes(fileBitmap));
								}
							}
							if(count == bitmapCount){
								break;
							}
						}
					}
				}
				if(bitmapCount != 0){
					if(instructions.size() != 0){
						deviceControlView.instructions = new Instruction[instructions.size()];
						for(int i = 0 ;i<instructions.size();i++){
							deviceControlView.instructions[i] = instructions.get(i);
						}
						deviceControlView.reInstructions = new ReInstruction[reInstructions.size()];
						for(int i = 0 ;i<reInstructions.size();i++){
							deviceControlView.reInstructions[i] = reInstructions.get(i);
						}
						deviceControlView.backgroundBitmaps = new byte[bitmaps.size()][];
						for(int i = 0 ;i>bitmaps.size();i++){
							deviceControlView.backgroundBitmaps[i] = bitmaps.get(i);
						}
						File[] ff = file.listFiles();
						if(ff != null && ff.length != 0){
							for(File file2:ff){
								if(file2.getName().equalsIgnoreCase("a.png")){
									deviceControlView.DEVICE_IMAGE = DeviceControlView.bitmapfileToBytes(file2);
								}
								if(file2.getName().equalsIgnoreCase("b.png")){
									deviceControlView.DEVICE_ON_IMAGE = DeviceControlView.bitmapfileToBytes(file2);
								}
								if(file2.getName().equalsIgnoreCase("c.png")){
									deviceControlView.DEVICE_OFF_IMAGE = DeviceControlView.bitmapfileToBytes(file2);
								}
								if(file2.getName().equalsIgnoreCase("d.png")){
									deviceControlView.DEVICE_UNKNOW_IMAGE = DeviceControlView.bitmapfileToBytes(file2);
								}
							}
						}
//						deviceControlView.instructions = (Instruction[]) instructions.toArray();
//						deviceControlView.reInstructions = (ReInstruction[]) reInstructions.toArray();
//						deviceControlView.backgroundBitmaps = (byte[][]) bitmaps.toArray();
					}
					DeviceControlView.saveControlView(deviceControlView, filepath.getText().toString());
					
				}else{
					Toast.makeText(DesignActivity.this, "指定文件夹图片文件格式不对或空", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	
	public byte hexTobyte(String string){
		int h = 0;
		int l = 0;
		if(string.charAt(0) == '1'){
			h = 1;
		}else if(string.charAt(0) == '2'){
			h = 2;
		}else if(string.charAt(0) == '3'){
			h = 3;
		}else if(string.charAt(0) == '4'){
			h = 4;
		}else if(string.charAt(0) == '5'){
			h = 5;
		}else if(string.charAt(0) == '6'){
			h = 6;
		}else if(string.charAt(0) == '7'){
			h = 7;
		}else if(string.charAt(0) == '8'){
			h = 8;
		}else if(string.charAt(0) == '9'){
			h = 9;
		}else if(string.charAt(0) == 'a' || string.charAt(0) == 'A'){
			h = 10;
		}else if(string.charAt(0) == 'b' || string.charAt(0) == 'B'){
			h = 11;
		}else if(string.charAt(0) == 'c' || string.charAt(0) == 'C'){
			h = 12;
		}else if(string.charAt(0) == 'd' || string.charAt(0) == 'D'){
			h = 13;
		}else if(string.charAt(0) == 'e' || string.charAt(0) == 'E'){
			h = 14;
		}else if(string.charAt(0) == 'f' || string.charAt(0) == 'F'){
			h = 15;
		}
		if(string.charAt(1) == '1'){
			l = 1;
		}else if(string.charAt(1) == '2'){
			l = 2;
		}else if(string.charAt(1) == '3'){
			l = 3;
		}else if(string.charAt(1) == '4'){
			l = 4;
		}else if(string.charAt(1) == '5'){
			l = 5;
		}else if(string.charAt(1) == '6'){
			l = 6;
		}else if(string.charAt(1) == '7'){
			l = 7;
		}else if(string.charAt(1) == '8'){
			l = 8;
		}else if(string.charAt(1) == '9'){
			l = 9;
		}else if(string.charAt(1) == 'a' || string.charAt(1) == 'A'){
			l = 10;
		}else if(string.charAt(1) == 'b' || string.charAt(1) == 'B'){
			l = 11;
		}else if(string.charAt(1) == 'c' || string.charAt(1) == 'C'){
			l = 12;
		}else if(string.charAt(1) == 'd' || string.charAt(1) == 'D'){
			l = 13;
		}else if(string.charAt(1) == 'e' || string.charAt(1) == 'E'){
			l = 14;
		}else if(string.charAt(1) == 'f' || string.charAt(1) == 'F'){
			l = 15;
		}
		return (byte)(h * 16 +l);
	}
}

