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

import java.io.ByteArrayInputStream;
import java.util.List;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import icu.easyj.core.util.ClassUtils;
import icu.easyj.poi.excel.annotation.Excel;
import icu.easyj.poi.excel.util.ExcelUtils;
import icu.easyj.spring.boot.test.MockResponse;
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
			if (ClassUtils.isExist("icu.easyj.poi.excel.annotation.Excel") && expectedClass.getAnnotation(Excel.class) != null) {
				list = ExcelUtils.toList(new ByteArrayInputStream(this.content), expectedClass, null);
			} else if (ClassUtils.isExist("cn.afterturn.easypoi.excel.ExcelImportUtil")) {
				list = ExcelImportUtil.importExcel(new ByteArrayInputStream(this.content), expectedClass, new ImportParams());
			} else {
				throw new TestException("当前未找到适合`" + expectedClass.getName() + "`类的Excel解析工具，请确定引用了相关依赖。");
			}
		} catch (TestException e) {
			throw e;
		} catch (Exception e) {
			throw new TestException("文件转列表失败！", e);
		}
		return new ListContentResult<>(super.mockResponse, resultActions, list);
	}

	//endregion
}
