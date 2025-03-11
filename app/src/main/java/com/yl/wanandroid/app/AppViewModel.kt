package com.yl.wanandroid.app

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.CollectionEvent
import com.yl.wanandroid.repository.CollectRepository
import com.yl.wanandroid.repository.LoginAndRegisterRepository
import com.yl.wanandroid.ui.adapter.OnCollectionEventListener
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast
import com.yl.wanandroid.utils.getStringFromResource
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @description: 全局ViewModel
 * @author YL Chen
 * @date 2025/2/28 16:27
 * @version 1.0
 */
@Singleton//单例
class AppViewModel @Inject constructor() :
    OnCollectionEventListener, BaseViewModel() {

    val shouldNavigateToLogin = MutableLiveData<Boolean>()

    val updateItemId = MutableLiveData<Int>()

    //收藏事件触发
    override fun onCollectionEvent(event: CollectionEvent) {
        if (isUserLogin()) {
            shouldNavigateToLogin.value = false
            if (event.isCollected) {
                //取消收藏
                //判断是在何处取消收藏的
                if (event.where) {
                    //2.我的收藏页面(因为收藏页面返回的json数据没有这两个字段)
                    cancelMyCollectArticle(event.id, event.originId)
                    //更新文章列表的收藏状态
                    this.updateItemId.value = event.originId
                } else {
                    //1.文章列表
                    cancelCollectArticle(event.id)
                    this.updateItemId.value = event.id
                }
            } else {
                //收藏
                collectArticle(event.id)
                this.updateItemId.value = event.id
            }
        } else {
            //通知View层跳转到登录页面
            shouldNavigateToLogin.value = true
        }
    }

    fun collectArticle(collectId: Int) {
        launchUI(
            errorCallback = { _, errMsg ->
                TipsToast.showTips(errMsg)
            }, requestCall = {
                CollectRepository.collectArticle(collectId)
                TipsToast.showTips(getStringFromResource(R.string.tip_success_collect))
            }
        )
    }

    fun collectOutsideArticle(title: String, author: String, link: String) {
        launchUI(
            errorCallback = { _, errMsg ->
                TipsToast.showTips(errMsg)
            }, requestCall = {
                CollectRepository.collectOutsideArticle(title, author, link)
                TipsToast.showTips(getStringFromResource(R.string.tip_success_collect))
            }
        )
    }


    fun cancelCollectArticle(id: Int) {
        launchUI(
            errorCallback = { _, errMsg ->
                TipsToast.showTips(errMsg)
            }, requestCall = {
                CollectRepository.cancelCollectArticle(id)
                TipsToast.showTips(getStringFromResource(R.string.tip_success_cancel_collect))
            }
        )
    }

    /**
     * 取消收藏(我的收藏界面的文章)
     * @param id Int
     * @param originId Int 此字段若不传会导致只有我的收藏界面的收藏会被取消,其他页面的收藏仍然显示收藏状态!
     */
    fun cancelMyCollectArticle(id: Int, originId: Int) {
        launchUI(
            errorCallback = { _, errMsg -> TipsToast.showTips(errMsg) },
            requestCall = {
                CollectRepository.cancelMyCollectArticle(id, originId)
                TipsToast.showTips(getStringFromResource(R.string.tip_success_cancel_collect))
            }
        )
    }


    //判断有无登录
    fun isUserLogin(): Boolean {
        runBlocking {
            if (LoginAndRegisterRepository.isUserLogin()) {
                //已登录
                LogUtils.d(this@AppViewModel, "isLogin1-->")
                true
            } else {
                //未登录
                TipsToast.showTips(getStringFromResource(R.string.tip_no_login))
                LogUtils.d(this@AppViewModel, "isLogin2-->")
                false
            }
        }.let {
            LogUtils.d(this@AppViewModel, "isLogin3-->$it")
            shouldNavigateToLogin.value = !it
            return it
        }
    }

    override fun onReload() {
        TODO("Not yet implemented")
    }
}



