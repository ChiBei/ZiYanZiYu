package com.chiibeii.ZiYanZiYu.logic.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList

@Entity(indices = [Index(value = ["id"],unique = true)])
data class BlogItem(
    // 时间戳可以拿来当 unique 键。。(Long,不是 string)
    var id: Long = Date().time,
    var userName: String,
    var userAvatar: Int,
    var userWords: String,
    var blogTime: String,
    var blogSource: String,
    var blogContent: String,
    var blogImage: Int,
    // 只能从一个转发来
    var reblogFrom: Long = 0,
    var isReblogFromDeleted:Boolean = false,
    // 可以以此为基础转发多条,存到集合里面
    var reblogTo: ArrayList<Long>,
    var comment: Boolean = false,
    var isStar: Boolean = false,
    var isLike: Boolean = false,
    var isDelete: Boolean = false,
    var isDraft: Boolean = false

    ) {
        @PrimaryKey(autoGenerate = true)
        var blogId: Int = 0
    }