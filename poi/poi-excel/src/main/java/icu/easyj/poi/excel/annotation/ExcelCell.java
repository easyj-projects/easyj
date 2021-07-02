/*
 *  Copyright 2021 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package icu.easyj.poi.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel表格的列对应属性的注解
 *
 * @author wangliang181230
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelCell {

	// 列头
	String headName();

	// 列头注释
	String headComment() default "";

	// 字段名（当要展示对象里的数据时，可以使用此属性来配置属性的位置，如：obj.field1.field2）
	String column() default "";

	// 列号（值从0开始，除了序号列外）
	int cellNum();

	// boolean型数据为true时，显示的文字
	String trueText() default "Y";

	// boolean型数据为false时，显示的文字
	String falseText() default "N";

	// 值转换：一般用于枚举值转换为对应文字时使用，格式如：0=暂存,1=已上报
	String convert() default "";


	// 格式化（应用于Date数据、浮点数据等等的格式化输出）
	String format() default "";


	// 显示效果相关属性

	// 水平位置：left、center、right
	String align() default "";

	// 竖直位置：top、middle|center、bottom
	String verAlign() default "middle";

	// 列宽
	int width() default -1;

	// 是否允许自动换行
	boolean wrapText() default false;

	// 字体颜色
	String color() default ""; // 默认：黑色

	// 背景颜色
	String backgroundColor() default ""; // 默认：白色

	// 是否隐藏列
	boolean hidden() default false;
}
