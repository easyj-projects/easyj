package org.easyframework.web.cache304.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.hutool.core.lang.Assert;
import org.easyframework.core.util.UrlUtils;

/**
 * Cache304配置存储器 默认实现类
 *
 * @author wangliang181230
 */
public class DefaultCache304ConfigStorageImpl implements ICache304ConfigStorage {

	/**
	 * path → config
	 */
	private final Map<String, Cache304Config> cache304ConfigMap;


	//region Constructor

	/**
	 * 默认构造函数
	 */
	public DefaultCache304ConfigStorageImpl() {
		cache304ConfigMap = new ConcurrentHashMap<>(4);
	}

	/**
	 * 带参构造函数
	 *
	 * @param cache304ConfigMap 保存配置的map
	 */
	public DefaultCache304ConfigStorageImpl(Map<String, Cache304Config> cache304ConfigMap) {
		this.cache304ConfigMap = cache304ConfigMap;
	}

	//endregion


	//region Override

	@Override
	public void putConfig(String path, Cache304Config config) {
		Assert.notNull(path, "path must be not null");
		Assert.notNull(config, "config must be not null");

		// 标准化路径
		path = UrlUtils.normalizePath(path);

		// 添加配置
		this.cache304ConfigMap.put(path, config);
	}

	@Override
	public Cache304Config getConfig(String path) {
		Assert.notNull(path, "path must be not null");

		// 标准化路径
		path = UrlUtils.normalizePath(path);

		// 获取参数
		return this.cache304ConfigMap.get(path.trim());
	}

	//endregion
}
