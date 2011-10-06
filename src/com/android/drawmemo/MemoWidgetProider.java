package com.android.drawmemo;




import java.io.File;

import com.drinking.utils.FileUtils;
import com.drinking.utils.GlobalValue;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;

public class MemoWidgetProider extends AppWidgetProvider {

	public void onUpdate(Context context,AppWidgetManager appWidgetManager,int[] appWidgetIds)
	{	
		super.onUpdate(context, appWidgetManager, appWidgetIds); 
		updateMemo(context);		 
	}
	 @Override  
	    public void onReceive(Context context, Intent intent) {  
		 	//if not call the super function,the onUpdate() function will not responsible
		 	super.onReceive(context, intent);
		 	
		 	if(intent.getAction().equals("android.appwidget.action.VIEW_MEMO_DETAIL")) {
		 		

		 		
		 	} else if(intent.getAction().equals("android.appwidget.action.UPDATE_DESK_MEMO")){
				File f=new File(GlobalValue.datsavepath);
				if(!f.exists())
					return;
				
				File []files=f.listFiles();
				Bitmap bitmap=null;
				if(files.length>0) {
					bitmap=FileUtils.readToNormal(files[0].getPath(),30,30,20);	
				}
		 		RemoteViews remoteview=new RemoteViews(context.getPackageName(),R.layout.memowidget);
		 		if(bitmap!=null) {
		 		remoteview.setImageViewBitmap(R.id.Desk_Memo, bitmap);
		 		}
		 		AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
				appWidgetManager.updateAppWidget(new ComponentName(context,MemoWidgetProider.class), remoteview);	
		 	}else {
		 		
		 		updateMemo(context);
		 	}
	    }  
	 
	 private void updateMemo(Context context)
	 { 
	        RemoteViews remoteview=new RemoteViews(context.getPackageName(),R.layout.memowidget);

			//implement view detail function
			
			Intent viewMemoIntent = new Intent(context, CreateMemo.class);
			viewMemoIntent.putExtra("path", "viewDetail");
			PendingIntent viewPendingIntent = PendingIntent.getActivity(context, 0, viewMemoIntent,PendingIntent.FLAG_UPDATE_CURRENT);
			remoteview.setOnClickPendingIntent(R.id.Desk_Memo, viewPendingIntent);
			
			//int requestCode set 1,so that pendingintent won't use the same intent
			//implement create new memo function
			
			Intent newMemoIntent = new Intent(context, CreateMemo.class);
//			newMemoIntent.putExtra("path", "newfile");
			PendingIntent newPendingIntent = PendingIntent.getActivity(context, 1, newMemoIntent,0);
			remoteview.setOnClickPendingIntent(R.id.desk_new_button, newPendingIntent);
			
			//init desk View
			Intent updateViewIntent=new Intent("android.appwidget.action.UPDATE_DESK_MEMO");
			context.sendBroadcast(updateViewIntent);
			
			
			//update widget
			AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
			appWidgetManager.updateAppWidget(new ComponentName(context,MemoWidgetProider.class), remoteview);
			
	 }
	
}
