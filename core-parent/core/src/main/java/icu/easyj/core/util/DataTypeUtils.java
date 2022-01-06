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
package icu.easyj.core.util;

import java.util.concurrent.ConcurrentHashMap;

import cn.hutool.core.text.StrPool;
import icu.easyj.core.enums.DataType;
import icu.easyj.core.exception.SkipCallbackWrapperException;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 数据类型工具类
 *
 * @author wangliang181230
 */
public abstract class DataTypeUtils {

	/**
	 * 缓存：dataType -> Class
	 */
	private static final ConcurrentHashMap<String, Class<?>> CLASS_CACHE = new ConcurrentHashMap<>();

	/**
	 * 数据类型字符串转换为对应的数据类
	 * 注：短字符串时，仅支持几种常用的数据类型。
	 *
	 * @param dataType 数据类型字符串
	 * @return 数据类
	 * @throws ClassNotFoundException 类未找到
	 */
	@NonNull
	public static Class<?> toClass(final String dataType) throws ClassNotFoundException {
		Assert.notNull(dataType, "'dataType' must not be null");

		try {
			return MapUtils.computeIfAbsent(CLASS_CACHE, dataType, key -> {
				try {
					if (dataType.contains(StrPool.DOT)) {
						return ReflectionUtils.getClassByName(dataType);
					} else {
						final DataType dataTypeEnum = DataType.getByType(dataType);
						if (dataTypeEnum == null) {
							throw new ClassNotFoundException("未知的类型：" + dataType);
						}
						return dataTypeEnum.getClazz();
					}
				} catch (ClassNotFoundException e) {
					throw new SkipCallbackWrapperException(e);
				}
			});
		} catch (SkipCallbackWrapperException e) {
			throw (ClassNotFoundException)e.getCause();
		}
	}
}
