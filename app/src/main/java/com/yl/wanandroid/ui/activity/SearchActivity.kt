package com.yl.wanandroid.ui.activity

import android.view.View
import com.yl.wanandroid.Constant
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMActivity
import com.yl.wanandroid.base.ViewStateEnum
import com.yl.wanandroid.databinding.ActivitySearchBinding
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.viewmodel.search.SearchActivityViewModel

/**
 * @description: 搜索界面
 * @author YL Chen
 * @date 2024/10/21 16:50
 * @version 1.0
 */
class SearchActivity:BaseVMActivity<ActivitySearchBinding, SearchActivityViewModel>(R.layout.activity_search){

    private var currentSearchHotKey: Int? = 0
    private var isSearch: Boolean? = false

    override fun initData() {
        super.initData()
        //获取跳转传递过来的数据
        val extras = intent.extras
        isSearch = extras?.getBoolean(Constant.isSearch, false)//是否由搜索列表跳转过来
        currentSearchHotKey = extras?.getInt(Constant.currentSearchHotKeyOrder, 1)//推荐搜索热词的order
        //必须加!!!!!!否则databinding无效!!!!!!
        mBinding.searchActivityViewModel=mViewModel
    }

    override fun initVMData() {
        mViewModel.getSearchHotkeyData()
/*        mBinding.ivBack.setOnClickListener{
            finish()
        }*/
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.isBack.observe(this){
            //返回上一层
            if (it){
                finish()
            }
        }
        mViewModel.search_fragment_visibility.observe(this){
            LogUtils.d(this,"it-->$it")
            if (it){
                mBinding.searchFragmentLayout.visibility = View.VISIBLE
                mBinding.searchResultFragmentLayout.visibility = View.GONE
            }else{
                mBinding.searchFragmentLayout.visibility = View.GONE
                mBinding.searchResultFragmentLayout.visibility = View.VISIBLE
            }
        }
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