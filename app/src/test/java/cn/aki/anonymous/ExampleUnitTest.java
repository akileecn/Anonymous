package cn.aki.anonymous;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.junit.Test;

import java.util.Date;

import cn.aki.anonymous.utils.C;
import cn.aki.anonymous.utils.DataUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        System.err.println(DataUtils.INSTANCE.unicode2string("\\u8be5\\u677f\\u5757\\u4e0d\\u5b58\\u5728"));
    }
}