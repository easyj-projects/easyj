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
import java.util.Map;

import icu.easyj.core.util.TypeBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

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
	 * 转换为指定泛型类型的对象
	 *
	 * @param text                字符串
	 * @param rawType             目标泛型类型
	 * @param actualTypeArguments 泛型实际类型数组
	 * @param <T>                 目标泛型类
	 * @return 目标泛型类型的对象
	 * @throws JSONParseException 转换异常
	 */
	default <T> T toBean(@NonNull String text, @NonNull Class<T> rawType, Type... actualTypeArguments) throws JSONParseException {
		Type type = TypeBuilder.buildGeneric(rawType, actualTypeArguments);
		return this.toBean(text, type);
	}

	/**
	 * 转换为指定类型的List
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
	 * 转换为指定类型的Map
	 *
	 * @param text       字符串
	 * @param keyClazz   键类型
	 * @param valueClazz 值类型
	 * @param <K>        键类
	 * @param <V>        值类
	 * @return 目标类型的Map对象
	 * @throws JSONParseException JSON解析失败
	 * @since 0.6.6
	 */
	@SuppressWarnings("all")
	default <K, V> Map<K, V> toMap(String text, @NonNull Class<K> keyClazz, @NonNull Class<V> valueClazz) {
		return this.toBean(text, Map.class, keyClazz, valueClazz);
	}

	/**
	 * 转换为指定类型的Map
	 *
	 * @param text     字符串
	 * @param keyClazz 键类型
	 * @param <K>      键类
	 * @return 目标类型的Map对象
	 * @throws JSONParseException JSON解析失败
	 * @since 0.6.6
	 */
	default <K> Map<K, Object> toMap(String text, @NonNull Class<K> keyClazz) {
		return this.toMap(text, keyClazz, Object.class);
	}

	/**
	 * 转换为指定类型的Map
	 *
	 * @param text 字符串
	 * @return 目标类型的Map对象
	 * @throws JSONParseException JSON解析失败
	 * @since 0.7.4
	 */
	default Map<String, Object> toMap(String text) {
		return this.toMap(text, String.class, Object.class);
	}

	/**
	 * 转换为 Key类型为String.class，Value类型为指定类型的Map
	 *
	 * @param text       字符串
	 * @param valueClazz 键值类型
	 * @param <V>        值类
	 * @return 目标类型的Map对象
	 * @throws JSONParseException JSON解析失败
	 * @since 0.7.4
	 */
	default <V> Map<String, V> toMap2(String text, @NonNull Class<V> valueClazz) {
		return this.toMap(text, String.class, valueClazz);
	}

	/**
	 * 转换为指定类型的Map
	 *
	 * @param text          字符串
	 * @param keyValueClazz 键值类型
	 * @param <KV>          键值类
	 * @return 目标类型的Map对象
	 * @throws JSONParseException JSON解析失败
	 * @since 0.6.6
	 */
	default <KV> Map<KV, KV> toMap3(String text, @NonNull Class<KV> keyValueClazz) {
		return this.toMap(text, keyValueClazz, keyValueClazz);
	}

	/**
	 * 转换为字符串
	 *
	 * @param obj 对象
	 * @return JSON字符串
	 * @throws JSONParseException 转换异常
	 */
	@NonNull
	String toJSONString(@Nullable Object obj) throws JSONParseException;
}
