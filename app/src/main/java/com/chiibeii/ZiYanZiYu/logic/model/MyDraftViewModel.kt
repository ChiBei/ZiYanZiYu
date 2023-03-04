package com.chiibeii.ZiYanZiYu.logic.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.chiibeii.ZiYanZiYu.logic.entity.BlogItem
import com.chiibeii.ZiYanZiYu.logic.repository.BlogItemRepository

class MyDraftViewModel : ViewModel() {

    val myDraftLiveData: LiveData<List<BlogItem>> =
        BlogItemRepository.get().loadDraftBlogItem()

}