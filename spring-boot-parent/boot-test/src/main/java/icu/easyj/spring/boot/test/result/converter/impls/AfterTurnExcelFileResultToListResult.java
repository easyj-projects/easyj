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
package icu.easyj.spring.boot.test.result.converter.impls;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.List;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.util.ReflectionUtils;
import icu.easyj.spring.boot.test.result.converter.IExcelFileResultToListResult;

/**
 * 基于AfterTurn的 {@link IExcelFileResultToListResult} 实现
 *
 * @author wangliang181230
 */
@LoadLevel(name = "afterturn", order = 2, dependOnClasses = {Excel.class, ExcelImportUtil.class})
public class AfterTurnExcelFileResultToListResult implements IExcelFileResultToListResult {

	@Override
	public boolean isMatch(Class<?> clazz) {
		Field[] fields = ReflectionUtils.getAllFields(clazz);
		for (Field field : fields) {
			if (field.getAnnotation(Excel.class) != null) {
				return true;
			}
		}

		return false;
	}

	@Override
	public <T> List<T> convert(byte[] fileBytes, Class<T> clazz) throws Exception {
		return ExcelImportUtil.importExcel(new ByteArrayInputStream(fileBytes), clazz, new ImportParams());
	}
}
