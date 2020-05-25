package com.example.orderfood;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity//注册界面的活动
{
    //初始化必要的组件，如各种输入框和提交按钮
    private EditText user_name, password, password_confirm, telephone;
    private Button submit;
    private RadioGroup gender;
    private String name, password_string, password_confirm_string, telephone_string, sex;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setContent();//设置注册界面的内容
    }

    private void setContent()
    {
        //关联各个输入框及按钮对象
        user_name = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        password_confirm = findViewById(R.id.password_confirm);
        telephone = findViewById(R.id.telephone);
        gender = findViewById(R.id.gender);
        submit = findViewById(R.id.register_submit);

        //设置输入框的焦点变换监听器为LoginActivity里对应的监听器
        //P.S. 这样做纯粹只是为了美观，没有设置也没有关系。
        user_name.setOnFocusChangeListener(LoginActivity.mOnFocusChangeListener);
        password.setOnFocusChangeListener(LoginActivity.mOnFocusChangeListener);
        password_confirm.setOnFocusChangeListener(LoginActivity.mOnFocusChangeListener);
        telephone.setOnFocusChangeListener(LoginActivity.mOnFocusChangeListener);

        //设置点击提交按钮后发生的操作
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //获取用户输入的字符串
                name = user_name.getText().toString();
                password_string = password.getText().toString();
                password_confirm_string = password_confirm.getText().toString();
                telephone_string = telephone.getText().toString();

                //对用户输入的用户名进行数据库查询
                Cursor cursor = LoginActivity.database.query("User", new String[]{"UserName"}, "UserName=?"
                        , new String[]{name}, null, null, null);
                //有信息未填充
                if(name.isEmpty() || password_string.isEmpty() || password_confirm_string.isEmpty() || telephone_string.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "您有未完善的信息哦！", Toast.LENGTH_LONG).show();
                }
                //用户想注册的用户名在数据库查询中返回了结果
                else if(cursor.getCount() != 0) {
                    Toast.makeText(RegisterActivity.this, "您的用户名已经被注册了，换一个吧", Toast.LENGTH_LONG).show();
                    cursor.close();
                }
                //输入密码的长度小于6位或者多于16位
                else if(password_string.length() <6 || password_string.length() > 16)
                {
                    Toast.makeText(RegisterActivity.this,"您的密码不符合规范哦",Toast.LENGTH_SHORT).show();
                }
                //两次密码输入不一致
                else if( !password_string.equals(password_confirm_string) )
                {
                    Toast.makeText(RegisterActivity.this,"两个密码不一致呢！",Toast.LENGTH_SHORT).show();
                }
                //手机号码长度不等于11位
                else if(telephone_string.length() != 11)
                {
                    Toast.makeText(RegisterActivity.this,"您的手机号码不符合规范哦",Toast.LENGTH_SHORT).show();
                }
                //用户输入的信息均为合法信息
                else
                {
                    //获取用户的性别属性
                    int id = gender.getCheckedRadioButtonId();
                    RadioButton choice = findViewById(id);
                    sex = choice.getText().toString();
                    //创建一个弹出窗口，以供用户确认输入信息
                    AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                    dialog.setIcon(R.mipmap.icon);
                    dialog.setTitle("确认信息：");
                    dialog.setMessage("用户名：" + name + "\n电话号码：" + telephone_string + "\n性别：" + sex + "\n");
                    dialog.setNegativeButton("取消", null);
                    //点击“确定提交”创建该用户（将信息载入数据库中）
                    dialog.setPositiveButton("确定提交", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            ContentValues value = new ContentValues();
                            value.put("UserName", name);
                            value.put("Password",password_string);
                            value.put("Telephone", telephone_string);
                            String t;
                            if(sex.equals("男"))
                                t = "M";
                            else t = "F";
                            value.put("Gender",t);
                            //数据载入数据库失败
                            if(LoginActivity.database.insert("User",null,value) == -1)
                                Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                            //载入数据成功，注册成功！
                            else Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    dialog.create().show();//展示弹出对话框
                }
            }
        });

    }
}
