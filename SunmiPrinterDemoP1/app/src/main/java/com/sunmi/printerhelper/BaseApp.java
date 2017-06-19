package com.sunmi.printerhelper;

import android.app.Application;
import android.widget.Toast;

import com.sunmi.printerhelper.utils.AidlUtil;

/**
 * Created by Administrator on 2017/4/27.
 */

public class BaseApp extends Application {
    private boolean isAidl;

    public boolean isAidl() {
        return isAidl;
    }

    public void setAidl(boolean aidl) {
        isAidl = aidl;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isAidl = true;
        AidlUtil.getInstance().connectPrinterService(this);
    }
}
