package com.android.drawmemo;


import com.drinking.utils.FileUtils;
import com.drinking.utils.GlobalValue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

public class Memo extends Activity {
    /** Called when the activity is first created. */
	private final int CREATEMEMO=0;
	private final int VIEWHISTORY=1;
	private
	 Handler handler=new Handler()
		{
			public void handleMessage(Message m)
			{
				switch(m.what)
				{
				case CREATEMEMO:
					CreateNew();
					break;
				case VIEWHISTORY:
					ViewHistory();
					break;
					
				}
			}
		};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//����û��title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,//����ȫ����ʾ 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FirstView fv=new FirstView(this,handler);
        setContentView(fv);
        if(!FileUtils.Exists(GlobalValue.mbgpath))//��һ�ε�����Դ
        {
        FileUtils.CreateFolder(GlobalValue.mbgpath);
        FileUtils.ResourceToSD(this.getResources(),GlobalValue.mbgpath);
        }
        if(!FileUtils.Exists(GlobalValue.picsavepath))//����pic�����ļ���
        {
        	FileUtils.CreateFolder(GlobalValue.picsavepath);
        }
        if(!FileUtils.Exists(GlobalValue.datsavepath))//����dat�����ļ���
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
    
}