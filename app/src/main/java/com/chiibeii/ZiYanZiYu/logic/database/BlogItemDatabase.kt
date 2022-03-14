package com.chiibeii.ZiYanZiYu.logic.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chiibeii.ZiYanZiYu.logic.dao.BlogItemDao
import com.chiibeii.ZiYanZiYu.logic.entity.BlogItem


@Database(version = 1, entities = [BlogItem::class], exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class BlogItemDatabase : RoomDatabase() {

    // 将 database 关联 dao
    abstract fun blogItemDao(): BlogItemDao

}