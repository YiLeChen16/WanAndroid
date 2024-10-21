package com.yl.wanandroid.ui.fragment.home


import androidx.recyclerview.widget.LinearLayoutManager
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.base.ViewStateEnum
import com.yl.wanandroid.databinding.FragmentRecommendBinding
import com.yl.wanandroid.ui.adapter.RecommendListAdapter
import com.yl.wanandroid.ui.custom.SearchBoxView
import com.yl.wanandroid.utils.LogUtils
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
    companion object {
        private var recommendFragment: RecommendFragment? = null
        fun newInstance(): RecommendFragment {
            if (recommendFragment == null) {
                recommendFragment = RecommendFragment()
            }
            return recommendFragment!!
        }
    }

    @Inject lateinit var recommendListAdapter:RecommendListAdapter

    override fun initView() {
        super.initView()
        //初始化监听事件
/*        mBinding.edSearchBox.setOnClickListener{
            //点击跳转到搜索界面

        }
        mBinding.btnSearch.setOnClickListener {
            //点击跳转到搜索界面并搜索

        }*/
    }

    override fun initVMData() {
        //获取推荐博客数据
        mViewModel.getRecommendBlogData()
        //获取搜索热词
        mViewModel.getSearchHotkeyData()
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.recommendBlogData.observe(viewLifecycleOwner) { recommendBlogData ->
            //将数据装载到列表中
            if(recommendBlogData != null){
                val datas = recommendBlogData.datas
                recommendListAdapter.setData(datas)
                mBinding.list.layoutManager = LinearLayoutManager(context)
                mBinding.list.adapter = recommendListAdapter
                //显示加载成功界面
                mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
            }
        }

        mViewModel.searchHotKeyData.observe(viewLifecycleOwner){
            searchHotKeyData->
            if(searchHotKeyData != null){
                //将请求回来的数据在搜索框中轮播
                LogUtils.d(this,"searchHotKeyData-->$searchHotKeyData")
                //设置数据
                val searchBoxView = mBinding.root.findViewById<SearchBoxView>(R.id.search_box_view)
                searchBoxView.setData(searchHotKeyData)
            }
        }
    }


}