package com.chiibeii.ZiYanZiYu.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.ui.fragment.BlogItemListMoreFragment
import com.chiibeii.ZiYanZiYu.ui.fragment.MainBlogItemListFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainTopAppBar)

        // 是第一次进，不存，默认是true
        val prefs = getSharedPreferences("isFirst", MODE_PRIVATE)
        val isFirst  = prefs.getBoolean("isFirst", true)
        if (isFirst){
            // 两个超链接
            val textView = View.inflate(this,R.layout.first_dialog,null)
            val tv01 = textView.findViewById<TextView>(R.id.textView01)
            val tv02 = textView.findViewById<TextView>(R.id.textView02)
            tv01.movementMethod = LinkMovementMethod.getInstance()
            tv02.movementMethod = LinkMovementMethod.getInstance()

            MaterialAlertDialogBuilder(this)
                .setTitle("温馨提示！")
                .setView(textView)
                .setCancelable(false)
                .setPositiveButton("同意并进入") { _, _ ->
                    // 不是第一次进了,修改为false
                    val sharedPreferences = getSharedPreferences("isFirst", MODE_PRIVATE).edit()
                    sharedPreferences.putBoolean("isFirst",false)
                    sharedPreferences.apply()

                    // 营造先点击，再加载fragment的效果，所以加载写在判断里面，写两遍
                    // fragment 初始化
                    val currentFragment =
                        supportFragmentManager.findFragmentById(R.id.frame_blog_item_list_fragment)
                    if (currentFragment == null) {
                        val fragment = MainBlogItemListFragment.newInstance()
                        supportFragmentManager.beginTransaction()
                            .add(R.id.frame_blog_item_list_fragment, fragment)
                            .commit()
                    }
                }
                .setNegativeButton("拒绝并退出") { _, _ ->
                    finish()
                }
                .create()
                .show()
        }else{
            // fragment 初始化
            val currentFragment =
                supportFragmentManager.findFragmentById(R.id.frame_blog_item_list_fragment)
            if (currentFragment == null) {
                val fragment = MainBlogItemListFragment.newInstance()
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.frame_blog_item_list_fragment, fragment)
                    .commit()
            }
        }

    }

    // 改完之后也要立即更新~
    override fun onStart() {
        super.onStart()
        initNavHeader()
    }

    // top_app_bar 的初始化
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    // top_app_bar 的点击事件：NavigationIcon menu上的WriteBlog
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        mainTopAppBar.setNavigationOnClickListener {
            // 在点击menu之后进行设置昵称和签名
            initNavHeader()
            mainDrawerLayout.open()
        }

        mainTopAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.write_blog -> {
                    val intent = Intent(this, WriteBlog::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // 顶部头像 签名 点击事件
        my_profile.setOnClickListener {
            val intent = Intent(this, MyBlog::class.java)
            startActivity(intent)
        }

        // nav 上的按钮
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                // 主页
                R.id.myHome -> {
                    mainDrawerLayout.close()
                }
                // 我的收藏
                R.id.myStar -> {
                    val intent = Intent(this, MyStar::class.java)
                    startActivity(intent)
                }
                // 草稿箱
                R.id.myDraft -> {
                    val intent = Intent(this, MyDraft::class.java)
                    startActivity(intent)
                }
                // 设置
                R.id.mySetting ->{
                    val intent = Intent(this, MySetting::class.java)
                    startActivity(intent)
                }
                // 回收站
                R.id.myTrash ->{
                    val intent = Intent(this, MyTrash::class.java)
                    startActivity(intent)
                }
            }
            it.isChecked = true
            // 加了反而会闪得一下关掉抽屉
//            mainDrawerLayout.close()
            true
        }
        return true
    }

//    //双次退出
//    override fun onBackPressed() {
//        super.onBackPressed()
//        Toast.makeText(this, "手滑了吗？", Toast.LENGTH_SHORT).show()
//    }

    fun initNavHeader(){
        val prefs = getSharedPreferences("editProfile", Context.MODE_PRIVATE)
        val user_words_in_prefs = prefs.getString("user_words", "一句很酷的签名！")
        val user_name_in_prefs = prefs.getString("user_name", "快去取个名！")
        val user_avatar_in_prefs = prefs.getInt("user_choose_avatar", R.drawable.avatar_male_7)
        nav_user_name?.text = user_name_in_prefs
        nav_user_words?.text = user_words_in_prefs
        nav_user_avatar?.setImageResource(user_avatar_in_prefs)
    }


}