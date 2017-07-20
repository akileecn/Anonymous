package cn.aki.anonymous.entity

/**
 * Created by Administrator on 2017/7/18.
 * 串
 */
class PostThread(
        var replyCount: Int = 0,
        var replys: List<Post> = listOf()
) : Post()