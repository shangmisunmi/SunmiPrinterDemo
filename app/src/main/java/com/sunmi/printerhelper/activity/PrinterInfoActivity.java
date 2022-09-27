package com.sunmi.printerhelper.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.printerhelper.BuildConfig;
import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.SunmiPrintHelper;


/**
 * Printer information display page
 */
public class PrinterInfoActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setMyTitle(R.string.info_title);
        setBack();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateInfo();
    }

    /**
     * update printer info
     */
    private void updateInfo(){
        ((TextView)findViewById(R.id.info1)).setText(SunmiPrintHelper.getInstance().getPrinterSerialNo());
        ((TextView)findViewById(R.id.info2)).setText(SunmiPrintHelper.getInstance().getDeviceModel());
        ((TextView)findViewById(R.id.info3)).setText(SunmiPrintHelper.getInstance().getPrinterVersion());
        ((TextView)findViewById(R.id.info4)).setText(SunmiPrintHelper.getInstance().getPrinterPaper());
        SunmiPrintHelper.getInstance().getPrinterDistance(new InnerResultCallback() {
            @Override
            public void onRunResult(boolean isSuccess) throws RemoteException {

            }

            @Override
            public void onReturnString(String result) throws RemoteException {
                ((TextView)findViewById(R.id.info5)).setText(result + "mm");
            }

            @Override
            public void onRaiseException(int code, String msg) throws RemoteException {

            }

            @Override
            public void onPrintResult(int code, String msg) throws RemoteException {

            }
        });
        SunmiPrintHelper.getInstance().getPrinterHead(new InnerResultCallback() {
            @Override
            public void onRunResult(boolean isSuccess) throws RemoteException {

            }

            @Override
            public void onReturnString(String result) throws RemoteException {
                ((TextView)findViewById(R.id.info7)).setText(result);
            }

            @Override
            public void onRaiseException(int code, String msg) throws RemoteException {

            }

            @Override
            public void onPrintResult(int code, String msg) throws RemoteException {

            }
        });
        getServiceVersion();
        ((TextView)findViewById(R.id.info9)).setText(BuildConfig.VERSION_NAME);
    }

    /**
     * get printservice version and version code
     */
    private void getServiceVersion(){
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("woyou.aidlservice.jiuiv5", 0);
            if(packageInfo != null){
                ((TextView)findViewById(R.id.info6)).setText(packageInfo.versionName);
                ((TextView)findViewById(R.id.info8)).setText(packageInfo.versionCode+"");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

}
