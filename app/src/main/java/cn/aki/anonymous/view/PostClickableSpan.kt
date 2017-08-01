package cn.aki.anonymous.view

import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AlertDialog
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import cn.aki.anonymous.R
import cn.aki.anonymous.base.C
import cn.aki.anonymous.entity.Post
import cn.aki.anonymous.utils.DataUtils
import kotlinx.android.synthetic.main.item_post.view.*
import org.jsoup.Jsoup

/**
 * Created by Administrator on 2017/7/28.
 * 串号点击文本
 */
class PostClickableSpan(val postId: String) : ClickableSpan() {
    override fun onClick(widget: View?) {
        LoadRefTask(widget!!.context, postId).execute()
    }

    override fun updateDrawState(ds: TextPaint?) {
        ds!!.color = 0xFF789922.toInt()
        ds.isUnderlineText = false
    }

    class LoadRefTask(val context: Context, val postId: String) : AsyncTask<Void, Void, Post>() {
        override fun doInBackground(vararg params: Void?): Post {
            val doc = Jsoup.connect(C.Api.createRefUrl(postId)).get()
            val post = Post()
            val elements = doc.allElements
            for (element in elements) {
                if (element.hasAttr("data-threads-id")) {
                    post.id = Integer.parseInt(element.attr("data-threads-id"))
                } else if (element.hasClass("h-threads-info-uid")) {
                    post.userid = element.text().substring(3)
                } else if (element.hasClass("h-threads-info-createdat")) {
                    post.now = element.text()
                } else if (element.hasClass("h-threads-content")) {
                    post.content = element.text()
                }
            }
            return post
        }

        override fun onPostExecute(result: Post?) {
            if (result == null) return
            val view = View.inflate(context, R.layout.item_post, null)
            view.text_user_id.text = result.recodeUserId
            view.text_id.text = result.recodeId
            view.text_now.text = result.recodeNow
            view.text_content.text = DataUtils.fromHtml(result.content)
            val dialog = AlertDialog.Builder(context).setView(view).create()
            view.setOnClickListener {
                dialog.cancel()
            }
            dialog.show()
        }
    }
}