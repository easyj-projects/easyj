/*
 * Copyright 2021-2025 the original author or authors.
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
package icu.easyj.poi.excel.util.hook;

import java.util.Objects;

import icu.easyj.poi.excel.hook.ICellMerger;
import icu.easyj.poi.excel.model.ExcelCellMapping;
import icu.easyj.poi.excel.util.model.TestClass;

public class MyCellMerger implements ICellMerger<TestClass> {

	@Override
	public boolean needMerge(ExcelCellMapping cellMapping, TestClass previous, TestClass current) {
		switch (cellMapping.getColumnFromFieldOrColumn()) {
			case "name":
				return Objects.equals(previous.getName(), current.getName());
			case "age":
				return Objects.equals(previous.getName(), current.getName()) &&
						Objects.equals(previous.getAge(), current.getAge());
			case "bClass.age":
				return Objects.equals(previous.getName(), current.getName()) &&
						Objects.equals(
								previous.getbClass() == null ? null : previous.getbClass().getAge(),
								current.getbClass() == null ? null : current.getbClass().getAge()
						);
			default:
				return false;
		}
	}
}
