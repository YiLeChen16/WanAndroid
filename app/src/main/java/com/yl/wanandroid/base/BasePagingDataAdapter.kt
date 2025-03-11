package com.yl.wanandroid.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @description: PagingAdapter的基类
 * @author YL Chen
 * @date 2025/3/11 20:53
 * @version 1.0
 */
abstract class BasePagingDataAdapter<ItemData : Any, ItemViewBinding : ViewDataBinding>(
    @LayoutRes private var itemLayoutId: Int,
    comparator: DiffUtil.ItemCallback<ItemData>
) : PagingDataAdapter<ItemData, BasePagingDataAdapter.ViewHolder>(comparator) {
    class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemViewBinding>(holder.itemView)
        bindItemData(binding, position)
        binding?.executePendingBindings()//需执行此句以更新界面,否则界面会闪烁
        //设置条目监听事件
        setListener(holder, binding, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //list条目绑定databinding
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemViewBinding>(
            inflater,
            itemLayoutId,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    //为子类的ViewBinding布局变量绑定数据
    abstract fun bindItemData(binding: ItemViewBinding?, position: Int)


    open fun setListener(holder: ViewHolder, binding: ItemViewBinding?, position: Int){}

}