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

import java.lang.reflect.InvocationTargetException;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnJavaVersion;
import icu.easyj.core.util.IStringService;
import org.springframework.lang.NonNull;

/**
 * JDK9及以上时，{@link IStringService} 的实现
 *
 * @author wangliang181230
 */
@LoadLevel(name = "JDK9~15-Base64", order = 900)
@DependsOnJavaVersion(min = 9F, max = 15F)
class Jdk9To15StringServiceImpl implements IStringService {

	@Override
	public char[] toCharArray(@NonNull CharSequence str) {
		return str.toString().toCharArray();
	}

	@Override
	public byte[] getValue(@NonNull CharSequence str) {
		// JDK9及以上：返回 byte[]
		try {
			return (byte[])StringReflection.STRING_VALUE_FIELD.get(str.toString());
		} catch (IllegalAccessException e) {
			throw new RuntimeException("获取字符串的value失败", e);
		}
	}

	@Override
	public byte getCoder(@NonNull CharSequence str) {
		try {
			return (byte)StringReflection.GET_STRING_CODER_METHOD.invoke(str.toString());
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException("获取字符串的coder失败", e);
		}
	}
}
