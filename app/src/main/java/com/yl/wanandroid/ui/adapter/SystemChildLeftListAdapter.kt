package com.yl.wanandroid.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yl.wanandroid.R
import com.yl.wanandroid.databinding.ItemSystemChildLeftTabBinding
import com.yl.wanandroid.model.SystemDataBeanItem
import javax.inject.Inject

/**
 * @description: 左侧RecyclerView适配器
 * @author YL Chen
 * @date 2024/12/19 20:32
 * @version 1.0
 */
class SystemChildLeftListAdapter @Inject constructor() :
    RecyclerView.Adapter<SystemChildLeftListAdapter.MyViewHolder>() {
    private var clickState: Boolean = false
    private lateinit var lastSelectedView: View
    private var selectedPosition: Int = 0
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
        this.data.clear()
        this.data = data
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val binding = DataBindingUtil.getBinding<ItemSystemChildLeftTabBinding>(holder.itemView)
        binding?.systemDataBeanItem = data[position]
        holder.itemView.tag = position
        if (position != selectedPosition) {
            holder.itemView.background =
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.shape_unpress_item)
        } else {
            holder.itemView.background =
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.shape_press_item)
            lastSelectedView = holder.itemView
        }
        holder.itemView.setOnClickListener {
            synchronized(this) {
                selectedPosition = position
                changeSelected(holder.itemView)
                clickState = true
                listener?.onTabTitleClick(position)
            }
        }
    }

    //判断是否为点击状态
    fun isClickState(): Boolean {
        return this.clickState
    }

    //设置是否为点击状态
    fun setClickState(b: Boolean) {
        this.clickState = b
    }

    //切换当前条目选中状态
    fun changeSelected(itemView: View) {
        lastSelectedView.background =
            ContextCompat.getDrawable(lastSelectedView.context, R.drawable.shape_unpress_item)
        itemView.background =
            ContextCompat.getDrawable(itemView.context, R.drawable.shape_press_item)
        lastSelectedView = itemView

    }

    override fun getItemCount(): Int {
        return data.size
    }


    fun setOnTabTitleClickListener(listener: OnTabTitleClickListener) {
        this.listener = listener
    }

    //自定义接口回调
    interface OnTabTitleClickListener {
        /**
         *  左侧标题列表被点击
         * @param position Int 当前分类位置
         */
        fun onTabTitleClick(position: Int)
    }
}