package com.chiibeii.xieyidian.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chiibeii.xieyidian.ui.fragment.MyBlogFragment
import com.chiibeii.xieyidian.ui.fragment.MyBlogPicturesFragment

class MyBlogTabViewPager2Adapter(fragmentActivity: FragmentActivity)
    : FragmentStateAdapter(fragmentActivity){

    private val fragmentList:List<Fragment> = listOf(MyBlogFragment(),MyBlogPicturesFragment())

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}