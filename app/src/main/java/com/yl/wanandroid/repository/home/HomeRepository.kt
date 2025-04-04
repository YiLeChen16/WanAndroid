package com.yl.wanandroid.repository.home

import com.yl.wanandroid.model.BannerDataBean
import com.yl.wanandroid.network.WanAndroidApiInterface
import com.yl.wanandroid.repository.base.BaseRepository

/**
 * @description: 首页仓库
 * @author YL Chen
 * @date 2024/9/10 16:32
 * @version 1.0
 */
object HomeRepository : BaseRepository() {

    /**
     * 网络请求Banner数据
     * @return BannerDataBean
     */
    suspend fun getBannerData():MutableList<BannerDataBean>?{
        return requestResponse {
            WanAndroidApiInterface.api.getBannerData()
        }
    }


}