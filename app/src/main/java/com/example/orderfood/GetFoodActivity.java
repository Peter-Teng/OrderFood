package com.example.orderfood;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Math.random;

public class GetFoodActivity extends AppCompatActivity
{
    //定义活动组件
    private String foodType, price;
    private int food_id;
    private TextView food_to_get, get_number, food_price;
    private Button submit;
    private RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //设置取餐页面的文字显示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_food);
        Intent intent = getIntent();
        foodType = intent.getStringExtra("food");
        price = intent.getStringExtra("price");
        food_id = Integer.valueOf(intent.getStringExtra("ID"));
        food_to_get = findViewById(R.id.food_to_get);
        food_to_get.setText(foodType);
        food_price = findViewById(R.id.food_to_get_price);
        food_price.setText(price);
        get_number = findViewById(R.id.get_number);
        int i = (int)(Math.random() * 200);
        get_number.setText("#" +  i );
        ratingBar = findViewById(R.id.rating);
        submit = findViewById(R.id.rating_submit);
        //点击“提交”按钮，载入评价
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //将当前订单信息载入数据库
                ContentValues value = new ContentValues();
                value.put("UID", MainActivity.UID);
                value.put("DishID", food_id);
                value.put("CommentScore", ratingBar.getRating());
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                value.put("OrderDate",simpleDateFormat.format(date));
                if(LoginActivity.database.insert("OrderHistory",null,value) == -1)
                    Toast.makeText(GetFoodActivity.this,"载入记录失败" + simpleDateFormat.format(date), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(GetFoodActivity.this,"载入记录成功" + simpleDateFormat.format(date), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
