package com.yl.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yl.wanandroid.R
import com.yl.wanandroid.model.RecommendBlogData
import javax.inject.Inject

/**
 * @description: 首页推荐列表适配器 TODO
 * @author YL Chen
 * @date 2024/10/20 18:33
 * @version 1.0
 */
class RecommendListAdapter @Inject constructor() :
    RecyclerView.Adapter<RecommendListAdapter.MyViewHolder>() {

    private var mRecommendBlogDatas: List<RecommendBlogData> = mutableListOf()

    class MyViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        var collection: ImageView? = itemView.findViewById(R.id.collection)
        var classify: TextView? = itemView.findViewById(R.id.classify)
        var author: TextView? = itemView.findViewById(R.id.author)
        var TagAuthorOrShareUser: TextView? = itemView.findViewById(R.id.tv_author_or_shareUser)
        var publishTime: TextView? = itemView.findViewById(R.id.publish_time)
        var title: TextView? = itemView.findViewById(R.id.title)

    }

    //暴露方法给外界设置数据
    fun setData(recommendBlogDatas: List<RecommendBlogData>) {
        this.mRecommendBlogDatas = recommendBlogDatas
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_blog_view, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mRecommendBlogDatas.size
    }

    //绑定数据
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title?.text = mRecommendBlogDatas[position].title
        holder.publishTime?.text = mRecommendBlogDatas[position].niceShareDate
        holder.TagAuthorOrShareUser?.text =
            if (mRecommendBlogDatas[position].author.isBlank()) "分享者：" else "作者："
        holder.author?.text =
            if (mRecommendBlogDatas[position].author.isBlank()) mRecommendBlogDatas[position].shareUser else mRecommendBlogDatas[position].author
        holder.classify?.text = mRecommendBlogDatas[position].superChapterName
        holder.collection?.setImageResource(if (mRecommendBlogDatas[position].collect) R.drawable.iv_collection else R.drawable.iv_no_collection)
    }
}