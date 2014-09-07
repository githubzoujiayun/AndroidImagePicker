/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lidroid.xutils.bitmap;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.lidroid.xutils.bitmap.core.BitmapSize;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.lang.reflect.Field;

public class BitmapCommonUtils {

    /**
     * @param context
     * @param dirName Only the folder name, not full path.
     * @return app_cache_path/dirName
     */
    public static String getDiskCacheDir(Context context, String dirName) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null) {
                cachePath = externalCacheDir.getPath();
            }
        }
        if (cachePath == null) {
            cachePath = context.getCacheDir().getPath();
        }

        return cachePath + File.separator + dirName;
    }

    public static long getAvailableSpace(File dir) {
        try {
            final StatFs stats = new StatFs(dir.getPath());
            return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
            return -1;
        }

    }

    private static BitmapSize screenSize = null;

    public static BitmapSize getScreenSize(Context context) {
        if (screenSize == null) {
            screenSize = new BitmapSize();
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            screenSize.setWidth(displayMetrics.widthPixels);
            screenSize.setHeight(displayMetrics.heightPixels);
        }
        return screenSize;
    }

    public static BitmapSize optimizeMaxSizeByView(View view, int maxImageWidth, int maxImageHeight) {
        int width = maxImageWidth;
        int height = maxImageHeight;

        if (width > 0 && height > 0) {
            return new BitmapSize(width, height);
        }

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null) {
            if (params.width > 0) {
                width = params.width;
            } else if (params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                width = view.getWidth();
            }

            if (params.height > 0) {
                height = params.height;
            } else if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                height = view.getHeight();
            }
        }

        if (width <= 0) width = getImageViewFieldValue(view, "mMaxWidth");
        if (height <= 0) height = getImageViewFieldValue(view, "mMaxHeight");

        BitmapSize screenSize = getScreenSize(view.getContext());
        if (width <= 0) width = screenSize.getWidth();
        if (height <= 0) height = screenSize.getHeight();

        return new BitmapSize(width, height);
    }

    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        if (object instanceof ImageView) {
            try {
                Field field = ImageView.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                int fieldValue = (Integer) field.get(object);
                if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                    value = fieldValue;
                }
            } catch (Throwable e) {
            }
        }
        return value;
    }
}
