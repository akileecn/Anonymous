package cn.aki.anonymous.utils

import android.content.Context
import android.widget.Toast

/**
 * Created by Administrator on 2017/7/20.
 */
object MessageUtils {
    fun showToast(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}