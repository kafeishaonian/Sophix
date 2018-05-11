package com.sophix.client.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.sophix.client.SophixApplication;

import java.util.List;

/**
 * Created by Hongmingwei on 2017/9/5.
 * Email: 648600445@qq.com
 */

public class AppManagerUtils {
    /**
     *  判断当前应用是否在后台
     * @return
     */
    public static boolean isAppOnForeground(){
        ActivityManager activityManager = (ActivityManager) SophixApplication.getInstance().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = SophixApplication.getInstance().getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
    /**
     * 获取app当前版本号
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        String appVersion = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appVersion = String.valueOf(packageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

}
