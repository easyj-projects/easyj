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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link TickClock} 测试类
 *
 * @author wangliang181230
 */
public class TickClockTest {

	@Test
	public void testCompareTo() {
		long currentTimeMicros = System.currentTimeMillis() * 1000;
		long nanoTime = System.nanoTime();

		TickClock tickClock = new TickClock(currentTimeMicros, nanoTime);
		Assertions.assertEquals(0, tickClock.compareTo(new TickClock(currentTimeMicros, nanoTime)));
		Assertions.assertEquals(1, tickClock.compareTo(new TickClock(currentTimeMicros - 1, nanoTime)));
		Assertions.assertEquals(-1, tickClock.compareTo(new TickClock(currentTimeMicros + 1, nanoTime)));
	}
}
