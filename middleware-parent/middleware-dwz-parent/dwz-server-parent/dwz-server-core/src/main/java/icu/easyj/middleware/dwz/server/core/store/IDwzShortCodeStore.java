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
package icu.easyj.middleware.dwz.server.core.store;

import icu.easyj.core.util.shortcode.ShortCodeUtils;
import org.springframework.lang.NonNull;

/**
 * 短链接码 存取接口
 *
 * @author wangliang181230
 */
public interface IDwzShortCodeStore {

	/**
	 * 获取下一个短链接码ID<br>
	 * 建议该ID是自增1的方式生成，可以有效控制短链接码的长度。使用初期可以低于
	 *
	 * @return 下一个短链接码ID
	 */
	long nextShortUrlCodeId();

	/**
	 * 生成新的短链接码
	 *
	 * @return shortUrlCode 短链接码
	 */
	@NonNull
	default String createShortUrlCode() {
		long nextShortCodeId = this.nextShortUrlCodeId();
		return ShortCodeUtils.toCode(nextShortCodeId);
	}
}
