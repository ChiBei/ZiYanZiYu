package com.chiibeii.ZiYanZiYu.ui.fragment

import android.content.ContentValues
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.logic.entity.BlogItem
import com.chiibeii.ZiYanZiYu.logic.model.MainBlogItemListViewModel
import com.chiibeii.ZiYanZiYu.ui.adapter.MainBlogItemListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.edit_profile_fragment.*
import kotlin.concurrent.thread


class MainBlogItemListFragment : Fragment() {

    private val mainBlogItemListViewModelInFragment by lazy {
        ViewModelProvider(this).get(MainBlogItemListViewModel::class.java)
    }
    private val myAdapter = MainBlogItemListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.blog_item_list_fragment, container, false)

        // 主界面的博客流展示
        val mainBlogItemListRecyclerView: RecyclerView =
            view.findViewById(R.id.main_BlogItemList_RecyclerView)

        mainBlogItemListRecyclerView.layoutManager = StaggeredGridLayoutManager(1,1)
        mainBlogItemListRecyclerView.adapter = myAdapter

//        // 下拉刷新
//        val swipeRefreshInMain : SwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_in_main)
//        swipeRefreshInMain.setColorSchemeResources(R.color.background_basic_darker_2)
//        swipeRefreshInMain.setOnRefreshListener {
//            // 写法不行啊。。但是还是有点用
//            myAdapter.notifyDataSetChanged()
//            swipeRefreshInMain.isRefreshing = false
//        }

        return view
    }

    // onViewCreated时，fragment 界面数据已经准备好了，防止观察的数据没有
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 记长度
        val a = arrayListOf<Int>()
        a.add(0)

        // 主界面的博客流展示
        val mainBlogItemListRecyclerView: RecyclerView =
            view.findViewById(R.id.main_BlogItemList_RecyclerView)

        // 从个人信息界面改东西还是有延迟，希望可以改进，here！
        // 直接观察 viewmodel里面的 查询到的所有blog的那个livedata
        // 监测的本来就是整个列表里面的每一个item，哪个变了，listAdapter就更新哪个
        // viewLifecycleOwner 是 fragment，这个observer 与 这个fragment 同生共死
        mainBlogItemListViewModelInFragment.blogItemListLiveData.observe(
            viewLifecycleOwner, {
                it?.let {
                    a.add(it.size)
                    // 如果是添加元素，滚到最上面,否则原地删除
                    if (a[a.size - 1] > a[a.size - 2]) {
                        // 提交新列表
                        myAdapter.submitList(it as MutableList<BlogItem>) {
                            mainBlogItemListRecyclerView.scrollToPosition(0)
                        }
                    } else {
                        myAdapter.submitList(it as MutableList<BlogItem>)
                    }
                }
            }
        )

        // 所以这里在放屁
//        for ( k in myAdapter.currentList ) {
//            mainBlogItemListViewModelInFragment.loadThisBlogItemLiveData(k.id).observe(viewLifecycleOwner,{
//                Toast.makeText(context, "item 变了", Toast.LENGTH_SHORT).show()
//                myAdapter.submitList(myAdapter.currentList)
//            })
//        }





    }

    // 让activity调用获取fragment实例？
    companion object {
        fun newInstance(): MainBlogItemListFragment {
            return MainBlogItemListFragment()
        }
    }

}