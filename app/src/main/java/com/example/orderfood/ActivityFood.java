package com.example.orderfood;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityFood extends AppCompatActivity
{
    //使用gridview显示菜单
    private List<Map<String, Object>> GridItems;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        //设置对应的xml文件
        //关联组件
        ImageView back = findViewById(R.id.back);
        GridView gridView = findViewById(R.id.food_grid);
        //设置退出按钮
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        //从上一个活动中取得intent及菜品种类信息
        Intent intent = getIntent();
        String DishType = intent.getStringExtra("FoodKey");
        TextView foodName = findViewById(R.id.food_name);
        foodName.setText(DishType);
        //使用sql查询，查询该项目类别下所有的菜品
        Cursor cursor = LoginActivity.database.query("Dish",new String[] {"DishName","Price","PicPath","DishID"},"FoodType = ?",new String[] {DishType},
                null,null,null);
        GridItems = new ArrayList<Map<String, Object>>();
        //移动cursor并且把数据装载进map中，以便使Gridview显示
        while(cursor.moveToNext())
        {
            Map<String, Object> map = new HashMap<String, Object>(); // 实例化Map对象
            map.put("image", getResource(cursor.getString(cursor.getColumnIndex("PicPath"))));
            map.put("name",cursor.getString(cursor.getColumnIndex("DishName")));
            map.put("price",cursor.getString(cursor.getColumnIndex("Price")));
            map.put("ID",cursor.getInt(cursor.getColumnIndex("DishID")));
            GridItems.add(map); // 将map对象添加到List集合中
        }
        adapter = new SimpleAdapter(this, GridItems,
                R.layout.grid_cell, new String[] { "image","name","price" }, new int[] {
                R.id.grid_image,R.id.FoodName, R.id.Price }); // 创建SimpleAdapter
        gridView.setAdapter(adapter);

        //模拟支付模块（就是一个对话框）
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                final Map<String, Object> map = ( Map<String, Object> )adapterView.getItemAtPosition(i);
                final AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityFood.this);
                dialog.setIcon(R.mipmap.icon);
                dialog.setTitle("这里是模拟支付模块,请选择您的支付方式:  "+map.get("name").toString() + "  "+map.get("price").toString());
                dialog.setSingleChoiceItems(new String[] {"微信支付","支付宝支付"},0,null);
                dialog.setNegativeButton("支付失败/取消支付", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ActivityFood.this,"支付失败",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setPositiveButton("模拟支付成功！", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        Intent intentNext = new Intent(ActivityFood.this,GetFoodActivity.class);
                        intentNext.putExtra("food",map.get("name").toString());
                        intentNext.putExtra("price",map.get("price").toString());
                        intentNext.putExtra("ID",map.get("ID").toString());
                        startActivity(intentNext);
                        finish();
                    }
                });
                dialog.create().show();
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        GridItems.clear();
        super.onDestroy();
    }

    private int  getResource(String imageName)
    {
        Context ctx=getBaseContext();
        int resId = getResources().getIdentifier(imageName, "drawable", ctx.getPackageName());
        //如果没有在"drawable"下找到imageName,将会返回0
        return resId;
    }
}
