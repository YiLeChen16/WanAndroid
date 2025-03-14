package com.yl.wanandroid.ui.adapter.my

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import com.yl.wanandroid.R
import com.yl.wanandroid.base.adapter.BasePagingDataAdapter
import com.yl.wanandroid.databinding.ItemBlogViewBinding
import com.yl.wanandroid.model.ArticleItemData
import com.yl.wanandroid.model.CollectionEvent
import com.yl.wanandroid.ui.activity.WebViewActivity
import com.yl.wanandroid.ui.adapter.OnCollectionEventListener
import com.yl.wanandroid.utils.LogUtils
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * @description: 分享列表适配器
 * @author YL Chen
 * @date 2025/3/8 22:10
 * @version 1.0
 */
class ShareListAdapter @Inject constructor(@ActivityContext val context: Context) :
    BasePagingDataAdapter<ArticleItemData, ItemBlogViewBinding>(R.layout.item_blog_view, COMPARATOR) {
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<ArticleItemData>() {
            override fun areItemsTheSame(
                oldItem: ArticleItemData,
                newItem: ArticleItemData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ArticleItemData,
                newItem: ArticleItemData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
    private var onCollectionEventListener: OnCollectionEventListener? = null


    override fun bindItemData(binding: ItemBlogViewBinding?, position: Int) {
        binding?.itemData = getItem(position)
    }

    override fun setListener(holder: ViewHolder, binding: ItemBlogViewBinding?, position: Int) {
        val itemData = getItem(position) ?: return
        //设置条目点击跳转
        holder.itemView.setOnClickListener {
            WebViewActivity.start(context, itemData.link)
        }
        //收藏按钮初始化
        LogUtils.d(this, "collect-->${itemData.collect}")
        if (itemData.collect) {
            //已收藏
            binding?.collection?.setImageResource(R.drawable.star)
        } else {
            //未收藏
            binding?.collection?.setImageResource(R.drawable.no_star)
        }
        //收藏按钮点击事件
        binding?.collection?.setOnClickListener {
            var originId = itemData.originId
            if (originId == 0) {
                originId = -1
            }
            val where =
                (itemData.adminAdd == null) && (itemData.apkLink == null)//因为收藏页面返回的json数据没有这两个字段
            //通知监听者
            onCollectionEventListener?.onCollectionEvent(
                CollectionEvent(
                    itemData.id,
                    originId,
                    itemData.collect,
                    where
                )
            )
        }
    }


    fun updateCollectionState(id: Int) {
        for (i in 0 until itemCount) {
            if (getItem(i)?.id == id){
                LogUtils.d(this, "data.id == id")
                getItem(i)!!.collect = getItem(i)!!.collect.not()
                notifyItemChanged(i)
                return
            }
        }
    }

    /**
     * 对外界提供设置收藏监听接口的方法
     * @param listener OnCollectionEventListener
     */
    fun setOnCollectionEventListener(listener: OnCollectionEventListener) {
        onCollectionEventListener = listener
    }
}