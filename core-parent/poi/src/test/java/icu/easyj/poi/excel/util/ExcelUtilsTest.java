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
package icu.easyj.poi.excel.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import icu.easyj.core.util.DateUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.poi.excel.util.model.TestClass;
import icu.easyj.web.poi.excel.ExcelExportUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link ExcelUtils} 测试类
 *
 * @author wangliang181230
 */
public class ExcelUtilsTest {

	@Test
	@SuppressWarnings("deprecation")
	public void testToExcelAndToList() throws Exception {
		// list to excel
		List<TestClass> list = new ArrayList<>();
		list.add(new TestClass("aaa", 1, 0, new Date(2020 - 1900, 1 - 1, 1), "desc111", Long.parseLong(Integer.MIN_VALUE + ""), 1.1D, new BigDecimal("1.1")));
		list.add(new TestClass("aaa", 1, 0, new Date(2020 - 1900, 1 - 1, 1), "desc111", Long.parseLong(Integer.MIN_VALUE + ""), 1.1D, new BigDecimal("1.1")));
		list.add(new TestClass("bbb", 2, 1, new Date(2019 - 1900, 2 - 1, 2), "desc222", Long.parseLong(Integer.MAX_VALUE + ""), -1.1D, new BigDecimal("-1.1")));
		list.add(new TestClass("bbb", 2, 2, new Date(2019 - 1900, 2 - 1, 2), "desc222", Long.MIN_VALUE, -Double.MAX_VALUE, new BigDecimal(-Double.MAX_VALUE)));
		list.add(new TestClass("ccc", 3, 2, new Date(2018 - 1900, 3 - 1, 3), "desc333", Long.MIN_VALUE, -Double.MAX_VALUE, new BigDecimal(-Double.MAX_VALUE)));
		list.add(new TestClass("ccc", 3, 2, new Date(2018 - 1900, 3 - 1, 3), "desc333", Long.MIN_VALUE, -Double.MAX_VALUE, new BigDecimal(-Double.MAX_VALUE)));
		list.add(new TestClass("ccc", 3, 2, new Date(2018 - 1900, 3 - 1, 3), "desc333", Long.MIN_VALUE, -Double.MAX_VALUE, new BigDecimal(-Double.MAX_VALUE)));
		list.add(new TestClass("ccc", 3, 2, new Date(2018 - 1900, 3 - 1, 3), "desc333", Long.MIN_VALUE, -Double.MAX_VALUE, new BigDecimal(-Double.MAX_VALUE)));
		list.add(new TestClass("ddd", 4, 3, new Date(2017 - 1900, 4 - 1, 4), "desc444", Long.MAX_VALUE, Double.MAX_VALUE, new BigDecimal(Double.MAX_VALUE)));
		list.add(new TestClass("ddd", 4, 3, new Date(2017 - 1900, 4 - 1, 4), "desc444", Long.MAX_VALUE, Double.MAX_VALUE, new BigDecimal(Double.MAX_VALUE)));
		list.add(new TestClass("ddd", 4, 3, new Date(2017 - 1900, 4 - 1, 4), "desc444", Long.MAX_VALUE, Double.MAX_VALUE, new BigDecimal(Double.MAX_VALUE)));
		list.add(new TestClass("ddd", 4, 3, new Date(2017 - 1900, 4 - 1, 4), "desc444", Long.MAX_VALUE, Double.MAX_VALUE, new BigDecimal(Double.MAX_VALUE)));

		List<TestClass> list2;
		try (Workbook workbook = ExcelUtils.toExcel(list, TestClass.class)) {
			// 如果想看一下导出的excel文件是什么样的，可以放开此注释。执行完测试方法后，去D盘根目录下，找到文件即可。
			ExcelExportUtils.saveToFile(workbook, "D:\\ExcelUtilsTest_" + DateUtils.toSecondsUnsigned(new Date()) + ".xlsx");

			// excel to list
			list2 = ExcelUtils.toList(workbook, TestClass.class, null);
		}

		Assertions.assertEquals(list.size(), list2.size());
		for (int i = 0; i < list.size(); i++) {
			TestClass a = list.get(i);
			TestClass b = list2.get(i);

			// 有注解，比较对应的值
			Assertions.assertEquals(a.getName(), b.getName());
			Assertions.assertEquals(a.getAge(), b.getAge());
			Assertions.assertEquals(a.getbClass().getAge(), b.getbClass().getAge());
			Assertions.assertEquals(a.getBirthday(), b.getBirthday());
			Assertions.assertEquals(a.getTestLong(), b.getTestLong());
			Assertions.assertEquals(a.getTestDouble(), b.getTestDouble());
			Assertions.assertEquals(a.getTestBigDecimal(), b.getTestBigDecimal());


			// 上面的示例数据中，testDouble和testBigDecimal的值故意设置成相同的值了，这里也比较一下
			Assertions.assertEquals(a.getTestDouble(), b.getTestBigDecimal().doubleValue());
			Assertions.assertEquals(a.getTestBigDecimal().doubleValue(), b.getTestDouble());

			// 无注解，不会解析，所以值为空。可以在导出的excel文件中查看效果。
			Assertions.assertNull(b.getDesc());
			a.setDesc(null); // a中，无注解的字段设置为null，方便后面的比较

			// 整体对象转为string后比较
			Assertions.assertEquals(StringUtils.toString(a), StringUtils.toString(b));
		}
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testMapToExcel() throws Exception {
		Map<String, List<?>> map = new HashMap<>();

		{
			// list to excel
			List<TestClass> list = new ArrayList<>();
			list.add(new TestClass("aaa", 1, 0, new Date(2020 - 1900, 1 - 1, 1), "desc111", Long.parseLong(Integer.MIN_VALUE + ""), 1.1D, new BigDecimal("1.1")));
			list.add(new TestClass("bbb", 2, 1, new Date(2019 - 1900, 2 - 1, 2), "desc222", Long.parseLong(Integer.MAX_VALUE + ""), -1.1D, new BigDecimal("-1.1")));
			list.add(new TestClass("ccc", 3, 2, new Date(2018 - 1900, 3 - 1, 3), "desc333", Long.MIN_VALUE, -Double.MAX_VALUE, new BigDecimal(-Double.MAX_VALUE)));
			list.add(new TestClass("ddd", 4, 3, new Date(2017 - 1900, 4 - 1, 4), "desc444", Long.MAX_VALUE, Double.MAX_VALUE, new BigDecimal(Double.MAX_VALUE)));

			map.put("list1", list);
		}
		{
			// list to excel
			List<TestClass> list = new ArrayList<>();
			list.add(new TestClass("eee", 1, 0, new Date(2020 - 1900, 1 - 1, 1), "desc111", Long.parseLong(Integer.MIN_VALUE + ""), 1.1D, new BigDecimal("1.1")));
			list.add(new TestClass("fff", 2, 1, new Date(2019 - 1900, 2 - 1, 2), "desc222", Long.parseLong(Integer.MAX_VALUE + ""), -1.1D, new BigDecimal("-1.1")));
			list.add(new TestClass("ggg", 3, 2, new Date(2018 - 1900, 3 - 1, 3), "desc333", Long.MIN_VALUE, -Double.MAX_VALUE, new BigDecimal(-Double.MAX_VALUE)));
			list.add(new TestClass("hhh", 4, 3, new Date(2017 - 1900, 4 - 1, 4), "desc444", Long.MAX_VALUE, Double.MAX_VALUE, new BigDecimal(Double.MAX_VALUE)));

			map.put("list2", list);
		}

		try (Workbook workbook = ExcelUtils.toExcel(map, TestClass.class)) {
			// 如果想看一下导出的excel文件是什么样的，可以放开此注释。执行完测试方法后，去D盘根目录下，找到文件即可。
			ExcelExportUtils.saveToFile(workbook, "D:\\ExcelUtilsTest2_" + DateUtils.toSecondsUnsigned(new Date()) + ".xlsx");
		}
	}
}
