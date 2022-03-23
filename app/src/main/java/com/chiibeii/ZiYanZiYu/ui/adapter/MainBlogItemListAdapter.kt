package com.chiibeii.ZiYanZiYu.ui.adapter

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
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
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.logic.entity.BlogItem
import com.chiibeii.ZiYanZiYu.logic.repository.BlogItemRepository
import com.chiibeii.ZiYanZiYu.ui.activity.*
import com.chiibeii.ZiYanZiYu.ui.activity.MyApplication.Companion.context
import com.chiibeii.ZiYanZiYu.ui.adapter.MainBlogItemListAdapter.MainBlogItemListViewHolder
import com.chiibeii.ZiYanZiYu.ui.fragment.BlogItemListMoreFragment
import kotlinx.android.synthetic.main.edit_profile_fragment.*
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

        private val reblogRecyclerView: View? = view.findViewById(R.id.reblogRecyclerView)
        private val isReadFullContent: View? = view.findViewById(R.id.isReadFullContent)
        val blogFront: View? = view.findViewById(R.id.blogItem_front)
        private val blogCenter: View? = view.findViewById(R.id.blogItem_center)
        private val blogItemReblogInBlogItem: View? = view.findViewById(R.id.blogItem_reblog_inBlogItem)
        private val userNameReblog: TextView = view.findViewById(R.id.userName_reblog)
        private val blogTimeReblog: TextView = view.findViewById(R.id.blogTime_reblog)
        val blogContentReblog: TextView = view.findViewById(R.id.blogContent_reblog)


        fun bind(blogItem: BlogItem) {
            Glide.with(context).load(blogItem.userAvatar).into(userAvatar)
            Glide.with(context).load(blogItem.blogImage).into(blogImage)
            blogContent.text = blogItem.blogContent
            blogSource.text = blogItem.blogSource
            blogTime.text = blogItem.blogTime
            userName.text = blogItem.userName

            blogImage.visibility = if (blogItem.blogImage != 0) View.VISIBLE else View.GONE

            // 可能不做的部分
            reblogRecyclerView?.visibility = View.GONE

            // 如果有转发
            if (blogItem.reblogFrom != 0L)  {
                // 转发可见
                blogItemReblogInBlogItem?.visibility = View.VISIBLE
                // 如果转发没被删除 ---> 在子线程更新 UI，使用message、handler、messageQueue、looper
                if (!blogItem.isReblogFromDeleted){
                    val updateReblogItem = 1
                    val handler = object : Handler(Looper.getMainLooper()) {
                        override fun handleMessage(msg: Message) {
                            when (msg.what) {
                                updateReblogItem -> {
                                    // 指针，而且要避免在子线程直接更改UI！！！
                                    // 此时，已经到了主线程了，但是数据库不能在主线程操作,所以通过 msg 在子线程处理好传过来
                                    // 【查询父亲是不是isDelete，如果是，那么就显示“已删除”】-->直接用新加的isReblogDeleted
                                    // 一次只能恢复一个，因为列表只传了一个，要是传了多个，就会把后面的东西覆盖在前面一个了，就会导致time、name被gone了---->恢复跳到主界面
                                    Log.d(TAG, "handleMessage: ${blogItem.blogContent} ${blogItem.isReblogFromDeleted} ")
                                    userNameReblog.text = blogItem.userName
                                    Log.d(TAG, "handleMessage: name1111111111111111111111111111111111111")
                                    blogTimeReblog.text = (msg.obj as ArrayList<String>)[0]
                                    Log.d(TAG, "handleMessage: time111111111111111111111111111111111111111111")
                                    blogContentReblog.text = (msg.obj as ArrayList<String>)[1]
                                    Log.d(TAG, "handleMessage: content11111111111111111111111111111111111111111111111")
                                }
                            }
                        }
                    }
                    thread {
                        val msg = Message()
                        // 如果reblogFrom没有被删
                        // 获取转发的内容，存到集合里面。上下的逻辑都是，如果没有被删，就正常传，否则只显示完蛋 即可
                        if(!blogItem.isReblogFromDeleted){
                            val blogTimeReblogText =
                                BlogItemRepository.get().loadThisBlogItem(blogItem.reblogFrom).blogTime
                            val blogContentReblogText =
                                BlogItemRepository.get().loadThisBlogItem(blogItem.reblogFrom).blogContent
                            val blogReblogMessage = ArrayList<String>()
                            blogReblogMessage.add(blogTimeReblogText)
                            blogReblogMessage.add(blogContentReblogText)
                            // 在线程之间传递这个集合
                            msg.obj = blogReblogMessage
                        }
                        msg.what = updateReblogItem
                        handler.sendMessage(msg)
                    }
                }else
                // 否则虽然有转发，但是转发被删
                {
                    Log.d(TAG, "handleMessage: ${blogItem.blogContent} ${blogItem.isReblogFromDeleted} ")
                    blogTimeReblog.visibility = View.GONE
                    Log.d(TAG, "handleMessage: time00000000000000000000000000000000000000000000000000")
                    userNameReblog.visibility = View.GONE
                    Log.d(TAG, "handleMessage: name000000000000000000000000000000000000000000000000000")
                    blogContentReblog.text = "啊偶，无法查看原博文了！"
                    Log.d(TAG, "handleMessage: content00000000000000000000000000000000000000000000000000000000")
                }
            }
            // 没有转发
            else
            {
                blogItemReblogInBlogItem?.visibility = View.GONE
            }

//             烦死了，一直闪------->好像后来又没有？？
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
                intent.putExtra("TheBlogItem", blogItem.toString())
                Log.d(TAG, "1111111111111111111111111111111111111111111bind: $blogItem")
                context.startActivity(intent)
            }

            // 转发按钮的点击事件
            isReblog?.setOnClickListener {
                val intent = Intent(context, WriteBlog::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                // 携带 来的这个activity的blogContent 去转发评论编辑
                intent.putExtra("TheBlogItem", blogItem.toString())
                intent.putExtra("FromWhere", "FromReblog")
                context.startActivity(intent)
                // 转发的，要到按下发送按钮，才加到 reblogTo集合里面，现在还远远没有
                // 草稿箱里面的删除，不涉及到reblogTo集合，不发出来不能成为别人 reblog的对象
            }
        }
    }

    // adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainBlogItemListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blog_item, parent, false)
        val holder = MainBlogItemListViewHolder(view)

        // 头部点击跳转到个人博客界面
        holder.blogFront?.setOnClickListener {
            val intent = Intent(context, MyBlog::class.java)
            // 要带一个 Flag！才能用 context的startActivity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }


//        holder.moreAction?.setOnClickListener {
//            // 弹出 bottomSheet
//            val blogItemListMoreBottomSheet = BlogItemListMoreFragment()
//            blogItemListMoreBottomSheet.show(, "aaa")
//        }


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
                        // 现在不删东西了，只把 自身的 isDelete位 置为 true，不考虑儿子孙子的指针了，全部保留，为以后的 嵌套转发 留一手
                        // 真实的删除操作留到 回收站 考虑
                        // 【思路历程：不管有没有父亲，没儿子，就直接删了，不留一点痕迹；不管有没有父亲，有儿子--->
                        // 要把儿子全删了，而且儿子有孙子，还要把孙子都删了！！不然报错原理是一模一样的，治标不治本----->无限层嵌套，办不到
                        // ----->只显示被删的直属上级”被删了“，还要考虑嵌套转发，好麻烦。。。】
                        // 使用isDelete位，就解决了，但是删除问题还要在草稿箱和回收站考虑
                        // 现在再追加 isReblogFromDeleted 位，来实现用livedata实时更新 ”被删除字样“
//                        MaterialAlertDialogBuilder(parent.context)
//                            .setMessage("是否将该博文放入回收站？")
//                            .setTitle("注意！")
//                            .setPositiveButton("放入回收站") { _, _ ->
                                thread {
                                    // 删本体,先不真删，先使用 isDelete位
                                    // 【bug所在地】，只要 isDelete 一改，上面就进入线程传递了，殊不知这个item马上就要被删了
                                    blogItem.isDelete = true
                                    // 有儿子的话，查儿子，把儿子的 isReblogFromDeleted 全部也改为 true(儿子要是被删了，虽然链接还在，但是已经没有了)
                                    if (blogItem.reblogTo.isNotEmpty()){
                                        for (k in blogItem.reblogTo) {
                                            // 必要的判空！！不要相信编译器---> 如果可以查到 where id = id，查不到就是被删了
                                            if (BlogItemRepository.get().loadThisBlogItem(k)!=null){
                                                val son = BlogItemRepository.get().loadThisBlogItem(k)
                                                son.isReblogFromDeleted = true
                                                BlogItemRepository.get().updateBlogItem(son)
                                            }
                                        }
                                    }
                                    BlogItemRepository.get().updateBlogItem(blogItem)
                                }
                                Toast.makeText(context, "已放入回收站", Toast.LENGTH_SHORT).show()
//                            }
//                            .setNegativeButton("取消放入") { _, _ ->
//                            }
//                            .create()
//                            .show()
                    }

                    R.id.editBlog -> {
                        thread {
                            Looper.prepare()
                            Toast.makeText(context, "还没写", Toast.LENGTH_SHORT).show()
                            Looper.loop()
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
                BlogItemRepository.get().updateBlogItem(blogItem)
            }
            if (blogItem.isStar) {
                Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show()
            } else {
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
        }
    }
}

