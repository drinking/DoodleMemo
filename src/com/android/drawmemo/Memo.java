package com.android.drawmemo;


import com.drinking.utils.FileUtils;
import com.drinking.utils.GlobalValue;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Memo extends Activity implements OnClickListener{

	Button newMemo;
	Button oldMemo;
	Button help;
	Button quit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置没有title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,//设置全屏显示 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);        
        
        newMemo=(Button)findViewById(R.id.new_memo_button);
        oldMemo=(Button)findViewById(R.id.old_memo_button);
        help=(Button)findViewById(R.id.help_button);
        quit=(Button)findViewById(R.id.quit_button);
        newMemo.setOnClickListener(this);
        oldMemo.setOnClickListener(this);
        help.setOnClickListener(this);
        quit.setOnClickListener(this);
        
        if(!FileUtils.Exists(GlobalValue.mbgpath))//第一次导入资源
        {
        FileUtils.CreateFolder(GlobalValue.mbgpath);
        FileUtils.ResourceToSD(this.getResources(),GlobalValue.mbgpath);
        }
        if(!FileUtils.Exists(GlobalValue.picsavepath))//创建pic储存文件夹
        {
        	FileUtils.CreateFolder(GlobalValue.picsavepath);
        }
        if(!FileUtils.Exists(GlobalValue.datsavepath))//创建dat储存文件夹
        {
        	FileUtils.CreateFolder(GlobalValue.datsavepath);
        }
        
    }
    public void CreateNew()
    {

    	Intent intent=new Intent(this,CreateMemo.class);
    	intent.putExtra("path", "nofile");
    	startActivity(intent);
    	
    }
    public void ViewHistory()
    {
    	Intent intent=new Intent(this,MemoHistory.class);
    	startActivity(intent);
    }
	@Override
	public void onClick(View view) {
		
		switch(view.getId()) {
		
		case R.id.new_memo_button:
			CreateNew();
			break;
		case R.id.old_memo_button:
			ViewHistory();
			break;
		case R.id.help_button:
			new AlertDialog.Builder(Memo.this)
            .setTitle("Doodle Memo")
            .setMessage(R.string.helpdoc)
            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					
					
				}
	    		
	    	}).show();
			break;
		case R.id.quit_button:
			Memo.this.finish();
			break;
		}
		
	}
    
}