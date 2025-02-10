package com.yl.wanandroid.model

/**
 * @description: TODO
 * @author YL Chen
 * @date 2025/2/10 18:20
 * @version 1.0
 */
data class Children(
    val articleList: List<ArticleItemData>,
    val author: String,
    val children: List<Any>,
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