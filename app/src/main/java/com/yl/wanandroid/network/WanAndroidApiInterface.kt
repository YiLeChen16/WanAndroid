package com.yl.wanandroid.network

import com.yl.wanandroid.model.ArticleDataBean
import com.yl.wanandroid.model.ArticleItemData
import com.yl.wanandroid.model.BannerDataBean
import com.yl.wanandroid.model.Children
import com.yl.wanandroid.model.CoinData
import com.yl.wanandroid.model.HarmonyColumnDataBean
import com.yl.wanandroid.model.PageResponse
import com.yl.wanandroid.model.ProjectCategoryDataBeanItem
import com.yl.wanandroid.model.RankItemData
import com.yl.wanandroid.model.SearchHotKeyDataBean
import com.yl.wanandroid.model.SystemDataBeanItem
import com.yl.wanandroid.model.User
import com.yl.wanandroid.model.UserDataBean
import com.yl.wanandroid.network.interceptor.CookiesInterceptor
import com.yl.wanandroid.network.interceptor.HeaderInterceptor
import com.yl.wanandroid.network.result.BaseResult
import com.yl.wanandroid.utils.LogUtils
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
            val build = OkHttpClient.Builder()
            //为OkHttp添加拦截器,以实现自动拦截存储Cookie和为需要Cookie的接口拦截添加Cookie
            build.addInterceptor(CookiesInterceptor())
            build.addInterceptor(HeaderInterceptor())
            val loggingInterceptor = HttpLoggingInterceptor { message: String ->
                LogUtils.i(
                    this,
                    message
                )
            }//日志拦截器
            build.addInterceptor(loggingInterceptor)
            val client = build.build()
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
    suspend fun getRecommendBlog(@Path("page") page: Int): BaseResult<ArticleDataBean>?


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
    ): BaseResult<ArticleDataBean>?


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
     * @return BaseResult<MutableList<ArticleDataBean>>?
     */
    @GET("/wenda/list/{page}/json")
    suspend fun getNormalWendaData(
        @Path("page") page: Int
    ): BaseResult<ArticleDataBean>?


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
    ): BaseResult<ArticleDataBean>?


    /**
     * 获取体系数据
     * @return BaseResult<MutableList<SystemDataBeanItem>>?
     */
    @GET("/tree/json")
    suspend fun getSystemData(): BaseResult<MutableList<SystemDataBeanItem>>?

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
    ): BaseResult<ArticleDataBean>?


    /**
     * 获取课程数据
     * @return BaseResult<MutableList<SystemDataBeanItem>>?
     */
    @GET("/chapter/547/sublist/json")
    suspend fun getSystemCourseData(): BaseResult<MutableList<SystemDataBeanItem>>?

    @GET("/article/list/{page}/json")
    suspend fun getSystemCourseArticleDataByCid(
        @Path("page") page: Int,
        @Query("cid") cid: Int,
        @Query("order_type") orderType: Int = 1//1默认为正序
    ): BaseResult<ArticleDataBean>?


    /**
     * 登录
     * @param username String
     * @param password String
     */
    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): BaseResult<User>?


    /**
     * 注册
     * @param username  用户名
     * @param password  密码
     * @param repassword  确认密码
     */
    @FormUrlEncoded
    @POST("/user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): BaseResult<User>?


    /**
     * 获取所有收藏的文章
     * @param page Int
     * @return BaseResult<ArticleDataBean>
     */
    @GET("/lg/collect/list/{page}/json")
    suspend fun getAllCollectArticle(@Path("page") page: Int): BaseResult<ArticleDataBean>?


    /**
     * 收藏站内文章
     * @param collectId Int
     */
    @POST("/lg/collect/{id}/json")
    suspend fun collectArticle(@Path("id") collectId: Int): BaseResult<Any>?


    /**
     * 收藏站外文章
     * @param title String
     * @param author String
     * @param link String
     */
    @POST("/lg/collect/add/json")
    suspend fun collectOutsideArticle(
        @Field("title") title: String,
        @Field("author") author: String,
        @Field("link") link: String
    ): BaseResult<Any>?


    /**
     * 取消收藏(文章列表)
     * @param id Int
     * @return BaseResult<Any>?
     */
    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun cancelCollectArticle(@Path("id") id: Int): BaseResult<Any>?

    /**
     * 取消收藏( 我的收藏页面)
     * @param id Int
     * @param originId Int 代表的是你收藏之前的那篇文章本身的id； 但是收藏支持主动添加，这种情况下，没有originId则为-1
     * @return BaseResult<Any>?
     */
    @POST("/lg/uncollect/{id}/json")
    suspend fun cancelMyCollectArticle(
        @Path("id") id: Int,
        @Query("originId") originId: Int = -1
    ): BaseResult<Any>?


    /**
     * 获取微信公众号名称列表
     * @return BaseResult<MutableList<Children>>?
     */
    @GET("/wxarticle/chapters/json")
    suspend fun getWxArticleTabs(): BaseResult<MutableList<Children>>?


    /**
     * 查看某个公众号历史数据
     * @param id Int
     * @param page Int
     * @return BaseResult<ArticleDataBean>
     */
    @GET("/wxarticle/list/{id}/{page}/json")
    suspend fun getWxArticlesByWxId(
        @Path("id") id: Int,
        @Path("page") page: Int
    ): BaseResult<ArticleDataBean>?


    /**
     * 获取用户信息
     * @return BaseResult<UserDataBean>?
     */
    @GET("/user/lg/userinfo/json")
    suspend fun getUserInfo(): BaseResult<UserDataBean>?

    /**
     * 获取用户积分列表
     * @param page Int 从1开始
     * @return BaseResult<UserCoinDataBean>?
     */
    @GET("/lg/coin/list/{page}/json")
    suspend fun getUserCoinList(
        @Path("page") page: Int
    ):BaseResult<PageResponse<CoinData>>


    /**
     * 获取积分排行榜
     * @param page Int 从1开始
     * @return BaseResult<PageResponse<RankItemData>>
     */
    @GET("/coin/rank/{page}/json")
    suspend fun getRankListData(
        @Path("page") page: Int
    ):BaseResult<PageResponse<RankItemData>>

    @GET("/user/logout/json")
    suspend fun logout():BaseResult<Any?>?
}