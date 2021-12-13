/*
 * Copyright 2021 the original author or authors.
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

/**
 * 自定义首行配置注解
 *
 * @author wangliang181230
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelCustomFirstRowConfig {

	//region 常量

	int DEFAULT_FONT_SIZE = 14;

	boolean DEFAULT_FONT_BOLD = true;

	int DEFAULT_ROW_HEIGHT = 25;

	String DEFAULT_ALIGN = "center";

	String DEFAULT_VER_ALIGN = "middle";

	//endregion


	/**
	 * 字体大小
	 *
	 * @return the font size
	 */
	int fontSize() default DEFAULT_FONT_SIZE;

	/**
	 * 字体粗体
	 *
	 * @return the font bold
	 */
	boolean fontBold() default DEFAULT_FONT_BOLD;

	/**
	 * 行高
	 *
	 * @return the row height
	 */
	int rowHeight() default DEFAULT_ROW_HEIGHT;

	/**
	 * 水平位置
	 * 值域：left、center、right
	 *
	 * @return the cell alignment
	 */
	String align() default DEFAULT_ALIGN;

	/**
	 * 竖直位置
	 * 值域：top、middle|center、bottom
	 *
	 * @return the cell vertical alignment
	 */
	String verAlign() default DEFAULT_VER_ALIGN;
}
