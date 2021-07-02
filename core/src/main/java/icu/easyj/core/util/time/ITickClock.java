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

/**
 * 记号时钟接口
 *
 * @author wangliang181230
 */
public interface ITickClock extends IClock {

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
}
