package com.chiibeii.ZiYanZiYu.logic.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.chiibeii.ZiYanZiYu.logic.entity.BlogItem

@Dao
interface BlogItemDao {

    // 增，返回值是id(可无)，真的吗？
    @Insert
    fun insertBlog(blogItem: BlogItem):Long

    // 删
    @Delete
    fun deleteOneBlogItem(blogItem: BlogItem)
    // 删 多项
    @Query("delete from BlogItem where isDelete = (:isDelete)")
    fun deleteTheseBlogItem(isDelete: Boolean)

    // 改-----yes
    @Update
    fun updateBlogItem(blogItem: BlogItem)

    // 改用户名（全部）
    @Query("update blogItem set userName =:userName")
    fun updateAllUserName(userName: String)
    // 改用户头像（全部）
    @Query("update blogItem set userAvatar =:userAvatar")
    fun updateAllUserAvatar(userAvatar: Int)
    // 改用户签名
    @Query("update blogItem set userWords =:userWords")
    fun updateAllUserWords(userWords: String)

//    // 改 为喜欢/不喜欢
//    @Query("update blogItem set isLike =:isLike where blogId= (:blogId)")
//    fun getLikedBlogItem(isLike: Boolean, blogId: Long)
//    // 改 为收藏/不收藏
//    @Query("update blogItem set isStar =:isStar where blogId= (:blogId)")
//    fun getStaredBlogItem(blogId: Long,isStar: Boolean)

    // 查全部----- yes，22号，加上了isDelete条件
    // 这个排序有问题啊！！！！从草稿箱发出去的顺序依然是第一次编辑的时间做id啊--->有吗，是不是修好了？
    @Query("select * from BlogItem where isDraft = 0 and isDelete = 0  order by id DESC")
    fun loadAllBlogItem(): LiveData<List<BlogItem>>
    // 查收藏的---- yes
    @Query("select * from BlogItem where isStar = (:isStar) order by id DESC")
    fun loadStaredBlogItem(isStar: Boolean): LiveData<List<BlogItem>>

    // 查一条(LiveData)
    @Query("select * from BlogItem where id = (:id)")
    fun loadThisBlogItemLiveData(id: Long): LiveData<BlogItem>
    // 查一条
    @Query("select * from BlogItem where id = (:id)")
    fun loadThisBlogItem(id: Long): BlogItem

    // 查草稿箱-----yes
    @Query("select * from BlogItem where isDraft = 1 order by id DESC")
    fun loadDraftBlogItem(): LiveData<List<BlogItem>>

    // 查回收站
    @Query("select * from BlogItem where isDelete = 1 order by id DESC")
    fun loadTrashBlogItem(): LiveData<List<BlogItem>>


    // 查头像,不能随便查一个id，因为可能被删了
    @Query("select userAvatar from BlogItem group by userAvatar having count(userAvatar)>0")
    fun loadUserAvatar(): LiveData<Int>

    // 查昵称
    @Query("select userName from BlogItem group by userName having count(userName)>1")
    fun loadUserName(): String

    // 查总条数
    @Query("select count(*) from BlogItem")
    fun loadBlogItemCount(): Int





}