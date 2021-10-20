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
package icu.easyj.core.util.string.impls;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnJavaVersion;
import icu.easyj.core.util.IStringService;
import org.springframework.lang.NonNull;

/**
 * JDK8及以下时，{@link IStringService} 的实现
 *
 * @author wangliang181230
 */
@LoadLevel(name = "JDK8-String", order = 180)
@DependsOnJavaVersion(max = 1.8F)
class Jdk8StringServiceImpl implements IStringService {

	@Override
	public char[] toCharArray(@NonNull CharSequence str) {
		return getValue(str);
	}

	@Override
	public char[] getValue(@NonNull CharSequence str) {
		// JDK8：返回 char[]
		try {
			return (char[])StringReflection.STRING_VALUE_FIELD.get(str.toString());
		} catch (IllegalAccessException e) {
			throw new RuntimeException("获取字符串的value失败", e);
		}
	}

	@Override
	public byte getCoder(@NonNull CharSequence str) {
		// 注意：性能较差，不建议在JDK8版本使用
		char[] chars = toCharArray(str);
		for (char c : chars) {
			if (c > Byte.MAX_VALUE) {
				return StringReflection.UTF16;
			}
		}
		return StringReflection.LATIN1;
	}
}
