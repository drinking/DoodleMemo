package com.android.drawmemo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.android.gestureview.Gesture;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;


public class Display extends View {
    public interface OnViewChanged {
        void viewchanged();
    }
    private GestureDetector mGestureDetector;  
    OnViewChanged listener;
	//屏幕长宽，须根据不同屏幕设置
	private static int screenwidth=320;		//M9 size 320*480 milestone size 320*569;
	private static int screenheight=480;
	private final int WRITE_MODEL=1;
	private final int DRAW_MODLE=2;
	private boolean SELECT=false;
	private int MENU_ID=0;
	private final int MENU_TRANSLATE=2;
	private final int MENU_SCALE=3;
	private final int MENU_ROTATE=4;
	int MODEL=WRITE_MODEL;//模式切换 书写模式1和绘图模式2
	ArrayList<Path> drawpath=new ArrayList<Path>();
	Path mPath=new Path();
	ArrayList<GestureBox>gesturelist=new ArrayList<GestureBox>();
	Paint mypaint;
	Paint cursorpaint;
	Canvas mycanvas=new Canvas();
	Bitmap drawbitmap=null;
	Cursor mycursor=null;
	int currentboxindex=-1;
	int line=0;
	int cursorstart=0;
	int previousx,previousy;
	int menu_x,menu_y;
	//绘图在bitmap上
	Bitmap  mBitmap;
	Bitmap mBackground;
	Bitmap mMenu;
	Canvas  mCanvas;
	Paint   mBitmapPaint;
	Bitmap backupbitmap;
	Gesture selectedgesture;
	
