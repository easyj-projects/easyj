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
package icu.easyj.core.util.base64.impls;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnJavaVersion;
import icu.easyj.core.util.Base64Utils;
import icu.easyj.core.util.IBase64Service;
import org.springframework.lang.NonNull;

/**
 * JDK16及以上时，{@link IBase64Service} 的实现
 *
 * @author wangliang181230
 */
@LoadLevel(name = "JDK16-Base64-Impl", order = 1600)
@DependsOnJavaVersion(min = 16F)
public class Jdk16Base64ServiceImpl implements IBase64Service {

	@Override
	public boolean isBase64(@NonNull CharSequence cs) {
		// 由于Jdk16及以上版本禁止了很多非法访问，所以没办法获取String.value和coder的值，所以只能直接
		return Base64Utils.isBase64Chars(cs.toString().toCharArray());
	}
}
