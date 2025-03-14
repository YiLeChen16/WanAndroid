package com.yl.wanandroid.ui.adapter

import com.yl.wanandroid.model.CollectionEvent

/**
 * 收藏事件监听回调接口
 */
interface OnCollectionEventListener {
    fun onCollectionEvent(event: CollectionEvent)
}