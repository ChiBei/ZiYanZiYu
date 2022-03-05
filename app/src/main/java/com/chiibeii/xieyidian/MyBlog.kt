package com.chiibeii.xieyidian

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.decodeByteArray
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.chiibeii.xieyidian.logic.MyApplication.Companion.context
import com.chiibeii.xieyidian.logic.model.MyBlogViewModel
import com.chiibeii.xieyidian.logic.model.MyDraftViewModel
import com.chiibeii.xieyidian.ui.adapter.MyBlogTabViewPager2Adapter
import com.chiibeii.xieyidian.ui.fragment.MyBlogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_my_blog.*
import kotlinx.android.synthetic.main.my_blog_profile.*
import kotlinx.android.synthetic.main.nav_header.*
import java.io.File
import java.nio.file.attribute.PosixFileAttributeView
import java.util.*
import android.os.Environment

import android.R.attr.bitmap
import kotlinx.android.synthetic.main.blog_item.*
import kotlinx.android.synthetic.main.nav_header.userAvatar


class
MyBlog : AppCompatActivity() {

    private val myBlogViewModelInFragment by lazy {
        ViewModelProvider(this).get(MyBlogViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_blog)
        setSupportActionBar(myBlogToolBarLayout)
        // 启用默认可变home按钮
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        edit_profile.setOnClickListener{
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }

        floatingButtonMyBlog.setOnClickListener{
            val intent = Intent(this, WriteBlog::class.java)
            startActivity(intent)
        }

        // viewPager2 + tabLayout
        my_blog_viewpager2.adapter = MyBlogTabViewPager2Adapter(this)
        TabLayoutMediator(my_blog_tablayout,my_blog_viewpager2){
                tabLayout: TabLayout.Tab, position:Int ->
            when(position){
                0->{
                    tabLayout.text = "博客"
                }
                1->{
                    tabLayout.text = "相册"
                }
            }
        }.attach()

        // 没用。。。。。
        val prefs = getSharedPreferences("editProfile", Context.MODE_PRIVATE)
        val userAvatarPath = prefs!!.getString("user_avatar_path", "一句很酷的签名！")
        val userAvatarPathURI = Uri.parse(userAvatarPath)
        Log.d("TAG", "路径：${userAvatarPathURI}")
        val bitmap = BitmapFactory.decodeFile(userAvatarPath)
        userAvatarInProfile.setImageBitmap(bitmap)

    }

    // 改完之后，设置好新的 昵称、签名
    override fun onStart() {
        super.onStart()

        val prefs = getSharedPreferences("editProfile", Context.MODE_PRIVATE)
        val user_words_in_prefs = prefs?.getString("user_words", "一句很酷的签名！")
        val user_name_in_prefs = prefs?.getString("user_name", "快去取个名字！")
        user_name_in_profile?.text = user_name_in_prefs
        user_words_in_profile?.text = user_words_in_prefs

        // 没用。。。。。
        val userAvatarPath = prefs!!.getString("user_avatar_path", "一句很酷的签名！")
        val userAvatarPathURI = Uri.parse(userAvatarPath)
        Log.d("TAG", "路径：${userAvatarPathURI}")
        val bitmap = BitmapFactory.decodeFile(userAvatarPath)
        userAvatarInProfile.setImageBitmap(bitmap)

//        val userAvatarPath = prefs?.getString("user_avatar_saved_by_mediastore_path", null)
//        val userAvatarBytes = File(userAvatarPath!!).readBytes()
//        val bitmap = decodeByteArray(userAvatarBytes,0,userAvatarBytes.size)
//        userAvatar.setImageBitmap(bitmap)

//         拿到 sharedPreference存的 string
//        val userAvatarInString = prefs?.getString("user_avatar_in_string", null)
//        if (userAvatarInString!=null){
//            // string 转 byteArray
//            val userAvatarInByteArray = userAvatarInString.toByteArray()
//            // byteArray 转 bitmap
//            val userAvatarInBitmap: Bitmap = decodeByteArray(userAvatarInByteArray,
//                0, userAvatarInByteArray.size)
//            // 设置 选取界面的 头像
//            userAvatar.setImageBitmap(userAvatarInBitmap)
//        }

    }

    // 有这个，不需要在布局指定menu了
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_blog, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
            R.id.search_blog ->{
                Toast.makeText(this, "搜索博客：还没有写，敬请期待~", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}