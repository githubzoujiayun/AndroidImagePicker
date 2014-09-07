package com.renyu.android_image_picker.crop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.renyu.android_image_picker.MainActivity;
import com.renyu.android_image_picker.R;
import com.renyu.android_image_picker.common.FileUtil;
import com.renyu.android_image_picker.myview.CropImageView3;

public class FinalImageActivity extends Activity {
	
	String imagePath="";
	
	CropImageView3 finalimage_cropImg=null;
	Button finalimage_ok=null;
	ImageView crop_close=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finalimage);
		
		imagePath=getIntent().getExtras().getString("path");
		
		init();
	}
	
	private void init() {
		finalimage_cropImg=(CropImageView3) findViewById(R.id.finalimage_cropImg);
		finalimage_cropImg.setDrawable(new BitmapDrawable(getResources(), BitmapFactory.decodeFile(imagePath)),100,100);

		finalimage_ok=(Button) findViewById(R.id.finalimage_ok);
		finalimage_ok.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(new Runnable(){

					@Override
					public void run() {
						try {
							FileUtil.writeImage(finalimage_cropImg.getCropImage(), FileUtil.SDCARD_PAHT+"/crop.png", 100);
							Intent mIntent=new Intent(FinalImageActivity.this, MainActivity.class);
							Bundle bundle=new Bundle();
							bundle.putString("cropImagePath", FileUtil.SDCARD_PAHT+"/crop.png");
							mIntent.putExtras(bundle);
							mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(mIntent);
						} catch(Exception e) {
							Toast.makeText(FinalImageActivity.this, "图片规格不正确", 3000).show();
						}
					}
				}).start();
			}});
		
		crop_close=(ImageView) findViewById(R.id.crop_close);
		crop_close.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
	}
}
