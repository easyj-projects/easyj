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
package icu.easyj.core.util.string;

import org.springframework.lang.NonNull;

/**
 * {@link String} 的功能接口，用于不同版本号使用不同方式执行
 *
 * @author wangliang181230
 */
public interface IStringService {

	/**
	 * 获取字符数组
	 *
	 * @param str 字符串
	 * @return 字符数组
	 */
	char[] toCharArray(@NonNull CharSequence str);

	/**
	 * 获取String的value属性值
	 * <p>
	 * 部分场景下，我们获取字符串的char数组，只是为了校验字符串，并没有任何修改、删除操作。<br>
	 * 但由于 {@link String#toCharArray()} 方法会复制一次字符数组，导致无谓的性能损耗。<br>
	 * 所以，开发了此方法用于提升性能。
	 *
	 * @param str 字符串
	 * @return java8返回char[]、java9及以上返回byte[]
	 * @throws IllegalArgumentException str为空时，抛出该异常
	 * @see String#toCharArray()
	 */
	Object getValue(@NonNull CharSequence str);

	/**
	 * 获取String的coder属性值
	 *
	 * @param str 字符串
	 * @return 字符编码的标识符（值域：0=LATIN1 | 1=UTF16）
	 * @throws IllegalArgumentException str为空时，抛出该异常
	 */
	byte getCoder(@NonNull CharSequence str);
}
