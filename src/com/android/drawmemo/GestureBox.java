package com.android.drawmemo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import com.android.gestureview.Gesture;

public class GestureBox {

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
