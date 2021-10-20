/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package icu.easyj.test.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 简单的性能测试工具
 *
 * @author wangliang181230
 */
public abstract class TestUtils {

	//region 性能测试

	//region Private

	/**
	 * 运行单个函数的性能测试
	 *
	 * @param threadCount 并行执行的线程数
	 * @param times       每个线程运行次数
	 * @param supplier    需运行的函数，返回值表示函数别名
	 * @return 运行耗时，单位：毫秒
	 */
	private static long executeOnePerformanceTest(final int threadCount, final int times, Supplier<?> supplier) {
		long startTime = System.nanoTime();
		AtomicInteger atomicInteger = new AtomicInteger(threadCount);
		String supplierName = supplier.get().toString();

		final Thread t = Thread.currentThread();

		// 先创建所有线程
		Thread[] threads = new Thread[threadCount];
		for (int i = 0; i < threadCount; i++) {
			threads[i] = new Thread(() -> {
				for (int j = 0; j < times; j++) {
					supplier.get();
				}
				if (atomicInteger.decrementAndGet() == 0) {
					// 恢复父线程
					t.resume();
				}
			});
		}

		// 运行所有线程
		for (Thread thread : threads) {
			thread.start();
		}

		// 挂起父线程
		t.suspend();

		// 计算耗时
		long cost = getCost(startTime);
		String costStr = String.valueOf(cost);

		// 打印日志
		System.out.println("| 函数名：" + StringUtils.rightPad(supplierName, 16 + supplierName.length() - InnerStringUtils.chineseLength(supplierName), ' ')
				+ "耗时：" + StringUtils.leftPad(costStr, 7, ' ') + " ms          |");
		return cost;
	}

	//endregion Private end


	/**
	 * 函数性能测试
	 *
	 * @param threadCount 并行执行的线程数
	 * @param times       每个线程运行次数
	 * @param suppliers   需比较性能的函数集，每个函数可以设置一个返回值，表示函数别名，会打印在控制台中
	 * @return costs 每个函数的总耗时
	 */
	@NonNull
	public static long[] performanceTest(int threadCount, int times, Supplier<?>... suppliers) {
		Assert.isTrue(threadCount > 0, "'sets' must be greater than 0");
		Assert.isTrue(times > 0, "'times' must be greater than 0");
		Assert.isTrue(suppliers != null && suppliers.length > 0, "'suppliers' must be not empty");

		// 先预热一下
		int i = times * 2;
		while (i-- > 0) {
			for (Supplier<?> supplier : suppliers) {
				supplier.get();
			}
		}

		// 正式开始运行
		System.out.println("--------------------------------------------------");
		System.out.println("| 开始性能测试：" + StringUtils.rightPad(threadCount + " * " + times, 36, ' ') + "|");
		System.out.println("--------------------------------------------------");
		long[] costs = new long[suppliers.length];
		for (int y = 0; y < suppliers.length; ++y) {
			costs[y] += executeOnePerformanceTest(threadCount, times, suppliers[y]);
		}
		System.out.println("--------------------------------------------------");
		return costs;
	}

	//endregion


	/**
	 * 获取耗时，单位：毫秒
	 *
	 * @param startNanoTime 起始纳秒数
	 * @return 耗时，单位：毫秒
	 */
	public static long getCost(long startNanoTime) {
		return (System.nanoTime() - startNanoTime) / 1000000;
	}
}
