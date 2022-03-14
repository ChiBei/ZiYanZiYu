package com.chiibeii.ZiYanZiYu.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.ui.fragment.SettingPreferenceFragment
import kotlinx.android.synthetic.main.activity_my_setting.*

class MySetting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_setting)
        setSupportActionBar(mySettingTopAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 跟其他fragment初始化其实一模一样
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout_my_setting_fragment,SettingPreferenceFragment())
            .commit()

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