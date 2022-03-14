package com.chiibeii.ZiYanZiYu.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View.*
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.createBitmap
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.logic.repository.BlogItemRepository
import kotlinx.android.synthetic.main.activity_blog_detailed_content.*
import kotlinx.android.synthetic.main.blog_item.*
import kotlin.concurrent.thread

class BlogDetailedContent : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_detailed_content)

        setSupportActionBar(blogDetailedContentTopAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 取回传过来的数值
        val theBlogItem = intent.getStringExtra("TheBlogItem")
        // 要使用正则来切割
        val theBlogItemSplited = theBlogItem?.split("=",",")
//        这是切割后的例子
//        [BlogItem(userName, ChiiBeii,
//        userAvatar, 2131165316,
//        blogTime, 2022/03/09 11:26,
//        blogSource,from 1,
//        blogContent,电饭锅哈哈哈,
//        blogImage,0,
//        isReblog, false,  comment, false,
//        isStar, false,  isLike, false,
//        isDelete, false,  isDraft, false)]

        val prefs = MyApplication.context.getSharedPreferences("editProfile", Context.MODE_PRIVATE)
        val user_name_in_prefs = prefs?.getString("user_name", "快去取个名字！")
        val user_avatar_in_prefs = prefs?.getInt("user_choose_avatar", R.drawable.avatar_male_7)
        userName.text = user_name_in_prefs
        userAvatar.setImageResource(user_avatar_in_prefs!!)
//        userAvatar.setImageResource(theBlogItemSplited!![3].toInt())

        blogTime.text = theBlogItemSplited!![5]
        blogContent.text = theBlogItemSplited[9]
        blogContent.maxLines = 1000

        blogImage.visibility = if (theBlogItemSplited[11].toInt() != 0)
            VISIBLE else GONE

//        tabOfBlogComment.visibility = if (theBlogItemSplited[15].toBoolean())
//            VISIBLE else GONE
        imageButtonSet.visibility = GONE
        isReadFullContent.visibility = GONE


        // 转发模块
        if (theBlogItemSplited[13].toLong() != 0L){
            blogItem_reblog_inBlogItem.visibility = VISIBLE
            // 注意detailed是主界面include来的，不要用错了id！
            // control查看源的时候要看看文件名，因为我的几个界面太像了！
            userName_reblog.text = user_name_in_prefs
            // 在子线程更新 UI，使用message、handler、messageQueue、looper
            val updateReblogItem = 1
            val handler = object : Handler(Looper.getMainLooper()){
                override fun handleMessage(msg: Message){
                    when (msg.what) {
                        updateReblogItem -> {
                            // 指针，而且要避免在子线程直接更改UI！！！
                            // 此时，已经到了主线程了，但是数据库不能在主线程操作,所以通过 msg 在子线程处理好传过来
                            blogTime_reblog.text = (msg.obj as ArrayList<String>)[0]
                            blogContent_reblog.text = (msg.obj as ArrayList<String>)[1]
                        }
                    }
                }
            }
            thread {
                val msg = Message()
                // 获取转发的内容，存到集合里面
                val blogTimeReblogText = BlogItemRepository.get().loadThisBlogItem(theBlogItemSplited[13].toLong()).blogTime
                val blogContentReblogText = BlogItemRepository.get().loadThisBlogItem(theBlogItemSplited[13].toLong()).blogContent
                val blogReblogMessage = ArrayList<String>()
                blogReblogMessage.add(blogTimeReblogText)
                blogReblogMessage.add(blogContentReblogText)
                // 在线程之间传递这个集合
                msg.obj = blogReblogMessage
                msg.what = updateReblogItem
                handler.sendMessage(msg)
            }
        }else{
            blogItem_reblog_inBlogItem.visibility = GONE
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.blog_detailed_content,menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val theBlogItem = intent.getStringExtra("TheBlogItem")
        val theBlogItemSplited = theBlogItem?.split("=",",")

        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.photoShare ->{
                Toast.makeText(this,"分享功能还没做",Toast.LENGTH_SHORT).show()
//                val bitmap = captureScrollView(scrollViewInDetailedBlogContent)
            }

            R.id.copyText -> {
                val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("blogContentCopied",
                    theBlogItemSplited?.get(9)
                )
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(this,"已复制正文到剪贴板",Toast.LENGTH_SHORT).show()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }


    private fun captureScrollView(scrollView: ScrollView): Bitmap {
        var h = 0
        // scrollview里面只有一个child
        for (i in 0 until scrollView.childCount) {
            val childView = scrollView.getChildAt(i)
            // 累加 子View的高度
            h += childView.height
            // 设置背景颜色
            childView.setBackgroundColor(Color.parseColor("#1a73e8"))
        }
        val bitmap = createBitmap(scrollView.width+50, h+1000)
        val canvas = Canvas(bitmap)
        // 将ScrollView绘制在画布上
        scrollView.draw(canvas)
        return bitmap
    }

}