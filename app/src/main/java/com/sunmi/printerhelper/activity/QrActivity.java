package com.sunmi.printerhelper.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.BitmapUtil;
import com.sunmi.printerhelper.utils.BluetoothUtil;
import com.sunmi.printerhelper.utils.ESCUtil;
import com.sunmi.printerhelper.utils.SunmiPrintHelper;

import sunmi.sunmiui.dialog.DialogCreater;
import sunmi.sunmiui.dialog.EditTextDialog;
import sunmi.sunmiui.dialog.ListDialog;

/**
 *  Example of printing a QR code
 *  @author kaltin
 */
public class QrActivity extends BaseActivity {
    private ImageView mImageView;
    private TextView mTextView1, mTextView2, mTextView3, mTextView4, mTextView5;
    private int print_num = 0;
    private int print_size = 8;
    private int error_level = 3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        setMyTitle(R.string.qr_title);
        setBack();

        mImageView = findViewById(R.id.qr_image);
        mTextView1 = findViewById(R.id.qr_text_content);
        mTextView2 = findViewById(R.id.qr_text_num);
        mTextView3 = findViewById(R.id.qr_text_size);
        mTextView4 = findViewById(R.id.qr_text_el);
        mTextView5 = findViewById(R.id.qr_align_info);

        findViewById(R.id.qr_content).setOnClickListener(new View.OnClickListener() {
            EditTextDialog mDialog;

            @Override
            public void onClick(View v) {
                mDialog = DialogCreater.createEditTextDialog(QrActivity.this, getResources().getString(R.string.cancel), getResources().getString(R.string.confirm), getResources().getString(R.string.input_qrcode), new View.OnClickListener() {
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
                final String[] mStrings = new String[]{getResources().getString(R.string.single), getResources().getString(R.string.twice)};
                final ListDialog listDialog = DialogCreater.createListDialog(QrActivity.this,  getResources().getString(R.string.array_qrcode),getResources().getString(R.string.cancel), mStrings);
                listDialog.setItemClickListener(new ListDialog.ItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        if (!BluetoothUtil.isBlueToothPrinter) {
                            Toast.makeText(QrActivity.this, R.string.toast_7, Toast.LENGTH_LONG).show();
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
                final ListDialog listDialog = DialogCreater.createListDialog(QrActivity.this, getResources().getString(R.string.size_qrcode), getResources().getString(R.string.cancel), new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
                listDialog.setItemClickListener(new ListDialog.ItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        position += 1;
                        if (print_num == 1 && Integer.parseInt(mTextView3.getText().toString()) > 7) {
                            Toast.makeText(QrActivity.this, R.string.toast_8, Toast.LENGTH_LONG).show();
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
                final ListDialog listDialog = DialogCreater.createListDialog(QrActivity.this, getResources().getString(R.string.error_qrcode), getResources().getString(R.string.cancel), el);
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

        findViewById(R.id.qr_align).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String[] pos = new String[]{getResources().getString(R.string.align_left),getResources().getString(R.string.align_mid), getResources().getString(R.string.align_right)};
                final ListDialog listDialog =  DialogCreater.createListDialog(QrActivity.this, getResources().getString(R.string.align_form), getResources().getString(R.string.cancel), pos);
                listDialog.setItemClickListener(new ListDialog.ItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        mTextView5.setText(pos[position]);
                        if(!BluetoothUtil.isBlueToothPrinter){
                            SunmiPrintHelper.getInstance().setAlign(position);
                        }else{
                            byte[] send;
                            if(position == 0){
                                send = ESCUtil.alignLeft();
                            }else if(position == 1){
                                send = ESCUtil.alignCenter();
                            }else {
                                send = ESCUtil.alignRight();
                            }
                            BluetoothUtil.sendData(send);
                        }
                        listDialog.cancel();
                    }
                });
                listDialog.show();
            }
        });
    }

    public void onClick(View view) {
        Bitmap bitmap = BitmapUtil.generateBitmap(mTextView1.getText().toString(), 9, 700, 700);
        if (bitmap != null) {
            mImageView.setImageDrawable(new BitmapDrawable(bitmap));
        }

        if (!BluetoothUtil.isBlueToothPrinter) {
            SunmiPrintHelper.getInstance().printQr(mTextView1.getText().toString(), print_size, error_level);
            SunmiPrintHelper.getInstance().feedPaper();
        } else {
            if (print_num == 0) {
                BluetoothUtil.sendData(ESCUtil.getPrintQRCode(mTextView1.getText().toString(), print_size, error_level));
                BluetoothUtil.sendData(ESCUtil.nextLine(3));
            } else {
                BluetoothUtil.sendData(ESCUtil.getPrintDoubleQRCode(mTextView1.getText().toString(), mTextView1.getText().toString(), print_size, error_level));
                BluetoothUtil.sendData(ESCUtil.nextLine(3));
            }

        }
    }
}
