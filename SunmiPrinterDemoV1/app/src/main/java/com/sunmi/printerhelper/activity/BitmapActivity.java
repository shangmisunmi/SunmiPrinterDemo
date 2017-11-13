package com.sunmi.printerhelper.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.AidlUtil;
import com.sunmi.printerhelper.utils.BluetoothUtil;
import com.sunmi.printerhelper.utils.ESCUtil;


import sunmi.sunmiui.dialog.DialogCreater;
import sunmi.sunmiui.dialog.ListDialog;

public class BitmapActivity extends BaseActivity {
    ImageView mImageView;
    TextView mTextView1, mTextView2, mTextView3;
    LinearLayout ll, ll1, ll2;
    Bitmap bitmap, bitmap1;
    CheckBox mCheckBox1, mCheckBox2;

    int mytype;   //位图打印方式
    int myorientation;  //接口打印排列

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
        setMyTitle(R.string.bitmap_title);
        setBack();
        initView();
        if(baseApp.isAidl()){
            ll.setVisibility(View.GONE);
            ll1.setVisibility(View.GONE);
            ll2.setVisibility(View.VISIBLE);
        }else{
            ll.setVisibility(View.VISIBLE);
            ll1.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.GONE);
        }
    }

    private void initView() {
        mTextView1 = (TextView) findViewById(R.id.pic_align_info);
        mTextView2 = (TextView) findViewById(R.id.pic_type_info);
        mTextView3 = (TextView) findViewById(R.id.pic_orientation_info);
        mCheckBox1 = (CheckBox) findViewById(R.id.pic_width);
        mCheckBox2 = (CheckBox) findViewById(R.id.pic_height);
        ll = (LinearLayout) findViewById(R.id.pic_style);
        ll1 = (LinearLayout) findViewById(R.id.pic_type);
        ll2 = (LinearLayout) findViewById(R.id.pic_orientation);
        findViewById(R.id.pic_align).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String[] pos = new String[]{getResources().getString(R.string.align_left), getResources().getString(R.string.align_mid), getResources().getString(R.string.align_right)};
                final ListDialog listDialog =  DialogCreater.createListDialog(BitmapActivity.this, getResources().getString(R.string.align_form), getResources().getString(R.string.cancel), pos);
                listDialog.setItemClickListener(new ListDialog.ItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        mTextView1.setText(pos[position]);
                        byte[] send;
                        if(position == 0){
                            send = ESCUtil.alignLeft();
                        }else if(position == 1){
                            send = ESCUtil.alignCenter();
                        }else {
                            send = ESCUtil.alignRight();
                        }
                        if(baseApp.isAidl()){
                            AidlUtil.getInstance().sendRawData(send);
                        }else{
                            BluetoothUtil.sendData(send);
                        }
                        listDialog.cancel();
                    }
                });
                listDialog.show();
            }
        });

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] orientation = new String[]{getResources().getString(R.string.pic_hor),getResources().getString(R.string.pic_ver)};
                final ListDialog listDialog = DialogCreater.createListDialog(BitmapActivity.this, getResources().getString(R.string.pic_pos), getResources().getString(R.string.cancel), orientation);
                listDialog.setItemClickListener(new ListDialog.ItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        mTextView3.setText(orientation[position]);
                        myorientation = position;
                        listDialog.cancel();
                    }
                });
                listDialog.show();
            }
        });

        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] type = new String[]{getResources().getString(R.string.pic_mode1),getResources().getString(R.string.pic_mode2), getResources().getString(R.string.pic_mode3), getResources().getString(R.string.pic_mode4), getResources().getString(R.string.pic_mode5)};
                final ListDialog listDialog = DialogCreater.createListDialog(BitmapActivity.this, getResources().getString(R.string.pic_mode), getResources().getString(R.string.cancel), type);
                listDialog.setItemClickListener(new ListDialog.ItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        mTextView2.setText(type[position]);
                        mytype = position;
                        if(position == 0){
                            ll.setVisibility(View.VISIBLE);
                        }else{
                            ll.setVisibility(View.INVISIBLE);
                        }
                        listDialog.cancel();
                    }
                });
                listDialog.show();
            }
        });
        mImageView = (ImageView) findViewById(R.id.bitmap_imageview);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTargetDensity = 160;
        options.inDensity = 160;
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test, options);

        }

        if (bitmap1 == null) {
            bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.test1, options);
            bitmap1 = scaleImage(bitmap1);
        }
        if (baseApp.isAidl()) {
            mImageView.setImageDrawable(new BitmapDrawable(bitmap));
        } else {
            mImageView.setImageDrawable(new BitmapDrawable(bitmap1));
        }

    }

    private Bitmap scaleImage(Bitmap bitmap1) {
        int width = bitmap1.getWidth();
        int height = bitmap1.getHeight();
        // 设置想要的大小
        int newWidth = (width/8+1)*8;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, 1);
        // 得到新的图片
        return Bitmap.createBitmap(bitmap1, 0, 0, width, height, matrix, true);
    }

    public void onClick(View view) {
        if (baseApp.isAidl()) {
            AidlUtil.getInstance().printBitmap(bitmap, myorientation);
        } else {
            if(mytype == 0){
                if(mCheckBox1.isChecked() && mCheckBox2.isChecked()){
                    BluetoothUtil.sendData(ESCUtil.printBitmap(bitmap1, 3));
                }else if(mCheckBox1.isChecked()){
                    BluetoothUtil.sendData(ESCUtil.printBitmap(bitmap1, 1));
                }else if(mCheckBox2.isChecked()){
                    BluetoothUtil.sendData(ESCUtil.printBitmap(bitmap1, 2));
                }else{
                    BluetoothUtil.sendData(ESCUtil.printBitmap(bitmap1, 0));
                }
            }else if(mytype == 1){
                BluetoothUtil.sendData(ESCUtil.selectBitmap(bitmap1, 0));
            }else if(mytype == 2){
                BluetoothUtil.sendData(ESCUtil.selectBitmap(bitmap1, 1));
            }else if(mytype == 3){
                BluetoothUtil.sendData(ESCUtil.selectBitmap(bitmap1, 32));
            }else if(mytype == 4){
                BluetoothUtil.sendData(ESCUtil.selectBitmap(bitmap1, 33));
            }

            BluetoothUtil.sendData(ESCUtil.nextLine(3));
        }
    }
}
