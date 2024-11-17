package com.yl.wanandroid.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.yl.wanandroid.utils.LogUtils

data class SearchResultDataBean(
    val curPage: Int,
    val datas: List<ItemData>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

data class SearchTag(
    val name: String,
    val url: String
)


