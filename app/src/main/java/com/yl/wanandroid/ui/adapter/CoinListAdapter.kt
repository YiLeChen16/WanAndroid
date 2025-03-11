package com.yl.wanandroid.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BasePagingDataAdapter
import com.yl.wanandroid.databinding.ItemCoinListBinding
import com.yl.wanandroid.model.CoinData
import javax.inject.Inject

/**
 * @description: 积分列表Paging适配器
 * @author YL Chen
 * @date 2025/3/4 22:05
 * @version 1.0
 */
class CoinListAdapter @Inject constructor() :
    BasePagingDataAdapter<CoinData, ItemCoinListBinding>(R.layout.item_coin_list, COMPARATOR) {


    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<CoinData>() {
            override fun areItemsTheSame(oldItem: CoinData, newItem: CoinData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CoinData, newItem: CoinData): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun bindItemData(binding: ItemCoinListBinding?, position: Int) {
        binding?.coinHistory = getItem(position)
    }

}