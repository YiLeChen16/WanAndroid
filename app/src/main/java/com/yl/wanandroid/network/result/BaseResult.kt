package com.yl.wanandroid.network.result

/**
 * 网络返回数据类
 * @param T
 * @property errorCode String 0:正常，非0异常
 * @property errorMsg String
 * @property data T
 * @constructor
 */

data class BaseResult<T>(val data: T,val errorCode: Int, val errorMsg: String, ) {
    fun isFailed(): Boolean {
        return errorCode != 0
    }
}
