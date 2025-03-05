package com.yl.wanandroid.ui.activity

import android.content.Intent
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMActivity
import com.yl.wanandroid.databinding.ActivityIntegralBinding
import com.yl.wanandroid.viewmodel.IntegralActivityViewModel
import com.yl.wanandroid.BR
import com.yl.wanandroid.Constant
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.adapter.CoinListAdapter
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @description: 积分界面 RecyclerView结合Paging实现列表自动分页管理
 * @author YL Chen
 * @date 2025/3/3 20:54
 * @version 1.0
 */
@AndroidEntryPoint
class IntegralActivity :
    BaseVMActivity<ActivityIntegralBinding, IntegralActivityViewModel>(R.layout.activity_integral) {

    @Inject
    lateinit var coinListAdapter: CoinListAdapter
    override fun initView() {
        super.initView()
        mBinding.toolbar.setNavigationOnClickListener {
            finish()
        }
        mRefreshLayout.setEnableLoadMore(false)
        mRefreshLayout.setEnableRefresh(false)
        mBinding.coinList.adapter = coinListAdapter
        mBinding.coinList.layoutManager = LinearLayoutManager(this)
        coinListAdapter.addLoadStateListener {
            when(it.refresh){
                is LoadState.NotLoading->{
                    mBinding.progressBar.visibility = View.INVISIBLE
                    mBinding.coinList.visibility = View.VISIBLE
                }
                is LoadState.Loading->{
                    mBinding.progressBar.visibility = View.VISIBLE
                    mBinding.coinList.visibility = View.INVISIBLE
                }
                is LoadState.Error->{
                    val state = it.refresh as LoadState.Error
                    mBinding.progressBar.visibility = View.INVISIBLE
                    TipsToast.showTips(state.error.message)
                }
            }
        }
    }

    override fun initVMData() {
        mViewModel.getCoinInfo()

    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.userInfo.observe(this) {
            if (it != null) {
                LogUtils.d(this@IntegralActivity, "it-->$it")
                mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
            }
        }
        lifecycleScope.launch {
            mViewModel.getUserCoinList().collect{
                pagingData->
                coinListAdapter.submitData(pagingData)
            }
        }

        mViewModel.gotoRule.observe(this){
            if (it){
                //跳转到规则界面
                WebViewActivity.start(this@IntegralActivity,Constant.RULE_URL)
                mViewModel.gotoRule.value = false
            }

        }

        mViewModel.gotoRank.observe(this){
            if (it){
                startActivity(Intent(this@IntegralActivity,RankActivity::class.java))
                mViewModel.gotoRank.value = false
            }

        }


    }

    override fun getVariableId(): Int {
        return BR.integralActivityViewModel
    }
}