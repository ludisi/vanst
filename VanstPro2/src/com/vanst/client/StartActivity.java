package com.vanst.client;


import java.lang.reflect.Method;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.StrictMode;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

public class StartActivity extends Activity {
	private ITelephony  iTelephony;
    private TelephonyManager manager;
    Button startButton;
//    ProgressBar progressBar;
    TextView textView;
    TextView waitTextView;
    static boolean loadFlag;
    final static int CONNECT_SINGLE = 0xac;
    final static int CONNECT_WIFI = 0xaa;
    final static int CONNECT_3G = 0xab;
    static boolean isStart = false;
    static boolean isExit = false;
    Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		if(msg.what == 0x01){
    			Toast.makeText(StartActivity.this, "���ӳɹ�", Toast.LENGTH_LONG).show();
    		}else if(msg.what == CONNECT_SINGLE){
    			Toast.makeText(StartActivity.this, "����Ϊ��������", Toast.LENGTH_LONG).show();
    		}else if(msg.what == CONNECT_WIFI){
    			new Connect(StartActivity.this,0);
    		}else if(msg.what == CONNECT_3G){
    			new Connect(StartActivity.this,0x3cf);
    		}
    	};
    };
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isStart = true;
//        Intent service = new Intent("android.intent.action.vanstservice");
//        startService(service);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .detectDiskReads().detectDiskWrites()
        .detectNetwork()
        .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        .detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
        .build());
//        Intent vanstService = new Intent("android.intent.action.vanstservice");
//        startService(vanstService);
        VanstWidgetProvider.initAppWidget(this);
        Intent vanstservice = new Intent("android.intent.action.vanstservice");
        startService(vanstservice);
        Connect.startActivity = this;
        Connect.activity = this;
        MainActivity.isOK = true;
        MainActivity.musicDo = new MusicDo(this);
        SharedPreferences pref=getSharedPreferences("config", MODE_PRIVATE);
        long time = pref.getLong("lasttime", 0);
        if(System.currentTimeMillis()- time < 2000 && time - System.currentTimeMillis() < 2000){
			SharedPreferences.Editor editor=pref.edit();
			editor.putLong("lasttime", 0);
			editor.commit();
        	finish();
        	return;
        }
//        boolean first = pref.getBoolean("first", true);
//        if(first){
//        	addShortCut();
////        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
////        	builder.setTitle("�Ƿ���������ݷ�ʽ��");
////        	builder.setPositiveButton("��", new DialogInterface.OnClickListener() {
////				public void onClick(DialogInterface dialog, int which) {
////					// TODO Auto-generated method stub
////					
////				}
////			});
////        	builder.setNegativeButton("��", null);
////        	builder.show();
//        	SharedPreferences.Editor editor=pref.edit();
//			editor.putBoolean("first", false);
//			editor.commit();
//        }
        Connect.useIp = pref.getString("useip", "10.10.100.254");
        Connect._3GIp = pref.getString("lanip", null);
        Connect.phoneNumber = pref.getString("phone", null);
        Connect.initSearch = true;
        Connect.initSynchronization = true;
        Connect.initRealtime = true;
        Connect.init3G = false;
//        int lastConnect = pref.getInt("connect", 0);
        boolean autoWifi = pref.getBoolean("autowifi", true);
        Room.count = pref.getInt("rooms", 2);
        Room.defaultAngle = pref.getInt("angel", 0);
        MyLog.i("useip",Connect.useIp);
        setContentView(R.layout.start);
        startButton = (Button)findViewById(R.id.start);
//        progressBar = (ProgressBar)findViewById(R.id.progressBar5);
        textView = (TextView)findViewById(R.id.load_text);
        waitTextView = (TextView)findViewById(R.id.wait_text);
        startButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(Connect.flag) enter();
				else dialogShow(false);
			}
		});
        phoner();
        manager.listen(new PhoneStateListener(){
        	   public void onCallStateChanged(int state, String incomingNumber) {
        	    super.onCallStateChanged(state, incomingNumber);
        	    MyLog.i("incomingNumber",incomingNumber+"ffaefawefwaef");
        	    switch(state){
        	    case TelephonyManager.CALL_STATE_OFFHOOK:
        	     try {
        	    	 if(Connect.needStopPhone && iTelephony!=null){
        	    		 Thread.sleep(15000);
        	    		 iTelephony.endCall();
        	    		 Connect.needStopPhone = false;
        	    	 }
        	     } catch (RemoteException e) {
        	      e.printStackTrace();
        	     } catch (InterruptedException e) {
					e.printStackTrace();
				}catch (Exception e) {
				}
        	    }
        	   }
        	         
        	  }, PhoneStateListener.LISTEN_CALL_STATE);
        new Get3GIP().start();
        boolean widget = false;
        Intent intent =getIntent();
        if(intent != null){
        	widget = intent.getBooleanExtra("widget", false);
        }
        if(autoWifi || widget){
//        	if(widget){
//        		moveTaskToBack(true);
//        	}
        	handler.sendEmptyMessage(CONNECT_WIFI);
        }
