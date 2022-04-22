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

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import icu.easyj.core.constant.AspectOrderConstants;
import icu.easyj.core.exception.SkipCallbackWrapperException;
import icu.easyj.core.util.ReflectionUtils;
import icu.easyj.web.cache304.annotation.Cache304;
import icu.easyj.web.cache304.annotation.Cache304AnnotationParser;
import icu.easyj.web.cache304.config.Cache304Config;
import icu.easyj.web.cache304.config.Cache304ConfigManagerFactory;
import icu.easyj.web.cache304.config.ICache304ConfigManager;
import icu.easyj.web.util.HttpUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

/**
 * Cache304切面类
 *
 * @author wangliang181230
 */
@Aspect
@Order(AspectOrderConstants.CACHE304)
public class Cache304Aspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(Cache304Aspect.class);

	//region 当前切面的启用状态

	private static final ThreadLocal<Boolean> DISABLED_LOCAL = new ThreadLocal<>();

	public static void disable() {
		DISABLED_LOCAL.set(true);
	}

	public static void enable() {
		DISABLED_LOCAL.remove();
	}

	//endregion


	@Pointcut("@annotation(icu.easyj.web.cache304.annotation.Cache304)")
	private void pointcutCache304() {
	}

	@Around("pointcutCache304()")
	public Object doCache304(ProceedingJoinPoint jp) throws Throwable {
		if (Boolean.TRUE.equals(DISABLED_LOCAL.get())) {
			return jp.proceed();
		}

		HttpServletRequest request = HttpUtils.getRequest();

		// 获取当前调用的controller方法
		MethodSignature ms = (MethodSignature)jp.getSignature();
		Method method = ms.getMethod();

		// 只有GET请求可使用 @Cache304
		if (HttpUtils.isNotGetRequest(request)) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("`@{}`不能用于非GET请求，建议将该注解从方法`{}`上移除掉。",
						Cache304.class.getSimpleName(), ReflectionUtils.methodToString(method));
			}
			return jp.proceed();
		}

		// 解析Cache304注解
		ICache304ConfigManager configManager = Cache304ConfigManagerFactory.getInstance();
		Cache304Config config = configManager.getConfig(request);
		if (config == null) {
			Cache304 anno = method.getAnnotation(Cache304.class);
			config = Cache304AnnotationParser.parse(anno);
			// 放入存储器，使`Cache304Filter`中 能够直接拦截下请求。
			configManager.putConfig(request, config);
		}

		// 执行304缓存方法
		try {
			HttpServletResponse response = HttpUtils.getResponse();
			return Cache304Utils.doCache(request, response, () -> {
				try {
					return jp.proceed();
				} catch (Throwable e) {
					throw new SkipCallbackWrapperException(e);
				}
			});
		} catch (SkipCallbackWrapperException e) {
			throw e.getCause();
		}
	}
}
