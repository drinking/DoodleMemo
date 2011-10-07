package com.android.drawmemo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.drinking.drawmemo.ui.GalleryFlow;
import com.drinking.drawmemo.ui.ImageAdapter;
import com.drinking.utils.GlobalValue;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.Toast;

public class MemoHistory extends Activity implements Gallery.OnItemClickListener{
    /** Called when the activity is first created. */
	File[] files;
	int fileindex;
	ImageAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.memoviewer);
        
        File f=new File(GlobalValue.picsavepath);
        	if(!f.exists()){
          	  Toast.makeText(this, "文件不存在...", Toast.LENGTH_SHORT).show();
        	  MemoHistory.this.finish();
        	  return;
        	};
        	
        GalleryFlow g=(GalleryFlow)findViewById(R.id.Gallery01);
        adapter=new ImageAdapter(this, getSD(f));
        adapter.createReflectedImages();
        g.setAdapter(adapter);
        g.setOnItemClickListener(this);
    }
    
    private List<String> getSD(File fi)
    {
     
      List<String> it=new ArrayList<String>();      
      //File f=new File("/sdcard");  
      files=fi.listFiles();
   
      if(files.length==0)
      {
    	  Toast.makeText(this, "no file found!", Toast.LENGTH_SHORT).show(); 
    	  this.finish();
      }
      for(int i=0;i<files.length;i++)
      {
        File file=files[i];
        if(getImageFile(file.getPath()))
          it.add(file.getPath());
        if(file.isDirectory())
        {
      	  it.addAll(getSD(file));
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
    public void onItemClick (AdapterView<?> parent, View view, 
      		 int position, long id) {

       	fileindex=position;
       	new AlertDialog.Builder(MemoHistory.this)
        .setTitle("操作")
        .setItems(R.array.select_dialog_items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	select(which);
            }
        })
        .create().show();

       } 
    public void select(int which)
    {
    	switch(which)
    	{
    	case 0:
    		if(files[fileindex].delete())
    		{
    			Toast.makeText(this, "删除成功...", Toast.LENGTH_SHORT).show();
    			adapter.delete(fileindex);
    			
    			adapter.notifyDataSetChanged();
    		}
    		else
    		{
    			Toast.makeText(this, "删除失败...", Toast.LENGTH_SHORT).show();
    		}
    		break;
    	case 1:
    		Intent intent=new Intent(Intent.ACTION_SEND);
           	intent.setType("img/*");
           	Uri imageuri=Uri.fromFile(files[fileindex]);
           	intent.putExtra(Intent.EXTRA_STREAM, imageuri);
           	intent.setType("image/*");
           	startActivity(Intent.createChooser(intent, "send to :"));
    		break;
    	case 2:
    		Intent i=new Intent(this,CreateMemo.class);
    		i.putExtra("path", files[fileindex].getAbsolutePath());
    		startActivity(i);
    		
    		this.finish();
    		break;
    	
    	}
    }
    @Override     
    public void onBackPressed() {
    	Intent intent=new Intent("android.appwidget.action.SENDTOUPDATE");
   		this.sendBroadcast(intent);
   		super.onBackPressed();
        
    }  
}


