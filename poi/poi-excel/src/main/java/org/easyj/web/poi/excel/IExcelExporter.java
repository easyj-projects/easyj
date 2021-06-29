package org.easyj.web.poi.excel;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * Excel导出器
 *
 * @author wangliang181230
 */
public interface IExcelExporter {

	/**
	 * 数据转换为excel文件并下载
	 *
	 * @param response 响应对象
	 * @param dataList 数据列表
	 * @param clazz    数据类
	 * @param fileName 导出文件名
	 * @param <T>      数据类型
	 * @throws IOException IO异常
	 */
	<T> void toExcelAndExport(HttpServletResponse response, List<T> dataList, Class<T> clazz, String fileName) throws IOException;
}
