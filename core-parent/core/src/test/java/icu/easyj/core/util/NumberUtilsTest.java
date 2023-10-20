package icu.easyj.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link NumberUtils} 测试类
 *
 * @author wangliang181230
 */
public class NumberUtilsTest {

	/**
	 * @see NumberUtils#convertScientificToNormal(String)
	 */
	@Test
	public void test_convertScientificToNormal() {
		Assertions.assertEquals("123456789", NumberUtils.convertScientificToNormal("1.23456789E8"));
		Assertions.assertEquals("123456789.1", NumberUtils.convertScientificToNormal("1.234567891E8"));
		Assertions.assertEquals("123456789.12", NumberUtils.convertScientificToNormal("1.2345678912E8"));

		Assertions.assertEquals("1234567890", NumberUtils.convertScientificToNormal("1.23456789E9"));
		Assertions.assertEquals("1234567891", NumberUtils.convertScientificToNormal("1.234567891E9"));
		Assertions.assertEquals("1234567891.2", NumberUtils.convertScientificToNormal("1.2345678912E9"));

		Assertions.assertEquals("12345678900", NumberUtils.convertScientificToNormal("1.23456789E10"));
		Assertions.assertEquals("12345678910", NumberUtils.convertScientificToNormal("1.234567891E10"));
		Assertions.assertEquals("12345678912", NumberUtils.convertScientificToNormal("1.2345678912E10"));

		Assertions.assertEquals("123456789000", NumberUtils.convertScientificToNormal("1.23456789E11"));
		Assertions.assertEquals("123456789100", NumberUtils.convertScientificToNormal("1.234567891E11"));
		Assertions.assertEquals("123456789120", NumberUtils.convertScientificToNormal("1.2345678912E11"));
	}

	/**
	 * @see NumberUtils#convertScientificToNormal(String)
	 */
	@Test
	public void test_toString() {
		Assertions.assertEquals("123456789", NumberUtils.doubleToString(123456789D));
		Assertions.assertEquals("123456789.1", NumberUtils.doubleToString(123456789.1D));
		Assertions.assertEquals("123456789.12", NumberUtils.doubleToString(123456789.120D));
	}

}
