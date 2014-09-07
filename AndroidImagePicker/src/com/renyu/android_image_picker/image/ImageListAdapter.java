/**
 * ImageGroupAdapter.java
 * ImageChooser
 * 
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.renyu.android_image_picker.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.core.BitmapSize;
import com.renyu.android_image_picker.R;
import com.renyu.android_image_picker.common.BitmapHelp;
import com.renyu.android_image_picker.myview.MyImageView;

import java.util.ArrayList;

/**
 * 某个图片组中图片列表适配器
 * 
 * @author likebamboo
 */
public class ImageListAdapter extends BaseAdapter {
	
	BitmapUtils bitmapUtils=null;;
	BitmapDisplayConfig config=null;
	
    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 图片列表
     */
    private ArrayList<String> mDataList = new ArrayList<String>();

    public ImageListAdapter(Context context, ArrayList<String> list, View container) {
        mDataList = list;
        mContext = context;
        
        bitmapUtils=BitmapHelp.getBitmapUtils(context);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setBitmapMaxSize(new BitmapSize(300, 300));
		config.setAutoRotation(true);
		config.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.pic_thumb));
		config.setLoadingDrawable(context.getResources().getDrawable(R.drawable.pic_thumb));
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public String getItem(int position) {
        if (position < 0 || position > mDataList.size()) {
            return null;
        }
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.image_list_item, null);
            holder.mImageIv = (MyImageView)view.findViewById(R.id.list_item_iv);
            holder.mClickArea = view.findViewById(R.id.list_item_cb_click_area);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        final String path = getItem(position);
        // 加载图片
        holder.mImageIv.setTag(path);
        // 加载图片
        bitmapUtils.display(holder.mImageIv, path, config, null);

        return view;
    }

    static class ViewHolder {
        public MyImageView mImageIv;

        public View mClickArea;
    }

}
