package icu.easyj.web.poi.excel;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * 默认的 Excel导出器 实现类
 *
 * @author wangliang181230
 */
public class DefaultExcelExporterImpl implements IExcelExporter {

	@Override
	public <T> void toExcelAndExport(HttpServletResponse response, List<T> dataList, Class<T> clazz, String fileName) throws IOException {
		ExcelExportUtils.toExcelAndExport(response, dataList, clazz, fileName);
	}
}
