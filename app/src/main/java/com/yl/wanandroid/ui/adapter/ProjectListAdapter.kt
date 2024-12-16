package com.yl.wanandroid.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yl.wanandroid.Constant
import com.yl.wanandroid.R
import com.yl.wanandroid.databinding.ItemProjectViewBinding
import com.yl.wanandroid.model.ArticleItemData
import com.yl.wanandroid.ui.activity.WebViewActivity
import com.yl.wanandroid.utils.LogUtils
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * @description: ProjectTabFragment页面列表适配器
 * @author YL Chen
 * @date 2024/12/15 19:11
 * @version 1.0
 */
class ProjectListAdapter @Inject constructor(@ActivityContext val context: Context) :
    RecyclerView.Adapter<ProjectListAdapter.MyViewHolder>() {
    private var data: MutableList<ArticleItemData> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemProjectViewBinding>(
            inflater,
            R.layout.item_project_view,
            parent,
            false
        )
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemProjectViewBinding>(holder.itemView)
        binding?.itemData = data[position]//为布局声明变量绑定数据
        binding?.executePendingBindings()//通知更新
        //设置条目点击事件
        holder.itemView.setOnClickListener {
            //点击跳转到WebActivity
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(Constant.TO_WEB_URL, data[position].link)//携带数据跳转
            context.startActivity(intent)
        }
        //设置加载网络图片
        val picUrl: String = data[position].envelopePic
        LogUtils.d(this, "picUrl-->$picUrl")
        if (binding?.img != null) {
            val options: RequestOptions = RequestOptions()
                .placeholder(R.drawable.img_loading)//图片加载出来前，显示的图片
                .fallback(R.drawable.img_blank) //url为空的时候,显示的图片
                .error(R.drawable.img_load_failure)//图片加载失败后，显示的图片
            Glide.with(context).load(picUrl).apply(options).into(binding.img)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    //暴露方法给外界设置数据
    fun setData(data: List<ArticleItemData>) {
        this.data = data.toMutableList()
        notifyDataSetChanged()
    }

    //暴露方法给外界添加数据
    fun addData(newData: List<ArticleItemData>){
        val oldIndex = this.data.size - 1
        this.data.addAll(newData)
        notifyItemRangeChanged(oldIndex,data.size)
    }

    class MyViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)
}