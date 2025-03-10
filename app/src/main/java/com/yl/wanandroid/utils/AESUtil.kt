package com.yl.wanandroid.utils


import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * @description: AES加密工具类
 * @author YL Chen
 * @date 2025/3/9 18:13
 * @version 1.0
 */
object AESUtil{
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

}
