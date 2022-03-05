package com.chiibeii.xieyidian

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.chiibeii.xieyidian.logic.MyApplication.Companion.context
import com.chiibeii.xieyidian.logic.model.MyStarViewModel
import com.chiibeii.xieyidian.logic.model.WriteBlogViewModel
import com.chiibeii.xieyidian.ui.fragment.MainBlogItemListFragment
import com.chiibeii.xieyidian.ui.fragment.MyStarFragment
import kotlinx.android.synthetic.main.activity_my_star.*
import kotlinx.android.synthetic.main.blog_item.*
import kotlin.concurrent.thread

class MyStar : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_star)
        setSupportActionBar(myStarTopAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // fragment 初始化
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.frameLayout_my_star_fragment)
        if (currentFragment == null) {
            val fragment = MyStarFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.frameLayout_my_star_fragment, fragment)
                .commit()
        }
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