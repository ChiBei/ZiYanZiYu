package com.chiibeii.xieyidian.logic.model

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chiibeii.xieyidian.R
import com.chiibeii.xieyidian.logic.MyApplication.Companion.context
import com.chiibeii.xieyidian.logic.database.BlogItemDatabase
import com.chiibeii.xieyidian.logic.entity.BlogItem
import com.chiibeii.xieyidian.logic.repository.BlogItemRepository
import java.util.*

class MainBlogItemListViewModel:ViewModel() {

    // 可供观察的 livedata: 全部通过 repository 访问
    val blogItemListLiveData:LiveData<List<BlogItem>> =
        BlogItemRepository.get().loadAllBlogItem()

//    val userName:LiveData<String> get() =

}