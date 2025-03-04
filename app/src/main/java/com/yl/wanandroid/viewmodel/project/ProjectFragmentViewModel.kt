package com.yl.wanandroid.viewmodel.project

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.ProjectCategoryDataBeanItem
import com.yl.wanandroid.repository.ProjectRepository
import com.yl.wanandroid.utils.TipsToast
import javax.inject.Inject

/**
 * @description: ProjectFragment对应ViewModel TODO::
 * @author YL Chen
 * @date 2024/9/7 16:05
 * @version 1.0
 */
class ProjectFragmentViewModel @Inject constructor():BaseViewModel() {

    //项目分类数据
    val projectCategoriesData = MutableLiveData<MutableList<ProjectCategoryDataBeanItem>?>()


    /**
     * 获取项目分类数据
     */
    fun getProjectCategory(){
        launchUI(
            errorCallback = {
                    _, errorMsg->
                TipsToast.showTips(errorMsg)
                projectCategoriesData.value = null
            },
            requestCall = {
                projectCategoriesData.value = ProjectRepository.getProjectCategory()
            }
        )
    }






    override fun onReload() {
        getProjectCategory()
    }
}