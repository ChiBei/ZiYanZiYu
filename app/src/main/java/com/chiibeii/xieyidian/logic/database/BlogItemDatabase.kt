package com.chiibeii.xieyidian.logic.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chiibeii.xieyidian.logic.dao.BlogItemDao
import com.chiibeii.xieyidian.logic.entity.BlogItem


@Database(version = 1, entities = [BlogItem::class], exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class BlogItemDatabase : RoomDatabase() {

    // 将 database 关联 dao
    abstract fun blogItemDao(): BlogItemDao

}