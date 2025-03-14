package com.yl.wanandroid

/**
 * 常量值
 */
object Constant {
    const val CURRENT_SEARCH_HOTKEY: String = "currentSearchHotKey"
    const val IS_SEARCH: String = "isSearch"
    const val WENDA_FRAGMENT_TYPE_KEY: String = "wendaType"//问答fragment类型
    const val HOT_WENDA_FRAGMENT_TYPE: String = "hotWenda"//热门问答fragment类型
    const val NORMAL_WENDA_FRAGMENT_TYPE: String = "moreWenda"//更多问答fragment类型
    const val TO_SYSTEM: String = "to_system_url"
    const val SYSTEM_ALL_KEYWORD_CID: String = "system_all_keyword_cid"//选中体系条目下的所有关键词cid
    const val SYSTEM_ALL_KEYWORD: String = "system_all_keyword"//选中体系条目下的所有关键词
    const val SYSTEM_CHOOSE_KEYWORD_CID: String = "system_choose_keyword_cid"//选中的关键词id
    val SYSTEM_COURSE_ID: String = "system_course_id"//体系课程的ID
    const val SYSTEM_COURSE_NAME: String = "system_course_name"//体系课程的标题
    const val USER_LOGIN_URL: String = "user/login"
    const val USER_REGISTER_URL: String = "user/register"
    const val USER_LOGOUT_URL: String = "user/logout"//退出登录接口
    const val SET_COOKIE = "set-cookie"
    const val HTTP_COOKIES_INFO = "http_cookies_info" //cookies缓存
    const val COLLECTION_URL = "/lg/collect" //收藏接口
    const val COIN_URL = "/lg/coin" //积分接口
    const val NOT_COLLECTION_URL = "/lg/uncollect"//取消收藏接口
    const val ARTICLE_URL = "/article"//文章接口
    const val USER_INFO_URL = "/user/lg"//用户信息接口
    const val WEN_DA_URL = "/wenda"//问答接口
    const val WX_ARTICLE_URL = "/wxarticle"//公众号文章接口
    const val KEY_COOKIE = "Cookie"
    const val FORGET_PASSWORD_URL = "https://www.wanandroid.com/blog/show/2947"
    const val RULE_URL = "https://www.wanandroid.com/blog/show/2653"
    const val DEFAULT_PAGE_START_NO_1 = 1//默认起始页数1
    const val DEFAULT_PAGE_START_NO_0 = 0//默认起始页数0
    const val PAGE_SIZE = 50
    const val KEY_PASSWORD = "userPassword_key"
    const val CONNECT_TIME_OUT = 10L//默认链接超时时间



}