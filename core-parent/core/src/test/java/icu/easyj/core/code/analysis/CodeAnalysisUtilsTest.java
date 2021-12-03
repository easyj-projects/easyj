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
package icu.easyj.core.code.analysis;

import icu.easyj.core.util.StringUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * {@link CodeAnalysisUtils} 测试类
 *
 * @author wangliang181230
 */
public class CodeAnalysisUtilsTest {

	@Test
	public void testAnalysisCode() {
		String expectedPrefix = "CodeAnalysisResult(variableName=\"encrypt\", fieldName=null, methodName=\"dec\", ";

		String code = "encrypt.dec(0, 1l, 2L, 3.1f, 3.2F, 4.1d, 4.2D, 4.3, '', \"\", ' ', \" \"" +
				", true, false, null, 'sadf', \" asdfasdf0u123 \", 'asdf\\'asdf', \"adsf\\\"fdsa124\");";

		//case: 取0个参数
		CodeAnalysisResult result = CodeAnalysisUtils.analysisCode(code, 0, true);
		Assertions.assertEquals(expectedPrefix + "parameters=[])",
				StringUtils.toString(result));

		//case: 取3个参数
		result = CodeAnalysisUtils.analysisCode(code, 3, true);
		Assertions.assertEquals(expectedPrefix + "parameters=[0, 1L, 2L])",
				StringUtils.toString(result));

		//case: 取所有参数
		result = CodeAnalysisUtils.analysisCode(code, true);
		Assertions.assertEquals(expectedPrefix + "parameters=[0, 1L, 2L, 3.1, 3.2, 4.1, 4.2, 4.3, \"\", \"\", \" \", \" \"" +
						", true, false, null, \"sadf\", \" asdfasdf0u123 \", \"asdf'asdf\", \"adsf\"fdsa124\"])",
				StringUtils.toString(result));
	}
}
