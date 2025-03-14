package com.yl.wanandroid.ui.activity.my.rank

import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.yl.wanandroid.R
import com.yl.wanandroid.base.activity.BaseVMActivity
import com.yl.wanandroid.databinding.ActivityRankBinding
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.adapter.my.RankListAdapter
import com.yl.wanandroid.utils.TipsToast
import com.yl.wanandroid.viewmodel.my.rank.RankActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @description: 排名界面
 * @author YL Chen
 * @date 2025/3/5 12:50
 * @version 1.0
 */
@AndroidEntryPoint
class RankActivity :
    BaseVMActivity<ActivityRankBinding, RankActivityViewModel>(R.layout.activity_rank) {

    @Inject
    lateinit var rankListAdapter: RankListAdapter

    override fun initView() {
        super.initView()
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setEnableLoadMore(false)
        mBinding.toolbar.setNavigationOnClickListener {
            finish()
        }
        mBinding.rankList.adapter = rankListAdapter
        mBinding.rankList.layoutManager = LinearLayoutManager(this)
        rankListAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    mBinding.progressBar.visibility = View.INVISIBLE
                    mBinding.rankList.visibility = View.VISIBLE
                }

                is LoadState.Loading -> {
                    mBinding.progressBar.visibility = View.VISIBLE
                    mBinding.rankList.visibility = View.INVISIBLE
                }

                is LoadState.Error -> {
                    val state = it.refresh as LoadState.Error
                    mBinding.progressBar.visibility = View.INVISIBLE
                    TipsToast.showTips(state.error.message)
                }
            }
        }
    }

    override fun initVMData() {
        mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
    }

    override fun observeLiveData() {
        super.observeLiveData()
        lifecycleScope.launch {
            mViewModel.getRankList().collect { pagingData ->
                rankListAdapter.submitData(pagingData)
            }
        }
    }

}