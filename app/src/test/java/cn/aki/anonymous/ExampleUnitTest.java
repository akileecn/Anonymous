package cn.aki.anonymous;

import org.junit.Test;

import cn.aki.anonymous.utils.DataUtils;

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