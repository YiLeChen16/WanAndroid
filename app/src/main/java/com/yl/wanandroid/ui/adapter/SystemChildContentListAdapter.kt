package com.yl.wanandroid.ui.adapter

import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseRecyclerViewAdapter
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
class SystemChildContentListAdapter @Inject constructor() :
    BaseRecyclerViewAdapter<SystemDataBeanItem, ItemSystemBinding>(R.layout.item_system) {


    private var listener: OnKeyWordClickListener? = null
    private var flowLayout: CommonFlowLayoutView? = null


    override fun bindItemData(binding: ItemSystemBinding?, position: Int) {
        binding?.systemDataBeanItem = datas[position]
        flowLayout = binding?.root?.findViewById(R.id.flow_layout)
        flowLayout?.setData(datas[position].children)
    }

    override fun setListener(
        holder: MyViewHolder,
        binding: ItemSystemBinding?,
        position: Int
    ) {
        //为item中的每个关键词设置点击事件
        flowLayout?.setOnItemClickListener(object :
            CommonFlowLayoutView.OnItemClickListener {
            override fun onKeyWordClick(child: Children) {
                listener?.onKeyWordClick(child.id, datas[position].children)
            }
        })
    }


    //暴露方法给外界设置监听回调接口
    fun setOnKeyWordClickListener(listener: OnKeyWordClickListener) {
        this.listener = listener
    }


    interface OnKeyWordClickListener {
        /**
         * 关键词被点击时回调
         * @param id Int 关键词对应的页面id
         * @param children List<Children> 全部页面的数据
         */
        fun onKeyWordClick(id: Int, children: List<Children>)
    }
}