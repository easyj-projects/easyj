package org.easyframework.core.function;

/**
 * 函数式接口：Callback
 *
 * @param <R> 返回数据的类型
 * @author wangliang181230
 */
@FunctionalInterface
public interface Callback<R> {

	/**
	 * 执行回执
	 *
	 * @return result 返回执行结果
	 * @throws Throwable 抛出任意异常
	 */
	R execute() throws Throwable;
}
