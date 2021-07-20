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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link CodeAnalysisUtils} 测试类
 *
 * @author wangliang181230
 */
class CodeAnalysisUtilsTest {

	@Test
	void testAnalysisCode() {
		String code = "encrypt.dec(11, 22L, 'asdf\\'asdf', \"adsf\\\"fdsa124\", true, false, null, 'sadf', 11l);";

		//case: 取0个参数
		CodeAnalysisResult result = CodeAnalysisUtils.analysisCode(code, 0, true);
		Assertions.assertEquals("CodeAnalysisResult(variableName=\"encrypt\", fieldName=null, methodName=\"dec\", parameters=null)",
				StringUtils.toString(result));

		//case: 取3个参数
		result = CodeAnalysisUtils.analysisCode(code, 3, true);
		Assertions.assertEquals("CodeAnalysisResult(variableName=\"encrypt\", fieldName=null, methodName=\"dec\", parameters=[11, 22L, \"asdf'asdf\"])",
				StringUtils.toString(result));

		//case: 取所有参数
		result = CodeAnalysisUtils.analysisCode(code, true);
		Assertions.assertEquals("CodeAnalysisResult(variableName=\"encrypt\", fieldName=null, methodName=\"dec\", parameters=[11, 22L, \"asdf'asdf\", \"adsf\"fdsa124\", true, false, null, \"sadf\", 11L])",
				StringUtils.toString(result));
	}
}
