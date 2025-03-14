package com.yl.wanandroid.viewmodel.project

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.viewModel.BaseViewModel
import com.yl.wanandroid.model.ArticleDataBean
import com.yl.wanandroid.repository.project.ProjectRepository
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: ProjectTabFragment对应的ViewModel
 * @author YL Chen
 * @date 2024/12/1 22:22
 * @version 1.0
 */
class ProjectTabFragmentViewModel: BaseViewModel() {
    var cid: Int = 0

    //项目数据
    val projectData = MutableLiveData<ArticleDataBean?>()

    private val DEFAULT_PAGE: Int = 1//默认加载页码

    //当前加载页码
    private var currentPage = DEFAULT_PAGE//初始为1

    //更多项目数据
    val moreProjectData = MutableLiveData<ArticleDataBean?>()

    fun getProjectDataByCid(cid:Int){
        launchUI(
            errorCallback = {
                    _, errorMsg->
                TipsToast.showTips(errorMsg)
                projectData.value = null
            },
            requestCall = {
                projectData.value = ProjectRepository.getProjectDataByCid(DEFAULT_PAGE,cid)
            }
        )
    }

    //加载更多
    fun getMoreProjectDataByCid(cid:Int){
        currentPage++
        launchUI(
            errorCallback = {
                    _, _ ->
                moreProjectData.value = null
                currentPage--
            },
            requestCall = {
                moreProjectData.value = ProjectRepository.getProjectDataByCid(currentPage,cid)
            }
        )
    }

    override fun onReload() {
        getProjectDataByCid(cid)
    }
}