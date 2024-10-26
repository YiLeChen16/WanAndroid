package com.yl.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yl.wanandroid.R
import com.yl.wanandroid.databinding.ItemBlogViewBinding
import com.yl.wanandroid.model.RecommendBlogData
import javax.inject.Inject

/**
 * @description: 首页推荐列表适配器
 * @author YL Chen
 * @date 2024/10/20 18:33
 * @version 1.0
 */
class RecommendListAdapter @Inject constructor() :
    RecyclerView.Adapter<RecommendListAdapter.MyViewHolder>() {

    private var mRecommendBlogDatas: List<RecommendBlogData> = mutableListOf()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    //暴露方法给外界设置数据
    fun setData(recommendBlogDatas: List<RecommendBlogData>) {
        this.mRecommendBlogDatas = recommendBlogDatas
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //list条目绑定databinding
        val inflater = LayoutInflater.from(parent.context)
        val itemBlogViewBinding = DataBindingUtil.inflate<ItemBlogViewBinding>(
            inflater,
            R.layout.item_blog_view,
            parent,
            false
        )
        return MyViewHolder(itemBlogViewBinding.root)
    }

    override fun getItemCount(): Int {
        return mRecommendBlogDatas.size
    }

    //绑定数据
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemBlogViewBinding>(holder.itemView)
        binding?.recommendBlogData = mRecommendBlogDatas[position]
    }
}