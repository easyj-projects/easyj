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

import cn.afterturn.easypoi.excel.annotation.Excel;
import icu.easyj.spring.boot.autoconfigure.web.poi.excel.export.excelexporterimpl.AfterturnExcelExporterImpl;
import icu.easyj.web.poi.excel.IExcelExporter;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * Afterturn-EasyPOI-Excel自动装配类
 *
 * @author wangliang181230
 */
@ConditionalOnClass({Workbook.class, Excel.class})
@ConditionalOnWebApplication
@AutoConfigureBefore(EasyjExcelExporterAutoConfiguration.class)
public class AfterturnExcelExporterAutoConfiguration {

	/**
	 * 基于`cn.afterturn:easypoi`开发的 {@link IExcelExporter} 实现
	 *
	 * @return excelExport Excel导出器
	 * @see IExcelExporter
	 * @see icu.easyj.web.poi.excel.ExcelExportAspect
	 * @see cn.afterturn.easypoi.excel.ExcelExportUtil
	 */
	@Bean
	@ConditionalOnProperty(name = "easyj.web.excel.export.type", havingValue = "afterturn", matchIfMissing = true)
	@ConditionalOnMissingBean
	public IExcelExporter afterturnExcelExporter() {
		return new AfterturnExcelExporterImpl();
	}
}
