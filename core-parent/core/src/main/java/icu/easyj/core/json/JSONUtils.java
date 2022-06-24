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

import icu.easyj.core.loader.EnhancedServiceLoader;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * JSON工具类
 *
 * @author wangliang181230
 */
public abstract class JSONUtils {

	/**
	 * JSON服务实现
	 */
	private static final IJSONService JSON_SERVICE = EnhancedServiceLoader.load(IJSONService.class);


	/**
	 * 转换为指定类型的对象
	 *
	 * @param text        字符串
	 * @param targetClazz 目标类型
	 * @param <T>         目标类
	 * @return 目标类型的对象
	 * @throws JSONParseException JSON解析失败
	 */
	public static <T> T toBean(String text, @NonNull Class<T> targetClazz) {
		if (text == null) {
			return null;
		}

		Assert.notNull(targetClazz, "'targetClazz' must not be null");

		return JSON_SERVICE.toBean(text, targetClazz);
	}

	/**
	 * 转换为指定类型的对象
	 *
	 * @param text       字符串
	 * @param targetType 目标类型
	 * @param <T>        目标类
	 * @return 目标类型的对象
	 * @throws JSONParseException JSON解析失败
	 */
	public static <T> T toBean(String text, @NonNull Type targetType) {
		if (text == null) {
			return null;
		}

		Assert.notNull(targetType, "'targetType' must not be null");

		return JSON_SERVICE.toBean(text, targetType);
	}

	/**
	 * 转换为指定泛型类型的对象
	 *
	 * @param text                字符串
	 * @param rawType             目标泛型类型
	 * @param actualTypeArguments 泛型实际类型数组
	 * @param <T>                 目标泛型类
	 * @return 目标泛型类型的对象
	 * @throws JSONParseException JSON解析失败
	 */
	public static <T> T toBean(String text, @NonNull Class<T> rawType, Type... actualTypeArguments) {
		if (text == null) {
			return null;
		}

		Assert.notNull(rawType, "'rawType' must not be null");
		Assert.notNull(actualTypeArguments, "'actualTypeArguments' must not be null");
		Assert.isTrue(actualTypeArguments.length > 0, "'actualTypeArguments' must not be empty");

		return JSON_SERVICE.toBean(text, rawType, actualTypeArguments);
	}

	/**
	 * 转换为指定类型的列表
	 *
	 * @param text        字符串
	 * @param targetClazz 目标类型
	 * @param <T>         目标类
	 * @return 目标类型的列表对象
	 * @throws JSONParseException JSON解析失败
	 */
	public static <T> List<T> toList(String text, @NonNull Class<T> targetClazz) {
		if (text == null) {
			return null;
		}

		Assert.notNull(targetClazz, "'targetClazz' must not be null");

		return JSON_SERVICE.toList(text, targetClazz);
	}

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
	public static <K, V> Map<K, V> toMap(String text, @NonNull Class<K> keyClazz, @NonNull Class<V> valueClazz) {
		if (text == null) {
			return null;
		}

		Assert.notNull(keyClazz, "'keyClazz' must not be null");
		Assert.notNull(valueClazz, "'valueClazz' must not be null");

		return JSON_SERVICE.toMap(text, keyClazz, valueClazz);
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
	public static <K> Map<K, Object> toMap(String text, @NonNull Class<K> keyClazz) {
		return toMap(text, keyClazz, Object.class);
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
	public static <KV> Map<KV, KV> toMap2(String text, @NonNull Class<KV> keyValueClazz) {
		return toMap(text, keyValueClazz, keyValueClazz);
	}

	/**
	 * 转换为字符串
	 *
	 * @param obj 对象
	 * @return JSON字符串
	 * @throws JSONParseException JSON解析失败
	 */
	@NonNull
	@SuppressWarnings("all")
	public static String toJSONString(@Nullable Object obj) {
		if (obj == null) {
			return "null";
		}

		return JSON_SERVICE.toJSONString(obj);
	}
}
