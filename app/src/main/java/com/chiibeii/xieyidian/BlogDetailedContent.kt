package com.chiibeii.xieyidian

import android.app.ActivityOptions
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.widget.PopupMenu
import android.widget.Toast
import com.chiibeii.xieyidian.R
import com.chiibeii.xieyidian.logic.MyApplication.Companion.context
import kotlinx.android.synthetic.main.activity_blog_detailed_content.*
import kotlinx.android.synthetic.main.blog_item.*

class BlogDetailedContent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_detailed_content)

        setSupportActionBar(blogDetailedContentTopAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imageButtonSet.visibility = GONE
        isReadFullContent.visibility = GONE

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.blog_detailed_content,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}