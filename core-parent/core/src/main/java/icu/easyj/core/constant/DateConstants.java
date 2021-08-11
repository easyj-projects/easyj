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
package icu.easyj.core.constant;

/**
 * Date相关常量
 *
 * @author wangliang181230
 */
public interface DateConstants {

	/**
	 * 一天的秒数
	 */
	long ONE_DAY_SEC = 24 * 60 * 60L;

	/**
	 * 半天的秒数
	 */
	long HALF_DAY_SEC = ONE_DAY_SEC / 2;

	/**
	 * 一天的毫秒数
	 */
	long ONE_DAY_MILL = ONE_DAY_SEC * 1000;

	/**
	 * 半天的毫秒数
	 */
	long HALF_DAY_MILL = ONE_DAY_MILL / 2;
}
