package com.chiibeii.ZiYanZiYu.logic.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(indices = [Index(value = ["id"],unique = true)])
data class BlogItem(
    var userName: String,
    var userAvatar: Int,
    var blogTime: String,
    var blogSource: String,
    var blogContent: String,
    var blogImage: Int,
    var isReblog: Long = 0,
    var comment: Boolean = false,
    var isStar: Boolean = false,
    var isLike: Boolean = false,
    var isDelete: Boolean = false,
    var isDraft: Boolean = false,
    // 时间戳可以拿来当 unique 键。。(Long,不是 string)
    var id: Long = Date().time
) {
    @PrimaryKey(autoGenerate = true)
    var blogId: Int = 0
}