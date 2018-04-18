package com.sunmi.printerhelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.AidlUtil;
import com.sunmi.printerhelper.utils.BluetoothUtil;
import com.sunmi.printerhelper.utils.BytesUtil;
import com.sunmi.printerhelper.utils.ESCUtil;

/**
 * 描述 : BlackLabel Test
 * 作者 : kaltin
 * 日期 : 2017/11/9 11:08
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
                Intent intent = new Intent();
                intent.setClassName("woyou.aidlservice.jiuiv5", "woyou.aidlservice.jiuiv5.SettingActivity");
                startActivity(intent);
            }
        });

        findViewById(R.id.bl_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AidlUtil.getInstance().getPrintMode() == 1){
                    if(baseApp.isAidl()){
                        AidlUtil.getInstance().sendRawData(ESCUtil.gogogo());
                    }else{
                        BluetoothUtil.sendData(ESCUtil.gogogo());
                    }
                }else{
                    Toast.makeText(BlackLabelActivity.this, R.string.toast_9, Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.bl_sample).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AidlUtil.getInstance().getPrintMode() != 1){
                    Toast.makeText(BlackLabelActivity.this, R.string.toast_9, Toast.LENGTH_LONG).show();
                }else{
                    byte[] rv = ESCUtil.getPrintQRCode("www.sunmi.com", 6, 3);
                    rv = BytesUtil.byteMerger(rv, new byte[]{0xa, 0xa,0xa});
                    if(baseApp.isAidl()){
                        AidlUtil.getInstance().sendRawData(rv);
                        AidlUtil.getInstance().sendRawData(ESCUtil.gogogo());
                        AidlUtil.getInstance().sendRawData(ESCUtil.cutter());
                    }else{
                        BluetoothUtil.sendData(rv);
                        BluetoothUtil.sendData(ESCUtil.gogogo());
                        BluetoothUtil.sendData(ESCUtil.cutter());
                    }
                }
            }
        });
    }
}
