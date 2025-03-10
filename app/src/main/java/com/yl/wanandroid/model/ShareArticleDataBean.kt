package com.yl.wanandroid.model

data class ShareArticleDataBean(
    val coinInfo: CoinInfo,
    val shareArticles: PageResponse<ArticleItemData>
)