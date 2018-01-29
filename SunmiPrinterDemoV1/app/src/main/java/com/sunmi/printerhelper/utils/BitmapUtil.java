package com.sunmi.printerhelper.utils;

import android.graphics.Bitmap;
import android.util.Xml;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.multi.qrcode.detector.MultiDetector;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/3.
 */

public class BitmapUtil {

    /**
     * 生成条码bitmap
     * @param content
     * @param format
     * @param width
     * @param height
     * @return
     */
    public static Bitmap generateBitmap(String content, int format, int width, int height) {
        if(content == null || content.equals(""))
            return null;
        BarcodeFormat barcodeFormat;
        switch (format){
            case 0:
                barcodeFormat = BarcodeFormat.UPC_A;
                break;
            case 1:
                barcodeFormat = BarcodeFormat.UPC_E;
                break;
            case 2:
                barcodeFormat = BarcodeFormat.EAN_13;
                break;
            case 3:
                barcodeFormat = BarcodeFormat.EAN_8;
                break;
            case 4:
                barcodeFormat = BarcodeFormat.CODE_39;
                break;
            case 5:
                barcodeFormat = BarcodeFormat.ITF;
                break;
            case 6:
                barcodeFormat = BarcodeFormat.CODABAR;
                break;
            case 7:
                barcodeFormat = BarcodeFormat.CODE_93;
                break;
            case 8:
                barcodeFormat = BarcodeFormat.CODE_128;
                break;
            case 9:
                barcodeFormat = BarcodeFormat.QR_CODE;
                break;
            default:
                barcodeFormat = BarcodeFormat.QR_CODE;
                height = width;
                break;
        }
        MultiFormatWriter qrCodeWriter = new MultiFormatWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "GBK");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        try {
            BitMatrix encode = qrCodeWriter.encode(content, barcodeFormat, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        return null;
    }
}
