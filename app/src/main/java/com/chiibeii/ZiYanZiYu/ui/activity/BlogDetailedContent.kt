package com.chiibeii.ZiYanZiYu.ui.activity

import android.content.*
import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.createBitmap
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.logic.repository.BlogItemRepository
import com.chiibeii.ZiYanZiYu.ui.activity.MyApplication.Companion.context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_blog_detailed_content.*
import kotlinx.android.synthetic.main.blog_item.*
import kotlin.concurrent.thread


class BlogDetailedContent : AppCompatActivity() {

    private val prefs = context.getSharedPreferences("editProfile", Context.MODE_PRIVATE)
    private val user_name_in_prefs = prefs?.getString("user_name", "快去取个名字！")
    private val user_avatar_in_prefs = prefs?.getInt("user_choose_avatar", R.drawable.avatar_male_7)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_detailed_content)

        setSupportActionBar(blogDetailedContentTopAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 取回传过来的数值
        val theBlogItem = intent.getStringExtra("TheBlogItem")
        // 要使用正则来切割
        val theBlogItemSplited = theBlogItem?.split("=", ",")
//        这是切割后的例子
//        [BlogItem(id, 1647058486309,
//        userName, ChiiBeii,
//        userAvatar, 2131165316,
//        userWords, 1111,
//        blogTime, 2022/03/09 11:26,
//        blogSource,from 1,
//        blogContent,电饭锅哈哈哈,
//        blogImage,0,
//        reblogFrom, 0, reblogTo,[],
//        comment, false,
//        isStar, false,  isLike, false,
//        isDelete, false,  isDraft, false
//        id, 1647058486309)]


        userName.text = user_name_in_prefs
        userAvatar.setImageResource(user_avatar_in_prefs!!)
//        userAvatar.setImageResource(theBlogItemSplited!![3].toInt())

        blogTime.text = theBlogItemSplited!![9]
        blogContent.text = theBlogItemSplited[13]
        blogContent.maxLines = 1000

        blogImage.visibility = if (theBlogItemSplited[15].toInt() != 0)
            VISIBLE else GONE

//        tabOfBlogComment.visibility = if (theBlogItemSplited[15].toBoolean())
//            VISIBLE else GONE
        imageButtonSet.visibility = GONE
        isReadFullContent.visibility = GONE

        // 转发模块
        if (theBlogItemSplited[17].toLong() != 0L) {
            blogItem_reblog_inBlogItem.visibility = VISIBLE
            // 注意detailed是主界面include来的，不要用错了id！
            // control查看源的时候要看看文件名，因为我的几个界面太像了！
            userName_reblog.text = user_name_in_prefs
            // 在子线程更新 UI，使用message、handler、messageQueue、looper
            val updateReblogItem = 1
            val handler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
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
                val blogTimeReblogText = BlogItemRepository.get()
                    .loadThisBlogItem(theBlogItemSplited[17].toLong()).blogTime
                val blogContentReblogText = BlogItemRepository.get()
                    .loadThisBlogItem(theBlogItemSplited[17].toLong()).blogContent
                val blogReblogMessage = ArrayList<String>()
                blogReblogMessage.add(blogTimeReblogText)
                blogReblogMessage.add(blogContentReblogText)
                // 在线程之间传递这个集合
                msg.obj = blogReblogMessage
                msg.what = updateReblogItem
                handler.sendMessage(msg)
            }
        } else {
            blogItem_reblog_inBlogItem.visibility = GONE
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.blog_detailed_content, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val theBlogItem = intent.getStringExtra("TheBlogItem")
        val theBlogItemSplited = theBlogItem?.split("=", ",", ")")

        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            // 分享图片
            R.id.photoShare -> {
                captureScrollView(scrollViewInDetailedBlogContent)
            }

            // 复制正文
            R.id.copyText -> {
                val clipboardManager =
                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("blogContentCopied",
                    theBlogItemSplited?.get(9)
                )
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(this, "已复制正文到剪贴板", Toast.LENGTH_SHORT).show()
                return true
            }

            // 删除博客
            R.id.deleteBlogInDetailedContent -> {
                MaterialAlertDialogBuilder(this)
                    .setMessage("删除将会无法撤回")
                    .setTitle("注意！")
                    .setPositiveButton("仍要删除") { _, _ ->
                        thread {
                            val theBlogItem = BlogItemRepository.get()
                                .loadThisBlogItem(theBlogItemSplited!![1].toLong())
                            BlogItemRepository.get().deleteOneBlogItem(theBlogItem)
                        }
                        Toast.makeText(MyApplication.context, "删除成功", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .setNegativeButton("不删除了") { _, _ ->
                        Toast.makeText(MyApplication.context, "OK，不删了", Toast.LENGTH_SHORT).show()
                    }
                    .create()
                    .show()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }


    private fun captureScrollView(scrollView: ScrollView) {
        var h = 0
        // scrollview里面只有一个child
        for (i in 0 until scrollView.childCount) {
            // 累加 子View的高度
            val childView = scrollView.getChildAt(i)
            h += childView.height
        }

        // 主体 bitmap
        val bitmap = createBitmap(scrollView.width, h)
        val canvas = Canvas(bitmap)
        scrollView.draw(canvas)
        val mainWidth = bitmap.width
        val mainHeight = bitmap.height

        // 头 和 脚 和 引号
        val basic = BitmapFactory.decodeResource(resources, R.drawable.basic)
        val top = BitmapFactory.decodeResource(resources, R.drawable.top)
        val bottom = BitmapFactory.decodeResource(resources, R.drawable.right)
        val headHeight = basic.height / 3
        val footHeight = basic.height / 4

        //合并三部分
        val newBitmap = createBitmap(mainWidth, headHeight + mainHeight + footHeight)
        val cv = Canvas(newBitmap)
        // 背景
        cv.drawColor(Color.parseColor("#1a73e8"))
        // bottom 引号
        cv.drawBitmap(bottom, 650F, headHeight + mainHeight - 150F, null)
        // top logo
        cv.drawBitmap(top, 450F, headHeight - 75F, null)

        // 主体卡片
        cv.drawBitmap(bitmap, 0F, headHeight.toFloat(), null)

        // 话题，字
        val paint = Paint()
        paint.textSize = 50F
        paint.color = Color.WHITE
        paint.typeface = Typeface.createFromAsset(this.assets, "youshe.ttf")
        cv.drawText("#$user_name_in_prefs" + "的自言自语",
            50F, headHeight + mainHeight.toFloat()+30, paint)

        cv.save() // 保存
        cv.restore() // 存储
        saveBitmapPhoto(newBitmap)

        basic.recycle()
        bitmap.recycle()
    }

    private fun saveBitmapPhoto(bitmap: Bitmap) {
        val resolver = application.contentResolver
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,
            "ZiYanZiYu_${System.currentTimeMillis()}")
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

        // 通知刷新？？？
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        // 会有两张，而且弃用了
//        MediaStore.Images.Media.insertImage(contentResolver, bitmap, "ZiYanZiYu_${System.currentTimeMillis()}", "file")

        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if (uri != null) {
            resolver.openOutputStream(uri).use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                Toast.makeText(this, "成功保存到相册，快去分享吧", Toast.LENGTH_LONG).show()
            }
        }

        // 备胎方法（要杀掉相册，打开才刷新，不确定。。。）
        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
    }


}