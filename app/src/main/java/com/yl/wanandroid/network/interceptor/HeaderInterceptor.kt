package com.yl.wanandroid.network.interceptor

import com.yl.wanandroid.Constant.ARTICLE_URL
import com.yl.wanandroid.Constant.COIN_URL
import com.yl.wanandroid.Constant.COLLECTION_URL
import com.yl.wanandroid.Constant.KEY_COOKIE
import com.yl.wanandroid.Constant.NOT_COLLECTION_URL
import com.yl.wanandroid.Constant.USER_INFO_URL
import com.yl.wanandroid.Constant.WEN_DA_URL
import com.yl.wanandroid.Constant.WX_ARTICLE_URL
import com.yl.wanandroid.network.manager.CookiesManager
import com.yl.wanandroid.utils.LogUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @description: 头信息拦截器
 * @author YL Chen
 * @date 2025/2/25 21:11
 * @version 1.0
 */
class HeaderInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newBuilder = request.newBuilder()
        newBuilder.addHeader("Content-type", "application/json; charset=utf-8")

        val host = request.url.host
        val url = request.url.toString()

        //给有需要的接口添加Cookies
        if (host.isNotEmpty() && (url.contains(COLLECTION_URL)
                    || url.contains(NOT_COLLECTION_URL)
                    || url.contains(ARTICLE_URL)//用于更新点击收藏后的界面
                    || url.contains(USER_INFO_URL)
                    || url.contains(WEN_DA_URL)//用于更新点击收藏后的界面
                    || url.contains(WX_ARTICLE_URL)//用于更新点击收藏后的界面
                    || url.contains(COIN_URL)
                )) {
            val cookies = CookiesManager.getCookies()
            LogUtils.e(this,"HeaderInterceptor:cookies:$cookies")
            if (!cookies.isNullOrEmpty()) {
                newBuilder.addHeader(KEY_COOKIE, cookies)
            }
        }
        return chain.proceed(newBuilder.build())
    }
}