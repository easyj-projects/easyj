package org.easyj.spring.boot.autoconfigure.office;

import org.apache.poi.ss.usermodel.Workbook;
import org.easyj.web.office.excel.DefaultExcelExporterImpl;
import org.easyj.web.office.excel.ExcelExportAspect;
import org.easyj.web.office.excel.IExcelExporter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * EasyJ-Office-Excel自动装配类
 *
 * @author wangliang@181230
 */
@ConditionalOnWebApplication
@EnableConfigurationProperties(EasyjOfficeExcelProperties.class)
public class EasyjOfficeExcelAutoConfiguration {

	/**
	 * 默认的 Excel导出器 实现
	 *
	 * @return excelExport Excel导出器
	 */
	@Bean
	@ConditionalOnClass(Workbook.class) // 默认的实现强依赖于Apache-POI，未来考虑做多种Excel解析的扩展支持
	@ConditionalOnMissingBean
	public IExcelExporter defaultExcelExporter() {
		return new DefaultExcelExporterImpl();
	}

	/**
	 * Excel导出切面Bean
	 *
	 * @param excelExporter Excel导出器
	 * @return excelExportAspect Excel导出切面
	 */
	@Bean
	public ExcelExportAspect excelExportAspect(IExcelExporter excelExporter) {
		return new ExcelExportAspect(excelExporter);
	}
}
