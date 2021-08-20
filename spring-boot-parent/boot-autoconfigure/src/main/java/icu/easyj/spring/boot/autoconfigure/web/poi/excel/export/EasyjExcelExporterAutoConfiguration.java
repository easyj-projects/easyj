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
package icu.easyj.spring.boot.autoconfigure.web.poi.excel.export;

import icu.easyj.web.poi.excel.ExcelExport;
import icu.easyj.web.poi.excel.ExcelExportAspect;
import icu.easyj.web.poi.excel.ExcelExportConfig;
import org.apache.poi.ss.usermodel.Workbook;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import static icu.easyj.spring.boot.autoconfigure.StarterConstants.WEB_POI_EXCEL_EXPORT_PREFIX;

/**
 * EasyJ-POI-Excel自动装配类
 *
 * @author wangliang181230
 * @see ExcelExportAspect
 */
@ConditionalOnClass({Workbook.class, ProceedingJoinPoint.class, ExcelExport.class})
@ConditionalOnWebApplication
public class EasyjExcelExporterAutoConfiguration {

	/**
	 * @return Excel导出配置
	 */
	@Bean
	@ConfigurationProperties(prefix = WEB_POI_EXCEL_EXPORT_PREFIX)
	public ExcelExportConfig excelExportConfig() {
		return new ExcelExportConfig();
	}

	/**
	 * Excel导出切面Bean
	 *
	 * @param excelExportConfig Excel导出配置
	 * @return excelExportAspect Excel导出切面
	 */
	@Bean
	public ExcelExportAspect excelExportAspect(ExcelExportConfig excelExportConfig) {
		return new ExcelExportAspect(excelExportConfig);
	}
}
