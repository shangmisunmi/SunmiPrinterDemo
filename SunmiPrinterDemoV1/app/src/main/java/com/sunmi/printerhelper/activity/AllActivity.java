package com.sunmi.printerhelper.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.UnsupportedEncodingException;

import sunmi.sunmiui.dialog.DialogCreater;
import sunmi.sunmiui.dialog.LoadingDialog;
import woyou.aidlservice.jiuiv5.ICallback;

/**
 * Created by Administrator on 2017/5/27.
 */

public class AllActivity extends BaseActivity implements View.OnClickListener {
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
        sendData(rv);
    }

    private void sendData(final byte[] send){
        if(baseApp.isAidl()){
            AidlUtil.getInstance().sendRawData(send);
        }else{
            BluetoothUtil.sendData(send);
        }
    }

    //打印小票样张（涵盖所有epson指令）
    public void onPrint(View view) {
        byte[] rv = BytesUtil.customData();
        Bitmap head = BitmapFactory.decodeResource(getResources(), R.mipmap.sunmi);
        //打印光栅位图
        rv = BytesUtil.byteMerger(rv,ESCUtil.printBitmap(head, 0));
        //打印光栅位图  倍宽
        rv = BytesUtil.byteMerger(rv,ESCUtil.printBitmap(head, 1));
        //打印光栅位图  倍高
        rv = BytesUtil.byteMerger(rv,ESCUtil.printBitmap(head, 2));
        //打印光栅位图  倍宽倍高
        rv = BytesUtil.byteMerger(rv,ESCUtil.printBitmap(head, 3));
        //选择位图指令  8点单密度
        rv = BytesUtil.byteMerger(rv,ESCUtil.selectBitmap(head, 0));
        //选择位图指令  8点双密度
        rv = BytesUtil.byteMerger(rv,ESCUtil.selectBitmap(head, 1));
        //选择位图指令  24点单密度
        rv = BytesUtil.byteMerger(rv,ESCUtil.selectBitmap(head, 32));
        //选择位图指令  24点双密度
        rv = BytesUtil.byteMerger(rv,ESCUtil.selectBitmap(head, 33));
        //之后将输出可显示的ascii码及制表符
        rv = BytesUtil.byteMerger(rv,BytesUtil.wordData());
        byte[] ascii = new byte[96];
        for(int i = 0; i < 95; i++){
            ascii[i] = (byte) (0x20+i);
        }
        ascii[95] = 0x0A;
        rv = BytesUtil.byteMerger(rv, ascii);
        char[] tabs = new char[116];
        for(int i = 0; i < 116; i++){
            tabs[i] = (char) (0x2500 + i);
        }
        String test = new String(tabs);
        try {
            rv = BytesUtil.byteMerger(rv, test.getBytes("gb18030"));
            rv = BytesUtil.byteMerger(rv, new byte[]{0x0A});
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(baseApp.isAidl()){
            AidlUtil.getInstance().sendRawData(rv);
        }else{
            BluetoothUtil.sendData(rv);
        }

    }
}
