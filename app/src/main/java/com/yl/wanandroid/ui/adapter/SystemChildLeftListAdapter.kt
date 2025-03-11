package com.yl.wanandroid.ui.adapter

import android.view.View
import androidx.core.content.ContextCompat
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseRecyclerViewAdapter
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
    BaseRecyclerViewAdapter<SystemDataBeanItem,ItemSystemChildLeftTabBinding>(R.layout.item_system_child_left_tab) {
    private var clickState: Boolean = false
    private lateinit var lastSelectedView: View
    private var selectedPosition: Int = 0
    private var listener: OnTabTitleClickListener? = null

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


    override fun bindItemData(binding: ItemSystemChildLeftTabBinding?, position: Int) {
        binding?.systemDataBeanItem = datas[position]
    }

    override fun setListener(
        holder: MyViewHolder,
        binding: ItemSystemChildLeftTabBinding?,
        position: Int
    ) {
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