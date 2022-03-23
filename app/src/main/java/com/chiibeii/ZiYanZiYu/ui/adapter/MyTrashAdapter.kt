package com.chiibeii.ZiYanZiYu.ui.adapter

import android.content.Context
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.logic.entity.BlogItem
import com.chiibeii.ZiYanZiYu.logic.repository.BlogItemRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.concurrent.thread

class MyTrashAdapter(
    private val context: Context?,
    private val blogItemList: List<BlogItem>
) :
    RecyclerView.Adapter<MyTrashAdapter.MyTrashViewHolder>() {

    inner class MyTrashViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val blogContent: TextView = view.findViewById(R.id.blogContentInTrash)
        private val deleteTrash: View? = view.findViewById(R.id.deleteTrash)
        private val restoreTrash: View? = view.findViewById(R.id.restoreTrash)

        private val isReblogInTrash:View? = view.findViewById(R.id.isReblogInTrash)

        fun bind(blogItem: BlogItem) {

            blogContent.text = blogItem.blogContent
            isReblogInTrash?.visibility = if (blogItem.reblogFrom != 0L) View.VISIBLE else View.GONE

            // 这里才是真正的删东西的地方
            // delete 按钮的点击事件
            deleteTrash?.setOnClickListener {
                // 获取点击的位置
                val blogItem = blogItemList[absoluteAdapterPosition]
                MaterialAlertDialogBuilder(context!!)
                    .setMessage("彻底删除操作将会无法撤回！")
                    .setTitle("注意！")
                    .setPositiveButton("仍要删除") { _, _ ->
                        thread {
                            BlogItemRepository.get().deleteOneBlogItem(blogItem)
                        }
                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("取消删除") { _, _ ->
                        Toast.makeText(context, "未删除", Toast.LENGTH_SHORT).show()
                    }
                    .create()
                    .show()
            }

            restoreTrash?.setOnClickListener{
                thread {
                    // 本身isDelete恢复
                    blogItem.isDelete = false
                    // 查儿子，把儿子的 isReblogFromDeleted 全部也改为 false
                    for (k in blogItem.reblogTo) {
                        // 必要的判空！！不要相信编译器
                        if(BlogItemRepository.get().loadThisBlogItem(k) != null){
                            val son = BlogItemRepository.get().loadThisBlogItem(k)
                            son.isReblogFromDeleted = false
                            BlogItemRepository.get().updateBlogItem(son)
                        }
                    }
                    BlogItemRepository.get().updateBlogItem(blogItem)
                    Looper.prepare()
                    Toast.makeText(context, "恢复成功", Toast.LENGTH_SHORT).show()
                    Looper.loop()
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyTrashAdapter.MyTrashViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.trash_item, parent, false)
        return MyTrashViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyTrashAdapter.MyTrashViewHolder,
        position: Int
    ) {
        val blogItem = blogItemList[position]
        holder.bind(blogItem)
    }

    override fun getItemCount(): Int = blogItemList.size

}