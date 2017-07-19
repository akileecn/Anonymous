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
        Request request = new Request.Builder().url("http://sdfdfd").build();
        Response response = new OkHttpClient().newCall(request).execute();
        System.err.println(JSON.toJSONString(response));
    }
}