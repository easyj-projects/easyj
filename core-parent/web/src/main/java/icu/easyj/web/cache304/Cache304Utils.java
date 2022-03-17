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
package icu.easyj.web.cache304;

import java.util.Date;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import icu.easyj.core.util.DateUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.core.util.ThrowableUtils;
import icu.easyj.web.cache304.config.Cache304Config;
import icu.easyj.web.cache304.config.Cache304ConfigManagerFactory;
import icu.easyj.web.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

/**
 * Cache304工具类
 *
 * @author wangliang181230
 */
public abstract class Cache304Utils {

	private static final Logger LOGGER = LoggerFactory.getLogger(Cache304Utils.class);

	/**
	 * 执行Cache304
	 *
	 * @param request  请求实例
	 * @param response 响应实例
	 * @param config   缓存配置
	 * @param callback 回执函数
	 * @return result 回执函数执行结果
	 */
	@Nullable
	public static Object doCache(HttpServletRequest request, HttpServletResponse response, Cache304Config config, Supplier<Object> callback) {
		// 配置为空时，不执行304缓存逻辑
		if (config == null) {
			return callback.get();
		}

		// 判断客户端是否不想使用缓存（一般用于客户端强制刷新后刷新缓存数据）
		if (HttpUtils.isNoCacheRequest(request)) {
			// 用户不想使用缓存，执行业务并设置缓存响应头
			return doCallbackAndSetCache304Header(callback, response, config, -1);
		}

		// 缓存时间串
		String ifModifiedSince = request.getHeader(HttpHeaders.IF_MODIFIED_SINCE);
		if (StringUtils.isEmpty(ifModifiedSince)) {
			// 不存在缓存，执行业务并设置缓存响应头
			return doCallbackAndSetCache304Header(callback, response, config, -1);
		}
		// 转换缓存时间类型
		Date lastModified;
		try {
			lastModified = DateUtils.parseAll(ifModifiedSince);
		} catch (Exception ex) {
			LOGGER.warn("解析时间字符串失败，header[\"{}\"] = {}, error = {}",
					HttpHeaders.IF_MODIFIED_SINCE, ifModifiedSince, ex.getMessage(), ex);
			// 头信息有误，执行业务并设置缓存响应头
			return doCallbackAndSetCache304Header(callback, response, config, -1);
		}

		// 判断缓存是否已过期
		long passedTime = System.currentTimeMillis() - lastModified.getTime(); // 缓存已过时间，单位：毫秒
		long cacheTime = CacheTimeComputer.computeTime(lastModified, config); // 缓存有效时间
		if (passedTime < cacheTime) {
			// 存在缓存且未过期，设置304响应状态，并返回null
			HttpUtils.setResponseStatus304(response);
			return null;
		} else {
			// 缓存存在但已过期，执行业务并设置缓存响应头
			try {
				return doCallbackAndSetCache304Header(callback, response, config, cacheTime);
			} catch (Throwable t) {
				// 出现异常时，允许客户端继续使用已过期的缓存，响应304，不抛出异常
				if (config.isUseCacheIfException()) {
					// 记录异常日志
					if (LOGGER.isErrorEnabled()) {
						// 因为callback方法可能会抛出包装异常，所以需要拆包装后，再记录异常日志。
						Throwable th = ThrowableUtils.unwrap(t);
						LOGGER.error("当前请求出现异常，但允许客户端继续使用缓存，返回304响应状态！不抛出异常，仅记录异常日志：", th);
					}

					// 设置304响应状态，并返回null
					HttpUtils.setResponseStatus304(response);
					return null;
				} else {
					// 不允许继续使用已过期的缓存，抛出异常
					throw t;
				}
			}
		}
	}

	//region `doCache`重载方法

	public static Object doCache(HttpServletRequest request, HttpServletResponse response, Supplier<Object> callback) {
		Cache304Config config = Cache304ConfigManagerFactory.getInstance().getConfig(request);
		return doCache(request, response, config, callback);
	}

	public static void doCache(HttpServletRequest request, HttpServletResponse response, Cache304Config config, Runnable runnable) {
		doCache(request, response, config, () -> {
			runnable.run();
			return null;
		});
	}

	public static void doCache(HttpServletRequest request, HttpServletResponse response, Runnable runnable) {
		Cache304Config config = Cache304ConfigManagerFactory.getInstance().getConfig(request);
		doCache(request, response, config, runnable);
	}

	//endregion

	/**
	 * 执行业务并设置缓存响应头
	 *
	 * @param callback  业务函数
	 * @param response  响应实例
	 * @param config    缓存配置
	 * @param cacheTime 缓存有效时间（大于0表示已计算过）
	 * @return result 业务返回的结果
	 */
	private static Object doCallbackAndSetCache304Header(Supplier<Object> callback, HttpServletResponse response,
														 Cache304Config config, long cacheTime) {
		// 缓存秒数
		long cacheSeconds;
		if (cacheTime > 0) {
			// 已经计算过，无需再重复计算，直接除以1000就是缓存有效秒数
			cacheSeconds = cacheTime / 1000;
		} else {
			cacheSeconds = CacheTimeComputer.computeTime(new Date(), config) / 1000;
		}

		// 先执行业务代码
		long startTime = System.nanoTime();
		Object result = callback.get();

		// 如果
		if (config.getCacheDays() > 0) {
			long runSeconds = (System.nanoTime() - startTime) / 1000000000;
			if (runSeconds > 0) {
				cacheSeconds -= runSeconds;
				if (cacheSeconds <= 0) {
					return result;
				}
			}
		}

		Date now = new Date();

		// 业务代码未出现异常，设置304缓存所需的响应头
		response.addDateHeader(HttpHeaders.LAST_MODIFIED, now.getTime());

		// 设置cache响应头
		if (config.isUseMaxAge()) {
			// 如果缓存秒数超过`limitMaxAge`的值，则重置为`limitMaxAge`的值。（这样处理的原因见注解`@Cache304`）
			if (config.getLimitMaxAge() > 0 && cacheSeconds > config.getLimitMaxAge()) {
				cacheSeconds = config.getLimitMaxAge();
			}

			response.setDateHeader(HttpHeaders.EXPIRES, new Date(now.getTime() + cacheSeconds * 1000).getTime());
			cacheSeconds += 1; // 加1秒，防止客户端已过期，但服务端验证未过期
			response.addHeader(HttpHeaders.CACHE_CONTROL, "max-age=" + cacheSeconds);
		}

		// 返回业务结果
		return result;
	}
}
