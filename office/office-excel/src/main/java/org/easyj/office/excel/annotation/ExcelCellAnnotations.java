package org.easyj.office.excel.annotation;

/**
 * excel表格的一个对象属性对应多个列的注解
 *
 * @author wangliang181230
 */
public @interface ExcelCellAnnotations {

	ExcelCellAnnotation[] value();

}
