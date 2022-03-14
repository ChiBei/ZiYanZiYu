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
import com.chiibeii.ZiYanZiYu.logic.model.MainBlogItemListViewModel
import com.chiibeii.ZiYanZiYu.ui.adapter.MainBlogItemListAdapter

class MyBlogFragment:Fragment() {

    private lateinit var mainBlogItemListRecyclerView: RecyclerView
    private var adapter: MainBlogItemListAdapter?= MainBlogItemListAdapter()

    // viewModel 初始化
    private val mainBlogItemListviewModelInFragment by lazy {
        ViewModelProvider(this).get(MainBlogItemListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.my_blog_fragment, container, false)

        // 主界面的博客流展示
        mainBlogItemListRecyclerView = view.findViewById(R.id.myBlog_RecyclerView)
        mainBlogItemListRecyclerView.layoutManager = LinearLayoutManager(context)
        // adapter初始值为 空
        mainBlogItemListRecyclerView.adapter = adapter

        return view
    }

    // onViewCreated时，fragment 界面数据已经准备好了，防止观察的数据没有
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 记长度
        val a = arrayListOf<Int>()
        a.add(0)

        // 直接观察 viewmodel里面的 查询到的所有blog的那个livedata
        // viewLifecycleOwner是fragment，这个observer与这个fragment同生共死
        mainBlogItemListviewModelInFragment.blogItemListLiveData.observe(
            viewLifecycleOwner, {
                it?.let {
                    a.add(it.size)
                    // 如果是添加元素，滚到最上面,否则原地删除
                    if (a[a.size - 1] > a[a.size - 2]) {
                        // 提交新列表
                        adapter!!.submitList(it as MutableList<BlogItem>) {
                            mainBlogItemListRecyclerView.scrollToPosition(0)
                        }
                    } else {
                        adapter!!.submitList(it as MutableList<BlogItem>)
                    }                }
            }
        )
    }

    // ?,让activity调用获取fragment实例？
    companion object {
        fun newInstance(): MainBlogItemListFragment {
            return MainBlogItemListFragment()
        }
    }


}