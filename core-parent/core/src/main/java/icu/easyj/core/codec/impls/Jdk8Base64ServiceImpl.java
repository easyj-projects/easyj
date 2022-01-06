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
package icu.easyj.core.codec.impls;

import icu.easyj.core.codec.Base64Utils;
import icu.easyj.core.codec.IBase64Service;
import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnJavaVersion;
import icu.easyj.core.util.StringUtils;
import org.springframework.lang.NonNull;

/**
 * JDK8及以下时，{@link IBase64Service} 的实现
 *
 * @author wangliang181230
 */
@LoadLevel(name = "JDK8-Base64", order = 180)
@DependsOnJavaVersion(max = 1.8F)
class Jdk8Base64ServiceImpl implements IBase64Service {

	@Override
	public boolean isBase64(@NonNull CharSequence cs) {
		// 通过反射直接获取字符串的字符数组，避免 `String.toCharArray()` 方法中的 `System.arraycopy()` 操作导致不必要的性能损耗。
		//char[] chars = cs.toString().toCharArray();
		char[] chars = (char[])StringUtils.getValue(cs);

		// 判断字符数组是否为Base64
		return Base64Utils.isBase64Chars(chars);
	}
}
