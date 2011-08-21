package com.android.drawmemo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.drinking.utils.GlobalValue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;



public class MemoViewer extends Activity implements Gallery.OnItemClickListener{
    /** Called when the activity is first created. */
	File[] files;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        	WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.memoviewer);
        
        int a=0;
        
        for(int i=0;i<10;++i)
        	a+=i;
        
        Gallery g=(Gallery)findViewById(R.id.Gallery01);
        File f=new File(GlobalValue.mbgpath);
        g.setAdapter(new ImageAdapter(this, getSD(f)));// 设置 Gallery 控件的图片源
        g.setOnItemClickListener(this);
       

        
    }
    public void onItemClick (AdapterView<?> parent, View view, 
   		 int position, long id) {
    	
     // TODO Auto-generated method stub
     String path=files[position].getPath();
     Intent i = new Intent();   
     Bundle b = new Bundle();   
     b.putString("MEMOPATH", path);
     Log.i(path, "oooooo");
     i.putExtras(b);   
     this.setResult(RESULT_OK, i);   
     this.finish();  
    	 
    }
    
    private List<String> getSD(File fi)
    {
      /* 设定目前所?路径 */
      List<String> it=new ArrayList<String>();      
      //File f=new File("/sdcard");  
      files=fi.listFiles();
   
      /* 将所有档案叵丈ArrayList中 */
      for(int i=0;i<files.length;i++)
      {
        File file=files[i];
        if(getImageFile(file.getPath()))
          it.add(file.getPath());
        if(file.isDirectory())
        {
      	  it.addAll(getSD(file));
      	  //getSD(file);
        }
        }
      return it;
    }
    private boolean getImageFile(String fName)
    {
      boolean re;
      
      String end=fName.substring(fName.lastIndexOf(".")+1,
                    fName.length()).toLowerCase(); 
      
      if(end.equals("jpg")||end.equals("gif")||end.equals("png")
              ||end.equals("jpeg")||end.equals("bmp"))
      {
        re=true;
      }
      else
      {
        re=false;
      }
      return re; 
    }
}
class ImageAdapter extends BaseAdapter {  
	 private Context mContext; //define Context  
	 private List<String>lis; 
	 public ImageAdapter(Context c,List<String>li) { //define ImageAdapter  
	        mContext = c;  
	        lis=li;
	    }  
	  
	    //get the picture number  
	    public int getCount() {  
	    	
	        return lis.size();  
	    }  
	     
	    public Object getItem(int position) {  
	        return position;  
	    }  
	  
	    public long getItemId(int position) {  
	        return position;  
	    }  
	  
	    public View getView(int position, View convertView, ViewGroup parent) 
	    {  
	        ImageView i = new ImageView(mContext);  
	        Bitmap bm = BitmapFactory.decodeFile(lis.
                    get(position).toString());
	        i.setImageBitmap(bm);
	        //i.setImageResource(mImageIds[position]);//set resource for the imageView  
	        i.setLayoutParams(new Gallery.LayoutParams(150,266));//layout  
	        i.setScaleType(ImageView.ScaleType.FIT_XY);//set scale type  
	        return i;  
	    }  

	}  

