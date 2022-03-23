package com.chiibeii.ZiYanZiYu.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.logic.repository.BlogItemRepository
import com.chiibeii.ZiYanZiYu.ui.fragment.MyTrashFragment
import kotlinx.android.synthetic.main.activity_my_trash.*

class MyTrash: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trash)
        setSupportActionBar(myTrashTopAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // fragment 初始化
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.frameLayout_my_trash_fragment)
        if (currentFragment == null) {
            val fragment = MyTrashFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.frameLayout_my_trash_fragment, fragment)
                .commit()
        }
    }

    // app_bar 的初始化
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_trash_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            android.R.id.home -> {
                finish()
                return true
            }

            R.id.deleteAll ->{
                Toast.makeText(this, "还没写", Toast.LENGTH_SHORT).show()
//                val trash =  BlogItemRepository.get().loadThisBlogItem()
//                for (k in list) {
//                    val son = BlogItemRepository.get().loadThisBlogItem(k)
//                    son.isReblogFromDeleted = false
//                    BlogItemRepository.get().updateBlogItem(son)
//                }
            }

            R.id.restoreAll ->{
                Toast.makeText(this, "还没写", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}