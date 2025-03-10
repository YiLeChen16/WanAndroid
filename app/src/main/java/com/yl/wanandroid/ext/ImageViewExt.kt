package com.yl.wanandroid.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.yl.wanandroid.R
import com.yl.wanandroid.app.ActivityManager
import java.io.File

/**
 * @description: ImageView扩展方法
 * @author YL Chen
 * @date 2025/3/10 21:29
 * @version 1.0
 */
/**
 * 加载图片，开启缓存
 * @param url 图片地址
 */
fun ImageView.setUrl(url: String?) {
    if (ActivityManager.isActivityDestroy(context)) {
        return
    }
    Glide.with(context).load(url)
        .placeholder(R.drawable.img_load_failure) // 占位符，异常时显示的图片
        .error(R.drawable.img_load_failure) // 错误时显示的图片
        .skipMemoryCache(false) //启用内存缓存
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE) //磁盘缓存策略
        .into(this)
}

/**
 * 加载图片，开启缓存
 * @param url 图片地址
 */
fun ImageView.loadFile(file: File?) {
    if (ActivityManager.isActivityDestroy(context)) {
        return
    }
    //请求配置
    val options = RequestOptions.circleCropTransform()
    Glide.with(context).load(file)
        .placeholder(R.drawable.img_load_failure) // 占位符，异常时显示的图片
        .error(R.drawable.img_load_failure) // 错误时显示的图片
        .skipMemoryCache(false) //启用内存缓存
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE) //磁盘缓存策略
        .apply(options) // 圆形
        .into(this)
}

