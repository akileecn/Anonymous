package cn.aki.anonymous.utils

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.Log
import cn.aki.anonymous.R
import cn.aki.anonymous.view.PostClickableSpan
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


/**
 * Created by Administrator on 2017/7/19.
 * 数据处理类
 */
object DataUtils {
    private var mContext: Context? = null
    private val parser = SimpleDateFormat("yyyy-MM-ddHH:mm:ss", Locale.getDefault())
    private val formatThisYear = SimpleDateFormat("MM月dd", Locale.getDefault())
    private val formatYear = SimpleDateFormat("yyyy MM月dd", Locale.getDefault())
    private const val MIN = 60 * 1000L
    private const val HOUR = 60 * MIN
    private const val DAY = 24 * HOUR
    private const val WEEK = 7 * DAY
    private const val YEAR = 356 * DAY
    private val PATTERN_POST_ID = Pattern.compile(">>(No\\.)?\\d+") // 串ID正则
    private val PATTERN_ID = Pattern.compile("\\d+")
    private val PATTERN_BR = Pattern.compile("<br ?/?>") // 回车正则
    const val FLAG_HANDLE_BR = 0x0001
    const val FLAG_HANDLE_POST_ID = 0x0001.shl(1)

    init {
        val timeZone = TimeZone.getTimeZone("GMT+08:00")
        parser.timeZone = timeZone
        formatThisYear.timeZone = timeZone
        formatYear.timeZone = timeZone
    }

    fun init(context: Context){
        mContext = context
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
     * unicode转码
     */
    fun unicode2string(unicode: String): String {
        val sb = StringBuilder()
        var i = unicode.indexOf("\\u")
        var pos = i
        while (i != -1) {
            if (i + 5 < unicode.length) {
                pos = i + 6
                sb.append(Integer.parseInt(unicode.substring(i + 2, i + 6), 16).toChar())
            }
            i = unicode.indexOf("\\u", pos)
        }
        return sb.toString()
    }

    fun fromHtml(html: String): Spanned {
        return fromHtml(html, FLAG_HANDLE_POST_ID)
    }

    @Suppress("DEPRECATION")
    fun fromHtml(html: String, flag: Int): Spanned {
        val handledHtml = if (flag and FLAG_HANDLE_BR != 0) handleBr(html) else html
        var result: Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(handledHtml, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(handledHtml)
        }
        if (flag and FLAG_HANDLE_POST_ID != 0) {
            result = handlePostId(result)
        }
        Log.e("html", result.toString())
        return result
    }

    /**
     * 处理串号
     */
    private fun handlePostId(source: CharSequence): Spanned {
        val matcher = PATTERN_POST_ID.matcher(source)
        val spannable = source as? Spannable ?: SpannableString(source)
        while (matcher.find()) {
            val postIdWord = matcher.group()
            val matcher2 = PATTERN_ID.matcher(postIdWord)
            if (matcher2.find()) {
                val postId = matcher2.group()
                spannable.setSpan(PostClickableSpan(postId), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        return spannable
    }

    /**
     * 处理换行
     */
    private fun handleBr(html: String): String {
        return PATTERN_BR.matcher(html).replaceAll(" ")
    }

    fun recodeAdminUserId(source: CharSequence): CharSequence{
        val spannable = source as? Spannable ?: SpannableString(source)
        spannable.setSpan(ForegroundColorSpan(Color.RED), 0, source.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }

    fun recodePoUserId(source: CharSequence): CharSequence{
        val spannable = source as? Spannable ?: SpannableString("(po)")
        spannable.setSpan(ForegroundColorSpan(mContext!!.getColor(R.color.forgive)), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val builder = SpannableStringBuilder(source)
        builder.append(spannable)
        return builder
    }
}