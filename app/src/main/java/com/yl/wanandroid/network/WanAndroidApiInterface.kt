package com.yl.wanandroid.network

import com.yl.wanandroid.model.BannerDataBean
import com.yl.wanandroid.network.result.BaseResult
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * 网络请求接口
 */
interface WanAndroidApiInterface {
    companion object {
        private const val BASE_URL = "https://www.wanandroid.com"
        //使用懒加载
        val api: WanAndroidApiInterface by lazy { createApi() }

        /**
         * 通过Retrofit的动态代理生成WanAndroidApiInterface实现类
         * @return WanAndroidApiInterface
         */
        private fun createApi(): WanAndroidApiInterface {
            val httpLoggingInterceptor =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(WanAndroidApiInterface::class.java)
        }
    }

    /**
     * 获取Banner数据
     * @return BaseResult<BannerDataBean>
     */
    @GET("/banner/json")
    suspend fun getBannerData():BaseResult<MutableList<BannerDataBean>>?
}