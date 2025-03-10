package com.yl.wanandroid.viewmodel.my

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.ArticleItemData
import com.yl.wanandroid.repository.ShareRepository
import com.yl.wanandroid.repository.UserRepository
import com.yl.wanandroid.room.entity.UserItem
import com.yl.wanandroid.utils.TipsToast
import kotlinx.coroutines.flow.Flow


/**
 * @description:分享界面ViewModel
 * @author YL Chen
 * @date 2025/3/8 20:22
 * @version 1.0
 */
class ShareActivityViewModel : BaseViewModel() {

    val shareArticleCount = ObservableField(0)
    val user = ObservableField<UserItem>()

    fun getShareArticleList(): Flow<PagingData<ArticleItemData>> {
        return ShareRepository.getUserShareArticleList().cachedIn(viewModelScope)
    }

    fun getUserShareArticleCountAndUserInfo(){
        launchUI(
            errorCallback = {
                _,errMsg->
                TipsToast.showTips(errMsg)
                shareArticleCount.set(0)
                user.set(null)
            },
            requestCall = {
                val countAndCoinInfo =
                    ShareRepository.getUserShareArticleCountAndCoinInfo()
                val user = UserRepository.getUser()
                shareArticleCount.set(countAndCoinInfo?.shareArticles?.total)
                this.user.set(user[0])
            }
        )
    }


    override fun onReload() {

    }
}