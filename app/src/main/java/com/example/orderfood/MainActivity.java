package com.example.orderfood;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity
{
    //记录按下退出键时间的变量
    private long millisec = 0;
    //应用底下四个图标的变量
    private ImageView imageFood;
    private ImageView imageRecommend;
    private ImageView imageList;
    private ImageView imageUser;
    //应用图标各自对应的“碎片页面”变量“
    private Fragment fragment = null;
    private Fragment fragment_food = new fragment_food();
    private Fragment fragment_user = new fragment_user();
    private Fragment fragment_recommend = new fragment_recommend();
    //记录用户ID的变量
    public static int UID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//设置该活动显示的页面为activity_main.xml

        Intent intent = getIntent();//获取在登录界面保存的intent对象，取得用户ID等基本信息
        UID = intent.getIntExtra("UID",0);

        //初始化主界面底下的四个图标对象，并且设置它们的监听器为listener（在下面定义）
        imageFood = findViewById(R.id.main_food);
        imageRecommend = findViewById(R.id.main_recommend);
        imageList = findViewById(R.id.main_list);
        imageUser = findViewById(R.id.main_user);
        imageFood.setOnClickListener(listener);
        imageList.setOnClickListener(listener);
        imageRecommend.setOnClickListener(listener);
        imageUser.setOnClickListener(listener);
    }

    //与loginactivity相似，实现连续点击两下退出键才退出app（详细内容参见LoginActivity.java）
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                exit_main();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private void exit_main()
    {
        if((System.currentTimeMillis()-millisec)>2000)
        {
            Toast.makeText(getApplicationContext(),
                    "再按一次退出OrderFood", Toast.LENGTH_SHORT).show();
            millisec = System.currentTimeMillis();
        }
        else
        {
            finish();
            System.exit(0);
        }
    }


    public void exit()
    {
        Intent exit = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(exit);
        onDestroy();
    }
    //定义listener，实现点击底下图标进行fragment的相互切换。
    private View.OnClickListener listener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            //创建fragmentManager和FragmentTransaction实例，准备切换fragment
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            //判断哪个图片被点击，将上方的fragment替换为新的对应的fragment
            switch(view.getId())
            {
                case R.id.main_food:
                    imageFood.setImageResource(R.drawable.home_food_chosen);
                    imageRecommend.setImageResource(R.drawable.home_recommend);
                    imageList.setImageResource(R.drawable.home_list);
                    imageUser.setImageResource(R.drawable.home_user);
                    fragment = fragment_food;
                    break;
                case R.id.main_recommend:
                    imageFood.setImageResource(R.drawable.home_food);
                    imageRecommend.setImageResource(R.drawable.home_recommend_chosen);
                    imageList.setImageResource(R.drawable.home_list);
                    imageUser.setImageResource(R.drawable.home_user);
                    fragment = fragment_recommend;
                    break;
                case R.id.main_list:
                    imageFood.setImageResource(R.drawable.home_food);
                    imageRecommend.setImageResource(R.drawable.home_recommend);
                    imageList.setImageResource(R.drawable.home_list_chosen);
                    imageUser.setImageResource(R.drawable.home_user);
                    fragment = new fragment_list();
                    break;
                case R.id.main_user:
                    imageFood.setImageResource(R.drawable.home_food);
                    imageRecommend.setImageResource(R.drawable.home_recommend);
                    imageList.setImageResource(R.drawable.home_list);
                    imageUser.setImageResource(R.drawable.home_user_chosen);
                    fragment = fragment_user;
                    break;
            }
            fragmentTransaction.replace(R.id.fragment,fragment);
            fragmentTransaction.commit();
        }
    };
}
