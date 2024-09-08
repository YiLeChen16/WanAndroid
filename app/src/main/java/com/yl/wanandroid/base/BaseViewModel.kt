package com.yl.wanandroid.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @description: ViewModel基类
 * @author YL Chen
 * @date 2024/9/6 14:17
 * @version 1.0
 */
open class BaseViewModel : ViewModel(), BaseLifeCycleObserver {

    /**
     * 控制状态视图的LiveData
     */
    val mStateViewLiveData = MutableLiveData<StateLayoutEnum>()

    /**
     * 更改状态视图的状态
     */
    fun changeStateView(
        state: StateLayoutEnum
    ) {
        // 对参数进行校验
        when (state) {
            StateLayoutEnum.DATA_LOADING -> {
                mStateViewLiveData.postValue(StateLayoutEnum.DATA_LOADING)
            }

            StateLayoutEnum.DATA_ERROR -> {
                mStateViewLiveData.postValue(StateLayoutEnum.DATA_ERROR)
            }

            StateLayoutEnum.DATA_NULL -> {
                mStateViewLiveData.postValue(StateLayoutEnum.DATA_NULL)
            }

            StateLayoutEnum.NET_ERROR -> {
                mStateViewLiveData.postValue(StateLayoutEnum.NET_ERROR)
            }
        }
    }


    /**
     * 获取Repository实例
     */
    inline fun <reified R> getRepository(): R? {
        try {
            val clazz = R::class.java
            return clazz.newInstance()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * View生命周期：视图创建
     */
    override fun onCreate() {

    }

    /**
     * View生命周期：视图可见
     */
    override fun onStart() {
    }

    /**
     * View生命周期：视图获取焦点
     */
    override fun onResume() {
    }

    /**
     * View生命周期：视图失去焦点
     */
    override fun onPause() {
    }

    /**
     * View生命周期：视图不可见
     */
    override fun onStop() {
    }

    /**
     * View生命周期：视图销毁
     */
    override fun onDestroy() {
    }
}