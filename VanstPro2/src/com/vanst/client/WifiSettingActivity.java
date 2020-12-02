package com.vanst.client;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class WifiSettingActivity extends Activity{
	Spinner editText1;
	EditText editText2;
	Button button;
//	Spinner spinner1;
//	Spinner spinner2;
	String renzheng = "OPEN";
	String suanfa = "NONE";
	ArrayAdapter<String> wifiNameAdapter;
	ArrayAdapter<String> adapter;
	ArrayAdapter<String> adapter2;
	String [] strings = new String[]{"OPEN","SHARED","WPAPSK","WPA2PSK"};
	String [] strings2 = new String[]{"NONE"};
	String [] wifiNames = new String[]{"没有找到WIFI信号"};
	String wifiName;
	List<ScanResult> mWifiList;
	//定义一个WifiManager对象  
    WifiManager mWifiManager;  
    //定义一个WifiInfo对象  
    WifiInfo mWifiInfo; 
    ScanResult mScanResult;
    StringBuffer sb;
    List<WifiConfiguration> mWifiConfigurations;  
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//取得WifiManager对象  
        mWifiManager=(WifiManager) getSystemService(Context.WIFI_SERVICE);  
        //取得WifiInfo对象  
        mWifiInfo=mWifiManager.getConnectionInfo(); 
        sb = new StringBuffer();
        //开始扫描网络  
        mWifiManager.startScan();  
        //得到扫描结果  
        mWifiList=mWifiManager.getScanResults();  
        //得到配置好的网络连接  
        mWifiConfigurations=mWifiManager.getConfiguredNetworks();  
        if(mWifiList!=null && mWifiList.size() != 0){  
        	wifiNames = new String[mWifiList.size()];
        	MyLog.i("wificount", mWifiList.size()+"");
            for(int i=0;i<mWifiList.size();i++){  
                //得到扫描结果  
                mScanResult=mWifiList.get(i); 
                wifiNames[i] = mScanResult.SSID;
                MyLog.i("StringBuffer",mScanResult.SSID);
//                if(mScanResult !=null)
//                sb=sb.append(mScanResult.BSSID+"  ").append(mScanResult.SSID+"   ")  
//                .append(mScanResult.capabilities+"   ").append(mScanResult.frequency+"   ")  
//                .append(mScanResult.level);  
//                MyLog.i("StringBuffer", sb.toString());
            }  
           wifiName = wifiNames[0];
        }  
		setContentView(R.layout.wifi);
		editText1 = (Spinner)findViewById(R.id.wifi_id);
		editText2 = (EditText)findViewById(R.id.wifi_password);
		button = (Button)findViewById(R.id.wifi_button);
//		spinner1 = (Spinner)findViewById(R.id.wifi_renzheng);
//		spinner2 = (Spinner)findViewById(R.id.wifi_suanfa);
		wifiNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,wifiNames);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,strings);
		adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,strings2);
		wifiNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		editText1.setAdapter(wifiNameAdapter);
//		spinner1.setAdapter(adapter);
//		spinner2.setAdapter(adapter2);
        editText1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        	@Override
        	public void onItemSelected(AdapterView<?> parent, View view,
        			int position, long id) {
        		wifiName = wifiNames[position];
        		MyLog.i("wifiName!!",wifiName);
        	}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
        
        
        
