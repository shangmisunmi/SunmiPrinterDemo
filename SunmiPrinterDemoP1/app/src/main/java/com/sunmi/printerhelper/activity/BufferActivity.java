package com.sunmi.printerhelper.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.AidlUtil;
import com.sunmi.printerhelper.utils.BytesUtil;

import woyou.aidlservice.jiuiv5.ICallback;

/**
 * 描述 : 事务打印模块
 * 作者 : kaltin
 * 日期 : 2017/11/9 15:32
 */

public class BufferActivity extends BaseActivity {

    boolean mark;
    TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer);
        setMyTitle(R.string.buffer_title);
        setBack();

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void initView() {
        mTextView = (TextView)findViewById(R.id.buffer_info);

        findViewById(R.id.buffer_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mark){
                    mark = false;
                    v.setBackgroundColor(getResources().getColor(R.color.text));
                    ((TextView)v).setText(R.string.enter_work);
                }else{
                    mark = true;
                    v.setBackgroundColor(getResources().getColor(R.color.gray));
                    ((TextView)v).setText(R.string.exit_work);
                }
            }
        });

        findViewById(R.id.buffer_print).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] rv = BytesUtil.getBaiduTestBytes();
                if(mark){
                    mTextView.setText(R.string.start_work);
                    AidlUtil.getInstance().sendRawDatabyBuffer(rv, mICallback);
                }else{
                    mTextView.setText(R.string.start_work_low);
                    AidlUtil.getInstance().sendRawData(rv);
                }
            }
        });
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

        //事务打印回调接口
        @Override
        public void onPrintResult(int code, String msg) throws RemoteException {
            final int res = code;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(res == 0){//打印成功
                        mTextView.setText(R.string.over_work);
                    }else{//打印失败
                        mTextView.setText(R.string.error_work);
                    }
                }
            });
        }
    };
}
