package com.yl.wanandroid.ui.fragment.system.left

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.yl.wanandroid.Constant
import com.yl.wanandroid.R
import com.yl.wanandroid.base.fragment.BaseVMFragment
import com.yl.wanandroid.databinding.FragmentSystemChildBinding
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.activity.system.SystemActivity
import com.yl.wanandroid.ui.adapter.system.left.SystemChildContentListAdapter
import com.yl.wanandroid.ui.adapter.system.left.SystemChildLeftListAdapter
import com.yl.wanandroid.ui.custom.LinearLayoutManagerWithScrollTop
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.viewmodel.system.left.SystemChildFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * @description: 体系子页面
 * @author YL Chen
 * @date 2024/12/18 21:57
 * @version 1.0
 */
@AndroidEntryPoint
class SystemChildFragment :
    BaseVMFragment<FragmentSystemChildBinding, SystemChildFragmentViewModel>(R.layout.fragment_system_child),
    SystemChildLeftListAdapter.OnTabTitleClickListener {


    @Inject
    lateinit var systemChildLeftListAdapter: SystemChildLeftListAdapter

    @Inject
    lateinit var systemChildContentListAdapter: SystemChildContentListAdapter


    override fun initView() {
        super.initView()
        //禁用刷新布局
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setEnableLoadMore(false)
        //解决左侧列表闪烁的问题
        mBinding.leftTab.itemAnimator = null
        //左侧列表设置适配器
        mBinding.leftTab.adapter = systemChildLeftListAdapter
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mBinding.leftTab.layoutManager = linearLayoutManager
        systemChildLeftListAdapter.setOnTabTitleClickListener(this)
        //右侧列表设置适配器
        mBinding.contentList.adapter = systemChildContentListAdapter
        val linearLayoutManager1 = LinearLayoutManagerWithScrollTop(context)
        linearLayoutManager1.orientation = LinearLayoutManager.VERTICAL
        mBinding.contentList.layoutManager = linearLayoutManager1
        //RecyclerView双表联动
        mBinding.contentList.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_IDLE) {
                    //当前右侧列表停止滑动
                    //还原左侧列表点击状态,等待下次监听
                    systemChildLeftListAdapter.setClickState(false)
                    mBinding.leftTab.isNestedScrollingEnabled = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                //判断是否为左侧列表条目点击导致的右侧列表滑动
                if (!systemChildLeftListAdapter.isClickState()) {//不是,则为右侧列表被拖拽导致的自身滑动,左侧列表需自动更新滑动
                    //获取第一个完全可见条目
                    val layoutManager =
                        mBinding.contentList.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition =
                        layoutManager.findFirstCompletelyVisibleItemPosition()
                    if (firstVisibleItemPosition >= 0 && firstVisibleItemPosition < systemChildContentListAdapter.itemCount) {
                        //更新左侧列表
                        //在leftTab渲染完毕后再进行滑动
                        mBinding.leftTab.post {
                            updateLeft(firstVisibleItemPosition)
                        }
                    }

                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun updateLeft(firstVisibleItemPosition: Int) {
        //左侧列表滑动
        mBinding.leftTab.smoothScrollToPosition(firstVisibleItemPosition)
        LogUtils.d(this, "position-->${firstVisibleItemPosition}")
        LogUtils.d(this, "mBinding.leftTab.layoutManager-->${mBinding.leftTab.layoutManager}")
        LogUtils.d(
            this, "view-->${
                mBinding.leftTab.layoutManager?.findViewByPosition(
                    firstVisibleItemPosition
                )
            }"
        )
        //左侧列表选中
        //需加判空否则会空指针异常!
        if (mBinding.leftTab.layoutManager?.findViewByPosition(
                firstVisibleItemPosition
            ) != null
        ) {
            systemChildLeftListAdapter.changeSelected(
                mBinding.leftTab.layoutManager?.findViewByPosition(
                    firstVisibleItemPosition
                )!!
            )
        }

    }


    override fun initVMData() {
        mViewModel.getSystemData()
        systemChildContentListAdapter.setOnKeyWordClickListener(mViewModel)
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.systemData.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            //设置数据
            systemChildLeftListAdapter.setData(it)
            systemChildContentListAdapter.setData(it)
            LogUtils.d(this@SystemChildFragment, "systemData-->$it")
            mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)

        }
        //监听跳转
        mViewModel.isGo.observe(viewLifecycleOwner) {
            if (it) {
                //跳转到SystemActivity
                mViewModel.isGo.value = false//重置此值
                val intent = Intent(context, SystemActivity::class.java)
                //携带数据跳转
                val bundle = Bundle()
                bundle.putIntegerArrayList(
                    Constant.SYSTEM_ALL_KEYWORD_CID,
                    mViewModel.keyWordIdList
                )
                bundle.putStringArrayList(Constant.SYSTEM_ALL_KEYWORD, mViewModel.keyWordList)
                bundle.putInt(Constant.SYSTEM_CHOOSE_KEYWORD_CID, mViewModel.chooseCid)
                intent.putExtra(Constant.TO_SYSTEM, bundle)
                startActivity(intent)
            }
        }

    }

    //左侧标题被点击
    override fun onTabTitleClick(position: Int) {
        LogUtils.d(this@SystemChildFragment, "currentPosition-->$position")
        //禁用左侧列表滑动
        mBinding.leftTab.isNestedScrollingEnabled = false
        //监听当前位置，联动右侧RecyclerView
        mBinding.contentList.smoothScrollToPosition(position)

    }

}