package cn.aki.anonymous

import android.app.Application
import cn.aki.anonymous.utils.DataClient

/**
 * Created by Administrator on 2017/7/17.
 */
class MyApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        // 测试
        System.setProperty("http.proxyHost", "192.168.31.131");
        System.setProperty("http.proxyPort", "8888");
        System.setProperty("https.proxyHost", "192.168.31.131");
        System.setProperty("https.proxyPort", "8888");
        DataClient.init(this)
    }

}
