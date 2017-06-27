package com.sunmi.printerhelper.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.AidlUtil;
import com.sunmi.printerhelper.utils.BitmapUtil;
import com.sunmi.printerhelper.utils.BluetoothUtil;
import com.sunmi.printerhelper.utils.ESCUtil;

import java.io.IOException;

import sunmi.sunmiui.dialog.DialogCreater;
import sunmi.sunmiui.dialog.EditTextDialog;
import sunmi.sunmiui.dialog.ListDialog;


/**
 * Created by Administrator on 2017/4/28.
 */

public class QrActivity extends BaseActivity {
    private ImageView mImageView;
    private TextView mTextView1, mTextView2, mTextView3, mTextView4;
    private int print_num = 0;
    private int print_size = 8;
    private int error_level = 3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        setMyTitle(R.string.qr_title);
        setBack();

        mImageView = (ImageView) findViewById(R.id.qr_image);
        mTextView1 = (TextView) findViewById(R.id.qr_text_content);
        mTextView2 = (TextView) findViewById(R.id.qr_text_num);
        mTextView3 = (TextView) findViewById(R.id.qr_text_size);
        mTextView4 = (TextView) findViewById(R.id.qr_text_el);

        findViewById(R.id.qr_content).setOnClickListener(new View.OnClickListener() {
            EditTextDialog mDialog;

            @Override
            public void onClick(View v) {
                mDialog = DialogCreater.createEditTextDialog(QrActivity.this, "取消", "确定", "请输入二维码内容", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.cancel();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTextView1.setText(mDialog.getEditText().getText());
                        mDialog.cancel();
                    }
                }, null);
                mDialog.setHintText("www.sunmi.com");
                mDialog.show();
            }
        });

        findViewById(R.id.qr_num).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] mStrings = new String[]{"单个", "双个"};
                final ListDialog listDialog = DialogCreater.createListDialog(QrActivity.this, "二维码排列方式", "取消", mStrings);
                listDialog.setItemClickListener(new ListDialog.ItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        if (baseApp.isAidl()) {
                            Toast.makeText(QrActivity.this, "AIDL 暂时只打印单个二维码", Toast.LENGTH_LONG).show();
                            position = 0;
                        } else {
                            mTextView3.setText("7");
                            print_size = 7;
                        }
                        mTextView2.setText(mStrings[position]);
                        print_num = position;
                        listDialog.cancel();
                    }
                });
                listDialog.show();
            }
        });

        findViewById(R.id.qr_size).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListDialog listDialog = DialogCreater.createListDialog(QrActivity.this, "二维码块大小", "取消", new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
                listDialog.setItemClickListener(new ListDialog.ItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        position += 1;
                        if (print_num == 1 && Integer.parseInt(mTextView3.getText().toString()) > 7) {
                            Toast.makeText(QrActivity.this, "两个二维码尺寸最大为7", Toast.LENGTH_LONG).show();
                            position = 7;
                        }
                        mTextView3.setText("" + position);
                        print_size = position;
                        listDialog.cancel();
                    }
                });
                listDialog.show();
            }
        });

        findViewById(R.id.qr_el).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] el = new String[]{"纠错级别L (7%)", "纠错级别M (15%)", "纠错级别Q (25%)", "纠错级别H (30%)"};
                final ListDialog listDialog = DialogCreater.createListDialog(QrActivity.this, "二维码纠错等级", "取消", el);
                listDialog.setItemClickListener(new ListDialog.ItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        mTextView4.setText(el[position]);
                        error_level = position;
                        listDialog.cancel();
                    }
                });
                listDialog.show();
            }
        });

        AidlUtil.getInstance().initPrinter();
    }

    public void onClick(View view) {
        Bitmap bitmap = BitmapUtil.generateBitmap(mTextView1.getText().toString(), 9, 700, 700);
        if (bitmap != null) {
            mImageView.setImageDrawable(new BitmapDrawable(bitmap));
        }

        //根据当前模式选择进行二维码打印
        if (baseApp.isAidl()) {
            AidlUtil.getInstance().printQr(mTextView1.getText().toString(), print_size, error_level);
        } else {

            if (print_num == 0) {
                BluetoothUtil.sendData(ESCUtil.getPrintQRCode(mTextView1.getText().toString(), print_size, error_level));
            } else {
                BluetoothUtil.sendData(ESCUtil.getPrintDoubleQRCode(mTextView1.getText().toString(), mTextView1.getText().toString(), print_size, error_level));
            }

        }
    }
}
