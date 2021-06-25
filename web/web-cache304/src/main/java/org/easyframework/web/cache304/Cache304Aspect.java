package org.easyframework.web.cache304;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.easyframework.core.exception.SkipCallbackWrapperException;
import org.easyframework.core.util.ReflectionUtils;
import org.easyframework.web.cache304.annotation.Cache304;
import org.easyframework.web.cache304.annotation.Cache304AnnotationParser;
import org.easyframework.web.cache304.config.Cache304Config;
import org.easyframework.web.cache304.config.Cache304ConfigStorageFactory;
import org.easyframework.web.cache304.config.ICache304ConfigStorage;
import org.easyframework.web.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Cache304切面类
 *
 * @author wangliang181230
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class Cache304Aspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(Cache304Aspect.class);

	@Pointcut("@annotation(org.easyframework.web.cache304.annotation.Cache304)")
	private void pointcutCache304() {
	}

	@Around("pointcutCache304()")
	public Object doCache304(ProceedingJoinPoint jp) throws Throwable {
		HttpServletRequest request = HttpUtils.getRequest();
		HttpServletResponse response = HttpUtils.getResponse();

		// 获取当前调用的controller方法
		MethodSignature ms = (MethodSignature)jp.getSignature();
		Method method = ms.getMethod(); // action方法

		// 只有GET请求可使用 @Cache304
		if (!"GET".equals(request.getMethod())) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("`@{}`不能用于非GET请求，建议将该注解从方法`{}`上移除掉。",
						Cache304.class.getSimpleName(), ReflectionUtils.methodToString(method));
			}
			return jp.proceed();
		}

		// 解析Cache304注解
		ICache304ConfigStorage configStorage = Cache304ConfigStorageFactory.getStorage();
		Cache304Config config = configStorage.getConfig(request);
		if (config == null) {
			Cache304 anno = method.getAnnotation(Cache304.class);
			config = Cache304AnnotationParser.parse(anno);
			// 放入存储器，使`Cache304Filter`中 能够直接拦截下请求。
			configStorage.putConfig(request, config);
		}

		// 执行304缓存方法
		try {
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