package com.yl.wanandroid.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.yl.wanandroid.utils.LogUtils

/**
 * @description: 通用列表条目数据
 * @author YL Chen
 * @date 2024/11/16 17:58
 * @version 1.0
 */
data class ArticleItemData(
    val adminAdd: Boolean,
    val apkLink: String,
    val audit: Int,
    val author: String,
    val canEdit: Boolean,
    val chapterId: Int,
    val chapterName: String,
    val collect: Boolean,
    val courseId: Int,
    val desc: String,
    val descMd: String,
    val envelopePic: String,
    val fresh: Boolean,
    val host: String,
    val id: Int,
    val isAdminAdd: Boolean,
    val link: String,
    val niceDate: String,
    val niceShareDate: String,
    val origin: String,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long,
    val realSuperChapterId: Int,
    val selfVisible: Int,
    val shareDate: Long,
    val shareUser: String,
    val superChapterId: Int,
    val superChapterName: String,
    val tags: List<Tag>,
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int
){
    companion object{
        @JvmStatic
        @BindingAdapter("android:src")
        fun setSrc(view: ImageView, resId: Int) {
            view.setImageResource(resId)
        }
    }

    //TODO::
    //条目收藏按钮被点击
    fun onCollectClick(view: ImageView, collect: Boolean, id:Int) {
        LogUtils.d(this,"SearchData-->onCollectClick----")
    }
}

data class Tag(
    val name: String,
    val url: String
)