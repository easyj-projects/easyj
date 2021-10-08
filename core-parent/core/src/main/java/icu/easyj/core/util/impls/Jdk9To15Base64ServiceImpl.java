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
package icu.easyj.core.util.impls;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.ServiceDependsOn;
import icu.easyj.core.util.Base64Utils;
import icu.easyj.core.util.IBase64Service;
import icu.easyj.core.util.StringUtils;
import org.springframework.lang.NonNull;

/**
 * JDK9~15时，{@link IBase64Service} 的实现
 *
 * @author wangliang181230
 */
@LoadLevel(name = "JDK9~15-Base64-Impl", order = 90)
@ServiceDependsOn(minJavaVersion = 9F, maxJavaVersion = 15F)
public class Jdk9To15Base64ServiceImpl implements IBase64Service {

	@Override
	public boolean isBase64(@NonNull CharSequence cs) {
		int coder = StringUtils.getCoder(cs);

		// coder为1时，表示字符串中存在双字节字符，肯定不是Base64，直接返回false
		if (coder == 0) {
			// 通过反射直接获取字符串的字节数组，避免 `String.getBytes(Charset)` 方法中不必要的性能损耗。
			//byte[] bytes = cs.toString().getBytes(StandardCharsets.UTF_8);
			byte[] bytes = (byte[])StringUtils.getValue(cs);

			// 判断字符数组是否为Base64
			return Base64Utils.isBase64Bytes(bytes);
		} else {
			// coder为1时，表示字符串中存在双字节字符，肯定不是Base64，直接返回false
			return false;
		}
	}
}
