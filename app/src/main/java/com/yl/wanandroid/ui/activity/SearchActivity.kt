package com.yl.wanandroid.ui.activity

import com.yl.wanandroid.Constant
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMActivity
import com.yl.wanandroid.base.ViewStateEnum
import com.yl.wanandroid.databinding.ActivitySearchBinding
import com.yl.wanandroid.viewmodel.SearchActivityViewModel

/**
 * @description: 搜索界面
 * @author YL Chen
 * @date 2024/10/21 16:50
 * @version 1.0
 */
class SearchActivity:BaseVMActivity<ActivitySearchBinding,SearchActivityViewModel>(R.layout.activity_search){

    private var currentSearchHotKey: Int? = 0

    override fun initData() {
        super.initData()
        //获取跳转传递过来的数据
        val extras = intent.extras
        currentSearchHotKey = extras?.getInt(Constant.currentSearchHotKeyOrder, 1)
    }
    override fun initVMData() {
        mViewModel.getSearchHotkeyData()
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.searchHotKeyData.observe(this){
                searchHotKeyData->
            if(searchHotKeyData != null){
                //设置数据
                mBinding.edSearchBox.hint = searchHotKeyData[currentSearchHotKey!!].name
                mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
            }
        }
    }
}