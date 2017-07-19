package cn.aki.anonymous.utils

import android.util.Log
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

    init {
        val timeZone = TimeZone.getTimeZone("GMT+08:00")
        parser.timeZone = timeZone
        formatThisYear.timeZone = timeZone
        formatYear.timeZone = timeZone
    }

    fun recodeNow(now: String): String {
        val parseNow = now.substring(0, 10) + now.substring(13)
        val nowDate = parser.parse(parseNow)
        val diff = System.currentTimeMillis() - nowDate.time
        return when {
            diff < MIN -> {
                Log.e("time", "$parseNow ${System.currentTimeMillis()} ${nowDate.time} ");
                "刚刚"
            }
            diff < HOUR -> (diff / MIN).toString() + "分钟前"
            diff < DAY -> (diff / HOUR).toString() + "小时前"
            diff < DAY * 2 -> "昨天"
            diff < WEEK -> (diff / DAY).toString() + "天前"
            diff < YEAR -> formatThisYear.format(nowDate)
            else -> formatYear.format(nowDate)
        }
    }

    fun recodeId(id: String): String {
        return "No.$id"
    }
}