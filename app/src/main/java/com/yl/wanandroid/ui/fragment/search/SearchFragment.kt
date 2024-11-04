package com.yl.wanandroid.ui.fragment.search

import com.yl.wanandroid.BR
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.base.ViewStateEnum
import com.yl.wanandroid.databinding.FragmentSearchBinding
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.viewmodel.search.SearchShareViewModel


/**
 * @description: 搜索记录和推荐搜索fragment
 * @author YL Chen
 * @date 2024/10/25 20:14
 * @version 1.0
 */
class SearchFragment :
    BaseVMFragment<FragmentSearchBinding, SearchShareViewModel>(R.layout.fragment_search) {
    companion object{
        private var searchFragment: SearchFragment? = null
        fun newInstance() : SearchFragment {
            if(searchFragment == null){
                searchFragment = SearchFragment()
            }
            return searchFragment!!
        }
    }

    override fun initVMData() {
        mViewModel.getSearchHotkeyData()//获取从首页搜索按钮跳转过来的搜索关键词数据
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.search_fragment_visibility.observe(this) {
            if (!it) {
                //此视图可见
                mViewModel.searchHotKeyData.observe(this) {
                    //TODO::设置数据给自定义View
                    LogUtils.d(this, "mViewModel.searchHotKeyData-->$it")
                    if (it != null) {
                        mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)//切换视图状态为成功状态
                    }
                }
            } else {
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