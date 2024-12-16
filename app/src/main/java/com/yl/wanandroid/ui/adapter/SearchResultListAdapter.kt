package com.yl.wanandroid.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yl.wanandroid.Constant
import com.yl.wanandroid.R
import com.yl.wanandroid.databinding.ItemSearchResultBlogViewBinding
import com.yl.wanandroid.model.ArticleItemData
import com.yl.wanandroid.ui.activity.WebViewActivity
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * @description: 搜索结果列表适配器 使用Inject注入
 * @author YL Chen
 * @date 2024/11/1 11:18
 * @version 1.0
 */
class SearchResultListAdapter @Inject constructor(@ActivityContext val context: Context) :
    RecyclerView.Adapter<SearchResultListAdapter.MyViewHolder>() {
    //搜索结果数据
    private var mSearchResultListDatas: MutableList<ArticleItemData> = mutableListOf()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //list条目绑定databinding
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemSearchResultBlogViewBinding>(
            inflater,
            R.layout.item_search_result_blog_view,
            parent,
            false
        )
        return MyViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return mSearchResultListDatas.size
    }

    //暴露方法给外界设置数据
    fun setData(data: List<ArticleItemData>) {
        this.mSearchResultListDatas.clear()
        this.mSearchResultListDatas.addAll(data)
        notifyDataSetChanged()
    }


    //暴露方法给外界添加数据
    fun addData(data: List<ArticleItemData>) {
        val oldIndex = mSearchResultListDatas.size - 1
        this.mSearchResultListDatas.addAll(data)
        notifyItemRangeChanged(oldIndex,mSearchResultListDatas.size)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //Databinding绑定数据
        val binding = DataBindingUtil.getBinding<ItemSearchResultBlogViewBinding>(holder.itemView)
        binding?.searchData = mSearchResultListDatas[position]
        binding?.executePendingBindings()//需执行此句以更新界面,否则界面会闪烁
        //设置条目点击跳转
        holder.itemView.setOnClickListener {
            //跳转到webViewActivity界面
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(Constant.TO_WEB_URL, mSearchResultListDatas[position].link)//携带数据跳转
            context.startActivity(intent)
        }
    }
}