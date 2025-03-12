package com.yl.wanandroid.ui.fragment.my

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.databinding.FragmentMyBinding
import com.yl.wanandroid.model.Children
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.activity.CollectActivity
import com.yl.wanandroid.ui.adapter.MyFragmentTabViewPagerAdapter
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.viewmodel.my.MyFragmentViewModel
import com.yl.wanandroid.BR
import com.yl.wanandroid.Constant
import com.yl.wanandroid.app.AppViewModel
import com.yl.wanandroid.ui.activity.IntegralActivity
import com.yl.wanandroid.ui.activity.LoginActivity
import com.yl.wanandroid.ui.activity.SearchActivity
import com.yl.wanandroid.ui.activity.SettingActivity
import com.yl.wanandroid.ui.activity.ShareActivity
import com.yl.wanandroid.ui.activity.UserInfoActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @description: 我的
 * @author YL Chen
 * @date 2024/9/7 16:02
 * @version 1.0
 */
@AndroidEntryPoint
class MyFragment : BaseVMFragment<FragmentMyBinding, MyFragmentViewModel>(R.layout.fragment_my) {


    private var myFragmentTabViewPagerAdapter: MyFragmentTabViewPagerAdapter? = null

    @Inject
    lateinit var appViewModel: AppViewModel

    override fun initView() {
        super.initView()
        //禁用刷新布局
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setEnableLoadMore(false)
        myFragmentTabViewPagerAdapter = MyFragmentTabViewPagerAdapter(this)
        //为ViewPager设置适配器
        mBinding.tabViewPager.adapter = myFragmentTabViewPagerAdapter
        // 添加 ViewPager2 的页面变化监听
        mBinding.tabViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                mBinding.recommendTab.getTabAt(position)?.select() // 更新 Tab 选中状态
            }
        })
        //TabLayout选中监听事件
        mBinding.recommendTab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab == null) {
                    return
                }
                // 设置 ViewPager2 当前项
                mBinding.tabViewPager.currentItem = tab.position
                //选中.字体加粗
                val tabSelected = tab.customView as TextView
                tabSelected.setTextColor(context!!.getColor(R.color.md_theme_primary))
                tabSelected.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                tabSelected.text = (tab.customView as TextView).text
                LogUtils.d(this@MyFragment, "${tabSelected.text}")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab == null) {
                    return
                }
                //未选中
                val tabSelected = tab.customView as TextView
                tabSelected.setTextColor(context!!.getColor(R.color.md_theme_onSurface))
                tabSelected.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
                tabSelected.text = (tab.customView as TextView).text
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })


    }

    //初始化tab条目的样式和值
    private fun initTab(items: MutableList<Children>) {
        for (i in 0 until items.size) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_tab, null) as TextView
            view.text = items[i].name
            view.tag = items[i].id//将分类tab的id存入tag中
            val tab: TabLayout.Tab = mBinding.recommendTab.newTab().setCustomView(view)
            mBinding.recommendTab.addTab(tab)
        }
        LogUtils.d(this, "initTab-->mBinding.tab.tabCount==${mBinding.recommendTab.tabCount}")
    }

    override fun initVMData() {
        //获取分类数据
        mViewModel.getWxArticleTabs()

        //判断用户是否已经登录
        if (appViewModel.isUserLogin()) {
            //已登录
            //获取积分信息
            mViewModel.getCoinInfo()
            //获取用户信息
            mViewModel.getUserInfo()
        }

    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.wxArticleTabs.observe(viewLifecycleOwner) {
            if (it == null)
                return@observe
            LogUtils.d(this@MyFragment, "it-->${it}")
            initTab(it)
            myFragmentTabViewPagerAdapter?.setFragmentSize(it)
            mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
        }

        mViewModel.gotoCollection.observe(viewLifecycleOwner) {
            if (it) {
                //判断登录
                if (appViewModel.isUserLogin()) {
                    startActivity(Intent(context, CollectActivity::class.java))
                }
                mViewModel.gotoCollection.value = false//重置变量
            }

        }

        mViewModel.gotoSearch.observe(viewLifecycleOwner) {
            if (it) {
                //判断登录
                if (appViewModel.isUserLogin()) {
                    //跳转到搜索界面
                    val intent = Intent(context, SearchActivity::class.java)
                    val bundle = Bundle()
                    bundle.putBoolean(Constant.IS_SEARCH, false)//表示从搜索框跳转过去
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
                mViewModel.gotoSearch.value = false//重置变量
            }
        }

        mViewModel.gotoMyIntegral.observe(viewLifecycleOwner) {
            if (it) {
                //跳转到积分页面(需登录)
                if (appViewModel.isUserLogin()) {
                    startActivity(Intent(context, IntegralActivity::class.java))
                }
                mViewModel.gotoMyIntegral.value = false//重置变量
            }
        }

        appViewModel.shouldNavigateToLogin.observe(viewLifecycleOwner) {
            if (it) {
                //重新请求数据
                mViewModel.getCoinInfo()
                mViewModel.getWxArticleTabs()
                //跳转到登录页面
                startActivity(Intent(context,LoginActivity::class.java))
                //重置变量,避免多次跳转
                appViewModel.shouldNavigateToLogin.value = false
            }
        }

        mViewModel.gotoSetting.observe(viewLifecycleOwner) {
            if (it) {
                //跳转到设置界面
                startActivity(Intent(context, SettingActivity::class.java))
                mViewModel.gotoSetting.value = false//重置变量
            }
        }

        mViewModel.gotoMyInfo.observe(this) {
            if (it) {
                if (appViewModel.isUserLogin()) {
                    //跳转到个人信息界面
                    startActivity(Intent(context, UserInfoActivity::class.java))
                    mViewModel.gotoMyInfo.value = false
                }

            }
        }

        mViewModel.gotoMyShare.observe(viewLifecycleOwner) {
            if (it) {
                //跳转到分享界面
                startActivity(Intent(context, ShareActivity::class.java))
                mViewModel.gotoMyShare.value = false//重置变量
            }
        }


    }

    override fun getVariableId(): Int {
        return BR.myFragmentViewModel
    }
}