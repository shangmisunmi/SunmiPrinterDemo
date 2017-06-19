package woyou.aidlservice.jiuiv5;

/**
 * 打印服务执行结果的回调
 */
interface ICallback {

	/**
	* 返回执行结果
	* @param isSuccess:	  true执行成功，false 执行失败
	*/
	oneway void onRunResult(boolean isSuccess);
	
	/**
	* 返回结果(字符串数据)
	* @param result:	结果，打印机上电以来打印长度(单位mm)
	*/
	oneway void onReturnString(String result);
	
	/**
	* 执行发生异常
	* code：	异常代码
	* msg:	异常描述
	*/
	oneway void  onRaiseException(int code, String msg);
}