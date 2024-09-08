package com.yl.wanandroid.network.exeception

/**
 * @description: 自定义接口网络请求异常类
 * @author YL Chen
 * @date 2024/9/10 17:20
 * @version 1.0
 */
class ApiException :Exception{
    var errCode: Int
    var errMsg: String

    constructor(error: ERROR, e: Throwable? = null) : super(e) {
        errCode = error.code
        errMsg = error.errMsg
    }

    constructor(code: Int, msg: String, e: Throwable? = null) : super(e) {
        this.errCode = code
        this.errMsg = msg
    }
}