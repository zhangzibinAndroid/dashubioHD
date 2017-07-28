package com.klw.singleleadsdk;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.xtremeprog.sdk.ble.BleService;
import com.xtremeprog.sdk.ble.IBle;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by altria on 17-7-7.
 */

public class DashuHdApplication extends Application {
    private static DashuHdApplication sInstance = null; // 单件对象
    private BleService mService = null;
    private IBle mBle = null;
    private static List<Activity> lists = new ArrayList<>();

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {

            mService = ((BleService.LocalBinder) rawBinder).getService();
            mBle = mService.getBle();
            if (mBle != null && !mBle.adapterEnabled()) {

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName classname) {
            mService = null;
        }
    };

    /**
     * 创建函数
     */
    @Override
    public void onCreate() {
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
        sInstance = this;

        Intent bindIntent = new Intent(this, BleService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 获取单件对象
     *
     * @return JP应用程序对象
     */
    public static DashuHdApplication getInstance() {
        return sInstance;
    }

    public IBle getIBle() {
        return mBle;
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
