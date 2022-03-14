package com.chiibeii.ZiYanZiYu.logic.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.sqlite.db.SimpleSQLiteQuery
import com.chiibeii.ZiYanZiYu.logic.database.BlogItemDatabase
import com.chiibeii.ZiYanZiYu.logic.entity.BlogItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

// 仓库类，ui直接与repository打交道
// repository只有一个，单例模式
class BlogItemRepository private constructor(context: Context) {

    // 初始化生成仓库repository
    // 读取 repository 数据
    private val database: BlogItemDatabase = Room.databaseBuilder(
        context.applicationContext,
        BlogItemDatabase::class.java,
        "blogItem_database"
    )
        .build()

    // 头像保存
    private val filesDir = context.applicationContext.filesDir

    // 数据库新建
    private val blogItemDao = database.blogItemDao()

    fun insertBlog(blogItem: BlogItem): Long {
        return blogItemDao.insertBlog(blogItem)
    }

    fun updateBlogContent(blogItem: BlogItem) {
        blogItemDao.updateBlogContent(blogItem)
    }

    fun getLikedBlogItem(blogItem: BlogItem) {
        blogItemDao.deleteOneBlogItem(blogItem)
    }

    fun deleteOneBlogItem(blogItem: BlogItem) {
        blogItemDao.deleteOneBlogItem(blogItem)
    }

    fun deleteTheseBlogItem(isDelete: Boolean) {
        blogItemDao.deleteTheseBlogItem(isDelete)
    }

    fun loadAllBlogItem(): LiveData<List<BlogItem>> = blogItemDao.loadAllBlogItem()

    fun loadStaredBlogItem(isStar: Boolean): LiveData<List<BlogItem>> =
        blogItemDao.loadStaredBlogItem(isStar)

    fun loadThisBlogItem(id: Long): BlogItem = blogItemDao.loadThisBlogItem(id)

    fun loadDraftBlogItem(): LiveData<List<BlogItem>> = blogItemDao.loadDraftBlogItem()

    fun getPhotoFile(blogItem: BlogItem): File = File(filesDir, "1")


    // 单例模式
    companion object {
        private var instance: BlogItemRepository? = null

        // 在应用启动后，在 my application 处调用
        fun initialize(context: Context) {
            if (instance == null) {
                instance = BlogItemRepository(context)
            }
        }

        fun get(): BlogItemRepository {
            return instance ?: throw IllegalStateException("还没有初始化repository！")
        }
    }


}