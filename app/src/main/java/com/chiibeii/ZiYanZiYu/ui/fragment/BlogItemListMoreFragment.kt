package com.chiibeii.ZiYanZiYu.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.logic.entity.EditAvatarItem
import com.chiibeii.ZiYanZiYu.ui.adapter.EditAvatarPicturesAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.ArrayList

class BlogItemListMoreFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.blog_item_list_more_bottom_sheet_fragment, container, false)

        return view
    }

}