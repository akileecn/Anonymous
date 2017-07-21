package cn.aki.library.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2016/2/25.
 * http请求
 */
public abstract class HttpTask<T> {
    private String url;
    private static final OkHttpClient defaultClient;

    static {
        defaultClient = new OkHttpClient.Builder()
                .connectTimeout(5_000, TimeUnit.MILLISECONDS)
                .readTimeout(5_000, TimeUnit.MILLISECONDS)
                .build();
    }

    HttpTask(String url) {
        this.url = url;
    }

    /**
     * http请求
     */
    public T execute() {
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = defaultClient.newCall(request).execute();
            if(response.isSuccessful()){
                return doHttp(response.body());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected abstract T doHttp(ResponseBody body) throws IOException;
}
