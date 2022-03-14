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
import com.chiibeii.ZiYanZiYu.logic.model.MyDraftViewModel
import com.chiibeii.ZiYanZiYu.ui.adapter.MyDraftAdapter

class MyDraftFragment:Fragment() {

    private var adapter = MyDraftAdapter(context, emptyList())
    private lateinit var myDraftRecyclerView:RecyclerView

    private val myDraftViewModelInFragment by lazy {
        ViewModelProvider(this).get(MyDraftViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.my_draft_fragment,container,false)

        myDraftRecyclerView = view.findViewById(R.id.myDraft_RecyclerView)
        myDraftRecyclerView.layoutManager = LinearLayoutManager(context)
        myDraftRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myDraftViewModelInFragment.myDraftLiveData.observe(
            viewLifecycleOwner,{
                it?.let {
                    updateUI(it)
                }
            }
        )
    }

    // 刷新界面，实际上就是重新把itemList给adapter
    private fun updateUI(blogItemList: List<BlogItem>){
        adapter = MyDraftAdapter(context,blogItemList)
        myDraftRecyclerView.adapter = adapter
    }

    // 让activity调用获取fragment实例？
    companion object {
        fun newInstance(): MyDraftFragment {
            return MyDraftFragment()
        }
    }


}