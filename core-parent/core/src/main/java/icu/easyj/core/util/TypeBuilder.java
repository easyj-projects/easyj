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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import cn.hutool.core.lang.ParameterizedTypeImpl;

/**
 * 类型创建者
 *
 * @author wangliang181230
 */
public abstract class TypeBuilder {

	/**
	 * 创建泛型类型
	 *
	 * @param rawType             原始类型
	 * @param actualTypeArguments 泛型参数实际类型数组
	 * @return 返回泛型类型
	 */
	public static ParameterizedType buildGeneric(Class<?> rawType, Type ownerType, Type[] actualTypeArguments) {
		return new ParameterizedTypeImpl(actualTypeArguments, ownerType, rawType);
	}

	/**
	 * 创建泛型类型
	 *
	 * @param rawType             原始类型
	 * @param actualTypeArguments 泛型参数实际类型数组
	 * @return 返回泛型类型
	 */
	public static ParameterizedType buildGeneric(Class<?> rawType, Type... actualTypeArguments) {
		return buildGeneric(rawType, null, actualTypeArguments);
	}

	/**
	 * 创建 {@link List} 泛型类型
	 *
	 * @param actualType 泛型参数实际类型
	 * @return 返回 {@link List} 泛型类型
	 */
	public static ParameterizedType buildList(Class<?> actualType) {
		return buildGeneric(List.class, actualType);
	}

	/**
	 * 创建 {@link Set} 泛型类型
	 *
	 * @param actualType 泛型参数实际类型
	 * @return 返回 {@link Set} 泛型类型
	 */
	public static ParameterizedType buildSet(Class<?> actualType) {
		return buildGeneric(Set.class, actualType);
	}
}
