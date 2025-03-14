package com.yl.wanandroid.ui.adapter.search

import com.yl.wanandroid.R
import com.yl.wanandroid.base.adapter.BaseRecyclerViewAdapter
import com.yl.wanandroid.databinding.ItemRecommendSearchViewBinding
import com.yl.wanandroid.model.SearchHotKeyDataBean
import javax.inject.Inject

/**
 * @description: 推荐搜索关键词列表适配器
 * @author YL Chen
 * @date 2024/11/8 21:30
 * @version 1.0
 */
class RecommendSearchListAdapter @Inject constructor() :
    BaseRecyclerViewAdapter<SearchHotKeyDataBean, ItemRecommendSearchViewBinding>(R.layout.item_recommend_search_view) {

    private var listener: OnItemClickListener? = null

    //提供方法给外部设置监听事件
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(k: String)//条目被点击
    }

    override fun setListener(
        holder: MyViewHolder,
        binding: ItemRecommendSearchViewBinding?,
        position: Int
    ) {
        //为条目设置点击事件
        holder.itemView.setOnClickListener {
            //点击搜索
            listener?.onItemClick(datas[position].name)
        }
    }

    override fun bindItemData(binding: ItemRecommendSearchViewBinding?, position: Int) {
        binding?.recommendSearchKeyword = datas[position].name
    }
}