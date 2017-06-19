package woyou.aidlservice.jiuiv5;

/**
 * 打印服务执行结果的回调
 */
interface ICallback {

	/**
	* 返回执行结果
	* @param isSuccess:	  true执行成功，false 执行失败
	*/
	oneway void onRunResult(boolean isSuccess, int code, String msg);

}