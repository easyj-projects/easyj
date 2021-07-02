/*
 *  Copyright 2021 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package icu.easyj.web.poi.excel;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * 默认的 Excel导出器 实现类
 *
 * @author wangliang181230
 */
public class DefaultExcelExporterImpl implements IExcelExporter {

	@Override
	public <T> void toExcelAndExport(HttpServletResponse response, List<T> dataList, Class<T> clazz, String fileName) throws IOException {
		ExcelExportUtils.toExcelAndExport(response, dataList, clazz, fileName);
	}
}
