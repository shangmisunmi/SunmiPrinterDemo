package com.sunmi.printerhelper.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

//常用指令封装
public class ESCUtil {

	public static final byte ESC = 0x1B;// Escape
	public static final byte FS =  0x1C;// Text delimiter
	public static final byte GS =  0x1D;// Group separator
	public static final byte DLE = 0x10;// data link escape
	public static final byte EOT = 0x04;// End of transmission
	public static final byte ENQ = 0x05;// Enquiry character
	public static final byte SP =  0x20;// Spaces
	public static final byte HT =  0x09;// Horizontal list
	public static final byte LF =  0x0A;//Print and wrap (horizontal orientation)
	public static final byte CR =  0x0D;// Home key
	public static final byte FF =  0x0C;// Carriage control (print and return to the standard mode (in page mode))
	public static final byte CAN = 0x18;// Canceled (cancel print data in page mode)

	//初始化打印机
	public static byte[] init_printer() {
		byte[] result = new byte[2];
		result[0] = ESC;
		result[1] = 0x40;
		return result;
	}

	//打印浓度指令
	public static byte[] setPrinterDarkness(int value){
		byte[] result = new byte[9];
		result[0] = GS;
		result[1] = 40;
		result[2] = 69;
		result[3] = 4;
		result[4] = 0;
		result[5] = 5;
		result[6] = 5;
		result[7] = (byte) (value >> 8);
		result[8] = (byte) value;
		return result;
	}

	/**
	 * 打印单个二维码 sunmi自定义指令
	 * @param code:			二维码数据
	 * @param modulesize:	二维码块大小(单位:点, 取值 1 至 16 )
	 * @param errorlevel:	二维码纠错等级(0 至 3)
	 *                0 -- 纠错级别L ( 7%)
	 *                1 -- 纠错级别M (15%)
	 *                2 -- 纠错级别Q (25%)
	 *                3 -- 纠错级别H (30%)
	 */
	public static byte[] getPrintQRCode(String code, int modulesize, int errorlevel){
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try{
			buffer.write(setQRCodeSize(modulesize));
			buffer.write(setQRCodeErrorLevel(errorlevel));
			buffer.write(getQCodeBytes(code));
			buffer.write(getBytesForPrintQRCode());
		}catch(Exception e){
			e.printStackTrace();
		}
		return buffer.toByteArray();
	}

	/**
	 * 使用光栅位图打印两个二维码
	 * 将多个二维码转换为光栅位图打印
	 */
	public static byte[] getPrintDoubleQRCode(String qr1, String qr2, int size){
		byte[] bytes1  = new byte[4];
		bytes1[0] = GS;
		bytes1[1] = 0x76;
		bytes1[2] = 0x30;
		bytes1[3] = 0x00;

		byte[] bytes2 = BytesUtil.getZXingQRCode(qr1, qr2, size);
		return BytesUtil.byteMerger(bytes1, bytes2);
	}

	/**
	 * 打印一维条形码
	 */
	public static byte[] getPrintBarCode(String data, int symbology, int height, int width, int textposition){
		if(symbology < 0 || symbology > 10){
			return new byte[]{LF};
		}

		if(width < 2 || width > 6){
			width = 2;
		}

		if(textposition <0 || textposition > 3){
			textposition = 0;
		}

		if(height < 1 || height>255){
			height = 162;
		}

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try{
			buffer.write(new byte[]{0x1D,0x66,0x01,0x1D,0x48,(byte)textposition,
					0x1D,0x77,(byte)width,0x1D,0x68,(byte)height,0x0A});

			byte[] barcode;
			if(symbology == 10){
				barcode = BytesUtil.getBytesFromDecString(data);
			}else{
				barcode = data.getBytes("GB18030");
			}


			if(symbology > 7){
				buffer.write(new byte[]{0x1D,0x6B,0x49,(byte)(barcode.length+2),0x7B, (byte) (0x41+symbology-8)});
			}else{
				buffer.write(new byte[]{0x1D,0x6B,(byte)(symbology + 0x41),(byte)barcode.length});
			}
			buffer.write(barcode);
		}catch(Exception e){
			e.printStackTrace();
		}
		return buffer.toByteArray();
	}

