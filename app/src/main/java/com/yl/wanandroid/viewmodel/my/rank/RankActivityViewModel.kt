package com.yl.wanandroid.viewmodel.my.rank

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yl.wanandroid.base.viewModel.BaseViewModel
import com.yl.wanandroid.model.RankItemData
import com.yl.wanandroid.repository.my.IntegralRepository
import kotlinx.coroutines.flow.Flow

/**
 * @description: 排名界面ViewModel
 * @author YL Chen
 * @date 2025/3/5 12:51
 * @version 1.0
 */
class RankActivityViewModel : BaseViewModel() {
    fun getRankList(): Flow<PagingData<RankItemData>> {
        return IntegralRepository.getRankListData().cachedIn(viewModelScope)
    }

    override fun onReload() {
        getRankList()
    }
}