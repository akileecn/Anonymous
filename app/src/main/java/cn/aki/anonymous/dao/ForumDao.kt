package cn.aki.anonymous.dao

import android.content.ContentValues
import android.content.Context
import android.util.Log
import cn.aki.anonymous.dto.ForumListDto
import cn.aki.anonymous.entity.Forum
import cn.aki.anonymous.utils.C
import com.alibaba.fastjson.JSON
import com.google.common.base.Throwables
import okhttp3.*
import java.io.IOException

/**
 * Created by Administrator on 2017/7/18.
 * 版块dao
 */
class ForumDao(context: Context) {
    private val mHttpClient = OkHttpClient()
    private val mHelper = MySQLiteOpenHelper(context)
    private val mSp = context.getSharedPreferences(C.SP.NAME, Context.MODE_PRIVATE)

    fun list(callback: (list: List<Forum>) -> Unit) {
        if (isInit()) {
            return callback(doList())
        } else {
            return init(callback)
        }
    }

    private fun doList(): List<Forum> {
        val db = mHelper.readableDatabase
        val cursor = db.rawQuery("select ${C.DB.FORUM_ID}, ${C.DB.FORUM_NAME} from ${C.DB.TABLE_FORUM}", null)
        val result = arrayListOf<Forum>()
        while (cursor.moveToNext()) {
            result.add(Forum(cursor.getInt(0), cursor.getString(1)))
        }
        cursor.close()
        db.close()
        return result
    }

    private fun init(callback: (list: List<Forum>) -> Unit) {
        if (isInit()) return
        val request = Request.Builder().url(C.Api.FORUM_LIST).build()
        mHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                if (response == null || !response.isSuccessful) {
                    Log.e("loadForum", "no response")
                    return
                }
                val listDto = JSON.parseObject(response.body()!!.string(), ForumListDto::class.java)
                if (!listDto.success || listDto.forum == null) {
                    Log.e("loadForum", "fail")
                    return
                }
                callback(listDto.forum!!)
                doInit(listDto.forum!!)
                mSp.edit().putBoolean(C.SP.FORUM_DB_INIT, true).apply()
            }

            override fun onFailure(call: Call?, e: IOException?) {
                Log.e("loadForum", Throwables.getStackTraceAsString(e))
            }
        })
    }

    private fun doInit(list: List<Forum>) {
        val db = mHelper.writableDatabase
        db.execSQL("delete from ${C.DB.TABLE_FORUM}")
        for ((id, name) in list) {
            val values = ContentValues()
            values.put(C.DB.FORUM_ID, id)
            values.put(C.DB.FORUM_NAME, name)
            db.insert(C.DB.TABLE_FORUM, null, values)
        }
    }

    private fun isInit(): Boolean {
        return mSp.getBoolean(C.SP.FORUM_DB_INIT, false)
    }

}