package cn.aki.anonymous.base

/**
 * Created by Administrator on 2017/7/20.
 * 请求返回
 */
open class Result<out T>(
        val success: Boolean,
        val data: T?,
        val message: String?){
    companion object {
        fun <T> success(data: T?): Result<T> {
            return Result(true, data, null)
        }

        fun <T> fail(message: String? = "A岛完了"): Result<T> {
            return Result(false, null, message)
        }
    }
}