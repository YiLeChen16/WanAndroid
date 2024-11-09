package com.yl.wanandroid.ui.fragment.search

import androidx.recyclerview.widget.GridLayoutManager
import com.yl.wanandroid.BR
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.databinding.FragmentSearchBinding
import com.yl.wanandroid.ui.adapter.RecommendSearchListAdapter
import com.yl.wanandroid.ui.custom.FlowLayoutView
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.viewmodel.search.SearchShareViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * @description: 搜索记录和推荐搜索fragment  TODO::自定义View
 * @author YL Chen
 * @date 2024/10/25 20:14
 * @version 1.0
 */
@AndroidEntryPoint
class SearchFragment :
    BaseVMFragment<FragmentSearchBinding, SearchShareViewModel>(R.layout.fragment_search),
    FlowLayoutView.OnItemClickListener, RecommendSearchListAdapter.OnItemClickListener {
    private lateinit var mSearchHistories: FlowLayoutView

    companion object {
        private var searchFragment: SearchFragment? = null
        fun newInstance(): SearchFragment {
            if (searchFragment == null) {
                searchFragment = SearchFragment()
            }
            return searchFragment!!
        }
    }

    @Inject
    lateinit var mRecommendSearchListAdapter: RecommendSearchListAdapter

    override fun initView() {
        super.initView()
        //禁止刷新和加载
        mRefreshLayout.setEnableLoadMore(false)
        mRefreshLayout.setEnableRefresh(false)

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
                //推荐搜索关键词数据
                mViewModel.searchHotKeyData.observe(this) { searchHotKeyDataBeans ->
                    LogUtils.d(this, "mViewModel.searchHotKeyData-->$searchHotKeyDataBeans")
                    if (searchHotKeyDataBeans != null) {
                        mRecommendSearchListAdapter.setData(searchHotKeyDataBeans)
                        mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)//切换视图状态为成功状态
                    }
                }
                //历史搜索关键词数据
                mViewModel.searchHistoriesList.observe(this) { searchHistoriesData ->
                    LogUtils.d(this, "mViewModel.searchHistoriesList-->$searchHistoriesData")
                    if (searchHistoriesData != null) {
                        mSearchHistories.setData(searchHistoriesData)
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

    override fun onDeleteButtonClick(k: String) {
        //某历史记录删除按钮被点击
        //通知viewModel删除对应数据
        mViewModel.searchHistoriesList.value?.remove(k)
    }

    override fun onAllHistoriesDelete() {
        //全部删除按钮被点击
        //通知viewModel删除全部历史数据
        mViewModel.searchHistoriesList.value?.clear()
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