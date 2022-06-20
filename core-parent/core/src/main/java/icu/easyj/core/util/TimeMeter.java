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
package icu.easyj.core.util;

/**
 * 计时器
 *
 * @author wangliang181230
 */
public class TimeMeter {
	private final long startNanoTime;


	public TimeMeter(long startNanoTime) {
		this.startNanoTime = startNanoTime;
	}


	public long spendNanoTime() {
		return System.nanoTime() - startNanoTime;
	}

	public long spendMicorsTime() {
		return spendNanoTime() / 1000;
	}

	public long spendMillisTime() {
		return spendNanoTime() / 1000000;
	}


	public long getStartNanoTime() {
		return startNanoTime;
	}


	public static TimeMeter create(long startNanoTime) {
		return new TimeMeter(startNanoTime);
	}

	public static TimeMeter create() {
		return create(System.nanoTime());
	}
}
