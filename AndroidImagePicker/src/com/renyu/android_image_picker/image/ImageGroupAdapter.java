/**
 * ImageGroupAdapter.java
 * ImageChooser
 * 
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.renyu.android_image_picker.image;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.core.BitmapSize;
import com.renyu.android_image_picker.R;
import com.renyu.android_image_picker.common.BitmapHelp;
import com.renyu.android_image_picker.model.ImageGroup;
import com.renyu.android_image_picker.myview.MyImageView;

/**
 * 分组图片适配器
 * 
 * @author likebamboo
 */
public class ImageGroupAdapter extends BaseAdapter {
	
	BitmapUtils bitmapUtils=null;;
	BitmapDisplayConfig config=null;
	
    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 图片列表
     */
    private List<ImageGroup> mDataList = null;

    public ImageGroupAdapter(Context context, List<ImageGroup> list, View container) {
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
    public ImageGroup getItem(int position) {
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
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.image_group_item, null);
            holder.mImageIv = (MyImageView)view.findViewById(R.id.group_item_image_iv);
            holder.mTitleTv = (TextView)view.findViewById(R.id.group_item_title_tv);
            holder.mCountTv = (TextView)view.findViewById(R.id.group_item_count_tv);

            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        ImageGroup item = getItem(position);
        if (item != null) {
            // 图片路径
            String path = item.getFirstImgPath();
            // 标题
            holder.mTitleTv.setText(item.getDirName());
            // 计数
            holder.mCountTv.setText(mContext.getString(R.string.image_count, item.getImageCount()));
            holder.mImageIv.setTag(path);
            // 加载图片
    		bitmapUtils.display(holder.mImageIv, path, config, null);	
        }
        return view;
    }

    static class ViewHolder {
        public MyImageView mImageIv;

        public TextView mTitleTv;

        public TextView mCountTv;
    }

}
