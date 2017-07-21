package cn.aki.library.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/2/23.
 * 文件工具类
 */
public final class FileUtils {
    /**
     * 字符串转文件
     */
    public static void string2file(String data, File file) {
        try (FileOutputStream os = new FileOutputStream(file)) {
            os.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件转字符串
     */
    public static String file2string(File file) {
        try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
            return new String(os.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
