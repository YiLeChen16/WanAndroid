package com.yl.wanandroid.base

import androidx.databinding.BaseObservable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * @description: 生命周期观察基类
 * @author YL Chen
 * @date 2024/9/6 14:28
 * @version 1.0
 */
 interface BaseLifeCycleObserver:LifecycleObserver {

     @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
     fun onCreate()

     @OnLifecycleEvent(Lifecycle.Event.ON_START)
     fun onStart()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause()

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy()
}