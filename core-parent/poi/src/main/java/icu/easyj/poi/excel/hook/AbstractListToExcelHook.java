/*
 * Copyright 2021-2022 the original author or authors.
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

import java.util.List;
import java.util.Map;

import icu.easyj.core.util.StringUtils;
import icu.easyj.poi.excel.annotation.ExcelCustomRowConfig;
import icu.easyj.poi.excel.model.ExcelMapping;
import icu.easyj.poi.excel.util.ExcelCellUtils;
import icu.easyj.poi.excel.util.ExcelColorUtils;
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
		// 创建自定义内容的首行
		this.createCustomFirstRow(context, sheet, mapping);
	}

	@Override
	public void onAfterCreateDataRows(Map<Object, Object> context, Sheet sheet, ExcelMapping mapping) {
		if (mapping.getAnno().showFooterRow()) {
			// 创建自定义内容的尾行
			this.createCustomFooterRow(context, sheet, mapping);
		}
	}

	//region 创建自定义内容的首行

	/**
	 * 创建自定义内容的首行，并合并该所有列（注意：在头行上面的一行）
	 *
	 * @param context 表格所需的上下文
	 * @param sheet   表格
	 * @param mapping 表格映射
	 */
	protected void createCustomFirstRow(Map<Object, Object> context, Sheet sheet, ExcelMapping mapping) {
		String firstRowContent = generateCustomFirstRowContent(context);
		if (StringUtils.isBlank(firstRowContent)) {
			// 没有内容，不创建自定义的首行
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
		CellRangeAddress cra = new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, cellCount - 1);
		sheet.addMergedRegion(cra);

		// 设置样式
		this.setCustomRowStyle(cell, mapping.getAnno().customFirstRow());
	}

	/**
	 * 生成首行的内容
	 *
	 * @param context 表格所需的上下文
	 * @return 生成首行的内容
	 */
	protected String generateCustomFirstRowContent(Map<Object, Object> context) {
		return null;
	}

	//endregion


	//region 创建自定义内容的尾行

	/**
	 * 创建自定义内容的尾行，并合并该所有列
	 *
	 * @param context 表格所需的上下文
	 * @param sheet   表格
	 * @param mapping 表格映射
	 */
	protected void createCustomFooterRow(Map<Object, Object> context, Sheet sheet, ExcelMapping mapping) {
		String footerRowContent = generateCustomFooterRowContent(context);
		if (StringUtils.isBlank(footerRowContent)) {
			// 没有内容，不创建自定义的尾行
			return;
		}

		int cellCount = mapping.getCellMappingList().size();
		if (mapping.isNeedNumberCell()) {
			cellCount++;
		}

		// 创建行及单元格
		Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
		Cell cell = row.createCell(0);
		cell.setCellValue(footerRowContent);
		// 合并单元格
		CellRangeAddress cra = new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, cellCount - 1);
		sheet.addMergedRegion(cra);

		// 设置样式
		this.setCustomRowStyle(cell, mapping.getAnno().customFooterRow());
	}

	/**
	 * 生成尾行的内容
	 *
	 * @param context 表格所需的上下文
	 * @return 生成尾行的内容
	 */
	protected String generateCustomFooterRowContent(Map<Object, Object> context) {
		List dataList = (List)context.get("dataList");
		return "总记录数：" + dataList.size();
	}

	//endregion


	/**
	 * 设置自定义行样式
	 *
	 * @param cell       首行单元格
	 * @param configAnno 表格映射
	 */
	protected void setCustomRowStyle(Cell cell, ExcelCustomRowConfig configAnno) {
		int fontSize = configAnno.fontSize();
		boolean fontBold = configAnno.fontBold();
		int rowHeight = configAnno.rowHeight();
		HorizontalAlignment align = ExcelCellUtils.convertAlign(configAnno.align(), HorizontalAlignment.CENTER);
		VerticalAlignment verAlign = ExcelCellUtils.convertVerAlign(configAnno.verAlign(), VerticalAlignment.CENTER);

		// 设置样式
		CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();
		// 设置粗体
		Font font = cell.getSheet().getWorkbook().createFont();
		font.setFontHeight((short)(fontSize * 20));
		font.setColor(ExcelColorUtils.TEAL_INDEX); // 字体颜色：湖蓝色
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
}
