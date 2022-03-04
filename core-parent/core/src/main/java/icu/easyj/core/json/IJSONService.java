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
package icu.easyj.core.json;

import java.lang.reflect.Type;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * JSON服务
 *
 * @author wangliang181230
 */
public interface IJSONService {

	/**
	 * 获取服务名
	 *
	 * @return 服务名
	 */
	@NonNull
	String getName();

	/**
	 * 转换为指定类型的对象
	 *
	 * @param text        字符串
	 * @param targetClazz 目标类型
	 * @param <T>         目标类
	 * @return 目标类型的对象
	 * @throws JSONParseException 转换异常
	 */
	@NonNull
	<T> T toBean(@NonNull String text, @NonNull Class<T> targetClazz) throws JSONParseException;

	/**
	 * 转换为指定类型的对象
	 *
	 * @param text       字符串
	 * @param targetType 目标类型
	 * @param <T>        目标类
	 * @return 目标类型的对象
	 * @throws JSONParseException 转换异常
	 */
	<T> T toBean(@NonNull String text, @NonNull Type targetType) throws JSONParseException;

	/**
	 * 转换为指定类型的对象
	 *
	 * @param text    字符串
	 * @param rawType 目标类型
	 * @param <T>     目标类
	 * @return 目标类型的对象
	 * @throws JSONParseException 转换异常
	 */
	default <T> T toBean(@NonNull String text, @NonNull Class<?> rawType, Class<?>... actualTypeArguments) throws JSONParseException {
		Type type = ParameterizedTypeImpl.make(rawType, actualTypeArguments, null);
		return this.toBean(text, type);
	}

	/**
	 * 转换为指定类型的列表
	 *
	 * @param text        字符串
	 * @param targetClazz 目标类型
	 * @param <T>         目标类
	 * @return 目标类型的列表对象
	 * @throws JSONParseException 转换异常
	 */
	@NonNull
	<T> List<T> toList(@NonNull String text, @NonNull Class<T> targetClazz) throws JSONParseException;

	/**
	 * 转换为字符串
	 *
	 * @param obj 对象
	 * @return JSON字符串
	 * @throws JSONParseException 转换异常
	 */
	@NonNull
	@SuppressWarnings("all")
	String toJSONString(@Nullable Object obj) throws JSONParseException;
}
