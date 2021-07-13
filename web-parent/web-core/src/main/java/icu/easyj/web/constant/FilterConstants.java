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
package icu.easyj.web.constant;

/**
 * 过滤器相关常量
 *
 * @author wangliang181230
 */
public interface FilterConstants {

	/**
	 * exclusions键值
	 */
	String EXCLUSIONS_PARAMETER_NAME = "exclusions";

	/**
	 * 全局过滤器需要排除的请求地址
	 */
	String GLOBAL_EXCLUSIONS = "GET:/actuator*" // 健康诊断相关的请求，不过滤
			+ ",GET:/swagger*,GET:/v2/api-docs,GET:/csrf" // swagger相关的请求，不过滤
			+ ",GET:/" // 首页，不过滤
			+ ",*:/error"; // error页，不过滤
}
