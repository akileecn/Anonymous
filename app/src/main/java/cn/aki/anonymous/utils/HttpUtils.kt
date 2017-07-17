package cn.aki.anonymous.utils

import cn.aki.anonymous.entity.Forum
import com.alibaba.fastjson.JSON
import okhttp3.*
import java.io.IOException
import kotlin.reflect.KClass

/**
 * Created by Administrator on 2017/7/17.
 */
object HttpUtils{
    val client: OkHttpClient = OkHttpClient()

    fun <T : Any> get(url: String, clazz: KClass<T>): T?{
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val str = response.body().toString()

        client.newCall(request).enqueue(object :Callback{
            override fun onResponse(call: Call?, response: Response?) {
                if(response == null || !response.isSuccessful){
                    return;
                }
                val list = JSON.parseArray(response.body().toString(), Forum::class.java)
            }

            override fun onFailure(call: Call?, e: IOException?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        return JSON.parseObject(str, clazz.java)
    }

    fun get(url: String): String? {
        return get(url, String::class)
    }

}