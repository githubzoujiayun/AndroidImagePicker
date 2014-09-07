/**
 * ImageListActivity.java
 * ImageChooser
 * 
 * Created by likebamboo on 2014-4-23
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.renyu.android_image_picker.image;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.renyu.android_image_picker.R;
import com.renyu.android_image_picker.common.ParamsManager;
import com.renyu.android_image_picker.crop.FinalImageActivity;

/**
 * 某个文件夹下的所有图片列表
 * 
 * @author likebamboo
 */
public class ImageListActivity extends Activity implements OnItemClickListener {

    GridView album_choice_grid=null;
    ImageListAdapter mImageAdapter=null;
    ImageView album_choice_close=null;
    
    ArrayList<String> mImages=null;
    String title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photodir);
        
        mImages=new ArrayList<String>();
        
        title=getIntent().getStringExtra(ParamsManager.EXTRA_TITLE);
        
        init();
    }

    private void init() {
    	album_choice_grid=(GridView)findViewById(R.id.album_choice_grid);
    	album_choice_close=(ImageView) findViewById(R.id.album_choice_close);
    	album_choice_close.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
    	if (getIntent().hasExtra(ParamsManager.EXTRA_IMAGES_DATAS)) {
            mImages=getIntent().getStringArrayListExtra(ParamsManager.EXTRA_IMAGES_DATAS);
            setAdapter(mImages);
        }
    }

    private void setAdapter(ArrayList<String> datas) {
        mImageAdapter=new ImageListAdapter(this, datas, album_choice_grid);
        album_choice_grid.setAdapter(mImageAdapter);
        album_choice_grid.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        System.out.println(mImages.get(arg2));
        Intent intent=new Intent(ImageListActivity.this, FinalImageActivity.class);
		Bundle bundle=new Bundle();
		bundle.putString("path", mImages.get(arg2));
		intent.putExtras(bundle);
		startActivity(intent);
    }

}
