package com.yl.wanandroid.ui.fragment.system

import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.databinding.FragmentSystemCourseBinding
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.viewmodel.system.SystemCourseFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @description: 体系下的课程子页面
 * @author YL Chen
 * @date 2024/12/18 21:58
 * @version 1.0
 */
class SystemCourseFragment:BaseVMFragment<FragmentSystemCourseBinding,SystemCourseFragmentViewModel>(
    R.layout.fragment_system_course) {
    override fun initVMData() {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            //测试模拟加载延迟
            delay(2000)
            //模拟加载成功
            mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
        }
    }
}