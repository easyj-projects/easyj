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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import icu.easyj.core.util.CollectionUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.poi.excel.annotation.ExcelCell;
import icu.easyj.poi.excel.hook.ListToExcelHookTrigger;
import icu.easyj.poi.excel.model.ExcelMapping;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

/**
 * Excel 工具类
 *
 * @author wangliang181230
 */
public abstract class ExcelUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

	//region excel转换为数据

	/**
	 * 加载Excel中的数据到List中
	 *
	 * @param book         Excel
	 * @param clazz        Excel文件映射类的信息
	 * @param validDataFun 验证数据有效性的Predicate函数，如果验证结果为false，则不读取该行数据到List中。
	 * @param <T>          泛型参数，即Excel文件映射的类型
	 * @return 返回映射类的集合
	 * @throws Exception 异常
	 */
	public static <T> List<T> toList(Workbook book, Class<T> clazz, Predicate<T> validDataFun) throws Exception {
		Sheet sheet = getHasDataSheet(book);
		if (sheet == null) {
			return new ArrayList<>(); // 没有数据，直接返回空
		}

		// 获取映射
		ExcelMapping mapping = ExcelMapping.getMapping(clazz);

		// 获取数据实际的起止行号
		int rowStart = sheet.getFirstRowNum();
		int rowEnd = sheet.getPhysicalNumberOfRows() - 1;
		while (ExcelRowUtils.isEmptyRow(sheet.getRow(rowStart))) {
			rowStart++; // 过滤起始的空行
			if (rowStart > rowEnd) {
				return new ArrayList<>(); // 没有数据了
			}
		}
		if (rowStart > rowEnd) {
			return new ArrayList<>(); // 没有数据了
		}
		if (rowStart < 0) {
			return new ArrayList<>(); // 没有数据，返回一个空
		}

		// 自动检索头行号
		Integer headRowNum = findHeadRowNum(sheet, rowStart, mapping);
		if (headRowNum != null) {
			rowStart = headRowNum + 1;
			if (rowStart > rowEnd) {
				return new ArrayList<>(); // 没有数据了
			}
		}

		// 是否含有序号列
		boolean hasNumberCell = getHasNumberCell(sheet, mapping);

		// 开始读取数据
		List<T> result = new ArrayList<>(); // 需要返回的数据列表
		Row row;
		Row headRow = (headRowNum != null ? sheet.getRow(headRowNum) : null);
		T t;
		for (int i = rowStart; i <= rowEnd; i++) {
			// 读取行
			row = sheet.getRow(i);
			if (ExcelRowUtils.isEmptyRow(row)) {
				continue; // 空行不读取
			}

			// 判断是否为合并单元格的特殊
			if (row.getLastCellNum() == 1 && mapping.getCellMappingList().size() > (mapping.isNeedNumberCell() ? 1 : 2)) {
				continue;
			}

			// 行转换为对象
			t = ExcelRowUtils.rowToObject(row, hasNumberCell, headRow, clazz, mapping);

			// 如果有数据有效性验证方法，则验证对象是否有效
			if (validDataFun == null || validDataFun.test(t)) {
				result.add(t);
			}
		}

		// 返回结果
		return result;
	}

	/**
	 * 加载Excel中的数据到List中
	 *
	 * @param is           Excel文件流
	 * @param clazz        Excel文件映射类的信息
	 * @param validDataFun 验证数据有效性的Predicate函数，如果验证结果为false，则不读取该行数据到List中。
	 * @param <T>          泛型参数，即Excel文件映射的类型
	 * @return 返回映射类的集合
	 * @throws Exception 异常
	 */
	public static <T> List<T> toList(InputStream is, Class<T> clazz, Predicate<T> validDataFun) throws Exception {
		try (Workbook book = WorkbookFactory.create(is)) {
			return toList(book, clazz, validDataFun);
		}
	}

	/**
	 * 加载Excel中的数据到List中
	 *
	 * @param filePath     Excel文件路径
	 * @param clazz        Excel文件映射类的信息
	 * @param validDataFun 验证数据有效性的Predicate函数，如果验证结果为false，则不读取该行数据到List中。
	 * @param <T>          泛型参数，即Excel文件映射的类型
	 * @return 返回映射类的集合
	 * @throws Exception 异常
	 */
	public static <T> List<T> toList(String filePath, Class<T> clazz, Predicate<T> validDataFun) throws Exception {
		try (InputStream is = new FileInputStream(filePath)) {
			return toList(is, clazz, validDataFun);
		}
	}

	// 重载方法
	public static <T> List<T> toList(Workbook book, Class<T> clazz) throws Exception {
		return toList(book, clazz, null);
	}

	// 重载方法
	public static <T> List<T> toList(InputStream is, Class<T> clazz) throws Exception {
		return toList(is, clazz, null);
	}

	// 重载方法
	public static <T> List<T> toList(String filePath, Class<T> clazz) throws Exception {
		return toList(filePath, clazz, null);
	}

	/**
	 * 获取Excel中实际的数据起始列号
	 *
	 * @param sheet   表格
	 * @param mapping 表格映射
	 * @return boolean 是否有序号列
	 */
	public static boolean getHasNumberCell(Sheet sheet, ExcelMapping mapping) {
		Row row;
		Cell cell;
		Object cellValue;
		for (int i = 0; i <= sheet.getPhysicalNumberOfRows() - 1; i++) {
			row = sheet.getRow(i);
			if (ExcelRowUtils.isEmptyRow(row)) {
				continue;
			}
			for (int j = 0; j < row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				cellValue = ExcelCellUtils.getCellValue(cell);
				if (cellValue == null) {
					continue;
				}
				if (cellValue.equals(mapping.getNumberCellHeadName()) || "序号".equals(cellValue)) {
					return true;
				}
			}
			break;
		}
		return false;
	}

	/**
	 * 获取头行号
	 *
	 * @param sheet       表格
	 * @param firstRowNum 起始行号
	 * @param mapping     表格映射
	 * @return 头行号
	 */
	@Nullable
	private static Integer findHeadRowNum(Sheet sheet, int firstRowNum, ExcelMapping mapping) {
		// 只检测前3行
		int i = 0;
		while (i < 3) {
			Row row = sheet.getRow(firstRowNum + i);
			if (row != null && ExcelRowUtils.isHeadRow(row, mapping)) {
				return row.getRowNum();
			}

			i++;
		}
		return null;
	}

	//endregion


	//region 数据转换为excel

	private static Sheet generateSheet(Workbook book, List<?> dataList, ExcelMapping mapping, String sheetName) {
		// 将列表数据设置到上下文中
		ExcelContext.put("dataList", dataList);

		// 创建表
		Sheet sheet;
		if (StringUtils.isBlank(sheetName)) {
			sheet = book.createSheet();
		} else {
			sheet = book.createSheet(sheetName);
		}
		// 写文件前，设置样式
		ExcelCellUtils.setCellStyle(sheet, mapping, -1, true);

		// 触发勾子：beforeCreateHeadRow
		ListToExcelHookTrigger.onBeforeCreateHeadRow(sheet, mapping);

		// 除去自定义行以外的首行
		int firstRowNum = sheet.getPhysicalNumberOfRows();
		// 创建头行
		ExcelRowUtils.createHeadRow(sheet, mapping);
		// 创建数据行
		if (CollectionUtils.isNotEmpty(dataList)) {
			// 创建数据行
			ExcelRowUtils.createDataRows(sheet, dataList, mapping);
			// 合并单元格 @since 0.7.8
			ExcelRowUtils.mergeSameCells(sheet, mapping);
		}

		// 触发勾子：afterCreateDataRows
		ListToExcelHookTrigger.onAfterCreateDataRows(sheet, mapping);

		// 表格内容填充完后，设置样式：如自适应宽度等
		ExcelCellUtils.setCellStyle(sheet, mapping, firstRowNum, false);

		return sheet;
	}


	/**
	 * 数据转换为excel
	 *
	 * @param dataList 数据列表
	 * @param clazz    数据类
	 * @return wbk 返回excel文件流
	 */
	public static Workbook toExcel(List<?> dataList, Class<?> clazz) {
		if (clazz == null) {
			if (CollectionUtils.isEmpty(dataList)) {
				throw new RuntimeException("数据为空且类型未知，无法转换为excel文件");
			}
			clazz = dataList.get(0).getClass();
		}

		ExcelMapping mapping = ExcelMapping.getMapping(clazz);
		if (CollectionUtils.isEmpty(mapping.getCellMappingList())) {
			throw new RuntimeException("“" + clazz.getName() + "” 类中未使用@" + ExcelCell.class.getSimpleName() + "配置任何列信息");
		}

		Workbook book = null;
		try {
			// 创建工作簿
			book = new HSSFWorkbook();

			// 生成表格
			generateSheet(book, dataList, mapping, mapping.getSheetName());
		} catch (Exception e) {
			try {
				if (book != null) {
					book.close();
				}
			} catch (IOException ignore) {
			}
			LOGGER.error("数据转换为excel失败：" + e.getMessage(), e);
			throw new RuntimeException("数据转换为excel失败", e);
		} finally {
			ExcelContext.remove("dataList");
		}

		return book;
	}

	/**
	 * 将数据转换为excel并保存到文件中
	 *
	 * @param dataList 数据列表
	 * @param clazz    数据类
	 * @param filePath 文件地址
	 * @param <T>      数据类型
	 * @throws IOException IO异常
	 */
	public static <T> void saveToExcelFile(List<T> dataList, Class<T> clazz, String filePath) throws IOException {
		try (Workbook book = toExcel(dataList, clazz)) {
			// 将excel工作薄对象保存到文件中
			File file = new File(filePath);

			try (FileOutputStream fos = new FileOutputStream(file)) {
				book.write(fos);
				fos.flush();
			}
		}
	}

	@Nullable
	public static Class<?> getClassFromMap(Map<String, List<?>> dataMap) {
		if (!dataMap.isEmpty()) {
			for (List<?> list : dataMap.values()) {
				if (!list.isEmpty()) {
					return list.get(0).getClass();
				}
			}
		}
		return null;
	}

	/**
	 * 数据转换为excel
	 *
	 * @param dataMap 数据列表
	 * @param clazz   数据类
	 * @return wbk 返回excel文件流
	 */
	public static Workbook toExcel(Map<String, List<?>> dataMap, Class<?> clazz) {
		if (clazz == null) {
			if (CollectionUtils.isEmpty(dataMap)) {
				throw new RuntimeException("数据为空且类型未知，无法转换为excel文件");
			}
			clazz = getClassFromMap(dataMap);
			if (clazz == null) {
				throw new RuntimeException("数据为空且类型未知，无法转换为excel文件");
			}
		}

		ExcelMapping mapping = ExcelMapping.getMapping(clazz);
		if (CollectionUtils.isEmpty(mapping.getCellMappingList())) {
			throw new RuntimeException("“" + clazz.getName() + "” 类中未使用@" + ExcelCell.class.getSimpleName() + "配置任何列信息");
		}

		Workbook book = null;
		try {
			// 创建工作簿
			book = new HSSFWorkbook();

			for (Map.Entry<String, List<?>> entry : dataMap.entrySet()) {
				String sheetName = entry.getKey();
				List<?> dataList = entry.getValue() == null ? Collections.emptyList() : entry.getValue();

				// 生成sheet
				generateSheet(book, dataList, mapping, sheetName);
			}
		} catch (Exception e) {
			try {
				if (book != null) {
					book.close();
				}
			} catch (IOException ignore) {
			}
			LOGGER.error("数据转换为excel失败：{}", e.getMessage(), e);
			throw new RuntimeException("数据转换为excel失败", e);
		} finally {
			ExcelContext.remove("dataList");
		}

		return book;
	}

	//endregion


	/**
	 * 获取有数据的Sheet
	 *
	 * @param book 工作簿
	 * @return 有数据的Sheet
	 */
	@Nullable
	public static Sheet getHasDataSheet(Workbook book) {
		if (book == null) {
			return null;
		}

		for (int i = 0; i < book.getNumberOfSheets(); i++) {
			Sheet sheet = book.getSheetAt(i);
			if (sheet.getPhysicalNumberOfRows() > 0) {
				return sheet;
			}
		}

		return null;
	}
}
