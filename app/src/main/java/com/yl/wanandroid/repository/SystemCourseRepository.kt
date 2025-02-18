package com.yl.wanandroid.repository

import com.yl.wanandroid.model.ArticleDataBean
import com.yl.wanandroid.model.SystemDataBeanItem
import com.yl.wanandroid.network.WanAndroidApiInterface
import com.yl.wanandroid.repository.base.BaseRepository

/**
 * @description: 体系课程仓库
 * @author YL Chen
 * @date 2025/2/12 16:42
 * @version 1.0
 */
class SystemCourseRepository:BaseRepository() {

    suspend fun getSystemCourseData():MutableList<SystemDataBeanItem>?{
        return requestResponse {
            WanAndroidApiInterface.api.getSystemCourseData()
        }
    }

    suspend fun getSystemCourseArticleDataByCid(page:Int,cid:Int,order:Int=1): ArticleDataBean?{
        return requestResponse {
            WanAndroidApiInterface.api.getSystemCourseArticleDataByCid(page,cid,order)
        }
    }
}