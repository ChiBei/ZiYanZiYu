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
import com.chiibeii.ZiYanZiYu.logic.model.MyTrashViewModel
import com.chiibeii.ZiYanZiYu.ui.adapter.MyTrashAdapter

class MyTrashFragment:Fragment() {

    private var adapter = MyTrashAdapter(context, emptyList())
    private lateinit var myTrashRecyclerView:RecyclerView

    private val myTrashViewModelInFragment by lazy {
        ViewModelProvider(this).get(MyTrashViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.my_trash_fragment,container,false)

        myTrashRecyclerView = view.findViewById(R.id.myTrash_RecyclerView)
        myTrashRecyclerView.layoutManager = LinearLayoutManager(context)
        myTrashRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myTrashViewModelInFragment.myTrashLiveData.observe(
            viewLifecycleOwner,{
                it?.let {
                    updateUI(it)
                }
            }
        )
    }

    // 刷新界面，实际上就是重新把itemList给adapter
    private fun updateUI(blogItemList: List<BlogItem>){
        adapter = MyTrashAdapter(context,blogItemList)
        myTrashRecyclerView.adapter = adapter
    }

    // 让activity调用获取fragment实例？
    companion object {
        fun newInstance(): MyTrashFragment {
            return MyTrashFragment()
        }
    }


}