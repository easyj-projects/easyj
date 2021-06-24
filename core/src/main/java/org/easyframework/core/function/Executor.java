package org.easyframework.core.function;

/**
 * 函数式接口：Executor
 *
 * @param <R> 返回数据的类型
 * @author wangliang181230
 */
@FunctionalInterface
public interface Executor<R> {

	/**
	 * 执行函数
	 *
	 * @return result 返回执行结果
	 * @throws Throwable 抛出任意异常
	 */
	R execute() throws Throwable;
}
