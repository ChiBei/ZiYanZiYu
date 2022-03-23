package com.chiibeii.ZiYanZiYu.logic.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chiibeii.ZiYanZiYu.logic.entity.BlogItem
import com.chiibeii.ZiYanZiYu.logic.repository.BlogItemRepository

class MyBlogViewModel:ViewModel() {

    var user_words = ""

    fun userNameLiveData() : MutableLiveData<String> = MutableLiveData<String>()

}