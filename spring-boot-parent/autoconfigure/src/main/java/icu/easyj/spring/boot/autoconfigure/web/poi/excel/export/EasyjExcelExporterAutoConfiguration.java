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

import icu.easyj.poi.excel.annotation.Excel;
import icu.easyj.web.poi.excel.DefaultExcelExporterImpl;
import icu.easyj.web.poi.excel.ExcelExportAspect;
import icu.easyj.web.poi.excel.ExcelExportConfig;
import icu.easyj.web.poi.excel.IExcelExporter;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * EasyJ-POI-Excel自动装配类
 *
 * @author wangliang181230
 * @see IExcelExporter
 * @see ExcelExportAspect
 */
@ConditionalOnClass(Workbook.class)
@ConditionalOnWebApplication
@EnableConfigurationProperties(ExcelExporterProperties.class)
public class EasyjExcelExporterAutoConfiguration {

	/**
	 * 基于`icu.easyj:easyj-poi-excel`开发的 {@link IExcelExporter} 实现
	 *
	 * @return excelExport Excel导出器
	 * @see DefaultExcelExporterImpl
	 * @see icu.easyj.web.poi.excel.ExcelExportAspect
	 * @see icu.easyj.web.poi.excel.ExcelExportUtils
	 */
	@Bean
	@ConditionalOnClass(Excel.class)
	@ConditionalOnProperty(name = "easyj.web.excel.export.type", havingValue = "easyj", matchIfMissing = true)
	@ConditionalOnMissingBean
	public IExcelExporter defaultExcelExporter() {
		return new DefaultExcelExporterImpl();
	}

	/**
	 * Excel导出切面Bean
	 *
	 * @param excelExporter Excel导出器
	 * @param properties    Excel导出配置
	 * @return excelExportAspect Excel导出切面
	 */
	@Bean
	public ExcelExportAspect excelExportAspect(IExcelExporter excelExporter, ExcelExporterProperties properties) {
		// 创建配置
		ExcelExportConfig config = new ExcelExportConfig();
		config.setListFieldName(properties.getListFieldName());

		// 创建切面
		return new ExcelExportAspect(excelExporter, config);
	}
}
