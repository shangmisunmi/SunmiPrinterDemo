package woyou.aidlservice.jiuiv5;

/**
 * 顾显反馈结果
 */
interface ILcdCallback {

	/**
	* 返回执行结果
	* @param show:		  true 显示成功  false 显示失败
	*/
	oneway void onRunResult(boolean show);	
}