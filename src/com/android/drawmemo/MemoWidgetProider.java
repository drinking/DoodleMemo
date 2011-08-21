package com.android.drawmemo;




import java.io.File;

import com.drinking.utils.GlobalValue;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;


public class MemoWidgetProider extends AppWidgetProvider {

	public void onUpdate(Context context,AppWidgetManager appWidgetManager,int[] appWidgetIds)
	{	
		super.onUpdate(context, appWidgetManager, appWidgetIds); 
		updateMemo(context);		 
	}
	 @Override  
	    public void onReceive(Context context, Intent intent) {  
	        // TODO Auto-generated method stub  
		 	//if not call the super function,the onUpdate() function will not responsible
		 	super.onReceive(context, intent); 
		 	updateMemo(context);			
	    }  
	 
	 private void updateMemo(Context context)
	 {
	        RemoteViews remoteview=new RemoteViews(context.getPackageName(),R.layout.memowidget);
			File f=new File(GlobalValue.picsavepath);
			File []files=f.listFiles();
			if(files.length>0)
			{
			Bitmap mbitmap=BitmapFactory.decodeFile(files[0].getPath());
			remoteview.setImageViewBitmap(R.id.ImageView01, mbitmap);
			remoteview.setImageViewResource(R.id.NewMemo, R.drawable.year2);
			Intent myintent = new Intent(context, CreateMemo.class);
			myintent.putExtra("path", files[0].getPath());
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myintent,PendingIntent.FLAG_UPDATE_CURRENT);
			remoteview.setOnClickPendingIntent(R.id.ImageView01, pendingIntent);
			
			Intent nintent = new Intent(context, CreateMemo.class);
			//int requestCode set 1,so that pendingintent won't use the same intent
			PendingIntent npendingintent = PendingIntent.getActivity(context, 1, nintent,0);
			remoteview.setOnClickPendingIntent(R.id.NewMemo, npendingintent);
			
			AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
			appWidgetManager.updateAppWidget(new ComponentName(context,MemoWidgetProider.class), remoteview);
			} 
	 }
	
}
