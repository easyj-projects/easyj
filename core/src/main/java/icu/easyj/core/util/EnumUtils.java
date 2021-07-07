package icu.easyj.core.util;

import org.springframework.util.Assert;

/**
 * 枚举工具类
 *
 * @author wangliang181230
 */
public abstract class EnumUtils {

	/**
	 * 根据枚举名称字符串，获取枚举（大小写不敏感）
	 *
	 * @param enumClass 枚举类
	 * @param enumName  枚举名称
	 * @param <E>       枚举类型
	 * @return enum 枚举
	 */
	public static <E extends Enum<?>> E fromName(Class<E> enumClass, String enumName) {
		Assert.notNull(enumClass, "enumClass must be not null");
		Assert.notNull(enumName, "enumName must be not null");

		E[] enums = enumClass.getEnumConstants();
		for (E e : enums) {
			if (e.name().equalsIgnoreCase(enumName)) {
				return e;
			}
		}

		throw new IllegalArgumentException("unknown enum name '" + enumName + "' for the enum '" + enumClass.getName() + "'.");
	}
}
