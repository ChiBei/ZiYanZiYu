package com.chiibeii.xieyidian.logic.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.chiibeii.xieyidian.logic.MyApplication.Companion.context
import com.chiibeii.xieyidian.logic.entity.BlogItem
import com.chiibeii.xieyidian.logic.repository.BlogItemRepository

class MyStarViewModel :ViewModel(){

    private val myStarBlogItemRepository = BlogItemRepository.get()

    // 可供观察的 livedata:
    val myStarBlogItemListLiveData: LiveData<List<BlogItem>> =
        myStarBlogItemRepository.loadStaredBlogItem(true)
}