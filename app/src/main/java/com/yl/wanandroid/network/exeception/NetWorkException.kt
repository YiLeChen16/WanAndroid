package com.yl.wanandroid.network.exeception

import java.io.IOException

/**
 * @description: 网络异常类
 * @author YL Chen
 * @date 2024/9/10 22:25
 * @version 1.0
 */
class NetWorkException(error: ERROR, e: Throwable? = null) : IOException(e) {
    var errCode: Int
    var errMsg: String

    init {
        errCode = error.code
        errMsg = error.errMsg
    }
}