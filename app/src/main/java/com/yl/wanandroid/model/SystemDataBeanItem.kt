package com.yl.wanandroid.model
//体系数据模型和课程数据模型
data class SystemDataBeanItem(
    val articleList: List<Any>,
    val author: String,
    val children: List<Children>,
    val courseId: Int,
    val cover: String,
    val desc: String,
    val id: Int,
    val lisense: String,
    val lisenseLink: String,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val type: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)




