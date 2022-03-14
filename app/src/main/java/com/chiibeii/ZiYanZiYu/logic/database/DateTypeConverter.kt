package com.chiibeii.ZiYanZiYu.logic.database

import androidx.room.TypeConverter
import java.util.*

// date类型数据存储和读取方法
class DateTypeConverter {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(a: Long?): Date? {
        return a?.let { Date(it) }
    }

    // 不用存 blogitem 类型的了
////    BlogItem(userName=ChiiBeii, userAvatar=2131165277, blogTime=Mon Feb 28 19:46:10 GMT 2022, blogSource=from 1,
////    blogContent=9999999999999999, blogImage=0, isReblog=false,
////    comment=false, isStar=true, isLike=false, isDelete=false, isSend=false)
//    @TypeConverter
//    fun fromBlogItem(blogItem: BlogItem): String {
//        return blogItem.toString()
//    }
//
//    @TypeConverter
//    fun toBlogItem(blogItem: String): BlogItem {
//
//        return BlogItem(
//            blogItem.split("[|]")[0],
//            blogItem.split("[|]")[1].toInt(),
//            Date(blogItem.split("[|]")[2].toLong()),
//            blogItem.split("[|]")[3],
//            blogItem.split("[|]")[4],
//            blogItem.split("[|]")[5].toInt(),
//            blogItem.split("[|]")[6].toBoolean(),
//            blogItem.split("[|]")[7].toBoolean(),
//            blogItem.split("[|]")[8].toBoolean(),
//            blogItem.split("[|]")[9].toBoolean(),
//            blogItem.split("[|]")[10].toBoolean(),
//            blogItem.split("[|]")[11].toBoolean(),
//        )
//    }
}