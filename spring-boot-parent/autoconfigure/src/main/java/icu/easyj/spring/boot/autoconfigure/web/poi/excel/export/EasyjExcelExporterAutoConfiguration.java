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
import org.apache.poi.ss.usermodel.Workbook;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * EasyJ-POI-Excel自动装配类
 *
 * @author wangliang181230
 * @see ExcelExportAspect
 */
@ConditionalOnClass({Workbook.class, ProceedingJoinPoint.class, ExcelExport.class})
@ConditionalOnWebApplication
@EnableConfigurationProperties(ExcelExporterProperties.class)
public class EasyjExcelExporterAutoConfiguration {

	/**
	 * Excel导出切面Bean
	 *
	 * @param properties Excel导出配置
	 * @return excelExportAspect Excel导出切面
	 */
	@Bean
	public ExcelExportAspect excelExportAspect(ExcelExporterProperties properties) {
		return new ExcelExportAspect(properties);
	}
}
