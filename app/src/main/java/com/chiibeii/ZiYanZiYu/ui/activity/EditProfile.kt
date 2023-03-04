package com.chiibeii.ZiYanZiYu.ui.activity

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.logic.model.EditProfileViewModel
import com.chiibeii.ZiYanZiYu.logic.model.MainBlogItemListViewModel
import com.chiibeii.ZiYanZiYu.logic.repository.BlogItemRepository
import com.chiibeii.ZiYanZiYu.ui.fragment.EditAvatarFragment
import kotlinx.android.synthetic.main.activity_my_blog.*
import kotlinx.android.synthetic.main.blog_item.*
import kotlinx.android.synthetic.main.edit_profile_fragment.*
import kotlinx.android.synthetic.main.edit_profile_fragment.edit_configure
import kotlinx.android.synthetic.main.edit_profile_fragment.edit_name
import kotlinx.android.synthetic.main.edit_profile_fragment.edit_words
import kotlinx.android.synthetic.main.my_blog_profile.*
import kotlin.concurrent.thread

class EditProfile : AppCompatActivity() {

    private val editProfileViewModelInFragment by lazy {
        ViewModelProvider(this).get(EditProfileViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile_fragment)
        setSupportActionBar(editProfileTopAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 监听 sharedPreference
        // 设置默认头像、昵称、签名
        val prefs = getSharedPreferences("editProfile", Context.MODE_PRIVATE)
        val user_avatar_in_prefs = prefs.getInt("user_choose_avatar", R.drawable.avatar_male_7)
        val user_name_in_prefs = prefs.getString("user_name", "快去取个名字！")
        val user_words_in_prefs = prefs.getString("user_words", "一句很酷的签名！")
        avatar_in_profile_fragment.setImageResource(user_avatar_in_prefs)
        edit_name.setText(user_name_in_prefs)
        edit_words.setText(user_words_in_prefs)

        // 自定义 换头像 功能
        button_user_choose_avatar.setOnClickListener {
            click_To_Change_Avatar.visibility = View.GONE
            // 选 照片
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, 1)

        }

        // 预置 换头像 功能
        button_user_choose_avatar_from_res.setOnClickListener {
            click_To_Change_Avatar.visibility = View.GONE
            // 弹出 bottomSheet
            val editAvatarBottomSheet = EditAvatarFragment()
            editAvatarBottomSheet.show(supportFragmentManager, "aaa")
        }

        // 提交,编辑昵称、签名 功能
        edit_configure.setOnClickListener {
            // 使用 sharedPreference 存储昵称和签名，用于轻量取值的地方
            val editor = getSharedPreferences("editProfile", Context.MODE_PRIVATE).edit()

            // 昵称签名为空，不变-->是在编辑进来的
            if (edit_name.length() == 0 || edit_words.length() == 0) {
                Toast.makeText(this, "昵称或签名为空", Toast.LENGTH_SHORT).show()
            } else {
                // 不为空再保存
                // 用于自动批量变化的地方，把数据库改咯
                thread {
                    BlogItemRepository.get().updateAllUserName(edit_name.text.toString())
                    BlogItemRepository.get().updateAllUserWords(edit_words.text.toString())
                }
                editor.putString("user_name", edit_name.text.toString())
                editor.putString("user_words", edit_words.text.toString())
                editor.apply()
                Toast.makeText(this, "昵称、签名保存成功", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        editProfileViewModelInFragment.userAvatarLiveData.observe(this) {
            // 数据库不为空时，空就查不了头像了
            if (it != null) {
                Log.d(TAG, "onCreate: 111111111111111111111111111111111111111111111")
                avatar_in_profile_fragment.setImageResource(it)
                Log.d(TAG, "onCreate: 222222222222222222222222222222222222222222222")
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK && intent != null) {
                    // 选取的图片的uri 保存在 intent的data 里面了
                    intent.data?.let { uri ->
                        val bitmap = contentResolver.openFileDescriptor(uri, "r")?.use {
                            BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
                        }
                        // 设置 头像
                        avatar_in_profile_fragment.setImageBitmap(bitmap)

                        // 保存一份到 picture，防止用户删掉
                        val resolver = application.contentResolver
                        val contentValues = ContentValues()
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "ZiYanZiYu_Avatar_${System.currentTimeMillis()}")
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

                        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                        if (uri != null) {
                            resolver.openOutputStream(uri).use {
                                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
                                Toast.makeText(this, "成功保存到相册，快去分享吧", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }

    // listener不好用，会被杀死。。。
//    override fun onResume() {
//        super.onResume()
//
//        // 监听 sharedPreference
//        val prefs = getSharedPreferences("editProfile", Context.MODE_PRIVATE)
//        val listener = OnSharedPreferenceChangeListener { prefs, _ ->
//            val id = prefs.getInt("user_choose_avatar", R.drawable.avatar_male_7)
//            Log.d(TAG, "id变了:${id} ")
//            edit_avatar_in_profile_fragment.setImageResource(id)
//            Log.d(TAG, "onCreate: 换了！")        }
//        // 注册 监听器
//        prefs.registerOnSharedPreferenceChangeListener(listener)
//
//    }
//
//    override fun onPause() {
//        super.onPause()
//
//        Log.d(TAG, "onPause: 进来了editprofile")
//        // 监听 sharedPreference
//        val prefs = getSharedPreferences("editProfile", Context.MODE_PRIVATE)
//        val listener = OnSharedPreferenceChangeListener { prefs, _ ->
//            val id = prefs.getInt("user_choose_avatar", R.drawable.avatar_male_7)
//            edit_avatar_in_profile_fragment.setImageResource(id)
//        }
//        prefs.unregisterOnSharedPreferenceChangeListener(listener)
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
