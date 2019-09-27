package com.zero.custom;

import com.github.promeg.pinyinhelper.Pinyin;

public class PinYinUtils {

    /**
     * 汉字 转 拼音
     */
    public static String pinYinRemoveSpace(String ch) {
        char[] chars = ch.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char c : chars) {
            //判断是否是空格
            if (!Character.isWhitespace(c)) {
                builder.append(Pinyin.toPinyin(c));
            }
        }
        return builder.toString();
    }
}
