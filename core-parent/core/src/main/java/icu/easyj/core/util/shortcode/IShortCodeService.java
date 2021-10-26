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

import org.springframework.lang.NonNull;

/**
 * long型ID 与 短字符串 互相转换的服务类
 * <p>
 * 用途：邀请码、短链接码、...等等
 *
 * @author wangliang181230
 */
public interface IShortCodeService {

	/**
	 * long型ID 转换为 短字符串
	 *
	 * @param id ID（必须大于等于0）
	 * @return 短字符串
	 * @throws IllegalArgumentException ID为null 或 ID小于0
	 */
	@NonNull
	String toCode(@NonNull Long id);

	/**
	 * 短字符串 转换为 long长整形ID
	 *
	 * @param shortCode 短字符串
	 * @return 原ID
	 * @throws IllegalArgumentException shortCode为null
	 */
	long toId(@NonNull String shortCode);
}
