package com.yl.wanandroid.utils

object StringUtils {
    fun cleanUpCharacter(s: String): String {
        // 替换 HTML 实体
        var newS = s
        newS = newS.replace("&mdash;", "—");
        newS = newS.replace("&mdash;", "——"); // 替换为中文的破折号
        // 可以添加其他替换规则

        return newS
    }

}