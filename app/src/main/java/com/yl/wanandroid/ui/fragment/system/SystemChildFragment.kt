package com.yl.wanandroid.ui.fragment.system

import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.databinding.FragmentSystemChildBinding
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.adapter.SystemChildLeftListAdapter
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.viewmodel.system.SystemChildFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.concurrent.thread

/**
 * @description: 体系子页面
 * @author YL Chen
 * @date 2024/12/18 21:57
 * @version 1.0
 */
@AndroidEntryPoint
class SystemChildFragment:BaseVMFragment<FragmentSystemChildBinding,SystemChildFragmentViewModel>(R.layout.fragment_system_child) {


    @Inject
    lateinit var systemChildLeftListAdapter: SystemChildLeftListAdapter

    override fun initView() {
        super.initView()
        //禁用刷新布局
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setEnableLoadMore(false)
        mBinding.leftTab.adapter = systemChildLeftListAdapter
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mBinding.leftTab.layoutManager = linearLayoutManager
        //左侧导航与viewPager联动

    }


    override fun initVMData() {
        mViewModel.getSystemData()
        systemChildLeftListAdapter.setOnTabTitleClickListener(mViewModel)
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.systemData.observe(viewLifecycleOwner){
            if(it != null){
                //设置数据
                systemChildLeftListAdapter.setData(it)
                LogUtils.d(this@SystemChildFragment,"systemData-->$it")
                mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
            }
        }
    }
}