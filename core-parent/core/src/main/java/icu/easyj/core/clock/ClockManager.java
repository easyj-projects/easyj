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
package icu.easyj.core.clock;

import javax.security.auth.Destroyable;

/**
 * 时钟管理器
 *
 * @author wangliang181230
 */
public abstract class ClockManager {

	/**
	 * 销毁时钟
	 *
	 * @param clock 时钟
	 */
	public static void destroy(IClock clock) {
		if (clock instanceof Destroyable) {
			try {
				((IAutoRefreshTickClock)clock).destroy();
			} catch (Exception ignore) {
				// do nothing
			}
		}
	}
}
