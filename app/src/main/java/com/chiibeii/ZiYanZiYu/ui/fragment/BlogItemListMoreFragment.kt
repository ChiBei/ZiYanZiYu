package com.chiibeii.ZiYanZiYu.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.logic.repository.BlogItemRepository
import com.chiibeii.ZiYanZiYu.ui.activity.MainActivity
import com.chiibeii.ZiYanZiYu.ui.activity.MyApplication
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.blog_item.view.*
import kotlinx.android.synthetic.main.blog_item_list_more_bottom_sheet_fragment.*
import kotlin.concurrent.thread

class BlogItemListMoreFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view =
            inflater.inflate(R.layout.blog_item_list_more_bottom_sheet_fragment, container, false)

        val text_share_in_bottom_sheet =
            view.findViewById<ImageButton>(R.id.text_share_in_bottom_sheet)
        val edit_content_in_bottom_sheet =
            view.findViewById<ImageButton>(R.id.edit_content_in_bottom_sheet)
        val delete_blog_in_bottom_sheet =
            view.findViewById<ImageButton>(R.id.delete_blog_in_bottom_sheet)

        text_share_in_bottom_sheet.setOnClickListener {

            val clipboardManager =
                getSystemService(context!!, MainActivity::class.java) as ClipboardManager
            val clipData = ClipData.newPlainText("blogContentCopied", it.blogContent.toString())
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(context, "已复制正文到剪贴板", Toast.LENGTH_SHORT).show()

        }

        edit_content_in_bottom_sheet.setOnClickListener {
            Toast.makeText(MyApplication.context, "还没写", Toast.LENGTH_SHORT).show()
        }

        delete_blog_in_bottom_sheet.setOnClickListener {

            // 现在不删东西了，只把 自身的 isDelete位 置为 true，不考虑儿子孙子的指针了，全部保留，为以后的 嵌套转发 留一手
            // 真实的删除操作留到 回收站 考虑
            // 【思路历程：不管有没有父亲，没儿子，就直接删了，不留一点痕迹；不管有没有父亲，有儿子--->
            // 要把儿子全删了，而且儿子有孙子，还要把孙子都删了！！不然报错原理是一模一样的，治标不治本----->无限层嵌套，办不到
            // ----->只显示被删的直属上级”被删了“，还要考虑嵌套转发，好麻烦。。。】
            // 使用isDelete位，就解决了，但是删除问题还要在草稿箱和回收站考虑
            // 现在再追加 isReblogFromDeleted 位，来实现用livedata实时更新 ”被删除字样“

//            thread {
//                // 删本体,先不真删，先使用 isDelete位
//                // 【bug所在地】，只要 isDelete 一改，上面就进入线程传递了，殊不知这个item马上就要被删了
//                this.isDelete = true
//                // 有儿子的话，查儿子，把儿子的 isReblogFromDeleted 全部也改为 true(儿子要是被删了，虽然链接还在，但是已经没有了)
//                if (blogItem.reblogTo.isNotEmpty()){
//                    for (k in blogItem.reblogTo) {
//                        // 必要的判空！！不要相信编译器---> 如果可以查到 where id = id，查不到就是被删了
//                        if (BlogItemRepository.get().loadThisBlogItem(k)!=null){
//                            val son = BlogItemRepository.get().loadThisBlogItem(k)
//                            son.isReblogFromDeleted = true
//                            BlogItemRepository.get().updateBlogItem(son)
//                        }
//                    }
//                }
//                BlogItemRepository.get().updateBlogItem(blogItem)
//            }
            Toast.makeText(MyApplication.context, "已放入回收站", Toast.LENGTH_SHORT).show()

        }

        return view
    }

}