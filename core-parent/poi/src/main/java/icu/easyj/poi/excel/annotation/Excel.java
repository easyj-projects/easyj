/*
 * Copyright 2021-2022 the original author or authors.
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
package icu.easyj.poi.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import icu.easyj.poi.excel.hook.IListToExcelHook;

/**
 * excel表格对应类的注解
 *
 * @author wangliang181230
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Excel {

	// 表名
	String sheetName() default "";

	// 是否需要边框
	boolean needBorder() default true;


	// 默认列宽
	int defaultWidth() default -1; // <=0时，表示采用excel默认的列宽

	// 列宽自适应
	// 注意：此功能对中文的支持不是很好。尽量在使用 @ExcelCell 注解时自定义width列宽。
	// 警告：此功能可能存在性能问题，数据较多时谨慎使用。
	boolean widthAutoSize() default false;


	// 是否需要头行
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
	ExcelCell[] cells() default {};

	/**
	 * 相同单元格合并定义
	 */
	String[] mergeSameCells() default {};

	/**
	 * 转换为Excel时的勾子列表
	 *
	 * @return hookClasses
	 */
	Class<? extends IListToExcelHook>[] toExcelHookClasses() default {};


	//region 自定义行

	ExcelCustomRowConfig customFirstRow() default @ExcelCustomRowConfig;

	boolean showFooterRow() default false;

	ExcelCustomRowConfig customFooterRow() default @ExcelCustomRowConfig;

	//endregion
}
