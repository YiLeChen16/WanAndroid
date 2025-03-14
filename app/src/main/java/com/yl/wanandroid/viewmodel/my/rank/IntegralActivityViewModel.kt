package com.yl.wanandroid.viewmodel.my.rank

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yl.wanandroid.base.viewModel.BaseViewModel
import com.yl.wanandroid.model.CoinData
import com.yl.wanandroid.model.UserDataBean
import com.yl.wanandroid.repository.my.IntegralRepository
import com.yl.wanandroid.repository.my.MyRepository
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
    val gotoRank = MutableLiveData<Boolean>()
    val gotoRule = MutableLiveData<Boolean>()

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

    fun onRankClick(){
        //通知UI跳转到排名界面
        gotoRank.value = true
    }

    fun onRule(){
        //通知UI跳转到规则界面
        gotoRule.value = true
    }

    fun getUserCoinList(): Flow<PagingData<CoinData>> {
        return IntegralRepository.getUserCoinList().cachedIn(viewModelScope)
    }

    override fun onReload() {
        getCoinInfo()
    }
}