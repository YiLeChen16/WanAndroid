package com.yl.wanandroid.ui.fragment.home


import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.yl.wanandroid.R
import com.yl.wanandroid.app.AppViewModel
import com.yl.wanandroid.base.fragment.BaseVMFragment
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.databinding.FragmentRecommendBinding
import com.yl.wanandroid.ui.activity.login.LoginActivity
import com.yl.wanandroid.ui.adapter.BlogListAdapter
import com.yl.wanandroid.ui.custom.SearchBoxView
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast
import com.yl.wanandroid.viewmodel.home.RecommendFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * @description: 首页推荐Fragment
 * @author YL Chen
 * @date 2024/10/20 15:07
 * @version 1.0
 */
@AndroidEntryPoint
class RecommendFragment :
    BaseVMFragment<FragmentRecommendBinding, RecommendFragmentViewModel>(R.layout.fragment_recommend) {


    @Inject
    lateinit var recommendListAdapter: BlogListAdapter

    @Inject
    lateinit var appViewModel: AppViewModel


    override fun initView() {
        super.initView()
        mBinding.list.layoutManager = LinearLayoutManager(context)
        mBinding.list.adapter = recommendListAdapter
        //设置刷新监听事件
        mRefreshLayout.setOnRefreshListener {
            LogUtils.d(this, "refresh")
            //获取第一页推荐博客数据
            mViewModel.getRecommendBlogData()
            //获取推荐热词
            mViewModel.getSearchHotkeyData()
        }
        //设置加载更多监听事件
        mRefreshLayout.setOnLoadMoreListener {
            LogUtils.d(this, "load more")
            //加载更多推荐博客数据
            mViewModel.loadMoreRecommendBlogData()
        }
    }

    override fun initVMData() {
        //获取推荐博客数据
        mViewModel.getRecommendBlogData()
        //获取搜索热词
        mViewModel.getSearchHotkeyData()

        //设置收藏监听事件
        recommendListAdapter.setOnCollectionEventListener(appViewModel)

        LogUtils.d(this, "appViewModel-->$appViewModel")
    }

    override fun observeLiveData() {
        super.observeLiveData()
        //观察并设置推荐博客数据
        mViewModel.recommendBlogData.observe(viewLifecycleOwner) { recommendBlogData ->
            LogUtils.d(this@RecommendFragment, "recommendBlogData-->${recommendBlogData?.datas}")
            //将数据装载到列表中
            if (recommendBlogData != null) {
                recommendListAdapter.setData(recommendBlogData.datas)
                //显示加载成功界面
                mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
                mRefreshLayout.finishRefresh()
            }
        }

        //观察并设置搜索热词数据
        mViewModel.searchHotKeyData.observe(viewLifecycleOwner) { searchHotKeyData ->
            if (searchHotKeyData != null) {
                //将请求回来的数据在搜索框中轮播
                LogUtils.d(this, "searchHotKeyData-->$searchHotKeyData")
                //设置数据
                val searchBoxView = mBinding.root.findViewById<SearchBoxView>(R.id.search_box_view)
                searchBoxView.setData(searchHotKeyData)
            }
        }

        //观察并设置加载更多博客数据
        mViewModel.loadMoreRecommendBlogData.observe(viewLifecycleOwner) { loadMoreRecommendBlogData ->
            if (loadMoreRecommendBlogData != null) {
                mRefreshLayout.finishLoadMore()
                if (loadMoreRecommendBlogData.curPage == loadMoreRecommendBlogData.pageCount + 1) {
                    //没有数据的一页==最后一页的下一页
                    TipsToast.showTips(R.string.tip_toast_last_page)
                    mRefreshLayout.finishLoadMoreWithNoMoreData()//标记当前没有更多数据了
                } else {
                    recommendListAdapter.addData(loadMoreRecommendBlogData.datas)
                    TipsToast.showTips(R.string.tip_toast_load_more_success)
                }
            } else {
                mRefreshLayout.finishLoadMore()
            }
        }

        appViewModel.shouldNavigateToLogin.observe(viewLifecycleOwner) {
            if (it) {
                //跳转到登录页面
                LogUtils.d(this,"appViewModel.isUserLogin.observe")
                startActivity(Intent(context, LoginActivity::class.java))
                //重置变量,避免多次跳转
                appViewModel.shouldNavigateToLogin.value = false
            }
        }

        //实现收藏状态实时更新
        appViewModel.updateItemId.observe(viewLifecycleOwner) {
            LogUtils.d(this@RecommendFragment, "appViewModel.event-->${it}")
            recommendListAdapter.updateCollectionState(it)
        }
    }
}