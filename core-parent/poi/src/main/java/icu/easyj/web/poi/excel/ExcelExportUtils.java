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
package icu.easyj.web.poi.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletResponse;

import icu.easyj.web.constant.ContentTypeConstants;
import icu.easyj.web.util.HttpUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;

/**
 * Excel导出工具类
 *
 * @author wangliang181230
 */
public abstract class ExcelExportUtils {

	/**
	 * 导出Excel文件
	 *
	 * @param response 响应对象
	 * @param book     the workbook
	 * @param fileName the file name
	 * @throws IOException the IO exception
	 */
	public static void exportExcel(HttpServletResponse response, Workbook book, String fileName) throws IOException {
		// 设置响应头信息
		setExcelExportResponse(response, fileName);

		// 设置文件流到输出流中
		book.write(response.getOutputStream());

		// 输出响应流
		response.getOutputStream().flush();
		// 关闭响应流
		response.getOutputStream().close();
	}

	/**
	 * 设置导出Excel文件所需的响应信息
	 *
	 * @param fileName 文件名
	 * @param response 响应对象
	 * @throws IOException IO异常
	 */
	public static void setExcelExportResponse(HttpServletResponse response, String fileName) throws IOException {
		String fileNameForHeader = new String(fileName.getBytes("gb2312"), "ISO8859-1");

		// 设置响应内容配置：设为文件并设置文件名
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileNameForHeader + "\"");  // 设置文件名
		// 设置响应内容类型：设置为微软excel文件
		response.setContentType(ContentTypeConstants.MS_EXCEL);
		// 字符编码
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());

		// 设置响应头，使该请求不被客户端缓存
		HttpUtils.setResponseNotAllowCache(response);
	}

	/**
	 * 将excel保存到文件中
	 *
	 * @param book           excel对象
	 * @param targetFilePath 目标文件路径
	 * @throws IOException IO异常
	 */
	public static void saveToFile(Workbook book, String targetFilePath) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(targetFilePath)) {
			book.write(fos);
		}
	}
}
