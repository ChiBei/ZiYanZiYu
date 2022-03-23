package com.chiibeii.ZiYanZiYu.ui.activity

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.provider.MediaStore
import android.widget.ScrollView
import android.widget.Toast
import androidx.core.graphics.createBitmap
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.ui.activity.MyApplication.Companion.context


private class PhotoShare {

    private val prefs = context.getSharedPreferences("editProfile", Context.MODE_PRIVATE)
    private val user_name_in_prefs = prefs?.getString("user_name", "快去取个名字！")

    fun captureScrollView(scrollView: ScrollView) {
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
        val basic = BitmapFactory.decodeResource(context.resources, R.drawable.basic)
        val top = BitmapFactory.decodeResource(context.resources, R.drawable.top)
        val bottom = BitmapFactory.decodeResource(context.resources, R.drawable.right)
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
        paint.typeface = Typeface.createFromAsset(MyApplication.context.assets, "youshe.ttf")
        cv.drawText("#$user_name_in_prefs" + "的自言自语",
            50F, headHeight + mainHeight.toFloat()+30, paint)

        cv.save() // 保存
        cv.restore() // 存储
        saveBitmapPhoto(newBitmap)

        basic.recycle()
        bitmap.recycle()
    }

    fun saveBitmapPhoto(bitmap: Bitmap) {
        val resolver = context.contentResolver
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,
            "ZiYanZiYu_${System.currentTimeMillis()}")
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

        // 通知刷新？？？
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        // 会有两张，而且弃用了
//        MediaStore.Images.Media.insertImage(contentResolver, bitmap, "ZiYanZiYu_${System.currentTimeMillis()}", "file")

        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if (uri != null) {
            resolver.openOutputStream(uri).use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                Toast.makeText(context, "成功保存到相册，快去分享吧", Toast.LENGTH_LONG).show()
            }
        }

        // 备胎方法（要杀掉相册，打开才刷新，不确定。。。）
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
    }


}