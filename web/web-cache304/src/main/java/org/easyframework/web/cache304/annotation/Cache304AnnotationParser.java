package org.easyframework.web.cache304.annotation;

import org.easyframework.web.cache304.config.Cache304Config;

/**
 * Cache304注解 解析器
 *
 * @author wangliang181230
 */
public class Cache304AnnotationParser {

	/**
	 * 解析{@link Cache304}注解
	 *
	 * @param annotation 注解
	 * @return config 缓存配置
	 */
	public static Cache304Config parse(Cache304 annotation) {
		Cache304Config config = new Cache304Config();

		config.setCacheSeconds(getCacheSeconds(annotation));
		config.setCacheDays(annotation.cacheDays());
		config.setUseMaxAge(annotation.useMaxAge());

		return config;
	}

	/**
	 * 从注解中获取缓存秒数
	 *
	 * @param annotation 注解
	 * @return cacheSeconds 缓存秒数
	 */
	private static long getCacheSeconds(Cache304 annotation) {
		if (annotation.cacheSeconds() > 0) {
			return annotation.cacheSeconds();
		}
		return annotation.value();
	}
}
