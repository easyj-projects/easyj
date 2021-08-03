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

import icu.easyj.spring.boot.test.result.FileExportResult;
import icu.easyj.spring.boot.test.result.ListContentResult;

/**
 * Excel类型的 {@link FileExportResult} 转换为 {@link ListContentResult} 的接口
 *
 * @author wangliang181230
 */
public interface IExcelFileResultToListResult {

	/**
	 * 判断是否可以使用该当前实现进行转换
	 *
	 * @param clazz 数据类型
	 * @return 是否可以转换
	 */
	boolean isMatch(Class<?> clazz);

	/**
	 * 转换为列表
	 *
	 * @param fileBytes 文件byte数组
	 * @param clazz     数据类
	 * @param <T>       数据类型
	 * @return list 列表数据
	 */
	<T> List<T> convert(byte[] fileBytes, Class<T> clazz) throws Exception;
}
