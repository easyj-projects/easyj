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
package icu.easyj.core.util;

import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 简单的性能测试工具
 *
 * @author wangliang181230
 */
public abstract class PerformanceTestUtils {

	/**
	 * 运行函数性能测试
	 *
	 * @param sets      运行几组测试
	 * @param times     每组运行函数次数
	 * @param suppliers 需比较性能的函数集，每个函数可以设置一个返回值，表示函数别名，会打印在控制台中
	 * @return 每个函数的总耗时
	 */
	@SafeVarargs
	@NonNull
	public static long[] execute(int sets, int times, Supplier<?>... suppliers) {
		Assert.isTrue(sets > 0, "'sets' must be greater than 0");
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
		System.out.println("| 开始运行性能测试：                                 |");
		System.out.println("--------------------------------------------------");
		long[] costs = new long[suppliers.length];
		while (sets-- > 0) {
			for (int y = 0; y < suppliers.length; ++y) {
				costs[y] += executeOne(times, suppliers[y]);
			}
			System.out.println("--------------------------------------------------");
		}
		return costs;
	}

	/**
	 * 运行函数性能测试
	 *
	 * @param times     运行函数次数
	 * @param suppliers 需比较性能的函数集，每个函数可以设置一个返回值，表示函数别名，会打印在控制台中
	 * @return 每个函数的耗时
	 */
	@SafeVarargs
	@NonNull
	public static long[] execute(int times, Supplier<String>... suppliers) {
		return execute(1, times, suppliers);
	}

	/**
	 * 运行单个函数的性能测试
	 *
	 * @param times    运行次数
	 * @param supplier 需运行的函数，返回值表示函数别名
	 * @return 运行耗时，单位：毫秒
	 */
	private static long executeOne(int times, Supplier<?> supplier) {
		long startTime = System.nanoTime();
		while (times-- > 1) {
			supplier.get();
		}
		String supplierName = supplier.get().toString();
		long cost = getCost(startTime);
		String costStr = String.valueOf(cost);
		System.out.println("| 函数名：" + StringUtils.rightPad(supplierName, 16 + supplierName.length() - icu.easyj.core.util.StringUtils.chineseLength(supplierName), ' ')
				+ "耗时：" + StringUtils.leftPad(costStr, 7, ' ') + " ms          |");
		return cost;
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
