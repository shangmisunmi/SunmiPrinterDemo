package com.sunmi.printerhelper.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.BitmapUtil;
import com.sunmi.printerhelper.utils.BluetoothUtil;
import com.sunmi.printerhelper.utils.ESCUtil;
import com.sunmi.printerhelper.utils.SunmiPrintHelper;

import sunmi.sunmiui.dialog.DialogCreater;
import sunmi.sunmiui.dialog.EditTextDialog;
import sunmi.sunmiui.dialog.ListDialog;

/**
 *  Example of printing a bar code
 *  @author kaltin
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
        mTextView1 = findViewById(R.id.bc_text_content);
        mTextView2 = findViewById(R.id.bc_text_encode);
        mTextView3 = findViewById(R.id.bc_text_position);
        mTextView4 = findViewById(R.id.bc_text_width);
        mTextView5 = findViewById(R.id.bc_text_height);
        mImageView = findViewById(R.id.bc_image);

        findViewById(R.id.bc_content).setOnClickListener(new View.OnClickListener() {
            EditTextDialog mDialog;

            @Override
            public void onClick(View v) {
                mDialog = DialogCreater.createEditTextDialog(BarCodeActivity.this,getResources().getString(R.string.cancel), getResources().getString(R.string.confirm), getResources().getString(R.string.input_barcode), new View.OnClickListener() {
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
                final String[] list = new String[]{"UPC-A", "UPC-E", "EAN13", "EAN8", "CODE39", "ITF", "CODABAR", "CODE93", "CODE128A", "CODE128B", "CODE128C"};
                final ListDialog listDialog = DialogCreater.createListDialog(BarCodeActivity.this, getResources().getString(R.string.encode_barcode), getResources().getString(R.string.cancel), list);
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
                final String[] list = new String[]{getResources().getString(R.string.no_print), getResources().getString(R.string.barcode_up), getResources().getString(R.string.barcode_down), getResources().getString(R.string.barcode_updown)};
                final ListDialog listDialog = DialogCreater.createListDialog(BarCodeActivity.this, getResources().getString(R.string.text_position), getResources().getString(R.string.cancel), list);
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
                showSeekBarDialog(BarCodeActivity.this, getResources().getString(R.string.barcode_height), 1, 255, mTextView5);
            }
        });

        findViewById(R.id.bc_width).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSeekBarDialog(BarCodeActivity.this, getResources().getString(R.string.barcode_width), 2, 6, mTextView4);
            }
        });
    }

    public void onClick(View view) {
        String text = mTextView1.getText().toString();
        int symbology;
        if(encode > 7){
            symbology = 8;
        }else{
            symbology = encode;
        }
        Bitmap bitmap = BitmapUtil.generateBitmap(text, symbology, 700, 400);
        if (bitmap != null) {
            mImageView.setImageDrawable(new BitmapDrawable(bitmap));
        }else{
            Toast.makeText(this, R.string.toast_9, Toast.LENGTH_LONG).show();
        }

        int height = Integer.parseInt(mTextView5.getText().toString());
        int width = Integer.parseInt(mTextView4.getText().toString());
        if (!BluetoothUtil.isBlueToothPrinter) {
            SunmiPrintHelper.getInstance().printBarCode(text, encode, height, width, position);
            SunmiPrintHelper.getInstance().feedPaper();
        } else {
            BluetoothUtil.sendData(ESCUtil.getPrintBarCode(text, encode, height, width, position));
            BluetoothUtil.sendData(ESCUtil.nextLine(3));
        }
    }

    /**
     * custom seekbar dialog
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
