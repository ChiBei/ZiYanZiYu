package com.chiibeii.xieyidian.logic.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class BlogItem(
    var userName: String,
    var userAvatar: Int,
    var blogTime: String,
    var blogSource: String,
    var blogContent: String,
    var blogImage: Int,
    var isReblog: Boolean = false,
    var comment: Boolean = false,
    var isStar: Boolean = false,
    var isLike: Boolean = false,
    var isDelete: Boolean = false,
    var isDraft: Boolean = false
) {
    @PrimaryKey(autoGenerate = true)
    var blogId: Long = 0

    val user_avatar_name get() = "IMG_$userAvatar.jpg"
}