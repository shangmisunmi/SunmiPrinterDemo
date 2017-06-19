package com.sunmi.printerhelper.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.AidlUtil;
import com.sunmi.printerhelper.utils.BluetoothUtil;
import com.sunmi.printerhelper.utils.BytesUtil;
import com.sunmi.printerhelper.utils.ESCUtil;

import sunmi.sunmiui.dialog.DialogCreater;
import sunmi.sunmiui.dialog.LoadingDialog;
import woyou.aidlservice.jiuiv5.ICallback;

/**
 * Created by Administrator on 2017/5/27.
 */

public class AllActivity extends BaseActivity implements View.OnClickListener {
    boolean mark = false;
    LoadingDialog mDialog;
    byte[] temp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        setMyTitle(R.string.all_title);
        setBack();

        findViewById(R.id.multi_one).setOnClickListener(this);
        findViewById(R.id.multi_two).setOnClickListener(this);
        findViewById(R.id.multi_three).setOnClickListener(this);
        findViewById(R.id.multi_four).setOnClickListener(this);
        findViewById(R.id.multi_five).setOnClickListener(this);
        findViewById(R.id.multi_six).setOnClickListener(this);

        findViewById(R.id.multi_buffer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!baseApp.isAidl()){
                    Toast.makeText(AllActivity.this, "事务打印请使用Aidl模式！", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    if(mark){
                        mark = false;
                        v.setBackgroundColor(getResources().getColor(R.color.text));
                        ((TextView)v).setText("进入事务打印");
                    }else{
                        mark = true;
                        v.setBackgroundColor(getResources().getColor(R.color.gray));
                        ((TextView)v).setText("退出事务打印");
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        byte[] rv = null;
        switch (v.getId()){
            case R.id.multi_one:
                rv = BytesUtil.getBaiduTestBytes();
                break;
            case R.id.multi_two:
                rv = BytesUtil.getMeituanBill();
                break;
            case R.id.multi_three:
                rv = BytesUtil.getErlmoData();
                break;
            case R.id.multi_four:
                rv = BytesUtil.getKoubeiData();
                break;
            case R.id.multi_five:
                rv = ESCUtil.printBitmap(BytesUtil.initBlackBlock(384));
                break;
            case R.id.multi_six:
                rv = ESCUtil.printBitmap(BytesUtil.initBlackBlock(800,384));
                break;
        }

        if(mark){
            sendDataThread(rv);
        }else{
            sendData(rv);
        }
    }

    ICallback mICallback = new ICallback.Stub() {
        @Override
        public void onRunResult(boolean isSuccess) throws RemoteException {

        }

        @Override
        public void onReturnString(String result) throws RemoteException {

        }

        @Override
        public void onRaiseException(int code, String msg) throws RemoteException {

        }

        @Override
        public void onPrintResult(int code, String msg) throws RemoteException {
            if(code == 0){
                mDialog.cancel();
            }else{
                AidlUtil.getInstance().sendRawDatabyBuffer(temp, mICallback);
            }
        }
    };

    private void sendDataThread(byte[] send){
        mDialog = DialogCreater.createLoadingDialog(this, "打印中~~~");
        //mDialog.show();
        temp = send;
        AidlUtil.getInstance().sendRawDatabyBuffer(temp, mICallback);
    }


    private void sendData(final byte[] send){
        if(baseApp.isAidl()){
            AidlUtil.getInstance().sendRawData(send);
        }else{
            BluetoothUtil.sendData(send);
        }
    }
}
