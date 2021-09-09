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
 * 各Filter的Order值
 * 值越大越晚执行
 *
 * @author wangliang181230
 */
public interface FilterOrderConstants {

	/**
	 * SpringMVC将Request存入ThreadLocal的过滤器的Order值。
	 * 自定义的Filter如果需要用到框架中的HttpUtil获取信息的话，则需要比此Order值要大，
	 * 否则 `RequestContextHolder.getRequestAttributes().getRequest()` 为空导致获取不到请求信息。
	 */
	int SPRING_MVC_REQUEST_SET_TO_THREAD_LOCAL = -105;

	/**
	 * 内部请求判断过滤器
	 */
	int INTERNAL_REQUEST = -100;

	/**
	 * 请求方式过滤
	 */
	int REQUEST_TYPE = -95;

	/**
	 * 无效请求拦截
	 */
	int INVALID_REQUEST = -90;

	/**
	 * 链路跟踪相关
	 */
	int TRACE = -85;

	/**
	 * 授权账号过滤器
	 */
	int APP_ACCOUNT = -80;

	/**
	 * 参数加密解密
	 */
	int PARAM_ENCRYPT = -75;
}
