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
package icu.easyj.poi.excel.hook;

import java.util.Map;

import icu.easyj.poi.excel.model.ExcelMapping;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * 列表转Excel时提供的勾子接口
 *
 * @author wangliang181230
 */
public interface IListToExcelHook {

	/**
	 * “创建头行之前” 触发的事件
	 *
	 * @param context 表格所需上下文
	 * @param sheet   表格
	 * @param mapping 表格映射
	 */
	default void onBeforeCreateHeadRow(Map<Object, Object> context, Sheet sheet, ExcelMapping mapping) {
	}

	/**
	 * “创建所有数据行之后” 触发的事件
	 *
	 * @param context 表格所需上下文
	 * @param sheet   表格
	 * @param mapping 表格映射
	 */
	default void onAfterCreateDataRows(Map<Object, Object> context, Sheet sheet, ExcelMapping mapping) {
	}
}
