## **项目介绍**

​	本项目是基于 MVVM（Model-View-ViewModel）架构模式，结合 **Kotlin 语言**，**Retrofit 网络请求库**，**OkHttp3 请求拦截器**，**Jetpack 组件**，和 **Material Design 3** 设计规范实现的 **[WanAndroid](https://www.wanandroid.com/blog/show/2)** 客户端应用。

- **MVVM 架构模式**：通过将业务逻辑与 UI 层分离，帮助开发者理解如何组织和管理应用的代码，提升代码的可维护性和可扩展性。
- **Kotlin 语言特性**：项目全面采用 Kotlin 语言，展示了 Kotlin 的简洁、安全和高效的特性，帮助开发者更好地掌握 Kotlin 编程技巧。
- **网络请求与数据处理**：使用 **Retrofit**  + **OkHttp3** 与 **Kotlin协程 **配合，展示了如何进行高效、可靠的网络请求，并处理 API 返回的数据。
- **Jetpack 组件**：项目集成了 Jetpack 的多个关键组件（包括 **LiveData**、**ViewModel**、**Flow**、**Paging**、**Room**、**Navigation** 等），帮助开发者了解现代 Android 开发的最佳实践。
- **Material Design 3**：采用最新的 Material Design 3 设计规范，展示了如何创建符合现代设计标准的界面，并提供一致且流畅的用户体验。

项目中每个功能都配有**详细的注释**，旨在帮助开发者理解应用的结构和实现，便于学习和代码审查。此外，本项目鼓励开发者提出建议和改进意见，促进开源社区的共同进步，若对本项目有任何更好的建议可在本项目的 **[issue](https://github.com/YiLeChen16/WanAndroid/issues)** 中提出，感谢支持~

## 实现功能

全局的功能实现有:

- **登录注册功能**
- **动态的收藏和取消收藏功能**：所有可见带有收藏按钮的item都可进行动态收藏和取消收藏功能，会进行实时的状态更新

此外，APP共分为四个主页面，符合主流产品级博客类APP的设计，各个页面实现的功能分别如下：

- **首页：**
  - 轮播图功能
  - 推荐博客文章功能
  - 搜索功能（仿主流博客类APP的搜索功能）
    - 搜索框搜索热词轮播，点击搜索框图标跳转搜索热词功能
    - 点击搜索框跳转搜索页面进行搜索功能
  - 展示问答博客功能
  - 展示鸿蒙专栏功能
  - 点击跳转收藏界面功能
- **项目**
  - 展示全部分类的项目
- **体系**
  - 体系数据展示（由双RecyclerView实现，双表联动），点击跳转展示对应关键词的文章列表
  - 课程列表展示，点击跳转展示课程列表数据（默认为正序，可设置倒序正序）
- **我的**
  - 用户基本信息和积分排名等级数据展示
  - 点击跳转搜索界面功能
  - 点击切换主题功能，目前支持白天主题和夜晚主题
  - 点击跳转个人信息界面功能
  - 点击跳转积分界面功能
    - 展示用户积分等级排名信息和积分获取记录列表
    - 点击跳转积分排行榜界面
    - 点击跳转积分规则界面
  - 点击跳转收藏界面功能
    - 展示用户收藏的全部文章
    - 动态取消收藏功能（实时更新到整个APP界面）
  - 点击跳转分享界面功能
    - 展示用户基本信息及分享文章总数
    - 展示用户分享的文章
    - 分享文章（正在开发中~）
  - 点击跳转设置界面功能
    - 清理缓存功能
    - 切换主题功能
    - 设置主题跟随系统功能
    - 退出登录功能
  - 展示公众号文章功能

## 项目演示

### 日间主题:
|![f5e43d5d26a5e4a764b8cf8dc0f1451](https://github.com/user-attachments/assets/1a798146-4b4c-4e8d-9cd3-d69feabab85c)|![acf22d44a1c8a2ac1b2e69d37b70df4](https://github.com/user-attachments/assets/b79e6405-58f9-4426-b425-d0088430ddc0)|![f5dddd63d23b8c89be6dd4169245901](https://github.com/user-attachments/assets/7c00cfeb-469b-4c7e-904e-f2713f293ca1)|![c07a2b2f6b540016219e8cb00df5025](https://github.com/user-attachments/assets/74cdcb66-965d-4d33-a5b6-87053e7048d4)|![b2a9d20aa0efb5023a9903573460b27](https://github.com/user-attachments/assets/ce6ccd3a-235c-4f4b-9a8c-116dd2a2dad2)|
| ---- | ---- | ---- | ---- | ---- |

### 夜间主题:
|![dc4638a1e5d3e76bf9349f79be42353](https://github.com/user-attachments/assets/e9050ee8-020c-4bb7-b9a2-3d3e18f2ff8f)|![5e71c09086a82c153b26bd525b85fff](https://github.com/user-attachments/assets/ef7cbe5d-dac5-47d1-8019-677a8ada3b99)|![d918ee3605b788b8ef9e0353a9ec757](https://github.com/user-attachments/assets/4a52e386-e66b-4c46-a3c6-b734b223a156)|![37914cbde6271585ceb1a394526cd0c](https://github.com/user-attachments/assets/6eed0939-cfca-4d45-890a-61e1993b1c97)|![ffde4ee99c05633a874a7e8ce3e6369](https://github.com/user-attachments/assets/f012f3aa-1c15-4f1d-a76e-8795561eaf02)|
| ---- | ---- | ---- | ---- | ---- |

### 搜索功能演示

![项目搜索功能演示](https://github.com/user-attachments/assets/b9777d7b-d482-458f-af4b-f7a22094382a)


## 项目使用的主要开源框架

- ### 网络请求框架：

  - ### [Retrofit](https://github.com/square/retrofit) 

  - ### [Okhttp3](https://github.com/square/okhttp)

  - ### [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines)

- ### 存储层框架 ：

  - ### [MMKV](https://github.com/Tencent/MMKV/)

  - ### [Room](https://github.com/androidx/androidx/tree/androidx-main/room)

- ### UI层框架：

  - ### 导航框架：[Navigation](https://github.com/androidx/androidx/tree/androidx-main/navigation)

  - ### 列表自动分页加载框架：[Paging](https://github.com/androidx/androidx/tree/androidx-main/paging)

  - ### 网络图片加载框架：[Glide](https://github.com/bumptech/glide)

- ### **依赖注入框架：[Hilt](https://mvnrepository.com/artifact/com.google.dagger/hilt-android)**

- ### 刷新框架：[SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)

- ### MarkDown语法解析框架：[MarkdownProcessor](https://github.com/yydcdut/RxMarkdown)
