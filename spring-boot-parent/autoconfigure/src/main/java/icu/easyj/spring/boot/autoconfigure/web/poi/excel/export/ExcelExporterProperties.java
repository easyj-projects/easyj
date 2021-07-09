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

import icu.easyj.web.poi.excel.IExcelExporter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Excel导出功能相关的配置
 *
 * @author wangliang181230
 */
@ConfigurationProperties(prefix = "easyj.web.excel.export")
public class ExcelExporterProperties {

	/**
	 * Excel导出器类型，可选值：auto（default）、afterturn、easyj
	 *
	 * @see EasyjExcelExporterAutoConfiguration#defaultExcelExporter()
	 * @see AfterturnExcelExporterAutoConfiguration#afterturnExcelExporter()
	 */
	private String type = "auto";

	/**
	 * 全局配置列表属性名
	 *
	 * @see icu.easyj.web.poi.excel.ExcelExportConfig#setListFieldName(String)
	 * @see EasyjExcelExporterAutoConfiguration#excelExportAspect(IExcelExporter, ExcelExporterProperties)
	 */
	private String listFieldName;


	//region Getter、Setter

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getListFieldName() {
		return listFieldName;
	}

	public void setListFieldName(String listFieldName) {
		this.listFieldName = listFieldName;
	}

	//endregion
}
