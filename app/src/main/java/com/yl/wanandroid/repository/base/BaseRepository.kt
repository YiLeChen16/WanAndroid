package com.yl.wanandroid.repository.base

import com.yl.wanandroid.network.exeception.ApiException
import com.yl.wanandroid.network.result.BaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

/**
 * @description: 基础仓库
 * @author YL Chen
 * @date 2024/9/10 17:06
 * @version 1.0
 */
open class BaseRepository {
   private val Default_Timeout:Long = 5 * 1000

    /**
     * IO中处理请求,请求错误抛出自定义异常
     * @param requestCall SuspendFunction0<BaseResult<T>?>
     * @return T?
     */
    suspend fun <T> requestResponse(requestCall: suspend () -> BaseResult<T>?): T? {
        val result = withContext(Dispatchers.IO) {
            withTimeout(Default_Timeout) {
                requestCall()
            }
        } ?: return null

        if (result.isFailed()) {
            throw ApiException(result.errCode, result.errMsg)
        }
        return result.data
    }
}