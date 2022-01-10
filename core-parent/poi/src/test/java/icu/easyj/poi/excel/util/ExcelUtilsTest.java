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
package icu.easyj.poi.excel.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		list.add(new TestClass("aaa", 1, 0, new Date(2020 - 1900, 1 - 1, 1), "desc111"));
		list.add(new TestClass("bbb", 2, 1, new Date(2019 - 1900, 2 - 1, 2), "desc222"));
		list.add(new TestClass("ccc", 3, 2, new Date(2018 - 1900, 3 - 1, 3), "desc333"));

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

			// 无注解，不会解析，所以值为空。可以在导出的excel文件中查看效果。
			Assertions.assertNull(b.getDesc());

			// 整体对象转为string后比较
			a.setDesc(null); // 无注解的字段设置为null
			Assertions.assertEquals(StringUtils.toString(a), StringUtils.toString(b));
		}
	}
}
