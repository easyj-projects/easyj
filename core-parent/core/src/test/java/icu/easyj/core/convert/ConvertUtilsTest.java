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
package icu.easyj.core.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import icu.easyj.core.enums.DateFormatType;
import icu.easyj.core.util.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link ConvertUtils} 测试类
 *
 * @author wangliang181230
 */
class ConvertUtilsTest {

	@Test
	void testStringToOtherConvert() {
		String source;


		//region String -> 时间

		// String -> Date
		{
			source = "2021-9-1 10:10";
			Date d = ConvertUtils.convert(source, Date.class);
			Assertions.assertEquals("2021-09-01 10:10:00.000", DateUtils.format(DateFormatType.SSS, d));
		}
		// String -> Date
		{
			Date now = new Date();
			source = DateUtils.format(DateFormatType.SSS, now);
			Date d = ConvertUtils.convert(source, Date.class);
			Assertions.assertEquals(now.getTime(), d.getTime());
		}

		//endregion


		// String -> Character
		{
			Character c = ConvertUtils.convert("c", Character.class);
			Assertions.assertEquals('c', c.charValue());
		}


		// String -> Boolean
		{
			Assertions.assertTrue(ConvertUtils.convert("True", Boolean.class));
			Assertions.assertFalse(ConvertUtils.convert("False", Boolean.class));
		}


		// String -> Byte
		{
			Assertions.assertEquals((byte)1, ConvertUtils.convert("1", Byte.class).byteValue());
			Assertions.assertEquals((byte)0, ConvertUtils.convert("0", byte.class).byteValue());
		}


		//region String -> 整形数字

		source = "111";
		// String -> Short
		{
			short s1 = ConvertUtils.convert(source, short.class);
			Assertions.assertEquals((short)111, s1);
			Short s2 = ConvertUtils.convert(source, Short.class);
			Assertions.assertEquals((short)111, s2.shortValue());
		}
		// String -> Integer
		{
			int i1 = ConvertUtils.convert(source, int.class);
			Assertions.assertEquals(111, i1);
			Integer i2 = ConvertUtils.convert(source, Integer.class);
			Assertions.assertEquals(111, i2.intValue());
		}
		// String -> Long
		{
			long l1 = ConvertUtils.convert(source, long.class);
			Assertions.assertEquals(111, l1);
			Long l2 = ConvertUtils.convert(source, Long.class);
			Assertions.assertEquals(111L, l2.longValue());
		}
		// String -> BigInteger
		{
			BigInteger bi = ConvertUtils.convert(source, BigInteger.class);
			Assertions.assertEquals(111, bi.intValue());
		}

		//endregion


		//region String -> 浮点数字

		source = "111.1";
		// String -> Float
		{
			float f1 = ConvertUtils.convert(source, float.class);
			Assertions.assertEquals(111.1F, f1);
			Float f2 = ConvertUtils.convert(source, Float.class);
			Assertions.assertEquals(111.1F, f2.floatValue());
		}
		// String -> Double
		{
			double f1 = ConvertUtils.convert(source, double.class);
			Assertions.assertEquals(111.1D, f1);
			Double f2 = ConvertUtils.convert(source, Double.class);
			Assertions.assertEquals(111.1D, f2.doubleValue());
		}
		// String -> BigDecimal
		{
			BigDecimal bd = ConvertUtils.convert(source, BigDecimal.class);
			Assertions.assertEquals(BigDecimal.valueOf(Double.parseDouble(source)), bd);
		}

		//endregion
	}
}
