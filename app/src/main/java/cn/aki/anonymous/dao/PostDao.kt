package cn.aki.anonymous.dao

import android.util.Log
import cn.aki.anonymous.entity.PostThread
import cn.aki.anonymous.utils.C
import com.alibaba.fastjson.JSON
import com.google.common.base.Throwables
import okhttp3.*
import java.io.IOException

/**
 * Created by Administrator on 2017/7/18.
 * 串dao
 */
class PostDao {
    private val mHttpClient = OkHttpClient()

    fun listThread(forumId: Int, page: Int, callback: (list: List<PostThread>) -> Unit){
        val request = Request.Builder().url(C.Api.createUrl(C.Api.THREAD_LIST, forumId, page)).build()
        mHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                if (response == null || !response.isSuccessful) {
                    Log.e("listThread", "no response")
                    return
                }
                val result = response.body()!!.string()
                val threads = JSON.parseArray(result, PostThread::class.java)
                callback(threads.toList())
            }

            override fun onFailure(call: Call?, e: IOException?) {
                Log.e("listThread", Throwables.getStackTraceAsString(e))
            }
        })
    }
}