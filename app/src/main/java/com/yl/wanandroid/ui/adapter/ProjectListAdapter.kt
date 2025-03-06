package com.yl.wanandroid.ui.adapter

import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yl.wanandroid.Constant
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseRecyclerViewAdapter
import com.yl.wanandroid.databinding.ItemProjectViewBinding
import com.yl.wanandroid.model.ArticleItemData
import com.yl.wanandroid.model.CollectionEvent
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
    BaseRecyclerViewAdapter<ArticleItemData, ItemProjectViewBinding>(R.layout.item_project_view) {

    override fun setViewBindingVariable(binding: ItemProjectViewBinding?, position: Int) {
        binding?.itemData = datas[position]//为布局声明变量绑定数据
    }

    override fun setListener(
        holder: MyViewHolder,
        binding: ItemProjectViewBinding?,
        position: Int
    ) {
        //收藏按钮初始化
        if (datas[position].collect) {
            //已收藏
            binding?.collection?.setImageResource(R.drawable.star)
        } else {
            //未收藏
            binding?.collection?.setImageResource(R.drawable.no_star)
        }
        //设置条目点击事件
        holder.itemView.setOnClickListener {
            //点击跳转到WebActivity
            WebViewActivity.start(context,datas[position].link)
        }
        //设置加载网络图片
        val picUrl: String = datas[position].envelopePic
        LogUtils.d(this, "picUrl-->$picUrl")
        if (binding?.img != null) {
            val options: RequestOptions = RequestOptions()
                .placeholder(R.drawable.img_loading)//图片加载出来前，显示的图片
                .fallback(R.drawable.img_blank) //url为空的时候,显示的图片
                .error(R.drawable.img_load_failure)//图片加载失败后，显示的图片
            Glide.with(context).load(picUrl).apply(options).into(binding.img)
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