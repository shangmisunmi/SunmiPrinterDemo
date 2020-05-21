package com.sunmi.printerhelper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.BluetoothUtil;
import com.sunmi.printerhelper.utils.BytesUtil;
import com.sunmi.printerhelper.utils.ESCUtil;
import com.sunmi.printerhelper.utils.SunmiPrintHelper;

/**
 *  Only Desktop printer（T1、T2） supports black mark function, this is how to call black mark api
 *  @author kaltin
 */
public class BlackLabelActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklabel);
        setMyTitle(R.string.blackline_title);
        setBack();
        initView();
    }

    private void initView() {
        findViewById(R.id.bl_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BlackLabelActivity.this, R.string.toast_11, Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.bl_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SunmiPrintHelper.getInstance().isBlackLabelMode()){
                    if(!BluetoothUtil.isBlueToothPrinter){
                        SunmiPrintHelper.getInstance().sendRawData(ESCUtil.gogogo());
                    }else{
                        BluetoothUtil.sendData(ESCUtil.gogogo());
                    }
                }else{
                    Toast.makeText(BlackLabelActivity.this, R.string.toast_10, Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.bl_sample).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!SunmiPrintHelper.getInstance().isBlackLabelMode()){
                    Toast.makeText(BlackLabelActivity.this, R.string.toast_10, Toast.LENGTH_LONG).show();
                }else{
                    if(!BluetoothUtil.isBlueToothPrinter){
                        SunmiPrintHelper.getInstance().printQr("www.sunmi.com", 6, 3);
                        SunmiPrintHelper.getInstance().print3Line();
                        SunmiPrintHelper.getInstance().sendRawData(ESCUtil.gogogo());
                        SunmiPrintHelper.getInstance().cutpaper();
                    }else{
                        byte[] rv = ESCUtil.getPrintQRCode("www.sunmi.com", 6, 3);
                        rv = BytesUtil.byteMerger(rv, new byte[]{0xa, 0xa,0xa});
                        BluetoothUtil.sendData(rv);
                        BluetoothUtil.sendData(ESCUtil.gogogo());
                        BluetoothUtil.sendData(ESCUtil.cutter());
                    }
                }
            }
        });
    }
}
