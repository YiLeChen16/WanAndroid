package com.yl.wanandroid.model

data class UserCoinDataBean(
    val curPage: Int,
    val datas: List<CoinData>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)