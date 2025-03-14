package com.yl.wanandroid.repository.project

import com.yl.wanandroid.model.ArticleDataBean
import com.yl.wanandroid.model.ProjectCategoryDataBeanItem
import com.yl.wanandroid.network.WanAndroidApiInterface
import com.yl.wanandroid.repository.base.BaseRepository

/**
 * @description: 项目页面仓库
 * @author YL Chen
 * @date 2024/11/24 22:43
 * @version 1.0
 */
object ProjectRepository: BaseRepository(){

    /**
     * 获取项目分类数据
     * @return MutableList<ProjectCategoryDataBeanItem>?
     */
    suspend fun getProjectCategory() : MutableList<ProjectCategoryDataBeanItem>?{
        return requestResponse {
            WanAndroidApiInterface.api.getProjectCategory()
        }
    }

    /**
     * 获取对应分类项目数据
     * @param page Int
     * @param cid Int
     * @return MutableList<ProjectItemDataBean>?
     */
    suspend fun getProjectDataByCid(page:Int,cid:Int):ArticleDataBean?{
        return requestResponse {
            WanAndroidApiInterface.api.getProjectDataByCid(page,cid)
        }
    }
}