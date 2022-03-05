package com.chiibeii.xieyidian.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chiibeii.xieyidian.R
import com.chiibeii.xieyidian.logic.entity.BlogItem
import com.chiibeii.xieyidian.logic.model.MainBlogItemListViewModel
import com.chiibeii.xieyidian.ui.adapter.MainBlogItemListAdapter
import kotlinx.android.synthetic.main.my_blog_profile.*

class MainBlogItemListFragment:Fragment() {

    private lateinit var mainBlogItemListRecyclerView:RecyclerView
    private var adapter: MainBlogItemListAdapter?= MainBlogItemListAdapter(context,emptyList())

    // viewModel 初始化
    private val mainBlogItemListViewModelInFragment by lazy {
        ViewModelProvider(this).get(MainBlogItemListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.blog_item_list_fragment, container, false)

        // 主界面的博客流展示
        // layoutManager
        mainBlogItemListRecyclerView = view.findViewById(R.id.main_BlogItemList_RecyclerView)
        mainBlogItemListRecyclerView.layoutManager = LinearLayoutManager(context)
        // adapter初始值为 空
        mainBlogItemListRecyclerView.adapter = adapter

        return view
    }

    // fragment 界面数据已经准备好了，防止观察的数据没有
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 直接观察 viewmodel里面的 查询到的所有blog的那个livedata
        // viewLifecycleOwner是fragment，这个observer与这个fragment同生共死
        mainBlogItemListViewModelInFragment.blogItemListLiveData.observe(
            viewLifecycleOwner, {
                it?.let {
                    updateUI(it)
                }
            }
        )

//        mainBlogItemListViewModelInFragment.userName.observe(
//            viewLifecycleOwner, {
//                it?.let {
//                    val prefs = context?.getSharedPreferences("editProfile", Context.MODE_PRIVATE)
//                    val user_words_in_prefs = prefs?.getString("user_words", "一句很酷的签名！")
//                    val user_name_in_prefs = prefs?.getString("user_name", "快去取个名字！")
//                    user_name_in_profile?.text = user_name_in_prefs
//                    user_words_in_profile?.text = user_words_in_prefs
//                }
//            }
//        )
    }

    // 刷新界面，实际上就是重新把itemList给adapter
    private fun updateUI(blogItemList: List<BlogItem>){
        adapter = MainBlogItemListAdapter(context,blogItemList)
        mainBlogItemListRecyclerView.adapter = adapter
    }

    // ?,让activity调用获取fragment实例？
    companion object {
        fun newInstance(): MainBlogItemListFragment {
            return MainBlogItemListFragment()
        }
    }

}