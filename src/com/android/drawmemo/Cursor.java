package com.android.drawmemo;

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

