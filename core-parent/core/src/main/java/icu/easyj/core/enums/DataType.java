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
package icu.easyj.core.enums;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import icu.easyj.core.util.StringUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;

/**
 * 数据类型（目前就一些常用的数据类型，可以根据需要再添加一些）
 *
 * @author wangliang181230
 */
public enum DataType {

	//region 字符型

	STRING(11, String.class, "String"),
	CHAR_PRIMITIVE(12, char.class, "char"),
	CHARACTER(13, Character.class, "Character", "Char"),

	//endregion


	//region 整形

	SHORT_PRIMITIVE(21, short.class, "short"),
	SHORT(22, Short.class, "Short"),
	INT_PRIMITIVE(23, int.class, "int"),
	INTEGER(24, Integer.class, "Integer", "Int"),
	LONG_PRIMITIVE(25, long.class, "long"),
	LONG(26, Long.class, "Long"),
	BIG_INTEGER(27, BigInteger.class, "BigInteger"),

	//endregion


	//region 浮点型

	FLOAT_PRIMITIVE(31, float.class, "float"),
	FLOAT(32, Float.class, "Float"),
	DOUBLE_PRIMITIVE(33, double.class, "double"),
	DOUBLE(34, Double.class, "Double"),
	BIG_DECIMAL(35, BigDecimal.class, "decimal", "BigDecimal"),

	//endregion


	//region 布尔型

	BOOLEAN_PRIMITIVE(41, boolean.class, "boolean", "bool"),
	BOOLEAN(42, Boolean.class, "Boolean"),

	//endregion


	//region 字节

	BYTE_PRIMITIVE(51, byte.class, "byte"),
	BYTE(52, Byte.class, "Byte"),

	//endregion


	//region 时间

	DATE(61, Date.class, "Date", "date"),
	DURATION(62, Duration.class, "Duration", "duration"),

	//endregion

	;


	//region Fields

	/**
	 * 代码：内存占用少，存数据库时，考虑存这个值
	 */
	private final int code;

	/**
	 * 数据类
	 */
	private final Class<?> clazz;

	/**
	 * 数据类型描述
	 */
	private final TypeDescriptor typeDesc;

	/**
	 * 类型字符串
	 */
	private final Set<String> types;

	//endregion

	DataType(int code, Class<?> clazz, String... types) {
		this.code = code;
		this.clazz = clazz;
		this.typeDesc = TypeDescriptor.valueOf(clazz);
		this.types = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(types)));
	}


	//region Getter

	public int getCode() {
		return code;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public TypeDescriptor getTypeDesc() {
		return typeDesc;
	}

	public boolean isPrimitive() {
		return clazz.isPrimitive();
	}

	//endregion


	//region Static Methods

	/**
	 * 根据代码获取枚举
	 *
	 * @param code 代码
	 * @return 枚举
	 */
	@Nullable
	public static DataType getByCode(final int code) {
		if (code <= 0) {
			return null;
		}
		for (DataType dataType : DataType.values()) {
			if (dataType.code == code) {
				return dataType;
			}
		}
		return null;
	}

	/**
	 * 根据类型字符串获取枚举
	 *
	 * @param type 类型字符串
	 * @return 枚举
	 */
	@Nullable
	public static DataType getByType(final String type) {
		if (StringUtils.isEmpty(type)) {
			return null;
		}
		// 先整体查找一遍
		for (DataType dataType : DataType.values()) {
			if (dataType.clazz.getName().equals(type)
					|| dataType.types.contains(type)) {
				return dataType;
			}
		}
		// 忽略大小写，从types中查找一遍
		for (DataType dataType : DataType.values()) {
			for (String tp : dataType.types) {
				if (tp.equalsIgnoreCase(type)) {
					return dataType;
				}
			}
		}
		return null;
	}

	/**
	 * 根据数据类获取枚举
	 *
	 * @param clazz 数据类
	 * @return 枚举
	 */
	@Nullable
	public static DataType getByClass(final Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		for (DataType dataType : DataType.values()) {
			if (dataType.clazz.equals(clazz)) {
				return dataType;
			}
		}
		return null;
	}

	//endregion
}
