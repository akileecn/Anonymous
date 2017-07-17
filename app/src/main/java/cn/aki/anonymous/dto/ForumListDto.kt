package cn.aki.anonymous.dto

import cn.aki.anonymous.entity.Forum

/**
 * Created by Administrator on 2017/7/17.
 */
data class ForumListDto(var success: Boolean = false, var forum: List<Forum>? = null)