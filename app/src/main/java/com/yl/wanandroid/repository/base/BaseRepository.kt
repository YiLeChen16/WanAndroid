package com.yl.wanandroid.repository.base

import com.yl.wanandroid.Constant
import com.yl.wanandroid.network.exeception.ApiException
import com.yl.wanandroid.network.result.BaseResult
import com.yl.wanandroid.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

/**
 * @description: 网络请求基础仓库
 * @author YL Chen
 * @date 2024/9/10 17:06
 * @version 1.0
 */
open class BaseRepository {
    /**
     * IO中处理请求,请求错误抛出自定义异常
     * @param requestCall SuspendFunction0<BaseResult<T>?>
     * @return T?
     */
    suspend fun <T> requestResponse(requestCall: suspend () -> BaseResult<T>?): T? {
        val result = withContext(Dispatchers.IO) {
            withTimeout(Constant.CONNECT_TIME_OUT * 1000) {
                requestCall()
            }
        } ?: return null
        LogUtils.e(this@BaseRepository,"result-->$result")

        if (result.isFailed()) {
            throw ApiException(result.errorCode, result.errorMsg)
        }
        return result.data
    }
}