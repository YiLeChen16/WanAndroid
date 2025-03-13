package com.yl.wanandroid.network.exeception

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import com.yl.wanandroid.utils.LogUtils
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import kotlin.coroutines.cancellation.CancellationException

/**
 * 统一错误处理工具类
 */
object ExceptionHandler {

    fun handleException(e: Throwable): ApiException {

        val ex: ApiException
        when (e) {
            is ApiException -> {
                LogUtils.e(this, "is ApiException")

                ex = ApiException(e.errCode, e.errMsg, e)
            }

            is NetWorkException -> {
                LogUtils.e(this, "is NetWorkException ")
                ex = ApiException(ERROR.NETWORD_ERROR, e)
            }

            is HttpException -> {
                LogUtils.e(this, "is HttpException code-->${e.code()}")
                ex = when (e.code()) {
                    ERROR.UNAUTHORIZED.code -> ApiException(ERROR.UNAUTHORIZED, e)
                    ERROR.FORBIDDEN.code -> ApiException(ERROR.FORBIDDEN, e)
                    ERROR.NOT_FOUND.code -> ApiException(ERROR.NOT_FOUND, e)
                    ERROR.REQUEST_TIMEOUT.code -> ApiException(ERROR.REQUEST_TIMEOUT, e)
                    ERROR.GATEWAY_TIMEOUT.code -> ApiException(ERROR.GATEWAY_TIMEOUT, e)
                    ERROR.INTERNAL_SERVER_ERROR.code -> ApiException(ERROR.INTERNAL_SERVER_ERROR, e)
                    ERROR.BAD_GATEWAY.code -> ApiException(ERROR.BAD_GATEWAY, e)
                    ERROR.SERVICE_UNAVAILABLE.code -> ApiException(ERROR.SERVICE_UNAVAILABLE, e)
                    else -> ApiException(e.code(), e.message(), e)
                }
            }

            is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                LogUtils.e(
                    this,
                    "is JsonParseException, is JSONException, is ParseException, is MalformedJsonException"
                )
                ex = ApiException(ERROR.PARSE_ERROR, e)
            }

            is ConnectException -> {
                LogUtils.e(this, "is ConnectException")
                ex = ApiException(ERROR.NETWORD_ERROR, e)
            }

            is javax.net.ssl.SSLException -> {
                LogUtils.e(this, "is javax.net.ssl.SSLException")
                ex = ApiException(ERROR.SSL_ERROR, e)
            }

            is java.net.SocketException -> {
                LogUtils.e(this, "is java.net.SocketException")
                ex = ApiException(ERROR.TIMEOUT_ERROR, e)
            }

            is java.net.SocketTimeoutException -> {
                LogUtils.e(this, "is java.net.SocketTimeoutException")
                ex = ApiException(ERROR.TIMEOUT_ERROR, e)
            }

            is java.net.UnknownHostException -> {
                LogUtils.e(this, "is java.net.UnknownHostException")
                ex = ApiException(ERROR.UNKNOW_HOST, e)
            }

            is CancellationException->{
                LogUtils.e(this, "is CancellationException")
                ex = ApiException(ERROR.JOB_CANCEL,e = null)
            }

            else -> {
                LogUtils.e(this,"e-->${e.printStackTrace()}")
                ex = if (!e.message.isNullOrEmpty()) ApiException(1000, e.message!!, e)
                else ApiException(ERROR.UNKNOWN, e)
            }
        }
        return ex
    }
}
