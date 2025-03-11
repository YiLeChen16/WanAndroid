package com.yl.wanandroid.network.manager

import com.tencent.mmkv.MMKV
import com.yl.wanandroid.Constant.HTTP_COOKIES_INFO
import com.yl.wanandroid.utils.LogUtils

/**
 * @description: Cookies管理类//TODO
 * @author YL Chen
 * @date 2025/2/25 18:23
 * @version 1.0
 */
object CookiesManager {

    /**
     * 解析Cookies
     * @param cookies
     */
    fun encodeCookie(cookies: List<String>?): String {
        val sb = StringBuilder()
        val set = HashSet<String>()
        cookies
            ?.map { cookie ->
                cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            }
            ?.forEach {
                it.filterNot { set.contains(it) }.forEach { set.add(it) }
            }
        LogUtils.e(this,"cookiesList:$cookies")
        val ite = set.iterator()
        while (ite.hasNext()) {
            val cookie = ite.next()
            sb.append(cookie).append(";")
        }
        val last = sb.lastIndexOf(";")
        if (sb.length - 1 == last) {
            sb.deleteCharAt(last)
        }
        return sb.toString()
    }
    /**
     * 保存Cookies
     * @param cookies
     */
    fun saveCookies(cookies: String) {
        val mmkv = MMKV.defaultMMKV()
        mmkv.encode(HTTP_COOKIES_INFO, cookies)
    }

    /**
     * 获取Cookies
     * @return cookies
     */
    fun getCookies(): String? {
        val mmkv = MMKV.defaultMMKV()
        LogUtils.d(this,"getCookies-->${mmkv.decodeString(HTTP_COOKIES_INFO, "")}")
        return mmkv.decodeString(HTTP_COOKIES_INFO, "")
    }


    /**
     * 清除Cookies
     * @param cookies
     */
    fun clearCookies() {
        saveCookies("")
    }
}
