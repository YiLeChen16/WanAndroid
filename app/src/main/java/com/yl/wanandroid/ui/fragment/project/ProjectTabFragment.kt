package com.yl.wanandroid.ui.fragment.project

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.databinding.FragmentProjectTabBinding
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.adapter.ProjectListAdapter
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast
import com.yl.wanandroid.viewmodel.project.ProjectTabFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @description: 项目页面的ViewPager条目Fragment TODO::
 * @author YL Chen
 * @date 2024/12/1 22:15
 * @version 1.0
 */
@AndroidEntryPoint
class ProjectTabFragment() :
    BaseVMFragment<FragmentProjectTabBinding, ProjectTabFragmentViewModel>(R.layout.fragment_project_tab) {
    constructor(cid: Int) : this() {
        this.cid = cid
    }

    private var cid: Int = 0

    @Inject
    lateinit var projectListAdapter: ProjectListAdapter

    override fun initView() {
        super.initView()
        //为列表设置适配器和布局管理器
        mBinding.list.adapter = projectListAdapter
        mBinding.list.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        //监听刷新和加载更多事件
        mRefreshLayout.setOnRefreshListener {
            mViewModel.getProjectDataByCid(cid)
        }
        mRefreshLayout.setOnLoadMoreListener {
            mViewModel.getMoreProjectDataByCid(cid)
        }
    }

    override fun initVMData() {
        mViewModel.cid = cid
        mViewModel.getProjectDataByCid(mViewModel.cid)
    }

    override fun observeLiveData() {
        mViewModel.projectData.observe(viewLifecycleOwner) {
            LogUtils.d(this, " mViewModel.projectData-->${it}")
            if (it != null) {
                //装载数据
                projectListAdapter.setData(it.datas)
                mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
            } else {
                TipsToast.showTips(R.string.tip_null)
                mViewModel.changeStateView(ViewStateEnum.VIEW_NET_ERROR)
            }
            mRefreshLayout.finishRefresh()
            mRefreshLayout.finishLoadMore()
        }
        mViewModel.moreProjectData.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.curPage == it.pageCount + 1) {
                    //最后一页后的一页,不包含数据
                    TipsToast.showTips(R.string.tip_toast_last_page)
                    mRefreshLayout.setEnableLoadMore(false)//标记当前没有更多数据了
                } else {
                    //装载数据
                    projectListAdapter.addData(it.datas)
                    mRefreshLayout.finishLoadMore()
                    TipsToast.showTips(R.string.tip_toast_load_more_success)
                }
            } else {
                TipsToast.showTips(R.string.tip_null)
                //若请求数据为空,则保留当前页面即可
                mRefreshLayout.finishLoadMore()
            }
        }
    }

}