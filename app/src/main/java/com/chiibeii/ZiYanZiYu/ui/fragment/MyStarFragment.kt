package com.chiibeii.ZiYanZiYu.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.logic.entity.BlogItem
import com.chiibeii.ZiYanZiYu.logic.model.MyStarViewModel
import com.chiibeii.ZiYanZiYu.ui.adapter.MainBlogItemListAdapter

class MyStarFragment:Fragment() {

    private lateinit var myStarRecyclerView: RecyclerView
    private var adapter: MainBlogItemListAdapter?= MainBlogItemListAdapter()

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
            viewLifecycleOwner
        ) {
            it?.let {
                adapter!!.submitList(it as MutableList<BlogItem>)
            }
        }
    }

    // 让activity调用获取fragment实例？
    companion object {
        fun newInstance(): MyStarFragment {
            return MyStarFragment()
        }
    }

}