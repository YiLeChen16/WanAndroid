package com.yl.wanandroid.ui.fragment.search

import androidx.recyclerview.widget.LinearLayoutManager
import com.yl.wanandroid.BR
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.databinding.FragmentSearchResultBinding
import com.yl.wanandroid.ui.adapter.SearchResultListAdapter
import com.yl.wanandroid.utils.LogUtils
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
    companion object {
        private var searchResultFragment: SearchResultFragment? = null
        fun newInstance(): SearchResultFragment {
            if (searchResultFragment == null) {
                searchResultFragment = SearchResultFragment()
            }
            return searchResultFragment!!
        }
    }

    @Inject
    lateinit var mSearchResultListAdapter: SearchResultListAdapter

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
        if (mViewModel.searchHintKeyWord.isNotEmpty()) {
            //将跳转获取到的关键词赋值到当前搜索关键词
            mViewModel.getSearchResultData(mViewModel.searchHintKeyWord)
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.search_fragment_visibility.observe(this) {
            if (it) {
                //此视图可见
                mViewModel.searchResultListData.observe(this) { searchResultDataBean ->
                    if (searchResultDataBean != null) {
                        LogUtils.d(this, "mViewModel.searchResultListData-->$searchResultDataBean")
                        //为适配器装载数据
                        mSearchResultListAdapter.setData(searchResultDataBean.datas)
                        //改变当前视图状态
                        mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
                        mRefreshLayout.finishRefresh()
                    }else{
                        mViewModel.changeStateView(ViewStateEnum.VIEW_EMPTY)
                    }
                }
                //监听当前搜索词
                mViewModel.mCurrentSearchKeyWord.observe(this){
                    //设置视图加载状态:解决列表闪烁问题
                    mViewModel.changeStateView(ViewStateEnum.VIEW_LOADING)
                    mViewModel.getSearchResultData(it)//搜索
                }
            } else {
                //此视图不可见
                mViewModel.changeStateView(ViewStateEnum.VIEW_NONE)//将视图状态设为NONE
            }
        }

        mViewModel.loadMoreSearchResultListData.observe(viewLifecycleOwner) { loadMoreSearchResultDataBean ->
            if(loadMoreSearchResultDataBean != null){
                LogUtils.d(this, "mViewModel.loadMoreSearchResultListData-->$loadMoreSearchResultDataBean")
                //为适配器添加数据
                mSearchResultListAdapter.addData(loadMoreSearchResultDataBean.datas)
                mRefreshLayout.finishLoadMore()
            }
        }

    }

    //重写此方法,获取xml布局中声明的变量id,以为xml布局中声明的变量赋值
    override fun getVariableId(): Int {
        return BR.searchShareViewModel
    }
}