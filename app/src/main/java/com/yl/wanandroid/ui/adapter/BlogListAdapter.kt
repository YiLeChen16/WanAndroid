package com.yl.wanandroid.ui.adapter

import android.content.Context
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseRecyclerViewAdapter
import com.yl.wanandroid.databinding.ItemBlogViewBinding
import com.yl.wanandroid.model.ArticleItemData
import com.yl.wanandroid.model.CollectionEvent
import com.yl.wanandroid.ui.activity.WebViewActivity
import com.yl.wanandroid.utils.LogUtils
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * @description: 博客列表适配器 使用DataBinding绑定条目
 * @author YL Chen
 * @date 2024/10/20 18:33
 * @version 1.0
 */
class BlogListAdapter @Inject constructor(@ActivityContext val context: Context) :
    BaseRecyclerViewAdapter<ArticleItemData, ItemBlogViewBinding>(R.layout.item_blog_view) {


    private var onCollectionEventListener: OnCollectionEventListener? = null


    override fun setListener(
        holder: MyViewHolder,
        binding: ItemBlogViewBinding?,
        position: Int
    ) {
        //设置条目点击跳转
        holder.itemView.setOnClickListener {
            WebViewActivity.start(context,datas[position].link)
        }
        //收藏按钮初始化
        LogUtils.d(this, "collect-->${datas[position].collect}")
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

    override fun setViewBindingVariable(binding: ItemBlogViewBinding?, position: Int) {
        binding?.itemData = datas[position]
    }


    /**
     * 用于我的收藏页面取消收藏时更新页面
     * @param originId Int
     */
    fun removeItemByOriginId(originId: Int) {
        for ((index, data) in datas.withIndex()) {
            if (data.originId == originId) {
                datas.remove(data)
                notifyItemRemoved(index)
                notifyItemRangeChanged(index, itemCount)
                return
            }
        }
    }


    fun updateCollectionState(originID: Int) {
        for ((index, data) in datas.withIndex()) {
            if (data.id == originID) {
                LogUtils.d(this, "data.id == id")
                datas[index].collect = !datas[index].collect
                notifyItemChanged(index)
                return
            }
        }
    }

    //自定义收藏事件监听接口:方法(收藏事件id)
    //在AppViewModel中注册监听事件,因为收藏是一个全局事件!一旦有一个适配器发生收藏事件,就将通知CollectViewModel
    //在CollectViewModel中定义一个对应的LiveData属性对象,监听收藏事件,一旦发生则修改属性对象
    //在需要界面实时更新收藏状态的界面,观察ViewModel中的属性对象
    //对界面的列表数据进行遍历,比对其中id==收藏id的collect属性,对其进行更新

    /**
     * 对外界提供设置收藏监听接口的方法
     * @param listener OnCollectionEventListener
     */
    fun setOnCollectionEventListener(listener: OnCollectionEventListener) {
        onCollectionEventListener = listener
    }


}