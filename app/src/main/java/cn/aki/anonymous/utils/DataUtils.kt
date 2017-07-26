package cn.aki.anonymous.utils

import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.util.Log
import org.xml.sax.XMLReader
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Administrator on 2017/7/19.
 * 数据处理类
 */
object DataUtils {
    private val parser = SimpleDateFormat("yyyy-MM-ddHH:mm:ss", Locale.getDefault())
    private val formatThisYear = SimpleDateFormat("MM月dd", Locale.getDefault())
    private val formatYear = SimpleDateFormat("yyyy MM月dd", Locale.getDefault())
    private const val MIN = 60 * 1000L
    private const val HOUR = 60 * MIN
    private const val DAY = 24 * HOUR
    private const val WEEK = 7 * DAY
    private const val YEAR = 356 * DAY

    private val mTagHandler = object: Html.TagHandler{
        override fun handleTag(opening: Boolean, tag: String?, output: Editable?, xmlReader: XMLReader?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        private fun <T> getLast(text: Spanned, kind: Class<T>): T? {
            /*
             * This knows that the last returned object from getSpans()
             * will be the most recently added.
             */
            val objs = text.getSpans(0, text.length, kind)
            if (objs.isEmpty()) {
                return null
            } else {
                return objs[objs.size - 1]
            }
        }
    }

    init {
        val timeZone = TimeZone.getTimeZone("GMT+08:00")
        parser.timeZone = timeZone
        formatThisYear.timeZone = timeZone
        formatYear.timeZone = timeZone
    }

    /**
     * 转译post.now字段
     */
    fun recodeNow(now: String): String {
        val parseNow = now.substring(0, 10) + now.substring(13)
        val nowDate = parser.parse(parseNow)
        val diff = System.currentTimeMillis() - nowDate.time
        return when {
            diff < MIN -> "刚刚"
            diff < HOUR -> (diff / MIN).toString() + "分钟前"
            diff < DAY -> (diff / HOUR).toString() + "小时前"
            diff < DAY * 2 -> "昨天"
            diff < WEEK -> (diff / DAY).toString() + "天前"
            diff < YEAR -> formatThisYear.format(nowDate)
            else -> formatYear.format(nowDate)
        }
    }

    /**
     * 转译post.id字段
     */
    fun recodeId(id: Int): String {
        return "No.$id"
    }

    /**
     * unicode转码
     */
    fun unicode2string(unicode: String): String {
        val sb = StringBuilder()
        var i = unicode.indexOf("\\u")
        var pos = i
        while(i != -1){
            if (i + 5 < unicode.length) {
                pos = i + 6
                sb.append(Integer.parseInt(unicode.substring(i + 2, i + 6), 16).toChar())
            }
            i = unicode.indexOf("\\u", pos)
        }
        return sb.toString()
    }

    @Suppress("DEPRECATION")
    fun fromHtml(html: String): Spanned {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, null, mTagHandler)
        }else{
            return Html.fromHtml(html, null, mTagHandler)
        }
    }

}