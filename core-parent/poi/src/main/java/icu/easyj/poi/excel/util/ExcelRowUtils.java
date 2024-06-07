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
package icu.easyj.poi.excel.util;

import java.util.List;

import icu.easyj.core.util.ReflectionUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.poi.excel.model.ExcelCellMapping;
import icu.easyj.poi.excel.model.ExcelMapping;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

/**
 * Excel行 工具类
 *
 * @author wangliang181230
 */
public abstract class ExcelRowUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelRowUtils.class);

	/**
	 * 行数据转换为映射的类对象
	 *
	 * @param row           行
	 * @param hasNumberCell 是否有序号列
	 * @param headRow       头行
	 * @param clazz         数据类
	 * @param mapping       表格映射
	 * @param <T>           数据类型
	 * @return rowObj 行数据对象
	 * @throws Exception 异常
	 */
	@NonNull
	public static <T> T rowToObject(Row row, boolean hasNumberCell, Row headRow, Class<T> clazz, ExcelMapping mapping) throws Exception {
		try {
			T t = clazz.newInstance();

			int cellStart = row.getFirstCellNum();
			int cellEnd = row.getLastCellNum();

			Cell cell;
			Object value;
			for (ExcelCellMapping cellMapping : mapping.getCellMappingList()) {
				int cellNum = cellMapping.getCellNum();
				if (hasNumberCell) {
					cellNum += 1;
				}

				// 判断列号是否正确，如果不正确，自动根据头行与映射信息配置的头行进行匹配
				if (headRow != null) {
					cellNum = getCellNumByHead(headRow, cellNum, cellMapping);
					if (cellNum == -1) {
						continue;
					}
				}

				if (cellNum < cellStart || cellNum >= cellEnd) {
					continue;
				}

				// 获取单元格
				cell = row.getCell(cellNum);
				if (ExcelCellUtils.isEmptyCell(cell)) {
					continue;
				}

				// 获取单元格的值
				value = ExcelCellUtils.getCellValue(cell, cellMapping);
				if (value == null || value.toString().trim().isEmpty()) {
					continue;
				}

				// 设置值到对象中
				if (StringUtils.isEmpty(cellMapping.getColumn())) {
					// 单层属性的值设置
					ReflectionUtils.setFieldValue(t, cellMapping.getField(), value);
				} else {
					// 多层属性的值设置
					ReflectionUtils.setFieldValue(t, cellMapping.getColumn(), value);
				}
			}

			return t;
		} catch (Exception e) {
			LOGGER.error("excel的行转换为对象时异常：--------\r\n{}\r\n-----------------------------------", e.getMessage());
			throw e;
		}
	}

	/**
	 * 根据头行获取当前列号
	 *
	 * @param headRow     头行
	 * @param cellNum     列号
	 * @param cellMapping 列映射
	 * @return cellNum 列号
	 */
	public static int getCellNumByHead(Row headRow, int cellNum, ExcelCellMapping cellMapping) {
		// 判断当前列号是否正确
		Cell cell = headRow.getCell(cellNum);
		Object value;
		if (cell != null) {
			// 如果当前列头与配置的列头一致，直接返回当前列号
			value = ExcelCellUtils.getCellValue(cell);
			if (value != null && value.toString().trim().equals(cellMapping.getHeadName())) {
				return cellNum;
			}
		}

		// 循环查找头列，如与配置的列头名称一致，就返回该列号
		for (int i = 0; i < headRow.getLastCellNum(); i++) {
			cell = headRow.getCell(i);
			if (cell == null) {
				continue;
			}
			value = ExcelCellUtils.getCellValue(cell);
			if (value != null && value.toString().trim().equals(cellMapping.getHeadName())) {
				return i;
			}
		}

		return -1; // 当前行列头不正确，并且也未匹配到任何列
	}

	/**
	 * 根据映射信息，判断该行是否为头行
	 *
	 * @param row     行
	 * @param mapping 表格映射
	 * @return isHeadRow 是否为头行
	 */
	public static boolean isHeadRow(Row row, ExcelMapping mapping) {
		int cellStart = row.getFirstCellNum();
		int cellEnd = row.getLastCellNum();

		Cell cell;
		Object value;
		for (ExcelCellMapping em : mapping.getCellMappingList()) {
			if (em.getCellNum() < cellStart || em.getCellNum() >= cellEnd || em.getHeadName() == null || em.getHeadName().isEmpty()) {
				continue;
			}

			cell = row.getCell(em.getCellNum());
			if (cell == null) {
				continue;
			}

			value = ExcelCellUtils.getCellValue(cell);
			if (value == null || value.toString().isEmpty()) {
				continue;
			}

			if ("序号".equals(value.toString()) || value.toString().equals(em.getHeadName()) || value.toString().equals(mapping.getNumberCellHeadName())) {
				// 匹配到一个单元格与映射中的头名称一致，表示这一行的确是头行
				return true;
			}
		}

		return false;
	}

	/**
	 * 判断是否为空行
	 *
	 * @param row 行
	 * @return isEmptyRow 是否为空行
	 */
	public static boolean isEmptyRow(Row row) {
		if (row == null) {
			return true;
		}

		int cellStart = row.getFirstCellNum();
		int cellEnd = row.getLastCellNum();

		Cell cell;
		for (int i = cellStart; i <= cellEnd; i++) {
			try {
				cell = row.getCell(i);
				if (!ExcelCellUtils.isEmptyCell(cell)) { // 判断单元格是否为空
					return false; // 单元格不为空，行就不为空
				}
			} catch (Exception ignore) {
			}
		}

		return true; // 是空行
	}

	/**
	 * 创建头行
	 *
	 * @param sheet   表格
	 * @param mapping 表格映射
	 */
	public static void createHeadRow(Sheet sheet, ExcelMapping mapping) {
		if (!mapping.isNeedHeadRow()) {
			return; // 不需要创建头行
		}

		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
		// 设置粗体
		Font font = sheet.getWorkbook().createFont();
		font.setColor(ExcelColorUtils.TEAL_INDEX); // 字体颜色：湖蓝色
		font.setBold(true); // 低版本
//		font.setBoldweight((short) 1000); // 高版本
		cellStyle.setFont(font);
		// 设置居左或居中
		if (mapping.isNeedFilter()) {
			cellStyle.setAlignment(HorizontalAlignment.LEFT); // 如果开启了筛选功能，则head居左
		} else {
			cellStyle.setAlignment(HorizontalAlignment.CENTER); // 如果未开启筛选功能，则head居中
		}

		// 创建行
		Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
		int cellNum = 0; // 标记当前列号

		// 创建序号列头
		Cell cell;
		if (mapping.isNeedNumberCell()) {
			cell = row.createCell(cellNum++);
			cell.setCellValue(mapping.getNumberCellHeadName());
			cell.setCellStyle(cellStyle);
		}

		// 得到一个POI的工具类
		CreationHelper factory = sheet.getWorkbook().getCreationHelper();
		// 得到一个换图的对象
		Drawing drawing = sheet.createDrawingPatriarch();
		// ClientAnchor是附属在WorkSheet上的一个对象，  其固定在一个单元格的左上角和右下角.
		ClientAnchor anchor = factory.createClientAnchor();

		// 创建数据的列头
		CellStyle cs;
		for (ExcelCellMapping cm : mapping.getCellMappingList()) {
			// 创建单元格
			cell = row.createCell(cellNum++);
			// 设置单元格的值
			cell.setCellValue(cm.getHeadName());

			// 设置单元格的样式
			cs = cell.getCellStyle();
			if (cs != null) {
				cs = sheet.getWorkbook().createCellStyle();
				cs.cloneStyleFrom(cell.getCellStyle());
				cs.setFont(font);
				cs.setAlignment(ExcelCellUtils.getCellStyleAlignment(cellStyle));
			} else {
				cs = cellStyle;
			}
			cell.setCellStyle(cs);

			// 添加注释
			if (StringUtils.isNotBlank(cm.getHeadComment())) {
				Comment comment1 = drawing.createCellComment(anchor);
				RichTextString str1 = factory.createRichTextString(cm.getHeadComment());
				comment1.setString(str1);
				cell.setCellComment(comment1);
			}
		}
	}

	/**
	 * 创建数据行
	 *
	 * @param sheet    表格
	 * @param dataList 数据列表
	 * @param mapping  表格映射
	 */
	public static void createDataRows(Sheet sheet, List<?> dataList, ExcelMapping mapping) {
		int rowNum = sheet.getPhysicalNumberOfRows(); // 开始行号
		int cellNum; // 当前列号
		int number = 1; // 序号

		Row row; // 行
		Cell cell; // 列
		for (Object data : dataList) {
			cellNum = 0;

			// 创建行
			row = sheet.createRow(rowNum++);
			// 创建序号列
			if (mapping.isNeedNumberCell()) { // 判断是否需要序号列
				cell = row.createCell(cellNum++);
				cell.setCellValue(number++);
			}
			// 循环创建各数据列
			for (ExcelCellMapping cellMapping : mapping.getCellMappingList()) {
				if (cellMapping.getField() == null) {
					continue;
				}

				// 创建数据列
				cell = row.createCell(cellNum++);
				// 设置单元格的值
				try {
					ExcelCellUtils.setCellValue(cell, data, cellMapping);
				} catch (Exception e) {
					LOGGER.error("设置列“" + cellMapping.getHeadName() + "”的信息失败：" + e.getMessage(), e);
				}
			}
		}
	}
}
