package org.easyj.office.excel.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel导出工具类
 *
 * @author wangliang181230
 */
public abstract class ExcelExportUtils {

	/**
	 * 数据转换为excel文件并下载
	 *
	 * @param response 响应对象
	 * @param dataList 数据列表
	 * @param clazz    数据类
	 * @param fileName 导出文件名
	 * @param <T>      数据类型'
	 * @throws IOException IO异常
	 */
	public static <T extends Object> void toExcelAndExport(HttpServletResponse response, List<T> dataList, Class<T> clazz, String fileName) throws IOException {
		// 数据转换为excel工作薄
		Workbook book = ExcelUtils.toExcel(dataList, clazz);
		// 设置响应头及响应流
		exportExcel(response, book, fileName);
	}

	/**
	 * 下载Excel文件
	 *
	 * @param response 响应对象
	 * @param book     the workbook
	 * @param fileName the file name
	 * @throws IOException the IO exception
	 */
	public static void exportExcel(HttpServletResponse response, Workbook book, String fileName) throws IOException {
		// 设置响应头信息
		setExcelHead(response, fileName);

		// 设置文件流到输出流中
		book.write(response.getOutputStream());

		// 关闭流
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

	/**
	 * 设置excel文件所需的响应头
	 *
	 * @param fileName 文件名
	 * @param response 响应对象
	 * @throws IOException IO异常
	 */
	public static void setExcelHead(HttpServletResponse response, String fileName) throws IOException {
		String filenameHead = new String(fileName.getBytes("gb2312"), "ISO8859-1");

		response.setHeader("Content-Disposition", "attachment; filename=\"" + filenameHead + "\"");  // 设置文件头编码格式
		response.setContentType("application/octet-stream; charset=UTF-8"); // 设置类型为流输出
		response.setHeader("Cache-Control", "no-cache");// 设置缓存头：无缓存
		response.setDateHeader("Expires", 0); // 设置时间头
	}

	/**
	 * 将excel保存到文件中
	 *
	 * @param book           excel对象
	 * @param targetFilePath 目标文件路径
	 * @throws IOException IO异常
	 */
	public static void saveToFile(Workbook book, String targetFilePath) throws IOException {
		FileOutputStream fos = new FileOutputStream(targetFilePath);
		book.write(fos);
	}
}
