/*
 * Copyright 2021-2024 the original author or authors.
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

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import icu.easyj.core.convert.ConvertUtils;
import icu.easyj.core.util.NumberUtils;
import icu.easyj.core.util.PatternUtils;
import icu.easyj.core.util.ReflectionUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.poi.excel.model.ExcelCellMapping;
import icu.easyj.poi.excel.model.ExcelMapping;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.lang.Nullable;

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

		// 获取CellType枚举
		CellType cellType = getCellType(cell);

		String str;
		switch (cellType) {
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
		// 获取CellType枚举
		CellType cellType = getCellType(cell);

		switch (cellType) {
			case STRING: // 字符串
				String str = cell.getStringCellValue().trim();

				// 如果第一个字符为 ` 或 '，且后面都是数字，说明前面这个是转义符，将其移除掉
				if (StringUtils.isNotEmpty(str) && PatternUtils.validate("[`']-?\\d+(\\.\\d*([eE]-?\\d+)?)?", str)) {
					str = str.substring(1);
				}

				return str;
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
		return convert(value, cellMapping.getField().getType());
	}

	public static <T> T convert(Object value, Class<T> targetClass) {
		if (value == null) {
			return null;
		}

		// Double 转 String 特殊处理：不使用科学记数法
		if (value instanceof Double && String.class.equals(targetClass)) {
			Double d = (Double) value;
			//noinspection unchecked
			return (T) NumberUtils.doubleToString(d);
		}

		return ConvertUtils.convert(value, targetClass);
	}

	/**
	 * 获取单元格的值
	 *
	 * @param cell        单元格
	 * @param targetClass 目标类
	 * @param <T>         目标类型
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
		} else if (fieldValue instanceof Number) {
			setCellNumberValue(cell, ((Number)fieldValue));
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

	private static void setCellNumberValue(Cell cell, Number numberValue) {
		double doubleValue = numberValue.doubleValue();
		if (doubleValue > Integer.MAX_VALUE || doubleValue < Integer.MIN_VALUE) {
			cell.setCellValue("'" + numberValue);
		} else {
			cell.setCellValue(doubleValue);
		}
	}

	/**
	 * 设置列样式
	 *
	 * @param sheet             表格
	 * @param mapping           表格映射
	 * @param headRowNum        头行号
	 * @param isBeforeWriteFile 是否在写文件前
	 */
	public static void setCellStyle(Sheet sheet, ExcelMapping mapping, int headRowNum, boolean isBeforeWriteFile) {
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
				font.setColor(ExcelColorUtils.TEAL_INDEX); // 字体颜色：湖蓝色
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

				//region 创建样式：位置
				// 创建样式：水平位置
				HorizontalAlignment align = convertAlign(cellMapping.getAlign(), null);
				if (align != null) {
					cellStyle.setAlignment(align);
				}
				// 创建样式：竖直位置
				VerticalAlignment verAlign = convertVerAlign(cellMapping.getVerAlign(), VerticalAlignment.CENTER);
				cellStyle.setVerticalAlignment(verAlign);
				//endregion

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
				CellRangeAddress cra = new CellRangeAddress(headRowNum, headRowNum, 0, sheet.getRow(headRowNum).getLastCellNum() - 1);
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

	public static HorizontalAlignment convertAlign(String align, @Nullable HorizontalAlignment defaultAlign) {
		if (align == null) {
			return defaultAlign;
		}
		switch (align.toLowerCase()) {
			case "center":
				return HorizontalAlignment.CENTER;
			case "left":
				return HorizontalAlignment.LEFT;
			case "right":
				return HorizontalAlignment.RIGHT;
			default:
				return defaultAlign;
		}
	}

	public static VerticalAlignment convertVerAlign(String verAlign, @Nullable VerticalAlignment defaultVerAlign) {
		if (verAlign == null) {
			return defaultVerAlign; // 默认
		}
		switch (verAlign.toLowerCase()) {
			case "middle":
			case "center":
				return VerticalAlignment.CENTER;
			case "top":
				return VerticalAlignment.TOP;
			case "bottom":
				return VerticalAlignment.BOTTOM;
			default:
				return defaultVerAlign; // 默认
		}
	}

	/**
	 * 兼容低版本POI
	 */
	public static CellType getCellType(Cell cell) {
		final CellType cellType;

		// 兼容性处理
		Object cellTypeValue;
		try {
			cellTypeValue = ReflectionUtils.invokeMethod(cell, "getCellType");
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("No such method: " + Cell.class.getName() + ".getCellType()", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Invoke method failed: " + Cell.class.getName() + ".getCellType()", e);
		}

		if (cellTypeValue instanceof CellType) {
			cellType = (CellType) cellTypeValue;
		} else {
			cellType = CellType.forInt(Integer.parseInt(cellTypeValue.toString()));
		}

		return cellType;
	}

	/**
	 * 兼容低版本POI
	 */
	public static HorizontalAlignment getCellStyleAlignment(CellStyle cellStyle) {
		final HorizontalAlignment alignment;

		// 兼容性处理
		Object alignmentValue;
		try {
			alignmentValue = ReflectionUtils.invokeMethod(cellStyle, "getAlignment");
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("No such method: " + CellStyle.class.getName() + ".getAlignment()", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Invoke method failed: " + CellStyle.class.getName() + ".getAlignment()", e);
		}

		if (alignmentValue instanceof HorizontalAlignment) {
			alignment = (HorizontalAlignment) alignmentValue;
		} else {
			alignment = HorizontalAlignment.forInt(Integer.parseInt(alignmentValue.toString()));
		}

		return alignment;
	}
}
