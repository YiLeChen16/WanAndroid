package com.yl.wanandroid.ui.activity

import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMActivity
import com.yl.wanandroid.databinding.ActivityShareBinding
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.viewmodel.my.ShareActivityViewModel
import com.yl.wanandroid.BR
import com.yl.wanandroid.ui.adapter.ShareListAdapter
import com.yl.wanandroid.utils.TipsToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @description: 分享界面
 * @author YL Chen
 * @date 2025/3/8 20:21
 * @version 1.0
 */
@AndroidEntryPoint
class ShareActivity :
    BaseVMActivity<ActivityShareBinding, ShareActivityViewModel>(R.layout.activity_share) {

    @Inject
    lateinit var shareListAdapter: ShareListAdapter

    override fun initView() {
        super.initView()
        mBinding.toolbar.setNavigationOnClickListener {
            finish()
        }
        mRefreshLayout.setEnableLoadMore(false)
        mRefreshLayout.setEnableRefresh(false)
        mBinding.rvShareList.adapter = shareListAdapter
        mBinding.rvShareList.layoutManager = LinearLayoutManager(this)
        shareListAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    mBinding.progressBar.visibility = View.INVISIBLE
                    mBinding.rvShareList.visibility = View.VISIBLE
                }

                is LoadState.Loading -> {
                    mBinding.progressBar.visibility = View.VISIBLE
                    mBinding.rvShareList.visibility = View.INVISIBLE
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
        mViewModel.getUserShareArticleCountAndUserInfo()
        mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
    }

    override fun observeLiveData() {
        super.observeLiveData()
        lifecycleScope.launch {
            mViewModel.getShareArticleList().collect{
                pagingData->
                shareListAdapter.submitData(pagingData)
                //TODO:将数据暂存到adapter中,以便设置收藏监听回调时使用

            }
        }
    }

    override fun getVariableId(): Int {
        return BR.shareActivityViewModel
    }
}