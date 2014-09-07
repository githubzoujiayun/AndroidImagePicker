package com.renyu.android_image_picker.image;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.renyu.android_image_picker.R;
import com.renyu.android_image_picker.common.CommonUtil;
import com.renyu.android_image_picker.common.ImageLoadTask;
import com.renyu.android_image_picker.common.OnTaskResultListener;
import com.renyu.android_image_picker.common.ParamsManager;
import com.renyu.android_image_picker.model.ImageGroup;

public class PhotoPickerActivity extends Activity implements OnItemClickListener {

    GridView album_choice_grid=null;
    ImageGroupAdapter mGroupAdapter=null;
    //图片扫描一般任务
    ImageLoadTask mLoadTask=null;
    ImageView album_choice_close=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photodir);
        init();
        loadImages();
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
    }

    /**
     * 加载图片
     */
    private void loadImages() {
        // 线程正在执行
        if (mLoadTask!=null&&mLoadTask.getStatus()==Status.RUNNING) {
            return;
        }

        mLoadTask=new ImageLoadTask(this, new OnTaskResultListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResult(boolean success, String error, Object result) {
                // 如果加载成功
                if(success&&result!=null&&result instanceof ArrayList) {
                    setImageAdapter((ArrayList<ImageGroup>)result);
                } 
            }
        });
        CommonUtil.execute(mLoadTask);
    }

    private void setImageAdapter(ArrayList<ImageGroup> data) {
        mGroupAdapter=new ImageGroupAdapter(this, data, album_choice_grid);
        album_choice_grid.setAdapter(mGroupAdapter);
        album_choice_grid.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
        ImageGroup imageGroup=mGroupAdapter.getItem(position);
        if (imageGroup==null) {
            return;
        }
        ArrayList<String> childList=imageGroup.getImages();
        Intent mIntent=new Intent(PhotoPickerActivity.this, ImageListActivity.class);
        mIntent.putExtra(ParamsManager.EXTRA_TITLE, imageGroup.getDirName());
        mIntent.putStringArrayListExtra(ParamsManager.EXTRA_IMAGES_DATAS, childList);
        startActivity(mIntent);
    }
}
