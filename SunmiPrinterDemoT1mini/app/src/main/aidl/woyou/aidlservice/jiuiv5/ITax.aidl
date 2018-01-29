package woyou.aidlservice.jiuiv5;

/**
 * 打印服务执行结果的回调
 */
interface ITax {

	oneway void onDataResult(in byte [] data);
	
}