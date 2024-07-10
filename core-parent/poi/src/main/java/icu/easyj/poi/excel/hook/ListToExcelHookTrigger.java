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
package icu.easyj.poi.excel.hook;

import java.util.List;

import icu.easyj.core.util.CollectionUtils;
import icu.easyj.poi.excel.model.ExcelMapping;
import icu.easyj.poi.excel.util.ExcelContext;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * 列表转Excel的勾子触发器
 *
 * @author wangliang181230
 */
public abstract class ListToExcelHookTrigger {

	/**
	 * 触发 创建头行之前 的事件
	 *
	 * @param sheet   表格
	 * @param mapping 表格映射
	 */
	public static void onBeforeCreateHeadRow(Sheet sheet, ExcelMapping mapping) {
		List<IListToExcelHook> hookList = mapping.getToExcelHookList();
		if (CollectionUtils.isEmpty(hookList)) {
			return;
		}
		for (IListToExcelHook hook : hookList) {
			hook.onBeforeCreateHeadRow(ExcelContext.get(), sheet, mapping);
		}
	}

	/**
	 * 触发 创建所有数据行之后 的事件
	 *
	 * @param sheet   表格
	 * @param mapping 表格映射
	 */
	public static void onAfterCreateDataRows(Sheet sheet, ExcelMapping mapping) {
		List<IListToExcelHook> hookList = mapping.getToExcelHookList();
		if (CollectionUtils.isEmpty(hookList)) {
			return;
		}
		for (IListToExcelHook hook : hookList) {
			hook.onAfterCreateDataRows(ExcelContext.get(), sheet, mapping);
		}
	}
}
