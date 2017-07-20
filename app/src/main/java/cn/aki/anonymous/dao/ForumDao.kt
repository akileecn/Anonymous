package cn.aki.anonymous.dao

import android.content.ContentValues
import android.content.Context
import cn.aki.anonymous.dto.ForumListDto
import cn.aki.anonymous.entity.Forum
import cn.aki.anonymous.utils.C
import cn.aki.anonymous.utils.JsonHttpTask
import cn.aki.anonymous.utils.Result
import com.alibaba.fastjson.JSON
import okhttp3.Request

/**
 * Created by Administrator on 2017/7/18.
 * 版块dao
 */
class ForumDao(context: Context) {
    private val mHelper = MySQLiteOpenHelper(context)
    private val mSp = context.getSharedPreferences(C.SP.NAME, Context.MODE_PRIVATE)

    fun list(callback: (result: Result<List<Forum>>) -> Unit) {
        if (isInit()) {
            callback(Result.success(doList()))
        } else {
            init(callback)
        }
    }

    /**
     * 从数据库中加载
     */
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

    /**
     * 初始化数据库数据
     */
    private fun init(callback: (result: Result<List<Forum>>) -> Unit) {
        val request = Request.Builder().url(C.Api.FORUM_LIST).build()
        JsonHttpTask(request) {
            if (it.success) {
                val listDto = JSON.parseObject(it.data, ForumListDto::class.java)
                if (!listDto.success || listDto.forum == null) {
                    callback(Result.fail())
                    return@JsonHttpTask
                }
                doInit(listDto.forum!!)
                mSp.edit().putBoolean(C.SP.FORUM_DB_INIT, true).apply()
                callback(Result.success(listDto.forum!!))
            } else {
                callback(Result.fail(it.message))
            }
        }.execute()
    }

    private fun doInit(list: List<Forum>) {
        val db = mHelper.writableDatabase
        db.execSQL("delete from ${C.DB.TABLE_FORUM}")
        for (item in list) {
            val values = ContentValues()
            values.put(C.DB.FORUM_ID, item.id)
            values.put(C.DB.FORUM_NAME, item.name)
            db.insert(C.DB.TABLE_FORUM, null, values)
        }
    }

    private fun isInit(): Boolean {
        return mSp.getBoolean(C.SP.FORUM_DB_INIT, false)
    }

}