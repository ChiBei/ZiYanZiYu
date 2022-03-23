package com.chiibeii.ZiYanZiYu.ui.adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.ui.activity.MyApplication.Companion.context
import com.chiibeii.ZiYanZiYu.logic.entity.EditAvatarItem
import com.chiibeii.ZiYanZiYu.logic.repository.BlogItemRepository
import kotlin.concurrent.thread

class EditAvatarPicturesAdapter(private val EditAvatarPicturesList:List<EditAvatarItem>)
    : RecyclerView.Adapter<EditAvatarPicturesAdapter.ViewHolder> (){

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val editAvatar:ImageView = view.findViewById(R.id.edit_avatar_pictures_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.edit_avatar_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val editAvatarInOnBindViewHolder = EditAvatarPicturesList[position]
        holder.editAvatar.setImageResource(editAvatarInOnBindViewHolder.editAvatarPictureID)

        holder.editAvatar.setOnClickListener{
            // 使用 sharedPreference 存储 选的头像,用于后续还没有换头像的时候用，或者在刚初始化的时候用
            val editor = context.getSharedPreferences("editProfile", Context.MODE_PRIVATE).edit()
            editor.putInt("user_choose_avatar", editAvatarInOnBindViewHolder.editAvatarPictureID)
            editor.apply()

            // 使用 room 存储 头像
            thread {
                BlogItemRepository.get().updateAllUserAvatar(editAvatarInOnBindViewHolder.editAvatarPictureID)
            }
            Toast.makeText(context, "头像更改成功", Toast.LENGTH_SHORT).show()

        }

    }

    override fun getItemCount(): Int =EditAvatarPicturesList.size
}