	//光栅位图打印
	public static byte[] printBitmap(Bitmap bitmap){
		byte[] bytes1  = new byte[4];
		bytes1[0] = GS;
		bytes1[1] = 0x76;
		bytes1[2] = 0x30;
		bytes1[3] = 0x00;

		byte[] bytes2 = BytesUtil.getBytesFromBitMap(bitmap);
		return BytesUtil.byteMerger(bytes1, bytes2);
	}

	//光栅位图打印 设置mode
	public static byte[] printBitmap(Bitmap bitmap, int mode){
		byte[] bytes1  = new byte[4];
		bytes1[0] = GS;
		bytes1[1] = 0x76;
		bytes1[2] = 0x30;
		bytes1[3] = (byte) mode;

		byte[] bytes2 = BytesUtil.getBytesFromBitMap(bitmap);
		return BytesUtil.byteMerger(bytes1, bytes2);
	}

	//光栅位图打印
	public static byte[] printBitmap(byte[] bytes){
		byte[] bytes1  = new byte[4];
		bytes1[0] = GS;
		bytes1[1] = 0x76;
		bytes1[2] = 0x30;
		bytes1[3] = 0x00;

		return BytesUtil.byteMerger(bytes1, bytes);
	}

	/*
	*	选择位图指令 设置mode
	*	需要设置1B 33 00将行间距设为0
	 */
	public static byte[] selectBitmap(Bitmap bitmap, int mode){
		return BytesUtil.byteMerger(BytesUtil.byteMerger(new byte[]{0x1B, 0x33, 0x00}, BytesUtil.getBytesFromBitMap(bitmap, mode)), new byte[]{0x0A, 0x1B, 0x32});
	}

	/**
	 * 跳指定行数
	 */
    public static byte[] nextLine(int lineNum) {
        byte[] result = new byte[lineNum];
        for (int i = 0; i < lineNum; i++) {
            result[i] = LF;
        }

        return result;
    }

    // ------------------------style set-----------------------------
	//设置默认行间距
	public static byte[] setDefaultLineSpace(){
		return new byte[]{0x1B, 0x32};
	}

	//设置行间距
	public static byte[] setLineSpace(int height){
    	return new byte[]{0x1B, 0x33, (byte) height};
	}

