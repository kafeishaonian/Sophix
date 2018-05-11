package com.sophix.client;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.sophix.client.util.AppManagerUtils;
import com.sophix.client.util.CheckPermission;
import com.taobao.sophix.SophixManager;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {
    /**
     * TAG
     */
    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * params
     */
    private static final int REQUEST_CODE = 0;//请求码
    private CheckPermission mPermission;

    static final String[] PERMISSION = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_WIFI_STATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPermission = new CheckPermission(this);

        EventBus.getDefault().post("");
        EventBus.getDefault().register(this);
        EventBus.getDefault().unregister(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        initSophix();
    }

    private void initSophix(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mPermission.setPermission(PERMISSION)) {
                ActivityCompat.requestPermissions(this,
                        PERMISSION,
                        REQUEST_CODE);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                //提示对话框设置权限
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        //判断当前应用是否在后台，以及是否需要杀死进程来进行更新补丁
        if (!AppManagerUtils.isAppOnForeground() && SophixApplication.getInstance().getAppActiveFlag()){
            //当应用在后台的时候  杀死进程
            SophixManager.getInstance().killProcessSafely();
        }
    }

}
