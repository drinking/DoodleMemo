package com.android.drawmemo;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.android.gestureview.ColorPickerDialog;
import com.android.gestureview.Gesture;
import com.android.gestureview.GestureOverlayView;
import com.drinking.utils.FileUtils;
import com.drinking.utils.GlobalValue;
import com.drinking.utils.Logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class CreateMemo extends Activity implements 
													Display.OnViewChanged{
	private final int REQUEST_CHANGE_MEMOBG=1;
	private final int REQUEST_SET_PAINT=2;
    private GestureOverlayView mgestureview;
    private Display displayview;			
    private Gesture mGesture;				
    private boolean canwrite=false;
    private boolean menuselect=false;
    private View hider;										
    ArrayList<Gesture> gestures=new ArrayList<Gesture>();
    Paint mPaint;
    private String fn=null;
    String filepath=null;
    
    public Logger log=new Logger("CreateMemo");

    public void paintChanged(Paint paint) {
    	//TODO
    	mPaint.set(paint);
        //mgestureview.setGesturePaint(mPaint);//完全复制会有错误，应该是某个值没设置
    	mgestureview.setGestureColor(paint.getColor());
    	mgestureview.setGestureStrokeWidth(paint.getStrokeWidth());
    	mgestureview.refresh();
        
        displayview.setPaint(mPaint);
    }
    public void viewchanged()
    {
    	if(canwrite==false)
    	{
    		mgestureview.setVisibility(View.VISIBLE);
    		hider.setVisibility(View.VISIBLE);
    		canwrite=true;
    	}
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.creatememo);
      
        /** use for get screen size
         *   WindowManager manager = getWindowManager(); 
        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();
        Log.i(Integer.toString(width), Integer.toString(height));
        */
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        //
        mgestureview=(GestureOverlayView)findViewById(R.id.gestures_overlay);
        mgestureview.setGestureStrokeType(1);
        //mgestureview.setGestureStrokeWidth(20);
        mgestureview.setGestureColor(mPaint.getColor());
        mgestureview.setGestureStrokeWidth(mPaint.getStrokeWidth());
        mgestureview.refresh();
        
        displayview=(Display)findViewById(R.id.displayview);
        displayview.setPaint(mPaint);
        displayview.addlistener(this);
        hider=findViewById(R.id.hider);
        
        Intent i=this.getIntent();
        filepath=i.getStringExtra("path");
        if(filepath!=null&&!filepath.equals("newfile"))
        {
			File f=new File(GlobalValue.picsavepath);
			File []files=f.listFiles();
			if(files!=null)
				filepath=files[0].getPath();
			else
				return;
        //displayview.changebackground(filepath);
        String path="/sdcard/DoodleMemo/save/dat/"+filepath.substring(filepath.length()-23, filepath.length()-4)+".dat";
        //filepath=filepath+"dat";
        displayview.readMemo(path);
        }
        

    }
    public void viewswitch()
    {	
    	if(canwrite==false)
    	{
    		mgestureview.setVisibility(View.VISIBLE);
    		hider.setVisibility(View.VISIBLE);
    		canwrite=true;
    	}
    	else
    	{	
    		mgestureview.setVisibility(View.INVISIBLE);
    		hider.setVisibility(View.INVISIBLE);
        	canwrite=false;
    	}

    }
    public void next(View v)
    {
    	mGesture=mgestureview.getGesture();
    	mgestureview.clear();
    	if(mGesture!=null)
    	{
    	gestures.add(mGesture);
    	displayview.AddGesture(mGesture);
    	}
    	else
    	{	//	对空格的处理
    		gestures.add(new Gesture());
        	displayview.AddGesture(new Gesture());
    	}
    }
    public void redraw(View v)
    {
    	mgestureview.clear(); 
    }

    public void save(){
    	
    	LayoutInflater factory=LayoutInflater.from(CreateMemo.this);
		final View v1=factory.inflate(R.layout.getfile,null);
                         //R.layout.login与login.xml文件名对应,把login转化成View类型

		EditText edt=(EditText) v1.findViewById(R.id.filename);
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");  
		Date curDate=new Date(System.currentTimeMillis());//获取当前时间     
		String filename=formatter.format(curDate);    
		edt.setText(filename);
		
		if(!menuselect)
		{
		AlertDialog.Builder dialog=new AlertDialog.Builder(CreateMemo.this);
		dialog.setTitle("保存");
		dialog.setView(v1);	
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	EditText et  = (EditText) v1.findViewById(R.id.filename);
            	fn=et.getText().toString();
        	    String picpath=GlobalValue.picsavepath+"/"+fn+".jpg";
        	    String datpath=GlobalValue.datsavepath+"/"+fn+".dat"; 
        	   	if(FileUtils.SaveBitmapToSD(picpath, displayview.toBitmap())&&displayview.saveMemo(datpath))      	    	       	    	
        	   		Toast.makeText(CreateMemo.this, "Saved "+fn, Toast.LENGTH_SHORT).show();
        	   	else
        	   		Toast.makeText(CreateMemo.this, "fail to save", Toast.LENGTH_SHORT).show();
        	   	
        	   	
        	   	Intent intent=new Intent("android.appwidget.action.UPDATE_DESK_MEMO");
    	   		CreateMemo.this.sendBroadcast(intent);
        	   
        	  	CreateMemo.this.finish();
        	  
        	  
            }
        });
		dialog.setNeutralButton("退出",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	
            		CreateMemo.this.finish();
            }
        });
		
        dialog.setNegativeButton("取消",null);
        
        dialog.show();
		}
		else
		{
			EditText et  = (EditText) v1.findViewById(R.id.filename);
        	fn=et.getText().toString();
    	    String picpath=GlobalValue.picsavepath+"/"+fn+".jpg";
    	    String datpath=GlobalValue.datsavepath+"/"+fn+".dat"; 
    	    FileUtils.SaveBitmapToSD(picpath, displayview.toBitmap());
    	    displayview.saveMemo(datpath);
    	   	Intent intent=new Intent("android.appwidget.action.SENDTOUPDATE");
	   		CreateMemo.this.sendBroadcast(intent);
    	   
    	    Intent i=new Intent(Intent.ACTION_SEND);
            intent.setType("img/*");
            Uri imageuri=Uri.fromFile(FileUtils.getFile(picpath));
            i.putExtra(Intent.EXTRA_STREAM, imageuri);
            i.setType("image/*");
            startActivity(Intent.createChooser(i, "send to :"));
    	   
		}
            
    }

    public void readgesture(){
    	int count=0;
    	ArrayList<GestureBox>gesturelist=new ArrayList<GestureBox>();
    	try{
    		
    		DataInputStream in=new DataInputStream(new FileInputStream("/sdcard/sample1.dat"));
    		count=in.readInt();
    		for (int i=0;i<count;i++){
    			gesturelist.add(GestureBox.deserialize(in));
    			//change visibility of deserialize(in) to public
    			
    		}
    		
    	}
    	catch(IOException e){
    		
    	}

    	displayview.setGestureBox(gesturelist);    	
    	
    }
    public void backspace(View v)
    {
    	displayview.backspace();   	
    }
    public void setpaint(View v)
    {
    	//TODO
    	Intent i=new Intent(CreateMemo.this,Paltte.class);
    	startActivityForResult(i,REQUEST_SET_PAINT);
    	
//    	new ColorPickerDialog(this, this, mPaint).show();
    }
    private static final int SWITCH_MENU_ID = Menu.FIRST;
    private static final int BGCHANGE_MENU_ID = Menu.FIRST + 1;
    private static final int CLEAR_MENU_ID = Menu.FIRST + 2;
    private static final int SAVE_MENU_ID = Menu.FIRST + 3;
    private static final int BRUSH_MENU_ID = Menu.FIRST + 4;
    private static final int MODEL_MENU_ID = Menu.FIRST + 5;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        
        menu.add(0, SWITCH_MENU_ID, 0, "擦除").setShortcut('3', 'c');
        menu.add(0, BGCHANGE_MENU_ID, 0, "背景").setShortcut('4', 's');
        menu.add(0, CLEAR_MENU_ID, 0, "清屏").setShortcut('5', 'z');
        menu.add(0, SAVE_MENU_ID, 0, "发送").setShortcut('5', 'z');
        menu.add(0, BRUSH_MENU_ID, 0, "画笔").setShortcut('5', 'z');
        menu.add(0, MODEL_MENU_ID, 0, "模式").setShortcut('5', 'z');
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xFF);

        switch (item.getItemId()) {
            case SWITCH_MENU_ID:
            	//viewswitch();
            	displayview.Eraser();
                return true;
            case BGCHANGE_MENU_ID:
            	Intent serverIntent = new Intent(this, MemoViewer.class);
                //startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            	startActivityForResult(serverIntent,REQUEST_CHANGE_MEMOBG);
                return true;
            case CLEAR_MENU_ID:
            	displayview.Clear();
                return true;
            case SAVE_MENU_ID:
            	menuselect=true;
            	save();
                return true;
            case BRUSH_MENU_ID:
            	
//            	new ColorPickerDialog(this, this, mPaint).show();
            	Intent i=new Intent(CreateMemo.this,Paltte.class);
            	startActivityForResult(i,REQUEST_SET_PAINT);
            	
                return true;
            case MODEL_MENU_ID:
            	switch(displayview.changemodel())
            	{
            	case 1:
            		Toast.makeText(this, "切换到书写模式，清屏模式随之更改", 1000).show();
            		break;
            	case 2:
            		Toast.makeText(this, "切换到绘图模式，清屏模式随之更改", 1000).show();
            		break;
            	}
            	
            	return true;
        }
        return super.onOptionsItemSelected(item);
    }
	@Override     
    public void onBackPressed() {//重写后退键
    	
    	if(canwrite==true)
    	{
    		mgestureview.setVisibility(View.INVISIBLE);
    		hider.setVisibility(View.INVISIBLE);
        	canwrite=false;
        	return;
    	}
    	else
    	{
    		//ask to save
    		menuselect=false;
    		save();
    	}
        
    }  
    protected void onActivityResult(int requestCode, int resultCode,   
    		                                    Intent data){   
    		        switch (requestCode){
    		        
    		        case REQUEST_CHANGE_MEMOBG: 
    		        	if(resultCode==RESULT_OK){
    		           Bundle b = data.getExtras();         
    		           String path = b.getString("MEMOPATH");
    		           displayview.changebackground(path);
    		        	}
    		           break;
    		        case REQUEST_SET_PAINT:
    		        	if(resultCode==RESULT_OK){
    		        		
    		        		Bundle bundle=data.getExtras();
    		        		int color=bundle.getInt("Color");
    		        		int width=bundle.getInt("Width");
    		        		if(color!=-1){
    		            	mgestureview.setGestureColor(color);
    		                displayview.setPaint(color,width);
    		        		}
    		        		if(width!=-1)
    		            	mgestureview.setGestureStrokeWidth(width);
    		        		
    		            	mgestureview.refresh();
    		            	
    		        		
    		        		
    		        	}
    		        	break;
    		               
    		       
    		        }   
    		    }  


}
