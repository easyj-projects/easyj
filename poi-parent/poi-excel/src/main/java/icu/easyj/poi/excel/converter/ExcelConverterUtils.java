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
package icu.easyj.poi.excel.converter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import icu.easyj.core.exception.ConvertException;
import icu.easyj.core.exception.ConverterNotFoundException;
import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.core.util.MapUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

/**
 * Excel转换器工具类
 *
 * @author wangliang181230
 */
public abstract class ExcelConverterUtils {

	/**
	 * 所有转换器
	 */
	private static final List<IExcelConverter> CONVERTERS = EnhancedServiceLoader.loadAll(IExcelConverter.class);


	//region 获取类对应的转换器

	/**
	 * 类对应的转换器缓存
	 */
	private static final Map<Class<?>, IExcelConverter> CLASS_CONVERTER_CACHE = new ConcurrentHashMap<>();

	/**
	 * 获取类对应的转换器
	 *
	 * @param clazz 数据类型
	 * @return excelConverter excel转换器
	 * @throws ConverterNotFoundException 转换器不存在或未匹配
	 */
	@NonNull
	public static IExcelConverter getConverter(@NonNull Class<?> clazz) throws ConverterNotFoundException {
		return MapUtils.computeIfAbsent(CLASS_CONVERTER_CACHE, clazz, k -> {
			if (CollectionUtils.isEmpty(CONVERTERS)) {
				throw new ConverterNotFoundException("没有任何Excel转换器可以使用：" + IExcelConverter.class.getName() + "，当前数据类型为：" + clazz.getName());
			}

			for (IExcelConverter converter : CONVERTERS) {
				if (converter.isMatch(clazz)) {
					return converter;
				}
			}

			throw new ConverterNotFoundException("未匹配到合适的Excel转换器，无法进行转换，当前数据类型为：" + clazz.getName());
		});
	}

	//endregion


	//region Excel转换方法（包含：Excel和List互相转换的方法）

	/**
	 * Excel文件转换为列表数据
	 *
	 * @param inputStream Excel文件流
	 * @param clazz       数据类
	 * @param <T>         数据类型
	 * @return list 列表数据
	 * @throws ConverterNotFoundException 转换器不存在 或 未匹配到合适的转换器
	 * @throws ConvertException           转换失败的异常
	 */
	public static <T> List<T> toList(InputStream inputStream, Class<T> clazz) throws ConvertException, ConverterNotFoundException {
		IExcelConverter converter = getConverter(clazz);
		return toList(converter, inputStream, clazz);
	}

	/**
	 * Excel文件转换为列表数据
	 *
	 * @param excelFileBytes Excel文件byte数组
	 * @param clazz          数据类
	 * @param <T>            数据类型
	 * @return list 列表数据
	 * @throws ConverterNotFoundException 转换器不存在 或 未匹配到合适的转换器
	 * @throws ConvertException           转换失败的异常
	 */
	public static <T> List<T> toList(byte[] excelFileBytes, Class<T> clazz) throws ConvertException, ConverterNotFoundException {
		IExcelConverter converter = getConverter(clazz);
		return toList(converter, new ByteArrayInputStream(excelFileBytes), clazz);
	}

	private static <T> List<T> toList(IExcelConverter converter, InputStream inputStream, Class<T> clazz) {
		try {
			return converter.toList(inputStream, clazz);
		} catch (ConvertException e) {
			throw e;
		} catch (Exception e) {
			throw new ConvertException("excel转换为数据列表失败", "EXCEL_TO_LIST_FAILED", e);
		}
	}

	/**
	 * 列表转换为Excel的workbook实例
	 *
	 * @param list  列表数据
	 * @param clazz 数据类
	 * @param <T>   数据类型
	 * @return excel的workbook实例
	 * @throws ConverterNotFoundException 转换器不存在 或 未匹配到合适的转换器
	 * @throws ConvertException           转换失败的异常
	 */
	public static <T> Workbook toExcel(List<T> list, Class<T> clazz) throws ConvertException, ConverterNotFoundException {
		IExcelConverter converter = getConverter(clazz);

		try {
			return converter.toExcel(list, clazz);
		} catch (ConvertException e) {
			throw e;
		} catch (Exception e) {
			throw new ConvertException("excel转换为数据列表失败", "EXCEL_TO_LIST_FAILED", e);
		}
	}

	//endregion
}
