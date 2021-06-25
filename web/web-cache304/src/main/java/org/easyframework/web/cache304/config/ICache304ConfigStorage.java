package org.easyframework.web.cache304.config;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;

/**
 * Cache304配置存储器
 *
 * @author wangliang181230
 */
public interface ICache304ConfigStorage {

	/**
	 * 添加配置
	 *
	 * @param path   请求路径
	 * @param config 缓存配置
	 */
	void putConfig(String path, Cache304Config config);

	/**
	 * 获取配置
	 *
	 * @param path 请求路径
	 * @return config Cache304配置
	 */
	Cache304Config getConfig(String path);


	//region Default Methods

	/**
	 * 添加配置
	 *
	 * @param request 请求实例
	 * @param config  缓存配置
	 */
	default void putConfig(HttpServletRequest request, Cache304Config config) {
		Assert.notNull(request, "request must be not null");
		this.putConfig(request.getRequestURI(), config);
	}

	/**
	 * 批量添加配置
	 *
	 * @param configMap 配置集合
	 */
	default void putConfig(Map<String, Cache304Config> configMap) {
		if (MapUtil.isNotEmpty(configMap)) {
			configMap.forEach(this::putConfig);
		}
	}

	/**
	 * 获取Cache304配置
	 *
	 * @param request 请求实例
	 * @return config Cache304配置
	 */
	default Cache304Config getConfig(HttpServletRequest request) {
		Assert.notNull(request, "request must be not null");
		return this.getConfig(request.getRequestURI());
	}

	//endregion
}
