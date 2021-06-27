package org.easyj.office.excel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.easyj.office.excel.annotation.ExcelCellAnnotation;
import org.easyj.office.excel.model.ExcelMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

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
	 * @param hasHeadRow   Excel中是否含的头行，如果为true，则跳过头行，从第二行开始读取数据行。（注：如果此参数传入null，则系统会自动根据映射类的属性注解中的头信息去验证Excel第一行是否为头行。
	 * @param <T>          泛型参数，即Excel文件映射的类
	 * @return 返回映射类的集合
	 * @throws Exception 异常
	 */
	public static <T extends Object> List<T> toList(Workbook book, Class<T> clazz, Predicate<T> validDataFun, Boolean hasHeadRow) throws Exception {
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

		// 如果hasHeadRow为空，则自动判断数据中的第一行是否为头行
		if (hasHeadRow == null) { // 如果是否含有行号参数为空，则自动根据excel的首行内容来判断首行是否为头行
			hasHeadRow = hasHeadRow(sheet, rowStart, mapping);
		}
		if (hasHeadRow) {
			rowStart++; // 有头行，则数据读取行号加1
			if (rowStart > rowEnd) {
				return new ArrayList<>(); // 没有数据了
			}
		}
		if (rowStart < 0) {
			return new ArrayList<>(); // 没有数据，返回一个空
		}

		// 是否含有序号列
		boolean hasNumberCell = getHasNumberCell(sheet, mapping);

		// 开始读取数据
		List<T> result = new ArrayList<>(); // 需要返回的数据列表
		Row row;
		Row headRow = (hasHeadRow ? sheet.getRow(rowStart - 1) : null);
		T t;
		for (int i = rowStart; i <= rowEnd; i++) {
			// 读取行
			row = sheet.getRow(i);
			if (ExcelRowUtils.isEmptyRow(row)) {
				continue; // 空行不读取
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
	 * @param hasHeadRow   Excel中是否含的头行，如果为true，则跳过头行，从第二行开始读取数据行。（注：如果此参数传入null，则系统会自动根据映射类的属性注解中的头信息去验证Excel第一行是否为头行。
	 * @param <T>          泛型参数，即Excel文件映射的类
	 * @return 返回映射类的集合
	 * @throws Exception 异常
	 */
	public static <T extends Object> List<T> toList(InputStream is, Class<T> clazz, Predicate<T> validDataFun, Boolean hasHeadRow) throws Exception {
		try (Workbook book = WorkbookFactory.create(is)) {
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

			// 如果hasHeadRow为空，则自动判断数据中的第一行是否为头行
			if (hasHeadRow == null) { // 如果是否含有行号参数为空，则自动根据excel的首行内容来判断首行是否为头行
				hasHeadRow = hasHeadRow(sheet, rowStart, mapping);
			}
			if (hasHeadRow) {
				rowStart++; // 有头行，则数据读取行号加1
				if (rowStart > rowEnd) {
					return new ArrayList<>(); // 没有数据了
				}
			}
			if (rowStart < 0) {
				return new ArrayList<>(); // 没有数据，返回一个空
			}

			// 是否含有序号列
			boolean hasNumberCell = getHasNumberCell(sheet, mapping);

			// 开始读取数据
			List<T> result = new ArrayList<>(); // 需要返回的数据列表
			Row row;
			Row headRow = (hasHeadRow ? sheet.getRow(rowStart - 1) : null);
			T t;
			for (int i = rowStart; i <= rowEnd; i++) {
				// 读取行
				row = sheet.getRow(i);
				if (ExcelRowUtils.isEmptyRow(row)) {
					continue; // 空行不读取
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
	}

	// 重载方法
	public static <T extends Object> List<T> toList(InputStream is, Class<T> clazz) throws Exception {
		return toList(is, clazz, null, null);
	}

	// 重载方法
	public static <T extends Object> List<T> toList(InputStream is, Class<T> clazz, Predicate<T> validDataFun) throws Exception {
		return toList(is, clazz, validDataFun, null);
	}

	// 重载方法
	public static <T extends Object> List<T> toList(InputStream is, Class<T> clazz, Boolean hasHeadRow) throws Exception {
		return toList(is, clazz, null, hasHeadRow);
	}

	// 重载方法
	public static <T extends Object> List<T> toList(String filePath, Class<T> clazz, Predicate<T> validDataFun, Boolean hasHeadRow) throws Exception {
		try (InputStream is = new FileInputStream(filePath)) {
			return toList(is, clazz, validDataFun, hasHeadRow);
		}
	}

	// 重载方法
	public static <T extends Object> List<T> toList(String filePath, Class<T> clazz) throws Exception {
		return toList(filePath, clazz, null, null);
	}

	// 重载方法
	public static <T extends Object> List<T> toList(String filePath, Class<T> clazz, Predicate<T> validDataFun) throws Exception {
		return toList(filePath, clazz, validDataFun, null);
	}

	// 重载方法
	public static <T extends Object> List<T> toList(String filePath, Class<T> clazz, Boolean hasHeadRow) throws Exception {
		return toList(filePath, clazz, null, hasHeadRow);
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
			if (ExcelRowUtils.isEmptyRow(row)) continue;
			for (int j = 0; j < row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				cellValue = ExcelCellUtils.getCellValue(cell);
				if (cellValue == null) continue;
				if (cellValue.equals(mapping.getNumberCellHeadName()) || cellValue.equals("序号")) {
					return true;
				}
			}
			break;
		}
		return false;
	}

	/**
	 * 判断第一行是否为头行（Head）
	 *
	 * @param sheet
	 * @param mapping
	 * @return
	 */
	private static boolean hasHeadRow(Sheet sheet, int firstRowNum, ExcelMapping mapping) {
		Row row = sheet.getRow(firstRowNum);
		if (row == null) return false;

		return ExcelRowUtils.isHeadRow(row, mapping);
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
	public static <T extends Object> Workbook toExcel(List<T> dataList, Class<T> clazz) {
		if (clazz == null) {
			if (StringUtils.isEmpty(dataList)) {
				throw new RuntimeException("数据为空且类型未知，无法转换为excel文件");
			}
			clazz = (Class<T>)dataList.get(0).getClass();
		}

		ExcelMapping mapping = ExcelMapping.getMapping(clazz);
		if (StringUtils.isEmpty(mapping.getCellMappingList())) {
			throw new RuntimeException("“" + clazz.getName() + "” 类中未使用@" + ExcelCellAnnotation.class.getSimpleName() + "配置任何列信息");
		}

		Workbook book = null;
		try {
			// 创建工作簿
			book = new HSSFWorkbook();
			// 创建表
			Sheet sheet;
			if (StringUtils.isEmpty(mapping.getSheetName())) sheet = book.createSheet();
			else sheet = book.createSheet(mapping.getSheetName());
			// 写文件前，设置样式
			ExcelCellUtils.setCellStyle(sheet, mapping, true);
			// 创建头行
			ExcelRowUtils.createHeadRow(sheet, mapping);
			// 创建数据行
			ExcelRowUtils.createDataRows(sheet, dataList, mapping);
			// 写文件后，设置样式：如自适应宽度等
			ExcelCellUtils.setCellStyle(sheet, mapping, false);
		} catch (Exception e) {
			try {
				if (book != null) book.close();
			} catch (IOException ignore) {
			}
			LOGGER.error("数据转换为excel失败：" + e.getMessage(), e);
			throw new RuntimeException("数据转换为excel失败", e);
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
	public static <T extends Object> void saveToExcelFile(List<T> dataList, Class<T> clazz, String filePath) throws IOException {
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
