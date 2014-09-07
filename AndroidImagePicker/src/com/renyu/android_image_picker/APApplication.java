package com.renyu.android_image_picker;

import android.app.Application;

public class APApplication extends Application {

	public static APApplication application=null;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		application=this;
	}
	
	public static APApplication getAPApplication() {
		return application;
	}
}
