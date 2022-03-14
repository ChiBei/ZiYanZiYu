package com.chiibeii.ZiYanZiYu.logic.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyBlogViewModel:ViewModel() {

    var user_words = ""

    fun userNameLiveData() : MutableLiveData<String> = MutableLiveData<String>()

}