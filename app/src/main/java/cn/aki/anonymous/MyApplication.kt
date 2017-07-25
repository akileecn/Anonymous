package cn.aki.anonymous

import android.app.Application
import cn.aki.library.utils.ImageUtils

/**
 * Created by Administrator on 2017/7/17.
 * 自定义应用
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 测试
        System.setProperty("http.proxyHost", "192.168.31.131")
        System.setProperty("http.proxyPort", "8888")
        System.setProperty("https.proxyHost", "192.168.31.131")
        System.setProperty("https.proxyPort", "8888")
        // 初始化工具类
        ImageUtils.init(this)
    }

}
