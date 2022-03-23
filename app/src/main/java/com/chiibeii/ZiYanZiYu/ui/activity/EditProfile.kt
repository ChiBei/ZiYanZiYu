package com.chiibeii.ZiYanZiYu.ui.activity

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
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
        edit_avatar_in_profile_fragment.setImageResource(user_avatar_in_prefs)
        edit_name.setText(user_name_in_prefs)
        edit_words.setText(user_words_in_prefs)

        // 换头像 功能
        edit_avatar_in_profile_fragment.setOnClickListener {
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


        editProfileViewModelInFragment.userAvatarLiveData.observe(this, {
            // 数据库不为空时，空就查不了头像了
            if (it!=null){
                Log.d(TAG, "onCreate: 111111111111111111111111111111111111111111111")
                edit_avatar_in_profile_fragment.setImageResource(it)
                Log.d(TAG, "onCreate: 222222222222222222222222222222222222222222222")
            }
            }
        )
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
