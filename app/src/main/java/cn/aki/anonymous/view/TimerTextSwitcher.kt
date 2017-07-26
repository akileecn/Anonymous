package cn.aki.anonymous.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextSwitcher
import android.widget.ViewSwitcher
import cn.aki.anonymous.R
import cn.aki.anonymous.entity.Post
import cn.aki.anonymous.utils.DataUtils

/**
 * Created by Administrator on 2017/7/26.
 * 文本定时切换界面
 */
class TimerTextSwitcher : TextSwitcher, ViewSwitcher.ViewFactory {
    private var mPosts: List<Post> = listOf()
    private var mCurrentPos = 0
    private var mHandler = Handler()
    private val mShowNextTask = Runnable {
        setText(DataUtils.fromHtml(mPosts[mCurrentPos].content))
        delayShowNext()
    }

    companion object {
        private val DELAY_MILLIS = 5_000L
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        setFactory(this)
    }

    override fun makeView(): View {
        return LayoutInflater.from(context).inflate(R.layout.item_text_replay, this, false)
    }

    /**
     * 初始化
     */
    fun initByPosts(posts: List<Post>) {
        mHandler.removeCallbacks(mShowNextTask)
        mPosts = posts
        mCurrentPos = 0
        if (mPosts.isEmpty()) {
            setCurrentText("")
            return
        }
        setCurrentText(DataUtils.fromHtml(mPosts[0].content))
        delayShowNext()
    }

    /**
     * 延迟显示下一条
     */
    private fun delayShowNext() {
        if (mPosts.size < 2) {
            return
        }
        if (mCurrentPos < mPosts.size - 1) {
            mCurrentPos++
        } else {
            mCurrentPos = 0
        }
        mHandler.postDelayed(mShowNextTask, DELAY_MILLIS)
    }

}