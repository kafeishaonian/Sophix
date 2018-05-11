package com.sophix.client.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * 检查权限工具类
 * Created by Hongmingwei on 2017/9/5.
 * Email: 648600445@qq.com
 */

public class CheckPermission {


    private final Context mContext;

    public CheckPermission(Context context){
        mContext = context.getApplicationContext();
    }

    /**
     * 检查权限，判断系统的权限集合
     * @param permissions
     * @return
     */
    public boolean setPermission(String... permissions){
        for (String permission : permissions){
            if (isLackPermission(permission)){
                return true;
            }
        }
        return false;
    }

    /**
     * 检查系统权限，判断档期那是否缺少权限
     * @param permission
     * @return
     */
    private boolean isLackPermission(String permission){
        return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_DENIED;
    }

}
