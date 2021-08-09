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

import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel转换器（Excel与List互相转换）
 *
 * @author wangliang181230
 */
public interface IExcelConverter {

	/**
	 * 判断是否可以使用当前实现进行转换
	 *
	 * @param clazz 数据类型
	 * @return 是否可以转换
	 */
	boolean isMatch(Class<?> clazz);

	/**
	 * excel转换为列表
	 *
	 * @param inputStream excel文件流
	 * @param clazz       数据类
	 * @param <T>         数据类型
	 * @return list 列表数据
	 * @throws Exception 转换失败
	 */
	<T> List<T> toList(InputStream inputStream, Class<T> clazz) throws Exception;

	/**
	 * excel转换为列表
	 *
	 * @param excelFileBytes 文件byte数组
	 * @param clazz          数据类
	 * @param <T>            数据类型
	 * @return list 列表数据
	 * @throws Exception 转换失败
	 */
	default <T> List<T> toList(byte[] excelFileBytes, Class<T> clazz) throws Exception {
		return toList(new ByteArrayInputStream(excelFileBytes), clazz);
	}

	/**
	 * 列表转换为excel
	 *
	 * @param list  数据列表
	 * @param clazz 数据类
	 * @param <T>   数据类型
	 * @return workbook 返回excel的Workbook实例
	 * @throws Exception 转换失败
	 */
	<T> Workbook toExcel(List<T> list, Class<T> clazz) throws Exception;
}
