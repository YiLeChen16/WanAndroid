package com.yl.wanandroid.ui.activity

import android.view.View
import android.widget.TextView
import com.yl.wanandroid.BR
import com.yl.wanandroid.Constant
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMActivity
import com.yl.wanandroid.base.ViewStateEnum
import com.yl.wanandroid.databinding.ActivitySearchBinding
import com.yl.wanandroid.ui.adapter.SearchResultListAdapter
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.viewmodel.search.SearchShareViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @description: 搜索界面
 * @author YL Chen
 * @date 2024/10/21 16:50
 * @version 1.0
 */
@AndroidEntryPoint
class SearchActivity :
    BaseVMActivity<ActivitySearchBinding, SearchShareViewModel>(R.layout.activity_search) {

    private var currentSearchHotKey: String? = null
    private var isSearch: Boolean? = false

    @Inject
    lateinit var searchResultListAdapter: SearchResultListAdapter//搜索结果列表适配器

    override fun initData() {
        //获取跳转传递过来的数据
        val extras = intent.extras
        isSearch = extras?.getBoolean(Constant.isSearch, false)//是否由搜索按钮跳转过来
        currentSearchHotKey = extras?.getString(Constant.currentSearchHotKey, "")//推荐搜索热词的order
        LogUtils.d(this,"currentSearchHotKey-->$currentSearchHotKey")
        mBinding.edSearchBox.hint = currentSearchHotKey
        super.initData()
    }


    override fun initVMData() {
        mViewModel.search_fragment_visibility.value = isSearch
        mViewModel.recommendSearchKey = currentSearchHotKey
        LogUtils.d(this,"mViewModel.firstSearchKey-->${mViewModel.recommendSearchKey}")
        mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.isBack.observe(this) {
            //返回上一层
            if (it) {
                finish()
            }
        }
        mViewModel.search_fragment_visibility.observe(this) {
            LogUtils.d(this, "it-->$it")
            if (it) {
                //将当前搜索关键词装载到搜索框中
                mBinding.edSearchBox.setText(currentSearchHotKey)
                //已搜索,将当前搜索框的提示词清空
                mBinding.edSearchBox.hint = ""
                //显示搜索结果列表
                showSearchResult()
            } else {
                //显示搜索界面
                showSearch()
            }
        }
    }

    private fun showSearch() {
        mBinding.searchFragment.visibility = View.VISIBLE
        mBinding.searchResultFragment.visibility = View.GONE
    }

    private fun showSearchResult() {
        mBinding.searchFragment.visibility = View.GONE
        mBinding.searchResultFragment.visibility = View.VISIBLE
    }

    //重写此方法,获取xml布局中声明的变量id,以为xml布局中声明的变量赋值
    override fun getVariableId(): Int {
        return BR.searchShareViewModel
    }
}