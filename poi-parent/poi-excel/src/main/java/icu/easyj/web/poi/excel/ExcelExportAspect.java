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
package icu.easyj.web.poi.excel;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import icu.easyj.core.constant.FileTypeConstants;
import icu.easyj.core.util.ReflectionUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.poi.excel.converter.ExcelConverterUtils;
import icu.easyj.web.poi.excel.exception.ExcelExportException;
import icu.easyj.web.util.HttpUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

/**
 * Excel文件导出切面类
 *
 * @author wangliang181230
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExcelExportAspect {

	/**
	 * Excel导出功能配置
	 */
	private final ExcelExportConfig config;

	/**
	 * 构造函数
	 *
	 * @param excelExportConfig Excel导出功能配置
	 */
	public ExcelExportAspect(ExcelExportConfig excelExportConfig) {
		Assert.notNull(excelExportConfig, "'excelExportConfig' must be not null");

		this.config = excelExportConfig;
	}


	/**
	 * 切入点为注解
	 */
	@Pointcut("@annotation(icu.easyj.web.poi.excel.ExcelExport)")
	private void pointcutExcelExport() {
	}

	/**
	 * 执行查询，然后导出excel文件
	 *
	 * @param jp 连接点
	 * @return 返回查询结果
	 * @throws Throwable 异常
	 */
	@Around("pointcutExcelExport()")
	public Object doQueryAndExportExcel(ProceedingJoinPoint jp) throws Throwable {
		Object result = jp.proceed();

		if (HttpUtils.isDoExportRequest()) {
			MethodSignature ms = (MethodSignature)jp.getSignature();
			Method method = ms.getMethod();

			ExcelExport annotation = method.getAnnotation(ExcelExport.class);

			// 如果返回数据不是列表，并且配置过列表属性名，则从数据的属性中获取列表数据
			if (result != null && !(result instanceof List) && !result.getClass().equals(annotation.dataType())) {
				String listFieldName = StringUtils.findNotEmptyOne(annotation.listFieldName(), config.getListFieldName());
				if (StringUtils.isNotEmpty(listFieldName)) {
					try {
						result = ReflectionUtils.getFieldValue(result, listFieldName);
					} catch (NoSuchFieldException e) {
						String errorMsg = "在返回数据的类型中，未找到属性：" + listFieldName + "，返回数据类型为：" + result.getClass().getName();
						throw new ExcelExportException(errorMsg, "NO_SUCH_FIELD");
					}
				} else {
					throw new ExcelExportException("返回数据不是列表数据，请在注解上设置`listFieldName`或全局配置`easyj.web.poi.excel.export.list-field-name`，从数据的属性中获取列表数据。",
							"NO_CONFIG");
				}
			}

			if (result == null) {
				// 如果数据为空，则设置为空列表，目的是为了输出一个没有数据，只有标题行的excel文件。可作为导出模板功能使用。
				result = Collections.emptyList();
			} else if (result.getClass().equals(annotation.dataType())) {
				// 如果数据类型与注解中配置的类型一致，则将它包装成列表
				result = Collections.singletonList(result);
			}

			// 数据转换为excel工作薄
			List dataList = (List)result;
			Class dataType = annotation.dataType();
			Workbook workbook = ExcelConverterUtils.toExcel(dataList, dataType);

			// 设置响应头及响应流
			HttpServletResponse response = HttpUtils.getResponse();
			String fileName = HttpUtils.generateExportFileName(annotation.fileNamePre(), FileTypeConstants.EXCEL_2007);
			ExcelExportUtils.exportExcel(response, workbook, fileName);

			// 设为null，方便GC回收
			result = null;
			dataList = null;
			dataType = null;
			fileName = null;

			// 文件导出，无需返回值
			return null;
		}

		return result;
	}
}
