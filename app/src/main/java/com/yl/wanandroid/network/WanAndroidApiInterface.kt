package com.yl.wanandroid.network

import com.yl.wanandroid.model.BannerDataBean
import com.yl.wanandroid.model.HarmonyColumnDataBean
import com.yl.wanandroid.model.ArticleItemData
import com.yl.wanandroid.model.NormalWendaDataBean
import com.yl.wanandroid.model.ProjectCategoryDataBeanItem
import com.yl.wanandroid.model.ProjectDataBean
import com.yl.wanandroid.model.RecommendBlogDataBean
import com.yl.wanandroid.model.SearchHotKeyDataBean
import com.yl.wanandroid.model.SearchResultDataBean
import com.yl.wanandroid.model.SystemArticleDataBean
import com.yl.wanandroid.model.SystemDataBeanItem
import com.yl.wanandroid.network.result.BaseResult
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

            val client = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

            val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create()).build()

            return retrofit.create(WanAndroidApiInterface::class.java)
        }
    }

    /**
     * 获取首页Banner数据
     * @return BaseResult<BannerDataBean>
     */
    @GET("/banner/json")
    suspend fun getBannerData(): BaseResult<MutableList<BannerDataBean>>?

    /**
     * 获取首页推荐博客数据
     * @param page Int 页码
     * @return BaseResult<MutableList<RecommendBlogDataBean>>?
     */
    @GET("/article/list/{page}/json")
    suspend fun getRecommendBlog(@Path("page") page: Int): BaseResult<RecommendBlogDataBean>?


    /**
     * 获取搜索热词数据
     * @return BaseResult<MutableList<SearchHotKeyDataBean>>?
     */
    @GET("/hotkey/json")
    suspend fun getSearchHotkey(): BaseResult<MutableList<SearchHotKeyDataBean>>?

    /**
     * 获取搜索关键词数据
     * @param page Int
     * @param k String
     * @return BaseResult<SearchResultDataBean>?
     */
    @POST("/article/query/{page}/json")
    @FormUrlEncoded
    suspend fun getSearchResult(
        @Path("page") page: Int,
        @Field("k") k: String
    ): BaseResult<SearchResultDataBean>?


    /**
     * 获取鸿蒙专栏数据
     * @return BaseResult<HarmonyColumnDataBean>?
     */
    @GET("/harmony/index/json")
    suspend fun getHarmonyColumnData(): BaseResult<HarmonyColumnDataBean>?


    /**
     * 热门问答数据
     * @return BaseResult<MutableList<ItemData>>?
     */
    @GET("/popular/wenda/json")
    suspend fun getHotWendaData(): BaseResult<MutableList<ArticleItemData>>?


    /**
     * 普通问答数据
     * @param page Int
     * @return BaseResult<MutableList<MoreWendaDataBean>>?
     */
    @GET("/wenda/list/{page}/json")
    suspend fun getNormalWendaData(
        @Path("page") page: Int
    ): BaseResult<NormalWendaDataBean>?


    /**
     * 获取项目分类数据
     * @return BaseResult<MutableList<ProjectCategoryDataBeanItem>>?
     */
    @GET("/project/tree/json")
    suspend fun getProjectCategory(): BaseResult<MutableList<ProjectCategoryDataBeanItem>>?

    /**
     * 获取对应分类项目的数据
     * @param page Int
     * @param cid Int
     * @return BaseResult<ProjectItemDataBean>?
     */
    @GET("/project/list/{page}/json")
    suspend fun getProjectDataByCid(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): BaseResult<ProjectDataBean>?


    /**
     * 获取体系数据
     * @return BaseResult<MutableList<SystemDataBeanItem>>?
     */
    @GET("/tree/json")
    suspend fun getSystemData():BaseResult<MutableList<SystemDataBeanItem>>?

    /**
     * 获取知识体系下的文章
     * @param page Int
     * @param cid Int
     * @return BaseResult<SystemArticleDataBean>?
     */
    @GET("/article/list/{page}/json")
    suspend fun getSystemArticleDataByCid(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ):BaseResult<SystemArticleDataBean>?
}