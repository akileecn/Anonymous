package cn.aki.anonymous;

import android.content.Context;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import cn.aki.anonymous.utils.DataUtils;

import static org.junit.Assert.*;

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
}
