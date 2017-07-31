package cn.aki.anonymous.view

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import cn.aki.anonymous.utils.MessageUtils
import org.jsoup.Jsoup

/**
 * Created by Administrator on 2017/7/28.
 * 串号点击文本
 */
class PostClickableSpan(val postId: String) : ClickableSpan() {
    override fun onClick(widget: View?) {
        MessageUtils.showToast(widget!!.context, postId)
        //TODO
    }

    override fun updateDrawState(ds: TextPaint?) {
        ds!!.color = 0xFF789922.toInt()
        ds.isUnderlineText = false
    }
}