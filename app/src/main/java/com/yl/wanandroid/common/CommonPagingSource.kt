package com.yl.wanandroid.common

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yl.wanandroid.Constant.DEFAULT_PAGE_START_NO_1
import com.yl.wanandroid.model.PageResponse
import com.yl.wanandroid.network.WanAndroidApiInterface
import com.yl.wanandroid.network.result.BaseResult

/**
 * @description: PagingSource 通用封装类
 * @author YL Chen
 * @date 2025/3/4 20:56
 * @version 1.0
 */
class CommonPagingSource<ItemDataBean : Any>(
    private val pageStart: Int = DEFAULT_PAGE_START_NO_1,
    private val requestResponse: suspend (Int, Int) -> List<ItemDataBean>
) :
    PagingSource<Int, ItemDataBean>() {
    override fun getRefreshKey(state: PagingState<Int, ItemDataBean>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemDataBean> {
        val page = params.key ?: pageStart
        return try {
            val data = requestResponse(page, params.loadSize)
            LoadResult.Page(
                data = data,
                prevKey = if (page == pageStart) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}