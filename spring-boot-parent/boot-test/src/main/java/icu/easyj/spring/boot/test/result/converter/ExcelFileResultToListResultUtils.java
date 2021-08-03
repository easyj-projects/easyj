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
package icu.easyj.spring.boot.test.result.converter;

import java.util.List;

import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.spring.boot.test.result.FileExportResult;
import icu.easyj.spring.boot.test.result.ListContentResult;
import icu.easyj.test.exception.TestException;

/**
 * Excel类型的 {@link FileExportResult} 转换为 {@link ListContentResult} 的工具类
 *
 * @author wangliang181230
 */
public class ExcelFileResultToListResultUtils {

	/**
	 * 所有转换器
	 */
	private static final List<IExcelFileResultToListResult> CONVERTERS = EnhancedServiceLoader.loadAll(IExcelFileResultToListResult.class);

	/**
	 * Excel文件转换为列表数据
	 *
	 * @param fileBytes Excel文件byte数组
	 * @param clazz     数据类
	 * @param <T>       数据类型
	 * @return list 列表数据
	 * @throws Exception     转换异常
	 * @throws TestException 转换器未找到 或 未匹配到合适的转换器
	 */
	public static <T> List<T> toList(byte[] fileBytes, Class<T> clazz) throws Exception {
		if (CONVERTERS == null || CONVERTERS.isEmpty()) {
			throw new TestException("未找到Excel转换器：" + IExcelFileResultToListResult.class.getName());
		}

		for (IExcelFileResultToListResult converter : CONVERTERS) {
			if (converter.isMatch(clazz)) {
				return converter.convert(fileBytes, clazz);
			}
		}

		throw new TestException("未匹配到适合`" + clazz.getName() + "`类的Excel转换器，请确定引用了相关依赖，并添加了相关注解。");
	}
}
