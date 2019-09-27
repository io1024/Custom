package com.zero.custom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zero.custom.R;
import com.zero.custom.bean.Person;

import java.util.List;

public class QuickIndexAdapter extends BaseAdapter {

    private List<Person> allPerson;
    private Context context;

    public QuickIndexAdapter(Context context, List<Person> data) {
        this.context = context;
        this.allPerson = data;
    }


    @Override
    public int getCount() {
        return allPerson == null ? 0 : allPerson.size();
    }

    @Override
    public Person getItem(int position) {
        return allPerson.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        View view;
//        if (convertView == null) {
//            view = View.inflate(context, R.layout.item_quick_index, false);
//        } else {
//            view = convertView;
//        }
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_quick_index, null, false);
            holder.tvEn = (TextView) convertView.findViewById(R.id.tvEn);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name = allPerson.get(position).getName();
        holder.tvName.setText(name);

//    private String letter = null;
        //判断当前字符和上一个字符是否相同，相同则不展示，不相同则展示
        //该方式会导致 listView 复用出问题，产生乱序情况
//        if (currentLetter.equals(letter)) {
//            holder.tvEn.setVisibility(View.GONE);
//        } else {
//            letter = currentLetter;
//            holder.tvEn.setVisibility(View.VISIBLE);
//        }

        //首字母
        String currentLetter = allPerson.get(position).getNamePinYin().charAt(0) + "";
        //定义一个变量，记录当前字母
        String letter = null;
        if (position == 0) {
            letter = currentLetter;
        } else {
            //获取前一个索引位置的首字母，然后与当前首字母比较，不一致再赋值，一致则不赋值
            //letter若不赋值，则为null
            String preLetter = allPerson.get(position - 1).getNamePinYin().charAt(0) + "";
            if (!TextUtils.equals(preLetter, currentLetter)) {
                letter = currentLetter;
            }
        }
        holder.tvEn.setText(letter);
        //当letter为null,则表示当前位置的首字符和前一个位置相同，需要隐藏
        holder.tvEn.setVisibility(letter == null ? View.GONE : View.VISIBLE);

        return convertView;
    }


    public static class ViewHolder {
        TextView tvEn;
        TextView tvName;
    }
}
