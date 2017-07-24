package cn.aki.anonymous.utils

import android.os.AsyncTask
import android.util.Log
import com.alibaba.fastjson.JSONException
import com.google.common.base.Throwables
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/**
 * Created by Administrator on 2017/7/20.
 * json网络请求
 */
open class JsonHttpTask(var request: Request? = null, val callback: ((Result<String>) -> Unit)? = null):AsyncTask<Any, Void, Result<String>>(){
    companion object {
        val httpClient = OkHttpClient()
        val tag = JsonHttpTask::class.java.simpleName!!
    }

    public constructor(url: String, callback: ((Result<String>) -> Unit)? = null) : this(null, callback) {
        val request = Request.Builder().url(url).build()
        this.request = request
    }

    override fun doInBackground(vararg params: Any?): Result<String> {
        val failResult = Result.fail<String>()
        try {
            val response = httpClient.newCall(request).execute()
            return if(response.isSuccessful){
                Result.success(response.body()?.string())
            }else{
                failResult
            }
        }catch (ex: IOException){
            Log.e(tag, Throwables.getStackTraceAsString(ex))
            return failResult
        }
    }

    override fun onPostExecute(result: Result<String>) {
        try{
            callback?.invoke(result)
        }catch (ex: JSONException){
//            Log.e(tag, Throwables.getStackTraceAsString(ex))
            callback?.invoke(Result.fail(DataUtils.unicode2string(result.data!!)))
        }
    }
}