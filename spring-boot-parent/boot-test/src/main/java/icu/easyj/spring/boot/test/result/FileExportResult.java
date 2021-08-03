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
package icu.easyj.spring.boot.test.result;

import java.util.List;

import icu.easyj.spring.boot.test.MockResponse;
import icu.easyj.spring.boot.test.result.converter.ExcelFileResultToListResultUtils;
import icu.easyj.test.exception.TestException;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.web.servlet.ResultActions;

/**
 * 文件导出结果
 *
 * @author wangliang181230
 */
public class FileExportResult extends GenericContentResult<byte[]> {

	/**
	 * 构造函数
	 *
	 * @param mockResponse  模拟响应
	 * @param resultActions 模拟返回操作
	 * @param fileBytes     文件byte数组
	 */
	public FileExportResult(MockResponse mockResponse, ResultActions resultActions, byte[] fileBytes) {
		super(mockResponse, resultActions, fileBytes);
	}


	//region 自定义校验方法

	/**
	 * 判断文件byte数组的长度
	 *
	 * @param expectedLength 预期长度
	 * @return self
	 */
	public FileExportResult is(int expectedLength) {
		Assertions.assertEquals(expectedLength, content.length);
		return this;
	}

	/**
	 * 如果文件是excel，则通过excel工具类，解析为列表数据
	 *
	 * @param expectedClass 数据数据类
	 * @param <T>           列表数据类型
	 * @return listContent 列表响应内容
	 */
	public <T> ListContentResult<T> excelToList(Class<T> expectedClass) {
		List<T> list;
		try {
			list = ExcelFileResultToListResultUtils.toList(this.content, expectedClass);
		} catch (TestException e) {
			throw e;
		} catch (Exception e) {
			throw new TestException("Excel文件转为列表数据失败！", e);
		}
		return new ListContentResult<>(super.mockResponse, resultActions, list);
	}

	//endregion
}
