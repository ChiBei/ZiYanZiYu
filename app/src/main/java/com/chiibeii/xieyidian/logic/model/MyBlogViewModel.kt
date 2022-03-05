package com.chiibeii.xieyidian.logic.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chiibeii.xieyidian.logic.entity.BlogItem
import com.chiibeii.xieyidian.logic.repository.BlogItemRepository

class MyBlogViewModel:ViewModel() {

    var user_words = ""

    fun userNameLiveData() : MutableLiveData<String> = MutableLiveData<String>()

}