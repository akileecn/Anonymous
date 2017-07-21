package cn.aki.library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import cn.aki.library.cache.ImageFileCache;
import cn.aki.library.cache.ImageMemoryCache;
import cn.aki.library.http.BitmapHttpTask;


/**
 * Created by Administrator on 2016/2/24.
 * 图片工具类
 */
public final class ImageUtils {
    private static final String TAG = ImageUtils.class.getName();
    private static ImageFileCache mFileCache;
    private static ImageMemoryCache mMemoryCache;
    private static transient boolean isInit = false;
    private static Bitmap defaultImage;

    private ImageUtils() {
    }

    public static void init(Context context) {
        if (isInit) {
            return;
        }
        isInit = true;
        mFileCache = new ImageFileCache(context);
        mMemoryCache = new ImageMemoryCache();
        defaultImage = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_menu_camera);
    }

    /**
     * 绑定图片
     */
    public static void bind(String url, ImageView imageView) {
        bind(url, imageView, null);
    }

    public static void bind(String url, ImageView imageView, BitmapFactory.Options options) {
        if (!isInit) {
            throw new UnsupportedOperationException("init first");
        }
        Log.d(TAG, "bind->url:" + url);
        //从内存中获得
        Bitmap bitmap = mMemoryCache.getCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            //从本地文件中获得
            bitmap = mFileCache.getCache(url, options);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                //保存内存缓存
                mMemoryCache.saveCache(url, bitmap);
            } else {
                //从网络获得
                new LoadingImageTask().execute(url, imageView);
            }
        }
    }

    /**
     * 加载图片
     */
    private static class LoadingImageTask extends AsyncTask<Object, Integer, Bitmap> {
        private ImageView imageView;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String url = (String) params[0];
            imageView = (ImageView) params[1];
            //网络缓存
            Bitmap bitmap = new BitmapHttpTask(url).execute();
            if (bitmap != null) {
                //文件缓存
                mFileCache.saveCache(url, bitmap);
                //内存缓存
                mMemoryCache.saveCache(url, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageBitmap(defaultImage);
            }
        }

    }
}
