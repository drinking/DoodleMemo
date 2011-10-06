package com.android.drawmemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class Help extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view=new View(Help.this);
		view.setBackgroundResource(R.drawable.helpdoc);
		this.setContentView(view);
	}
	
}
