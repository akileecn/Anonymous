package cn.aki.anonymous.utils

/**
 * Created by Aki on 2017/7/14.
 * 常量
 */
object C {
    object Api{
        const val FORUM_LIST = "http://cover.acfunwiki.org/cn.json"
        // https://h.nimingban.com/Api/showf?appid=nimingban&id=4&page=1
        const val DOMAIN = "https://h.nimingban.com/"
        const val THREAD_LIST = DOMAIN + "Api/showf"

        fun createUrl(api: String, id: Int, page: Int): String{
            return "$api?id=$id&page=$page"
        }
    }
    object DB{
        const val TABLE_FORUM = "FORUM"
        const val FORUM_ID = "ID"
        const val FORUM_NAME = "NAME"
    }
    // SharedPreferences
    object SP{
        const val NAME = "anonymous"
        const val FORUM_DB_INIT = "FORUM_DB_INT" // 版块列表数据库是否初始化
    }
}