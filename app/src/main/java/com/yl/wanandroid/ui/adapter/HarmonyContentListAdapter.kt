package com.yl.wanandroid.ui.adapter

import android.content.Context
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseRecyclerViewAdapter
import com.yl.wanandroid.databinding.ItemHarmonyViewBinding
import com.yl.wanandroid.model.ArticleItemData
import com.yl.wanandroid.ui.activity.WebViewActivity
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * @description: 首页鸿蒙页内容列表适配器
 * @author YL Chen
 * @date 2024/11/16 18:14
 * @version 1.0
 */
class HarmonyContentListAdapter @Inject constructor(@ActivityContext val context: Context) :BaseRecyclerViewAdapter<ArticleItemData,ItemHarmonyViewBinding>(R.layout.item_harmony_view){

    override fun bindItemData(binding: ItemHarmonyViewBinding?, position: Int) {
        binding?.article = datas[position]
    }

    override fun setListener(
        holder: MyViewHolder,
        binding: ItemHarmonyViewBinding?,
        position: Int
    ) {
        //设置条目点击事件
        holder.itemView.setOnClickListener {
            //点击跳转到WebActivity
            WebViewActivity.start(context,datas[position].link)
        }
    }

}