package com.chiibeii.ZiYanZiYu.ui.adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.chiibeii.ZiYanZiYu.R
import com.chiibeii.ZiYanZiYu.ui.activity.WriteBlog
import com.chiibeii.ZiYanZiYu.logic.entity.BlogItem
import com.chiibeii.ZiYanZiYu.logic.repository.BlogItemRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.concurrent.thread

class MyDraftAdapter(
    private val context: Context?,
    private val blogItemList: List<BlogItem>
) :
    RecyclerView.Adapter<MyDraftAdapter.MyDraftViewHolder>() {

    inner class MyDraftViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val blogContent: TextView = view.findViewById(R.id.blogContentInDraft)
        private val blogTime: TextView = itemView.findViewById(R.id.blogTimeInDraft)
        private val deleteDraft: View? = view.findViewById(R.id.deleteDraft)
        private val editDraft: View? = view.findViewById(R.id.editDraft)
        private val isReblogInDraft:View? = view.findViewById(R.id.isReblogInDraft)

        fun bind(blogItem: BlogItem) {

            blogContent.text = blogItem.blogContent
            blogTime.text = "最后编辑于" + blogItem.blogTime
            isReblogInDraft?.visibility = if (blogItem.reblogFrom != 0L) View.VISIBLE else View.GONE

            // delete 按钮的点击事件
            deleteDraft?.setOnClickListener {
                // 获取点击的位置
                val blogItem = blogItemList[absoluteAdapterPosition]
                MaterialAlertDialogBuilder(context!!)
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

            // 编辑 按钮
            editDraft?.setOnClickListener{
                val intent = Intent(context, WriteBlog::class.java)
                // 传一些信息出去！
                intent.putExtra("TheBlogItem",blogItem.toString())
                intent.putExtra("FromWhere","FromDraft")
                Log.d(TAG, "editDrafteditDraft:$blogItem ")
                context?.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyDraftAdapter.MyDraftViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.draft_item, parent, false)
        return MyDraftViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyDraftAdapter.MyDraftViewHolder,
        position: Int
    ) {
        val blogItem = blogItemList[position]
        holder.bind(blogItem)
    }

    override fun getItemCount(): Int = blogItemList.size
}