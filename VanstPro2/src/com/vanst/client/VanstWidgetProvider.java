package com.vanst.client;



import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class VanstWidgetProvider extends AppWidgetProvider {
	static int max;
	static int progress;
	static boolean connect;
	static VanstWidgetProvider vanstWidgetProvider;
	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
	}
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		vanstWidgetProvider = this;
		final int N = appWidgetIds.length;
		for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context,WidgetActivity.class);
            Intent intent1 = new Intent(context, WidgetActivity1.class);
            Intent intent2 = new Intent(context, WidgetActivity2.class);
            Intent intent3 = new Intent(context, WidgetActivity3.class);
            Intent intent4 = new Intent(context, WidgetActivity4.class);
            Intent intent5 = new Intent(context, WidgetActivity5.class);
            Intent intent6 = new Intent(context, WidgetActivity6.class);
            Intent intent7 = new Intent(context, WidgetActivity7.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent1, 0);
            PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);
            PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0, intent3, 0);
            PendingIntent pendingIntent4 = PendingIntent.getActivity(context, 0, intent4, 0);
            PendingIntent pendingIntent5 = PendingIntent.getActivity(context, 0, intent5, 0);
            PendingIntent pendingIntent6 = PendingIntent.getActivity(context, 0, intent6, 0);
            PendingIntent pendingIntent7 = PendingIntent.getActivity(context, 0, intent7, 0);
            // Get the layout for the App Widget and attach an on-click listener to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widgetla);
            views.setOnClickPendingIntent(R.id.widgetla_on, pendingIntent1);
            views.setOnClickPendingIntent(R.id.widgetla_off, pendingIntent2);
            views.setOnClickPendingIntent(R.id.widgetla_moshi1, pendingIntent3);
            views.setOnClickPendingIntent(R.id.widgetla_moshi2, pendingIntent4);
            views.setOnClickPendingIntent(R.id.widgetla_moshi3, pendingIntent5);
            views.setOnClickPendingIntent(R.id.widgetla_moshi4, pendingIntent6);
            views.setOnClickPendingIntent(R.id.widgetla_connect, pendingIntent);
            views.setOnClickPendingIntent(R.id.widgetla_room,pendingIntent7);
            updateAppWidget(context);
            // Tell the AppWidgetManager to perform an update on the current App Widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	
	public static void initAppWidget(Context context){
		if(vanstWidgetProvider!=null)
		vanstWidgetProvider.onEnabled(context);
		RemoteViews remoteViews  = new  RemoteViews(context.getPackageName(),R.layout.widgetla);    
		//进行控件的修改，比如图片等    
        
        if(MainActivity.widgetbusy)
		remoteViews.setViewVisibility(R.id.widget_image, View.VISIBLE);
        else remoteViews.setViewVisibility(R.id.widget_image, View.GONE);
		remoteViews.setProgressBar(R.id.widget_image, max, progress, false);
		if(MainView.roomNum == 0 || !Connect.isEnter){
			remoteViews.setTextViewText(R.id.widgetla_room, "房间");
			remoteViews.setTextColor(R.id.widgetla_room, Color.BLACK);
		}else{
			remoteViews.setTextViewText(R.id.widgetla_room, "房间"+MainView.roomNum);
			remoteViews.setTextColor(R.id.widgetla_room, Color.GREEN);
		}
		if(Connect.isEnter){
			remoteViews.setImageViewResource(R.id.widgetla_vanst,R.drawable.vanstgreenlogo);
		}else{
			remoteViews.setImageViewResource(R.id.widgetla_vanst,R.drawable.vanstwhitelogo);
		}
		
		if(Connect.flag){
			remoteViews.setTextColor(R.id.widgetla_connect, Color.GREEN);
		}else{
			remoteViews.setTextColor(R.id.widgetla_connect, Color.BLACK);
		}
		if(Connect.widgetConnect){
			remoteViews.setViewVisibility(R.id.widgetla_connectprogressbar, View.VISIBLE);
		}else{
			remoteViews.setViewVisibility(R.id.widgetla_connectprogressbar, View.GONE);
		}
		if(MainActivity.widgetroom){
			remoteViews.setViewVisibility(R.id.widgetla_roomprogressbar, View.VISIBLE);
		}else{
			remoteViews.setViewVisibility(R.id.widgetla_roomprogressbar, View.GONE);
		}
		Intent intent = new Intent(context,WidgetActivity.class);
        Intent intent1 = new Intent(context, WidgetActivity1.class);
        Intent intent2 = new Intent(context, WidgetActivity2.class);
        Intent intent3 = new Intent(context, WidgetActivity3.class);
        Intent intent4 = new Intent(context, WidgetActivity4.class);
        Intent intent5 = new Intent(context, WidgetActivity5.class);
        Intent intent6 = new Intent(context, WidgetActivity6.class);
        Intent intent7 = new Intent(context, WidgetActivity7.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent1, 0);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);
        PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0, intent3, 0);
        PendingIntent pendingIntent4 = PendingIntent.getActivity(context, 0, intent4, 0);
        PendingIntent pendingIntent5 = PendingIntent.getActivity(context, 0, intent5, 0);
        PendingIntent pendingIntent6 = PendingIntent.getActivity(context, 0, intent6, 0);
        PendingIntent pendingIntent7 = PendingIntent.getActivity(context, 0, intent7, 0);
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widgetla);
        remoteViews.setOnClickPendingIntent(R.id.widgetla_on, pendingIntent1);
        remoteViews.setOnClickPendingIntent(R.id.widgetla_off, pendingIntent2);
        remoteViews.setOnClickPendingIntent(R.id.widgetla_moshi1, pendingIntent3);
        remoteViews.setOnClickPendingIntent(R.id.widgetla_moshi2, pendingIntent4);
        remoteViews.setOnClickPendingIntent(R.id.widgetla_moshi3, pendingIntent5);
        remoteViews.setOnClickPendingIntent(R.id.widgetla_moshi4, pendingIntent6);
        remoteViews.setOnClickPendingIntent(R.id.widgetla_connect, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.widgetla_room,pendingIntent7);
		
		//获得appwidget管理实例，用于管理appwidget以便进行更新操作   
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);      //相当于获得所有本程序创建的appwidget   
		ComponentName componentName = new ComponentName(context,VanstWidgetProvider.class);      //更新
		appWidgetManager.updateAppWidget(componentName, remoteViews);   
	}
	
	public static void updateAppWidget(Context context) {   
		initAppWidget(context);
//        RemoteViews remoteViews  = new  RemoteViews(context.getPackageName(),R.layout.widgetla);    
//		//进行控件的修改，比如图片等    
//        
//        
//        
//        
//        
//        if(MainActivity.widgetbusy)
//		remoteViews.setViewVisibility(R.id.widget_image, View.VISIBLE);
//        else remoteViews.setViewVisibility(R.id.widget_image, View.GONE);
//		remoteViews.setProgressBar(R.id.widget_image, max, progress, false);
//		if(MainView.roomNum == 0 || !Connect.isEnter){
//			remoteViews.setTextViewText(R.id.widgetla_room, "房间");
//			remoteViews.setTextColor(R.id.widgetla_room, Color.BLACK);
//		}else{
//			remoteViews.setTextViewText(R.id.widgetla_room, "房间"+MainView.roomNum);
//			remoteViews.setTextColor(R.id.widgetla_room, Color.GREEN);
//		}
//		
//		
//		if(Connect.flag){
//			remoteViews.setTextColor(R.id.widgetla_connect, Color.GREEN);
//		}else{
//			remoteViews.setTextColor(R.id.widgetla_connect, Color.BLACK);
//		}
//		if(Connect.widgetConnect){
//			remoteViews.setViewVisibility(R.id.widgetla_connectprogressbar, View.VISIBLE);
//		}else{
//			remoteViews.setViewVisibility(R.id.widgetla_connectprogressbar, View.GONE);
//		}
//		if(MainActivity.widgetroom){
//			remoteViews.setViewVisibility(R.id.widgetla_roomprogressbar, View.VISIBLE);
//		}else{
//			remoteViews.setViewVisibility(R.id.widgetla_roomprogressbar, View.GONE);
//		}
//		//获得appwidget管理实例，用于管理appwidget以便进行更新操作   
//		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);      //相当于获得所有本程序创建的appwidget   
//		ComponentName componentName = new ComponentName(context,VanstWidgetProvider.class);      //更新
//		appWidgetManager.updateAppWidget(componentName, remoteViews);    
//        if (list.size() > 3) {   
//            views.setTextViewText(R.id.textView01, list.get(0).title);   
//            views.setTextViewText(R.id.textView02, list.get(1).title);   
//            views.setTextViewText(R.id.textView03, list.get(2).title);   
//        }   
//        Intent detailIntent=new Intent(context,NewsSiteList.class);   
//        PendingIntent pending=PendingIntent.getActivity(context, 0, detailIntent, 0);   
//        views.setOnClickPendingIntent(R.id.textView01, pending);   
//        views.setOnClickPendingIntent(R.id.textView02, pending);   
//        views.setOnClickPendingIntent(R.id.textView03, pending);   
    } 
	
	@Override
	public void onReceive(Context context, Intent intent) {
		vanstWidgetProvider = this;
		if(intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE")){
//			Toast.makeText(context, "vanst", Toast.LENGTH_SHORT).show();
			onEnabled(context);
			initAppWidget(context);
		}
//		if (intent.getAction().equals("vasnt.widget.refresh"))   {        
//			//只能通过远程对象来设置appwidget中的控件状态   
//			RemoteViews remoteViews  = new  RemoteViews(context.getPackageName(),R.layout.widgetla);    
//			//进行控件的修改，比如图片等    
//			remoteViews.setViewVisibility(R.id.widget_image, View.VISIBLE);
//			remoteViews.setProgressBar(R.id.widget_image, max, progress, true);
//			//获得appwidget管理实例，用于管理appwidget以便进行更新操作   
//			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);      //相当于获得所有本程序创建的appwidget   
//			ComponentName componentName = new ComponentName(context,VanstWidgetProvider.class);      //更新
//			appWidgetManager.updateAppWidget(componentName, remoteViews);    
//			}
		super.onReceive(context, intent);
	}
}
