package com.chiibeii.ZiYanZiYu.logic.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.chiibeii.ZiYanZiYu.logic.entity.BlogItem
import com.chiibeii.ZiYanZiYu.logic.repository.BlogItemRepository

class MainBlogItemListViewModel : ViewModel() {

    // 可供观察的 livedata: 全部通过 repository 访问
    val blogItemListLiveData: LiveData<List<BlogItem>> =
        BlogItemRepository.get().loadAllBlogItem()

    // 可供观察的 livedata: 全部通过 repository 访问
    fun loadThisBlogItemLiveData(blogId: Long):LiveData<BlogItem>{
        return BlogItemRepository.get().loadThisBlogItemLiveData(blogId)
    }

}