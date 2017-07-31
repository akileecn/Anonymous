package cn.aki.anonymous.entity

/**
 * Created by Administrator on 2017/7/18.
 * 串
 */
class PostThread(
        var replyCount: Int = 0
) : Post() {
    var replys: List<Post> = listOf()
        get() = field
        set(value) {
            field = value
            value.forEach {
                it.isPo = (it.userid == this@PostThread.userid)
            }
        }
}