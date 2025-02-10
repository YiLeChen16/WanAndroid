package com.yl.wanandroid.ui.fragment.system

import androidx.recyclerview.widget.LinearLayoutManager
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.databinding.FragmentSystemActivityBinding
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.adapter.BlogListAdapter
import com.yl.wanandroid.utils.TipsToast
import com.yl.wanandroid.viewmodel.system.SystemActivityFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @description: SystemActivity的ViewPager下的每个Fragment
 * @author YL Chen
 * @date 2025/2/7 16:33
 * @version 1.0
 */
@AndroidEntryPoint
class SystemActivityFragment() :
    BaseVMFragment<FragmentSystemActivityBinding, SystemActivityFragmentViewModel>(
        R.layout.fragment_system_activity
    ) {
    private var cid: Int = 0

    constructor(cid: Int) : this() {
        this.cid = cid
    }

    @Inject
    lateinit var articleListAdapter: BlogListAdapter

    override fun initView() {
        super.initView()
        //为RecyclerView设置适配器
        mBinding.articleList.adapter = articleListAdapter
        mBinding.articleList.layoutManager = LinearLayoutManager(context)
        //刷新监听,加载更多监听
        mRefreshLayout.setOnRefreshListener{
            mViewModel.getSystemArticleDataByCid(mViewModel.cid)
        }
        mRefreshLayout.setOnLoadMoreListener {
            mViewModel.loadMOreSystemArticleDataByCid()
        }
    }

    override fun initVMData() {
        mViewModel.cid = cid
        mViewModel.getSystemArticleDataByCid(cid)
    }

    override fun observeLiveData() {
        mViewModel.systemArticleData.observe(viewLifecycleOwner) {
            if (it == null) {
                mViewModel.changeStateView(ViewStateEnum.VIEW_NET_ERROR)
                return@observe
            }
            //为适配器设置数据
            articleListAdapter.setData(it.datas)
            mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
            mRefreshLayout.finishRefresh()
        }
        //更多数据
        mViewModel.loadMoreSystemArticleData.observe(viewLifecycleOwner){
            if (it != null){
                mRefreshLayout.finishLoadMore()
                if (it.curPage == it.pageCount + 1){
                    //最后一页的下一页
                    TipsToast.showTips(R.string.tip_toast_last_page)
                    mRefreshLayout.finishLoadMoreWithNoMoreData()//标记当前没有更多数据了
                }else{
                    articleListAdapter.addData(it.datas)
                    TipsToast.showTips(R.string.tip_toast_load_more_success)
                }
            }else{
                mRefreshLayout.finishLoadMore()
            }
        }
    }
}