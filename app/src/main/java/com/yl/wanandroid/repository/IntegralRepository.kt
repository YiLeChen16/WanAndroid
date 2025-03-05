package com.yl.wanandroid.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yl.wanandroid.common.CommonPagingSource
import com.yl.wanandroid.model.CoinData
import com.yl.wanandroid.model.RankItemData
import com.yl.wanandroid.network.WanAndroidApiInterface
import com.yl.wanandroid.repository.base.BaseRepository
import kotlinx.coroutines.flow.Flow

object IntegralRepository : BaseRepository() {
    private const val PAGE_SIZE = 50
    fun getUserCoinList(): Flow<PagingData<CoinData>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = {
                CommonPagingSource { page, _ ->
                    WanAndroidApiInterface.api.getUserCoinList(
                        page
                    ).data.datas
                }
            }
        ).flow
    }

    fun getRankListData(): Flow<PagingData<RankItemData>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = {
                CommonPagingSource { page, _ ->
                    WanAndroidApiInterface.api.getRankListData(page).data.datas
                }
            }
        ).flow
    }
}