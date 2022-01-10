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
package icu.easyj.poi.excel.util.hook;

import java.util.Map;

import icu.easyj.poi.excel.hook.AbstractListToExcelHook;

/**
 * 测试用的勾子类
 *
 * @author wangliang181230
 */
public class TestListToExcelHook extends AbstractListToExcelHook {

	@Override
	protected String generateCustomFirstRowContent(Map<Object, Object> context) {
		return "测试用的自定义首行内容";
	}
}
