package com.example.orderfood;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity//登录界面的创建
{
    private long millisec = 0;//记录按下退出键时间的变量
    private EditText userName, Password;//获取输入框
    public static SQLprocess sqLprocess= new SQLprocess();//建立sqlite查询对象
    public static SQLiteDatabase database;//建立sqlite数据库对象

    @Override
    protected void onCreate(Bundle savedInstanceState)//活动的onCreate函数
    {
        database = sqLprocess.openDatabase(getApplicationContext());//获取sqlite数据库
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);//设置显示的界面为activity_login.xml
        userName = findViewById(R.id.user_name);//获取用户名输入框对象
        userName.setOnFocusChangeListener(mOnFocusChangeListener);
        Password = findViewById(R.id.password);//获取密码输入框对象
        Password.setOnFocusChangeListener(mOnFocusChangeListener);
        setDrawable();//设置要显示的图像大小
        setButton();//设置按钮功能
    }
    private void setButton()//设置按钮各自对应功能的函数
    {
        Button login = findViewById(R.id.login_button);//获取登录按钮对象
        login.setOnClickListener(new View.OnClickListener()
        {
            //分别取得姓名和密码输入框内的内容
            @Override
            public void onClick(View view)
            {
                String name = userName.getText().toString();
                String pass = Password.getText().toString();
                if(name.isEmpty()|| pass.isEmpty())//假如用户名或者密码输入框为空
                    Toast.makeText(LoginActivity.this,"请输入用户名和密码！",Toast.LENGTH_SHORT).show();
                else {
                    //创建cursor进行数据库访问，检索是否存在当前用户
                    Cursor cursor = database.query("User", new String[]{"UID", "UserName", "Password", "Gender", "Telephone"}, "UserName=? and Password=?"
                            , new String[]{name, pass}, null, null, null);
                    //数据库中未检索到当前用户
                    if (cursor.getCount() == 0)
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        //登陆成功的操作
                    else {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        //创建一个intent对象，保存用户的数据信息，方便接下来用户在浏览个人页面的时候进行显示（不必进行二次查找操作）
                        intent.putExtra("UserName", name);
                        cursor.moveToNext();
                        intent.putExtra("Gender", cursor.getString(cursor.getColumnIndex("Gender")));
                        intent.putExtra("Telephone", cursor.getString(cursor.getColumnIndex("Telephone")));
                        intent.putExtra("UID", cursor.getInt(cursor.getColumnIndex("UID")));
                        //跳转到mainActivity，即客户端主界面
                        startActivity(intent);
                        //结束并且删除当前活动
                        finish();
                    }
                }
            }
        });
        //设置“注册”文本框的点击事件监听器
        final TextView register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //点击“注册”这一文本框后，跳转到注册页面
                Intent Register = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity (Register);
            }
        });
        //设置“忘记密码”文本框的点击事件监听器
        TextView forget = findViewById(R.id.forget_pwd);
        forget.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //点击“忘记密码”这一文本框后，跳转到忘记密码页面
                Intent forget = new Intent(LoginActivity.this,ForgetPassword.class);
                startActivity(forget);
            }
        });
    }

    //设置用户焦点监听函数，设定当用户获取到输入框焦点时，隐藏当前输入框的hint
    //而当用户失去输入框焦点时，再重新显示输入框的hint
    public static View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            EditText textView = (EditText)v;
            String hint;
            if (hasFocus) {
                hint = textView.getHint().toString();
                textView.setTag(hint);
                textView.setHint("");
            } else {
                hint = textView.getTag().toString();
                textView.setHint(hint);
            }
        }
    };

    //由于用户名和密码输入框旁的两幅图像较大，原图显示缺乏美感，故此处进行两幅图像的大小适配。
    private void setDrawable()
    {
        Drawable drawable = getResources().getDrawable(R.drawable.portrait);
        drawable.setBounds(0,0,80,80);
        userName.setCompoundDrawables(drawable,null,null,null);
        drawable = getResources().getDrawable(R.drawable.lock);
        drawable.setBounds(0,0,80,80);
        Password.setCompoundDrawables(drawable,null,null,null);
    }

    //下面两个函数是为了实现连续点击两下退出键才退出app的
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            exit_main();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit_main()
    {
        if((System.currentTimeMillis()-millisec)>2000)
        //假如当前时间与上次按下退出键的时间差大于2sec，则弹出提示：“再按一次退出键退出”
        {
            Toast.makeText(getApplicationContext(),
                    "再按一次退出OrderFood", Toast.LENGTH_SHORT).show();
            millisec = System.currentTimeMillis();
        }
        else//两次按下退出键之间的时间小于两秒，直接结束应用。
            {
                finish();
                System.exit(0);
            }
    }
}
