package com.chiibeii.xieyidian.logic

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.chiibeii.xieyidian.logic.repository.BlogItemRepository
import com.google.android.material.color.DynamicColors

class MyApplication:Application() {

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        // 全局context
        context = applicationContext
        // 上来先初始化 repository，防止有查询动作，但是还没有初始化
        BlogItemRepository.initialize(this)
        // material you
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}