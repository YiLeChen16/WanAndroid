package com.yl.wanandroid.repository

import com.yl.wanandroid.model.BannerDataBean
import com.yl.wanandroid.network.WanAndroidApiInterface
import com.yl.wanandroid.repository.base.BaseRepository
import javax.inject.Inject

/**
 * @description: 首页仓库
 * @author YL Chen
 * @date 2024/9/10 16:32
 * @version 1.0
 */
class HomeRepository @Inject constructor() : BaseRepository() {

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