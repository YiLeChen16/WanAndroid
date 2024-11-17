package com.yl.wanandroid.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yl.wanandroid.Constant
import com.yl.wanandroid.R
import com.yl.wanandroid.databinding.ItemHarmonyViewBinding
import com.yl.wanandroid.model.ItemData
import com.yl.wanandroid.ui.activity.WebViewActivity
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * @description: 首页鸿蒙页内容列表适配器
 * @author YL Chen
 * @date 2024/11/16 18:14
 * @version 1.0
 */
class HarmonyContentListAdapter @Inject constructor(@ActivityContext val context: Context) :RecyclerView.Adapter<HarmonyContentListAdapter.MyViewHolder>(){
    private val data: MutableList<ItemData> = mutableListOf()

    class MyViewHolder(binding:ViewBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemHarmonyViewBinding>(
            inflater,
            R.layout.item_harmony_view,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemHarmonyViewBinding>(holder.itemView)
        binding?.article = data[position]//为布局声明变量绑定数据
        binding?.executePendingBindings()//通知更新
        //设置条目点击事件
        holder.itemView.setOnClickListener {
            //点击跳转到WebActivity
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(Constant.toWebUrlKey, data[position].link)//携带数据跳转
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    //暴露方法给外界设置数据
    fun setData(data: List<ItemData>) {
        this.data.clear()//清空数据
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}