package cn.aki.anonymous.entity

import android.text.TextUtils
import android.widget.ImageView
import cn.aki.anonymous.utils.C
import cn.aki.anonymous.utils.DataUtils
import cn.aki.library.utils.ImageUtils

/**
 * Created by Administrator on 2017/7/18.
 * 子串
 */
open class Post(
        var id: Int = 0,
        var img: String = "",
        var ext: String = "",
        var now: String = "",
        var userid: String = "",
        var name: String = "",
        var email: String = "",
        var title: String = "",
        var content: String = "",
        var sage: Boolean = false,
        var admin: Boolean = false
) {
    val recodeNow: String
        get() = DataUtils.recodeNow(now)
    val recodeId: String
        get() = DataUtils.recodeId(id)

    /**
     * 绑定缩略图
     */
    fun bindThumb(view: ImageView) {
        view.setImageDrawable(null)
        if (!TextUtils.isEmpty(img) && !TextUtils.isEmpty(ext)) {
            ImageUtils.bind(C.Api.createThumbUrl(img, ext), view)
        }
    }

    val imageUrl: String?
        get() {
            return if (!TextUtils.isEmpty(img) && !TextUtils.isEmpty(ext)) {
                C.Api.createImageUrl(img, ext)
            } else {
                null
            }
        }
}
