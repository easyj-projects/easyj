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
package icu.easyj.poi.excel.util.model;

import java.math.BigDecimal;
import java.util.Date;

import icu.easyj.poi.excel.annotation.Excel;
import icu.easyj.poi.excel.annotation.ExcelCell;
import icu.easyj.poi.excel.annotation.ExcelCustomRowConfig;
import icu.easyj.poi.excel.util.hook.TestListToExcelHook;

/**
 * 测试类
 *
 * @author wangliang181230
 */
@Excel(
		toExcelHookClasses = {TestListToExcelHook.class},
		customFirstRow = @ExcelCustomRowConfig(fontSize = 20, fontBold = false, rowHeight = 40, align = "left", verAlign = "top"),
		showFooterRow = true
)
public class TestClass {

	@ExcelCell(headName = "姓名", cellNum = 0)
	private String name;

	@ExcelCell(headName = "年龄", cellNum = 1)
	private Integer age;

	@ExcelCell(headName = "周岁", cellNum = 2, column = "age")
	private TestBClass bClass;

	@ExcelCell(headName = "出生日期", cellNum = 3, format = "yyyy-MM-dd")
	private Date birthday;

	// 测试无注解的情况
	private String desc;

	@ExcelCell(headName = "测试Long", cellNum = 4, width = 150)
	private Long testLong;

	@ExcelCell(headName = "测试Double", cellNum = 5, width = 200)
	private Double testDouble;

	@ExcelCell(headName = "测试BigDecimal", cellNum = 6, width = 200)
	private BigDecimal testBigDecimal;


	public TestClass() {
	}

	public TestClass(String name, Integer age, Integer age2, Date birthday, String desc, Long testLong, Double testDouble, BigDecimal testBigDecimal) {
		this.name = name;
		this.age = age;
		this.bClass = new TestBClass(null, age2);
		this.birthday = birthday;
		this.desc = desc;
		this.testLong = testLong;
		this.testDouble = testDouble;
		this.testBigDecimal = testBigDecimal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public TestBClass getbClass() {
		return bClass;
	}

	public void setbClass(TestBClass bClass) {
		this.bClass = bClass;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Long getTestLong() {
		return testLong;
	}

	public void setTestLong(Long testLong) {
		this.testLong = testLong;
	}

	public Double getTestDouble() {
		return testDouble;
	}

	public void setTestDouble(Double testDouble) {
		this.testDouble = testDouble;
	}

	public BigDecimal getTestBigDecimal() {
		return testBigDecimal;
	}

	public void setTestBigDecimal(BigDecimal testBigDecimal) {
		this.testBigDecimal = testBigDecimal;
	}
}
