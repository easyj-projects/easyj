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
package icu.easyj.poi.excel.util;

import java.math.BigDecimal;
import java.util.Date;

import icu.easyj.core.convert.ConvertUtils;
import icu.easyj.core.util.ReflectionUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.poi.excel.model.ExcelCellMapping;
import icu.easyj.poi.excel.model.ExcelMapping;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 获取单元格的值的 工具类
 *
 * @author wangliang181230
 */
public abstract class ExcelCellUtils {

	/**
	 * 判断是否为空单元格
	 *
	 * @param cell 单元格
	 * @return isEmptyCell是否为空单元格
	 */
	public static boolean isEmptyCell(Cell cell) {
		if (cell == null) {
			return true;
		}

		String str;
		switch (cell.getCellType()) {
			// 返回值为字符串的两种，需要验证值
			case STRING: // 字符串
				str = cell.getStringCellValue();
				if (StringUtils.isNotBlank(str)) {
					return false;
				}
				break;
			case FORMULA: // 公式
				str = cell.getCellFormula();
				if (StringUtils.isNotBlank(str)) {
					return false;
				}
				break;

			// 这几种格式的肯定不会是空值
			case NUMERIC: // 数字
			case BOOLEAN: // Boolean
				return false;

			// 其他的，均算作空值
			case BLANK: // 空值
			case ERROR: // 故障
			case _NONE: // 空
			default:
				return true;
		}
		return true;
	}

	/**
	 * 获取单元格的值
	 *
	 * @param cell 单元格
	 * @return cellValue 单元格的值
	 */
	public static Object getCellValue(Cell cell) {
		switch (cell.getCellType()) {
			case STRING: // 字符串
				return cell.getStringCellValue().trim();
			case NUMERIC: // 数字
				return cell.getNumericCellValue();
			case FORMULA: // 公式
				return cell.getCellFormula().trim();
			case BOOLEAN: // Boolean
				return cell.getBooleanCellValue();

			// 其他的，均算作空值
//			case CellType.BLANK: // 空值
//			case CellType.ERROR: // 故障
//			case -1:
			default:
				return null;
		}
	}

	/**
	 * 获取单元格的值
	 *
	 * @param cell        单元格
	 * @param cellMapping 列映射
	 * @return cellValue 单元格的值
	 */
	public static Object getCellValue(Cell cell, ExcelCellMapping cellMapping) {
		// 当属性类型为日期时，从cell中直接获取日期值
		try {
			if (cellMapping.getField().getType().equals(Date.class)) {
				return cell.getDateCellValue();
			}
		} catch (Exception ignore) {
		}

		Object value = getCellValue(cell);
		if (value != null) {
			// 如果配置了convert，则根据convert进行值的转换
			if (cellMapping.getConvertMap2() != null) {
				String convertValue = cellMapping.getConvertMap2().get(value.toString().trim());
				if (StringUtils.isNotBlank(convertValue)) {
					value = convertValue;
				}
			}
			// boolean类型特殊处理
			else if (cellMapping.getField().getType().equals(Boolean.class)) {
				String strValue = value.toString();
				if (strValue.equals(cellMapping.getTrueText()) || "Y".equals(strValue)) {
					return true;
				} else if (strValue.equals(cellMapping.getFalseText()) || "N".equals(strValue)) {
					return false;
				}
			}
		}

		// 转换类型
		return ConvertUtils.convert(value, cellMapping.getField().getType());
	}

	/**
	 * 获取单元格的值
	 *
	 * @param cell        单元格
	 * @param targetClass 目标类型
	 * @return cellValue 单元格的值
	 */
	public static <T> T getCellValue(Cell cell, Class<T> targetClass) {
		// 当属性类型为日期时，从cell中直接获取日期值
		if (targetClass.equals(Date.class)) {
			try {
				return (T)cell.getDateCellValue();
			} catch (RuntimeException ignore) {
			}
		}

		Object value = getCellValue(cell);
		return ConvertUtils.convert(value, targetClass);
	}

