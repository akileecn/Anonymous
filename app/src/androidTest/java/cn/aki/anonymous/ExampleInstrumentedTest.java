package cn.aki.anonymous;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.Spanned;

import com.alibaba.fastjson.JSON;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import cn.aki.anonymous.entity.Post;
import cn.aki.anonymous.utils.DataUtils;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("cn.aki.anonymous", appContext.getPackageName());

        String html = "&gt;&gt;No.159181<br />⊂彡☆))∀`)";
        Spanned spanned = DataUtils.INSTANCE.fromHtml(html);
        System.err.println(spanned);

    }

    @Test
    public void parseHtml() throws IOException {
        Document doc = Jsoup.connect("https://tnmb.org/Home/Forum/ref?id=171206").get();
        Post post = new Post();
        Elements elements = doc.getAllElements();
        for (Element element : elements) {
            if (element.hasAttr("data-threads-id")) {
                post.setId(Integer.parseInt(element.attr("data-threads-id")));
            } else if (element.hasClass("h-threads-info-uid")) {
                post.setUserid(element.text().substring(3));
            } else if (element.hasClass("h-threads-info-createdat")) {
                post.setNow(element.text());
            } else if (element.hasClass("h-threads-content")) {
                post.setContent(element.text());
            }
        }
        System.err.println(JSON.toJSONString(post));
    }
}
