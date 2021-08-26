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

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.util.ReflectionUtils;
import icu.easyj.poi.excel.annotation.Excel;
import icu.easyj.poi.excel.annotation.ExcelCell;
import icu.easyj.poi.excel.converter.IExcelConverter;
import icu.easyj.poi.excel.util.ExcelUtils;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 基于EasyJ的 {@link IExcelConverter} 实现
 *
 * @author wangliang181230
 */
@LoadLevel(name = "easyj", order = 1, dependOnClasses = {Excel.class})
public class EasyjExcelConverter implements IExcelConverter {

	@Override
	public boolean isMatch(Class<?> clazz) {
		// 如果类上有`@Excel`注解，则匹配成功
		if (clazz.getAnnotation(Excel.class) != null) {
			return true;
		}

		// 如果存在一个属性有`@ExcelCell`注解，则匹配成功
		Field[] fields = ReflectionUtils.getAllFields(clazz);
		for (Field field : fields) {
			if (field.getAnnotation(ExcelCell.class) != null) {
				return true;
			}
		}

		return false;
	}

	@Override
	public <T> List<T> toList(InputStream inputStream, Class<T> clazz) throws Exception {
		return ExcelUtils.toList(inputStream, clazz, null);
	}

	@Override
	public <T> Workbook toExcel(List<T> list, Class<T> clazz) throws Exception {
		return ExcelUtils.toExcel(list, clazz);
	}
}