//        if(lastConnect == 1){
//        	handler.sendEmptyMessage(CONNECT_WIFI);
//        }else if(lastConnect == 2){
//        	handler.sendEmptyMessage(CONNECT_3G);
//        }
    }
    @Override
    protected void onResume() {
    	MyLog.i("StartActivity","orResume");
    	MyLog.i("isExit",Connect.isEnter+"");
    	Connect.activity = this;
    	VanstWidgetProvider.initAppWidget(this);
    	if(Connect.isEnter || MainActivity.exit){
    		onDestroy();
    	}else{
    		super.onResume();
    	}
    }
    @Override
    protected void onDestroy() {
    	isStart = false;
    	String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
        //����֪ͨ��չ�ֵ�������Ϣ
        int icon = R.drawable.ic_launcher;
        CharSequence tickerText = "Vanst";
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
        //��������֪ͨ��ʱҪչ�ֵ�������Ϣ
        Context context = getApplicationContext();
        CharSequence contentTitle = "Vanst";
        CharSequence contentText = "Vanst�������߽�ȫ�µ���������";
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vanst.com"));
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);
        notification.setLatestEventInfo(context, contentTitle, contentText,contentIntent); 
        notification.flags|=Notification.FLAG_AUTO_CANCEL;
//        notification.defaults |= Notification.DEFAULT_SOUND;
        //��mNotificationManager��notify����֪ͨ�û����ɱ�������Ϣ֪ͨ  
        mNotificationManager.notify(1, notification);
        Connect.discoonectWifi();
    	System.exit(1);
    	super.onDestroy();
    }
    public void enter(){
    	Connect.isEnter = true;
    	
//    	startActivity(new Intent(StartActivity.this, MainActivity.class));
//		Intent intent = new Intent("android.intent.action.proMain");
//		startActivity(intent);
//		setVisible(false);
		Intent intent = new Intent("android.intent.action.proMain");
		startActivity(intent);
//		overridePendingTransition( android.R.anim.slide_out_right,android.R.anim.slide_in_left);
//		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
//		overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
//		StartActivity.this.finish();
//    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
//    	builder.setIcon(android.R.drawable.ic_menu_info_details);
//    	builder.setTitle("���������ӳɹ���");
//    	builder.setMessage("���ȷ�Ͻ����������");
//    	builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
//			
//			public void onClick(DialogInterface arg0, int arg1) {
//				
//			}
//		});
//    	builder.show();
		
    }
    static public void exitStart(){
    	System.exit(0);
    }
    public void wifiDialog(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
    	builder.setIcon(android.R.drawable.ic_menu_search);
    	builder.setTitle("����ʧ���Ƿ�Ҫ���о�����������");
    	builder.setPositiveButton("��������", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				startButton.setEnabled(false);
//				progressBar.setProgress(0);
//				progressBar.setVisibility(View.VISIBLE);
				textView.setVisibility(View.VISIBLE);
				
			}
		});
    }
    public void phoneDialog(){
    	LayoutInflater layoutInflater = LayoutInflater.from(this); 
        View myLoginView = layoutInflater.inflate(R.layout.dialog_3g, null);
    	AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
    	builder.setIcon(android.R.drawable.stat_sys_phone_call);
    	builder.setTitle("����ʧ���Ƿ�Ҫ�����ٴ���֤��");
    	final EditText editText = (EditText)myLoginView.findViewById(R.id.dialog_edit);
		Button button = (Button)myLoginView.findViewById(R.id.dialog_bt);
		editText.setText(Connect.phoneNumber);
		editText.setHint("11λ�ֻ���");
		editText.setInputType(InputType.TYPE_CLASS_PHONE);
		editText.setSingleLine();
		builder.setView(myLoginView);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				final SharedPreferences pref=getSharedPreferences("config", Context.MODE_PRIVATE);
				final String phones = pref.getString("phonehistroy", "");
				final String [] phoneNumbers = phones.split(" ");
				if(phones.length() !=0){
					AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
					builder.setTitle("��ʷ��¼");
					builder.setIcon(android.R.drawable.ic_menu_more);
					builder.setItems(phoneNumbers, new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog, final int which) {
					    	Connect.phoneNumber = phoneNumbers[which];
					    	editText.setText(phoneNumbers[which]);
					    }
					});
					builder.setPositiveButton("��ռ�¼", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1){
							SharedPreferences.Editor editor=pref.edit();
							editor.putString("phonehistroy", "");
							editor.commit();
						}
					});
					builder.setNegativeButton("����", null);
					builder.show();
				}else{
					Toast.makeText(StartActivity.this, "��¼Ϊ��" ,Toast.LENGTH_SHORT).show();
				}
			}
		});
    	builder.setPositiveButton("�û���֤", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				if(checkPhoneNumber(editText.getText().toString())){
					final SharedPreferences pref=getSharedPreferences("config", Context.MODE_PRIVATE);
					final String phones = pref.getString("phonehistroy", "");
					final String [] phoneNumbers = phones.split(" ");
					Toast.makeText(StartActivity.this, "�û���֤�С�����", Toast.LENGTH_LONG).show();
					Toast.makeText(StartActivity.this, "�����ĵȴ��������Ļظ�...", Toast.LENGTH_LONG).show();
					waitTextView.setVisibility(View.VISIBLE);
					Connect.phoneNumber = editText.getText().toString();
					Connect.phone3g = true;
					SharedPreferences.Editor editor=pref.edit();
					editor.putString("phone", Connect.phoneNumber);
					editor.commit();
					boolean have = false;
					for(String phoneNumber:phoneNumbers){
						if(phoneNumber.equals(Connect.phoneNumber)){
							have = true;
						}
					}
					if(!have){
						if(phones.length() == 0) editor.putString("phonehistroy", Connect.phoneNumber); 
						else editor.putString("phonehistroy", Connect.phoneNumber+" "+phones);
						editor.commit();
					}
					try{
						Connect.PhoneCall(StartActivity.this);
					}catch (Exception e) {
						Toast.makeText(StartActivity.this, "�޷���֤�������Ƿ����SIM��", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(StartActivity.this, "��������ȷ��11λ��Ч����", Toast.LENGTH_LONG).show();
					phoneDialog();
				}
			}
		});
    	builder.setNeutralButton("�ٴ�����", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				Toast.makeText(StartActivity.this, "����Ϊ��������", Toast.LENGTH_SHORT).show();
				new Thread(){
					public void run() {
						try {
							sleep(500);
						} catch (Exception e) {
						}
						handler.sendEmptyMessage(CONNECT_3G);
					};
				}.start();
			}
		});
    	builder.setNegativeButton("����", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				dialogShow(true);
			}
		});
    	builder.show();
    }
    public void dialogShow(boolean isReconnect){
    	AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
    	if(isReconnect){
    		builder.setTitle("����ʧ���Ƿ�Ҫ�������ӣ�");
    	}else{
    		builder.setTitle("��ѡ�����ӷ�ʽ��");
    	}
    	builder.setIcon(android.R.drawable.ic_menu_manage);
    	builder.setPositiveButton("WIFI����", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				Toast.makeText(StartActivity.this, "����Ϊ��������...", Toast.LENGTH_SHORT).show();
				new Thread(){
					public void run() {
						try {
							sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						handler.sendEmptyMessage(CONNECT_WIFI);
					};
				}.start();
			}
		});
    	builder.setNeutralButton("3G����", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				Toast.makeText(StartActivity.this, "����Ϊ��������...", Toast.LENGTH_SHORT).show();
				new Thread(){
					public void run() {
						try {
							sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						handler.sendEmptyMessage(CONNECT_3G);
					};
				}.start();
				
			}
		});
    	builder.setNegativeButton("ֱ�ӽ���", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				Connect.isEnter = true;
				Intent intent = new Intent("android.intent.action.proMain");
				startActivity(intent);
			}
		});
    	builder.show();
    }
    public void phoner(){
        manager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
           Class <TelephonyManager> c = TelephonyManager.class;  
            Method getITelephonyMethod = null;  
            try {  
                   getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[])null);  
                   getITelephonyMethod.setAccessible(true);  
             iTelephony = (ITelephony) getITelephonyMethod.invoke(manager, (Object[])null);  
            } catch (IllegalArgumentException e) {  
                  e.printStackTrace();  
            } catch (Exception e) {  
                 e.printStackTrace(); 

            } 
       }
    public void connect3g(){
    	Toast.makeText(StartActivity.this, "����Ϊ��������", Toast.LENGTH_SHORT).show();
		new Thread(){
			public void run() {
				try {
					sleep(500);
				} catch (Exception e) {
				}
				handler.sendEmptyMessage(CONNECT_3G);
			};
		}.start();
    }
    private boolean checkPhoneNumber(String phoneNumber){
		if(phoneNumber == null || phoneNumber.length() == 0) return false;
		if(phoneNumber.charAt(0) != '1') return false;
		char a[] = phoneNumber.toCharArray();
		if(phoneNumber.length() !=11){
			return false;
		}
		for(char c:a){
			if(c<'0'||c>'9'){
				return false;
			}
		}
		return true;
	}
    class Get3GIP extends Thread{
		public void run() {
			while(true){
				if(Connect._3GIp!=null)MyLog.i("3GIP",Connect._3GIp);
				else MyLog.i("3GIP","no3gip");
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Connect.deleteSms();
			}
		}
	}
    
    private  void addShortCut() {  
        //��ݷ�ʽ����  
    	Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");  
        
        //��ݷ�ʽ������  
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Vanst����");  
        shortcut.putExtra("duplicate", false); //�������ظ�����  
              
        //ָ����ǰ��ActivityΪ��ݷ�ʽ�����Ķ���: �� com.everest.video.VideoPlayer  
        //ע��: ComponentName�ĵڶ�������������ϵ��(.)�������ݷ�ʽ�޷�������Ӧ����  
        ComponentName comp = new ComponentName(this.getPackageName(), "."+this.getLocalClassName()); 
        
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));  
//        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(this,StartActivity.class));  
      
        //��ݷ�ʽ��ͼ��  
        ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher);  
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);  
        sendBroadcast(shortcut);
    }  
    
}