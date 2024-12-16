package com.yl.wanandroid.model



data class RecommendBlogDataBean(
    val curPage: Int,
    val datas: List<ArticleItemData>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)


//data class RecommendBlogTag(
//    val name: String,
//    val url: String
//)

