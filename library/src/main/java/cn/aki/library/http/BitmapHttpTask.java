package cn.aki.library.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2016/2/25.
 * 图片下载
 */
public class BitmapHttpTask extends HttpTask<Bitmap> {
    public BitmapHttpTask(String url) {
        super(url);
    }

    @Override
    protected Bitmap doHttp(ResponseBody body) throws IOException {
        return BitmapFactory.decodeStream(body.byteStream());
    }
}
