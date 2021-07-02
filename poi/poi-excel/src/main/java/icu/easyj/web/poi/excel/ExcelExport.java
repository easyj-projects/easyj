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
package icu.easyj.web.poi.excel;

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
