package com.chiibeii.xieyidian.ui.adapter

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chiibeii.xieyidian.R
import com.chiibeii.xieyidian.logic.entity.BlogItem
import com.chiibeii.xieyidian.BlogDetailedContent
import com.chiibeii.xieyidian.MyBlog
import com.chiibeii.xieyidian.logic.MyApplication
import com.chiibeii.xieyidian.logic.MyApplication.Companion.context
import com.chiibeii.xieyidian.logic.model.MyStarViewModel
import com.chiibeii.xieyidian.logic.repository.BlogItemRepository
import kotlinx.android.synthetic.main.blog_item.*
import kotlinx.android.synthetic.main.my_blog_profile.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class MainBlogItemListAdapter(
    private val context: Context?,
    private val blogItemList: List<BlogItem>
) :
    RecyclerView.Adapter<MainBlogItemListAdapter.MainBlogItemListViewHolder>() {

    // viewHolder
    inner class MainBlogItemListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = itemView.findViewById(R.id.userName)
        val userAvatar: ImageView = view.findViewById(R.id.userAvatar)
        val blogTime: TextView = itemView.findViewById(R.id.blogTime)
        val blogSource: TextView = view.findViewById(R.id.blogSource)
        val blogContent: TextView = view.findViewById(R.id.blogContent)
        val blogImage: ImageView = view.findViewById(R.id.blogImage)
        val isReblog: View? = view.findViewById(R.id.isReblogButton)
        val comment: View? = view.findViewById(R.id.commentButton)
        var isStar: View? = view.findViewById(R.id.isStarButton)
        val isLike: View? = view.findViewById(R.id.isLikeButton)
        val moreAction: View? = view.findViewById(R.id.moreActionButton)
        val tabOfBlogComment: View? = view.findViewById(R.id.tabOfBlogComment)
        val commentRecyclerView: View? = view.findViewById(R.id.commentRecyclerView)
        val isReadFullContent: View? = view.findViewById(R.id.isReadFullContent)

        val blogFront:View? = view.findViewById(R.id.blogItem_front)
        val blogCenter:View? = view.findViewById(R.id.blogItem_center)
    }

    // adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainBlogItemListViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.blog_item, parent, false)
        val blogViewHolder = MainBlogItemListViewHolder(view)

        // 头部点击跳转到个人博客界面
        blogViewHolder.blogFront?.setOnClickListener{
            val intent = Intent(context,MyBlog::class.java)
            context!!.startActivity(intent)
        }

        // 正文的点击事件
        blogViewHolder.blogCenter?.setOnClickListener {
            val intent = Intent(context, BlogDetailedContent::class.java)
//            val options =
//                ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
//            context!!.startActivity(intent, options.toBundle())
            context!!.startActivity(intent)

        }

        // more按钮的点击事件
        blogViewHolder.moreAction?.setOnClickListener {
            // 获取点击的位置
            val position = blogViewHolder.absoluteAdapterPosition
            val blogItem = blogItemList[position]

            // 弹窗
            val popup = PopupMenu(context, blogViewHolder.moreAction)
            popup.menuInflater.inflate(R.menu.more_action, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.deleteBlog -> {
                        AlertDialog.Builder(context)
                            .setMessage("删除将会无法撤回")
                            .setTitle("注意！")
                            .setPositiveButton("仍要删除") { _, _ ->
                                thread {
                                    BlogItemRepository.get().deleteOneBlogItem(blogItem)
                                }
                                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("不删除了") { _, _ ->
                                Toast.makeText(context, "OK，不删了", Toast.LENGTH_SHORT).show()
                            }
                            .create()
                            .show()
                    }
                    R.id.editBlog -> {
                        thread {
//                            blogItemTest01.blogContent="0000000000000000000000000000"
//                            blogItemDaoForTest.updateBlog(blogItemTest01)
                        }
                    }
                }
                true
            }
            popup.show()
        }

        blogViewHolder.tabOfBlogComment?.visibility = View.GONE
        blogViewHolder.commentRecyclerView?.visibility = View.GONE

        return blogViewHolder
    }

    override fun onBindViewHolder(holder: MainBlogItemListViewHolder, position: Int) {
        val blogItem = blogItemList[position]
        Glide.with(context!!).load(blogItem.userAvatar).into(holder.userAvatar)
        Glide.with(context).load(blogItem.blogImage).into(holder.blogImage)
        holder.userName.text = blogItem.userName
        holder.blogContent.text = blogItem.blogContent
        holder.blogSource.text = blogItem.blogSource
        holder.blogTime.text = blogItem.blogTime
        holder.blogImage.visibility = if (blogItem.blogImage != 0) View.VISIBLE else View.GONE

        holder.isStar?.setOnClickListener {
            blogItem.isStar = !blogItem.isStar
            thread {
                BlogItemRepository.get().updateBlogContent(blogItem)
            }
            if (blogItem.isStar)
                Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(context, "取消收藏成功", Toast.LENGTH_SHORT).show()
        }

        // 在 textview 渲染完之后执行
        holder.blogContent.post {
            val linesCount = holder.blogContent.lineCount
            holder.isReadFullContent?.visibility = if (linesCount >= 5) View.VISIBLE else View.GONE
        }

        val prefs = context.getSharedPreferences("editProfile", Context.MODE_PRIVATE)
        val user_name_in_prefs = prefs?.getString("user_name", "快去取个名字！")
        holder.userName.text = user_name_in_prefs

    }

    override fun getItemCount() = blogItemList.size

}