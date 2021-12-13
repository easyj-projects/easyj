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

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.lang.NonNull;

/**
 * 代码解析结果
 *
 * @author wangliang181230
 */
public class CodeAnalysisResult {

	/**
	 * 变量名
	 */
	private String variableName;

	/**
	 * 属性名
	 */
	private String fieldName;

	/**
	 * 方法名
	 */
	private String methodName;

	/**
	 * 参数数组
	 */
	private Object[] parameters;

	/**
	 * 选取参数
	 *
	 * @param size 选取参数的数量
	 * @return newParameters
	 */
	@NonNull
	public Object[] chooseParameters(int size) {
		if (ArrayUtils.isEmpty(parameters)) {
			return ArrayUtils.EMPTY_OBJECT_ARRAY;
		}
		Object[] result = new Object[Math.max(size, this.parameters.length)];
		System.arraycopy(this.parameters, 0, result, 0, result.length);
		return result;
	}


	//region Getter、Setter

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	//endregion
}
