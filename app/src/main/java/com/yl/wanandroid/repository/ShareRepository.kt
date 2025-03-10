package com.yl.wanandroid.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yl.wanandroid.Constant.DEFAULT_PAGE_START_NO_1
import com.yl.wanandroid.Constant.PAGE_SIZE
import com.yl.wanandroid.common.CommonPagingSource
import com.yl.wanandroid.model.ArticleItemData
import com.yl.wanandroid.model.ShareArticleDataBean
import com.yl.wanandroid.network.WanAndroidApiInterface
import com.yl.wanandroid.repository.base.BaseRepository
import kotlinx.coroutines.flow.Flow

/**
 * @description: 分享界面仓库//TODO
 * @author YL Chen
 * @date 2025/3/8 21:53
 * @version 1.0
 */
object ShareRepository:BaseRepository() {

    /**
     * 获取用户分享的文章列表
     * @return Flow<PagingData<Any>>
     */
     fun getUserShareArticleList():Flow<PagingData<ArticleItemData>>{
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = {
                CommonPagingSource { page, _ ->
                    WanAndroidApiInterface.api.getMyShareArticle(page).data.shareArticles.datas
                }
            }
        ).flow
    }

    /**
     * 获取用户分享的文章数目和用户信息
     * @return ShareArticleDataBean?
     */
    suspend fun getUserShareArticleCountAndCoinInfo():ShareArticleDataBean?{
        return requestResponse {
            WanAndroidApiInterface.api.getMyShareArticle(DEFAULT_PAGE_START_NO_1)
        }
    }
}