package org.easyj.office.excel.util.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.easyj.office.excel.annotation.ExcelCell;

/**
 * @author wangliang181230
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestClass {

	@ExcelCell(headName = "姓名", cellNum = 0)
	private String name;

	@ExcelCell(headName = "年龄", cellNum = 1)
	private Integer age;

	@ExcelCell(headName = "出生日期", cellNum = 2)
	private Date birthday;

	// 测试无注解的情况
	private String desc;
}