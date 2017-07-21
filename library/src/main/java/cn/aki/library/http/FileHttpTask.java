package cn.aki.library.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2016/2/25.
 * http文件下载任务
 */
public class FileHttpTask extends HttpTask<Void> {
    private File mFile;

    public FileHttpTask(String url, File file) {
        super(url);
        mFile = file;
    }

    @Override
    protected Void doHttp(ResponseBody body) throws IOException {
        try (BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(mFile))) {
            BufferedInputStream is = new BufferedInputStream(body.byteStream());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        }
        return null;
    }
}
