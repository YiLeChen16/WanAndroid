package com.yl.wanandroid.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
<<<<<<< HEAD
import androidx.lifecycle.viewModelScope
import com.yl.wanandroid.network.exeception.ExceptionHandler
import com.yl.wanandroid.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
=======
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451

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
<<<<<<< HEAD
    val mStateViewLiveData = MutableLiveData<ViewStateEnum>()

    /**
     * 切换到UI线程
     * @param errorCallback SuspendFunction0<Unit> 错误回调
     * @param requestCall SuspendFunction0<Unit> 网络请求函数
     */
    fun launchUI(
        errorCallback: suspend (Int?, String?) -> Unit,
        requestCall: suspend () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            //统一进行异常捕获
            safeApiCall(errorCallback, requestCall)
        }
    }

    /**
     * 对网络请求进行统一异常捕获
     * @param errorCallback SuspendFunction2<Int?, String?, Unit> 错误回调
     * @param requestCall SuspendFunction0<Unit> 网络请求函数
     * @return T? 网络请求成功数据
     */
    suspend fun<T> safeApiCall(
        errorCallback: suspend (Int?, String?) -> Unit,
        requestCall: suspend () -> T?
    ): T? {
        try {
            //返回网络请求结果
            return requestCall()
        }catch (e: Exception){
            LogUtils.e(this@BaseViewModel,e.message.toString())
            e.printStackTrace()
            //统一异常处理
            //将异常转为ApiException
            val apiException = ExceptionHandler.handleException(e)
            errorCallback(apiException.errCode,apiException.errMsg)
        }
        return null
    }
=======
    val mStateViewLiveData = MutableLiveData<StateLayoutEnum>()
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451

    /**
     * 更改状态视图的状态
     */
    fun changeStateView(
<<<<<<< HEAD
        state: ViewStateEnum
    ) {
        // 对参数进行校验
        when (state) {
            ViewStateEnum.VIEW_LOADING -> {
                mStateViewLiveData.postValue(ViewStateEnum.VIEW_LOADING)
            }

            ViewStateEnum.VIEW_EMPTY -> {
                mStateViewLiveData.postValue(ViewStateEnum.VIEW_EMPTY)
            }


            ViewStateEnum.VIEW_NET_ERROR -> {
                mStateViewLiveData.postValue(ViewStateEnum.VIEW_NET_ERROR)
            }

            ViewStateEnum.VIEW_LOAD_SUCCESS -> {
                mStateViewLiveData.postValue(ViewStateEnum.VIEW_LOAD_SUCCESS)
            }
            ViewStateEnum.VIEW_NONE -> {
                mStateViewLiveData.postValue(ViewStateEnum.VIEW_NONE)
=======
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
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
            }
        }
    }


    /**
     * 获取Repository实例
     */
<<<<<<< HEAD
/*    inline fun <reified R> getRepository(): R? {
=======
    inline fun <reified R> getRepository(): R? {
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
        try {
            val clazz = R::class.java
            return clazz.newInstance()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
<<<<<<< HEAD
    }*/
=======
    }
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451

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