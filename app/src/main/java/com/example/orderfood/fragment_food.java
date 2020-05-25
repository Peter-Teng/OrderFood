package com.example.orderfood;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fragment_food extends Fragment
//食物类别选择fragment，在这个fragment中显示一个ListView，以供用户选择他想订的餐品类别。
{
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_main_food, container, false);
        //将当前页面fragment填充为fragment_food
        setListView(view);
        //设置ListView显示的内容
        return view;
    }


    private void setListView(View view)
    {
        //关联ListView
        ListView food_list = view.findViewById(R.id.food_list);
        //设置ListView显示的图片以及文字
        int[] imageId = new int[] {R.mipmap.steak,R.mipmap.shaolu,R.mipmap.dumplings,R.mipmap.noodles
                ,R.mipmap.frying_food,R.mipmap.fish,R.mipmap.fry_meat,R.mipmap.rice_noodle,R.mipmap.dimsum};
        String[] food = new String[] {"扒饭","烧卤","饺子","面条","精炒","渔粉","烤肉","肠粉","广式点心"};
        //创建List把图片以及文字放入
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < imageId.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>(); // 实例化Map对象
            map.put("image", imageId[i]);
            map.put("food", food[i]);
            listItems.add(map); // 将map对象添加到List集合中
        }
        //设置Adapter
        SimpleAdapter adapter = new SimpleAdapter(this.getActivity(), listItems,
                R.layout.list_item, new String[] { "food", "image" }, new int[] {
                R.id.title, R.id.image }); // 创建SimpleAdapter
        food_list.setAdapter(adapter); // 将适配器与ListView关联

        //设置ListView项目被点击后触发事件的点击事件监听器
        food_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Map<String, Object> map = ( Map<String, Object> )adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity().getApplicationContext(),ActivityFood.class);
                //记录被点击的项目
                intent.putExtra("FoodKey",map.get("food").toString());
                //打开点餐活动，准备显示菜单
                startActivity(intent);
            }
        });

    }
}
