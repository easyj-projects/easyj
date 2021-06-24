package org.easyframework.core.function;

/**
 * 函数式接口：Runnable
 *
 * @author wangliang181230
 */
@FunctionalInterface
public interface Runnable {

	/**
	 * 执行函数
	 *
	 * @throws Throwable 抛出任意异常
	 */
	void run() throws Throwable;
}
