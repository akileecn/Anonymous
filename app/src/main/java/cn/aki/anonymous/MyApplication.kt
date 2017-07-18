package cn.aki.anonymous

import android.app.Application
import cn.aki.anonymous.utils.DataClient

/**
 * Created by Administrator on 2017/7/17.
 */
class MyApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        DataClient.init(this)
    }

}
