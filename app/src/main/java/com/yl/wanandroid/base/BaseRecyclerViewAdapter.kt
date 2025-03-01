package com.yl.wanandroid.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding

/**
 * @description: RecyclerView适配器基类
 * @author YL Chen
 * @date 2025/2/28 22:40
 * @version 1.0
 */
abstract class BaseRecyclerViewAdapter<ItemData,ItemViewBinding:ViewDataBinding>(private var itemLayoutId: Int) : RecyclerView.Adapter<BaseRecyclerViewAdapter.MyViewHolder>() {
    class MyViewHolder(binding: ViewBinding) : ViewHolder(binding.root)

    var datas: MutableList<ItemData> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        //list条目绑定databinding
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemViewBinding>(
            inflater,
            itemLayoutId,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemViewBinding>(holder.itemView)
        setViewBindingVariable(binding,position)
        binding?.executePendingBindings()//需执行此句以更新界面,否则界面会闪烁
        //设置条目监听事件
        setListener(holder,binding,position)
    }

    abstract fun setListener(holder: MyViewHolder,binding: ItemViewBinding?,position:Int)

    //为子类的ViewBinding布局变量绑定数据
    abstract fun setViewBindingVariable(binding: ItemViewBinding?,position: Int)

    //暴露方法给外界设置数据
    @SuppressLint("NotifyDataSetChanged")
    fun setData(itemDatas: List<ItemData>) {
        this.datas.clear()//清空数据
        this.datas.addAll(itemDatas)
        notifyDataSetChanged()
    }

    //暴露方法给外界添加数据
    fun addData(itemDatas: List<ItemData>) {
        val oldIndex = datas.size - 1
        this.datas.addAll(itemDatas)
        notifyItemRangeChanged(oldIndex, itemDatas.size)
    }
}