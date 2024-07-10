/*
 * Copyright 2021-2024 the original author or authors.
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
package icu.easyj.core.util.string.impls;

import java.lang.annotation.Native;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnJavaVersion;
import icu.easyj.core.util.string.IStringService;
import org.springframework.lang.NonNull;

/**
 * JDK9及以上时，{@link IStringService} 的实现
 *
 * @author wangliang181230
 */
@LoadLevel(name = "JDK16~Latest-String", order = 1600)
@DependsOnJavaVersion(min = 16F)
class Jdk16ToLatestStringServiceImpl implements IStringService {

	@Native
	static final byte LATIN1 = 0;
	@Native
	static final byte UTF16 = 1;

	@Override
	public char[] toCharArray(@NonNull CharSequence str) {
		return str.toString().toCharArray();
	}

	@Override
	public char[] getValue(@NonNull CharSequence str) {
		// JDK16及以上：返回 char[]
		return this.toCharArray(str);
	}

	@Override
	public byte getCoder(@NonNull CharSequence str) {
		// 注意：性能较差，不建议在JDK16及以上版本使用
		char[] chars = toCharArray(str);
		for (char c : chars) {
			if (c > Byte.MAX_VALUE) {
				return UTF16;
			}
		}
		return LATIN1;
	}
}
