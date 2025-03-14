package com.yl.wanandroid.repository.my

import com.yl.wanandroid.utils.CacheDataUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @description:设置界面仓库
 * @author YL Chen
 * @date 2025/3/13 10:32
 * @version 1.0
 */
object SettingRepository {
    suspend fun getTotalCache(): String {
        return withContext(Dispatchers.IO) {
            CacheDataUtils.getTotalCacheSize()
        }
    }

    suspend fun clearCache(){
        return withContext(Dispatchers.IO){
            CacheDataUtils.clearAllCache()
        }
    }
}