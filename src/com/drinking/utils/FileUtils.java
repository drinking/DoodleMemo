package com.drinking.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.android.drawmemo.CreateMemo;
import com.android.drawmemo.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FileUtils {
	public static boolean Exists(String path)
	{
		File f=new File(path);
		return f.exists();
		
	}
	public static boolean CreateFolder(String path)
	{
		File f=new File(path); //"/sdcard/DoodleMemo/saved/test"
		try{ 
		        
			if(!f.exists())//���ж�Ŀ¼�Ƿ���ڣ� 
			{
				return f.mkdirs();//Ŀ¼�����ڽ���Ŀ¼�� 
				
			}
			
			}
		catch(Exception   e)
		{
			e.printStackTrace();
			Log.i("CreateFolder", "fail");
		}
		return false;
	}
	public static boolean DeleteAllFiles(String path)
	{
		File   f=new   File(path);//���ļ�Ŀ¼ ����"/sdcard/memo/saved/"
		boolean del=false;
		try{ 
			if(f.exists())
			{
				File[] files=f.listFiles();

				for(int i=0;i<files.length;i++)
				{
					if(files[i].isFile())
					{
						del=files[i].delete();
					}
				}				
			}
			return del;
		}
		
		catch(Exception   e)
		{
			e.printStackTrace();
			return false;
		}
		
	}
	public static boolean ResourceToSD(Resources res,String path)
	{
		FileOutputStream out;
		try {
			//Ч������ ��֪����ô��
			for(int i=0;i<4;i++)
			{
				out = new FileOutputStream(path+"/memoybg"+Integer.toString(i)+".png");
				BitmapFactory.decodeResource(res, R.drawable.year1+i).compress(Bitmap.CompressFormat.PNG, 90, out);
			}
			
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	public static boolean SaveBitmapToSD(String path ,Bitmap mbitmap)
	{           	
            	try
            	{	
        	    	FileOutputStream out = new FileOutputStream(path);
        	    	return mbitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
        	    	
            	}
        		catch(IOException e){
        			return false;
        		}	
	}
	public static File getFile(String path)
	{
		File file=new File(path);
		return file;
	}

}
