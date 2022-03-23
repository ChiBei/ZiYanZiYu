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

class EditAvatarFragment : BottomSheetDialogFragment() {

    private lateinit var editAvatarPicturesRecyclerview: RecyclerView
    private lateinit var buttonMale: Button
    private lateinit var buttonFemale: Button

    private val editAvatarItemList = ArrayList<EditAvatarItem>()
    private val editAvatarItemList_male = ArrayList<EditAvatarItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initEditAvatarList()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.edit_avatar_bottom_sheet_fragment, container, false)

        editAvatarPicturesRecyclerview = view.findViewById(R.id.edit_avatar_pictures_recyclerview)
        val layoutManager = GridLayoutManager(context,4)
        editAvatarPicturesRecyclerview.layoutManager = layoutManager
        val adapter_male_default = EditAvatarPicturesAdapter(editAvatarItemList_male)
        editAvatarPicturesRecyclerview.adapter = adapter_male_default

        buttonMale = view.findViewById(R.id.button_male)
        buttonFemale = view.findViewById(R.id.button_female)

        buttonMale.setOnClickListener{
            editAvatarPicturesRecyclerview = view.findViewById(R.id.edit_avatar_pictures_recyclerview)
            val layoutManager = GridLayoutManager(context,4)
            editAvatarPicturesRecyclerview.layoutManager = layoutManager

            val adapter_female = EditAvatarPicturesAdapter(editAvatarItemList_male)
            editAvatarPicturesRecyclerview.adapter = adapter_female
        }

        buttonFemale.setOnClickListener{
            editAvatarPicturesRecyclerview = view.findViewById(R.id.edit_avatar_pictures_recyclerview)
            val layoutManager = GridLayoutManager(context,4)
            editAvatarPicturesRecyclerview.layoutManager = layoutManager

            val adapter_female = EditAvatarPicturesAdapter(editAvatarItemList)
            editAvatarPicturesRecyclerview.adapter = adapter_female
        }

        return view
    }

    private fun initEditAvatarList() {
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_1))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_2))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_3))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_4))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_5))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_6))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_7))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_8))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_9))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_10))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_11))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_12))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_13))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_14))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_15))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_16))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_17))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_18))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_19))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_20))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_21))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_22))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_23))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_24))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_25))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_26))
        editAvatarItemList.add(EditAvatarItem(R.drawable.avatar_female_27))

        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_1))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_2))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_3))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_4))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_5))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_6))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_7))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_8))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_9))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_10))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_11))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_12))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_13))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_14))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_15))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_16))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_17))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_18))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_19))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_20))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_21))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_22))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_23))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_24))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_25))
        editAvatarItemList_male.add(EditAvatarItem(R.drawable.avatar_male_26))


    }





}