package com.yl.wanandroid.viewmodel.project

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.ProjectDataBean
import com.yl.wanandroid.repository.ProjectRepository
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: ProjectTabFragment对应的ViewModel
 * @author YL Chen
 * @date 2024/12/1 22:22
 * @version 1.0
 */
class ProjectTabFragmentViewModel:BaseViewModel() {
    var cid: Int = 0

    //获取对应仓库
    private val projectRepository = getRepository<ProjectRepository>()

    //项目数据
    val projectData = MutableLiveData<ProjectDataBean?>()

    private val DEFAULT_PAGE: Int = 0//默认加载页码

    //当前加载页码
    private var currentPage = DEFAULT_PAGE//初始为0

    //更多项目数据
    val moreProjectData = MutableLiveData<ProjectDataBean?>()

    fun getProjectDataByCid(cid:Int){
        launchUI(
            errorCallback = {
                    _, errorMsg->
                TipsToast.showTips(errorMsg)
                projectData.value = null
            },
            requestCall = {
                projectData.value = projectRepository?.getProjectDataByCid(DEFAULT_PAGE,cid)
            }
        )
    }

    //加载更多
    fun getMoreProjectDataByCid(cid:Int){
        currentPage++
        launchUI(
            errorCallback = {
                _,errorMsg->
                moreProjectData.value = null
                currentPage--
            },
            requestCall = {
                moreProjectData.value = projectRepository?.getProjectDataByCid(currentPage,cid)
            }
        )
    }

    override fun onReload() {
        getProjectDataByCid(cid)
    }
}