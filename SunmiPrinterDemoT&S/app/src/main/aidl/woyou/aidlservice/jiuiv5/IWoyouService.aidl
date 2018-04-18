/**
* JIUI T1 打印服务
* AIDL Version: 2.1
*/

package woyou.aidlservice.jiuiv5;

import woyou.aidlservice.jiuiv5.ICallback;
import android.graphics.Bitmap;
import woyou.aidlservice.jiuiv5.ITax;

interface IWoyouService
{
	/**
	* 打印机固件升级(只供系统组件调用,开发者调用无效)
	* @param buffer
	* @param size
	* @param filename
	* @param iapInterface
	*/
	void updateFirmware();

	/**
	* 打印机固件状态
	* return:   0--未知， A5--bootloader, C3--print
	*/
	int getFirmwareStatus();

	/**
	* 取WoyouService服务版本
	*/
	String getServiceVersion();

	/**
	 * 初始化打印机，重置打印机的逻辑程序，但不清空缓存区数据，因此
	 * 未完成的打印作业将在重置后继续
	 * @param callback 回调
	 * @return
	 */
	void printerInit(in ICallback callback);

	/**
	* 打印机自检，打印机会打印自检页
	* @param callback 回调
	*/
	void printerSelfChecking(in ICallback callback);

	/**
	* 获取打印机板序列号
	*/
	String getPrinterSerialNo();

	/**
	* 获取打印机固件版本号
	*/
	String getPrinterVersion();

	/**
	* 获取打印机型号
	*/
	String getPrinterModal();

	/**
	* 获取打印头打印长度
	*/
	int getPrintedLength();

	/**
	 * 打印机走纸(强制换行，结束之前的打印内容后走纸n行)
	 * @param n:	走纸行数
	 * @param callback  结果回调
	 * @return
	 */
	void lineWrap(int n, in ICallback callback);

	/**
	* 使用原始指令打印
	* @param data	        指令
	* @param callback  结果回调
	*/
	void sendRAWData(in byte[] data, in ICallback callback);

	/**
	* 设置对齐模式，对之后打印有影响，除非初始化
	* @param alignment:	对齐方式 0--居左 , 1--居中, 2--居右
	* @param callback  结果回调
	*/
	void setAlignment(int alignment, in ICallback callback);

	/**
	* 设置打印字体, 对之后打印有影响，除非初始化
	* (目前只支持一种字体"gh"，gh是一种等宽中文字体，之后会提供更多字体选择)
	* @param typeface:		字体名称
	*/
	void setFontName(String typeface, in ICallback callback);

	/**
	* 设置字体大小, 对之后打印有影响，除非初始化
	* 注意：字体大小是超出标准国际指令的打印方式，
	* 调整字体大小会影响字符宽度，每行字符数量也会随之改变，
	* 因此按等宽字体形成的排版可能会错乱
	* @param fontsize:	字体大小
	*/
	void setFontSize(float fontsize, in ICallback callback);

	/**
	* 打印文字，文字宽度满一行自动换行排版，不满一整行不打印除非强制换行
	* @param text:	要打印的文字字符串
	*/
	void printText(String text, in ICallback callback);

	/**
	* 打印指定字体的文本，字体设置只对本次有效
	* @param text:			要打印文字
	* @param typeface:		字体名称（目前只支持"gh"字体）
	* @param fontsize:		字体大小
	*/
	void printTextWithFont(String text, String typeface, float fontsize, in ICallback callback);

	/**
	* 打印表格的一行，可以指定列宽、对齐方式
	* @param colsTextArr   各列文本字符串数组
	* @param colsWidthArr  各列宽度数组(以英文字符计算, 每个中文字符占两个英文字符, 每个宽度大于0)
	* @param colsAlign	        各列对齐方式(0居左, 1居中, 2居右)
	* 备注: 三个参数的数组长度应该一致, 如果colsText[i]的宽度大于colsWidth[i], 则文本换行
	*/
	void printColumnsText(in String[] colsTextArr, in int[] colsWidthArr, in int[] colsAlign, in ICallback callback);


	/**
	* 打印图片
	* @param bitmap: 	图片bitmap对象(最大宽度384像素，超过无法打印并且回调callback异常函数)
	*/
	void printBitmap(in Bitmap bitmap, in ICallback callback);

