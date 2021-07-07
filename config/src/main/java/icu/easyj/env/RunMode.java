package icu.easyj.env;

import icu.easyj.core.util.EnumUtils;

/**
 * 运行模式枚举
 *
 * @author wangliang181230
 */
public enum RunMode {

	/**
	 * 发行模式
	 */
	RELEASE,

	/**
	 * 调试模式
	 */
	DEBUG;


	/**
	 * 根据 `模式名` 获取 `运行模式枚举`
	 *
	 * @param mode 模式名
	 * @return enum 运行模式枚举
	 */
	public static RunMode get(String mode) {
		return EnumUtils.fromName(RunMode.class, mode);
	}
}
