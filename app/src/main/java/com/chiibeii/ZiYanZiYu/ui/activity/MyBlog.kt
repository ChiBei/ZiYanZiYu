package com.chiibeii.ZiYanZiYu.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.ui.adapter.MyBlogTabViewPager2Adapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_my_blog.*
import kotlinx.android.synthetic.main.my_blog_profile.*


class MyBlog : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_blog)
        setSupportActionBar(myBlogToolBarLayout)
        // 启用默认可变home按钮
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 编辑
        edit_profile.setOnClickListener{
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }

        // 编辑
        user_avatar_in_profile.setOnClickListener{
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
    }

    // 改完之后，设置好新的 昵称、签名、头像
    override fun onStart() {
        super.onStart()

        val prefs = getSharedPreferences("editProfile", Context.MODE_PRIVATE)
        val user_words_in_prefs = prefs.getString("user_words", "一句很酷的签名！")
        val user_name_in_prefs = prefs.getString("user_name", "快去取个名字！")
        val user_avatar_in_prefs = prefs.getInt("user_choose_avatar", R.drawable.avatar_male_7)
        user_name_in_profile?.text = user_name_in_prefs
        user_words_in_profile?.text = user_words_in_prefs
        user_avatar_in_profile?.setImageResource(user_avatar_in_prefs)

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
                Toast.makeText(this, "还没有写", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}