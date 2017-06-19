package com.sunmi.printerhelper.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.AidlUtil;
import com.sunmi.printerhelper.utils.BluetoothUtil;
import com.sunmi.printerhelper.utils.ESCUtil;

import java.io.IOException;

/**
 * Created by Administrator on 2017/5/5.
 */

public class BitmapActivity extends BaseActivity {
    ImageView mImageView;
    Bitmap bitmap, bitmap1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
        setMyTitle(R.string.bitmap_title);
        setBack();

        mImageView = (ImageView) findViewById(R.id.bitmap_imageview);
        initView();

        AidlUtil.getInstance().initPrinter();
    }

    private void initView() {
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.raw.test);
        }

        if (bitmap1 == null) {
            bitmap1 = BitmapFactory.decodeResource(getResources(), R.raw.test1);
        }
        if (baseApp.isAidl()) {
            mImageView.setImageDrawable(new BitmapDrawable(bitmap));
        } else {
            mImageView.setImageDrawable(new BitmapDrawable(bitmap1));
        }

    }

    public void onClick(View view) {
        if (baseApp.isAidl()) {
            AidlUtil.getInstance().printBitmap(bitmap);
        } else {

            BluetoothUtil.sendData(ESCUtil.printBitmap(bitmap1));
            BluetoothUtil.sendData(ESCUtil.nextLine(3));

        }
    }
}
