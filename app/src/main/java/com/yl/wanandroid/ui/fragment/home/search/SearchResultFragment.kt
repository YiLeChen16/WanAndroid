package com.yl.wanandroid.ui.fragment.home.search

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.yl.wanandroid.BR
import com.yl.wanandroid.R
import com.yl.wanandroid.app.AppViewModel
import com.yl.wanandroid.base.fragment.BaseVMFragment
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.databinding.FragmentSearchResultBinding
import com.yl.wanandroid.ui.activity.login.LoginActivity
import com.yl.wanandroid.ui.adapter.search.SearchResultListAdapter
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast
import com.yl.wanandroid.viewmodel.search.SearchShareViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @description: 搜索结果Fragment
 * @author YL Chen
 * @date 2024/10/25 20:18
 * @version 1.0
 */
@AndroidEntryPoint
class SearchResultFragment :
    BaseVMFragment<FragmentSearchResultBinding, SearchShareViewModel>(
        R.layout.fragment_search_result
    ) {

    @Inject
    lateinit var mSearchResultListAdapter: SearchResultListAdapter

    @Inject
    lateinit var appViewModel: AppViewModel

    override fun initView() {
        super.initView()
        //为列表设置适配器
        mBinding.searchResultList.adapter = mSearchResultListAdapter
        //为列表设置布局管理器
        mBinding.searchResultList.layoutManager = LinearLayoutManager(context)
        //设置刷新框架监听事件
        mRefreshLayout.setOnRefreshListener {
            //刷新监听事件
            mViewModel.getSearchResultData(mViewModel.mCurrentSearchKeyWord.value!!)
        }
        mRefreshLayout.setOnLoadMoreListener {
            //加载更多监听事件
            mViewModel.loadMoreSearchResultData()
        }
    }

    override fun initVMData() {
        if (mViewModel.searchHintKeyWord.isNotEmpty() && mViewModel.editData.value.isNullOrEmpty()) {
            //将跳转获取到的关键词赋值到当前搜索关键词
            mViewModel.getSearchResultData(mViewModel.searchHintKeyWord)
        }
        //设置收藏监听事件
        mSearchResultListAdapter.setOnCollectionEventListener(appViewModel)
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.search_fragment_visibility.observe(this) {
            if (it) {
                //此视图可见
                //触发更新
                LogUtils.d(this@SearchResultFragment, "触发更新")
                mViewModel.mCurrentSearchKeyWord.value = mViewModel.mCurrentSearchKeyWord.value
            } else {
                //此视图不可见
                mViewModel.changeStateView(ViewStateEnum.VIEW_NONE)//将视图状态设为NONE
            }
        }
        mViewModel.searchResultListData.observe(this) { searchResultDataBean ->
            if (mViewModel.search_fragment_visibility.value == true) {
                if (searchResultDataBean == null) return@observe
                LogUtils.d(this, "mViewModel.searchResultListData-->$searchResultDataBean")
                if (searchResultDataBean.datas.isNotEmpty()) {
                    //为适配器装载数据
                    mSearchResultListAdapter.setData(searchResultDataBean.datas)
                    //改变当前视图状态
                    mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
                } else {
                    //数据为空
                    mViewModel.changeStateView(ViewStateEnum.VIEW_EMPTY)
                }
                mRefreshLayout.finishRefresh()

            }

        }
        //监听当前搜索词
        mViewModel.mCurrentSearchKeyWord.observe(this) {
            if (mViewModel.search_fragment_visibility.value == true) {
                //设置视图加载状态:解决列表闪烁问题
                mViewModel.changeStateView(ViewStateEnum.VIEW_LOADING)
                mViewModel.getSearchResultData(it)//搜索
                //将当前刷新框架的加载更多重置
                mRefreshLayout.setEnableLoadMore(true)
            }
        }

        mViewModel.loadMoreSearchResultListData.observe(viewLifecycleOwner) { loadMoreSearchResultDataBean ->
            mRefreshLayout.finishLoadMore()
            if (loadMoreSearchResultDataBean != null) {
                LogUtils.d(
                    this,
                    "mViewModel.loadMoreSearchResultListData-->$loadMoreSearchResultDataBean"
                )
                if (loadMoreSearchResultDataBean.curPage == loadMoreSearchResultDataBean.pageCount + 1) {
                    //最后一页后的一页,不包含数据
                    TipsToast.showTips(R.string.tip_toast_last_page)
                    mRefreshLayout.setEnableLoadMore(false)//标记当前没有更多数据了
                } else {
                    //为适配器添加数据
                    mSearchResultListAdapter.addData(loadMoreSearchResultDataBean.datas)
                    TipsToast.showTips(R.string.tip_toast_load_more_success)
                    mRefreshLayout.finishLoadMore()
                }
            } else {
                TipsToast.showTips(R.string.tip_null)
                mRefreshLayout.finishLoadMore()
            }
        }

        appViewModel.shouldNavigateToLogin.observe(this) {
            //跳转到登录页面
            if (it) {
                startActivity(Intent(context, LoginActivity::class.java))
                //重置变量,避免多次跳转
                appViewModel.shouldNavigateToLogin.value = false
            }
        }

        //实时更新界面收藏状态
        appViewModel.updateItemId.observe(viewLifecycleOwner) {
            mSearchResultListAdapter.updateCollectionState(it)
        }
    }

    //重写此方法,获取xml布局中声明的变量id,以为xml布局中声明的变量赋值
    override fun getVariableId(): Int {
        return BR.searchShareViewModel
    }
}