package com.chiibeii.ZiYanZiYu.ui.adapter

import android.os.Handler
import android.os.Looper
import android.os.Message
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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chiibeii.ZiYanZiYu.ui.activity.BlogDetailedContent
import com.chiibeii.ZiYanZiYu.ui.activity.MyBlog
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.ui.activity.WriteBlog
import com.chiibeii.ZiYanZiYu.ui.activity.MyApplication.Companion.context
import com.chiibeii.ZiYanZiYu.logic.entity.BlogItem
import com.chiibeii.ZiYanZiYu.logic.repository.BlogItemRepository
import com.chiibeii.ZiYanZiYu.ui.adapter.MainBlogItemListAdapter.MainBlogItemListViewHolder
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.blog_item.*
import kotlin.concurrent.thread

class MainBlogItemListAdapter
    : ListAdapter<BlogItem, MainBlogItemListViewHolder>(MainBlogItemListDiffUtil) {

    // viewHolder
    inner class MainBlogItemListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val userName: TextView = itemView.findViewById(R.id.userName)
        private val userAvatar: ImageView = view.findViewById(R.id.userAvatar)
        private val blogTime: TextView = itemView.findViewById(R.id.blogTime)
        private val blogSource: TextView = view.findViewById(R.id.blogSource)
        private val blogContent: TextView = view.findViewById(R.id.blogContent)
        private val blogImage: ImageView = view.findViewById(R.id.blogImage)
        private val isReblog: View? = view.findViewById(R.id.isReblogButton)
        val comment: View? = view.findViewById(R.id.commentButton)
        var isStar: View? = view.findViewById(R.id.isStarButton)
        val isLike: View? = view.findViewById(R.id.isLikeButton)
        val moreAction: View? = view.findViewById(R.id.moreActionButton)
//        private val tabOfBlogComment: View? = view.findViewById(R.id.tabOfBlogComment)
        private val reblogRecyclerView: View? = view.findViewById(R.id.reblogRecyclerView)
        private val isReadFullContent: View? = view.findViewById(R.id.isReadFullContent)
        val blogFront: View? = view.findViewById(R.id.blogItem_front)
        private val blogCenter: View? = view.findViewById(R.id.blogItem_center)
        private val blogItemReblogInBlogItem: View? = view.findViewById(R.id.blogItem_reblog_inBlogItem)
        private val userNameReblog:TextView  = view.findViewById(R.id.userName_reblog)
        private val blogTimeReblog:TextView = view.findViewById(R.id.blogTime_reblog)
        val blogContentReblog:TextView = view.findViewById(R.id.blogContent_reblog)


        fun bind(blogItem: BlogItem) {
//            Glide.with(context).load(blogItem.userAvatar).into(userAvatar)
            Glide.with(context).load(blogItem.blogImage).into(blogImage)
            blogContent.text = blogItem.blogContent
            blogSource.text = blogItem.blogSource
            blogTime.text = blogItem.blogTime

            val prefs = context.getSharedPreferences("editProfile", Context.MODE_PRIVATE)
            val user_name_in_prefs = prefs?.getString("user_name", "快去取个名字！")
            val user_avatar_in_prefs = prefs.getInt("user_choose_avatar", R.drawable.avatar_male_7)
            userName.text = user_name_in_prefs
            userAvatar.setImageResource(user_avatar_in_prefs)

            blogImage.visibility = if (blogItem.blogImage != 0) View.VISIBLE else View.GONE

            // 很有可能不做的两个部分
//            tabOfBlogComment?.visibility = View.GONE
            reblogRecyclerView?.visibility = View.GONE

            userNameReblog.text = user_name_in_prefs

            // 转发模块的可见性：注意0L
            if (blogItem.isReblog != 0L){
                blogItemReblogInBlogItem?.visibility = View.VISIBLE
                // 在子线程更新 UI，使用message、handler、messageQueue、looper
                val updateReblogItem = 1
                val handler = object :Handler(Looper.getMainLooper()){
                    override fun handleMessage(msg:Message){
                        when (msg.what) {
                            updateReblogItem -> {
                                // 指针，而且要避免在子线程直接更改UI！！！
                                // 此时，已经到了主线程了，但是数据库不能在主线程操作,所以通过 msg 在子线程处理好传过来
                                blogTimeReblog.text = (msg.obj as ArrayList<String>)[0]
                                blogContentReblog.text = (msg.obj as ArrayList<String>)[1]
                            }
                        }
                    }
                }
                thread {
                    val msg = Message()
                    // 获取转发的内容，存到集合里面
                    val blogTimeReblogText = BlogItemRepository.get().loadThisBlogItem(blogItem.isReblog).blogTime
                    val blogContentReblogText = BlogItemRepository.get().loadThisBlogItem(blogItem.isReblog).blogContent
                    val blogReblogMessage = ArrayList<String>()
                    blogReblogMessage.add(blogTimeReblogText)
                    blogReblogMessage.add(blogContentReblogText)
                    // 在线程之间传递这个集合
                    msg.obj = blogReblogMessage
                    msg.what = updateReblogItem
                    handler.sendMessage(msg)
                }
            }else{
                blogItemReblogInBlogItem?.visibility = View.GONE
            }

//             烦死了，一直闪
//             在 textview 渲染完之后执行
            blogContent.post {
                val linesCount = blogContent.lineCount
                isReadFullContent?.visibility = if (linesCount >= 5) View.VISIBLE else View.GONE
            }

            // 正文的点击事件
            blogCenter?.setOnClickListener {
                val intent = Intent(context, BlogDetailedContent::class.java)
//            val options =
//                ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
//            context!!.startActivity(intent, options.toBundle())
                // activity外进activity，要加这个flags
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("TheBlogItem",blogItem.toString())
                Log.d(TAG, "1111111111111111111111111111111111111111111bind: $blogItem")
                context.startActivity(intent)
            }

            // 转发按钮的点击事件
            isReblog?.setOnClickListener{
                val intent = Intent(context, WriteBlog::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                // 携带 来的这个activity的blogContent 去转发评论编辑
                intent.putExtra("TheBlogItem",blogItem.toString())
                intent.putExtra("FromWhere","FromReblog")
                context.startActivity(intent)
            }

        }
    }

    // adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MainBlogItemListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.blog_item, parent, false)
        val holder = MainBlogItemListViewHolder(view)

        // 头部点击跳转到个人博客界面
        holder.blogFront?.setOnClickListener {
            val intent = Intent(context, MyBlog::class.java)
            // 要带一个 Flag！才能用 context的startActivity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        // more按钮的点击事件
        holder.moreAction?.setOnClickListener {
            // 新方法，获取 item
            val blogItem = getItem(holder.absoluteAdapterPosition)

            // 弹窗
            val popup = PopupMenu(parent.context, holder.moreAction)
            popup.menuInflater.inflate(R.menu.more_action, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.deleteBlog -> {
                        MaterialAlertDialogBuilder(parent.context)
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
                            Looper.prepare()
                            Toast.makeText(context, "还没写", Toast.LENGTH_SHORT).show()
                            Looper.loop()
//                            blogItemTest01.blogContent="0000000000000000000000000000"
//                            blogItemDaoForTest.updateBlog(blogItemTest01)
                        }
                    }
                }
                true
            }
            popup.show()
        }

        return holder
    }

    override fun onBindViewHolder(holder: MainBlogItemListViewHolder, position: Int) {
        holder.bind(getItem(position))
        val blogItem = getItem(position)

        // 图标状态初始化
        holder.isStar!!.isActivated = blogItem.isStar

        // 收藏按钮 点击事件
        holder.isStar?.setOnClickListener {
            blogItem.isStar = !blogItem.isStar
            thread {
                BlogItemRepository.get().updateBlogContent(blogItem)
            }
            if (blogItem.isStar){
                Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "取消收藏成功", Toast.LENGTH_SHORT).show()
            }
            // 图标需要更改
            holder.isStar!!.isActivated = blogItem.isStar
        }

    }

    object MainBlogItemListDiffUtil : DiffUtil.ItemCallback<BlogItem>() {

        override fun areItemsTheSame(oldItem: BlogItem, newItem: BlogItem): Boolean {
            // 借用主键来比较,看是不是同一个对象
            return oldItem.blogId == newItem.blogId
        }

        override fun areContentsTheSame(oldItem: BlogItem, newItem: BlogItem): Boolean {
            // 看各项内容是否相同
            return newItem == oldItem

//            oldItem.blogContent == newItem.blogContent &&
//            oldItem.blogImage == newItem.blogImage &&
//            oldItem.comment == newItem.comment &&
//            oldItem.blogSource == newItem.blogSource &&
//            oldItem.userName == newItem.userName &&
//            oldItem.userAvatar == newItem.userAvatar &&
//            oldItem.blogTime == newItem.blogTime &&
//            oldItem.isReblog == newItem.isReblog &&
//            oldItem.isStar == newItem.isStar &&
//            oldItem.isLike == newItem.isLike &&
//            oldItem.isDelete == newItem.isDelete &&
//            oldItem.isDraft == newItem.isDraft
        }
    }
}

