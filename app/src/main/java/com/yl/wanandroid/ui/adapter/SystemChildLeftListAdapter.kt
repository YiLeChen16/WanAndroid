package com.yl.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yl.wanandroid.R
import com.yl.wanandroid.databinding.ItemSystemChildLeftTabBinding
import com.yl.wanandroid.model.Children
import com.yl.wanandroid.model.SystemDataBeanItem
import javax.inject.Inject

/**
 * @description: 左侧RecyclerView适配器
 * @author YL Chen
 * @date 2024/12/19 20:32
 * @version 1.0
 */
class SystemChildLeftListAdapter @Inject constructor() : RecyclerView.Adapter<SystemChildLeftListAdapter.MyViewHolder>() {
    private var listener: OnTabTitleClickListener? = null
    private var data: MutableList<SystemDataBeanItem> = mutableListOf()

    class MyViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemSystemChildLeftTabBinding>(
            inflater,
            R.layout.item_system_child_left_tab,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    fun setData(data: MutableList<SystemDataBeanItem>) {
        this.data = data
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemSystemChildLeftTabBinding>(holder.itemView)
        binding?.systemDataBeanItem = data[position]
        holder.itemView.setOnClickListener {
            listener?.onTabTitleClick(data[position].children)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setOnTabTitleClickListener(listener:OnTabTitleClickListener){
        this.listener = listener
    }

    //自定义接口回调
    interface OnTabTitleClickListener{
        /**
         * 左侧标题列表被点击
         * @param children List<Children> 返回对应的children数据
         */
        fun onTabTitleClick(children:List<Children>)
    }

}