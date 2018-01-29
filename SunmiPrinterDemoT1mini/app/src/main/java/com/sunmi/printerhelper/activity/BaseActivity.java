package com.sunmi.printerhelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sunmi.printerhelper.BaseApp;
import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.AidlUtil;
import com.sunmi.printerhelper.utils.BluetoothUtil;
import com.sunmi.printerhelper.utils.BytesUtil;

import sunmi.sunmiui.dialog.DialogCreater;
import sunmi.sunmiui.dialog.EditTextDialog;

/**
 *
 * Created by Administrator on 2017/4/27.
 */

public abstract class BaseActivity extends AppCompatActivity{
    BaseApp baseApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseApp = (BaseApp) getApplication();
    }

    /**
     * 设置标题
     * @param title
     */
    void setMyTitle(String title){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
    }

    void setMyTitle(@StringRes int title){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        if(!baseApp.isAidl()){
            actionBar.setSubtitle("bluetooth®");
        }else{
            actionBar.setSubtitle("");
        }
    }

    void setBack(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hexprint, menu);
        return true;
    }

    EditTextDialog mEditTextDialog;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_print:
                //Toast.makeText(this, "将实现十六进制指令发送", Toast.LENGTH_SHORT).show();
                mEditTextDialog = DialogCreater.createEditTextDialog(this, getResources().getString(R.string.cancel), getResources().getString(R.string.confirm), getResources().getString(R.string.input_order), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEditTextDialog.cancel();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = mEditTextDialog.getEditText().getText().toString();
                        AidlUtil.getInstance().sendRawData(BytesUtil.getBytesFromHexString(text));
                        mEditTextDialog.cancel();
                    }
                }, null);
                mEditTextDialog.show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
