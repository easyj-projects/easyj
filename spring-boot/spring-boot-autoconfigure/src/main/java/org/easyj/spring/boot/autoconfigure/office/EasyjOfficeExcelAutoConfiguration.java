package org.easyj.spring.boot.autoconfigure.office;

import org.apache.poi.ss.usermodel.Workbook;
import org.easyj.web.office.excel.ExcelExportAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * EasyJ-Office-Excel自动装配类
 *
 * @author wangliang@181230
 */
@ConditionalOnClass(Workbook.class) // 目前强依赖于Apache-POI，未来考虑做多种Excel解析的扩展支持
@EnableConfigurationProperties(EasyjOfficeExcelProperties.class)
public class EasyjOfficeExcelAutoConfiguration {

	@Bean
	public ExcelExportAspect excelExportAspect() {
		return new ExcelExportAspect();
	}
}
