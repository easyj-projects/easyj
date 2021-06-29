package org.easyj.web.poi.excel;

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
public @interface ExcelExport {

	/**
	 * 导出文件名前缀，格式如：{fileNamePre}_{yyyyMMddHHmmssSSS}.xlsx
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
}
