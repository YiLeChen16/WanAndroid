package com.yl.wanandroid.ui.adapter.my

import androidx.recyclerview.widget.DiffUtil
import com.yl.wanandroid.R
import com.yl.wanandroid.base.adapter.BasePagingDataAdapter
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
    BasePagingDataAdapter<RankItemData, ItemRankListBinding>(R.layout.item_rank_list, COMPARATOR) {

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

    override fun bindItemData(binding: ItemRankListBinding?, position: Int) {
        binding?.rankItemData = getItem(position)
    }
}