package com.yl.wanandroid.model

data class PageResponse<ItemData>(
    val curPage: Int,
    val datas: List<ItemData>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)
