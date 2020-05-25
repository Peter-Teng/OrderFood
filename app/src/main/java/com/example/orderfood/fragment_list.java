package com.example.orderfood;


import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fragment_list extends Fragment//历史记录Fragment
{
    //与fragment food类似，使用listView展示历史记录条目
    private ListView listView;
    private SimpleAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //在用户点击“订单”图标时，将当前fragment转换为fragment list
        View view = inflater.inflate(R.layout.fragment_main_list,container,false);
        listView = view.findViewById(R.id.history);
        setListView(view);//设置ListView显示的内容
        return view;
    }

    private void setListView(View view)
    {
        //发起sql查询，查询当前用户的历史记录，并且返回cursor数据库指针
        Cursor cursor = LoginActivity.database.rawQuery("select OrderDate, PicPath, DishName from Dish natural join OrderHistory where UID = ? order by HistoryID desc;",
                new String[] {String.valueOf(MainActivity.UID)});
        //Cursor cursor = LoginActivity.database.query("OrderHistory",new String[] {"DishID", "OrderDate"},
        //        "UID = ?", new String[] {String.valueOf(MainActivity.UID)} ,null,null,"rowid desc");
        if(cursor.getCount() == 0)//假如没有历史记录，直接返回
            return;
        else
        {
            int i = 0;//最多只显示十条记录
            List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
            while(cursor.moveToNext() && i < 10)
            {
                //从数据库查询结果中提取字符串和图片并且放入List中
                String OrderDate = cursor.getString(cursor.getColumnIndex("OrderDate"));
                Map<String, Object> map = new HashMap<String, Object>(); // 实例化Map对象
                map.put("image",getResource(cursor.getString(cursor.getColumnIndex("PicPath"))));
                map.put("name",cursor.getString(cursor.getColumnIndex("DishName")));
                map.put("Date", OrderDate);
                listItems.add(map);
                i++;
            }
            cursor.close();
            //设置adapter，在ListView中显示。
            adapter = new SimpleAdapter(getContext(), listItems, R.layout.history_item, new String[] {"image","name","Date"}
                                        ,new int[] {R.id.food_image,R.id.food_name,R.id.order_date});
            listView.setAdapter(adapter);
        }

    }

    private int  getResource(String imageName)
    {
        //从drawable文件目录下取得名字为《imageName》的图片id
        Context ctx = getActivity().getBaseContext();
        int resId = getResources().getIdentifier(imageName, "drawable", ctx.getPackageName());
        //如果没有在"drawable"下找到imageName,将会返回0
        return resId;
    }
}
