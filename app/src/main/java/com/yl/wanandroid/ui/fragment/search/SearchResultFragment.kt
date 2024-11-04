package com.yl.wanandroid.ui.fragment.search

import androidx.recyclerview.widget.LinearLayoutManager
import com.yl.wanandroid.BR
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.base.ViewStateEnum
import com.yl.wanandroid.databinding.FragmentSearchResultBinding
import com.yl.wanandroid.model.SearchResultDataBean
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

    override fun initVMData() {
        if(mViewModel.recommendSearchKey != null){
            mViewModel.getSearchResultData(mViewModel.recommendSearchKey!!)
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.search_fragment_visibility.observe(this){
            if (it){
                //此视图可见
                mViewModel.searchResultListData.observe(this) {
                    searchResultDataBean->
                    if (searchResultDataBean != null) {
                        //TODO::
                        LogUtils.d(this,"mViewModel.searchResultList-->$it")
                        //为适配器装载数据
                        mSearchResultListAdapter.setData(searchResultDataBean.datas)
                        //为列表设置适配器
                        mBinding.searchResultList.adapter = mSearchResultListAdapter
                        //为列表设置布局管理器
                        mBinding.searchResultList.layoutManager = LinearLayoutManager(context)
                        mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
                    }
                }
            }else{
                //此视图不可见
                mViewModel.changeStateView(ViewStateEnum.VIEW_NONE)//将视图状态设为NONE
            }
        }

    }

    //重写此方法,获取xml布局中声明的变量id,以为xml布局中声明的变量赋值
    override fun getVariableId(): Int {
        return BR.searchShareViewModel
    }
}