	/**
	 * 设置单元格的值
	 *
	 * @param cell        单元格
	 * @param data        数据
	 * @param cellMapping 列映射
	 * @throws NoSuchFieldException 字段未找到的异常
	 */
	public static void setCellValue(Cell cell, Object data, ExcelCellMapping cellMapping) throws NoSuchFieldException {
		// 获取字段值
		Object fieldValue = (StringUtils.isEmpty(cellMapping.getColumn())
				? ReflectionUtils.getFieldValue(data, cellMapping.getField())
				: ReflectionUtils.getFieldValue(data, cellMapping.getColumn()));
		if (fieldValue == null) {
			return; // 字段为空
		}

		// 如果存在值转换的map，即注解中配置了convert属性，则根据值进行值的转换
		if (cellMapping.getConvertMap() != null) {
			String fieldValue2 = cellMapping.getConvertMap().get(fieldValue.toString().trim());
			if (fieldValue2 != null) {
				cell.setCellValue(fieldValue2);
				return;
			}
		}

		// 设置字段值到单元格中
		if (fieldValue instanceof Date) {
			cell.setCellValue((Date)fieldValue);
		} else if (fieldValue instanceof Double) {
			cell.setCellValue((Double)fieldValue);
		} else if (fieldValue instanceof Float) {
			cell.setCellValue((Float)fieldValue);
		} else if (fieldValue instanceof BigDecimal) {
			cell.setCellValue(((BigDecimal)fieldValue).doubleValue());
		} else if (fieldValue instanceof Number) {
			cell.setCellValue(((Number)fieldValue).doubleValue());
		} else if (fieldValue instanceof Boolean) {
			if ((Boolean)fieldValue) {
				cell.setCellValue(cellMapping.getTrueText());
			} else {
				cell.setCellValue(cellMapping.getFalseText());
			}
		} else {
			String fieldValueStr = fieldValue.toString();
			if (fieldValueStr.contains("\r\n")) {
				cell.setCellValue(new HSSFRichTextString(fieldValueStr));
			} else {
				cell.setCellValue(fieldValueStr);
			}
		}
	}

