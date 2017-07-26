package cn.aki.anonymous.utils

import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.util.Log
import org.xml.sax.XMLReader
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


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
    private const val PATTERN_POST_ID = "&gt;&gt;(No\\.)?\\d+" // 串ID正则
    private const val PATTERN_BR = "<br ?/?>" // 回车正则
    const val FLAG_HANDLE_BR = 0x0001
    const val FLAG_HANDLE_POST_ID = 0x0001.shl(1)

    private val mTagHandler = object: Html.TagHandler{
        override fun handleTag(opening: Boolean, tag: String?, output: Editable?, xmlReader: XMLReader?) {
            //TODO
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

    fun fromHtml(html: String): Spanned{
        return fromHtml(html, FLAG_HANDLE_POST_ID)
    }

    @Suppress("DEPRECATION")
    fun fromHtml(html: String, flag: Int): Spanned {
        var handledHtml = if(flag and FLAG_HANDLE_BR != 0) handleBr(html) else html
        handledHtml = if(flag and FLAG_HANDLE_POST_ID != 0) handlePostId(handledHtml) else handledHtml
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return Html.fromHtml(handledHtml, Html.FROM_HTML_MODE_LEGACY, null, mTagHandler)
        }else{
            return Html.fromHtml(handledHtml, null, mTagHandler)
        }
    }

    /**
     * 处理串号
     */
    private fun handlePostId(html: String): String{
        val matcher = Pattern.compile(PATTERN_POST_ID).matcher(html)
        val sb = StringBuilder()
        var end = 0
        while (matcher.find()) {
            sb.append(html.substring(end, matcher.start())).append("<font color='#789922'>").append(matcher.group()).append("</font>")
            end = matcher.end()
        }
        sb.append(html.substring(end))
        return sb.toString()
    }

    /**
     * 处理换行
     */
    private fun handleBr(html: String): String{
        return Pattern.compile(PATTERN_BR).matcher(html).replaceAll(" ")
    }

}