package com.example.myporject;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LangugeUtils {
    public static void translateText(Activity context, String sta){
        if (sta.equals("zh")){
            Resources resources = context.getResources();// 获得res资源对象
            Configuration config = resources.getConfiguration();// 获得设置对象
            DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
            config.locale = Locale.US; // 英文
            resources.updateConfiguration(config, dm);
            context.recreate();
        }else {
            //转换为中文
            Resources resources = context.getResources();// 获得res资源对象
            Configuration config = resources.getConfiguration();// 获得设置对象
            DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
            config.locale = Locale.SIMPLIFIED_CHINESE; // 英文
            resources.updateConfiguration(config, dm);
            context.recreate();
        }
    }
}
