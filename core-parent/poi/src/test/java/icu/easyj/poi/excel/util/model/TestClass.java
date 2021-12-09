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
package icu.easyj.poi.excel.util.model;

import java.util.Date;

import icu.easyj.poi.excel.annotation.Excel;
import icu.easyj.poi.excel.annotation.ExcelCell;
import icu.easyj.poi.excel.annotation.ExcelCustomFirstRowConfig;
import icu.easyj.poi.excel.util.hook.TestListToExcelHook;

/**
 * 测试类
 *
 * @author wangliang181230
 */
@Excel(toExcelHookClasses = {TestListToExcelHook.class})
@ExcelCustomFirstRowConfig(fontSize = 20, fontBold = false, rowHeight = 40, align = "left", verAlign = "top")
public class TestClass {

	@ExcelCell(headName = "姓名", cellNum = 0)
	private String name;

	@ExcelCell(headName = "年龄", cellNum = 1)
	private Integer age;

	@ExcelCell(headName = "周岁", cellNum = 2, column = "age")
	private TestBClass bClass;

	@ExcelCell(headName = "出生日期", cellNum = 3)
	private Date birthday;

	// 测试无注解的情况
	private String desc;

	public TestClass() {
	}

	public TestClass(String name, Integer age, Integer age2, Date birthday, String desc) {
		this.name = name;
		this.age = age;
		this.bClass = new TestBClass(null, age2);
		this.birthday = birthday;
		this.desc = desc;
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
}
