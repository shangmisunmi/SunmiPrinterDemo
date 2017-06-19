package com.sunmi.printerhelper.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

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
 * Created by Administrator on 2017/5/2.
 */

public class BarCodeActivity extends BaseActivity {
    private TextView mTextView1, mTextView2, mTextView3, mTextView4, mTextView5;
    private ImageView mImageView;
    private int encode, position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        setMyTitle(R.string.barcode_title);
        setBack();

        encode = 8;
        position = 1;
        mTextView1 = (TextView) findViewById(R.id.bc_text_content);
        mTextView2 = (TextView) findViewById(R.id.bc_text_encode);
        mTextView3 = (TextView) findViewById(R.id.bc_text_position);
        mTextView4 = (TextView) findViewById(R.id.bc_text_width);
        mTextView5 = (TextView) findViewById(R.id.bc_text_height);
        mImageView = (ImageView) findViewById(R.id.bc_image);

        findViewById(R.id.bc_content).setOnClickListener(new View.OnClickListener() {
            EditTextDialog mDialog;

            @Override
            public void onClick(View v) {
                mDialog = DialogCreater.createEditTextDialog(BarCodeActivity.this, "取消", "确定", "请输入条形码内容", new View.OnClickListener() {
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
                mDialog.show();
            }
        });

        findViewById(R.id.bc_encode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] list = new String[]{"UPC-A", "UPC-E", "EAN13", "EAN8", "CODE39", "ITF", "CODABAR", "CODE93", "CODE128"};
                final ListDialog listDialog = DialogCreater.createListDialog(BarCodeActivity.this, "条形码编码", "取消", list);
                listDialog.setItemClickListener(new ListDialog.ItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {

                        mTextView2.setText(list[position]);
                        encode = position;
                        listDialog.cancel();
                    }
                });
                listDialog.show();
            }
        });

        findViewById(R.id.bc_position).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] list = new String[]{"不打印文字", "条形码上方", "条形码下方", "条形码上下方"};
                final ListDialog listDialog = DialogCreater.createListDialog(BarCodeActivity.this, "文字位置", "取消", list);
                listDialog.setItemClickListener(new ListDialog.ItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        mTextView3.setText(list[position]);
                        BarCodeActivity.this.position = position;
                        listDialog.cancel();
                    }
                });
                listDialog.show();
            }
        });

        findViewById(R.id.bc_height).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSeekBarDialog(BarCodeActivity.this, "条形码高度", 1, 255, mTextView5);
            }
        });

        findViewById(R.id.bc_width).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSeekBarDialog(BarCodeActivity.this, "条形码宽度", 2, 6, mTextView4);
            }
        });

        AidlUtil.getInstance().initPrinter();
    }

    public void onClick(View view) {
        String text = mTextView1.getText().toString();
        Bitmap bitmap = BitmapUtil.generateBitmap(text, encode, 700, 400);
        if (bitmap != null) {
            mImageView.setImageDrawable(new BitmapDrawable(bitmap));
        }

        int height = Integer.parseInt(mTextView5.getText().toString());
        int width = Integer.parseInt(mTextView4.getText().toString());
        if (baseApp.isAidl()) {
            AidlUtil.getInstance().printBarCode(text, encode, height, width, position);
        } else {

            BluetoothUtil.sendData(ESCUtil.getPrintBarCode(text, encode, height, width, position));
            BluetoothUtil.sendData(ESCUtil.nextLine(3));

        }
    }

    /**
     * 自定义的seekbar dialog
     *
     * @param context
     * @param title
     * @param min
     * @param max
     * @param set
     */
    private void showSeekBarDialog(Context context, String title, final int min, final int max, final TextView set) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.widget_seekbar, null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        TextView tv_title = (TextView) view.findViewById(R.id.sb_title);
        TextView tv_start = (TextView) view.findViewById(R.id.sb_start);
        TextView tv_end = (TextView) view.findViewById(R.id.sb_end);
        final TextView tv_result = (TextView) view.findViewById(R.id.sb_result);
        TextView tv_ok = (TextView) view.findViewById(R.id.sb_ok);
        TextView tv_cancel = (TextView) view.findViewById(R.id.sb_cancel);
        SeekBar sb = (SeekBar) view.findViewById(R.id.sb_seekbar);
        tv_title.setText(title);
        tv_start.setText(min + "");
        tv_end.setText(max + "");
        tv_result.setText(set.getText());
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set.setText(tv_result.getText());
                dialog.cancel();
            }
        });
        sb.setMax(max - min);
        sb.setProgress(Integer.parseInt(set.getText().toString()) - min);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int rs = min + progress;
                tv_result.setText(rs + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dialog.show();
    }
}
