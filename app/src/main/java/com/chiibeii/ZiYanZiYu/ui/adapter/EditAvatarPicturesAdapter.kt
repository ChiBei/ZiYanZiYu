package com.chiibeii.ZiYanZiYu.ui.adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.ui.activity.MyApplication.Companion.context
import com.chiibeii.ZiYanZiYu.logic.entity.EditAvatarItem

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
            // 使用 sharedPreference 存储 选的头像
            val editor = context.getSharedPreferences("editProfile", Context.MODE_PRIVATE).edit()
            editor.putInt("user_choose_avatar", editAvatarInOnBindViewHolder.editAvatarPictureID)
            Log.d(TAG, "onBindViewHolder:${editAvatarInOnBindViewHolder.editAvatarPictureID} ")
            editor.apply()
        }

    }

    override fun getItemCount(): Int =EditAvatarPicturesList.size
}