	public Display(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
		
	}
	public Display(Context context,AttributeSet attars)
	{
		super(context,attars);
		init();
	}
	public void addlistener(OnViewChanged lis)
	{
		listener=lis;
	}
	public void init()
	{
		mypaint=new Paint();
        mypaint.setAntiAlias(true);
        mypaint.setDither(true);
        mypaint.setColor(Color.BLACK);
        mypaint.setStyle(Paint.Style.STROKE);
        mypaint.setStrokeJoin(Paint.Join.ROUND);
        mypaint.setStrokeCap(Paint.Cap.ROUND);
        mypaint.setStrokeWidth(12);
        cursorpaint=new Paint(mypaint);
        cursorpaint.setStrokeWidth(4);
        
        mBitmap = Bitmap.createBitmap(screenwidth, screenheight, Bitmap.Config.ARGB_8888);    
        //mBitmap=Bitmap.createBitmap(BitmapFactory.decodeFile("/sdcard/memo/memopaper/05.jpg"));
        //mBackground=Bitmap.createScaledBitmap(BitmapFactory.decodeFile("/sdcard/memo/memopaper/05.jpg"), screenwidth, screenheight,false); 
        mBackground=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.year2), screenwidth, screenheight,false); 
        //backupbitmap=Bitmap.createBitmap(mBitmap);
        mMenu=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.menu), 180, 180,false); 
        mCanvas = new Canvas(mBitmap);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mycursor=new Cursor(screenwidth,screenheight);
        mGestureDetector = new GestureDetector(new MySimpleGesture());  
			
	}
	public void setpaint(Paint paint)
	{
		//mypaint.set(paint);
		mypaint.setColor(paint.getColor());
		mypaint.setStrokeWidth(paint.getStrokeWidth());
		cursorpaint.setColor(paint.getColor());
		mypaint.setXfermode(null);
	}
	public void AddGesture(Gesture mygesture)
	{
		gesturelist.add(new GestureBox(mygesture,mycursor.getX(),mycursor.getY(),mycursor.getbitmapsize(),mypaint));
		mycursor.next();
		postInvalidate();
	}
	public void Clear()
	{
		if(gesturelist.size()>0&&mycursor!=null)
		{
		gesturelist.clear();
		mycursor.toinitial();
		
		}
		if(mBitmap!=null&&mycursor==null)
		{
			 mBitmap.recycle();
			 mBitmap = Bitmap.createBitmap(screenwidth, screenheight, Bitmap.Config.ARGB_8888);
			 mCanvas.setBitmap(mBitmap);
		}
		invalidate();
	}
	public void Eraser()
	{
		mypaint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.CLEAR));
	}
	public void onDraw(Canvas canvas)
	{	
		canvas.drawBitmap(mBackground, 0, 0, mBitmapPaint);
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		canvas.drawPath(mPath, mypaint);
		if(gesturelist.size()>0)
		{
			for(GestureBox gb:gesturelist)
			{
				canvas.drawBitmap(gb.toBitmap(), gb.getX(), gb.getY(), gb.getPaint());
			}
		}
		if(mycursor!=null)
		{
		canvas.drawLine(mycursor.getX(),mycursor.getY()+mycursor.getbitmapsize(),mycursor.getX()+mycursor.getbitmapsize() , mycursor.getY()+mycursor.getbitmapsize(), cursorpaint);
		}
		if(SELECT)
		{
			int size=gesturelist.get(currentboxindex).getBitmapSize();
			int offset=(size-mycursor.getbitmapsize()*3)/2;
			menu_x=gesturelist.get(currentboxindex).getX()+offset;
			menu_y=gesturelist.get(currentboxindex).getY()+offset;
			canvas.drawBitmap(mMenu, menu_x, menu_y, mypaint);
			
		}
		
	}
	public void newline()
	{
		if(mycursor!=null)
		mycursor.newline();
		invalidate();
	}

	public void backspace()
	{
		if(mycursor!=null&&gesturelist.size()>0)
		{
		mycursor.backspace(gesturelist.get(gesturelist.size()-1).getX(), gesturelist.get(gesturelist.size()-1).getY());
		gesturelist.remove(gesturelist.size()-1);
		invalidate();
		}
	}
	public int changemodel()
	{
		if(MODEL==WRITE_MODEL)
		{
			mycursor=null;
			MODEL=DRAW_MODLE;
			invalidate();
			return DRAW_MODLE;
			
		}
		else 
		{
			mycursor=new Cursor(screenwidth,screenheight);
			MODEL=WRITE_MODEL;
			invalidate();
			return WRITE_MODEL;
		}
	}
	public void changebackground(String str)
	{
		mBackground.recycle();//清除原有的bitmap
		mBackground=Bitmap.createScaledBitmap(BitmapFactory.decodeFile(str), screenwidth, screenheight,false); 
		//backupbitmap.recycle();
	    //backupbitmap=Bitmap.createBitmap(mBitmap);//备份
	    //mCanvas.setBitmap(mBitmap);//不设置则不能画
		invalidate();
	     
	}
	public Bitmap toBitmap()
	{
		 mCanvas.setBitmap(mBackground);
		 mCanvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		 if(gesturelist.size()>0)
			{
				for(GestureBox gb:gesturelist)
				{
					mCanvas.drawBitmap(gb.toBitmap(), gb.getX(), gb.getY(), gb.getPaint());
				
				}
			}
		 return mBackground;
	}
	 private float mX, mY;
     private static final float TOUCH_TOLERANCE = 4;
    private void touch_down(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mypaint);
        // kill this so we don't double draw
        mPath.reset();
    }
    private void writemodel_touch_up(int x,int y) {
    	//currentboxindex=-1;
    	if(-2==currentboxindex)
    	{
    		currentboxindex=-1;
    	}
    	SELECT=false;
    	MENU_ID=0;
    }
    private void writemodel_touch_down(int x, int y) {
    	previousx=x;
    	previousy=y;
    	if(mycursor.contains(x, y)) 
    	{
    		currentboxindex=-2;
    		SELECT=false;
    		return;
    	} 	
    	if(SELECT)
    	SelectMenu(x,y);
    }
    private void writemodel_touch_move(int x, int y) {
    	if(x==previousx&&y==previousy) return;
    	int deltx=x-previousx;
    	int delty=y-previousy;
    	previousx=x;
    	previousy=y;

    	if(currentboxindex==-2)
    	{
    		mycursor.setposition(x-30, y-30);
    	}
    	switch(MENU_ID)
    	{
    	case MENU_TRANSLATE:
    		gesturelist.get(currentboxindex).Translate(deltx, delty);
    		break;
    	case MENU_SCALE:
    		gesturelist.get(currentboxindex).Scale(deltx, delty);
    		break;
    	case MENU_ROTATE:
    		gesturelist.get(currentboxindex).Rotate(deltx, delty);
    		break;
    		
    		
    	}
    }
    @Override 
    public boolean onTouchEvent(MotionEvent e) {
        int x = (int)e.getX();
        int y = (int)e.getY();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
        	if(MODEL==WRITE_MODEL)
        		writemodel_touch_move(x,y);
        	else
        		touch_move(x,y);
        	invalidate();	 
        	break;
        case MotionEvent.ACTION_DOWN:
        	if(MODEL==WRITE_MODEL)
        	writemodel_touch_down(x,y);
        	else
        	touch_down(x,y);
        	 invalidate();
        	break;
        case MotionEvent.ACTION_UP:
        	Log.i("upupupup", "upupupup");
        	if(MODEL==WRITE_MODEL)
        	writemodel_touch_up(x,y);
        	else
        		touch_up();      		
        	invalidate();
        	Log.i("upupupup", "upupupup");
        	break;
           
        }

        mGestureDetector.onTouchEvent(e);
        return true;
    }
    private class MySimpleGesture extends SimpleOnGestureListener {    
        public boolean onDoubleTap(MotionEvent e) {  
           // Log.i("MyGesture", "onDoubleTap");
        	if(MODEL==WRITE_MODEL)
        	listener.viewchanged();    
        	
            return super.onDoubleTap(e);  
        }  

        public boolean onDoubleTapEvent(MotionEvent e) {  
          //  Log.i("MyGesture", "onDoubleTapEvent");  
            return super.onDoubleTapEvent(e);  
        }  

        public boolean onDown(MotionEvent e) {  
           // Log.i("MyGesture", "onDown");  
            return super.onDown(e);  
        }  

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {  
           // Log.i(Float.toString(velocityY), "onFling");  
            return super.onFling(e1, e2, velocityX, velocityY);  
        }  

        public void onLongPress(MotionEvent e) {  
           // Log.i("MyGesture", "onLongPress");   
        
            super.onLongPress(e);  
        }  

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {  
         //   Log.i("MyGesture", "onScroll");
            
            return super.onScroll(e1, e2, distanceX, distanceY);  
        }  

        public void onShowPress(MotionEvent e) {  
           // Log.i("MyGesture", "onShowPress");  
            super.onShowPress(e);  
        }  

        public boolean onSingleTapConfirmed(MotionEvent e) {  
           // Log.i("MyGesture", "onSingleTapConfirmed");  
            return super.onSingleTapConfirmed(e);  
        }  
        public boolean onSingleTapUp(MotionEvent e) {  
            Log.i("MyGesture", "onSingleTapUp");  
            SELECT=false;
            for(int i=0;i<gesturelist.size();i++)
        	{
        		if(gesturelist.get(i).contains((int)e.getX(), (int)e.getY()))
        		{
        			currentboxindex=i;     			
        			SELECT=true;
        			break;
        		}  
        	}
           
            return super.onSingleTapUp(e);  
        }  
    }
	public void SelectMenu(int x,int y)
	{
		if(currentboxindex>-1)
		{
			//set false before return
			SELECT=false;
			if(x>menu_x+60&&x<menu_x+120&&y>menu_y&&y<menu_y+60)
			{
				//delete region
				  gesturelist.remove(currentboxindex);
				  currentboxindex=-1;
				  return;
			}
			if(x>menu_x+60&&x<menu_x+120&&y>menu_y+120&&y<menu_y+180)
			{
				MENU_ID=MENU_TRANSLATE;
				return;
			}
			if(x>menu_x&&x<menu_x+60&&y>menu_y+60&&y<menu_y+120)
			{
				MENU_ID=MENU_SCALE;
				return;
			}
			if(x>menu_x+120&&x<menu_x+180&&y>menu_y+60&&y<menu_y+120)
			{
				MENU_ID=MENU_ROTATE;
				return;
			}
			
			
		}
	}
	public ArrayList<GestureBox> getGestureBox()
	{
		return gesturelist;
	}
	public void setGestureBox(ArrayList<GestureBox> gb)
	{
		gesturelist=gb;
	}
	public boolean saveMemo(String filename)// throws IOException 
	{
		try
    	{
		DataOutputStream out=new DataOutputStream(new FileOutputStream(filename)); 
		out.writeInt(gesturelist.size());
		for(int i=0;i<gesturelist.size();i++)
		{
			gesturelist.get(i).serialize(out);
		}
    	}catch(IOException e){
			return false;
		}	
		return true;
	}
	public void readMemo(String filepath)
	{
		try {
			DataInputStream in=new DataInputStream(new FileInputStream(filepath));
			try {
				int count=in.readInt();
				for(int i=0;i<count;i++)
				{
					gesturelist.add(GestureBox.deserialize(in));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}

