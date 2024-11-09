package com.yl.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yl.wanandroid.R
import com.yl.wanandroid.databinding.ItemRecommendSearchViewBinding
import com.yl.wanandroid.model.SearchHotKeyDataBean
import javax.inject.Inject

/**
 * @description: 推荐搜索关键词列表适配器
 * @author YL Chen
 * @date 2024/11/8 21:30
 * @version 1.0
 */
class RecommendSearchListAdapter @Inject constructor():RecyclerView.Adapter<RecommendSearchListAdapter.MyViewHolder>() {

    private var listener: OnItemClickListener? = null
    private  var data:MutableList<SearchHotKeyDataBean> = mutableListOf()

    class MyViewHolder(val binding:ViewBinding):RecyclerView.ViewHolder(binding.root)


    //绑定数据
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemRecommendSearchViewBinding>(holder.itemView)
        binding?.recommendSearchKeyword = data[position].name
        binding?.executePendingBindings()//需执行此句以更新界面,否则界面会闪烁
        //为条目设置点击事件
        holder.itemView.setOnClickListener {
            //TODO::点击搜索
            listener?.onItemClick(data[position].name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemRecommendSearchViewBinding>(
            inflater,
            R.layout.item_recommend_search_view,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    //提供方法给外界设置数据
    fun setData(data:List<SearchHotKeyDataBean>){
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    //提供方法给外部设置监听事件
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(k: String)//条目被点击
    }
}