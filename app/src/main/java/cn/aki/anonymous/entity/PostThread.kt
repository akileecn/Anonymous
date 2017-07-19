package cn.aki.anonymous.entity

import cn.aki.anonymous.utils.DataUtils

/**
 * Created by Administrator on 2017/7/18.
 * 串
 */
data class PostThread(
        var id: String = "",
        var img: String = "",
        var ext: String = "",
        var now: String = "",
        var userid: String = "",
        var name: String = "",
        var email: String = "",
        var title: String = "",
        var content: String = "",
        var sage: Boolean = false,
        var admin: Boolean = false,
        var replyCount: Int = 0,
        var replys: List<Post> = listOf()
) {
    val recodeNow: String
        get() = DataUtils.recodeNow(now)
    val recodeId: String
        get() = DataUtils.recodeId(id)
}