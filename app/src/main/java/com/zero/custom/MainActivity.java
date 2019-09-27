package com.zero.custom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import com.zero.custom.view.ItemRelativeView;

/**
 * 首页
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ItemRelativeView itemRelativeView = findViewById(R.id.itemRelativeView);
        itemRelativeView.setRightTextContent("右边边");
    }


    //快速索引界面
    public void tvQuickIndex(View view) {
        startActivity(new Intent(this, QuickIndexActivity.class));
    }
}


