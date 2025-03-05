package com.yl.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yl.wanandroid.R
import com.yl.wanandroid.databinding.ItemRankListBinding
import com.yl.wanandroid.model.RankItemData
import javax.inject.Inject

/**
 * @description: 排行榜列表适配器
 * @author YL Chen
 * @date 2025/3/5 13:47
 * @version 1.0
 */
class RankListAdapter @Inject constructor() :
    PagingDataAdapter<RankItemData, RankListAdapter.ViewHolder>(COMPARATOR) {
    class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<RankItemData>() {
            override fun areItemsTheSame(oldItem: RankItemData, newItem: RankItemData): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: RankItemData, newItem: RankItemData): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val binding = DataBindingUtil.getBinding<ItemRankListBinding>(holder.itemView)
        binding?.rankItemData = item
        binding?.executePendingBindings()//需执行此句以更新界面,否则界面会闪烁
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemRankListBinding>(
            inflater,
            R.layout.item_rank_list,
            parent,
            false
        )
        return ViewHolder(binding)
    }
}