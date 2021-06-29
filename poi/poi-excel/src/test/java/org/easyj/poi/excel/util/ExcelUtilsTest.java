package org.easyj.poi.excel.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.easyj.poi.excel.util.model.TestClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link ExcelUtils} 测试类
 *
 * @author wangliang181230
 */
class ExcelUtilsTest {

	@Test
	void testToExcelAndToList() throws Exception {
		// list to excel
		List<TestClass> list = new ArrayList<>();
		list.add(new TestClass("aaa", 1, new Date(2020 - 1900, 1 - 1, 1), "desc111"));
		list.add(new TestClass("bbb", 2, new Date(2019 - 1900, 2 - 1, 2), "desc222"));
		list.add(new TestClass("ccc", 3, new Date(2018 - 1900, 3 - 1, 3), "desc333"));
		Workbook workbook = ExcelUtils.toExcel(list, TestClass.class);

		// 如果想看一下导出的excel文件是什么样的，可以放开此注释。执行完测试方法后，去D盘根目录下，找到文件即可。
		//ExcelExportUtils.saveToFile(workbook, "D:\\ExcelUtilsTest_" + System.currentTimeMillis() + ".xlsx");

		// excel to list
		List<TestClass> list2 = ExcelUtils.toList(workbook, TestClass.class, null, true);
		Assertions.assertEquals(list.size(), list2.size());
		for (int i = 0; i < list.size(); i++) {
			TestClass a = list.get(i);
			TestClass b = list2.get(i);
			Assertions.assertEquals(a.getName(), b.getName());
			Assertions.assertEquals(a.getAge(), b.getAge());
			Assertions.assertEquals(a.getBirthday(), b.getBirthday());

			// 无注解，不会解析，所以值为空。可以在导出的excel文件中查看效果。
			Assertions.assertNull(b.getDesc());
		}
	}
}
