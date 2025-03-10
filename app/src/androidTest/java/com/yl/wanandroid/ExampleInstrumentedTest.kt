package com.yl.wanandroid

import android.util.Base64
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.yl.wanandroid", appContext.packageName)
    }

    // AES加密
    fun encrypt(text: String, key: String): String {
        val cipher = Cipher.getInstance("AES")
        val secretKey = SecretKeySpec(key.toByteArray(), "AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encrypted = cipher.doFinal(text.toByteArray())
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    // AES解密
    fun decrypt(encryptedText: String, key: String): String {
        val cipher = Cipher.getInstance("AES")
        val secretKey = SecretKeySpec(key.toByteArray(), "AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decrypted = cipher.doFinal(Base64.decode(encryptedText, Base64.DEFAULT))
        return String(decrypted)
    }

    @Test
    fun AESTest(){
        val password = "12345"
        val key = "keykeykeykeykeyy"  // 秘钥必须为16字节或32字节
        val encrypt = encrypt(password, key)
        val decrypt = decrypt(encrypt, key)
        Log.d("AESTest","encrypt-->$encrypt")
        Log.d("AESTest","decrypt-->$decrypt")
        assertEquals(true, password == decrypt)
    }
}