package com.chiibeii.ZiYanZiYu.logic.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    @TypeConverter
    fun fromReblogTo(arrayList: ArrayList<Long>): String {
        // 使用gson，一劳永逸
        return Gson().toJson(arrayList)
    }

    @TypeConverter
    fun toReblogTo(arrayListString: String): ArrayList<Long> {

        // 并不会
        val listType = object : TypeToken<ArrayList<Long>>(){}.type
        return Gson().fromJson(arrayListString, listType)

        // 切割太麻烦了。。。还有错误
//        val arrayList = ArrayList<Int>()
//        Log.d(TAG, "toReblogTo: $arrayList")
//        val arrayListStringList = arrayListString.split(",")
//        for (k in arrayListStringList){
//            arrayList.add(k.toInt())
//        }
//        return arrayList
    }
}