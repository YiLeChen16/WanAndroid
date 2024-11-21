package com.yl.wanandroid.ui.fragment.home.wenda

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.yl.wanandroid.Constant
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.databinding.FragmentWendaHotOrNormalBinding
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.adapter.BlogListAdapter
import com.yl.wanandroid.utils.TipsToast
import com.yl.wanandroid.viewmodel.home.HotOrMoreWendaFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @description: 热门问答或更多问答Fragment,由于这两个Fragment逻辑基本相同,考虑复用一个Fragment类
 * @author YL Chen
 * @date 2024/11/19 18:00
 * @version 1.0
 */
@AndroidEntryPoint
class HotOrNormalWendaFragment :
    BaseVMFragment<FragmentWendaHotOrNormalBinding, HotOrMoreWendaFragmentViewModel>(R.layout.fragment_wenda_hot_or_normal) {
    private var type: String? = null

    companion object {
        //根据Type创建不同类型的Fragment
        fun newInstance(type: String): HotOrNormalWendaFragment {
            val fragment = HotOrNormalWendaFragment()
            val bundle = Bundle()
            bundle.putString(Constant.WENDA_FRAGMENT_TYPE_KEY, type)//传递类型参数
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var listAdapter: BlogListAdapter

    override fun initView() {
        super.initView()
        type = arguments?.getString(Constant.WENDA_FRAGMENT_TYPE_KEY)
        if (type == Constant.HOT_WENDA_FRAGMENT_TYPE) {
            //禁止加载更多
            mRefreshLayout.setEnableLoadMore(false)
            mRefreshLayout.setOnRefreshListener {
                mViewModel.getHotWendaData()
            }
        } else {
            mRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
                override fun onRefresh(refreshLayout: RefreshLayout) {
                    mViewModel.getNormalWendaData()
                }

                override fun onLoadMore(refreshLayout: RefreshLayout) {
                    mViewModel.getMoreNormalWendaData()
                }
            })
        }
        mBinding.list.adapter = listAdapter
        mBinding.list.layoutManager = LinearLayoutManager(context)
    }


    override fun initVMData() {
        //将当前type存储到ViewModel中
        mViewModel.type = type!!
        //根据传递的type加载不同的列表数据
        loadData()
    }

    //根据传递的type加载不同的列表数据
    private fun loadData() {
        if (type == Constant.HOT_WENDA_FRAGMENT_TYPE) {
            //加载热门问答模块数据
            mViewModel.getHotWendaData()
        } else {
            //加载更多问答模块数据
            mViewModel.getNormalWendaData()
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        //根据type不同观察不同的数据
        if (type == Constant.HOT_WENDA_FRAGMENT_TYPE) {
            mViewModel.hotWendaData.observe(viewLifecycleOwner) { hotWendaData ->
                if (hotWendaData != null) {
                    //为列表装载数据
                    listAdapter.setData(hotWendaData.toMutableList())
                    mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
                    mRefreshLayout.finishRefresh()
                }
            }
        } else {
            mViewModel.normalWendaData.observe(viewLifecycleOwner) { normalWendaData ->
                if (normalWendaData != null) {
                    listAdapter.setData(normalWendaData.datas.toMutableList())
                    mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
                    mRefreshLayout.finishRefresh()
                }
            }

            mViewModel.moreNormalWendaData.observe(viewLifecycleOwner) { moreNormalWendaData ->
                if (moreNormalWendaData != null) {
                    mRefreshLayout.finishLoadMore()
                    if (moreNormalWendaData.curPage == moreNormalWendaData.pageCount + 1) {
                        //最后一页
                        TipsToast.showTips(R.string.tip_toast_last_page)
                        mRefreshLayout.finishLoadMoreWithNoMoreData()
                    } else {
                        //装载数据
                        listAdapter.addData(moreNormalWendaData.datas)
                        TipsToast.showTips(R.string.tip_toast_load_more_success)
                    }
                }else{
                    mRefreshLayout.finishLoadMore()
                }
            }
        }
    }
}