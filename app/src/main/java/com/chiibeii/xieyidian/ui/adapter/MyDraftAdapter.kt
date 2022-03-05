package com.chiibeii.xieyidian.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.chiibeii.xieyidian.R
import com.chiibeii.xieyidian.logic.entity.BlogItem
import com.chiibeii.xieyidian.logic.repository.BlogItemRepository
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class MyDraftAdapter(
    private val context: Context?,
    private val blogItemList: List<BlogItem>
) :
    RecyclerView.Adapter<MyDraftAdapter.MyDraftViewHolder>() {

    inner class MyDraftViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val blogContent: TextView = view.findViewById(R.id.blogContentInDraft)
        val blogTime: TextView = itemView.findViewById(R.id.blogTimeInDraft)
        val deleteDraft: View? = view.findViewById(R.id.deleteDraft)
        val editDraft: View? = view.findViewById(R.id.editDraft)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyDraftAdapter.MyDraftViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.draft_item, parent, false)
        val draftViewHolder = MyDraftViewHolder(view)


        // more按钮的点击事件
        draftViewHolder.deleteDraft?.setOnClickListener {
            // 获取点击的位置
            val position = draftViewHolder.absoluteAdapterPosition
            val blogItem = blogItemList[position]
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

        return draftViewHolder
    }

    override fun onBindViewHolder(
        holder: MyDraftAdapter.MyDraftViewHolder,
        position: Int
    ) {
        val blogItem = blogItemList[position]
        holder.blogContent.text = blogItem.blogContent
        holder.blogTime.text = "最后编辑于" + blogItem.blogTime
    }

    override fun getItemCount(): Int = blogItemList.size
}