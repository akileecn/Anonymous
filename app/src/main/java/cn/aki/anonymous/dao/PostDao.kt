package cn.aki.anonymous.dao

import cn.aki.anonymous.entity.PostThread
import cn.aki.anonymous.utils.C
import cn.aki.anonymous.utils.JsonHttpTask
import cn.aki.anonymous.utils.Result
import com.alibaba.fastjson.JSON
import okhttp3.Request

/**
 * Created by Administrator on 2017/7/18.
 * 串dao
 */
class PostDao {
    fun listThread(forumId: Int, page: Int, callback: (result: Result<List<PostThread>>) -> Unit) {
        val request = Request.Builder().url(C.Api.createUrl(C.Api.THREAD_LIST, forumId, page)).build()
        JsonHttpTask(request) {
            if (it.success) {
                val threads = JSON.parseArray(it.data, PostThread::class.java)
                callback(Result.success(threads))
            } else {
                callback(Result.fail(it.message))
            }
        }.execute()
    }
}