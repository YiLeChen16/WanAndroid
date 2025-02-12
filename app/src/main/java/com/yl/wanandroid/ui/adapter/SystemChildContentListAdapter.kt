package com.yl.wanandroid.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yl.wanandroid.R
import com.yl.wanandroid.databinding.ItemSystemBinding
import com.yl.wanandroid.model.Children
import com.yl.wanandroid.model.SystemDataBeanItem
import com.yl.wanandroid.ui.custom.CommonFlowLayoutView
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

    private  var listener: OnKeyWordClickListener? = null
    private var data: MutableList<SystemDataBeanItem> = mutableListOf()


    class MyViewHolder(binding: ViewBinding):RecyclerView.ViewHolder(binding.root){
        var flowLayout:CommonFlowLayoutView? = null
        init {
            flowLayout = binding.root.findViewById(R.id.flow_layout)
        }
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val binding = DataBindingUtil.getBinding<ItemSystemBinding>(holder.itemView)
        binding?.systemDataBeanItem = data[position]
        holder.flowLayout?.setData(data[position].children)
        //为item中的每个关键词设置点击事件
        holder.flowLayout?.setOnItemClickListener(object :CommonFlowLayoutView.OnItemClickListener{
            override fun onKeyWordClick(child: Children) {
                listener?.onKeyWordClick(child.id,data[position].children)
            }

        })
    }

    override fun getItemCount(): Int {
        return data.size
    }

    //暴露接口给外界设置数据
    fun setData(data: MutableList<SystemDataBeanItem>) {
        this.data = data
    }


    //暴露方法给外界设置监听回调接口
    fun setOnKeyWordClickListener(listener: OnKeyWordClickListener){
        this.listener = listener
    }


    interface OnKeyWordClickListener{
        /**
         * 关键词被点击时回调
         * @param id Int 关键词对应的页面id
         * @param children List<Children> 全部页面的数据
         */
        fun onKeyWordClick(id:Int,children:List<Children>)
    }
}