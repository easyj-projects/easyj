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
import icu.easyj.web.poi.excel.exception.ExcelExportException;
import icu.easyj.web.util.HttpUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelExportAspect.class);

	//region 当前切面的启用状态

	private static final ThreadLocal<Boolean> DISABLED_LOCAL = new ThreadLocal<>();

	public static void disable() {
		DISABLED_LOCAL.set(true);
	}

	public static void enable() {
		DISABLED_LOCAL.remove();
	}

	//endregion


	/**
	 * Excel导出器
	 */
	private final IExcelExporter excelExporter;

	/**
	 * Excel导出功能配置
	 */
	private final ExcelExportConfig config;

	/**
	 * 构造函数
	 *
	 * @param excelExporter     Excel导出器
	 * @param excelExportConfig Excel导出功能配置
	 */
	public ExcelExportAspect(IExcelExporter excelExporter, ExcelExportConfig excelExportConfig) {
		Assert.notNull(excelExporter, "excelExporter must be not null");
		Assert.notNull(excelExportConfig, "excelExportConfig must be not null");

		this.excelExporter = excelExporter;
		this.config = excelExportConfig;
	}

	/**
	 * 构造函数
	 *
	 * @param excelExporter Excel导出器
	 */
	public ExcelExportAspect(IExcelExporter excelExporter) {
		this(excelExporter, new ExcelExportConfig());
	}


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

		if (Boolean.TRUE.equals(DISABLED_LOCAL.get())) {
			enable();
			return result;
		}

		if (HttpUtils.isDoExportRequest()) {
			MethodSignature ms = (MethodSignature)jp.getSignature();
			Method method = ms.getMethod();

			Object data = result;
			ExcelExport annotation = method.getAnnotation(ExcelExport.class);

			// 如果返回数据不是列表，并且配置过列表属性名，则从数据的属性中获取列表数据
			if (data != null && !(data instanceof List) && !data.getClass().equals(annotation.dataType())) {
				String listFieldName = StringUtils.findNotEmptyOne(annotation.listFieldName(), config.getListFieldName());
				if (org.springframework.util.StringUtils.hasLength(listFieldName)) {
					try {
						data = ReflectionUtils.getFieldValue(data, listFieldName);
					} catch (NoSuchFieldException e) {
						throw new ExcelExportException("在返回数据中，未找到属性：" + listFieldName +
								"，返回数据类型为：" + data.getClass().getName());
					}
				} else {
					throw new ExcelExportException("返回数据不是列表数据，请在注解上设置或全局配置`listFieldName`，从数据的属性中获取列表数据。");
				}
			}

			if (data == null) {
				// 如果数据为空，则设置为空列表，目的是为了输出一个没有数据，只有标题行的excel文件。
				data = Collections.emptyList();
			} else if (data.getClass().equals(annotation.dataType())) {
				// 如果数据类型与注解中配置的类型一致，则将它包装成列表
				data = Collections.singletonList(data);
			}

			// 准备参数
			HttpServletResponse response = HttpUtils.getResponse();
			List dataList = (List)data;
			Class dataType = annotation.dataType();
			String fileName = HttpUtils.generateExportFileName(annotation.fileNamePre(), FileTypeConstants.EXCEL_2007);

			// 转为excel并导出
			excelExporter.toExcelAndExport(response, dataList, dataType, fileName);

			// 文件导出，无需返回值
			return null;
		}

		return result;
	}
}
