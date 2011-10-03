package com.android.drawmemo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;


public class FirstView extends View {
	Rect newmemo;
	Rect memohistory;
	Rect help;
	Rect quit;
	Bitmap newmemopic;
	Bitmap memohistorypic;
	Bitmap newmemodpic;
	Bitmap memohistorydpic;
	Bitmap helppic;
	Bitmap quitpic;
	Bitmap helpdownpic;
	Bitmap quitdownpic;
	Paint paint;
	Paint paint1;
	Paint paint2;
	Paint paint3;
	Paint paint4;
	Paint fontpaint;
	Context mycontext;
	Bitmap bg;
	int INILEFT=50;
	int INITOP=120;
	int INIRIGHT=190;
	int INIBOTTOM=160;
	int VERTICAL_Y=60;
	int buttondown=0;
	Handler myhandler;
	public FirstView(Context context,Handler handler) {
		super(context);
		// TODO Auto-generated constructor stub\
		myhandler=handler;
		mycontext=context;
		paint=new Paint();
		paint1=new Paint();
		paint2=new Paint();
		paint3=new Paint();
		paint4=new Paint();
		fontpaint=new Paint();
		paint.setColor(Color.BLUE);
		paint1.setColor(Color.BLUE);
		paint2.setColor(Color.BLUE);
		paint3.setColor(Color.BLUE);
		paint4.setColor(Color.BLUE);
		fontpaint.setColor(Color.WHITE);
		fontpaint.setTextSize(30);
		newmemo=new Rect(INILEFT,INITOP,INIRIGHT,INIBOTTOM);
		memohistory=new Rect(INILEFT,INITOP+VERTICAL_Y,INIRIGHT,INIBOTTOM+VERTICAL_Y);
		help=new Rect(INILEFT,INITOP+VERTICAL_Y*2,INIRIGHT,INIBOTTOM+VERTICAL_Y*2);
		quit=new Rect(INILEFT,INITOP+VERTICAL_Y*3,INIRIGHT,INIBOTTOM+VERTICAL_Y*3);
//		newmemopic=BitmapFactory.decodeResource(getResources(), R.drawable.newmemopic);
//		memohistorypic=BitmapFactory.decodeResource(getResources(), R.drawable.memohistorypic);
//		newmemodpic=BitmapFactory.decodeResource(getResources(), R.drawable.newmemopic);
//		memohistorydpic=BitmapFactory.decodeResource(getResources(), R.drawable.memohistorypic);
//		helppic=BitmapFactory.decodeResource(getResources(), R.drawable.helppic);
//		quitpic=BitmapFactory.decodeResource(getResources(), R.drawable.quitpic);
//		helpdownpic=BitmapFactory.decodeResource(getResources(), R.drawable.helppic);
//		quitdownpic=BitmapFactory.decodeResource(getResources(), R.drawable.quitpic);
		
		
	}
	public void onDraw(Canvas canvas)
	{	

//		this.setBackgroundResource(R.drawable.mainbg);
		if(buttondown==0)
		{
		canvas.drawBitmap(newmemopic, null, newmemo, paint);
		canvas.drawBitmap(memohistorypic, null, memohistory, paint);
		canvas.drawBitmap(helppic, null, help, paint);
		canvas.drawBitmap(quitpic, null, quit, paint);
		}
		
		switch(buttondown)
		{
	
		case 1:
			
			canvas.drawBitmap(newmemodpic, null, newmemo, paint);
			canvas.drawBitmap(memohistorypic, null, memohistory, paint);
			canvas.drawBitmap(helppic, null, help, paint);
			canvas.drawBitmap(quitpic, null, quit, paint);
			
			break;
		case 2:
			canvas.drawBitmap(newmemopic, null, newmemo, paint);
			canvas.drawBitmap(memohistorydpic, null, memohistory, paint);
			canvas.drawBitmap(helppic, null, help, paint);
			canvas.drawBitmap(quitpic, null, quit, paint);
			break;
		case 3:
			canvas.drawBitmap(newmemopic, null, newmemo, paint);
			canvas.drawBitmap(memohistorypic, null, memohistory, paint);
			canvas.drawBitmap(helpdownpic, null, help, paint);
			canvas.drawBitmap(quitpic, null, quit, paint);
			break;
		case 4:
			canvas.drawBitmap(newmemopic, null, newmemo, paint);
			canvas.drawBitmap(memohistorypic, null, memohistory, paint);
			canvas.drawBitmap(helppic, null, help, paint);
			canvas.drawBitmap(quitdownpic, null, quit, paint);
			break;
			default:
				break;
		}


	}
	public boolean onTouchEvent(MotionEvent event)
	{
		if(newmemo.contains((int)event.getX(), (int)event.getY()))
		{
		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{
			//paint.setColor(Color.GRAY);
			buttondown=1;
			postInvalidate();
			
		}
		if(event.getAction()==MotionEvent.ACTION_UP)
		{
			
			myhandler.sendEmptyMessage(0);
			
		}
		}
		else if(memohistory.contains((int)event.getX(), (int)event.getY()))
		{
		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{
			buttondown=2;
			postInvalidate();
			
		}
		if(event.getAction()==MotionEvent.ACTION_UP)
		{
			myhandler.sendEmptyMessage(1);
		}
		}
		else if(help.contains((int)event.getX(), (int)event.getY()))
		{
		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{
			buttondown=3;
			postInvalidate();
			
		}
		if(event.getAction()==MotionEvent.ACTION_UP)
		{
			new AlertDialog.Builder(mycontext)
            .setTitle("Doodle Memo1.0 ")
            .setMessage(R.string.helpdoc)
            .setPositiveButton("确定", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					
					
				}
	    		
	    	}).show();
		}
		}
		else if(quit.contains((int)event.getX(), (int)event.getY()))
		{
			
		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{
			buttondown=4;
			postInvalidate();
			
		}
		if(event.getAction()==MotionEvent.ACTION_UP)
		{
			
			System.exit(0);
		}
		}
		if(event.getAction()==MotionEvent.ACTION_UP)
		{
			buttondown=0;
			postInvalidate();
		}
		return true;
	}

}
