package com.sophix.client;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.sophix.client.util.AppConstants;
import com.sophix.client.util.AppManagerUtils;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

/**
 * Created by Hongmingwei on 2017/9/5.
 * Email: 648600445@qq.com
 */

public class SophixApplication extends Application {
    /**
     * TAG
     */
    private static final String TAG = SophixApplication.class.getSimpleName();

    private boolean appActiveFlag;
    private static SophixApplication instance = null;

    public SophixApplication() {
        super();
        instance = this;
    }

    public static SophixApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initSophix();
    }
    public void setAppActiveFlag(boolean appActiveFlag){
        this.appActiveFlag = appActiveFlag;
    }
    public boolean getAppActiveFlag(){
        return appActiveFlag;
    }
    private void initSophix() {
        setAppActiveFlag(false);
        SophixManager.getInstance().setContext(this)
                .setAppVersion(AppManagerUtils.getAppVersion(this))//应用的版本号
                .setSecretMetaData(AppConstants.APP_ID, AppConstants.APP_SECRET, AppConstants.APP_RSA_KEY) //设置id, secret, rsa，提高安全性，便于混淆代码
                .setAesKey(AppConstants.APP_AES_KEY) //可选，对补丁进行对称加密，必须是16位数字或字母组合
                .setEnableDebug(true) //上线一定要设置为false，这是设置是否可调试模式，默认false
                .setUnsupportedModel(Build.MODEL, Build.VERSION.SDK_INT) //加入不支持设备的黑名单不进行热修复
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {//设置补丁加载状态监听
                    @Override
                    public void onLoad(int mode, int code, String info, int i2) {
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            Log.e(TAG, "onLoad: ==== 版本更新成功");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            //需要冷启动来进行更新
                            setAppActiveFlag(true);
                        } else if (code == PatchStatus.CODE_LOAD_FAIL){
                            //补丁加载失败，需要清空本地补丁，不需要调用，系统监听到补丁引发崩溃情况会自动清空
                        }
                    }
                }).initialize();//初始化并加载本地补丁
        //从服务器拉取补丁
        SophixManager.getInstance().queryAndLoadNewPatch();
    }
}
