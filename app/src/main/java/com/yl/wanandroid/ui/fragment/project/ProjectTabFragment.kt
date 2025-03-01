package com.yl.wanandroid.ui.fragment.project

import android.content.Intent
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.yl.wanandroid.R
import com.yl.wanandroid.app.AppViewModel
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.databinding.FragmentProjectTabBinding
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.activity.LoginActivity
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

    @Inject
    lateinit var appViewModel: AppViewModel

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
        //避免条目点击收藏按钮时刷新条目执行了动画操作导致了刷新闪烁问题
        //关闭动画效果
        val sa: SimpleItemAnimator = mBinding.list.itemAnimator as SimpleItemAnimator
        sa.supportsChangeAnimations = false
        //设置动画为空
        mBinding.list.itemAnimator = null
    }

    override fun initVMData() {
        mViewModel.cid = cid
        mViewModel.getProjectDataByCid(mViewModel.cid)

        //设置收藏监听事件
        projectListAdapter.setOnCollectionEventListener(appViewModel)
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
        appViewModel.isUserLogin.observe(this){
            //跳转到登录页面
            startActivity(Intent(context, LoginActivity::class.java))
        }
        //实现收藏页面取消收藏时此界面的列表收藏状态也能实时更新
        appViewModel.event.observe(viewLifecycleOwner) {
            LogUtils.d(this@ProjectTabFragment, "appViewModel.event-->${it}")
            projectListAdapter.updateCollectionState(it.originId)//收藏页面的originId对应此页面的id
        }
    }

}