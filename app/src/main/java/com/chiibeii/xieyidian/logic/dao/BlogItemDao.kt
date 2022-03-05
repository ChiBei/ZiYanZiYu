package com.chiibeii.xieyidian.logic.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.chiibeii.xieyidian.logic.entity.BlogItem

@Dao
interface BlogItemDao {

    // 增，返回值是id(可无)
    @Insert
    fun insertBlog(blogItem: BlogItem)

    // 删
    @Delete
    fun deleteOneBlogItem(blogItem: BlogItem)
    // 删 多项
    @Query("delete from BlogItem where isDelete = (:isDelete)")
    fun deleteTheseBlogItem(isDelete: Boolean)

    // 改-----yes
    @Update
    fun updateBlogContent(blogItem: BlogItem)

//    // 改 为喜欢/不喜欢
//    @Query("update blogItem set isLike =:isLike where blogId= (:blogId)")
//    fun getLikedBlogItem(isLike: Boolean, blogId: Long)
//    // 改 为收藏/不收藏
//    @Query("update blogItem set isStar =:isStar where blogId= (:blogId)")
//    fun getStaredBlogItem(blogId: Long,isStar: Boolean)

    // 查全部----- yes
    @Query("select * from BlogItem where isDraft = 0 order by blogId DESC")
    fun loadAllBlogItem(): LiveData<List<BlogItem>>
    // 查收藏的---- yes
    @Query("select * from BlogItem where isStar = (:isStar) order by blogId DESC")
    fun loadStaredBlogItem(isStar: Boolean): LiveData<List<BlogItem>>
//    // 查一条
//    @Query("select * from BlogItem where isStar = (:isStar)")
//    fun loadThisBlogItem(blogItem: BlogItem): LiveData<BlogItem>

    // 查草稿箱
    @Query("select * from BlogItem where isDraft = 1 order by blogId DESC")
    fun loadDraftBlogItem(): LiveData<List<BlogItem>>

}