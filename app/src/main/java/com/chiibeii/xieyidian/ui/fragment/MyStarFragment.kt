package com.chiibeii.xieyidian.ui.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chiibeii.xieyidian.R
import com.chiibeii.xieyidian.logic.entity.BlogItem
import com.chiibeii.xieyidian.logic.model.MyStarViewModel
import com.chiibeii.xieyidian.ui.adapter.MainBlogItemListAdapter
import kotlinx.android.synthetic.main.blog_item.*
import kotlinx.android.synthetic.main.my_star_fragment.*
import kotlin.concurrent.thread

class MyStarFragment:Fragment() {

    private lateinit var myStarRecyclerView: RecyclerView
    private var adapter: MainBlogItemListAdapter?= MainBlogItemListAdapter(context,emptyList())

    // viewModel 初始化
    private val mainBlogItemListviewModelInFragment by lazy {
        ViewModelProvider(this).get(MyStarViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.my_star_fragment, container, false)

        // 主界面的博客流展示
        // layoutManager
        myStarRecyclerView = view.findViewById(R.id.myStar_RecyclerView)
        myStarRecyclerView.layoutManager = LinearLayoutManager(context)
        // adapter初始值为 空
        myStarRecyclerView.adapter = adapter

        return view
    }

    // fragment 界面数据已经准备好了，防止观察的数据没有
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 直接观察 viewmodel里面的 查询到的所有blog的那个livedata
        // viewLifecycleOwner是fragment，这个observer与这个fragment同生共死
        mainBlogItemListviewModelInFragment.myStarBlogItemListLiveData.observe(
            viewLifecycleOwner, {
                it?.let {
                    updateUI(it)
                    Log.d(TAG, it.toString())
                }
            }
        )
    }

    // 刷新界面，实际上就是重新把itemList给adapter
    private fun updateUI(blogItemList: List<BlogItem>){
        adapter = MainBlogItemListAdapter(context,blogItemList)
        myStarRecyclerView.adapter = adapter
    }

    // 让activity调用获取fragment实例？
    companion object {
        fun newInstance(): MyStarFragment {
            return MyStarFragment()
        }
    }

}