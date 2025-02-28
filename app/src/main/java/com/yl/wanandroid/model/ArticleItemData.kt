package com.yl.wanandroid.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.yl.wanandroid.R
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.viewmodel.home.CollectViewModel

/**
 * @description: 通用列表条目数据
 * @author YL Chen
 * @date 2024/11/16 17:58
 * @version 1.0
 */
data class ArticleItemData(
    val adminAdd: Boolean?,
    val apkLink: String?,
    val audit: Int,
    val author: String,
    val canEdit: Boolean,
    val chapterId: Int,
    val chapterName: String,
    var collect: Boolean,
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
    val niceShareDate: String?,
    val origin: String,
    val originId: Int = -1,//此为收藏页面文章原本的id,只有收藏页面的json会有此字段
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
) {
    companion object {
        /* @JvmStatic
         @BindingAdapter("android:src")
         fun setSrc(view: ImageView, resId: Int) {
             view.setImageResource(resId)
         }*/

        /*   @BindingAdapter("app:imgUrl")
           @JvmStatic
           fun load(imageView: ImageView, collect: Boolean) {
               if (collect){
                   imageView.setImageResource(R.drawable.star)
               }else{
                   imageView.setImageResource(R.drawable.no_star)
               }
           }*/
    }


    //TODO::写到适配器中
    //条目收藏按钮被点击
    /*    fun onCollectClick(view: ImageView, collect: Boolean, id:Int) {
            val collectActivityViewModel = CollectViewModel()
            //view.setImageResource()
            if (!collect){
                //收藏
                view.setImageResource(R.drawable.star)
                collectActivityViewModel.collectArticle(id)
            }else{
                //取消收藏
                view.setImageResource(R.drawable.no_star)
                //判断来自哪个页面
                if (this.niceShareDate.isNullOrEmpty()){
                    //来自"我的收藏"页面
                    collectActivityViewModel.cancelMyCollectArticle(id)
                }else{
                    //来自"文章列表"
                    collectActivityViewModel.cancelCollectArticle(id)
                }
            }
            this.collect = !collect
            LogUtils.d(this,"SearchData-->onCollectClick----")
        }*/
}

data class Tag(
    val name: String,
    val url: String
)