package com.example.orderfood;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class fragment_user extends Fragment//用户页面fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //在用户点击“用户”图标时，将当前fragment转换为fragment user
        View view = inflater.inflate(R.layout.fragment_main_user, container, false);
        setContent(view);//设置显示的内容
        return view;
    }

    private void setContent(View view)
    {
        //关联TextView组件
        TextView username = view.findViewById(R.id.name);
        TextView gender = view.findViewById(R.id.gender);
        TextView telephone = view.findViewById(R.id.telephone);
        Intent detail = getActivity().getIntent();
        //取得intent，然后显示保存的信息
        String tmp = "用户名：  " + detail.getStringExtra("UserName");
        username.setText(tmp);
        tmp = "性别：    " + detail.getStringExtra("Gender");
        gender.setText(tmp);
        tmp = "电话号码：  " + detail.getStringExtra("Telephone");
        telephone.setText(tmp);
        TextView editMessage = view.findViewById(R.id.edit_message);

        //设置“编辑信息”的点解事件监听器
        editMessage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getActivity(),"请期待",Toast.LENGTH_SHORT).show();
            }
        });

        //设置退出按钮的点击事件监听器
        Button exit = view.findViewById(R.id.exit_button);
        exit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //弹出AlertDialog，再次确认用户是否退出。
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setIcon(R.mipmap.icon);
                dialog.setTitle("您确定要退出吗？");
                dialog.setNegativeButton("哎呀，手滑了！",null);
                dialog.setPositiveButton("退出", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.exit();
                    }
                });
                dialog.create().show();
            }
        });
    }
}