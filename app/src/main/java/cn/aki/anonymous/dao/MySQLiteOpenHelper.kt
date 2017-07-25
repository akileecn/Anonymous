package cn.aki.anonymous.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Administrator on 2017/7/18.
 * 本地数据库帮助类
 */
class MySQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, "anonymous.db", null, 1){

    override fun onCreate(db: SQLiteDatabase?) {
        // 版块
        db!!.execSQL("create table forum (" +
                "pk integer primary key autoincrement" +
                ", id integer" +
                ", name varchar(100)" +
                ", order_by integer" +
                ", disabled boolean)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}