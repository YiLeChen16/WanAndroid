package com.yl.wanandroid.ui.fragment.home

import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.base.ViewStateEnum
import com.yl.wanandroid.databinding.FragmentWendaBinding
import com.yl.wanandroid.viewmodel.home.WendaFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @description: 首页问答Fragment TODO
 * @author YL Chen
 * @date 2024/10/20 16:22
 * @version 1.0
 */
class WendaFragment:BaseVMFragment<FragmentWendaBinding, WendaFragmentViewModel>(R.layout.fragment_wenda) {
    companion object{
        private var wendaFragment: WendaFragment? = null
        fun newInstance() : WendaFragment {
            if(wendaFragment == null){
                wendaFragment = WendaFragment()
            }
            return wendaFragment!!
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