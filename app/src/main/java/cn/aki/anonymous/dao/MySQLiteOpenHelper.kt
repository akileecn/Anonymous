package cn.aki.anonymous.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cn.aki.anonymous.utils.C

/**
 * Created by Administrator on 2017/7/18.
 * 本地数据库帮助类
 */
class MySQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, "anonymous.db", null, 1){

    override fun onCreate(db: SQLiteDatabase?) {
        // 版块
        db!!.execSQL("create table ${C.DB.TABLE_FORUM} (" +
                "_id integer primary key autoincrement" +
                ", ${C.DB.FORUM_ID} integer" +
                ", ${C.DB.FORUM_NAME} varchar(100))")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}