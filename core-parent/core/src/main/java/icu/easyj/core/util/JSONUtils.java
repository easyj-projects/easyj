/*
 * Copyright 2021 the original author or authors.
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

import java.util.List;

import icu.easyj.core.exception.JSONParseException;
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
	private static final IJSONService SERVICE = EnhancedServiceLoader.load(IJSONService.class);


	/**
	 * 转换为指定类型的对象
	 *
	 * @param text        字符串
	 * @param targetClazz 目标类型
	 * @param <T>         目标类
	 * @return 目标类型的对象
	 * @throws icu.easyj.core.exception.JSONParseException JSON解析失败
	 */
	public static <T> T toBean(String text, @NonNull Class<T> targetClazz) {
		if (text == null) {
			return null;
		}

		Assert.notNull(targetClazz, "'targetClazz' must not be null");

		try {
			return SERVICE.toBean(text, targetClazz);
		} catch (Exception e) {
			throw new JSONParseException("解析JSON失败", e);
		}
	}

	/**
	 * 转换为指定类型的列表
	 *
	 * @param text        字符串
	 * @param targetClazz 目标类型
	 * @param <T>         目标类
	 * @return 目标类型的列表对象
	 * @throws icu.easyj.core.exception.JSONParseException JSON解析失败
	 */
	public static <T> List<T> toList(String text, @NonNull Class<T> targetClazz) {
		if (text == null) {
			return null;
		}

		Assert.notNull(targetClazz, "'targetClazz' must not be null");

		try {
			return SERVICE.toList(text, targetClazz);
		} catch (Exception e) {
			throw new JSONParseException("解析JSON失败", e);
		}
	}

	/**
	 * 转换为字符串
	 *
	 * @param obj 对象
	 * @return JSON字符串
	 * @throws icu.easyj.core.exception.JSONParseException JSON解析失败
	 */
	@NonNull
	@SuppressWarnings("all")
	public static String toJSONString(@Nullable Object obj) {
		if (obj == null) {
			return "null";
		}

		try {
			return SERVICE.toJSONString(obj);
		} catch (Exception e) {
			throw new JSONParseException("解析JSON失败", e);
		}
	}
}
