package com.yl.wanandroid.network.interceptor


import com.yl.wanandroid.Constant.SET_COOKIE
import com.yl.wanandroid.Constant.USER_LOGIN_URL
import com.yl.wanandroid.Constant.USER_LOGOUT_URL
import com.yl.wanandroid.Constant.USER_REGISTER_URL
import com.yl.wanandroid.network.manager.CookiesManager
import com.yl.wanandroid.utils.LogUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author mingyan.su
 * @date   2023/3/27 07:26
 * @desc   Cookies拦截器
 */
class CookiesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newBuilder = request.newBuilder()

        val response = chain.proceed(newBuilder.build())
        val url = request.url.toString()
        // set-cookie maybe has multi, login to save cookie
        if ((url.contains(USER_LOGIN_URL) || url.contains(USER_REGISTER_URL))
                && response.headers(SET_COOKIE).size > 1//默认会返回一个JSESSIONID Cookie ,只有当登录成功或注册成功时才会返回返回账号密码
        ) {
            val cookies = response.headers(SET_COOKIE)
            val cookiesStr = CookiesManager.encodeCookie(cookies)
            CookiesManager.saveCookies(cookiesStr)
            LogUtils.e(this@CookiesInterceptor,"CookiesInterceptor:cookies:$cookies")
        }
        //拦截退出登录请求
        if(url.contains(USER_LOGOUT_URL) && response.headers(SET_COOKIE).isNotEmpty()){
            //清除本地Cookie
            CookiesManager.clearCookies()
        }
        return response
    }
}