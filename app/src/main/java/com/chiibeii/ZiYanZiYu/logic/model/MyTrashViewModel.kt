package com.chiibeii.ZiYanZiYu.logic.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.chiibeii.ZiYanZiYu.logic.entity.BlogItem
import com.chiibeii.ZiYanZiYu.logic.repository.BlogItemRepository

class MyTrashViewModel : ViewModel() {

    val myTrashLiveData: LiveData<List<BlogItem>> =
        BlogItemRepository.get().loadTrashBlogItem()

}