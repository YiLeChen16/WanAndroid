package com.yl.wanandroid.ui.fragment.home.search

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.yl.wanandroid.BR
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.databinding.FragmentSearchBinding
import com.yl.wanandroid.ui.adapter.RecommendSearchListAdapter
import com.yl.wanandroid.ui.custom.HistoryFlowLayoutView
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.viewmodel.search.SearchShareViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * @description: 搜索记录和推荐搜索fragment
 * @author YL Chen
 * @date 2024/10/25 20:14
 * @version 1.0
 */
@AndroidEntryPoint
class SearchFragment :
    BaseVMFragment<FragmentSearchBinding, SearchShareViewModel>(R.layout.fragment_search),
    HistoryFlowLayoutView.OnItemClickListener, RecommendSearchListAdapter.OnItemClickListener {
    private lateinit var mSearchHistories: HistoryFlowLayoutView


    @Inject
    lateinit var mRecommendSearchListAdapter: RecommendSearchListAdapter

    override fun initView() {
        super.initView()
        //禁止加载
        mRefreshLayout.setEnableLoadMore(false)

        //设置刷新监听
        mRefreshLayout.setOnRefreshListener{
            mViewModel.getSearchHotkeyData()
        }

        //初始化历史搜索记录自定义View
        mSearchHistories = mBinding.root.findViewById(R.id.search_histories)
        //为推荐搜索列表设置适配器
        mBinding.recommendSearchList.adapter = mRecommendSearchListAdapter
        mBinding.recommendSearchList.layoutManager = GridLayoutManager(context, 2)//两列的列表

        //为自定义View设置监听事件
        mSearchHistories.setListener(this)

        //为推荐搜索列表条目设置监听事件
        mRecommendSearchListAdapter.setOnItemClickListener(this)
    }

    override fun initVMData() {
        mViewModel.getSearchHotkeyData()//获取从首页搜索按钮跳转过来的搜索关键词数据
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.search_fragment_visibility.observe(this) {
            if (!it) {
                //此视图可见
                //触发更新
                mViewModel.searchHotKeyData.value = mViewModel.searchHotKeyData.value
                mViewModel.searchHistoriesList.value = mViewModel.searchHistoriesList.value
            } else {
                //此视图不可见
                mViewModel.changeStateView(ViewStateEnum.VIEW_NONE)//将视图状态设为NONE
            }
        }
        //推荐搜索关键词数据
        mViewModel.searchHotKeyData.observe(this) { searchHotKeyDataBeans ->
            if(mViewModel.search_fragment_visibility.value == false){
                //只在可见时对值进行设置
                LogUtils.d(this, "mViewModel.searchHotKeyData-->$searchHotKeyDataBeans")
                if (searchHotKeyDataBeans != null) {
                    mBinding.recommendSearch.visibility = View.VISIBLE
                    mViewModel.isNoNetWork = false
                    mRecommendSearchListAdapter.setData(searchHotKeyDataBeans)
                }else{
                    //隐藏推荐搜索列表
                    mBinding.recommendSearch.visibility = View.INVISIBLE
                }
                mRefreshLayout.finishRefresh()
                mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)//切换视图状态为成功状态
            }
        }
        //历史搜索关键词数据
        //不可将此部分代码嵌套在mViewModel.search_fragment_visibility.observe(this) {
        //            if (!it) {..}..}中,因为会导致mSearchHistories.setData(searchHistoriesData)被反复调用,导致历史记录自定义View重复绘制子View
        mViewModel.searchHistoriesList.observe(this) { searchHistoriesData ->
            if(mViewModel.search_fragment_visibility.value == false){
                //只在可见时对值进行设置
                LogUtils.d(this, "mViewModel.searchHistoriesList-->$searchHistoriesData")
                if (searchHistoriesData != null) {
                    mSearchHistories.setData(searchHistoriesData)
                }
            }
        }
    }

    //重写此方法,获取xml布局中声明的变量id,以为xml布局中声明的变量赋值
    override fun getVariableId(): Int {
        return BR.searchShareViewModel
    }

    /**
     * 历史搜索记录自定义View的监听回调事件
     */
    override fun onKeyWordClick(k: String) {
        //历史记录被点击
        /*
        顺序不能改变,不然会导致可以正常搜索,但搜索框的数据不会设置为当前搜索的k.
        因为搜索框的数据显示是由search_fragment_visibility控制的,
        但是只在监听到SearchResultFragment可见时立马设置进去的
        若先设置可见性,则再更新关键词,会导致提前触发搜索框数据修改为未更新前的关键词
        */
        //将当前搜索关键词放入搜索框中
        mViewModel.mCurrentSearchKeyWord.value = k//更新当前搜索词
        //隐藏当前布局,显示搜索结果布局
        mViewModel.search_fragment_visibility.value = true
    }

    override fun onDeleteButtonClick(id: Int) {
        //某历史记录删除按钮被点击
        //通知viewModel删除对应数据
        mViewModel.deleteSearchHistory(id)
    }

    override fun onAllHistoriesDelete() {
        //全部删除按钮被点击
        //通知viewModel删除全部历史数据
        mViewModel.deleteAllSearchHistory()
    }

    /**
     * 推荐搜索列表条目点击事件
     * @param k String
     */
    override fun onItemClick(k: String) {
        //点击搜索对应关键词
        /*
        顺序不能改变,不然会导致可以正常搜索,但搜索框的数据不会设置为当前搜索的k.
        因为搜索框的数据显示是由search_fragment_visibility控制的,
        但是只在监听到SearchResultFragment可见时立马设置进去的
        若先设置可见性,则再更新关键词,会导致提前触发搜索框数据修改为未更新前的关键词
        */
        //将当前搜索关键词放入搜索框中
        mViewModel.mCurrentSearchKeyWord.value = k//更新当前搜索词
        //隐藏当前布局,显示搜索结果布局
        mViewModel.search_fragment_visibility.value = true
    }
}