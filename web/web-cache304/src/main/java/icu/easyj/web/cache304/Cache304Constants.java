/*
 *  Copyright 2021 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package icu.easyj.web.cache304;

import icu.easyj.core.constant.DateConstants;

/**
 * Cache304相关常量
 *
 * @author wangliang181230
 */
public interface Cache304Constants {

	long DEFAULT_CACHE_SECONDS = -1L;

	int DEFAULT_CACHE_DAYS = -1;

	boolean DEFAULT_USE_MAX_AGE = true;

	long DEFAULT_LIMIT_MAX_AGE = DateConstants.HALF_DAY_SEC;

	/**
	 * 临界误差时间：10分钟
	 * 用于解决：以天来计算缓存秒数时，避免客户端与服务器端的时间差导致缓存多存了一天
	 */
	long CRITICAL_TIME = 10 * 60 * 1000L;
}
