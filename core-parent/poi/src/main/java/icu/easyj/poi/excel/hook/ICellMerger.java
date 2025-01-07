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
package icu.easyj.poi.excel.hook;

import icu.easyj.poi.excel.model.ExcelCellMapping;

public interface ICellMerger<T> {

	/**
	 * 是否合并单元格
	 *
	 * @param cellMapping 当前单元格
	 * @param previous    上一条数据
	 * @param current     当前数据
	 * @return 是否合并：true=合并 | false=不合并
	 */
	boolean needMerge(ExcelCellMapping cellMapping, T previous, T current);

}
