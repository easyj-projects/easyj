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
package icu.easyj.poi.excel.hook;

import java.util.Map;

import icu.easyj.core.util.StringUtils;
import icu.easyj.poi.excel.annotation.ExcelCustomFirstRowConfig;
import icu.easyj.poi.excel.model.ExcelMapping;
import icu.easyj.poi.excel.util.ExcelCellUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 列表转Excel时提供的勾子抽象类
 *
 * @author wangliang181230
 */
public abstract class AbstractListToExcelHook implements IListToExcelHook {

	@Override
	public void onBeforeCreateHeadRow(Map<Object, Object> context, Sheet sheet, ExcelMapping mapping) {
		// 创建自定义内容的第一行
		this.createCustomFirstRow(context, sheet, mapping);
	}


	//region 创建自定义内容的第一行

	/**
	 * 创建自定义内容的第一行，并合并该所有列（注意：在头行上面的一行）
	 *
	 * @param context 表格所需的上下文
	 * @param sheet   表格
	 * @param mapping 表格映射
	 */
	protected void createCustomFirstRow(Map<Object, Object> context, Sheet sheet, ExcelMapping mapping) {
		String firstRowContent = generateCustomFirstRowContent(context);
		if (StringUtils.isBlank(firstRowContent)) {
			// 没有内容，不创建自定义的第一行
			return;
		}

		int cellCount = mapping.getCellMappingList().size();
		if (mapping.isNeedNumberCell()) {
			cellCount++;
		}

		// 创建行及单元格
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue(firstRowContent);
		// 合并单元格
		CellRangeAddress cra = new CellRangeAddress(0, 0, 0, cellCount - 1);
		sheet.addMergedRegion(cra);

		// 设置样式
		this.setCustomFirstRowStyle(cell, mapping);
	}

	/**
	 * 设置首行样式
	 *
	 * @param cell    首行单元格
	 * @param mapping 表格映射
	 */
	protected void setCustomFirstRowStyle(Cell cell, ExcelMapping mapping) {
		int fontSize = 14;
		boolean fontBold = true;
		int rowHeight = 25;
		HorizontalAlignment align = HorizontalAlignment.CENTER;
		VerticalAlignment verAlign = VerticalAlignment.CENTER;

		ExcelCustomFirstRowConfig configAnno = mapping.getClazz().getAnnotation(ExcelCustomFirstRowConfig.class);
		if (configAnno != null) {
			fontSize = configAnno.fontSize();
			fontBold = configAnno.fontBold();
			rowHeight = configAnno.rowHeight();
			align = ExcelCellUtils.convertAlign(configAnno.align(), align);
			verAlign = ExcelCellUtils.convertVerAlign(configAnno.verAlign(), verAlign);
		}

		// 设置样式
		CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();
		// 设置粗体
		Font font = cell.getSheet().getWorkbook().createFont();
		font.setFontHeight((short)(fontSize * 20));
		font.setColor(HSSFColor.HSSFColorPredefined.TEAL.getIndex()); // 字体颜色：湖蓝色
		if (fontBold) {
			font.setBold(true); // 低版本
//			font.setBoldweight((short) 1000); // 高版本
		}
		cellStyle.setFont(font);
		// 设置居左或居中
		cellStyle.setAlignment(align); // 左右居中
		cellStyle.setVerticalAlignment(verAlign); // 上下居中
		cell.setCellStyle(cellStyle);
		// 设置行高
		cell.getRow().setHeight((short)(rowHeight * 20));
	}

	/**
	 * 生成第一行的内容
	 *
	 * @param context 表格所需数据
	 * @return 生成第一行的内容
	 */
	protected String generateCustomFirstRowContent(Map<Object, Object> context) {
		return null;
	}

	//endregion
}
