package org.easyframework.web.cache304.config;

/**
 * Cache304配置存储器 工厂类
 *
 * @author wangliang181230
 */
public class Cache304ConfigStorageFactory {

	/**
	 * 配置存储器
	 */
	private static ICache304ConfigStorage configStorage;

	/**
	 * 获取配置存储器
	 *
	 * @return storage 配置存储器
	 */
	public static ICache304ConfigStorage getStorage() {
		if (configStorage == null) {
			synchronized (Cache304ConfigStorageFactory.class) {
				if (configStorage == null) {
					configStorage = new DefaultCache304ConfigStorageImpl();
				}
			}
		}
		return configStorage;
	}

	/**
	 * 设置配置存储器
	 *
	 * @param storage 配置存储器
	 */
	public static void setStorage(ICache304ConfigStorage storage) {
		configStorage = storage;
	}
}
