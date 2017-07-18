package cn.aki.anonymous;

import org.junit.Test;

import cn.aki.anonymous.utils.C;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        System.err.println(ACUrl.getTimeLineUrl(1));

        // https://h.nimingban.com/Api/showf?id=综合1&page=2
        System.err.println(ACUrl.getPostListUrl("综合1", 1));

        System.err.println(C.Api.THREAD_LIST);
    }
}