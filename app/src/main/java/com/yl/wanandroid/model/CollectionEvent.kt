package com.yl.wanandroid.model

/**
 * 收藏事件
 * @property id Int
 * @property originId Int
 * @property isCollected Boolean
 * @constructor
 */
data class CollectionEvent(
    val id: Int,
    var originId:Int = -1,
    val isCollected:Boolean,
    val where:Boolean//表示触发收藏或取消收藏的位置,true为我的收藏页面,false为文章列表
)
