package com.android.drawmemo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.android.gestureview.Gesture;
import com.android.gestureview.GestureStroke;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

public class Cursor {
	int positionx,positiony;
	int startx,starty;
	int bitmapsize;
	int screenwidth;
	int screenheight;
	Rect rect;
	Cursor(int width,int height)
	{
		bitmapsize=60;
		screenwidth=width;
		screenheight=height;
		int i;
		for(i=0;i<width;i++)
		{
			if((screenwidth-i*bitmapsize)<0) break;
		}
		// to write more words cancel the first space
		//cursorstart=(width%70)/2;
		positionx=(screenwidth-(i-1)*bitmapsize)/2;
		startx=positionx;
		
		positiony=10;
		starty=positiony;
		rect=new Rect(positionx,positiony,positionx+bitmapsize,positiony+bitmapsize);
		
	}
	Cursor(int size,int width,int height)
	{
		bitmapsize=size;
		screenwidth=width;
		screenheight=height;
		int i;
		for(i=0;i<width;i++)
		{
			if((screenwidth-i*bitmapsize)<0) break;
		}
		positionx=(screenwidth-(i-1)*bitmapsize)/2;
		startx=positionx;

		positionx=positionx+bitmapsize;
		positiony=10;
		starty=positiony;
	}
	public int getX()
	{
		return positionx;
	}
	public int getY()
	{
		return positiony;
	}
	public int getbitmapsize()
	{
		return bitmapsize;
	}
	public void backspace(int x,int y)
	{
		positionx=x;
		positiony=y;
		setrectpositioin();
	}
	public void newline()
	{
		positionx=startx;
		positiony+=bitmapsize;
		setrectpositioin();
	}
	public void next()
	{
		if(positionx+2*bitmapsize>screenwidth)				
		{				
			positionx=startx;
			positiony+=bitmapsize;
		}
		else if(positiony+bitmapsize>screenheight)
		{
			positionx=startx;
			positiony=starty;
			
		}
		else
		{
			positionx+=bitmapsize;
			
		}
		setrectpositioin();
		
	}
	public void toinitial()
	{
		positionx=startx;
		positiony=starty;
		setrectpositioin();
	}
	public boolean contains(int x,int y)
	{
		return rect.contains(x, y);
	}
	public void setposition(int x,int y)
	{
		positionx=x;
		positiony=y;
		rect.left=x;
		rect.top=y;
		rect.bottom=y+bitmapsize;
		rect.right=x+bitmapsize;	
	}
	private void setrectpositioin()
	{
		rect.left=positionx;
		rect.right=positionx+bitmapsize;
		rect.top=positiony;
		rect.bottom=positiony+bitmapsize;
	}
	
}
class GestureBox
{
	Gesture mygesture;
	int positionx,positiony;
	int bitmapsize;
	int inset=2;
	Rect rect;
	int color;
	Paint gesturepaint;
	Bitmap mybitmap;
	boolean focus=false;
	int angle=0;
	GestureBox(Gesture gesture,int px,int py,int size,Paint paint)
	{
		mygesture=gesture;
		positionx=px;
		positiony=py;
		bitmapsize=size;
		rect=new Rect(px,py,px+size,py+size);
		gesturepaint=new Paint(paint);
		mybitmap=Bitmap.createBitmap(mygesture.toBitmap(bitmapsize, bitmapsize, inset, gesturepaint));
	}
	GestureBox(Gesture gesture,int px,int py,int size,int angle,Paint paint)
	{
		mygesture=gesture;
		positionx=px;
		positiony=py;
		bitmapsize=size;
		rect=new Rect(px,py,px+size,py+size);
		gesturepaint=new Paint(paint);
		Matrix m = new Matrix();
        m.setRotate(angle,bitmapsize/2,bitmapsize/2);
		mybitmap=Bitmap.createBitmap(mygesture.toBitmap(bitmapsize, bitmapsize, inset, gesturepaint),0,0,bitmapsize,bitmapsize,m,true);
	//	mybitmap=Bitmap.createBitmap(mygesture.toBitmap(bitmapsize, bitmapsize, inset, gesturepaint));
	}
	GestureBox(Gesture gesture,int px,int py,int size,int setinset,int mcolor)
	{
		mygesture=gesture;
		positionx=px;
		positiony=py;
		bitmapsize=size;
		inset=setinset;
	}
	public int getX()
	{
		return positionx;
	}
	public int getY()
	{
		return positiony;
	}
	public Paint getPaint()
	{
		return gesturepaint;
	}
	public Bitmap toBitmap()
	{
		
		return mybitmap;
	}
	public Bitmap focusBitmap()
	{
		return mygesture.toBitmap(2*bitmapsize, 2*bitmapsize, inset, gesturepaint);
	}
	public boolean contains(int x,int y)
	{
		return rect.contains(x, y);
	}
	public void setposition(int x,int y)
	{
		positionx=x;
		positiony=y;
		rect.left=x;
		rect.top=y;
		rect.bottom=y+bitmapsize;
		rect.right=x+bitmapsize;	
	}
	public void setfocus()
	{
		if(focus==false)
		{
			focus=true;
			setposition(positionx-bitmapsize/2,positiony-bitmapsize/2);
			mybitmap.recycle();
			mybitmap=Bitmap.createBitmap(mygesture.toBitmap(bitmapsize*2, bitmapsize*2, inset, gesturepaint));
			
		}
		else
		{
			focus=false;
			setposition(positionx+bitmapsize/2,positiony+bitmapsize/2);
			mybitmap.recycle();
			mybitmap=Bitmap.createBitmap(mygesture.toBitmap(bitmapsize, bitmapsize, inset, gesturepaint));
			
		}
	}
	public boolean getfocus()
	{
		return focus;
	}
	public int getBitmapSize()
	{
		return bitmapsize;
	}
	public void Rotate(int deltX,int deltY)
	{
		mybitmap.recycle();
		 Matrix m = new Matrix();
		 angle+=deltY;
         m.setRotate(angle,bitmapsize/2,bitmapsize/2);
		mybitmap=Bitmap.createBitmap(mygesture.toBitmap(bitmapsize, bitmapsize, inset, gesturepaint),0,0,bitmapsize,bitmapsize,m,true);
	}
	public void Rotate(int angle)
	{
		 Matrix m = new Matrix();
         m.setRotate(angle,bitmapsize/2,bitmapsize/2);
		mybitmap=Bitmap.createBitmap(mygesture.toBitmap(bitmapsize, bitmapsize, inset, gesturepaint),0,0,bitmapsize,bitmapsize,m,true);
	}
	public void Translate(int deltX,int deltY)
	{
		positionx+=deltX;
		positiony+=deltY;
		rect.left=positionx;
		rect.top=positiony;
		rect.bottom=positiony+bitmapsize;
		rect.right=positionx+bitmapsize;	
	}
	public void Scale(int deltX,int deltY)
	{
		bitmapsize-=deltY;
		//avoid of bitmapsize==0
		if(bitmapsize<5) 
			return;
		mybitmap.recycle();
		mybitmap=Bitmap.createBitmap(mygesture.toBitmap(bitmapsize+deltX, bitmapsize+deltX, inset, gesturepaint));
		
		rect.left=positionx;
		rect.top=positiony;
		rect.bottom=positiony+bitmapsize;
		rect.right=positionx+bitmapsize;	
		
	}
    public void serialize(DataOutputStream out) throws IOException {

    	out.writeInt(positionx);
    	out.writeInt(positiony);
    	out.writeInt(angle);
    	out.writeInt(bitmapsize);
    	out.writeInt(gesturepaint.getColor());
    	out.writeFloat(gesturepaint.getStrokeWidth());
    	
    	mygesture.serialize(out);
    	
    }
    public static GestureBox deserialize(DataInputStream in) throws IOException {
        

        // Gesture ID
         int inx= in.readInt();
         int iny= in.readInt();
         int inangle= in.readInt();
         int insize= in.readInt();
         int color=in.readInt();
         float strokewidth=in.readFloat();
         Paint mypaint=new Paint();
         mypaint.setStrokeWidth(strokewidth);
         mypaint.setColor(color);
        return new GestureBox(Gesture.deserialize(in),inx,iny,insize,inangle,mypaint);
    }
	
}
