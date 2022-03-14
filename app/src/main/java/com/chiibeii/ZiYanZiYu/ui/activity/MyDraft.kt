package com.chiibeii.ZiYanZiYu.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.ui.fragment.MyDraftFragment
import kotlinx.android.synthetic.main.activity_my_draft.*

class MyDraft:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_draft)
        setSupportActionBar(myDraftTopAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // fragment 初始化
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.frameLayout_my_draft_fragment)
        if (currentFragment == null) {
            val fragment = MyDraftFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.frameLayout_my_draft_fragment, fragment)
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