package com.yl.wanandroid.ext

import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

/**
 * EditText的扩展函数
 */
/**
 * EditText.textChangeFlow()     // 构建输入框文字变化流
 * .filter { it.isNotEmpty() }   // 过滤空内容，避免无效网络请求
 * .debounce(300)                // 300ms防抖
 * .flatMapLatest { searchFlow(it.toString()) } // 新搜索覆盖旧搜索
 * .flowOn(Dispatchers.IO)       // 让搜索在异步线程中执行
 * .onEach { updateUi(it) }      // 获取搜索结果并更新界面
 * .launchIn(mainScope)          // 在主线程收集搜索结果
 */
fun EditText.textChangeFlow(): Flow<String> = callbackFlow {
    val watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let { trySend(it.toString()).isSuccess }
        }
    }
    addTextChangedListener(watcher)
    awaitClose { removeTextChangedListener(watcher) } // 阻塞以保证流一直运行
}

/**
 * 监听EditText文本变化
 */
fun EditText.setEditTextChange(
    callBack: () -> Unit,
    lifecycleScope: LifecycleCoroutineScope
) {
    textChangeFlow()
        .debounce(300)
        .flowOn(Dispatchers.IO)
        .onEach {
            withContext(Dispatchers.Main) { // 切换回主线程
                callBack()
            }
        }
        .launchIn(lifecycleScope)
}


/**
 * 隐藏文本
 * @receiver EditText
 */
fun EditText.setTextHide() {
    transformationMethod = HideReturnsTransformationMethod.getInstance()
}

/**
 * 显示文本
 * @receiver EditText
 */
fun EditText.setTextShow(){
    transformationMethod = PasswordTransformationMethod.getInstance()
}