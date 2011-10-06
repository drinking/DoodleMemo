package com.drinking.utils;

import android.util.Log;

public class Logger {

	String tag;
	public Logger(String Tag){
		tag=Tag;
	}
	public void d(String msg){
		Log.d(tag, msg);
	}
}
