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

import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.function.Supplier;

import icu.easyj.test.exception.TestException;
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
	 * @param function    需运行的函数，返回值表示函数别名
	 * @return 运行耗时，单位：毫秒
	 */
	private static long executeOnePerformanceTest(final int threadCount, final int times, Function<TestParam, ?> function) {
		long startTime = getStartTime();
		String supplierName = function.apply(new TestParam(0, 0, true)).toString();

		// 多线程并发测试时
		if (threadCount > 1) {
			CountDownLatch countDownLatch = new CountDownLatch(threadCount);

			// 先创建所有线程
			Thread[] threads = new Thread[threadCount];
			for (int i = 0; i < threadCount; i++) {
				final int threadNo = i + 1;
				threads[i] = new Thread(() -> {
					TestParam param = new TestParam(threadNo, 0, false);
					for (int j = 1; j <= times; j++) {
						function.apply(param.setRunNo(j));
					}
					countDownLatch.countDown();
				});
				threads[i].setName("TestThread_" + (i + 1));
			}

			// 运行所有线程
			for (Thread thread : threads) {
				thread.start();
			}

			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				throw new TestException("测试线程被中断", e);
			}
		} else {
			// 单线程测试
			TestParam param = new TestParam(1, 0, false);
			for (int i = 1; i <= times; i++) {
				function.apply(param.setRunNo(i));
			}
		}

		// 计算总耗时
		long endTime = System.nanoTime();
		long cost = (endTime - startTime) / 1000000;
		String costStr = String.valueOf(cost);
		// 计算单次耗时
		long onceCost = (System.nanoTime() - startTime) / times;
		String onceCostStr = (onceCost) + " ns";

		// 打印日志
		System.out.println("| 函数名：" + StringUtils.rightPad(supplierName, 16 + supplierName.length() - InnerStringUtils.chineseLength(supplierName))
				+ "耗时：" + StringUtils.leftPad(costStr, 7) + " ms         "
				+ "单次：" + StringUtils.leftPad(onceCostStr, 7 + 6) + " |");
		return cost;
	}

	//endregion Private end


	/**
	 * 函数性能测试
	 *
	 * @param threadCount 并行执行的线程数
	 * @param times       每个线程运行次数
	 * @param functions   需比较性能的函数集，每个函数可以设置一个返回值，表示函数别名，会打印在控制台中
	 * @return costs 每个函数的总耗时
	 */
	@NonNull
	public static long[] performanceTest(int threadCount, int times, Function<TestParam, ?>... functions) {
		Assert.isTrue(threadCount > 0, "'sets' must be greater than 0");
		Assert.isTrue(times > 0, "'times' must be greater than 0");
		Assert.isTrue(functions != null && functions.length > 0, "'functions' must be not empty");

		// 先预热一下
		System.out.println("\r\n性能测试预热中...");
		long startTime = getStartTime();
		for (Function<TestParam, ?> function : functions) {
			long startTime1 = getStartTime();
			TestParam param = new TestParam(0, 0, true);
			for (int i = 1; i <= times; ++i) {
				function.apply(param.setRunNo(i));
			}
			System.out.println(getCost(startTime1));
		}
		System.out.println("性能测试预热完成: " + getCost(startTime) + " ms\r\n");

		// 正式开始运行
		System.out.println("--------------------------------------------------------------------");
		System.out.println("| 开始性能测试：" + StringUtils.rightPad(threadCount + " * " + times, 54) + "|");
		System.out.println("--------------------------------------------------------------------");
		long[] costs = new long[functions.length];
		for (int y = 0; y < functions.length; ++y) {
			costs[y] += executeOnePerformanceTest(threadCount, times, functions[y]);
		}
		System.out.println("--------------------------------------------------------------------");
		return costs;
	}

	// 重载方法
	@NonNull
	public static long[] performanceTest(int threadCount, int times, Supplier<?>... suppliers) {
		Assert.isTrue(suppliers != null && suppliers.length > 0, "'suppliers' must be not empty");

		Function<TestParam, ?>[] functions = new Function[suppliers.length];
		for (int i = 0; i < suppliers.length; ++i) {
			final int n = i;
			functions[i] = t -> suppliers[n].get();
		}

		return performanceTest(threadCount, times, functions);
	}

	//endregion


	/**
	 * 获取测试起始纳秒时间
	 *
	 * @return 起始纳秒时间
	 */
	public static long getStartTime() {
		return System.nanoTime();
	}

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
