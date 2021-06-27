package org.easyj.office.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel表格对应类的注解
 *
 * @author wangliang181230
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelAnnotation {

	// 表名
	String sheetName() default "";

	// 是否需要边框
	boolean needBorder() default true;


	// 默认列宽
	int defaultWidth() default -1; // <=0时，表示采用excel默认的列宽

	// 列宽自适应
	// 注意：此功能对中文的支持不是很好。尽量在使用 @ExcelCellAnnotation 注解时自定义width列宽。
	// 警告：此功能可能存在性能问题，数据较多时谨慎使用。
	boolean widthAutoSize() default false;


	// 是否需要头名称行
	boolean needHeadRow() default true;

	// 是否冻结头行
	boolean freezeHeadRow() default true; // 默认：true=冻结


	// 是否需要序号列
	boolean needNumberCell() default true;

	// 序号列的列头
	String numberCellHeadName() default "序号";

	// 是否冻结序号列
	boolean freezeNumberCell() default false; // 默认：false=不冻结


	// 冻结的数据列的数量，不包含序号列
	int freezeDataCells() default 0;

	// 是否需要列筛选功能
	boolean needFilter() default true; // 默认：true=需要

	// 配置列信息，主要用于获取基类中的属性
	ExcelCellAnnotation[] cells() default {};
}
