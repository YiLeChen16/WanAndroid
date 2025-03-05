package com.yl.wanandroid.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.CoinData
import com.yl.wanandroid.model.UserDataBean
import com.yl.wanandroid.repository.IntegralRepository
import com.yl.wanandroid.repository.MyRepository
import com.yl.wanandroid.utils.TipsToast
import kotlinx.coroutines.flow.Flow

/**
 * @description: 积分界面ViewModel
 * @author YL Chen
 * @date 2025/3/3 20:57
 * @version 1.0
 */
class IntegralActivityViewModel : BaseViewModel() {


    val userInfo = MutableLiveData<UserDataBean?>()

    val level = ObservableField("0")
    val integral = ObservableField("0")
    val rank = ObservableField("0")

    fun getCoinInfo() {
        launchUI(errorCallback = { _, errMsg ->
            TipsToast.showTips(errMsg)
            userInfo.value = null
        }, requestCall = {
            userInfo.value = MyRepository.getUserInfo()
            level.set(userInfo.value?.coinInfo?.level.toString())
            integral.set(userInfo.value?.coinInfo?.coinCount.toString())
            rank.set(userInfo.value?.coinInfo?.rank)
        })
    }

    fun getUserCoinList(): Flow<PagingData<CoinData>> {
        return IntegralRepository.getUserCoinList().cachedIn(viewModelScope)
    }

    override fun onReload() {
        getCoinInfo()
    }
}