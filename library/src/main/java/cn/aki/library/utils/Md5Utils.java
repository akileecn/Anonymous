package cn.aki.library.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2015/11/19.
 * md5加密工具类
 */
public final class Md5Utils {
    /**
     * MD5加密
     */
    public static String encode(String code) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes;
            bytes = digest.digest(code.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                int i = b & 0xff;
                String hex = Integer.toHexString(i);
                if (hex.length() < 2) {
                    hex = "0" + hex;
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 对文件进行MD5加密
     */
    public static String encode(File file) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            // 读取文件加密
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buff = new byte[1024];
            int size;
            while ((size = bis.read(buff)) != -1) {
                digest.update(buff, 0, size);
            }
            byte[] result = digest.digest();
            // 解析为字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                int i = b & 0xff;
                String hex = Integer.toHexString(i);
                if (hex.length() < 2) {
                    hex = "0" + hex;
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
