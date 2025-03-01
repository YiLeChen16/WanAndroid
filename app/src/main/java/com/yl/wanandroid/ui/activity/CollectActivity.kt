package com.yl.wanandroid.ui.activity

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.yl.wanandroid.R
import com.yl.wanandroid.app.AppViewModel
import com.yl.wanandroid.base.BaseVMActivity
import com.yl.wanandroid.databinding.ActivityCollectBinding
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.adapter.BlogListAdapter
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast
import com.yl.wanandroid.viewmodel.home.CollectViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @description: 收藏界面
 * @author YL Chen
 * @date 2025/2/26 13:57
 * @version 1.0
 */
@AndroidEntryPoint
class CollectActivity :
    BaseVMActivity<ActivityCollectBinding, CollectViewModel>(R.layout.activity_collect) {
    @Inject
    lateinit var listAdapter: BlogListAdapter

    @Inject
    lateinit var appViewModel: AppViewModel

    override fun initView() {
        super.initView()
        //设置适配器和布局管理器
        mBinding.collectList.adapter = listAdapter
        mBinding.collectList.layoutManager = LinearLayoutManager(this)
        //禁止刷新
        mRefreshLayout.setEnableRefresh(false)
        //返回键监听
        mBinding.toolbar.setNavigationOnClickListener {
            finish()
        }
        //加载更多监听
        mRefreshLayout.setOnLoadMoreListener {
            mViewModel.getMoreAllCollectArticle()
        }
    }

    override fun initVMData() {
        mViewModel.getAllCollectArticle()
        //设置收藏监听事件
        listAdapter.setOnCollectionEventListener(appViewModel)
        LogUtils.d(this, "appViewModel-->$appViewModel")

    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.collectArticles.observe(this) {
            if (it == null)
                return@observe
            LogUtils.d(this@CollectActivity, "data-->${it.datas}")
            listAdapter.setData(it.datas)
            mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
        }

        mViewModel.moreCollectArticles.observe(this){
            mRefreshLayout.finishLoadMore()
            if (it == null)
                return@observe
            LogUtils.d(this@CollectActivity, "data-->${it.datas}")
            if (it.curPage == it.pageCount + 1) {
                //最后一页
                TipsToast.showTips(R.string.tip_toast_last_page)
                mRefreshLayout.finishLoadMoreWithNoMoreData()
            } else {
                //为适配器添加数据
                listAdapter.addData(it.datas)
                TipsToast.showTips(R.string.tip_toast_load_more_success)
            }
        }

        appViewModel.isUserLogin.observe(this) {
            if (!it) {
                //跳转到登录页面
                startActivity(Intent(this@CollectActivity, LoginActivity::class.java))
            }
        }

        appViewModel.event.observe(this) {
            //更新我的收藏页面的列表
            listAdapter.removeItemByOriginId(it.originId)
        }
    }

}