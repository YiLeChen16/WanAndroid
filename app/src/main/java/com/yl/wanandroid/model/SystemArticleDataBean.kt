package com.yl.wanandroid.model

data class SystemArticleDataBean(
    val curPage: Int,
    val datas: List<ArticleItemData>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)