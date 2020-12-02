package com.vanst.client;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class PhotoActivity extends ListActivity {
//	public static String SDCARD_ROOT = "/mnt/sdcard/DCIM/";
	public static String SDCARD_ROOT = "/mnt/sdcard/";
	ListView listView;
	List<Map<String, Object>> list;
	ProgressDialog progressDialog;
	int fileNum = 0;
	int choseNum = 0;
	boolean empty = false;
    String currDir=SDCARD_ROOT;//当前目录
    final static int LIST_FINISHED = 0x01;
    final static int PROGRESS_FRESH = 0x02;
    Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		if(msg.what == LIST_FINISHED){
    			SimpleAdapter adapter = new SimpleAdapter(PhotoActivity.this, list, R.layout.item,
    					new String[] { "icon", "name" }, new int[] { R.id.icon,
    							R.id.name});
    			adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
    				
    				@Override
    				public boolean setViewValue(View view, Object data, String arg2) {
    					// TODO Auto-generated method stub
    					if(view instanceof ImageView && data instanceof Bitmap){    
    				        ImageView i = (ImageView)view;    
    				        i.setImageBitmap((Bitmap) data);    
    				        return true;    
    				    }  
    					return false;
    				}
    			});
    			listView.setAdapter(adapter);
    			progressDialog.dismiss();
    		}else if(msg.what == PROGRESS_FRESH){
    			progressDialog.setMessage("正在加载相册，请稍后...("+choseNum+"/"+fileNum+")");
    		}
    	};
    };
    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		listView = getListView();
		listView.setBackgroundResource(R.drawable.ui_bg);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setDivider(getResources().getDrawable(R.drawable.white));
		listView.setDividerHeight(1);
        //检测SDCARD是否挂载
//		SDCARD_ROOT = Environment.getExternalStorageDirectory().toString();
		MyLog.i("SDCARD_ROOT",Environment.getExternalStorageDirectory().toString());
//		Log.i("ff", SDCARD_ROOT.equals(Environment.getExternalStorageDirectory().toString())+"");
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			inflateListView(SDCARD_ROOT);
		
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					@SuppressWarnings("unchecked")
					Map<String, Object> map=(Map<String, Object>)listView.getItemAtPosition(position);
					boolean isDir=(Boolean)map.get("isDir");
					String path=(String)map.get("path");
					if(isDir){
//						Toast.makeText(PhotoActivity.this,
//								"是目录:"+path,Toast.LENGTH_SHORT).show();
						currDir=path;
						//浏览子目录 path /mnt/sdcard/image
						inflateListView(path);
					}else{
	//					Toast.makeText(MainActivity.this,
	//							"是文件:"+path,Toast.LENGTH_SHORT).show();
						if(empty){
							finish();
						}else{
						    Intent data=new Intent();
						    data.putExtra("path", path);
							setResult(Activity.RESULT_OK, data);
							finish();
						}
					}
					
				}
	
			});
		}else{
			Toast.makeText(this, "未找到SD卡！",Toast.LENGTH_LONG).show();
			finish();
		}

	}

	private void inflateListView(final String path) {
		progressDialog = new ProgressDialog(PhotoActivity.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("正在加载相册，请稍后...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		new Thread(){
			public void run() {
				list = new ArrayList<Map<String, Object>>();
				File file = new File(path);
				if(!file.exists()) file.mkdir();
				File ff[] = file.listFiles();
				fileNum = ff.length;
				choseNum = 0;
				for (File f : ff) {
					if (f.isDirectory()) {
						if(path.equals(SDCARD_ROOT)|| (path+"/").equals(SDCARD_ROOT)){
							if(f.getName().equalsIgnoreCase("DCIM")||f.getName().equalsIgnoreCase("image")||f.getName().equalsIgnoreCase("picture")||f.getName().equalsIgnoreCase("pictures")||f.getName().equalsIgnoreCase("download")){
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("icon", R.drawable.fold);
								map.put("name", f.getName());
								map.put("isDir", true);
								map.put("path", f.getAbsolutePath());
								list.add(map);	
							}
						}else{
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("icon", R.drawable.fold);
							map.put("name", f.getName());
							map.put("isDir", true);
							map.put("path", f.getAbsolutePath());
							list.add(map);
						}
					}
				}

				for (File f : ff) {
					choseNum++;
					handler.sendEmptyMessage(PROGRESS_FRESH);
					if (f.isFile()) {
						if(checkFileName(f.getName())){
							Map<String, Object> map = new HashMap<String, Object>();
//							Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
							Bitmap bitmap = null;
							BitmapFactory.decodeFile(f.getPath(), Room.optReadBound);
							int weigth = Room.optReadBound.outWidth;
							int height = Room.optReadBound.outHeight;
							if(weigth >= 200 || height >= 200){
								int inSampleSize = 0;
								if(weigth > height) inSampleSize = (int) (weigth / 100);
								else inSampleSize = (int)(height / 100);
								Room.optReadBitmap.inSampleSize = inSampleSize;
								bitmap = BitmapFactory.decodeFile(f.getPath(),Room.optReadBitmap);
							}else{
								Room.optReadBitmap.inSampleSize = 1;
								bitmap = BitmapFactory.decodeFile(f.getPath(),Room.optReadBitmap);
							}
							if(bitmap !=null){
								empty = false;
								map.put("icon",bitmap);
								long size = f.length();
								size = size / 1024 + ((size % 1024 > 0) ? 1 : 0);
								map.put("size", size + "KB");
								map.put("name", f.getName());
								map.put("isDir", false);
								map.put("path", f.getAbsolutePath());
								list.add(map);
								MyLog.i("path", f.getPath());
								MyLog.i("past",f.getAbsolutePath());
							}
						}
					}
				}
				if(list.isEmpty()){
					empty = true;
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("isDir", false);
					map.put("path", "null");
					map.put("name", "<相册为空>");
					list.add(map);
				}
				handler.sendEmptyMessage(LIST_FINISHED);
			};
		}.start();
		
		
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(!currDir.equals(SDCARD_ROOT) && !(currDir+"/").equals(SDCARD_ROOT+"")){
				//计算父目录
				String parentDir=currDir.substring(0,currDir.lastIndexOf("/"));
			    MyLog.i("parentDir",parentDir);
				currDir=parentDir;
		       //返回上一级目录
				inflateListView(parentDir);	
			return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	static boolean checkFileName(String name){
		if(name != null&&name.length()>0){
			int i = name.lastIndexOf('.');
			if(i>-1&&i<name.length()){
				String lastName = name.substring(i+1);
				if(lastName.equalsIgnoreCase("jpg")||lastName.equalsIgnoreCase("png")||lastName.equalsIgnoreCase("bmp")||lastName.equalsIgnoreCase("jpeg")){
					return true;
				}
			}
		}
		return false;
	}

}
