package com.renyu.android_image_picker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.renyu.android_image_picker.camera.CameraActivity;
import com.renyu.android_image_picker.camera.CameraActivityLow;
import com.renyu.android_image_picker.common.CommonUtil;
import com.renyu.android_image_picker.image.PhotoPickerActivity;

public class MainActivity extends Activity {
	
	ImageView choice_album=null;
	ImageView choice_camera=null;
	ImageView choice_close=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
    	init();
    }

    private void init() {
    	choice_album=(ImageView) findViewById(R.id.choice_album);
    	choice_album.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, PhotoPickerActivity.class);
				startActivity(intent);
			}});
    	
    	choice_camera=(ImageView) findViewById(R.id.choice_camera);
    	choice_camera.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.HONEYCOMB) {
		        	intent.setClass(MainActivity.this, CameraActivity.class);
		        }
		        else {
		        	intent.setClass(MainActivity.this, CameraActivityLow.class);
		        }
				startActivity(intent);
			}});
    	
    	choice_close=(ImageView) findViewById(R.id.choice_close);
    	choice_close.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
    }

    @Override
    protected void onNewIntent(Intent intent) {
    	// TODO Auto-generated method stub
    	super.onNewIntent(intent);
    	final String path=intent.getExtras().getString("cropImagePath");
    	System.out.println(path);
    	finish();
    	
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				CommonUtil.upload(path, "ae5c1df974384a8c99ef7bf0d843cbcb");
			}
		}).start();
    }
}
