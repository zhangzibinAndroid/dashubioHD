package com.returnlive.dashubiohd.application;

import android.app.Activity;
import android.app.Application;

import com.zhy.autolayout.config.AutoLayoutConifg;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/12 0012
 * 时间： 上午 10:33
 * 描述： autolayout初始化
 */

public class DashuHdApplication extends Application {
    private static List<Activity> lists = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
    }


    public static void addActivity(Activity activity) {
        lists.add(activity);
    }

    public static void clearActivity() {
        if (lists != null) {
            for (Activity activity : lists) {
                activity.finish();
            }

            lists.clear();
        }
    }
}