	/**
	* 打印一维条码
	* @param data: 		条码数据
	* @param symbology: 	条码类型
	*    0 -- UPC-A，
	*    1 -- UPC-E，
	*    2 -- JAN13(EAN13)，
	*    3 -- JAN8(EAN8)，
	*    4 -- CODE39，
	*    5 -- ITF，
	*    6 -- CODABAR，
	*    7 -- CODE93，
	*    8 -- CODE128
	* @param height: 		条码高度, 取值1到255, 默认162
	* @param width: 		条码宽度, 取值2至6, 默认2
	* @param textposition:	文字位置 0--不打印文字, 1--文字在条码上方, 2--文字在条码下方, 3--条码上下方均打印
	*/
	void printBarCode(String data, int symbology, int height, int width, int textposition,  in ICallback callback);

	/**
	* 打印二维条码
	* @param data:			二维码数据
	* @param modulesize:	二维码块大小(单位:点, 取值 1 至 16 )
	* @param errorlevel:	二维码纠错等级(0 至 3)，
	*                0 -- 纠错级别L ( 7%)，
	*                1 -- 纠错级别M (15%)，
	*                2 -- 纠错级别Q (25%)，
	*                3 -- 纠错级别H (30%)
	*/
	void printQRCode(String data, int modulesize, int errorlevel, in ICallback callback);

	/**
	* 打印文字，文字宽度满一行自动换行排版，不满一整行不打印除非强制换行
	* 文字按矢量文字宽度原样输出，即每个字符不等宽
	* @param text:	要打印的文字字符串
	* Ver 1.7.6中增加
	*/
	void printOriginalText(String text, in ICallback callback);

	/**
	* 打印缓冲区内容
	*/
	void commitPrinterBuffer();

	/**
	*切纸
	*/
	void cutPaper(in ICallback callback);

	/**
	* 获取切刀次数
	*/
	int getCutPaperTimes();

	/**
	* 打开钱柜
	*/
	void openDrawer(in ICallback callback);

	/**
	* 取钱柜累计打开次数
	*/
	int getOpenDrawerTimes();

	/**
	* 进入缓冲模式，所有打印调用将缓存，调用commitPrinterBuffe()后打印
	*
	* @param clean: 是否清除缓冲区内容
	*
	*/
	void enterPrinterBuffer(in boolean clean);

	/**
	* 退出缓冲模式
	*
	* @param commit: 是否打印出缓冲区内容
	*
	*/
	void exitPrinterBuffer(in boolean commit);

	void tax(in byte [] data,in ITax callback);

	//获取当前打印机模式：0 普通模式 1黑标模式
	int getPrinterMode();

	//获取黑标模式打印机自动走纸距离
	int getPrinterBBMDistance();

	/**
	* 打印表格的一行，可以指定列宽、对齐方式
	* @param colsTextArr   各列文本字符串数组
	* @param colsWidthArr  各列宽度权重即各列所占比例
	* @param colsAlign	        各列对齐方式(0居左, 1居中, 2居右)
	* 备注: 三个参数的数组长度应该一致, 如果colsText[i]的宽度大于colsWidth[i], 则文本换行
	*/
	void printColumnsString(in String[] colsTextArr, in int[] colsWidthArr, in int[] colsAlign, in ICallback callback);

	/**
	* 获取打印机的最新状态
	* 返回值：1 打印机正常 2、打印机更新状态 3 获取状态异常  4 缺纸 5 过热  6 开盖 7切刀异常 8切刀恢复 505未检测到打印机
	**/
	int updatePrinterState();


	/**
	* 带反馈打印缓冲区内容
	*
	* @param callback: 反馈
	*
	*/
	void commitPrinterBufferWithCallback(in ICallback callback);

	/**
	* 带反馈退出缓冲打印模式
	*
	* @param commit： 是否提交缓冲区内容
	* @param callback: 反馈
	*
	*/
	void exitPrinterBufferWithCallback(in boolean commit, in ICallback callback);

    /**
    * 打印图片
    * @param bitmap: 	图片bitmap对象(最大宽度384像素，图片超过1M无法打印)
    * @param type:      目前有两种打印方式：0、同printBitmap 1、阈值200的黑白化图片 2、灰度图片
    */
    void printBitmapCustom(in Bitmap bitmap, in int type, in ICallback callback);
}