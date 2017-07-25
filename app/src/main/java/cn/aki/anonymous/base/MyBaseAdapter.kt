package cn.aki.anonymous.base

import android.widget.BaseAdapter

/**
 * Created by Aki on 2017/7/14.
 */
abstract class MyBaseAdapter<out T>(val list:List<T>): BaseAdapter(){

    override fun getItem(position: Int): T {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

}