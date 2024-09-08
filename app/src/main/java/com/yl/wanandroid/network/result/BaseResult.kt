package com.yl.wanandroid.network.result

/**
 * 网络返回数据类
 * @param T
 * @property errCode String 0:正常，非0异常
 * @property errMsg String
 * @property data T
 * @constructor
 */

data class BaseResult<T>(val errCode: Int, val errMsg: String, val data: T) {
    fun isFailed(): Boolean {
        return errCode!=0
    }
}
