package com.chiibeii.xieyidian.logic.model

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.chiibeii.xieyidian.logic.entity.BlogItem
import com.chiibeii.xieyidian.logic.repository.BlogItemRepository
import java.io.File

class EditProfileViewModel:ViewModel() {
    val userName:String = ""
    val userWords:String =""
    val userAvatar: Int = 0

    fun getPhotoFile(blogItem: BlogItem):File{
        return BlogItemRepository.get().getPhotoFile(blogItem)
    }
}