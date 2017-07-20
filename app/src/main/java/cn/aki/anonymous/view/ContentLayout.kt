package cn.aki.anonymous.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import cn.aki.anonymous.R
import kotlinx.android.synthetic.main.layout_error.view.*

/**
 * Created by Administrator on 2017/7/20.
 * 带错误页面的layout
 */
class ContentLayout(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    val errorView:View = inflate(context, R.layout.layout_error, null)

    fun showError(message: String, callback: () -> Unit){
        errorView.error_text.text = message
        errorView.visibility = View.VISIBLE
        if(indexOfChild(errorView) == -1)addView(errorView)
        error_layout.setOnClickListener{
            hideError()
            callback()
        }
    }

    fun hideError(){
        errorView.visibility = View.GONE
    }
}