package com.renyu.android_image_picker.common;


import android.content.Context;
import com.lidroid.xutils.BitmapUtils;

public class BitmapHelp {
	
    private BitmapHelp() {
    	
    }

    private static BitmapUtils bitmapUtils;

    /**
     * BitmapUtils不是单例的 根据需要重载多个获取实例的方法
     *
     * @param appContext application context
     * @return
     */
    public static BitmapUtils getBitmapUtils(Context appContext) {
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(appContext);
            //改方法可以解决图片加载不出来的bug
            //bitmapUtils.clearCache();
        }
        return bitmapUtils;
    }
}
