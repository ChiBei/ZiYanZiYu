package com.chiibeii.ZiYanZiYu.ui.activity

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.ui.fragment.EditAvatarFragment
import kotlinx.android.synthetic.main.edit_profile_fragment.*
import kotlinx.android.synthetic.main.edit_profile_fragment.edit_configure
import kotlinx.android.synthetic.main.edit_profile_fragment.edit_name
import kotlinx.android.synthetic.main.edit_profile_fragment.edit_words

class EditProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile_fragment)
        setSupportActionBar(editProfileTopAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 监听 sharedPreference
        val prefs = getSharedPreferences("editProfile", Context.MODE_PRIVATE)

        // 设置默认头像
        val id = prefs.getInt("user_choose_avatar", R.drawable.avatar_male_7)
        edit_avatar_in_profile_fragment.setImageResource(id)

        // 换头像 功能
        edit_avatar_in_profile_fragment.setOnClickListener {
            click_To_Change_Avatar.visibility = View.GONE
            // 弹出 bottomSheet
            val editAvatarBottomSheet = EditAvatarFragment()
            editAvatarBottomSheet.show(supportFragmentManager, "aaa")
        }

        // 提交,编辑昵称、签名 功能
        edit_configure.setOnClickListener {
            // 使用 sharedPreference 存储昵称和签名
            val editor = getSharedPreferences("editProfile", Context.MODE_PRIVATE).edit()

            // 昵称签名为空，不变-->是在编辑进来的
            if (edit_name.length() == 0 || edit_words.length() == 0) {
                Toast.makeText(this, "未修改昵称或签名", Toast.LENGTH_SHORT).show()
            } else {
                // 不为空再保存
                editor.putString("user_name", edit_name.text.toString())
                editor.putString("user_words", edit_words.text.toString())
                editor.apply()
                Toast.makeText(this, "头像、昵称、签名保存成功", Toast.LENGTH_SHORT).show()
            }
//            // 改完可以到主界面了
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        // 监听 sharedPreference
        val prefs = getSharedPreferences("editProfile", Context.MODE_PRIVATE)
        val listener = OnSharedPreferenceChangeListener { prefs, _ ->
            val id = prefs.getInt("user_choose_avatar", R.drawable.avatar_male_7)
            Log.d(TAG, "id变了:${id} ")
            edit_avatar_in_profile_fragment.setImageResource(id)
            Log.d(TAG, "onCreate: 换了！")        }
        // 注册 监听器
        prefs.registerOnSharedPreferenceChangeListener(listener)

    }

    override fun onPause() {
        super.onPause()

        Log.d(TAG, "onPause: 进来了editprofile")
        // 监听 sharedPreference
        val prefs = getSharedPreferences("editProfile", Context.MODE_PRIVATE)
        val listener = OnSharedPreferenceChangeListener { prefs, _ ->
            val id = prefs.getInt("user_choose_avatar", R.drawable.avatar_male_7)
            edit_avatar_in_profile_fragment.setImageResource(id)
        }
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }

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