	/**
	 * 设置列样式
	 *
	 * @param sheet             表格
	 * @param mapping           表格映射
	 * @param isBeforeWriteFile 是否在写文件前
	 */
	public static void setCellStyle(Sheet sheet, ExcelMapping mapping, boolean isBeforeWriteFile) {
		if (isBeforeWriteFile) { // 写文件前设置的样式
			int cellNum = 0; // 列号

			Workbook book = sheet.getWorkbook();

			// 设置默认列宽
			// 注：此方法不起作用，在下面循环列时，直接设置在列上
//			if (mapping.getDefaultWidth() > 0) {
//				sheet.setDefaultColumnWidth(mapping.getDefaultWidth() * 36);
//			}

			// 冻结窗口设置
			int colSplit = Math.max(mapping.getFreezeDataCells(), 0); // 冻结的列数
			int rowSplit = mapping.isNeedHeadRow() && mapping.isFreezeHeadRow() ? 1 : 0; // 冻结的行数
			if (colSplit > 0) {
				if (mapping.isNeedNumberCell()) {
					colSplit++;
				}
			} else {
				if (mapping.isNeedNumberCell() && mapping.isFreezeNumberCell()) {
					colSplit++;
				}
			}
			if (colSplit > 0 || rowSplit > 0) {
				sheet.createFreezePane(colSplit, rowSplit);
			}

			// 设置序号列样式
			if (mapping.isNeedNumberCell()) {
				CellStyle numberCellStyle = book.createCellStyle();
				numberCellStyle.setDataFormat(book.createDataFormat().getFormat("0_ ")); // 数字居右显示
				//numberCellStyle.setAlignment(HorizontalAlignment.CENTER); // 居中显示
				if (mapping.isNeedBorder()) {
					// 边框粗细
					numberCellStyle.setBorderTop(BorderStyle.THIN);
					numberCellStyle.setBorderRight(BorderStyle.THIN);
					numberCellStyle.setBorderBottom(BorderStyle.THIN);
					numberCellStyle.setBorderLeft(BorderStyle.THIN);
				}
				// 字体
				Font font = sheet.getWorkbook().createFont();
				font.setColor(HSSFColor.HSSFColorPredefined.TEAL.getIndex()); // 字体颜色：湖蓝色
				numberCellStyle.setFont(font);
				// 竖直居中
				numberCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 默认竖直居中
				// 设置样式
				sheet.setDefaultColumnStyle(cellNum, numberCellStyle);
				cellNum++;
			}

			// 设置数据列样式
			CellStyle cellStyle;
			for (ExcelCellMapping cellMapping : mapping.getCellMappingList()) {
				// 设置列宽
				if (cellMapping.getWidth() > 0) {
					sheet.setColumnWidth(cellNum, (int)(cellMapping.getWidth() * 36.1));
				} else if (mapping.getDefaultWidth() > 0) { // 注: 由于上面设置默认列宽不起作用，直接设置在列上
					sheet.setColumnWidth(cellNum, (int)(mapping.getDefaultWidth() * 36.1));
				}

				// 设置隐藏
				if (cellMapping.isHidden()) {
					sheet.setColumnHidden(cellNum, true);
				}

				// 创建样式
				cellStyle = book.createCellStyle();
				// 创建样式：字体
				if (StringUtils.isNotBlank(cellMapping.getColor())) {
					Font font = book.createFont();
					font.setColor(cellMapping.getColorIndex());
					cellStyle.setFont(font);
				}
				// 创建样式：格式化
				if (StringUtils.isNotBlank(cellMapping.getFormat())) {
					cellStyle.setDataFormat(book.createDataFormat().getFormat(cellMapping.getFormat()));
				}
				//// 创建样式：位置 ////
				// 创建样式：水平位置
				if (StringUtils.isNotBlank(cellMapping.getAlign())) {
					switch (cellMapping.getAlign()) {
						case "center":
							cellStyle.setAlignment(HorizontalAlignment.CENTER);
							break;
						case "left":
							cellStyle.setAlignment(HorizontalAlignment.LEFT);
							break;
						case "right":
							cellStyle.setAlignment(HorizontalAlignment.RIGHT);
							break;
						default:
							break;
					}
				}
				// 创建样式：竖直位置
				if (StringUtils.isNotBlank(cellMapping.getVerAlign())) {
					switch (cellMapping.getVerAlign()) {
						case "middle":
						case "center":
							cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 默认
							break;
						case "top":
							cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
							break;
						case "bottom":
							cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
							break;
						default:
							cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 默认
							break;
					}
				}
				// 是否允许自动换行
				cellStyle.setWrapText(cellMapping.isWrapText());
				// 创建样式：背景颜色
				if (StringUtils.isNotBlank(cellMapping.getBackgroundColor())) {
					cellStyle.setFillForegroundColor(cellMapping.getBackgroundColorIndex());
					cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				}
				// 创建样式：边框粗细
				if (mapping.isNeedBorder()) {
					cellStyle.setBorderTop(BorderStyle.THIN);
					cellStyle.setBorderRight(BorderStyle.THIN);
					cellStyle.setBorderBottom(BorderStyle.THIN);
					cellStyle.setBorderLeft(BorderStyle.THIN);
				}
				// 设置样式
				sheet.setDefaultColumnStyle(cellNum, cellStyle);

				cellNum++;
			}
		} else { // 写文件后设置的样式
			// 设置筛选功能
			if (mapping.isNeedHeadRow() && mapping.isNeedFilter()) {
				CellRangeAddress cra = new CellRangeAddress(0, 0, 0, sheet.getRow(0).getLastCellNum() - 1);
				sheet.setAutoFilter(cra);
			}

			// 列宽自适应，警告：此功能在数据较多的情况下非常消耗性能，请谨慎使用
			if (mapping.isWidthAutoSize()) {
				Row row0 = sheet.getRow(0);
				if (row0 != null) {
					for (int i = 0; i < row0.getLastCellNum(); i++) {
						sheet.autoSizeColumn(i, true);
					}
				}
			}
		}
	}
}
