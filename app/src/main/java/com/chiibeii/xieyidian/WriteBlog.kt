package com.chiibeii.xieyidian

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.chiibeii.xieyidian.logic.MyApplication.Companion.context
import com.chiibeii.xieyidian.logic.entity.BlogItem
import com.chiibeii.xieyidian.logic.model.WriteBlogViewModel
import com.chiibeii.xieyidian.logic.repository.BlogItemRepository
import kotlinx.android.synthetic.main.activity_write_blog.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class WriteBlog : AppCompatActivity() {

    // viewModel 初始化
    private val writeBlogViewModelInFragment by lazy {
        ViewModelProvider(this).get(WriteBlogViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_blog)

        // 弹出小键盘，但是没用
        val inputManager: InputMethodManager = edit_now.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(edit_now, 0)

        setSupportActionBar(writeBlogTopAppBar)
        // 启用默认可变home按钮
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 保存按钮的点击事件: 保存到 草稿箱
        saveBlog.setOnClickListener{

            // 判断为空
            if (edit_now.text.toString()==""){
                Toast.makeText(this, "输入不能为空哦", Toast.LENGTH_SHORT).show()
            }else {
                android.app.AlertDialog.Builder(this)
                    .setMessage("保存到草稿箱并退出编辑？")
                    .setTitle("提醒！")
                    .setPositiveButton("保存并退出编辑") { _, _ ->

                        val time = SimpleDateFormat("yyyy/MM/dd HH:mm").format(Date())
                        // 输入框的内容 给 新建的 blogItem
                        val blogItemEditNow = BlogItem(
                            "ChiiBeii", R.drawable.avatar,
                            time, "from draft", "", 0,
                            false, false, false, false, false, true
                        )
                        blogItemEditNow.blogContent = edit_now.text.toString()
                        thread {
                            writeBlogViewModelInFragment.insertBlogItem(blogItemEditNow)
                        }
                        Toast.makeText(context, "草稿保存成功", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .setNegativeButton("取消") { _, _ ->
                        Toast.makeText(this, "OK,继续编辑~", Toast.LENGTH_SHORT).show()
                    }
                    .create()
                    .show()
            }
        }




    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when(item.itemId){
                android.R.id.home -> {
                    finish()
                }
                // 发送按钮的点击事件
                R.id.sendBlog ->{
                    // 点击 send 之后，这个时间才是想要留的最终的时间，保证只会生成一次
                    val time = SimpleDateFormat("yyyy/MM/dd HH:mm").format(Date())
                    val blogItemEditNow = BlogItem("ChiiBeii",R.drawable.avatar,
                        time,"from 1","",0,
                        false,false,false,false,false,false)

                    // 判断为空
                    if (edit_now.text.toString()==""){
                        Toast.makeText(this, "输入不能为空哦", Toast.LENGTH_SHORT).show()
                    }else{
                        blogItemEditNow.blogContent = edit_now.text.toString()

                        thread {
                            writeBlogViewModelInFragment.insertBlogItem(blogItemEditNow)
                        }
                        Toast.makeText(this, "成功发送一条博客", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.write_blog,menu)
        return true
    }
}
