package cn.aki.anonymous.utils

/**
 * Created by Aki on 2017/7/14.
 * 常量
 */
object C {
    object Api{
        const val FORUM_LIST = "http://cover.acfunwiki.org/cn.json" // 版块列表
        const val NOTICE = "http://cover.acfunwiki.org/nmb-notice.json" // 通知
        // https://h.nimingban.com/Api/showf?appid=nimingban&id=4&page=1
//        const val DOMAIN = "https://h.nimingban.com"
        private const val DOMAIN = "http://h.adnmb.com"
        const val THREAD_LIST = DOMAIN + "/Api/showf" // 串列表
        const val POST_LIST = DOMAIN + "/Api/thread"  // 回复列表
        private const val IMAGE_DOMAIN = "http://h.adnmb.com/Public/Upload" // 图片服务器域名
        const val IMAGE_BASE = IMAGE_DOMAIN + "/image" // 图片基本路径
        const val THUMB_BASE = IMAGE_DOMAIN + "/thumb" // 缩略图基本路径

        fun createUrl(api: String, id: Int, page: Int): String{
            return "$api?id=$id&page=$page"
        }

        fun createImageUrl(img: String, ext: String): String{
            return "$IMAGE_BASE/$img$ext"
        }

        fun createThumbUrl(img: String, ext: String): String{
            return "$THUMB_BASE/$img$ext"
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
        const val NOTICE_TIMESTAMP = "NOTICE_TIMESTAMP" // 公告时间戳
    }
    // intent extra key
    object Extra{
        // 图片连接
        const val IMAGE_URL = "IMAGE_URL"
        // 串ID
        const val THREAD_ID = "THREAD_ID"
    }
}