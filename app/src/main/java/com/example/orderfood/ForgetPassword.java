package com.example.orderfood;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPassword extends AppCompatActivity
{
    //初始化输入框和各种组件。
    private EditText name, telephone;
    private RadioGroup radioGroup;
    private String gender;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);//设置该活动显示的布局文件
        setView();
    }

    private void setView()
    {
        //关联屏幕组件以及程序初始化变量
        name = findViewById(R.id.user_name);
        name.setOnFocusChangeListener(LoginActivity.mOnFocusChangeListener);
        telephone = findViewById(R.id.telephone);
        telephone.setOnFocusChangeListener(LoginActivity.mOnFocusChangeListener);
        Button submit = findViewById(R.id.forget_submit);
        radioGroup = findViewById(R.id.gender);
        result = findViewById(R.id.result);
        //设置提交按钮的点击事件监听器
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                //获取用户输入的用户名、手机号以及性别资讯
                String user_name = name.getText().toString();
                String telePhone = telephone.getText().toString();
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton choice = findViewById(id);
                if(choice.getText().toString().equals("男"))
                    gender = "M";
                else gender = "F";
                //根据用户输入的信息，进行sql查询
                Cursor cursor = LoginActivity.database.query("User",new String[]{"UserName", "Password", "Gender", "Telephone"},
                        "UserName=? and Telephone=? and Gender=?"
                        , new String[]{user_name, telePhone, gender}, null, null, null);
                //假如用户输入的信息查询结果为空
                if(cursor.getCount() == 0) {
                    result.setText("");
                    Toast.makeText(ForgetPassword.this, "不存在此用户哦！", Toast.LENGTH_LONG).show();
                }
                //在页面下隐藏的文本框处更改文本框内容，显示用户的密码。
                else
                {
                    cursor.moveToNext();
                    String tmp = cursor.getString(cursor.getColumnIndex("Password"));
                    result.setText("您的密码是： " + tmp);
                }
            }
        });



    }
}
