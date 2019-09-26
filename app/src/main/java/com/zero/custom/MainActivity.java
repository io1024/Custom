package com.zero.custom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;

/**
 * 首页
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    //快速索引界面
    public void tvQuickIndex(View view) {
        startActivity(new Intent(this, QuickIndexActivity.class));
    }
}


