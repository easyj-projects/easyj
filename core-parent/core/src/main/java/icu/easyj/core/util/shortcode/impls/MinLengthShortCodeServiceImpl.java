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
package icu.easyj.core.util.shortcode.impls;

import java.util.concurrent.ThreadLocalRandom;

import cn.hutool.core.util.ArrayUtil;
import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.util.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * long型ID 与 短字符串 互相转换服务<br>
 * 基于默认实现的基础上，添加最小长度的功能
 *
 * @author wangliang181230
 */
@LoadLevel(name = "min-length", order = 100)
public class MinLengthShortCodeServiceImpl extends DefaultShortCodeServiceImpl {

	//region 一组默认的参数 常量

	/**
	 * 默认的自定义进制（不含分隔字符 {@link #DEFAULT_SEPARATOR}）
	 *
	 * @see #DEFAULT_SEPARATOR
	 */
	public static final char[] DEFAULT_CHAR_TABLE = new char[]{
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			/*'0',*/ '1', '2', '3', '4', '5', '6', '7', '8', '9'
	};

	/**
	 * 默认的分隔字符（不能与 {@link #DEFAULT_CHAR_TABLE} 中的字符重复）
	 *
	 * @see #DEFAULT_CHAR_TABLE
	 */
	public static final char DEFAULT_SEPARATOR = '0'; // 数字0

	/**
	 * 默认的短字符串最小长度
	 */
	public static final int DEFAULT_MIN_LENGTH = 5;

	//endregion


	/**
	 * 最短长度所需的分隔符（用于toId时解析使用）（不能与 {@link super#charTable} 中的字符重复）
	 */
	private final char separator;

	/**
	 * 最短长度
	 */
	private final int minLength;


	/**
	 * 构造函数
	 *
	 * @param charTable          字符集
	 * @param separator          分隔字符
	 * @param minLength          最小长度
	 * @param needCheckSeparator 是否校验分隔符的有效性
	 */
	public MinLengthShortCodeServiceImpl(char[] charTable, char separator, int minLength, boolean needCheckSeparator) {
		super(charTable);

		if (needCheckSeparator) {
			// 判断 separator 是否存在于charTable，如果存在，则抛出异常
			if (ArrayUtil.contains(charTable, separator)) {
				throw new IllegalArgumentException("字符集中不能包含分隔字符，否则生成的短字符串将无法反向解析");
			}
		}

		this.separator = separator;
		this.minLength = minLength;
	}

	public MinLengthShortCodeServiceImpl(char[] charTable, char separator, int minLength) {
		this(charTable, separator, minLength, true);
	}

	public MinLengthShortCodeServiceImpl(char[] charTable, char separator) {
		this(charTable, separator, DEFAULT_MIN_LENGTH, true);
	}

	public MinLengthShortCodeServiceImpl() {
		this(DEFAULT_CHAR_TABLE, DEFAULT_SEPARATOR, DEFAULT_MIN_LENGTH, true);
	}


	@NonNull
	@Override
	public String toCode(@NonNull Long id) {
		Assert.isTrue(id != null && id >= 0, "ID必须大于等于0");

		// 调用默认实现的方法
		String shortCode = super.toCode(id);

		// 长度不够时，自动随机补全
		if (shortCode.length() < this.minLength) {
			StringBuilder sb = new StringBuilder();
			sb.append(this.separator);
			for (int i = 1; i < this.minLength - shortCode.length(); i++) {
				sb.append(this.charTable[ThreadLocalRandom.current().nextInt(this.charTable.length)]);
			}
			shortCode += sb.toString();
		}

		return shortCode;
	}

	@Override
	public long toId(@NonNull String shortCode) {
		Assert.notNull(shortCode, "'shortCode' must not be null");

		if (shortCode.isEmpty()) {
			return 0L;
		}

		char[] chars = StringUtils.toCharArray(shortCode);
		long id = 0L;
		for (int i = 0; i < chars.length; i++) {
			int index = 0;
			for (int j = 0; j < this.charTable.length; j++) {
				if (chars[i] == this.charTable[j]) {
					index = j;
					break;
				}
			}
			// 当前方法相对于父类方法，多了这个判断，其他代码完全相同
			if (chars[i] == this.separator) {
				break;
			}
			if (i > 0) {
				id = id * this.charTable.length + index;
			} else {
				id = index;
			}
		}
		return id;
	}
}
