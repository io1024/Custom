package com.zero.custom;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.github.promeg.pinyinhelper.Pinyin;
import com.zero.custom.view.QuickIndexBar;

/**
 * 1. 绘制字母
 * 2. 处理触摸事件
 * 3. 监听回调传递信息
 * 4. 汉字 转 拼音
 * 5. 通过拼音进行排序展示
 * 6. 根据拼音首字母分组
 * 7. 将listView 和 自定义控件结合起来
 */
public class QuickIndexActivity extends BaseActivity implements QuickIndexBar.LetterUpdateListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_index);

        QuickIndexBar quickIndexBar = findViewById(R.id.quickIndexBar);
        quickIndexBar.setOnLetterUpdateListener(this);
        String s = pinYinRemoveSpace("he黑色 ， 的星 期 五123&*");
        System.out.println("pingying" + s);
    }


    @Override
    public void onLetterUpdate(String letter) {

    }


    /**
     * 汉字 转 拼音
     */

    private String pinYinRemoveSpace(String ch) {
        char[] chars = ch.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            //判断是否是空格
            if (!Character.isWhitespace(c)) {
                builder.append(Pinyin.toPinyin(c));
            }
        }
        return builder.toString();
    }

}


