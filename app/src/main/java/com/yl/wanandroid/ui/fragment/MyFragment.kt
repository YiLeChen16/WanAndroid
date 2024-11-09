package com.yl.wanandroid.ui.fragment

import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.databinding.FragmentMyBinding
import com.yl.wanandroid.viewmodel.MyFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @description: 我的
 * @author YL Chen
 * @date 2024/9/7 16:02
 * @version 1.0
 */
class MyFragment: BaseVMFragment<FragmentMyBinding, MyFragmentViewModel>(R.layout.fragment_my)  {
    companion object{
        private var myFragment:MyFragment? = null
        fun newInstance() :MyFragment{
            if(myFragment == null){
                myFragment = MyFragment()
            }
            return myFragment!!
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
    }
}