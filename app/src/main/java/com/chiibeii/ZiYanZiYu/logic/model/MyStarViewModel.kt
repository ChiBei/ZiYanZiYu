package com.chiibeii.ZiYanZiYu.logic.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.chiibeii.ZiYanZiYu.logic.entity.BlogItem
import com.chiibeii.ZiYanZiYu.logic.repository.BlogItemRepository

class MyStarViewModel :ViewModel(){

    private val myStarBlogItemRepository = BlogItemRepository.get()

    // 可供观察的 livedata:
    val myStarBlogItemListLiveData: LiveData<List<BlogItem>> =
        myStarBlogItemRepository.loadStaredBlogItem(true)
}