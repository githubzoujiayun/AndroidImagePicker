package com.renyu.android_image_picker.camera;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.renyu.android_image_picker.R;

public class CameraActivity extends FragmentActivity {
	
	//是否有前后摄像头
	boolean hasTwoCameras=false;
	//是否处于前置摄像头模式
	boolean isFFC=true;
	MyCameraFragment fragment=null;
	
	ImageView camera_close=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_camera);
		
		camera_close=(ImageView) findViewById(R.id.camera_close);
		camera_close.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		if(Camera.getNumberOfCameras()>1) {
			hasTwoCameras=true;
		}
		
		changeFFC();
	}
	
	public void changeFFC() {
		if(isFFC) {
			fragment=MyCameraFragment.getInstance(false);
		}
		else {
			fragment=MyCameraFragment.getInstance(true);
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
		isFFC=!isFFC;
	}
	
	public boolean hasTwoCameras() {
		return hasTwoCameras;
	}

	public void closeCamera() {
		finish();
	}
}
