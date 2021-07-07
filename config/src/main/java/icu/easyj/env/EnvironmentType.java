package icu.easyj.env;

import icu.easyj.core.util.EnumUtils;

/**
 * 环境类型枚举
 *
 * @author wangliang181230
 */
public enum EnvironmentType {

	/**
	 * 生产环境
	 */
	PROD,

	/**
	 * 沙箱环境
	 */
	SANDBOX,

	/**
	 * 测试环境
	 */
	TEST,

	/**
	 * 开发环境
	 */
	DEV;

	/**
	 * 根据 `环境名` 获取 `环境类型枚举`
	 *
	 * @param name 环境名
	 * @return enum 环境类型枚举
	 */
	public static EnvironmentType get(String name) {
		return EnumUtils.fromName(EnvironmentType.class, name);
	}
}
