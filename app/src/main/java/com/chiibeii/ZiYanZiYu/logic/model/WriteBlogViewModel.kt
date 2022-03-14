package com.chiibeii.ZiYanZiYu.logic.model

import androidx.lifecycle.*
import com.chiibeii.ZiYanZiYu.logic.entity.BlogItem
import com.chiibeii.ZiYanZiYu.logic.repository.BlogItemRepository
import java.util.*

class WriteBlogViewModel : ViewModel() {



    var userId: String = ""
    var userName: String = ""
    var userAvatar: Int = 0
    var blogSource: String = ""

    var blogTime: String = Date().toString()

    var blogImage: Int = 0
    var isReblog: Boolean = false
    var comment: Boolean = false
    var isStar = false
    var isLike: Boolean = false
    var isDelete = false
    var isDraft = false

    var blogContent = MutableLiveData<String>()

    fun saveBlogContent(blogItem: BlogItem){
        BlogItemRepository.get().updateBlogContent(blogItem)
    }

    fun insertBlogItem(blogItem: BlogItem):Long{
        return BlogItemRepository.get().insertBlog(blogItem)
    }

    fun updateBlogItem(blogItem: BlogItem){
        BlogItemRepository.get().updateBlogContent(blogItem)
    }

    fun loadThisBlogItem(blogId: Long):BlogItem{
        return BlogItemRepository.get().loadThisBlogItem(blogId)
    }

    fun loadDraftBlogItem(){
        BlogItemRepository.get().loadDraftBlogItem()
    }

//    // 被销毁前会被调用
//    override fun onCleared() {
//        super.onCleared()
//        Toast.makeText(context, "viewmodel 被销毁了", Toast.LENGTH_SHORT).show()
//    }
}