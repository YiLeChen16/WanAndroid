package com.yl.wanandroid.model

data class NormalWendaDataBean(
    val curPage: Int,
    val datas: List<ItemData>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)