package cn.aki.anonymous.utils

import android.content.Context
import cn.aki.anonymous.dao.ForumDao
import cn.aki.anonymous.dao.PostDao
import cn.aki.anonymous.entity.Forum
import cn.aki.anonymous.entity.PostThread

/**
 * Created by Administrator on 2017/7/18.
 * 数据加载客户端
 */
object DataClient {
    var mContext: Context? = null
    var mForumDao: ForumDao? = null
    var mThreadDao: PostDao? = null

    fun init(context: Context){
        mContext = context
        mForumDao = ForumDao(context)
        mThreadDao = PostDao()
    }

    /**
     * 版块列表
     */
    fun listForum(callback: (list: List<Forum>) -> Unit){
        mForumDao!!.list(callback)
    }

    /**
     * 串列表
     */
    fun listThread(forumId: Int, page: Int, callback: (list: List<PostThread>) -> Unit){
        mThreadDao!!.listThread(forumId, page, callback)
    }

}