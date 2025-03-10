package com.yl.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yl.wanandroid.R
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
    PagingDataAdapter<CoinData, CoinListAdapter.ViewHolder>(COMPARATOR) {
    class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    }

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val binding = DataBindingUtil.getBinding<ItemCoinListBinding>(holder.itemView)
        binding?.coinHistory = item
        binding?.executePendingBindings()//需执行此句以更新界面,否则界面会闪烁

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemCoinListBinding>(
            inflater,
            R.layout.item_coin_list,
            parent,
            false
        )
        return ViewHolder(binding)
    }

}