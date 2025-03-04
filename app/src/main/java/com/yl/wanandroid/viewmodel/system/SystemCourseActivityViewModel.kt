package com.yl.wanandroid.viewmodel.system

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.ArticleDataBean
import com.yl.wanandroid.repository.SystemCourseRepository
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: SystemCourseActivity对应的ViewModel
 * @author YL Chen
 * @date 2025/2/15 16:28
 * @version 1.0
 */
class SystemCourseActivityViewModel : BaseViewModel() {

    var courseId = -1
    var courseName =
        ObservableField<String>()//ObservableField此种类型的变量才可被xml中的变量绑定!!不可用MutableLiveData!!!
    val sortState = MutableLiveData(false)

    val courseArticleData = MutableLiveData<ArticleDataBean?>()
    //加载更多数据
    val loadMoreCourseArticleData = MutableLiveData<ArticleDataBean?>()


    private val DEFAULT_PAGE: Int = 0//默认加载页码

    private var currentPage = DEFAULT_PAGE//当前页码



    //排序按钮被点击
    fun onSortClick() {
        //切换选中状态
        sortState.value = !sortState.value!!
        //修改当前加载页面
        currentPage = DEFAULT_PAGE
        //判断正序还是倒序
        getSystemCourseArticleDataByCid(courseId,judgeSort())
    }

    fun judgeSort() :Int{
        return if (sortState.value != null && sortState.value == true) {
            //倒序
            0
        } else {
            //正序
            1
        }
    }

    fun getSystemCourseArticleDataByCid(cid: Int, order: Int = 1) {
        launchUI(errorCallback = { _, errMsg ->
            TipsToast.showTips(errMsg)
            courseArticleData.value = null
        }, requestCall = {
            courseArticleData.value =
                SystemCourseRepository.getSystemCourseArticleDataByCid(DEFAULT_PAGE,cid, order)
        })
    }

    //加载更多
    fun loadMoreSystemCourseArticleDataByCid(cid: Int, order: Int = 1) {
        //页数加一
        currentPage++
        launchUI(errorCallback = { _, errMsg ->
            TipsToast.showTips(errMsg)
            loadMoreCourseArticleData.value = null
            //页数回退
            currentPage--
        }, requestCall = {
            loadMoreCourseArticleData.value =
                SystemCourseRepository.getSystemCourseArticleDataByCid(currentPage,cid, order)
        })
    }

    override fun onReload() {
       getSystemCourseArticleDataByCid(courseId,judgeSort())
    }
}