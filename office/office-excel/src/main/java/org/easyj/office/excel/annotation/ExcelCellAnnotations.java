package org.easyj.office.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel表格的一个对象属性对应多个列的注解
 *
 * @author wangliang181230
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelCellAnnotations {

	ExcelCellAnnotation[] value();

}