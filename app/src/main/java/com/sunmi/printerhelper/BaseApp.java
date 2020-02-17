package com.sunmi.printerhelper;

import android.app.Application;

import com.sunmi.printerhelper.utils.SunmiPrintHelper;

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    /**
     * Connect print service through interface library
     */
    private void init(){
        SunmiPrintHelper.getInstance().initSunmiPrinterService(this);
    }
}
