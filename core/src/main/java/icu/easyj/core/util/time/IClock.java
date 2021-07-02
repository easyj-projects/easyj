/*
 *  Copyright 2021 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package icu.easyj.core.util.time;

import java.util.Date;

/**
 * 时钟接口
 *
 * @author wangliang181230
 */
public interface IClock {

	/**
	 * 当前时间
	 *
	 * @return now 当前时间
	 */
	default Date now() {
		return new Date(currentTimeMillis());
	}

	/**
	 * 当前毫秒数
	 *
	 * @return timeMillis 毫秒数
	 */
	default long currentTimeMillis() {
		return currentTimeNanos() / 1000000;
	}

	/**
	 * 当前微秒数
	 *
	 * @return timeMicros 微秒数
	 */
	default long currentTimeMicros() {
		return currentTimeNanos() / 1000;
	}

	/**
	 * 当前纳秒数
	 * 注意：值格式与`System.nanoTime()`并不相同
	 *
	 * @return timeNanos 纳秒数
	 */
	long currentTimeNanos();
}