	// ------------------------underline-----------------------------
	//设置下划线1点
	public static byte[] underlineWithOneDotWidthOn() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 45;
		result[2] = 1;
		return result;
	}

	//设置下划线2点
	public static byte[] underlineWithTwoDotWidthOn() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 45;
		result[2] = 2;
		return result;
	}

	//取消下划线
	public static byte[] underlineOff() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 45;
		result[2] = 0;
		return result;
	}

	// ------------------------bold-----------------------------
	/**
	 * 字体加粗
	 */
	public static byte[] boldOn() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 69;
		result[2] = 0xF;
		return result;
	}

	/**
	 * 取消字体加粗
	 */
	public static byte[] boldOff() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 69;
		result[2] = 0;
		return result;
	}

	// ------------------------character-----------------------------
	/*
	*单字节模式开启
	 */
	public static byte[] singleByte(){
		byte[] result = new byte[2];
		result[0] = FS;
		result[1] = 0x2E;
		return result;
	}

	/*
	*单字节模式关闭
 	*/
	public static byte[] singleByteOff(){
		byte[] result = new byte[2];
		result[0] = FS;
		result[1] = 0x26;
		return result;
	}

	/**
	 * 设置单字节字符集
	 */
	public static byte[] setCodeSystemSingle(byte charset){
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 0x74;
		result[2] = charset;
		return result;
	}

	/**
	 * 设置多字节字符集
	 */
	public static byte[] setCodeSystem(byte charset){
		byte[] result = new byte[3];
		result[0] = FS;
		result[1] = 0x43;
		result[2] = charset;
		return result;
	}

	// ------------------------Align-----------------------------

	/**
	 * 居左
	 */
	public static byte[] alignLeft() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 97;
		result[2] = 0;
		return result;
	}

	/**
	 * 居中对齐
	 */
	public static byte[] alignCenter() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 97;
		result[2] = 1;
		return result;
	}

	/**
	 * 居右
	 */
	public static byte[] alignRight() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 97;
		result[2] = 2;
		return result;
	}

	//切刀
	public static byte[] cutter() {
		byte[] data = new byte[]{0x1d, 0x56, 0x01};
		return data;
	}

	/**
	 * 	标签or黑标模式定位
	 * 	由于兼容性原因只有手持机可以支持SDK,其他设备必须使用指令方法
	 * 	For compatibility reasons, only the handheld can support the SDK, other devices must use the command method
	 */
	public static byte[] labellocate() {
		return new byte[]{0x1C, 0x28, 0x4C, 0x02, 0x00, 0x43, 0x31};
	}

	/**
	 * 标签or黑标模式送出
	 * 由于兼容性原因只有手持机可以支持SDK,其他设备必须使用指令方法
	 * For compatibility reasons, only the handheld can support the SDK, other devices must use the command method
	 */
	public static byte[] labelout(){
		return new byte[]{0x1C, 0x28, 0x4C, 0x02, 0x00, 0x42, 0x31};
	}



	////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////          private                /////////////////////////
	////////////////////////////////////////////////////////////////////////////////////
	private static byte[] setQRCodeSize(int modulesize){
		//二维码块大小设置指令
		byte[] dtmp = new byte[8];
		dtmp[0] = GS;
		dtmp[1] = 0x28;
		dtmp[2] = 0x6B;
		dtmp[3] = 0x03;
		dtmp[4] = 0x00;
		dtmp[5] = 0x31;
		dtmp[6] = 0x43;
		dtmp[7] = (byte)modulesize;
		return dtmp;
	}

	private static byte[] setQRCodeErrorLevel(int errorlevel){
		//二维码纠错等级设置指令
		byte[] dtmp = new byte[8];
		dtmp[0] = GS;
		dtmp[1] = 0x28;
		dtmp[2] = 0x6B;
		dtmp[3] = 0x03;
		dtmp[4] = 0x00;
		dtmp[5] = 0x31;
		dtmp[6] = 0x45;
		dtmp[7] = (byte)(48+errorlevel);
		return dtmp;
	}

	//打印已存入数据的二维码
	private static byte[] getBytesForPrintQRCode(){
		byte[] dtmp = new byte[9];
		dtmp[0] = 0x1D;
		dtmp[1] = 0x28;
		dtmp[2] = 0x6B;
		dtmp[3] = 0x03;
		dtmp[4] = 0x00;
		dtmp[5] = 0x31;
		dtmp[6] = 0x51;
		dtmp[7] = 0x30;
		dtmp[8] = 0x0A;
		return dtmp;
	}

	private static byte[] getQCodeBytes(String code) {
		//二维码存入指令
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			byte[] d = code.getBytes("GB18030");
			int len = d.length + 3;
			if (len > 7092) len = 7092;
			buffer.write((byte) 0x1D);
			buffer.write((byte) 0x28);
			buffer.write((byte) 0x6B);
			buffer.write((byte) len);
			buffer.write((byte) (len >> 8));
			buffer.write((byte) 0x31);
			buffer.write((byte) 0x50);
			buffer.write((byte) 0x30);
			for (int i = 0; i < d.length && i < len; i++) {
				buffer.write(d[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toByteArray();
	}
}