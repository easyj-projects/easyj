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
package icu.easyj.core.util.shortcode;

import icu.easyj.core.loader.EnhancedServiceLoader;

/**
 * long型ID 与 短字符串 互相转换的工具类
 * <p>
 * 用途：邀请码、短链接码、...等等
 *
 * @author wangliang181230
 */
public abstract class ShortCodeUtils {

	/**
	 * 默认的 短字符串服务
	 */
	public static final IShortCodeService DEFAULT = EnhancedServiceLoader.load(IShortCodeService.class, "default");

	/**
	 * 最小长度的 短字符串服务（默认最小长度5）
	 */
	public static final IShortCodeService MIN_LENGTH = EnhancedServiceLoader.load(IShortCodeService.class, "min-length");


	/**
	 * 根据ID生成短字符串
	 *
	 * @param id ID
	 * @return 短字符串
	 * @throws IllegalArgumentException ID小于0
	 */
	public static String toCode(Long id) {
		return DEFAULT.toCode(id);
	}

	/**
	 * 短字符串转为64位长整形ID
	 *
	 * @param shortCode 短字符串
	 * @return 原ID
	 */
	public static long toId(String shortCode) {
		return DEFAULT.toId(shortCode);
	}
}
