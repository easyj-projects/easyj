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
package icu.easyj.spring.boot.autoconfigure.web.poi.excel.export.excelexporterimpl;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import icu.easyj.web.poi.excel.ExcelExportUtils;
import icu.easyj.web.poi.excel.IExcelExporter;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 基于`cn.afterturn:easypoi`开发的 {@link IExcelExporter} 实现
 *
 * @author wangliang181230
 * @see IExcelExporter
 * @see icu.easyj.web.poi.excel.ExcelExportAspect
 * @see ExcelExportUtil
 */
public class AfterturnExcelExporterImpl implements IExcelExporter {

	@Override
	public <T> void toExcelAndExport(HttpServletResponse response, List<T> dataList, Class<T> clazz, String fileName) throws IOException {
		// 数据转换为excel工作薄
		Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), clazz, dataList);
		// 设置响应头及响应流
		ExcelExportUtils.exportExcel(response, workbook, fileName);
	}
}
