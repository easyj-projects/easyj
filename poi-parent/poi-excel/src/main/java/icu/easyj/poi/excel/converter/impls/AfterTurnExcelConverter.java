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
package icu.easyj.poi.excel.converter.impls;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnClass;
import icu.easyj.core.util.ReflectionUtils;
import icu.easyj.poi.excel.converter.IExcelConverter;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 基于AfterTurn的 {@link IExcelConverter} 实现
 *
 * @author wangliang181230
 */
@LoadLevel(name = "afterturn", order = 2)
@DependsOnClass({cn.afterturn.easypoi.excel.annotation.Excel.class, ExcelImportUtil.class})
public class AfterTurnExcelConverter implements IExcelConverter {

	@Override
	public boolean isMatch(Class<?> clazz) {
		Field[] fields = ReflectionUtils.getAllFields(clazz);
		for (Field field : fields) {
			if (field.getAnnotation(cn.afterturn.easypoi.excel.annotation.Excel.class) != null) {
				return true;
			}
		}

		return false;
	}

	@Override
	public <T> List<T> toList(InputStream inputStream, Class<T> clazz) throws Exception {
		return ExcelImportUtil.importExcel(inputStream, clazz, new ImportParams());
	}

	@Override
	public <T> Workbook toExcel(List<T> list, Class<T> clazz) throws Exception {
		return ExcelExportUtil.exportExcel(new ExportParams(), clazz, list);
	}
}
