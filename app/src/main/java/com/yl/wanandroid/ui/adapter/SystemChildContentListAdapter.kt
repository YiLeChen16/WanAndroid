package com.yl.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yl.wanandroid.R
import com.yl.wanandroid.databinding.ItemSystemBinding
import com.yl.wanandroid.model.SystemDataBeanItem
import com.yl.wanandroid.ui.custom.CommonFlowLayoutView
import com.yl.wanandroid.utils.LogUtils
import javax.inject.Inject

/**
 * @description: 体系右侧内容列表适配器
 * @author YL Chen
 * @date 2025/1/23 14:56
 * @version 1.0
 */
class SystemChildContentListAdapter @Inject constructor():RecyclerView.Adapter<SystemChildContentListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemSystemBinding>(
            inflater,
            R.layout.item_system,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    private var data: MutableList<SystemDataBeanItem> = mutableListOf()


    class MyViewHolder(binding: ViewBinding):RecyclerView.ViewHolder(binding.root){
        var flowLayout:CommonFlowLayoutView? = null
        init {
            flowLayout = binding.root.findViewById(R.id.flow_layout)
        }
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val binding = DataBindingUtil.getBinding<ItemSystemBinding>(holder.itemView)
        binding?.systemDataBeanItem = data[position]
        holder.flowLayout?.setData(data[position].children)
        holder.itemView.setOnClickListener {
            LogUtils.d(this,"flow item is clicked!")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    //暴露接口给外界设置数据
    fun setData(data: MutableList<SystemDataBeanItem>) {
        this.data = data
    }
}