package com.sunmi.printerhelper.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.SunmiPrintHelper;

/**
 * Example of transaction printing
 * Transaction printing allows developers to get the real results of printing,
 * So it can only be called through the API not supported by ble
 *
 * Currently, only V1 model are not supported for transaction printing
 * @author kaltin
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
        mTextView = findViewById(R.id.buffer_info);

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
                if(mark){
                    mTextView.setText(R.string.start_work);
                    SunmiPrintHelper.getInstance().printTrans(getApplicationContext(), mCallback);
                }else{
                    mTextView.setText(R.string.start_work_low);
                    SunmiPrintHelper.getInstance().printExample(getApplicationContext());
                }
            }
        });
    }

    InnerResultCallback mCallback = new InnerResultCallback() {
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
            final int res = code;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(res == 0){
                        mTextView.setText(R.string.over_work);
                        //TODO Follow-up after successful
                    }else{
                        mTextView.setText(R.string.error_work);
                        //TODO Follow-up after failed, such as reprint
                    }
                }
            });
        }
    };
}
