package com.yl.wanandroid.ui.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.yl.wanandroid.Constant
import com.yl.wanandroid.R
import com.yl.wanandroid.databinding.ItemCourseListBinding
import com.yl.wanandroid.model.SystemDataBeanItem
import com.yl.wanandroid.ui.activity.SystemCourseActivity
import com.yl.wanandroid.utils.LogUtils
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject


/**
 * @description: 体系课程列表适配器
 * @author YL Chen
 * @date 2025/2/12 17:21
 * @version 1.0
 */
class CourseListAdapter @Inject constructor(@ActivityContext val context: Context) :
    RecyclerView.Adapter<CourseListAdapter.MyViewHolder>() {
    private var data: MutableList<SystemDataBeanItem> = mutableListOf()

    class MyViewHolder(binding: ViewBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater =
            LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemCourseListBinding>(
            inflater,
            R.layout.item_course_list,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemCourseListBinding>(holder.itemView)
        binding?.itemData = data[position]
        //设置加载网络图片
        val picUrl: String = data[position].cover
        LogUtils.d(this, "picUrl-->$picUrl")
        if (binding?.image != null) {
            // 设置圆角半径为10dp
            val radius = 20
            //统一设置4个角
            val options: RequestOptions = RequestOptions().transform(RoundedCorners(radius))
                .placeholder(R.drawable.img_loading)//图片加载出来前，显示的图片
                .fallback(R.drawable.img_blank) //url为空的时候,显示的图片
                .error(R.drawable.img_load_failure)//图片加载失败后，显示的图片
            Glide.with(holder.itemView.context).load(picUrl).apply(options).into(binding.image)
        }
        //条目点击事件
        holder.itemView.setOnClickListener {
            //点击跳转到SystemCourseActivity
            val intent = Intent(context, SystemCourseActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(Constant.SYSTEM_COURSE_ID,data[position].id)
            bundle.putString(Constant.SYSTEM_COURSE_NAME,data[position].name)
            intent.putExtras(bundle)//携带数据跳转
            context.startActivity(intent)
            LogUtils.d(this,"id->${data[position].id}")
            LogUtils.d(this,"name->${data[position].name}")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: MutableList<SystemDataBeanItem>) {
        this.data = data
    }
}