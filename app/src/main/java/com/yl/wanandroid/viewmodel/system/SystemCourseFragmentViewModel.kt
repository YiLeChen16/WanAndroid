package com.yl.wanandroid.viewmodel.system

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.SystemDataBeanItem
import com.yl.wanandroid.repository.SystemCourseRepository
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: 系统课程页面ViewModel
 * @author YL Chen
 * @date 2024/12/18 22:03
 * @version 1.0
 */
class SystemCourseFragmentViewModel : BaseViewModel() {

    val systemCourse = MutableLiveData<MutableList<SystemDataBeanItem>?>()


    /**
     * 获取课程列表数据
     */
    fun getSystemCourse() {
        launchUI(errorCallback = { _, errMsg ->
            TipsToast.showTips(errMsg)
            systemCourse.value = null
        }, requestCall = {
            systemCourse.value = SystemCourseRepository.getSystemCourseData()
        })
    }

    override fun onReload() {
        getSystemCourse()
    }
}