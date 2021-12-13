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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
		Sheet sheet = book.getSheetAt(0);

		// 获取映射
		ExcelMapping mapping = ExcelMapping.getMapping(clazz);

		// 获取数据实际的起始行号
		int rowStart = sheet.getFirstRowNum();
		int rowEnd = sheet.getLastRowNum();
		while (ExcelRowUtils.isEmptyRow(sheet.getRow(rowStart))) {
			rowStart++; // 过滤起始的空行
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
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
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
		// 只检测5行
		int i = 0;
		while (i < 5) {
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

	/**
	 * 数据转换为excel
	 *
	 * @param dataList 数据列表
	 * @param clazz    数据类
	 * @param <T>      数据类型
	 * @return wbk 返回excel文件流
	 */
	public static <T> Workbook toExcel(List<T> dataList, Class<T> clazz) {
		if (clazz == null) {
			if (CollectionUtils.isEmpty(dataList)) {
				throw new RuntimeException("数据为空且类型未知，无法转换为excel文件");
			}
			clazz = (Class<T>)dataList.get(0).getClass();
		}

		ExcelMapping mapping = ExcelMapping.getMapping(clazz);
		if (CollectionUtils.isEmpty(mapping.getCellMappingList())) {
			throw new RuntimeException("“" + clazz.getName() + "” 类中未使用@" + ExcelCell.class.getSimpleName() + "配置任何列信息");
		}

		Workbook book = null;
		try {
			// 将列表数据设置到上下文中
			ExcelContext.put("dataList", dataList);

			// 创建工作簿
			book = new HSSFWorkbook();
			// 创建表
			Sheet sheet;
			if (StringUtils.isBlank(mapping.getSheetName())) {
				sheet = book.createSheet();
			} else {
				sheet = book.createSheet(mapping.getSheetName());
			}
			// 写文件前，设置样式
			ExcelCellUtils.setCellStyle(sheet, mapping, -1, true);

			// 触发勾子：beforeCreateHeadRow
			ListToExcelHookTrigger.onBeforeCreateHeadRow(sheet, mapping);

			// 除去自定义行以外的首行
			int firstRowNum = sheet.getLastRowNum() + 1;
			// 创建头行
			ExcelRowUtils.createHeadRow(sheet, mapping);
			// 创建数据行
			ExcelRowUtils.createDataRows(sheet, dataList, mapping);

			// 触发勾子：afterCreateDataRows
			ListToExcelHookTrigger.onAfterCreateDataRows(sheet, mapping);

			// 写文件后，设置样式：如自适应宽度等
			ExcelCellUtils.setCellStyle(sheet, mapping, firstRowNum, false);
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

	//endregion
}
