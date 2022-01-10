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

/**
 * 记号时钟接口
 *
 * @author wangliang181230
 */
public interface ITickClock extends IClock, Comparable<ITickClock> {

	/**
	 * 获取基准微秒数
	 *
	 * @return baseEpochMicros 基准微秒数
	 */
	long getBaseEpochMicros();

	/**
	 * 获取基准记号纳秒数
	 *
	 * @return baseTickNanos 基准记号纳秒数
	 */
	long getBaseTickNanos();

	/**
	 * 获取已经过的纳秒数
	 * 说明：以baseTickNanos为基准，经过的纳秒数
	 *
	 * @return passedNanos 已经过的纳秒数
	 */
	default long getPassedNanos() {
		return System.nanoTime() - getBaseTickNanos();
	}

	/**
	 * 比较两个时钟，哪个时间更大一些
	 * <p>
	 * 注意：比较的不是基准微秒数哪个大，而是哪个记号时钟在同一时间生成的时间更大
	 *
	 * @param otherClock 其他时钟
	 * @return -1=otherClock大 0=一样大 1=当前时钟大
	 */
	@Override
	default int compareTo(ITickClock otherClock) {
		// 取当前时钟的 `基准记号纳秒数` 作为时间点，来计算两个时钟在这个时间点的纳秒数
		long nanoTime = getBaseTickNanos();

		long nanos = nanoTime - getBaseTickNanos() + getBaseEpochMicros() * 1000;
		long otherNanos = nanoTime - otherClock.getBaseTickNanos() + otherClock.getBaseEpochMicros() * 1000;

		return Long.compare(nanos, otherNanos);
	}
}
