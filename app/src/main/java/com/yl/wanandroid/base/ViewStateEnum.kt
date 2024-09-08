package com.yl.wanandroid.base

/**
 * @description: 视图状态枚举类
 * @author YL Chen
 * @date 2024/9/6 15:03
 * @version 1.0
 */
enum class ViewStateEnum {
    VIEW_LOADING,//数据加载中
    VIEW_EMPTY,//数据加载为空
    VIEW_NET_ERROR,//网络错误
    VIEW_LOAD_SUCCESS,//加载成功
    VIEW_NONE//默认值
}