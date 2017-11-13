package com.sunmi.printerhelper.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.printerhelper.BuildConfig;
import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.AidlUtil;
import com.sunmi.printerhelper.utils.PrinterCallback;

import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
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
        List<String> info = AidlUtil.getInstance().getPrinterInfo(new PrinterCallback() {
            String result;

            @Override
            public String getResult() {
                return result;
            }

            @Override
            public void onReturnString(String result) {
                this.result = result;
            }


        });
        if(info != null && info.size() == 7){
            ((TextView)findViewById(R.id.info1)).setText(info.get(0));
            ((TextView)findViewById(R.id.info2)).setText(info.get(1));
            ((TextView)findViewById(R.id.info3)).setText(info.get(2));
            ((TextView)findViewById(R.id.info5)).setText(info.get(3)+"mm");
            ((TextView)findViewById(R.id.info6)).setText(info.get(5));
            ((TextView)findViewById(R.id.info7)).setText(info.get(4));
            ((TextView)findViewById(R.id.info8)).setText(info.get(6));
        }else{
            Toast.makeText(this, R.string.toast_2,Toast.LENGTH_LONG).show();
        }
        ((TextView)findViewById(R.id.info9)).setText(BuildConfig.VERSION_NAME);
    }

}
