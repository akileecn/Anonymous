package cn.aki.anonymous.dao

import cn.aki.anonymous.entity.PostThread
import cn.aki.anonymous.base.C
import cn.aki.anonymous.base.JsonHttpTask
import cn.aki.anonymous.base.Result
import com.alibaba.fastjson.JSON
import okhttp3.Request

/**
 * Created by Administrator on 2017/7/18.
 * 串dao
 */
class PostDao {
    fun listThread(forumId: Int, page: Int, callback: (result: Result<List<PostThread>>) -> Unit) {
        val url = if(forumId == -1) C.Api.createTimeLineUrl(page) else C.Api.createUrl(C.Api.THREAD_LIST, forumId, page)
        val request = Request.Builder().url(url).build()
        JsonHttpTask(request) {
            if (it.success) {
                val threads = JSON.parseArray(it.data, PostThread::class.java)
                callback(Result.success(threads))
            } else {
                callback(Result.fail(it.message))
            }
        }.execute()
    }

    fun listPost(threadId: Int, page: Int, callback: (result: Result<PostThread>) -> Unit){
        JsonHttpTask(C.Api.createUrl(C.Api.POST_LIST, threadId, page)) {
            if (it.success) {
                val threads = JSON.parseObject(it.data, PostThread::class.java)
                callback(Result.success(threads))
            } else {
                callback(Result.fail(it.message))
            }
        }.execute()
    }
}