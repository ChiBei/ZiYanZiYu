package com.chiibeii.xieyidian

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64.*
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_my_blog.*
import kotlinx.android.synthetic.main.activity_write_blog.*
import kotlinx.android.synthetic.main.blog_item.*
import kotlinx.android.synthetic.main.edit_profile_fragment.*
import kotlinx.android.synthetic.main.my_blog_profile.*
import kotlinx.android.synthetic.main.nav_header.*
import java.io.ByteArrayOutputStream
import java.security.PublicKey
import java.util.*

class EditProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile_fragment)
        setSupportActionBar(editProfileTopAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 换头像 功能
        edit_avatar.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        // 编辑昵称、签名 功能
        edit_configure.setOnClickListener {

            if(edit_name.length()==0||edit_words.length()==0){
                Toast.makeText(this, "昵称或签名不能为空哦~", Toast.LENGTH_SHORT).show()
            }else{
                // 使用 sharedPreference 存储昵称和签名
                val editor = getSharedPreferences("editProfile", Context.MODE_PRIVATE).edit()
                editor.putString("user_name", edit_name.text.toString())
                editor.putString("user_words", edit_words.text.toString())
                editor.apply()

                android.app.AlertDialog.Builder(this)
                    .setMessage("保存并退出？")
                    .setTitle("提醒！")
                    .setPositiveButton("保存并退出") { _, _ ->
                        finish()

                    }
                    .setNegativeButton("还没写好") { _, _ ->
                        Toast.makeText(this, "OK,继续写~", Toast.LENGTH_SHORT).show()
                    }
                    .create()
                    .show()
            }

        }
    }

    // startActivityForResult 来这里处理结果
    override fun onActivityResult(requestCode: Int, resultCode: Int, dataInIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, dataInIntent)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK && dataInIntent != null) {
                    dataInIntent.data?.let { uri ->
                        val bitmap = contentResolver.openFileDescriptor(uri, "r")?.use {
                            BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
                        }
                        // 设置 选取界面的 头像，转瞬即逝
                        edit_avatar.setImageBitmap(bitmap)

                        // 保存到 pictures/$title 目录了,保存此路径到 sharedPreference
                        val userAvatarPath = MediaStore.Images.Media.insertImage(contentResolver, bitmap!!,
                            "user_avatar", "file")
                        val editor = getSharedPreferences("editProfile", Context.MODE_PRIVATE).edit()
                        editor.putString("user_avatar_path", userAvatarPath)
                        editor.apply()

//                        val userAvatarPath = MediaStore.Images.Media.insertImage(contentResolver,bitmap,
//                            "user_avatar_saved_by_mediastore","file")
//
//                        val editor = getSharedPreferences("editProfile", Context.MODE_PRIVATE).edit()
//                        editor.putString("user_avatar_saved_by_mediastore_path", userAvatarPath)
//                        editor.apply()


//                        // bitmap 保存成 string
//                        // 保存输出流，压缩
//                        val byteArrayOutputStream = ByteArrayOutputStream()
//                        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//                        // 利用base64，转换为 byteArray
//                        val byteArray= byteArrayOutputStream.toByteArray()
//                        // 转为 String
//                        val userAvatarInString:String = Base64.getEncoder().encodeToString(byteArray)
//                        // 把 string 存到 sharedPreference
//                        val editor = getSharedPreferences("editProfile", Context.MODE_PRIVATE).edit()
//                        editor.putString("user_avatar_in_string", userAvatarInString)
//                        editor.apply()

                    }
                    Toast.makeText(this, "设置头像成功", Toast.LENGTH_SHORT).show()
                }
                Toast.makeText(this, "未选取图片", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
