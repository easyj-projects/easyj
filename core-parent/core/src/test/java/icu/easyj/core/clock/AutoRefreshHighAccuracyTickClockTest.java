/*
 * Copyright 2021-2022 the original author or authors.
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
package icu.easyj.core.clock;

import java.util.concurrent.atomic.AtomicInteger;

import icu.easyj.core.util.DateUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * {@link AutoRefreshHighAccuracyTickClock} 测试类
 *
 * @author wangliang181230
 */
public class AutoRefreshHighAccuracyTickClockTest {

	@Test
	public void test() throws Exception {
		int i = 100;
		while (i-- > 0) {
			final AtomicInteger atomicInteger = new AtomicInteger(0);

			AutoRefreshHighAccuracyTickClock tickClock = new AutoRefreshHighAccuracyTickClock("test", () -> {
				atomicInteger.addAndGet(1);
				return new TickClock(System.currentTimeMillis() * 1000);
			});

			Thread.sleep(100);

			{
				Assertions.assertTrue(tickClock.isAutoRefreshing(), "记号时钟自动刷新状态不是true");
				this.testInternal(tickClock);
				Assertions.assertEquals(11, atomicInteger.get());
			}

			//Thread.sleep(15000);
			Thread.sleep(100);

			{
				tickClock.stopAutoRefresh();
				Assertions.assertFalse(tickClock.isAutoRefreshing(), "记号时钟自动刷新状态不是false");
				this.testInternal(tickClock);
				//Assertions.assertEquals(21, atomicInteger.get());
			}

			//Thread.sleep(15000);
			Thread.sleep(100);

			{
				tickClock.startAutoRefresh();
				Assertions.assertTrue(tickClock.isAutoRefreshing(), "记号时钟自动刷新状态不是true");
				this.testInternal(tickClock);
				//Assertions.assertEquals(21, atomicInteger.get());
			}

			{
				tickClock.stopAutoRefresh();
				Assertions.assertFalse(tickClock.isAutoRefreshing(), "记号时钟自动刷新状态不是false");
			}

			// 销毁
			Assertions.assertFalse(tickClock.isDestroyed());
			tickClock.destroy();
			Assertions.assertTrue(tickClock.isDestroyed());

			System.out.println();
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println();
		}
	}

	private void testInternal(AutoRefreshHighAccuracyTickClock tickClock) {
		long diff = tickClock.currentTimeMillis() - System.currentTimeMillis();
		System.out.println("时钟偏差的值：" + diff);
		System.out.println("时钟当前时间：" + DateUtils.toMilliseconds(tickClock.now()));

		Assertions.assertTrue(diff >= -5, "时钟偏差值: " + diff + " ms < -5 ms"); // 时钟偏差值不能大于5ms
		Assertions.assertTrue(diff <= 1, "时钟偏差值: " + diff + " ms > 1 ms"); // 时钟偏差值不能大于1ms
	}
}
