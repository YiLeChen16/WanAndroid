package com.yl.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yl.wanandroid.R
import com.yl.wanandroid.databinding.ItemBlogViewBinding
import com.yl.wanandroid.model.ArticleItemData
import javax.inject.Inject

/**
 * @description: TODO:收藏监听回调
 * @author YL Chen
 * @date 2025/3/8 22:10
 * @version 1.0
 */
class ShareListAdapter @Inject constructor() :
    PagingDataAdapter<ArticleItemData, ShareListAdapter.ViewHolder>(COMPARATOR) {
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<ArticleItemData>() {
            override fun areItemsTheSame(oldItem: ArticleItemData, newItem: ArticleItemData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ArticleItemData, newItem: ArticleItemData): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val binding = DataBindingUtil.getBinding<ItemBlogViewBinding>(holder.itemView)
        binding?.itemData = item
        binding?.executePendingBindings()//需执行此句以更新界面,否则界面会闪烁
        //为item设置点击事件

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemBlogViewBinding>(
            inflater,
            R.layout.item_blog_view,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    /*fun updateCollectionState(id: Int) {
        for ((index, data) in datas.withIndex()) {
            if (data.id == id) {
                LogUtils.d(this, "data.id == id")
                datas[index].collect = !datas[index].collect
                notifyItemChanged(index)
                return
            }
        }
    }*/

}