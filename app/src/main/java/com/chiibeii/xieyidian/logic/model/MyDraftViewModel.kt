package com.chiibeii.xieyidian.logic.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.chiibeii.xieyidian.logic.entity.BlogItem
import com.chiibeii.xieyidian.logic.repository.BlogItemRepository

class MyDraftViewModel:ViewModel() {

    val myDraftLiveData: LiveData<List<BlogItem>> =
        BlogItemRepository.get().loadDraftBlogItem()

}