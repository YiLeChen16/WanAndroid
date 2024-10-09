package com.yl.wanandroid.ui.fragment

import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
<<<<<<< HEAD
import com.yl.wanandroid.base.ViewStateEnum
import com.yl.wanandroid.databinding.FragmentSystemBinding
import com.yl.wanandroid.viewmodel.SystemFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
=======
import com.yl.wanandroid.databinding.FragmentSystemBinding
import com.yl.wanandroid.viewmodel.SystemFragmentViewModel
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451

/**
 * @description: 体系
 * @author YL Chen
 * @date 2024/9/7 16:02
 * @version 1.0
 */
class SystemFragment:BaseVMFragment<FragmentSystemBinding,SystemFragmentViewModel>(R.layout.fragment_system) {
<<<<<<< HEAD
    companion object{
        private var systemFragment:SystemFragment? = null
        fun newInstance() :SystemFragment{
            if(systemFragment == null){
                systemFragment = SystemFragment()
            }
            return systemFragment!!
        }
    }
    override fun initVMData() {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            //测试模拟加载延迟
            delay(2000)
            //模拟加载成功
            mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
        }
=======
    override fun initVMData() {

>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
    }
}