//		spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,  
//	                long arg3) {  
//				if(arg2 == 0){
//					renzheng = strings[0];
//					strings2 = new String[]{"NONE"};
//					adapter2 = new ArrayAdapter<String>(WifiSettingActivity.this, android.R.layout.simple_spinner_item,strings2);
//					adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//					spinner2.setAdapter(adapter2);
//					suanfa = "NONE";
//				}else if(arg2 == 1){
//					renzheng = strings[1];
//					strings2 = new String[]{"WEP-HEX","WEP-ASCII"};
//					adapter2 = new ArrayAdapter<String>(WifiSettingActivity.this, android.R.layout.simple_spinner_item,strings2);
//					adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//					spinner2.setAdapter(adapter2);
//					suanfa = "WEP-HEX";
//				}else if(arg2 == 2){
//					renzheng = strings[2];
//					strings2 = new String[]{"AES","TKIP"};
//					adapter2 = new ArrayAdapter<String>(WifiSettingActivity.this, android.R.layout.simple_spinner_item,strings2);
//					adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//					spinner2.setAdapter(adapter2);
//				}else if(arg2 == 3){
//					renzheng = strings[3];
//					strings2 = new String[]{"AES","TKIP"};
//					adapter2 = new ArrayAdapter<String>(WifiSettingActivity.this, android.R.layout.simple_spinner_item,strings2);
//					adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//					spinner2.setAdapter(adapter2);
//				} 
//	        }
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				
//			} 
//		
//		
//		});
//		spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				suanfa = strings2[arg2];
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				
//			}
//		});
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!Connect.flag){
					Toast.makeText(WifiSettingActivity.this, "服务器未连接！", Toast.LENGTH_LONG).show();
				}else if(mWifiList==null || mWifiList.size() == 0){
					Toast.makeText(WifiSettingActivity.this, "没有找到WIFI信号", Toast.LENGTH_LONG).show();
				}else if(editText2.getText().toString() == null || editText2.getText().toString().length() == 0){
					Toast.makeText(WifiSettingActivity.this, "名称或密码不能为空", Toast.LENGTH_LONG).show();
				}else{
					String name = wifiName;
					String password = editText2.getText().toString();
					Device.typeBytes = new byte[90];
					for(int i = 0;i < name.length();i++){
						Device.typeBytes[i] = (byte) name.charAt(i);
					}
					for(int i = 0;i < renzheng.length();i++){
						Device.typeBytes[20+i] = (byte) renzheng.charAt(i);
					}
					for(int i = 0;i < suanfa.length();i++){
						Device.typeBytes[30+i] = (byte) suanfa.charAt(i);
					}
					for(int i = 0;i < password.length();i++){
						Device.typeBytes[40+i] = (byte) password.charAt(i);
					}
//					for(int i = 0;i < Device.typeBytes.length;i++){
//						MyLog.i("wifi",(char)Device.typeBytes[i]+"");
//					}
					
					if(Connect.sockets != null && Connect.sockets.size()>1){
			    		AlertDialog.Builder builder1 = new AlertDialog.Builder(WifiSettingActivity.this);
			    		builder1.setTitle("有多个服务器链接，请选择一个进行配置");
			    		String []ips = new String[Connect.sockets.size()];
			    		int j = 0;
			    		for(SocketIP socketIP:Connect.sockets){
			    			ips[j] = socketIP.ip;
			    			j++;
			    		}
			    		builder1.setItems(ips,new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								Connect.in = Connect.ins[which];
								Connect.out = Connect.outs[which];
								Connect.socket = Connect.sockets.get(which).socket;
								final AlertDialog.Builder builder = new AlertDialog.Builder(WifiSettingActivity.this);
								builder.setTitle("请输入密码");
								builder.setIcon(android.R.drawable.ic_menu_edit);
								final EditText editText = new EditText(WifiSettingActivity.this);
								editText.setHint("同步密码");
								builder.setView(editText);
								editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
								builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface arg0, int arg1) {
										Connect.pwd = editText.getText().toString();
										Connect.isShared = 1;
										Connect.askShare(editText.getText().toString());
										MainActivity.progressDialog = new ProgressDialog(WifiSettingActivity.this);
										MainActivity.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
										MainActivity.progressDialog.setMessage("请稍等..等待密码验证");
										MainActivity.progressDialog.setButton("取消",new DialogInterface.OnClickListener() {
											
											public void onClick(DialogInterface arg0,
													int arg1) {
												Connect.rwPose = Connect.control;
											}
										});
										MainActivity.progressDialog.setCancelable(false);
										MainActivity.progressDialog.show();
									}
								});
								builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
									public void onClick(DialogInterface arg0, int arg1) {
										Connect.rwPose = Connect.control;
									}
								});
								builder.show();
							}
						});
			    		builder1.show();
			    	}else{
			    		final AlertDialog.Builder builder = new AlertDialog.Builder(WifiSettingActivity.this);
						builder.setTitle("请输入密码");
						builder.setIcon(android.R.drawable.ic_menu_edit);
						final EditText editText = new EditText(WifiSettingActivity.this);
						editText.setHint("同步密码");
						builder.setView(editText);
						editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
						builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								Connect.pwd = editText.getText().toString();
								Connect.isShared = 1;
								Connect.askShare(editText.getText().toString());
								MainActivity.progressDialog = new ProgressDialog(WifiSettingActivity.this);
								MainActivity.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								MainActivity.progressDialog.setMessage("请稍等..等待密码验证");
								MainActivity.progressDialog.setButton("取消",new DialogInterface.OnClickListener() {
									
									public void onClick(DialogInterface arg0,
											int arg1) {
										Connect.rwPose = Connect.control;
									}
								});
								MainActivity.progressDialog.setCancelable(false);
								MainActivity.progressDialog.show();
							}
						});
						builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface arg0, int arg1) {
								Connect.rwPose = Connect.control;
							}
						});
						builder.show();
			    	}
				}
					
			}
		});
	}
	@Override
	protected void onResume() {
		Connect.activity = this;
		super.onResume();
	}
	
	public void connectConfiguration(int index) { 
        if (index > mWifiConfigurations.size()) { 
            return; 
        } 
        mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId, 
                true); 
    } 
}
