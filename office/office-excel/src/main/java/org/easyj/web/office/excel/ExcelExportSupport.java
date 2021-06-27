package org.easyj.web.office.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 赋予普通接口Excel文件导出功能
 *
 * @author wangliang181230
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelExportSupport {

	/**
	 * 导出文件名前缀，格式如：{fileNamePre}_{yyyy-MM-dd_HH：mm：ss_SSS}.xlsx
	 *
	 * @return fileNamePre 文件名前缀
	 */
	String fileNamePre() default "";

	/**
	 * 数据类型
	 *
	 * @return dataType 数据类型
	 */
	Class<?> dataType();

//	/**
//	 * 切换为文件导出功能的依据参数的参数名
//	 *
//	 * @return paramName 参数名
//	 */
//	String paramName() default "doExcelExport";
//
//	/**
//	 * 切换为文件导出功能的依据参数的参数值
//	 *
//	 * @return paramValue 参数值
//	 */
//	String paramValue() default "true";
}
