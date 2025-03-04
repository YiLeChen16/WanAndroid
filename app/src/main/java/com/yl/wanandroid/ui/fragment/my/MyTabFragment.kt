package com.yl.wanandroid.ui.fragment.my

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.SimpleItemAnimator
import com.yl.wanandroid.R
import com.yl.wanandroid.app.AppViewModel
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.databinding.FragmentMyTabBinding
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.activity.LoginActivity
import com.yl.wanandroid.ui.adapter.BlogListAdapter
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast
import com.yl.wanandroid.viewmodel.my.MyTabFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @description: "我的"界面下的TabFragment
 * @author YL Chen
 * @date 2025/3/2 15:01
 * @version 1.0
 */
@AndroidEntryPoint
class MyTabFragment() :
    BaseVMFragment<FragmentMyTabBinding, MyTabFragmentViewModel>(R.layout.fragment_my_tab) {
    constructor(id: Int) : this() {
        this.id = id
    }

    @Inject
    lateinit var listAdapter: BlogListAdapter

    @Inject
    lateinit var appViewModel: AppViewModel

    private var id: Int = 0
    override fun initView() {
        super.initView()
        mBinding.list.adapter = listAdapter
        mBinding.list.layoutManager = LinearLayoutManager(context)
        mRefreshLayout.setOnRefreshListener {
            mViewModel.getWxArticlesByWxId()
        }
        mRefreshLayout.setOnLoadMoreListener {
            mViewModel.getMoreWxArticlesByWxId()
        }
    }

    override fun initVMData() {
        mViewModel.id = this.id
        mViewModel.getWxArticlesByWxId()
        listAdapter.setOnCollectionEventListener(appViewModel)
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.wxArticles.observe(viewLifecycleOwner) {
            mRefreshLayout.finishRefresh()
            LogUtils.d(this@MyTabFragment, "it-->${it?.datas}")
            if (it == null) return@observe
            listAdapter.setData(it.datas)
            mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
        }

        mViewModel.moreWxArticles.observe(viewLifecycleOwner) {
            mRefreshLayout.finishLoadMore()
            if (it == null) return@observe
            listAdapter.addData(it.datas)
            TipsToast.showTips(getString(R.string.tip_toast_load_more_success))
        }


        //当我的收藏页面更新时,刷新此页面的收藏状态
        appViewModel.event.observe(viewLifecycleOwner) {
            listAdapter.updateCollectionState(it.originId)//一定要传originId而不是id
        }

        appViewModel.isUserLogin.observe(viewLifecycleOwner) {
            if (!it) {
                startActivity(Intent(context, LoginActivity::class.java))
            }
        }
    }
}