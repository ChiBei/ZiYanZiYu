package com.chiibeii.ZiYanZiYu.ui.activity

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.ui.activity.MyApplication.Companion.context
import com.chiibeii.ZiYanZiYu.logic.entity.BlogItem
import com.chiibeii.ZiYanZiYu.logic.model.WriteBlogViewModel
import com.chiibeii.ZiYanZiYu.logic.repository.BlogItemRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_write_blog.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class WriteBlog : AppCompatActivity() {

    // viewModel 初始化
    private val writeBlogViewModelInFragment by lazy {
        ViewModelProvider(this).get(WriteBlogViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_blog)
        setSupportActionBar(writeBlogTopAppBar)
        // 启用默认可变home按钮
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 弹出小键盘,有用，要在activity里面设置拉伸！(做imageButton顶起来效果才发现的)
        val inputManager: InputMethodManager = edit_now.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(edit_now, 0)

        // 取回传过来的数值
        val theBlogItem = intent.getStringExtra("TheBlogItem")
        val fromWhere = intent.getStringExtra("FromWhere")
        // 要使用正则来切割
        val theBlogItemSplited = theBlogItem?.split("=", ",",")")
        Log.d(TAG, "onCreate: $theBlogItemSplited")
//        这是切割后的例子
//        [BlogItem(id, 1647058486309,
//        userName, ChiiBeii,
//        userAvatar, 2131165316,
//        userWords, 1111,
//        blogTime, 2022/03/09 11:26,
//        blogSource,from 1,
//        blogContent,电饭锅哈哈哈,
//        blogImage,0,
//        reblogFrom, 0, isReblogDeleted,false, reblogTo,[],
//        comment, false,
//        isStar, false,  isLike, false,
//        isDelete, false,  isDraft, false )]

        val prefs = context.getSharedPreferences("editProfile", Context.MODE_PRIVATE)
        val user_name_in_prefs = prefs?.getString("user_name", "快去取个名字！")
        userName_reblog_inWriteBlog.text = user_name_in_prefs

        // 界面显示根据来源不一样，是不一样的
        // 草稿箱里的reblog和普通的reblog其实不一样，两者转发部分的内容设置是不一样的，所以不能合并处理
        // From Edit 还没写！
        when (fromWhere) {
            // 草稿来的，必能携带信息
            "FromDraft" -> {
                // 初始化草稿箱内容到输入框
                edit_now.setText(theBlogItemSplited!![13])

                // 判断草稿箱中的草稿，是不是有 reblog别人（转发部分 卡片整体的可见性）
                if (theBlogItemSplited[17].toLong() != 0L){
                    blogItem_reblog_inWriteBlog.visibility = View.VISIBLE

                    // 转发来的，必能携带信息，但是要分线程处理：查询数据库和设置值
                    // 在子线程更新 UI，使用message、handler、messageQueue、looper
                    val updateReblogItem = 1
                    val handler = object : Handler(Looper.getMainLooper()){
                        override fun handleMessage(msg: Message){
                            when (msg.what) {
                                updateReblogItem -> {
                                    // 指针，而且要避免在子线程直接更改UI！！！
                                    // 此时，已经到了主线程了，但是数据库不能在主线程操作,所以通过 msg 在子线程处理好传过来
                                    blogTime_reblog_inWriteBlog.text = (msg.obj as ArrayList<String>)[0]
                                    blogContent_reblog_inWriteBlog.text = (msg.obj as ArrayList<String>)[1]
                                }
                            }
                        }
                    }
                    thread {
                        val msg = Message()
                        // 获取转发的内容，存到集合里面
                        val blogTimeReblogText = BlogItemRepository.get().loadThisBlogItem(theBlogItemSplited[17].toLong()).blogTime
                        val blogContentReblogText = BlogItemRepository.get().loadThisBlogItem(theBlogItemSplited[17].toLong()).blogContent
                        val blogReblogMessage = ArrayList<String>()
                        blogReblogMessage.add(blogTimeReblogText)
                        blogReblogMessage.add(blogContentReblogText)
                        // 在线程之间传递这个集合
                        msg.obj = blogReblogMessage
                        msg.what = updateReblogItem
                        handler.sendMessage(msg)
                    }

                }else{
                    blogItem_reblog_inWriteBlog.visibility = View.GONE
                }

                // 嵌套转发，，，不知道效果怎么样
                blogImage_reblog_inWriteBlog.visibility = if (theBlogItemSplited[15].toInt() != 0)
                    View.VISIBLE else View.GONE

            }
            // 来自转发按钮
            "FromReblog" -> {
                // 肯定 reblog别人啊（转发部分 卡片整体的可见性）
                blogItem_reblog_inWriteBlog.visibility = View.VISIBLE

                // 转发来的，必能携带信息
                blogTime_reblog_inWriteBlog.text = theBlogItemSplited!![9]
                blogContent_reblog_inWriteBlog.text = theBlogItemSplited[13]

                // 嵌套转发，，，不知道效果怎么样
                blogImage_reblog_inWriteBlog.visibility = if (theBlogItemSplited[15].toInt() != 0)
                    View.VISIBLE else View.GONE

            }
            // 来自编辑按钮，什么都没带，没有额外操作
            else
            -> {

            }
        }

        // 保存按钮的点击事件: 保存到 草稿箱
        saveBlog.setOnClickListener {
            // 判断为空
            if (edit_now.text.toString() == "") {
                Toast.makeText(this, "输入不能为空哦", Toast.LENGTH_SHORT).show()
            } else {
                MaterialAlertDialogBuilder(this)
                    .setMessage("保存到草稿箱并退出编辑？")
                    .setTitle("提醒！")
                    .setPositiveButton("保存并退出编辑") { _, _ ->
                        // SAVE 主逻辑 实现位置【2022.3.12已经写完】
                        val time = SimpleDateFormat("yyyy/MM/dd HH:mm").format(Date())

                        // 取回传过来的数值
                        val theBlogItem = intent.getStringExtra("TheBlogItem")
                        val fromWhere = intent.getStringExtra("FromWhere")
                        // 要使用正则来切割
                        val theBlogItemSplited = theBlogItem?.split("=", ",",")")

                        // 输入框的内容 给 新建的 blogItem/或者草稿箱来的blogItem
                        when (fromWhere) {
                            "FromDraft" -> {
                                thread {
                                    // 从传来的对象 的切割结果 就知道 id了！！根据id查出对象
                                    val thisBlogItem = BlogItemRepository.get().loadThisBlogItem(theBlogItemSplited!![1].toLong())
                                    // 判断更新没，不然再存一遍，时间更改就没意义了
                                    if(thisBlogItem.blogContent == edit_now.text.toString()){
                                        // 子线程toast，需要looper！
                                        Looper.prepare()
                                        Toast.makeText(this, "未做更改", Toast.LENGTH_SHORT).show()
                                        Looper.loop()
                                        // 不finish 哦，等用户改！
                                    }else{
                                        // unique的时间戳要保持最后一次保存时候的 【最新】时间值
                                        // 要保证只有在 发送/保存 按钮点击的逻辑里面才能更改时间和时间戳id
                                        // 更改id，是为了使排序顺序正常
                                        thisBlogItem.blogTime = time
                                        thisBlogItem.id = Date().time

                                        // 存正文到数据库
                                        thisBlogItem.blogContent = edit_now.text.toString()
                                        // 来自草稿箱，正文更新了，正文不是null -> 更新到数据库
                                        BlogItemRepository.get().updateBlogItem(thisBlogItem)

                                        Looper.prepare()
                                        Toast.makeText(this, "成功发送一条博客", Toast.LENGTH_SHORT).show()
                                        Looper.loop()
                                        finish()
                                    }
                                }
                            }

                            // 来自发了一个转发评论，现在要保存草稿 (这个case结合了上下两种case~)
                            "FromReblog" -> {
                                // 从传来的对象 的切割结果 就知道 id了
                                // 注意：isReblog是 【转发”指针“】
                                val blogItemEditNow = createANewBlogItem()
                                blogItemEditNow.isDraft = true
                                blogItemEditNow.reblogFrom = theBlogItemSplited!![1].toLong()
                                blogItemEditNow.blogContent = edit_now.text.toString()
                                thread {
                                    writeBlogViewModelInFragment.insertBlogItem(blogItemEditNow)
                                }
                                Toast.makeText(context, "草稿保存成功", Toast.LENGTH_SHORT).show()
                                finish()
                            }

                            // 来自发博客直接存草稿的情况
                            else -> {
                                val blogItemEditNow = createANewBlogItem()
                                blogItemEditNow.isDraft = true
                                blogItemEditNow.blogContent = edit_now.text.toString()
                                thread {
                                    writeBlogViewModelInFragment.insertBlogItem(blogItemEditNow)
                                }
                                Toast.makeText(context, "草稿保存成功", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                    }
                    .setNegativeButton("取消") { _, _ ->
                        Toast.makeText(this, "OK,继续编辑~", Toast.LENGTH_SHORT).show()
                    }
                    .create()
                    .show()
            }
        }

        addEmoji.setOnClickListener{
            Toast.makeText(this, "还没写", Toast.LENGTH_SHORT).show()
        }

        addPhoto.setOnClickListener{
            Toast.makeText(this, "也还没写，先占个位", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            
            // 发送按钮的点击事件，已经写完【2022.3.12】修修改改，22号
            R.id.sendBlog -> {

                // 取回传过来的数值
                val theBlogItem = intent.getStringExtra("TheBlogItem")
                val fromWhere = intent.getStringExtra("FromWhere")
                // 要使用正则来切割
                val theBlogItemSplited = theBlogItem?.split("=", ",",")")
                // 不能放子线程，为什么？
                // 点击 send 之后，这个时间才是想要留的最终的时间，保证只会生成一次
                val time = SimpleDateFormat("yyyy/MM/dd HH:mm").format(Date())

                // 还有一个来源是，主界面的edit，他比较特殊，他不需要更新发出的时间，要显示编辑记录？（really？）
                // 草稿箱来的，发出去，就要把 isDraft 置 false
                when (fromWhere) {
                    "FromDraft" -> {
                        thread {
                            // 从传来的对象 的切割结果 就知道 id了！！根据id查出对象
                            val thisBlogItem = BlogItemRepository.get().loadThisBlogItem(theBlogItemSplited!![1].toLong())
                            // 对对象的 isDraft 进行操作
                            // 其实保存和发送的【主要区别】在 isDraft值不一样 和 blog的初始值不一样 ，isDraft决定了这条博客是发到 主界面还是草稿箱
                            thisBlogItem.isDraft = false
                            // unique的时间戳要保持最后一次发送的 【最新】时间值
                            // 要保证只有在 发送/保存 按钮点击的逻辑里面才能更改时间和时间戳id
                            thisBlogItem.blogTime = time
                            thisBlogItem.id = Date().time

                            // 判断输入内容是否为空
                            if (edit_now.text.toString() == "") {
                                Toast.makeText(this, "输入不能为空哦", Toast.LENGTH_SHORT).show()
                            } else {
                                // 存正文到数据库
                                thisBlogItem.blogContent = edit_now.text.toString()
                            }
                            // 更新到数据库
                            BlogItemRepository.get().updateBlogItem(thisBlogItem)
                        }
                        Toast.makeText(this, "成功发送一条博客", Toast.LENGTH_SHORT).show()
                        finish()

                    }
                    "FromReblog" -> {
                        // 这里要处理 reblogTo的逻辑，要加到这个集合里面
                        // 从传来的对象的切割结果知道 id了,根据id进行赋值isReblog,isReblog 决定了这条博客在主界面的展示形式
                        val blogItemEditNow = createANewBlogItem()
                        // 【转发】的博客的逻辑处理
                        blogItemEditNow.reblogFrom = theBlogItemSplited!![1].toLong()
                        // 【被转发】的博客的逻辑处理
                        // 从传来的对象 的切割结果 就知道 id了！根据id查出被转发的对象,再把新生成的对象的id存到之前的被转发的对象的 reblogTo集合 里面
                        thread {
                            val thisBlogItemReblogFrom = BlogItemRepository.get().loadThisBlogItem(
                                theBlogItemSplited[1].toLong())
                            thisBlogItemReblogFrom.reblogTo.add(blogItemEditNow.id)
                            BlogItemRepository.get().updateBlogItem(thisBlogItemReblogFrom)
                        }
                        // 判断输入内容是否为空
                        if (edit_now.text.toString() == "") {
                            Toast.makeText(this, "输入不能为空哦", Toast.LENGTH_SHORT).show()
                        } else {
                            // 存正文到数据库
                            blogItemEditNow.blogContent = edit_now.text.toString()
                            thread {
                                writeBlogViewModelInFragment.insertBlogItem(blogItemEditNow)
                            }
                            Toast.makeText(this, "成功发送一条博客", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    else -> {
                        // isReblog 只是多一个isReblog的【指针】罢了,其他一样，分开写是因为有空指针异常
                        val blogItemEditNow = createANewBlogItem()
                        // 判断输入内容是否为空
                        if (edit_now.text.toString() == "") {
                            Toast.makeText(this, "输入不能为空哦", Toast.LENGTH_SHORT).show()
                        } else {
                            // 存正文到数据库
                            blogItemEditNow.blogContent = edit_now.text.toString()
                            thread {
                                writeBlogViewModelInFragment.insertBlogItem(blogItemEditNow)
                            }
                            Toast.makeText(this, "成功发送一条博客", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.write_blog, menu)
        return true
    }

    private fun createANewBlogItem(): BlogItem {
        //需要跨线程，太麻烦，不如用 getSharedPreferences
//        thread {
//            val userName = BlogItemRepository.get().loadUserName()
//            val userAvatar = BlogItemRepository.get().loadUserAvatar()
//        }

        // 获取最新头像
        val prefs = context.getSharedPreferences("editProfile", Context.MODE_PRIVATE)
        val user_avatar_in_prefs = prefs.getInt("user_choose_avatar", R.drawable.avatar_male_7)
        val user_name_in_prefs = prefs.getString("user_name", "快去取个名字！")

        return BlogItem(
            Date().time,user_name_in_prefs!!, user_avatar_in_prefs," ",
            SimpleDateFormat("yyyy/MM/dd HH:mm").format(Date()),
            " ", " ", 0, 0,false,ArrayList(),
            false, false, false, false, false,
        )
    }

}
