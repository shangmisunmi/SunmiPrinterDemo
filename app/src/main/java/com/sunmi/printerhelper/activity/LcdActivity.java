package com.sunmi.printerhelper.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.SunmiPrintHelper;


/**
 * <pre>
 *      Used to send content to LCD screen when the device is T1mini or T2mini
 *      It only controlled by Api not ble
 *  </pre>
 *
 * @author kaltin
 * @since create at 2020-02-19
 */
public class LcdActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lcd);
        setMyTitle(R.string.lcd_title);
        setBack();

        //init lcd 、light up lcd and clear screen
        SunmiPrintHelper.getInstance().controlLcd(1);
        SunmiPrintHelper.getInstance().controlLcd(2);
        SunmiPrintHelper.getInstance().controlLcd(4);
    }


    public void text(View view) {
        SunmiPrintHelper.getInstance().sendTextToLcd();
    }

    public void texts(View view) {
        SunmiPrintHelper.getInstance().sendTextsToLcd();
    }

    public void pic(View view) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //No scaling
        options.inScaled = false;
        //Make the bitmap have the current device's dpi
        options.inDensity = getResources().getDisplayMetrics().densityDpi;

        SunmiPrintHelper.getInstance().sendPicToLcd(BitmapFactory.decodeResource(getResources(),
                R.drawable.mini, options));
    }
}
