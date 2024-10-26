package com.yl.wanandroid.ui.fragment.search

import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.base.ViewStateEnum
import com.yl.wanandroid.databinding.FragmentSearchResultBinding
import com.yl.wanandroid.viewmodel.search.SearchResultFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @description: 搜索结果Fragment
 * @author YL Chen
 * @date 2024/10/25 20:18
 * @version 1.0
 */
class SearchResultFragment :
    BaseVMFragment<FragmentSearchResultBinding, SearchResultFragmentViewModel>(
        R.layout.fragment_search_result
    ) {
    override fun initVMData() {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
/*        scope.launch {
            //测试模拟加载延迟
            delay(2000)
            //模拟加载成功
            mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
        }*/
    }
}