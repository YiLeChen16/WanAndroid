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
    RecyclerView.Adapter<BlogListAdapter.MyViewHolder>() {


    private var datas: MutableList<ArticleItemData> = mutableListOf()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var onCollectionEventListener: OnCollectionEventListener? = null

    //暴露方法给外界设置数据
    fun setData(recommendBlogDatas: List<ArticleItemData>) {
        this.datas.clear()//清空数据
        this.datas.addAll(recommendBlogDatas)
        notifyDataSetChanged()
    }

    //暴露方法给外界添加数据
    fun addData(recommendBlogDatas: List<ArticleItemData>) {
        val oldIndex = datas.size - 1
        this.datas.addAll(recommendBlogDatas)
        notifyItemRangeChanged(oldIndex, recommendBlogDatas.size)
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
            //先进行伪更新.更新界面收藏状态(注意: 必须先进行此操作再通知监听者,否则removeItemByOriginId方法的使用会有问题,会导致移除最后一个条目时此处索引越界)
            datas[position].collect = !datas[position].collect
            notifyItemChanged(position)//更新界面
            //通知监听者
            onCollectionEventListener?.onCollectionEvent(
                CollectionEvent(
                    datas[position].id,
                    originId,
                    !datas[position].collect,
                    where
                )
            )

        }
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


    fun updateCollectionState(id:Int){
        for ((index, data) in datas.withIndex()) {
            if (data.id == id) {
                LogUtils.d(this,"data.id == id")
                datas[index].collect = !datas[index].collect
                notifyItemChanged(index)
                return
            }
        }
    }

    //TODO::
    //自定义收藏事件监听接口:方法(收藏事件id)
    //在AppViewModel中注册监听事件,因为收藏是一个全局事件!一旦有一个适配器发生收藏事件,就将通知CollectViewModel
    //在CollectViewModel中定义一个对应的LiveData属性对象,监听收藏事件,一旦发生则修改属性对象
    //在需要界面实时更新收藏状态的界面,观察ViewModel中的属性对象
    //对界面的列表数据进行遍历,比对其中id==收藏id的collect属性,对其进行更新

    /**
     * 对外界提供设置接口的方法
     * @param listener OnCollectionEventListener
     */
    fun setOnCollectionEventListener(listener: OnCollectionEventListener) {
        onCollectionEventListener = listener
    }

    interface OnCollectionEventListener {
        fun onCollectionEvent(event: CollectionEvent)
    }
}