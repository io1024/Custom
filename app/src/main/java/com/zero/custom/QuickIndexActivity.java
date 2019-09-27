package com.zero.custom;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.zero.custom.adapter.QuickIndexAdapter;
import com.zero.custom.bean.Person;
import com.zero.custom.view.QuickIndexBar;

import java.util.*;

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

    private TextView tvToast;
    private ListView listView;
    private QuickIndexBar quickIndexBar;
    private final String[] names = {"安", "赵", "王", "周", "李", "黄", "宋", "唐", "程", "何", "吴", "关", "孔", "潘", "任", "宁", "项"};
    private List<Person> persons;
    //创建一个handler对象来做一个Toast提示
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_index);

        tvToast = findViewById(R.id.tvToast);
        listView = findViewById(R.id.listView);
        quickIndexBar = findViewById(R.id.quickIndexBar);
        quickIndexBar.setOnLetterUpdateListener(this);


        //模拟：创建一些名字数据
        Random random;
        int tag = 0;
        StringBuilder builder = new StringBuilder();
        persons = new ArrayList<>();
        Person person;
        for (int i = 0; i < 60; i++) {
            random = new Random();
            int index = random.nextInt(names.length);
            builder.append(names[index]);
            tag++;
            if (tag == 2) {
                person = new Person(builder.toString());
                persons.add(person);
                builder.delete(0, builder.length());
                tag = 0;
            }
        }
        //集合排序
        Collections.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getNamePinYin().compareTo(o2.getNamePinYin());
            }
        });

        QuickIndexAdapter adapter = new QuickIndexAdapter(this, persons);
        listView.setAdapter(adapter);
    }


    @Override
    public void onLetterUpdate(String letter) {
        showCustomToast(letter);
        //触摸监听回调
        //找到数据集合中首次出现letter的索引位置，listView直接跳到该索引位置
        for (int index = 0; index < persons.size(); index++) {
            String pin = persons.get(index).getNamePinYin().charAt(0) + "";
            if (TextUtils.equals(pin, letter)) {
                listView.setSelection(index);
                break;
            }
        }
    }

    //自定义一个Toast,用于选中字母的提示
    private void showCustomToast(String letter) {
        tvToast.setText(letter);
        tvToast.setVisibility(View.VISIBLE);
        //移除之前的任务和消息
        handler.removeCallbacksAndMessages(null);
        //300毫秒之后隐藏
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvToast.setVisibility(View.GONE);
            }
        }, 300);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //避免handler造成内存泄漏
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}


