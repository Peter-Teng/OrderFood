package com.example.orderfood;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

public class SQLprocess//通过这个类将数据库复制到应用内存中
{
    String filePath = "data/data/com.example.orderfood/OrderFoodDB.db";//数据库起始目录
    String pathStr = "data/data/com.example.orderfood";//数据库目标目录

    public SQLiteDatabase openDatabase(Context context)
    {
        File jhPath = new File(filePath);
        if(jhPath.exists())//假如数据库已经存在，将返回数据库本体
            return SQLiteDatabase.openOrCreateDatabase(jhPath,null);
        else//数据库不存在于目标目录中，将数据库复制到应用专属内存下
        {
            File path = new File(pathStr);
            path.mkdir();
            try
            {
                AssetManager am = context.getAssets();
                InputStream is = am.open("OrderFoodDB.db");
                FileOutputStream fos = new FileOutputStream(jhPath);
                byte[] buffer = new byte[1024];
                int count = 0;
                while((count = is.read(buffer)) > 0)
                {
                    fos.write(buffer,0,count);
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
            return openDatabase(context);
        }
    }
}
