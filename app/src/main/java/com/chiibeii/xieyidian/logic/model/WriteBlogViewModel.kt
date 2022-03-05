package com.chiibeii.xieyidian.logic.model

import android.widget.Toast
import androidx.lifecycle.*
import androidx.room.Query
import com.chiibeii.xieyidian.logic.MyApplication.Companion.context
import com.chiibeii.xieyidian.logic.dao.BlogItemDao
import com.chiibeii.xieyidian.logic.entity.BlogItem
import com.chiibeii.xieyidian.logic.repository.BlogItemRepository
import kotlinx.coroutines.launch
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
    var isStar: Boolean = false
    var isLike: Boolean = false
    var isDelete: Boolean = false
    var isDraft:Boolean = false

    var blogContent = MutableLiveData<String>()

    fun saveBlogContent(blogItem: BlogItem){
        BlogItemRepository.get().updateBlogContent(blogItem)
    }

    fun insertBlogItem(blogItem: BlogItem){
        BlogItemRepository.get().insertBlog(blogItem)
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