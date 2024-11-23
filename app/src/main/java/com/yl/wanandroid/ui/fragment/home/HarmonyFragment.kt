package com.yl.wanandroid.ui.fragment.home

import androidx.recyclerview.widget.LinearLayoutManager
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.databinding.FragmentHarmonyBinding
import com.yl.wanandroid.viewmodel.home.HarmonyFragmentViewModel
import com.yl.wanandroid.BR
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.adapter.HarmonyContentListAdapter
import com.yl.wanandroid.utils.LogUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * @description: 首页鸿蒙Fragment
 * @author YL Chen
 * @date 2024/10/20 16:08
 * @version 1.0
 */
@AndroidEntryPoint
class HarmonyFragment : BaseVMFragment<FragmentHarmonyBinding, HarmonyFragmentViewModel>(
    R.layout.fragment_harmony
) {


    @Inject
    lateinit var harmonyContentListAdapter: HarmonyContentListAdapter

    override fun initView() {
        super.initView()
        mBinding.tvLink.isEnabled = true//设置为选中状态
        //设置列表适配器及布局管理器
        mBinding.contentList.adapter = harmonyContentListAdapter
        mBinding.contentList.layoutManager = LinearLayoutManager(context)
        //禁止上拉加载
        mRefreshLayout.setEnableLoadMore(false)
    }

    override fun initVMData() {
        mViewModel.getHarmonyColumnData()
    }

    override fun observeLiveData() {
        super.observeLiveData()
        //初始化数据
        mViewModel.harmonyColumnDataBean.observe(viewLifecycleOwner){
            harmonyColumnData->
            LogUtils.d(this,"harmonyColumnData-->$harmonyColumnData")
            if(harmonyColumnData != null){
                when (mViewModel.show.value) {
                    0 -> {
                        mViewModel.onTvLinkClick()
                    }
                    1 -> {
                        mViewModel.onTvProjectClick()
                    }
                    else -> {
                        mViewModel.onTvToolClick()
                    }
                }
                mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
                mRefreshLayout.finishRefresh()
            }else{
                mViewModel.changeStateView(ViewStateEnum.VIEW_NET_ERROR)
            }
        }

        //更改左侧导航视图选中状态
        mViewModel.show.observe(viewLifecycleOwner) {
            when (it) {
                0 -> {
                    mBinding.tvLink.isSelected = true
                    mBinding.tvProject.isSelected = false
                    mBinding.tvTool.isSelected = false
                }

                1 -> {
                    mBinding.tvLink.isSelected = false
                    mBinding.tvProject.isSelected = true
                    mBinding.tvTool.isSelected = false
                }

                else -> {
                    mBinding.tvLink.isSelected = false
                    mBinding.tvProject.isSelected = false
                    mBinding.tvTool.isSelected = true
                }
            }
        }

        mViewModel.linkData.observe(viewLifecycleOwner) {
            //给适配器设置数据
            if (it != null) {
                harmonyContentListAdapter.setData(it)
                //mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
            } else {
                mViewModel.changeStateView(ViewStateEnum.VIEW_NET_ERROR)
            }
        }

        mViewModel.projectData.observe(viewLifecycleOwner) {
            //给适配器设置数据
            if (it != null) {
                harmonyContentListAdapter.setData(it)
                //mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
            } else {
                mViewModel.changeStateView(ViewStateEnum.VIEW_NET_ERROR)
            }
        }

        mViewModel.toolData.observe(viewLifecycleOwner) {
            //给适配器设置数据
            if (it != null) {
                harmonyContentListAdapter.setData(it)
                //mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
            } else {
                mViewModel.changeStateView(ViewStateEnum.VIEW_NET_ERROR)
            }
        }
    }

    override fun getVariableId(): Int {
        return BR.harmonyFragmentViewModel
    }
}