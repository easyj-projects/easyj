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

import icu.easyj.web.poi.excel.ExcelExportConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static icu.easyj.spring.boot.autoconfigure.StarterConstants.WEB_POI_EXCEL_EXPORT_PREFIX;

/**
 * Excel导出功能相关的配置
 *
 * @author wangliang181230
 */
@ConfigurationProperties(prefix = WEB_POI_EXCEL_EXPORT_PREFIX)
public class ExcelExporterProperties extends ExcelExportConfig {
}
