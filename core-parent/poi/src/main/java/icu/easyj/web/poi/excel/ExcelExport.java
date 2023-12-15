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
package icu.easyj.web.poi.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 赋予 列表数据查询的GET接口，导出为Excel文件的能力
 *
 * @author wangliang181230
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelExport {

	/**
	 * 导出的Excel文件的文件名前缀<br>
	 * 最终生成的文件名格式如：{fileNamePre}_{yyyyMMddHHmmssSSS}.xlsx
	 *
	 * @return fileNamePre 文件名前缀
	 */
	String fileNamePre();

	/**
	 * 数据类型
	 *
	 * @return dataType 数据类型
	 */
	Class<?> dataType() default Object.class;

	/**
	 * 数据类型解析器
	 *
	 * @return dataTypeParser 数据类型解析器
	 */
	Class<? extends DataTypeParser> dataTypeParser() default NonDataTypeParser.class;

	/**
	 * 列表属性在返回数据中的属性名。
	 * <p>
	 * 说明：当方法返回的数据不是`List`，而是一个分页包装对象时，需要设置该参数。<br>
	 * 也可以通过{@link ExcelExportAspect}的构造函数注入{@link ExcelExportConfig}配置参数，来全局配置`listFieldName`。
	 *
	 * @return listFieldName 列表属性名
	 * @see ExcelExportConfig
	 */
	String listFieldName() default "";

	/**
	 * 是否直接导出Excel文件，而不再根据GET参数 `doExport=true` 来判断是否导出。<br>
	 * 当设置为 `true` 时，这个接口就是一个导出Excel文件的接口，而不再是一个列表数据查询的接口。
	 *
	 * @since 0.7.6
	 */
	boolean doExportDirect() default false;
}
