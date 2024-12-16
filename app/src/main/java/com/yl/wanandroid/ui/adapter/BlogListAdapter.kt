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
import com.yl.wanandroid.databinding.ItemBlogViewBinding
import com.yl.wanandroid.model.ArticleItemData
import com.yl.wanandroid.ui.activity.WebViewActivity
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * @description: 博客列表适配器 使用DataBinding绑定条目
 * @author YL Chen
 * @date 2024/10/20 18:33
 * @version 1.0
 */
class BlogListAdapter @Inject constructor(@ActivityContext val context: Context) :
    RecyclerView.Adapter<BlogListAdapter.MyViewHolder>() {



    private var datas: MutableList<ArticleItemData> = mutableListOf()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    //暴露方法给外界设置数据
    fun setData(recommendBlogDatas: List<ArticleItemData>) {
        this.datas.clear()//清空数据
        this.datas.addAll(recommendBlogDatas)
        notifyDataSetChanged()
    }

    //暴露方法给外界添加数据
    fun addData(recommendBlogDatas: List<ArticleItemData>){
        val oldIndex = datas.size - 1
        this.datas.addAll(recommendBlogDatas)
        notifyItemRangeChanged(oldIndex,recommendBlogDatas.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //list条目绑定databinding
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemBlogViewBinding>(
            inflater,
            R.layout.item_blog_view,
            parent,
            false
        )
        return MyViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    //使用DataBinding绑定数据
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemBlogViewBinding>(holder.itemView)
        binding?.itemData = datas[position]
        binding?.executePendingBindings()
        //设置条目点击跳转
        holder.itemView.setOnClickListener {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(Constant.TO_WEB_URL, datas[position].link)//携带数据跳转
            context.startActivity(intent)
        }
    }
}