/*
 *  Copyright 2021 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
