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
package icu.easyj.core.util.shortcode.impls;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.util.StringUtils;
import icu.easyj.core.util.shortcode.IShortCodeService;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * long型ID 与 短字符串 互相转换服务 默认实现
 *
 * @author wangliang181230
 */
@LoadLevel(name = "default", order = 200)
public class DefaultShortCodeServiceImpl implements IShortCodeService {

	//region 常量

	/**
	 * 默认的自定义进制
	 */
	private static final char[] DEFAULT_CHAR_TABLE = new char[]{
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
	};

	//endregion


	/**
	 * 字符集
	 */
	protected final char[] charTable;

	protected final String firstChar;


	public DefaultShortCodeServiceImpl() {
		this(DEFAULT_CHAR_TABLE);
	}

	public DefaultShortCodeServiceImpl(char[] charTable) {
		// 字符集不能为空，且必须包含2个以上的字符
		Assert.isTrue(charTable != null && charTable.length > 2, "'charTable' must not be null");

		this.charTable = charTable;
		this.firstChar = String.valueOf(charTable[0]);
	}


	@NonNull
	@Override
	public String toCode(@NonNull Long id) {
		Assert.isTrue(id != null && id >= 0, "ID必须大于等于0");

		if (id == 0) {
			return firstChar;
		}

		// 自定义进制字符集长度
		int charTableLength = charTable.length;

		double power = Math.log(id) / Math.log(charTableLength); // Math.pow(charTableLength, power) == id
		int charPos = (int)(power) + 2;

		// 生成字符数组
		char[] buf = new char[charPos];
		while (id > 0) {
			int index = (int)(id % charTableLength);
			buf[--charPos] = charTable[index];
			id /= charTableLength;
		}

		if (charPos == 0) {
			return new String(buf);
		} else {
			return new String(buf, charPos, (buf.length - charPos));
		}
	}


	@Override
	public long toId(@NonNull String shortCode) {
		Assert.isTrue(StringUtils.isNotBlank(shortCode), "'shortCode' must not be blank");

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
			if (i > 0) {
				id = id * this.charTable.length + index;
			} else {
				id = index;
			}
		}
		return id;
	}
}
