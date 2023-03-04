package com.chiibeii.ZiYanZiYu.logic.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.chiibeii.ZiYanZiYu.logic.entity.BlogItem
import com.chiibeii.ZiYanZiYu.logic.repository.BlogItemRepository
import java.io.File

class EditProfileViewModel : ViewModel() {
    val userName: String = ""
    val userWords: String = ""

    fun getPhotoFile(blogItem: BlogItem): File {
        return BlogItemRepository.get().getPhotoFile(blogItem)
    }

    // 可供观察的 livedata: 全部通过 repository 访问
    val userAvatarLiveData: LiveData<Int> =
        BlogItemRepository.get().loadUserAvatar()

}