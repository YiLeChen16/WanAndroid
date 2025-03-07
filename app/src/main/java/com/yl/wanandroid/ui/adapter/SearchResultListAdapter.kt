package com.yl.wanandroid.ui.adapter

import android.content.Context
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseRecyclerViewAdapter
import com.yl.wanandroid.databinding.ItemSearchResultBlogViewBinding
import com.yl.wanandroid.model.ArticleItemData
import com.yl.wanandroid.model.CollectionEvent
import com.yl.wanandroid.ui.activity.WebViewActivity
import com.yl.wanandroid.utils.LogUtils
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
/**
 * @description: 搜索结果列表适配器 使用Inject注入
 * @author YL Chen
 * @date 2024/11/1 11:18
 * @version 1.0
 */
class SearchResultListAdapter @Inject constructor(@ActivityContext val context: Context) :
    BaseRecyclerViewAdapter<ArticleItemData, ItemSearchResultBlogViewBinding>(R.layout.item_search_result_blog_view) {

    override fun setViewBindingVariable(binding: ItemSearchResultBlogViewBinding?, position: Int) {
        binding?.searchData = datas[position]
    }

    override fun setListener(
        holder: MyViewHolder,
        binding: ItemSearchResultBlogViewBinding?,
        position: Int
    ) {
        //设置条目点击跳转
        holder.itemView.setOnClickListener {
            //跳转到webViewActivity界面
            WebViewActivity.start(context,datas[position].link)
        }
        //收藏按钮初始化
        if (datas[position].collect) {
            //已收藏
            binding?.collection?.setImageResource(R.drawable.star)
        } else {
            //未收藏
            binding?.collection?.setImageResource(R.drawable.no_star)
        }
        //收藏按钮点击事件
        binding?.collection?.setOnClickListener {
            var originId = datas[position].originId
            if (originId == 0) {
                originId = -1
            }
            val where =
                (datas[position].adminAdd == null) && (datas[position].apkLink == null)//因为收藏页面返回的json数据没有这两个字段
            //通知监听者
            onCollectionEventListener?.onCollectionEvent(
                CollectionEvent(
                    datas[position].id,
                    originId,
                    datas[position].collect,
                    where
                )
            )
        }
    }

    fun updateCollectionState(id: Int) {
        for ((index, data) in datas.withIndex()) {
            if (data.id == id) {
                LogUtils.d(this, "data.id == id")
                datas[index].collect = !datas[index].collect
                notifyItemChanged(index)
                return
            }
        }
    }

    /**
     * 对外界提供设置接口的方法
     * @param listener OnCollectionEventListener
     */
    fun setOnCollectionEventListener(listener: OnCollectionEventListener) {
        onCollectionEventListener = listener
    }

    private var onCollectionEventListener: OnCollectionEventListener? = null
}