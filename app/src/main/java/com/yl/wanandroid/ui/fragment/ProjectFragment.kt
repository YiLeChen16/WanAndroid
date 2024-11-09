package com.yl.wanandroid.ui.fragment

import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.databinding.FragmentProjectBinding
import com.yl.wanandroid.viewmodel.ProjectFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @description: 项目
 * @author YL Chen
 * @date 2024/9/7 16:01
 * @version 1.0
 */
class ProjectFragment:BaseVMFragment<FragmentProjectBinding,ProjectFragmentViewModel>(R.layout.fragment_project) {
    companion object{
        private var projectFragment:ProjectFragment? = null
        fun newInstance() :ProjectFragment{
            if(projectFragment == null){
                projectFragment = ProjectFragment()
            }
            return projectFragment!!
        }
    }

    @Inject lateinit var projectFragmentViewModel: ProjectFragmentViewModel

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