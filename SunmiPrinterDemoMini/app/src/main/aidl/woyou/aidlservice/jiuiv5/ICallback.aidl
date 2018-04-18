package woyou.aidlservice.jiuiv5;

/**
 * 打印服务执行结果的回调
 */
interface ICallback {

	/**
	* 返回接口执行的结果
	* 备注：此回调只表明接口执行是否成功但不表明打印机的工作结果，如需要获取打印机结果请用事务模式
	* @param isSuccess:	  true执行成功，false 执行失败
	*/
	oneway void onRunResult(boolean isSuccess);

	/**
	* 返回接口执行的结果(字符串数据)
	* @param result:	结果，打印机上电以来打印长度等(单位mm)
	*/
	oneway void onReturnString(String result);

	/**
	* 返回接口执行失败时发生异常情况的具体原因
	* code：	异常代码
	* msg:	异常描述
	*/
	oneway void  onRaiseException(int code, String msg);

	/**
	* 返回打印机结果
	* code：	异常代码 0 成功 1 失败
	* msg:	异常描述
	*/
	oneway void  onPrintResult(int code, String msg);
	
}