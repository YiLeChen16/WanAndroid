package com.yl.wanandroid.ui.fragment


import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.base.ViewStateEnum
import com.yl.wanandroid.databinding.FragmentHomeBinding
import com.yl.wanandroid.ui.custom.BannerView
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @description: 首页
 * @author YL Chen
 * @date 2024/9/7 15:42
 * @version 1.0
 */
@AndroidEntryPoint
class HomeFragment:BaseVMFragment<FragmentHomeBinding,HomeFragmentViewModel>(R.layout.fragment_home) {

    @Inject
    lateinit var homeFragmentViewModel:HomeFragmentViewModel

    companion object{
        private var homeFragment:HomeFragment? = null
        fun newInstance() :HomeFragment{
            if(homeFragment == null){
                homeFragment = HomeFragment()
            }
            return homeFragment!!
        }
    }

    override fun initVMData() {
        homeFragmentViewModel.getBannerData()
    }

    override fun initView() {
        super.initView()
    }

    override fun observeLiveData() {
        homeFragmentViewModel.bannerData.observe(viewLifecycleOwner){
                bannerData->
            //将数据装载到Banner控件中
            LogUtils.d(this@HomeFragment,"bannerData-->${bannerData.toString()}")
            if(bannerData != null){
                mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
                //自定义控件无法直接使用DataBing，会报空指针异常
                //val bannerView = rootView.findViewById<BannerView>(R.id.banner_view)
                val bannerView = mBinding.root.findViewById<BannerView>(R.id.banner_view)
                LogUtils.d(this@HomeFragment,"initVMData-->$bannerView")
                bannerView.setData(bannerData)
            }
        }
    }

}