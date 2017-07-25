package cn.aki.anonymous.dao

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import cn.aki.anonymous.base.C
import cn.aki.anonymous.base.Result
import cn.aki.anonymous.entity.Forum
import com.alibaba.fastjson.JSON

/**
 * Created by Administrator on 2017/7/18.
 * 版块dao
 */
class ForumDao(val context: Context) {
    private val mHelper = MySQLiteOpenHelper(context)
    private val mSp: SharedPreferences by lazy { context.getSharedPreferences(C.SP.NAME, Context.MODE_PRIVATE) }

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
        val cursor = db.rawQuery("select pk, id, name from forum where disabled = 0 order by order_by", null)
        val result = arrayListOf<Forum>()
        while (cursor.moveToNext()) {
            result.add(Forum(cursor.getInt(0), cursor.getInt(1), cursor.getString(2)))
        }
        cursor.close()
        db.close()
        return result
    }

    /**
     * 初始化数据库数据
     */
    private fun init(callback: (result: Result<List<Forum>>) -> Unit) {
//        JsonHttpTask(C.Api.FORUM_LIST) {
//            if (it.success) {
//                val listDto = JSON.parseObject(it.data, ForumListDto::class.java)
//                if (!listDto.success || listDto.forum == null) {
//                    callback(Result.fail())
//                    return@JsonHttpTask
//                }
//                doInit(listDto.forum!!)
//                mSp.edit().putBoolean(C.SP.FORUM_DB_INIT, true).apply()
//                callback(Result.success(listDto.forum!!))
//            } else {
//                callback(Result.fail(it.message))
//            }
//        }.execute()
        // 加载备胎岛
        val list = JSON.parseArray(C.Api.backUpJson, Forum::class.java)
        doInit(list)
        mSp.edit().putBoolean(C.SP.FORUM_DB_INIT, true).apply()
        callback(Result.success(list))
    }

    private fun doInit(list: List<Forum>) {
        val db = mHelper.writableDatabase
        db.beginTransaction()
        try{
            db.execSQL("delete from forum")
            var index: Int = 0
            list.forEach {
                val values = ContentValues()
                values.put("id", it.id)
                values.put("name", it.name)
                values.put("order_by", index)
                values.put("disabled", false)
                db.insert("forum", null, values)
                index++
            }
            db.setTransactionSuccessful()
        }finally {
            db.endTransaction()
        }
    }

    private fun isInit(): Boolean {
        return mSp.getBoolean(C.SP.FORUM_DB_INIT, false)
    }

    fun update(updateList: Collection<Forum>, disabledList: Collection<Forum>) {
        val db = mHelper.writableDatabase
        db.beginTransaction()
        try {
            var index: Int = 0
            updateList.forEach {
                val values = ContentValues()
                values.put("id", it.id)
                values.put("name", it.name)
                values.put("order_by", index)
                db.update("forum", values, "pk=?", arrayOf(it.pk.toString()))
                index++
            }
            disabledList.forEach {
                val values = ContentValues()
                values.put("disabled", true)
                db.update("forum", values, "pk=?", arrayOf(it.pk.toString()))
            }
            db.setTransactionSuccessful()
        }finally {
            db.endTransaction()
        }
    }
}